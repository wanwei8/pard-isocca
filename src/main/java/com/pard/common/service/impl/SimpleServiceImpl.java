package com.pard.common.service.impl;

import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.datatables.DataTablesRepository;
import com.pard.common.persistence.BaseEntity;
import com.pard.common.service.BaseService;
import com.pard.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by wawe on 17/5/19.
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class SimpleServiceImpl<T extends BaseEntity, R extends DataTablesRepository> implements BaseService<T> {

    @Autowired
    protected CacheManager cacheManager;

    protected R repository;
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract String getCacheName();

    protected R getRepository() {
        return repository;
    }

    protected abstract void setRepository(R repository);

    @Override
    public T findOne(String id) {
        return (T) repository.findOne(id);
    }

    @Override
    public long count(Specification<T> specification) {
        return repository.count(specification);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input) {
        return repository.findAll(input);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
        clearCache();
    }

    @Override
    public void save(T model) {
        if (StringUtils.isBlank(model.getId()))
            model.preInsert();
        model.preUpdate();
        repository.save(model);
        clearCache();
    }

    public void clearCache() {
        Cache cache = cacheManager.getCache(getCacheName());
        if (cache != null) {
            cache.clear();
        }
    }
}
