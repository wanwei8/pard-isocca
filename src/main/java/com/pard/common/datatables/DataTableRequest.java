package com.pard.common.datatables;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pard.common.utils.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wawe on 17/5/4.
 */
public class DataTableRequest implements Serializable {
    private Integer draw = 1;

    private Integer start = 0;

    private Integer length = 10;

    private Search search;

    private List<Order> order;

    private List<Column> columns;

    public DataTableRequest() {
        search = new Search();
        order = Lists.newArrayList();
        columns = Lists.newArrayList();
    }

    public Integer getDraw() {
        return draw;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getLength() {
        return length;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Search getSearch() {
        return search;
    }

    public List<Order> getOrder() {
        return order;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Map<String, Column> getColumnsAsMap() {
        Map<String, Column> map = Maps.newHashMap();
        for (Column column : columns) {
            map.put(column.getData(), column);
        }
        return map;
    }

    public Column getColumn(String columnName) {
        if (StringUtils.isBlank(columnName)) {
            return null;
        }

        for (Column column : columns) {
            if (columnName.equals(column.getData())) {
                return column;
            }
        }
        return null;
    }

    public void addColumn(String columnName, boolean searchable, boolean orderable,
                          String searchValue) {
        this.columns.add(new Column(columnName, "", searchable, orderable,
                new Search(searchValue, false)));
    }

    public void addOrder(String columnName, boolean ascending) {
        if (StringUtils.isBlank(columnName)) {
            return;
        }

        for (int i = 0; i < columns.size(); i++) {
            if (!columnName.equals(columns.get(i).getData())) {
                continue;
            }
            order.add(new Order(i, ascending ? "asc" : "desc"));
        }
    }
}
