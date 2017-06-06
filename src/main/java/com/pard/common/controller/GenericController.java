package com.pard.common.controller;

import com.pard.common.constant.StringConstant;
import com.pard.common.exception.NotFoundException;
import com.pard.common.persistence.BaseEntity;
import com.pard.common.service.BaseService;
import com.pard.common.utils.ClassUtils;
import com.pard.common.utils.ReflectionUtils;
import com.pard.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wawe on 17/6/1.
 */
public abstract class GenericController implements StringConstant {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 管理基础路径
     */
    @Value("${adminPath}")
    protected String adminPath;

    /***
     * Rest路径
     */
    @Value("${apiPath}")
    protected String apiPath;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    /**
     * 判断对象是否为空，如果为空将抛出(@link NotFoundException)
     *
     * @param obj
     * @param msg
     */
    public void assertFound(Object obj, String msg) {
        if (obj == null) throw new NotFoundException(msg);
    }
}
