package com.pard.common.datatables;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wawe on 17/5/1.
 */
public class DataTableResponse<T> implements Serializable {

    private int draw;

    private long recordsTotal = 0;

    private long recordsFiltered = 0;

    private List<T> data;

    private String error;

    public DataTableResponse() {
        data = Lists.newArrayList();
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
