package com.pard.common.logger;

import com.alibaba.fastjson.JSON;
import com.pard.common.message.FastJsonHttpMessageConverter;
import com.pard.modules.sys.entity.LoggerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wawe on 17/5/31.
 */
public class Slf4jAccessLoggerPersisting implements AccessLoggerPersisting {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired(required = false)
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    @Override
    public void save(LoggerInfo loggerInfo) {
        if (logger.isInfoEnabled())
            if (fastJsonHttpMessageConverter == null)
                logger.info(JSON.toJSONString(loggerInfo));
            else
                logger.info(fastJsonHttpMessageConverter.converter(loggerInfo));
    }
}
