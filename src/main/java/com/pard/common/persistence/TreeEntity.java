package com.pard.common.persistence;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.utils.ReflectionUtils;
import com.pard.common.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created by wawe on 17/4/22.
 */
@MappedSuperclass
public abstract class TreeEntity<T extends BaseEntity> extends DataEntity<T> {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JSONField(serialize = false)
    protected T parent;    // 父级编号

    @Column(length = 2000)
    @JSONField(serialize = false)
    protected String parentIds; // 所有父级编号

    @Column(length = 100)
    protected String name;    // 名称

    @Column(scale = 10)
    protected Integer sort;        // 排序

    public TreeEntity() {
        super();
        this.sort = 30;
    }

    public TreeEntity(String id) {
        super(id);
    }

    public abstract T getParent();

    public abstract void setParent(T parent);

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        String id = null;
        if (parent != null) {
            id = (String) ReflectionUtils.getFieldValue(parent, "id");
        }
        return StringUtils.isNotBlank(id) ? id : "0";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        T tree = (T) obj;
        return id != null && id.equals(tree.getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == id ? 0 : id.hashCode());
        hash = 31 * hash + (null == name ? 0 : name.hashCode());
        hash = 31 * hash + (null == parentIds ? 0 : parentIds.hashCode());
        return hash;
    }
}
