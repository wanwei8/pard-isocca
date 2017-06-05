package com.pard.modules.sys.entity;

import com.pard.common.persistence.TreeEntity;
import com.pard.common.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by wawe on 17/4/22.
 */
@Entity
@Table(name = "sys_area")
public class Area extends TreeEntity<Area> {

    private static final long serialVersionUID = 1L;
    @Column(length = 100)
    private String code;    // 区域编码
    @Column(length = 1)
    private String type;    // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
    @Transient
    private String typeLabel;

    public Area() {
        super();
        this.sort = 30;
    }

    public Area(String id) {
        super(id);
        this.sort = 30;
    }

    public Area(String id, String pid, String name) {
        super(id);
        if (StringUtils.isNotBlank(pid)) {
            this.parent = new Area(pid);
        }
        this.name = name;
    }

    public Area(String id, String pid, String name, String parentIds) {
        this(id, pid, name);
        this.parentIds = parentIds;
    }

    @Override
    public Area getParent() {
        return parent;
    }

    @Override
    public void setParent(Area parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    @Override
    public String toString() {
        return name;
    }
}
