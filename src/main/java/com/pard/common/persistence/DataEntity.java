package com.pard.common.persistence;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.utils.UserUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wawe on 17/4/22.
 */
@MappedSuperclass
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 1L;

    protected String remarks;    // 备注

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @JSONField(serialize = false)
    protected User createBy;    // 创建者

    @Column(nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm", serialize = false)
    protected Date createDate;    // 创建日期

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    protected User updateBy;    // 更新者

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm", serialize = false)
    protected Date updateDate;    // 更新日期

    @Column(length = 1, nullable = false)
    @JSONField(serialize = false)
    protected String delFlag = DEL_FLAG_NORMAL;    // 删除标记（0：正常；1：删除；2：审核）

    public DataEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public DataEntity(String id) {
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord) {
            setId(IdGen.uuid());
        }
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
        }
        this.updateDate = new Date();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
