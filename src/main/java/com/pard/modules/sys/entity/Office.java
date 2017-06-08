package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.persistence.TreeEntity;
import com.pard.common.utils.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by wawe on 17/4/22.
 */
@Entity
@Table(name = "sys_office")
public class Office extends TreeEntity<Office> {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Area area;        // 归属区域

    @Column(length = 100)
    private String code;    // 机构编码

    @Column(length = 1, nullable = false)
    private String type;    // 机构类型（1：公司；2：部门；3：小组）

    @Column(length = 1, nullable = false)
    private String grade;    // 机构等级（1：一级；2：二级；3：三级；4：四级）

    @Column
    private String address; // 联系地址

    @Column(length = 100)
    private String zipCode; // 邮政编码

    @Column(length = 100)
    private String master;    // 负责人

    @Column(length = 200)
    private String phone;    // 电话

    @Column(length = 200)
    private String fax;    // 传真

    @Column(length = 200)
    private String email;    // 邮箱

    @Column(length = 1)
    private String useable;//是否可用

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn
    private User primaryPerson;//主负责人

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn
    private User deputyPerson;//副负责人

    @Transient
    @JSONField(serialize = false)
    private List<String> childDeptList;//快速添加子部门

    @Transient
    private String typeLabel;

    public Office() {
        super();
        this.type = "2";
    }

    public Office(String id) {
        super(id);
    }

    public Office(String id, String pid, String name) {
        this(id);
        if (StringUtils.isNotBlank(pid)) {
            this.parent = new Office(pid);
        }
        this.name = name;
    }

    public Office(String id, String pid, String name, String parentIds, String type) {
        this(id, pid, name);
        this.parentIds = parentIds;
        this.type = type;
    }

    public Office(String id, String pid, String name, String code, String type, Integer sort, String useable, String remarks) {
        this(id, pid, name);
        this.code = code;
        this.type = type;
        this.sort = sort;
        this.useable = useable;
        this.remarks = remarks;
    }

    @Override
    public Office getParent() {
        return parent;
    }

    @Override
    public void setParent(Office parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public User getPrimaryPerson() {
        return primaryPerson;
    }

    public void setPrimaryPerson(User primaryPerson) {
        this.primaryPerson = primaryPerson;
    }

    public User getDeputyPerson() {
        return deputyPerson;
    }

    public void setDeputyPerson(User deputyPerson) {
        this.deputyPerson = deputyPerson;
    }

    public List<String> getChildDeptList() {
        return childDeptList;
    }

    public void setChildDeptList(List<String> childDeptList) {
        this.childDeptList = childDeptList;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getIcon() {
        switch (type) {
            case "1":
                return "fa fa-sitemap yellow";
            case "2":
                return "fa fa-group bluelightplus";
            case "3":
                return "fa fa-user lightgreenplus";
            default:
                return "fa fa-";
        }
    }
}
