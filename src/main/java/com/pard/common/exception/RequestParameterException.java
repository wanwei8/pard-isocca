package com.pard.common.exception;

/**
 * Created by wawe on 17/6/5.
 */
public class RequestParameterException extends ValidationException {
    public RequestParameterException() {
        super("参数错误");
    }
}
