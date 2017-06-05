package com.pard.common.jsonp;

import com.pard.common.utils.StringUtils;
import com.pard.common.utils.ThreadLocalUtils;
import com.pard.common.utils.WebUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by wawe on 17/5/31.
 */
@Aspect
@Order
@Component
public class AopJsonpSupport {
    @Before(value = "@annotation(jsonp)")
    public void around(Jsonp jsonp) throws Throwable {
        String callback = WebUtil.getHttpServletRequest().getParameter(jsonp.value());
        if (!StringUtils.isNullOrEmpty(callback)) {
            ThreadLocalUtils.put("jsonp-callback", callback);
        }
    }
}
