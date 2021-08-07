/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.exception;

import com.interswitch.tims.model.SystemExchange;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author ndegel
 */
public class InvalidPayloadException extends RuntimeException {

    protected String message;

    protected SystemExchange systemExchange;
    protected HttpServletRequest request;

    public InvalidPayloadException(String message, HttpServletRequest request,SystemExchange systemExchange) {
        super(message);
        this.request = request;
        this.systemExchange = systemExchange;
    }

   
}
