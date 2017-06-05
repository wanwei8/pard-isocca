package com.pard.common.exception;

/**
 * 业务异常，用于抛出给前端提示错误信息
 * Created by wawe on 17/5/30.
 */
public class BusinessException extends RuntimeException {
    private int status = 400;

    public BusinessException(String message) {
        this(message, 400);
    }

    public BusinessException(String message, int status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
