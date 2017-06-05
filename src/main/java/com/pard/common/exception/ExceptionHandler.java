package com.pard.common.exception;


import com.pard.common.message.ResponseMessage;

/**
 * Created by wawe on 17/5/30.
 */
public interface ExceptionHandler {

    <T extends Throwable> boolean support(Class<T> e);

    ResponseMessage handle(Throwable e);
}
