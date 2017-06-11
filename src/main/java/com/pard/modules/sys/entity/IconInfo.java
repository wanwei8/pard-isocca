package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wawe on 17/5/16.
 */
@Entity
@Table(name = "icon_info")
public class IconInfo extends DataEntity<IconInfo> {

    @Column(length = 200, nullable = false)
    private String displayName;

    @Column(length = 400, nullable = false)
    private String className;

    @Column(length = 50, nullable = false)
    @JSONField(serialize = false)
    private String sourceType;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    @JSONField(serialize = false)
    public String getId() {
        return super.getId();
    }

    @Override
    @JSONField(serialize = false)
    public String getRemarks() {
        return super.getRemarks();
    }

    public IconInfo() {
    }

    public IconInfo(String id) {
        super(id);
    }

}
