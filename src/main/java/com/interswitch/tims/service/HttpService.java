/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.model.ProviderData;
import com.interswitch.tims.util.ConstantUtil;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;

/**
 *
 * @author ndegel
 */
@Service
public class HttpService {

    Logger logger = LoggerFactory.getLogger(getClass());

    public void send(ProviderData providerData) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        if (providerData.isIs2waySSL()) {
            //httpClientBuilder = addSSLFactory(httpClientBuilder, providerData);
        }
        try (CloseableHttpClient httpclient = httpClientBuilder.build()) {

            HttpRequestBase httpRequestBase = createHttpBaseRequest(providerData);

            CloseableHttpResponse providerResponse = httpclient.execute(httpRequestBase);
            providerData.setStatuCode(providerResponse.getStatusLine().getStatusCode());
            HttpEntity entity = providerResponse.getEntity();

            String responseString = EntityUtils.toString(entity, "UTF-8");
            providerData.setResponse(responseString);
            logResponse(providerData);
        }
    }

    private HttpRequestBase createHttpBaseRequest(ProviderData providerData) throws Exception {
        HttpRequestBase httpRequestBase = null;
        switch (providerData.getHttpMethod().toUpperCase()) {
            case ConstantUtil.HTTP_POST:
                httpRequestBase = createHttpPostBase(providerData);
                break;
            case ConstantUtil.HTTP_PUT:
                httpRequestBase = createHttpPutBase(providerData.getUrl(), providerData.getRequest());
                break;
            case ConstantUtil.HTTP_GET:
                httpRequestBase = createHttpGetBase(providerData);
                break;
        }

        if (providerData.getOutheaders() != null) {
            httpRequestBase.setHeaders(providerData.getOutheaders());
        }

        logRequest(httpRequestBase, providerData.getRequest());
        return httpRequestBase;
    }

    private HttpGet createHttpGetBase(ProviderData providerData) throws Exception {
        HttpGet httpGet = (providerData.getUrl() == null ? new HttpGet(providerData.getUri()) : new HttpGet(providerData.getUrl()));
        return httpGet;
    }

    private HttpPost createHttpPostBase(ProviderData providerData) throws Exception {

        HttpPost httpPost = (providerData.getUrl() == null ? new HttpPost(providerData.getUri()) : new HttpPost(providerData.getUrl()));
        if (providerData.getRequest() != null) {
            httpPost.setEntity(new StringEntity(providerData.getRequest()));
        }

        return httpPost;
    }

    private HttpPut createHttpPutBase(String url, String request) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        if (request != null) {
            httpPut.setEntity(new StringEntity(request));
        }
        httpPut.setEntity(new StringEntity(request));
        return httpPut;
    }

    private void logRequest(HttpRequestBase httpRequestBase, String request) {

        logger.info("----------------------------");
        logger.info("Address: {}", httpRequestBase.getURI());
        logger.info("HttpMethod: {}", httpRequestBase.getMethod());
        logger.info("Headers: {}", getHeaders(httpRequestBase.getAllHeaders()));
        logger.info("Payload: {}", request);

    }

    private String getHeaders(Header[] headers) {
        String headersline = "";
        for (Header header : headers) {
            headersline = headersline + ",[" + header.getName() + ": " + header.getValue() + "]";
        }

        return headersline;
    }

    private void logResponse(ProviderData providerData) {

        logger.info("Response Status Code: {}", providerData.getStatuCode());
        logger.info("Headers: {}", getHeaders(providerData.getOutheaders()));
        logger.info("Payload: {}", providerData.getResponse());
        logger.info("----------------------------");

    }
}
