package com.pard.common.exception;

/**
 * Created by wawe on 17/5/30.
 */
public class AuthorizeForbiddenException extends BusinessException {
    public AuthorizeForbiddenException(String message) {
        super(message, 403);
    }

    public AuthorizeForbiddenException(String message, Throwable cause) {
        super(message, cause, 403);
    }
}
