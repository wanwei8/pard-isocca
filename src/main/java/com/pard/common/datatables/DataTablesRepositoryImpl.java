package com.pard.common.datatables;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

import static com.pard.common.datatables.DataTableUtils.getPageable;

/**
 * Created by wawe on 17/5/5.
 */
public class DataTablesRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements DataTablesRepository<T, ID> {


    public DataTablesRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input) {
        return findAll(input, null, null, null);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification) {
        return findAll(input, additionalSpecification, null, null);
    }

    @Override
    public DataTableResponse<T> findAll(DataTableRequest input, Specification<T> additionalSpecification, Specification<T> preFilteringSpecification) {
        return findAll(input, additionalSpecification, preFilteringSpecification, null);
    }

    @Override
    public <R> DataTableResponse<R> findAll(DataTableRequest input, Converter<T, R> converter) {
        return findAll(input, null, null, converter);
    }

    @Override
    public <R> DataTableResponse<R> findAll(DataTableRequest input, Specification<T> additionalSpecification, Specification<T> preFilteringSpecification, Converter<T, R> converter) {
        DataTableResponse<R> output = new DataTableResponse<R>();
        output.setDraw(input.getDraw());
        if (input.getLength() <= 0) {
            return output;
        }

        try {
            long recordsTotal =
                    preFilteringSpecification == null ? count() : count(preFilteringSpecification);
            if (recordsTotal == 0) {
                return output;
            }
            output.setRecordsTotal(recordsTotal);

            Specification<T> specification = SpecificationFactory.createSpecification(input);
            Page<T> data = findAll(Specifications.where(specification).and(additionalSpecification)
                    .and(preFilteringSpecification), getPageable(input));

            List<R> content = converter == null ? (List<R>) data.getContent() : data.map(converter).getContent();
            output.setData(content);
            output.setRecordsFiltered(data.getTotalElements());
        } catch (Exception e) {
            output.setError(e.toString());
        }

        return output;
    }
}
