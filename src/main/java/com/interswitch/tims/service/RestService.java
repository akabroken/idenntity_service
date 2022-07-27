/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.*;
import com.interswitch.tims.exception.*;
import com.interswitch.tims.model.FuseModel;
import com.interswitch.tims.model.SystemExchange;
import com.interswitch.tims.util.*;
import static com.interswitch.tims.util.SystemUtil.marshallJson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.Payload;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.servlet.http.*;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ndegel
 */
@Service
public class RestService implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(RestService.class);

    @Autowired
    FuseService fuseService;

    @Autowired
    KeyService keyService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        preProcess(request);
        return true;
    }

    private void preProcess(HttpServletRequest request) throws Exception {
        SystemExchange systemExchange = getSystemExchange(request);
        systemExchange.setUrl(getURL(request));
        request.setAttribute(ConstantUtil.SYSTEM_EXCHANGE, systemExchange);

        if (!request.getServletPath().equals("/api/v1/service/status")) {
            FuseModel client = authenticateAuth(systemExchange, request);
            authenticateRequest(systemExchange, request, getURL(request), client);
        }
    }

    private SystemExchange getSystemExchange(HttpServletRequest request) throws Exception {
        SystemExchange systemExchange = new SystemExchange();

        String keyId = request.getHeader("keyId");
        if (keyId != null) {
            InputStream requestInputStream = request.getInputStream();
            String payload = StreamUtils.copyToString(requestInputStream, StandardCharsets.UTF_8);
            systemExchange.setRequest(payload);
            systemExchange.setKeyID(keyId);
            addMLEPayload(systemExchange);
        }

        systemExchange.setSessionKeyId(request.getHeader("sessionKeyId"));
        if (systemExchange.getSessionKeyId() != null) {
            setSessionKey(request, systemExchange);
        }

        return systemExchange;
    }

    private void setSessionKey(HttpServletRequest request, SystemExchange systemExchange) {
        systemExchange.setSessionKey(keyService.getSessionKey(systemExchange.getSessionKeyId()));
        if (systemExchange.getSessionKey() == null) {
            throw new InvalidPayloadException("Invalid session Key ID", request, systemExchange);
        }
    }

    private void addMLEPayload(SystemExchange systemExchange) throws IOException, Exception {
        systemExchange.setRequest(getDecryptedPayload(systemExchange.getRequest()));
    }

    public static String getURL(HttpServletRequest req) {

        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    private void authenticateRequest(SystemExchange systemExchange, 
            HttpServletRequest request, String url, FuseModel client) throws Exception {
        authenticateTimestamp(systemExchange, request);
        authenticateSignature(systemExchange, request, url, client);
    }

    protected FuseModel authenticateAuth(SystemExchange systemExchange, HttpServletRequest request) throws SystemException {

        String authorization = request.getHeader("authorization");

        if (authorization == null || authorization.isEmpty()) {
            throw new InvalidPayloadException("Missing authorization header", request, systemExchange);
        }

        if (authorization.split(" ").length != 2) {
            throw new InvalidPayloadException("Incorrect authorization header", request, systemExchange);
        }

        String authRealm = authorization.split(" ")[0];

        if (!authRealm.equals("InterswitchAuth")) {
            throw new InvalidPayloadException("Incorrect authorization realm", request, systemExchange);
        }
        String base64 = new String(Base64.decode(authorization.split(" ")[1].getBytes()));
        FuseModel client = fuseService.fetchClient(base64);
        if (client == null) {
            throw new InvalidPayloadException("Invalid client", request, systemExchange);
        }

        systemExchange.setClientID(base64);

        return client;
    }

    protected void authenticateTimestamp(SystemExchange systemExchange, HttpServletRequest request) throws SystemException {
        long allowedTimestampWindow = 900;
        String timestamp = request.getHeader("timestamp");
        if (timestamp == null || timestamp.isEmpty()) {
            throw new InvalidPayloadException("Missing timestamp header", request, systemExchange);
        }

        long currentTime = System.currentTimeMillis() / 1000L;
        long requestTime = Long.valueOf(timestamp);
        long timeDiff = Math.abs(currentTime - requestTime);
        if (timeDiff > allowedTimestampWindow) {
            throw new InvalidPayloadException("Invalid timestamp, please check date and time settings", request, systemExchange);
        }
    }

    protected void authenticateSignature(SystemExchange systemExchange, HttpServletRequest request, String url, FuseModel client) throws Exception {
        String clientSignature = request.getHeader("signature");
        if (clientSignature == null || clientSignature.isEmpty()) {
            throw new InvalidPayloadException("Missing signature header", request, systemExchange);
        }

        String signatureMethod = request.getHeader("signatureMethod");
        if (signatureMethod == null || signatureMethod.isEmpty()) {
            throw new InvalidPayloadException("Missing signature method header", request, systemExchange);
        }
        url = url.replace("http", "https");

        String secret = new String(CryptoUtil.decrypt3DES(Base64.decode(client.getClientSecret()), ConstantUtil.DES_ECD_PKCS5PADDING, "497f80e62513f4628f2a6d760464580de9ef0deacbc738b6"));

        String cipher = String.join("&", request.getMethod(), URLEncoder.encode(url), String.valueOf(request.getHeader("timestamp")), request.getHeader("nonce"), systemExchange.getClientID(), secret);
        logger.info("Signature Cipher " + cipher);
        MessageDigest messageDigest = MessageDigest.getInstance(signatureMethod);
        byte[] signBytes = messageDigest.digest(cipher.getBytes());
        String mobaSignature = new String(Base64.encode(signBytes));

        logger.info("Client Signature " + clientSignature);
        logger.info("Identity Signature " + mobaSignature);

        if (!mobaSignature.equals(clientSignature)) {
            throw new InvalidPayloadException("Invalid signature", request, systemExchange);
        }

    }

    public String getEncryptedPayload(String plainText, String keyId) throws Exception {

        JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);

        headerBuilder.keyID(keyId);
        headerBuilder.customParam("iat", System.currentTimeMillis());

        JWEObject jweObject = new JWEObject(headerBuilder.build(), new Payload(plainText));
        jweObject.encrypt(new RSAEncrypter(keyService.getMlePubKey(keyId + "_MLE_PUB_KEY")));
        return jweObject.serialize();
    }

    public String getDecryptedPayload(String payload) throws Exception {

        BaseModel encData = (BaseModel) SystemUtil.unmarshallJson(payload, BaseModel.class);
        JWEObject jweObject = JWEObject.parse(encData.getEncData());
        jweObject.decrypt(new RSADecrypter(keyService.getMlePrivateKey()));
        String response = jweObject.getPayload().toString();

        logger.info("Request = " + response);
        return response;
    }

    public SystemResponse setResponse(HttpServletRequest request, HttpServletResponse response, SystemExchange systemExchange, SystemResponse systemResponse) throws Exception {
        systemResponse.setMessage(systemResponse.getMessage() == null ? ResponseUtil.getMessage(systemResponse.getCode()) : systemResponse.getMessage());
        systemExchange.setResponse(marshallJson(systemResponse));
        response.setStatus(ResponseUtil.httpStatusCode(systemResponse.getCode()).value());
        request.setAttribute(ConstantUtil.SYSTEM_EXCHANGE, systemExchange);

        if (systemExchange.getKeyID() != null) {
            SystemResponse encResponse = new SystemResponse();
            encResponse.setEncData(getEncryptedPayload(SystemUtil.marshallJson(systemResponse), systemExchange.getKeyID()));
            systemResponse = encResponse;
        }
        SystemUtil.logResponse(request, response, systemExchange);
        return systemResponse;
    }

    public SystemResponse setResponse(HttpServletRequest request, SystemExchange systemExchange, SystemResponse systemResponse) throws Exception {
        systemResponse.setMessage(systemResponse.getMessage() == null ? ResponseUtil.getMessage(systemResponse.getCode()) : systemResponse.getMessage());
        systemExchange.setResponse(marshallJson(systemResponse));

        request.setAttribute(ConstantUtil.SYSTEM_EXCHANGE, systemExchange);

        if (systemExchange.getKeyID() != null) {
            SystemResponse encResponse = new SystemResponse();
            encResponse.setEncData(getEncryptedPayload(SystemUtil.marshallJson(systemResponse), systemExchange.getKeyID()));
            systemResponse = encResponse;
        }
        SystemUtil.logResponse(systemExchange);
        return systemResponse;
    }
}
