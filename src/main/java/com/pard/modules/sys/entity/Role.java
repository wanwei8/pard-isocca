package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.pard.common.constant.StringConstant;
import com.pard.common.persistence.DataEntity;
import com.pard.common.utils.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by wawe on 17/6/12.
 */
@Entity
@Table(name = "sys_role")
public class Role extends DataEntity<Role> {

    /**
     * 角色名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 数据范围 1：所有数据；2：所在单位及以下数据；3：所在单位数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9按明细设置
     */
    @Column(nullable = false, length = 1)
    @JSONField(serialize = false)
    private String dataScope;

    /**
     * 是否是可用
     */
    @Column(nullable = false, length = 1)
    private String useable;

    /**
     * 归属单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Office company;

    /**
     * 归属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Office office;

    /**
     * 角色所含用户
     */
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "sys_role_user", joinColumns = @JoinColumn(name = "role_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false))
    @JSONField(serialize = false)
    private List<User> userList = Lists.newArrayList();

    /**
     * 角色的对应的功能，一个角色可有多个功能
     */
    @ManyToMany
    @JoinTable(name = "sys_role_menu", joinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "menu_id", nullable = false, updatable = false))
    @JSONField(serialize = false)
    private List<Menu> menuList = Lists.newArrayList();

    /**
     * 角色的数据范围对应的机构，一个角色可有查看多个机构的数据
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_office", joinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "office_id", nullable = false, updatable = false))
    @JSONField(serialize = false)
    private List<Office> officeList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public String getCompanyId() {
        if (company != null) {
            return company.getId();
        }
        return "";
    }

    public String getOfficeId() {
        if (office != null) {
            return office.getId();
        }
        return "";
    }

    public String getCompanyName() {
        if (company != null) {
            return company.getName();
        }
        return "";
    }

    public String getOfficeName() {
        if (office != null) {
            return office.getName();
        }
        return "";
    }

    public Role() {
        super();
        name = "";
        useable = StringConstant.YES;
    }

    public Role(String id) {
        this();
        this.id = id;
    }

    public Role(String id, String name, String useable, String companyId, String companyName, String officeId, String officeName) {
        this(id);
        this.name = name;
        this.useable = useable;
        if (StringUtils.isNotBlank(companyId)) {
            this.company = new Office(companyId, companyName);
        }
        if (StringUtils.isNotBlank(officeId)) {
            this.office = new Office(officeId, officeName);
        }
    }
}
