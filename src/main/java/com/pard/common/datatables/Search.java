package com.pard.common.datatables;

/**
 * Created by wawe on 17/5/5.
 */
public class Search {

    private String value;

    private Boolean regex;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getRegex() {
        return regex;
    }

    public void setRegex(Boolean regex) {
        this.regex = regex;
    }

    public Search() {

    }

    public Search(String searchValue, boolean regex) {
        this.value = searchValue;
        this.regex = regex;
    }
}
