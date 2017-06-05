package com.pard.modules.sys.entity;

import com.pard.common.persistence.DataEntity;
import com.pard.common.utils.IdGen;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wawe on 17/5/3.
 */
@Entity
@Table(name = "sys_dict")
public class Dict extends DataEntity<Dict> {
    public Dict() {
        sort = 10;
    }

    public Dict(String id) {
        this();
        setId(id);
    }

    public Dict(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public Dict(String id, String value, String label, String type, int sort) {
        this(id, value, label, type, null, sort, null, null);
    }

    public Dict(String value, String label, String type, String description, int sort, User createBy, User updateBy) {
        this(IdGen.uuid(), value, label, type, description, sort, createBy, updateBy);
    }

    public Dict(String id, String value, String label, String type, String description, int sort, User createBy, User updateBy) {
        this(id);
        setValue(value);
        setLabel(label);
        setType(type);
        setDescription(description);
        setSort(sort);
        setCreateDate(new Date());
        setCreateBy(createBy);
        setUpdateDate(new Date());
        setUpdateBy(updateBy);
    }

    private static final long serialVersionUID = -1L;

    /**
     * 键值
     */
    @Column(length = 100, nullable = false)
    private String value;

    /**
     * 标签
     */
    @Column(nullable = false, length = 100)
    private String label;

    /**
     * 类型
     */
    @Column(nullable = false, length = 100)
    private String type;

    /**
     * 描述
     */
    @Column
    private String description;

    /**
     * 排序
     */
    @Column(nullable = false)
    private Integer sort;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return label;
    }
}
