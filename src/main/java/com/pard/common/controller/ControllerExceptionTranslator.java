package com.pard.common.controller;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.pard.common.constant.MessageConstant;
import com.pard.common.exception.BusinessException;
import com.pard.common.message.ResponseMessage;
import com.pard.common.utils.StringUtils;
import com.pard.common.utils.WebUtil;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一错误处理
 * Created by wawe on 17/6/1.
 */
@ControllerAdvice(annotations = Controller.class)
@Order(10)
public class ControllerExceptionTranslator {

    @ExceptionHandler(BusinessException.class)
    ModelAndView handleExceptionView(BusinessException exception, HttpServletResponse response) {
        response.setStatus(exception.getStatus());
        ModelAndView modelAndView = new ModelAndView("error/" + exception.getStatus());
        modelAndView.addAllObjects(ResponseMessage.error(exception.getMessage(), exception.getStatus()).toMap());
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("absPath", WebUtil.getBasePath(WebUtil.getHttpServletRequest()));
        return modelAndView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    ModelAndView handleExceptionView(AccessDeniedException exception, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(403);
        String requestType = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(requestType)) {
            FastJsonJsonView view = new FastJsonJsonView();
            view.setAttributesMap(ResponseMessage.error(MessageConstant.Access_Denied_Exception, 403).toMap());
            return new ModelAndView(view);
        }

        ModelAndView modelAndView = new ModelAndView("error/" + 403);
        modelAndView.addAllObjects(ResponseMessage.error(MessageConstant.Access_Denied_Exception, 403).toMap());
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("absPath", WebUtil.getBasePath(WebUtil.getHttpServletRequest()));
        return modelAndView;
    }

    @ExceptionHandler(Throwable.class)
    ModelAndView handleExceptionView(Throwable exception, HttpServletResponse response) {
        response.setStatus(500);
        ModelAndView modelAndView = new ModelAndView("error/" + 500);
        modelAndView.addAllObjects(ResponseMessage.error(exception.getMessage(), 500).toMap());
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("absPath", WebUtil.getBasePath(WebUtil.getHttpServletRequest()));
        return modelAndView;
    }
}
