package com.pard.common.exception;

/**
 * Created by wawe on 17/5/30.
 */
public class AuthorizeException extends BusinessException {

    public AuthorizeException(String message) {
        super(message, 401);
    }

    public AuthorizeException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
