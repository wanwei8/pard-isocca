package com.pard.common.datatables;

/**
 * Created by wawe on 17/5/5.
 */
public class Order {

    private Integer column;

    private String dir;

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Order() {

    }

    public Order(int column, String dir) {
        this.column = column;
        this.dir = dir;
    }
}
