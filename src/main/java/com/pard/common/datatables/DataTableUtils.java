package com.pard.common.datatables;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * Created by wawe on 17/5/5.
 */
public class DataTableUtils {

    public final static String OR_SEPARATOR = "+";
    public final static String ESCAPED_OR_SEPARATOR = "\\+";
    public final static String ATTRIBUTE_SEPARATOR = ".";
    public final static String ESCAPED_ATTRIBUTE_SEPARATOR = "\\.";
    public final static char ESCAPED_CHAR = '\\';
    public final static String NULL = "NULL";
    public final static String ESCAPED_NULL = "\\NULL";

    public static boolean isBoolean(String filterValue) {
        return "TRUE".equalsIgnoreCase(filterValue) || "FALSE".equalsIgnoreCase(filterValue);
    }

    public static String getLikeFilterValue(String filterValue) {
        return "%" +
                filterValue.toLowerCase().replaceAll("%", "\\\\" + "%").replaceAll("_", "\\\\" + "_") +
                "%";
    }

    public static Pageable getPageable(DataTableRequest input) {
        List<Order> orders = Lists.newArrayList();
        for (com.pard.common.datatables.Order order : input.getOrder()) {
            Column column = input.getColumns().get(order.getColumn());
            if (column.getOrderable()) {
                String sortColumn = column.getData();
                Direction sortDirection = Direction.fromString(order.getDir());
                orders.add(new Order(sortDirection, sortColumn));
            }
        }

        Sort sort = orders.isEmpty() ? null : new Sort(orders);
        if (input.getLength() == -1) {
            input.setStart(0);
            input.setLength(Integer.MAX_VALUE);
        }
        return new DataTablePageRequest(input.getStart(), input.getLength(), sort);
    }

    private static class DataTablePageRequest implements Pageable {
        private final int offset;

        private final int pageSize;

        private final Sort sort;

        public DataTablePageRequest(int offset, int pageSize, Sort sort) {
            this.offset = offset;
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public int getPageNumber() {
            return 0;
        }

        @Override
        public int getPageSize() {
            return pageSize;
        }

        @Override
        public int getOffset() {
            return offset;
        }

        @Override
        public Sort getSort() {
            return sort;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    }
}
