package com.pard.common.service;

import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.persistence.BaseEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by wawe on 17/5/22.
 */
public interface BaseService<T extends BaseEntity> {

    T findOne(String id);

    long count(Specification<T> specification);

    DataTableResponse<T> findAll(DataTableRequest input);

    DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification);

    DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification, Specification<T> preFilteringSpecification);

    void delete(String id);

    void save(T model);
}
