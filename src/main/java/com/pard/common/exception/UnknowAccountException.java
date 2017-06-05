package com.pard.common.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * Created by wawe on 17/5/11.
 */
public class UnknowAccountException extends AccountStatusException {
    public UnknowAccountException(String msg) {
        super(msg);
    }

    public UnknowAccountException(String msg, Throwable t) {
        super(msg, t);
    }
}
