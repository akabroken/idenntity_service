/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.exception;

import com.interswitch.tims.model.SystemExchange;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

/**
 *
 * @author ndegel
 */
public class SystemException extends RuntimeException {

    protected HttpStatus httpCode;
    protected String code;

    protected SystemExchange systemExchange;
    protected HttpServletRequest request;

    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(HttpStatus httpStatus, String code, String message) {
        super(message);
        this.code = code;
        this.httpCode = httpStatus;
    }

    public SystemException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpCode = httpStatus;
    }

    public SystemException(String code,HttpStatus httpStatus, String message, HttpServletRequest request,SystemExchange systemExchange) {
        super(message);
        this.httpCode = httpStatus;
        this.request = request;
        this.systemExchange = systemExchange;
    }

    public SystemException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(HttpStatus httpCode) {
        this.httpCode = httpCode;
    }

}
