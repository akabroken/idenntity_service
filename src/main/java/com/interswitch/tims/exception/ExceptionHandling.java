package com.interswitch.tims.exception;

import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.BaseModel;
import com.interswitch.tims.service.*;
import com.interswitch.tims.util.*;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestService RestService;

    private void printStackTrace(Exception ex) {

        logger.info(ex.getMessage(), ex);

    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> systemException(SystemException ex) throws Exception {
        SystemResponse response = new SystemResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());

        SystemUtil.logRequest(ex.request, ex.systemExchange);

        printStackTrace(ex);
        ex.systemExchange.setHttpStatus(ex.httpCode.toString());
        RestService.setResponse(ex.request, ex.systemExchange, response);
        return new ResponseEntity<>(response, ex.httpCode);
    }

    @ExceptionHandler(InvalidPayloadException.class)
    public ResponseEntity<Object> systemException(InvalidPayloadException ex) throws Exception {
        SystemResponse response = new SystemResponse();
        response.setCode(ResponseUtil.INVALID_PAYLOAD_E38);
        response.setMessage(ex.getMessage());

        SystemUtil.logRequest(ex.request, ex.systemExchange);

        printStackTrace(ex);
        RestService.setResponse(ex.request, ex.systemExchange, response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        SystemResponse response = new SystemResponse();
        response.setCode(ResponseUtil.SYSTEM_ERROR_E21);
        response.setMessage(ResponseUtil.SYSTEM_ERROR_MSG);

        printStackTrace(ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        SystemResponse response = new SystemResponse();
        response.setCode(ResponseUtil.INVALID_PAYLOAD_E38);
        response.setMessage("Invalid request payload");
        ArrayList<BaseModel> errors = new ArrayList<>();
        result.getAllErrors().stream().map((ObjectError objectError) -> {
            BaseModel error = new BaseModel();
            error.setMessage(objectError.getDefaultMessage());
            return error;
        }).forEach((BaseModel error) -> {
            errors.add(error);
        });
        response.setErrors(errors);
        printStackTrace(ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return exception(ex);
    }

    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SystemResponse response = new SystemResponse();
        response.setCode(ResponseUtil.INVALID_PAYLOAD_E38);
        response.setMessage(ex.getMessage());
        printStackTrace(ex);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SystemResponse response = new SystemResponse();
        response.setCode(ResponseUtil.INVALID_PAYLOAD_E38);
        response.setMessage(ex.getMessage());
        printStackTrace(ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
