package com.pard.common.datatables;

/**
 * Created by wawe on 17/5/5.
 */
public class Column {
    private String data;

    private String name;

    private Boolean searchable;

    private Boolean orderable;

    private Search search;

    public Column() {
        this.search = new Search();
    }

    public Column(String data, String name, boolean searchable, boolean orderable, Search search) {
        this();
        this.data = data;
        this.name = name;
        this.searchable = searchable;
        this.orderable = orderable;
        if (search != null) {
            this.search.setValue(search.getValue());
            this.search.setRegex(search.getRegex());
        }
    }

    public void setSearchValue(String searchValue) {
        this.search.setValue(searchValue);
    }

    public void setSearchRegex(boolean searchRegex) {
        this.search.setRegex(searchRegex);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public Boolean getOrderable() {
        return orderable;
    }

    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
