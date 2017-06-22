package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.repository.DictRepository;
import com.pard.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
@CacheConfig(cacheNames = "dicts")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Component("dictService")
public class DictServiceImpl extends SimpleServiceImpl<Dict, DictRepository> implements DictService {

    @Autowired
    @Override
    protected void setRepository(DictRepository repository) {
        this.repository = repository;
    }

    @Override
    public int findMaxSortByType(String type) {
        return repository.findMaxSortByType(type);
    }

    @Cacheable(value = "distinct_dict_type_cache")
    @Override
    public List<String> findDistinctDictType() {
        return getRepository().findDistinctDictType();
    }

    @Cacheable
    @Override
    public List<Dict> findByType(String type) {
        return getRepository().findByType(type);
    }

    @Transactional
    @CacheEvict(allEntries = true, cacheNames = {"distinct_dict_type_cache"})
    @Override
    public void delete(String id) {
        super.delete(id);
    }

    @Transactional
    @CacheEvict(allEntries = true, cacheNames = {"distinct_dict_type_cache"})
    @Override
    public void save(Dict model) {
        super.save(model);
    }
}
