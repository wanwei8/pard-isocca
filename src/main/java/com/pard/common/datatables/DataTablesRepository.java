package com.pard.common.datatables;

import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by wawe on 17/5/5.
 */
@NoRepositoryBean
public interface DataTablesRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

    DataTableResponse<T> findAll(DataTableRequest input);

    DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification);

    DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification,
                                 Specification<T> preFilteringSpecification);

    <R> DataTableResponse<R> findAll(DataTableRequest input, Converter<T, R> converter);

    <R> DataTableResponse<R> findAll(DataTableRequest input, Specification<T> additionalSpecification,
                                     Specification<T> preFilteringSpecification, Converter<T, R> converter);
}
