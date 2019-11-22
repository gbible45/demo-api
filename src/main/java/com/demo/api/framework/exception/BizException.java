package com.demo.api.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by csupshin on 18/07/16.
 */
public class BizException extends RuntimeException {

    private final ResponseEntity<?> response;

    private final HttpStatus httpStatus ;

    /**
     * BizException 생성자
     *
     * @param message
     */
    public BizException(String message) {
        super(message);
        response = null;
        httpStatus = null;
    }

    public BizException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        response = null;

    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        response = null;
        httpStatus = null;
    }

    public BizException(String message, ResponseEntity<?> response) {
        super(message);
        this.response = response;
        httpStatus = null;
    }

    public ResponseEntity<?> getResponse() {
        return response;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
