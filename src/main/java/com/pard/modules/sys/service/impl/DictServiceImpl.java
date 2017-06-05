package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.repository.DictRepository;
import com.pard.modules.sys.service.DictService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
@CacheConfig(cacheNames = "dicts")
@Component("dictService")
public class DictServiceImpl extends SimpleServiceImpl<Dict, DictRepository> implements DictService {

    @Autowired
    @Override
    protected void setRepository(DictRepository repository) {
        this.repository = repository;
    }

    @Override
    protected String getCacheName() {
        return "dicts";
    }

    @Override
    public int findMaxSortByType(String type) {
        return repository.findMaxSortByType(type);
    }

    @Override
    public List<String> findDistinctDictType() {
        return getRepository().findDistinctDictType();
    }

    @Override
    public List<Dict> findByType(String type) {
        return getRepository().findByType(type);
    }
}
