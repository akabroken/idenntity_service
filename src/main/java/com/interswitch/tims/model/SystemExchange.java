/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.model;

import lombok.Data;

/**
 *
 * @author ndegel
 */
@Data
public class SystemExchange {

    private String url;
    private String httpMethod;
    private String servletPath;
    private String requestHeaders;
    private String request;
    private String response;
    private String httpStatus;

    private Object payload;

    private String clientID;
    private String keyID;
    private Integer instID;
    private String sessionKeyId;
    private String sessionKey;
}
