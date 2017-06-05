package com.pard.common.logger;

import com.pard.modules.sys.entity.LoggerInfo;

/**
 * Created by wawe on 17/5/31.
 */
public interface AccessLoggerPersisting {
    void save(LoggerInfo loggerInfo);
}
