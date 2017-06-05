package com.pard.common.exception;

/**
 * Created by wawe on 17/5/30.
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}
