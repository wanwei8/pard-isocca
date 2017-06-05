package com.pard.common;

import com.pard.common.exception.BusinessException;
import com.pard.common.logger.AccessLoggerPersisting;
import com.pard.common.logger.AopAccessLoggerResolver;
import com.pard.common.logger.Slf4jAccessLoggerPersisting;
import com.pard.common.message.FastJsonHttpMessageConverter;
import com.pard.common.message.ResponseMessage;
import com.pard.modules.sys.entity.LoggerInfo;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.utils.UserUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/6/1.
 */
@Configuration
@ConditionalOnProperty(name = "pard.access-logger", havingValue = "true")
public class AopAccessLoggerResolverAutoConfiguration {

    @Bean
    public AopAccessLoggerResolverConfiguration aopAccessLoggerResolverConfiguration() {
        return new AopAccessLoggerResolverConfiguration();
    }

    public Slf4jAccessLoggerPersisting slf4jAccessLoggerPersisting() {
        return new Slf4jAccessLoggerPersisting();
    }

    @Aspect
    @Order(Ordered.HIGHEST_PRECEDENCE)
    static class AopAccessLoggerResolverConfiguration extends AopAccessLoggerResolver {

        @Autowired(required = false)
        private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

        @Autowired(required = false)
        private List<AccessLoggerPersisting> accessLoggerPersisting;

        @PostConstruct
        private void init() {
            if (fastJsonHttpMessageConverter == null) {
                fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
            }
        }

        @Around(value = "execution(* com.pard.modules..controller..*Controller..*(..))||@annotation(com.pard.common.logger.annotation.AccessLogger)")
        public Object around(ProceedingJoinPoint pjp) throws Throwable {
            LoggerInfo loggerInfo = resolver(pjp);
            long requestTime = System.currentTimeMillis();
            Object result = null;
            try {
                result = pjp.proceed();
            } catch (Throwable e) {
                if (!(e instanceof BusinessException)) {
                    result = ResponseMessage.error(e.getMessage());
                    loggerInfo.setExceptionInfo(e.getMessage());
                } else {
                    result = ResponseMessage.error(e.getMessage(), ((BusinessException) e).getStatus());
                }
                throw e;
            } finally {
                long responseTime = System.currentTimeMillis();
                User user = UserUtils.getUser();
                loggerInfo.setRequestTime(new Date(requestTime));
                loggerInfo.setResponseTime(new Date(responseTime));
                loggerInfo.setResponseContext(fastJsonHttpMessageConverter.converter(result));
                if (user != null) {
                    loggerInfo.setUser(user);
                }
                if (result instanceof ResponseMessage) {
                    loggerInfo.setResponseCode(String.valueOf(((ResponseMessage) result).getCode()));
                }
                if (accessLoggerPersisting != null) {
                    accessLoggerPersisting.forEach(loggerPersisting -> loggerPersisting.save(loggerInfo));
                }
            }
            return result;
        }
    }
}
