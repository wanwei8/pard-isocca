package com.pard.common.datatables;

import com.google.common.collect.Lists;
import com.pard.common.utils.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import java.util.List;

import static com.pard.common.datatables.DataTableUtils.*;

/**
 * Created by wawe on 17/5/5.
 */
public class SpecificationFactory {
    public static <T> Specification<T> createSpecification(final DataTableRequest input) {
        return new DataTablesSpecification<T>(input);
    }

    private static class DataTablesSpecification<T> implements Specification<T> {
        private final DataTableRequest input;

        public DataTablesSpecification(DataTableRequest input) {
            this.input = input;
        }

        private static <S> Expression<S> getExpression(Root<?> root, String columnData, Class<S> clazz) {
            if (!columnData.contains(ATTRIBUTE_SEPARATOR)) {
                return root.get(columnData).as(clazz);
            }
            String[] values = columnData.split(ESCAPED_ATTRIBUTE_SEPARATOR);
            if (root.getModel().getAttribute(values[0]).getPersistentAttributeType() == Attribute.PersistentAttributeType.EMBEDDED) {
                return root.get(values[0]).get(values[1]).as(clazz);
            }
            From<?, ?> from = root;
            for (int i = 0; i < values.length; i++) {
                from = from.join(values[i], JoinType.LEFT);
            }
            return from.get(values[values.length - 1]).as(clazz);
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = criteriaBuilder.conjunction();
            Expression<Boolean> booleanExpression;
            Expression<String> stringExpression;

            for (Column column : input.getColumns()) {
                String filterValue = column.getSearch().getValue();
                boolean isColumnSearchable = column.getSearchable() && StringUtils.isNotBlank(filterValue);
                if (!isColumnSearchable) {
                    continue;
                }

                if (filterValue.contains(OR_SEPARATOR)) {
                    boolean nullable = false;
                    List<String> values = Lists.newArrayList();
                    for (String value : filterValue.split(ESCAPED_OR_SEPARATOR)) {
                        if (NULL.equals(value)) {
                            nullable = true;
                        } else {
                            values.add(ESCAPED_NULL.equals(value) ? NULL : value);
                        }
                    }
                    if (values.size() > 0 && isBoolean(values.get(0))) {
                        Object[] booleanValues = new Boolean[values.size()];
                        for (int i = 0; i < values.size(); i++) {
                            booleanValues[i] = Boolean.valueOf(values.get(i));
                        }
                        booleanExpression = getExpression(root, column.getData(), Boolean.class);
                        Predicate in = booleanExpression.in(booleanValues);
                        if (nullable) {
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(in, booleanExpression.isNull()));
                        } else {
                            predicate = criteriaBuilder.and(predicate, in);
                        }
                    } else {
                        stringExpression = getExpression(root, column.getData(), String.class);
                        if (values.isEmpty()) {
                            if (nullable) {
                                predicate = criteriaBuilder.and(predicate, stringExpression.isNull());
                            }
                            continue;
                        }
                        Predicate in = stringExpression.in(values);
                        if (nullable) {
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(in, stringExpression.isNull()));
                        } else {
                            predicate = criteriaBuilder.and(predicate, in);
                        }
                    }
                    continue;
                }
                if (isBoolean(filterValue)) {
                    booleanExpression = getExpression(root, column.getData(), Boolean.class);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(booleanExpression, Boolean.valueOf(filterValue)));
                    continue;
                }

                stringExpression = getExpression(root, column.getData(), String.class);
                if (NULL.equals(filterValue)) {
                    predicate = criteriaBuilder.and(predicate, stringExpression.isNull());
                    continue;
                }

                String likeFilterValue =
                        getLikeFilterValue(ESCAPED_NULL.equals(filterValue) ? NULL : filterValue);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(stringExpression), likeFilterValue, ESCAPED_CHAR));
            }

            String globalFilterValue = input.getSearch().getValue();
            if (StringUtils.isNotBlank(globalFilterValue)) {
                Predicate matchOneColumnPredicate = criteriaBuilder.disjunction();

                for (Column column : input.getColumns()) {
                    if (column.getSearchable()) {
                        Expression<String> expression = getExpression(root, column.getData(), String.class);

                        matchOneColumnPredicate = criteriaBuilder.or(matchOneColumnPredicate,
                                criteriaBuilder.like(criteriaBuilder.lower(expression),
                                        getLikeFilterValue(globalFilterValue), ESCAPED_CHAR));
                    }
                }

                predicate = criteriaBuilder.and(predicate, matchOneColumnPredicate);
            }

            boolean isCountQuery = criteriaQuery.getResultType() == Long.class;
            if (isCountQuery) {
                return predicate;
            }

            for (Column column : input.getColumns()) {
                boolean isJoinable = column.getSearchable() &&
                        column.getData().contains(ATTRIBUTE_SEPARATOR);
                if (!isJoinable) {
                    continue;
                }

                String[] values = column.getData().split(ESCAPED_ATTRIBUTE_SEPARATOR);
                Attribute.PersistentAttributeType type =
                        root.getModel().getAttribute(values[0]).getPersistentAttributeType();
                if (type != Attribute.PersistentAttributeType.ONE_TO_ONE
                        && type != Attribute.PersistentAttributeType.MANY_TO_ONE) {
                    continue;
                }

                Fetch<?, ?> fetch = null;
                for (int i = 0; i < values.length - 1; i++) {
                    fetch = (fetch == null ? root : fetch).fetch(values[i], JoinType.LEFT);
                }
            }
            return predicate;
        }
    }
}
