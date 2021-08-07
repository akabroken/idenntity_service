package com.interswitch.tims.util;

import java.io.IOException;

import org.slf4j.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.SystemExchange;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.*;

public class SystemUtil {

    private static final Logger logger = LoggerFactory.getLogger(SystemUtil.class);

    public static String marshallJson(Object jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        if (jsonObject != null) {
            try {
                return mapper.writeValueAsString(jsonObject);
            } catch (JsonProcessingException ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public static Object unmarshallJson(String jsonString, Class objectClass) {
        ObjectMapper mapper = new ObjectMapper();
        if (jsonString != null) {
            try {
                return mapper.readValue(jsonString, objectClass);
            } catch (IOException ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
        return null;

    }

    public static SystemResponse setResponse(HttpServletRequest request, HttpServletResponse response, SystemExchange systemExchange, SystemResponse systemResponse) {
        systemResponse.setMessage(systemResponse.getMessage() == null ? ResponseUtil.getMessage(systemResponse.getCode()) : systemResponse.getMessage());
        systemExchange.setResponse(marshallJson(systemResponse));
        response.setStatus(ResponseUtil.httpStatusCode(systemResponse.getCode()).value());
        request.setAttribute(ConstantUtil.SYSTEM_EXCHANGE, systemExchange);

        logResponse(request, response, systemExchange);
        return systemResponse;
    }

    public static SystemExchange setRequest(HttpServletRequest request, Object requestObj) {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        systemExchange.setRequest(marshallJson(requestObj));
        request.setAttribute(ConstantUtil.SYSTEM_EXCHANGE, systemExchange);
        logRequest(request, systemExchange);
        return systemExchange;
    }

    public static String getStan() {
        SecureRandom random = new SecureRandom();
        String num = String.valueOf(random.nextInt(999999)).trim();
        int len = num.length();
        for (int i = 0; i < (6 - len); i++) {
            num = num.concat("1");
        }
        return num;
    }

    private static boolean logProcess(String url) {
        boolean logprocess = true;

        if (url.equals("/api/v1/service/status")) {
            logprocess = false;
        }

        return logprocess;
    }

    public static void logRequest(final HttpServletRequest request, SystemExchange systemExchange) {
        try {
            if (logProcess(request.getServletPath())) {
                logger.info("--------------------------------------REQUEST----------------------------------------------------");
                logger.info(request.getMethod() + " " + systemExchange.getUrl());
                Map<String, String> headers = Collections.list((request).getHeaderNames()).stream().collect(Collectors.toMap(h -> h, (request)::getHeader));
                logger.info("Headers:" + headers.toString());
                logger.info("Payload:" + systemExchange.getRequest());
            }
        } catch (Exception ex) {
            logger.error("logRequest", ex);
        }
    }

    public static void logResponse(HttpServletRequest request, HttpServletResponse response, SystemExchange systemExchange) {
        try {
            if (logProcess(request.getServletPath())) {
                logger.info("-------------------------------------RESPONSE----------------------------------------------------");
                logger.info(String.valueOf(response.getStatus()));
                logger.info("Headers:" + getHeaders(response));
                logger.info("Payload:" + systemExchange.getResponse());
                logger.info("---------------------------------------END--------------------------------------------------------");
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        }
    }

    public static void logResponse(SystemExchange systemExchange) {
        try {

            logger.info("-------------------------------------RESPONSE----------------------------------------------------");
            logger.info(String.valueOf(systemExchange.getHttpStatus()));
            //               logger.info("Headers:" + getHeaders(response));
            logger.info("Payload:" + systemExchange.getResponse());
            logger.info("---------------------------------------END--------------------------------------------------------");

        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        }
    }

    private static String getHeaders(HttpServletResponse response) {
        HashMap<String, String> headers = new HashMap<>();

        response.getHeaderNames().stream().forEach(header -> {
            headers.put(header, response.getHeaders(header).toString());
        });

        return headers.toString();
    }
}
