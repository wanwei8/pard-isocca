package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.pard.common.persistence.DataEntity;
import com.pard.common.utils.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/6/4.
 */
@Entity
@Table(name = "sys_user")
public class User extends DataEntity<User> {
    /**
     * 归属公司
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    @JSONField(serialize = false)
    private Office company;

    /**
     * 归属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    @JSONField(serialize = false)
    private Office office;

    /**
     * 登录名
     */
    @Column(length = 100, nullable = false)
    private String loginName;

    /**
     * 密码
     */
    @Column(length = 100, nullable = false)
    @JSONField(serialize = false)
    private String password;

    /**
     * 工号
     */
    @Column(length = 100)
    private String no;

    /**
     * 姓名
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * 邮箱
     */
    @Column(length = 200)
    private String email;

    /**
     * 电话
     */
    @Column(length = 200)
    private String phone;

    /**
     * 手机
     */
    @Column(length = 200)
    private String mobile;

    /**
     * 传真
     */
    @Column(length = 200)
    private String fax;

    /**
     * 用户类型
     */
    @Column(length = 1)
    @JSONField(serialize = false)
    private String userType;

    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String photo;

    /**
     * 最后登录IP
     */
    @Column(length = 100)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date loginDate;

    /**
     * 是否可登录
     */
    @Column(length = 1, nullable = false)
    private String loginFlag;

    /**
     * 职务头衔
     */
    @Column(length = 50)
    private String duties;

    /**
     * 性别 0:女/1:男
     */
    @Column
    @JSONField(serialize = false)
    private Short gender;

    /**
     * 新密码
     */
    @Transient
    @JSONField(serialize = false)
    private String newPassword;

    /**
     * 原登录名
     */
    @Transient
    @JSONField(serialize = false)
    private String oldName;

    /**
     * 原照片
     */
    @Transient
    @JSONField(serialize = false)
    private String oldPhoto;

    /**
     * 用户所属角色，一个用户可以有多个角色
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userList")
    @JSONField(serialize = false)
    private List<Role> roleList = Lists.newArrayList();

    public User() {
        super();
        loginFlag = "0";
        gender = 1;
        company = new Office();
        office = new Office();
    }

    public User(String id) {
        this();
        this.id = id;
    }

    public User(String id, String name) {
        this(id);
        this.name = name;
    }

    public User(String id, String no, String loginName, String name, String loginFlag, String companyId, String companyName,
                String officeId, String officeName) {
        this(id);
        this.no = no;
        this.loginName = loginName;
        this.name = name;
        this.loginFlag = loginFlag;
        if (StringUtils.isNotBlank(companyId)) {
            this.company = new Office(companyId, companyName);
        }
        if (StringUtils.isNotBlank(officeId)) {
            this.office = new Office(officeId, officeName);
        }
    }

    public User(User user) {
        this.id = user.getId();
        this.company = user.getCompany();
        this.createBy = user.getCreateBy();
        this.createDate = user.getCreateDate();
        this.delFlag = user.getDelFlag();
        this.email = user.getEmail();
        this.loginDate = user.getLoginDate();
        this.loginFlag = user.getLoginFlag();
        this.loginIp = user.getLoginIp();
        this.loginDate = user.getLoginDate();
        this.loginName = user.getLoginName();
        this.mobile = user.getMobile();
        this.name = user.getName();
        this.no = user.getNo();
        this.office = user.getOffice();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.photo = user.getPhoto();
        this.remarks = user.getRemarks();
        this.userType = user.getUserType();
        this.updateBy = user.getUpdateBy();
        this.updateDate = user.getUpdateDate();
        this.duties = user.getDuties();
        this.gender = user.getGender();
    }

    public static boolean isAdmin(String id) {
        return StringUtils.repeat("0", 32).equals(id);
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
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
        return company != null ? company.getName() : "";
    }

    public String getOfficeName() {
        return office != null ? office.getName() : "";
    }

    public String getOldPhoto() {
        return oldPhoto;
    }

    public void setOldPhoto(String oldPhoto) {
        this.oldPhoto = oldPhoto;
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }
}
