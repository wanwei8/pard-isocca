package com.pard.common.exception;


import org.springframework.security.authentication.AccountStatusException;

/**
 * @Desciption: 自定义验证码错误处理类
 * Created by wawe on 17/5/11.
 */
public class IncorrectCaptchaException extends AccountStatusException {

    public IncorrectCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public IncorrectCaptchaException(String msg) {
        super(msg);
    }
}
