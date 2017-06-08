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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("delFlag").as(String.class), T.DEL_FLAG_NORMAL);
                return criteriaBuilder.and(predicate);
            }
        };
        return repository.findAll(input, null, specification);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification) {
        return repository.findAll(input, additionalSpecification);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification, Specification<T> preFilteringSpecification) {
        return repository.findAll(input, additionalSpecification, preFilteringSpecification);
    }

    @Modifying
    @Transactional
    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Transactional
    @Override
    public void save(T model) {
        if (StringUtils.isBlank(model.getId()))
            model.preInsert();
        model.preUpdate();
        repository.save(model);
    }

    public void clearCache() {
        Cache cache = cacheManager.getCache(getCacheName());
        if (cache != null) {
            cache.clear();
        }
    }
}
