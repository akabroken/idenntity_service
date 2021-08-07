/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.util;

import org.springframework.http.HttpStatus;

/**
 *
 * @author ndegel
 */
public final class ResponseUtil {

    public static final String SUCCESS_CODE_0 = "0";
    public static final String ENTITY_MISSING_CODE_1 = "1";

    public static final String SYSTEM_ERROR_E21 = "E21";
    public static final String INVALID_PAYLOAD_E38 = "E38";

    public static final String SYSTEM_ERROR_MSG = "Unable to process request,contact support";

    public static String getMessage(String code) {

        String message = "";
        switch (code) {
            case SUCCESS_CODE_0:
                message = "Success";
                break;
            case ENTITY_MISSING_CODE_1:
                message = "Entity not found";
                break;
            default:
                message = SYSTEM_ERROR_MSG;
                break;
        }

        return message;
    }

    public static HttpStatus httpStatusCode(String code) {
        HttpStatus httpStatus = null;

        switch (code) {
            case SUCCESS_CODE_0:
                httpStatus = HttpStatus.OK;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return httpStatus;
    }
}
