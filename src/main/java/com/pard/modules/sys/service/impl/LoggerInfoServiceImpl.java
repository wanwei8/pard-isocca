package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.modules.sys.entity.LoggerInfo;
import com.pard.modules.sys.repository.LoggerInfoRepository;
import com.pard.modules.sys.service.LoggerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wawe on 17/6/8.
 */
@Component("loggerInfoService")
public class LoggerInfoServiceImpl extends SimpleServiceImpl<LoggerInfo, LoggerInfoRepository> implements LoggerInfoService {

    @Override
    public void save(LoggerInfo model) {
        super.save(model);
    }

    @Autowired
    @Override
    protected void setRepository(LoggerInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoggerInfo findOne(String id) {
        return getRepository().findOne(id);
    }
}
