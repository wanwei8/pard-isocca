package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.persistence.BaseEntity;
import com.pard.common.persistence.DataEntity;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 日志Entity
 *
 * @author Wawe
 * @version 2017-4-22
 */
@Entity
@Table(name = "sys_log")
public class Log extends BaseEntity<Log> {
    /**
     * 日志类型 接入日志
     */
    public static final String TYPE_ACCESS = "1";
    /**
     * 日志类型 错误日志
     */
    public static final String TYPE_EXCEPTION = "2";

    private static final long serialVersionUID = 1L;
    /**
     * 日志类型
     */
    @Column(length = 1)
    private String type = TYPE_ACCESS;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 创建者
     */
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(updatable = false)
    private User createBy;

    /**
     * 创建时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date createDate;

    /**
     * 操作用户的IP地址
     */
    private String remoteAddr;

    /**
     * 操作的URI
     */
    private String requestUri;

    /**
     * 操作的方式
     */
    @Column(length = 5)
    private String method;

    /**
     * 操作提交的数据
     */
    @Lob
    private String params;

    /**
     * 操作用户代理信息
     */
    private String userAgent;

    /**
     * 异常信息
     */
    @Lob
    private String exception;

    public Log() {
        super();
    }

    public Log(String id) {
        super(id);
    }

    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord) {
            setId(IdGen.uuid());
        }
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.createBy = user;
        }
        this.createDate = new Date();
    }

    @Override
    public void preUpdate() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setParams(Map paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
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

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
