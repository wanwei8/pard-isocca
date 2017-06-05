package com.pard.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.pard.common.persistence.BaseEntity;
import com.pard.common.utils.DateTimeUtils;
import com.pard.common.utils.IdGen;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wawe on 17/5/31.
 */
@Entity
@Table(name = "logger_info")
public class LoggerInfo extends BaseEntity<LoggerInfo> {

    /**
     * 请求者ip
     */
    @Column(length = 50)
    private String clientIp;

    /**
     * 请求路径
     */
    @Column(length = 1000)
    private String requestUri;

    /**
     * 完整路径
     */
    @Column(length = 1000)
    private String requestUrl;

    /**
     * 对应方法，格式为 HTTP方法+Java方法 如：GET.list()
     */
    @Column(length = 50)
    private String requestMethod;

    /**
     * 响应结果
     */
    private String responseContext;

    /**
     * 用户信息
     */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @JSONField(serialize = false)
    private User user;

    /**
     * 请求时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date requestTime;

    /**
     * 响应时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date responseTime;

    /**
     * 请求耗时
     */
    private long useTime = 1;

    /**
     * referer信息
     */
    private String referer;

    /**
     * 客户端标识
     */
    private String userAgent;

    /**
     * 响应码
     */
    private String responseCode;

    /**
     * 请求头信息
     */
    private String requestHeader;

    /**
     * 对应类名
     */
    private String className;

    /**
     * 功能摘要
     */
    private String moduleDesc;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应异常
     */
    private String exceptionInfo;

    /**
     * 命中缓存
     */
    private String cacheKey;

    @Override
    public void preInsert() {
        if (isNewRecord) {
            setId(IdGen.uuid());
        }
    }

    @Override
    public void preUpdate() {

    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponseContext() {
        return responseContext;
    }

    public void setResponseContext(String responseContext) {
        this.responseContext = responseContext;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public long getUseTime() {
        if (useTime == 1)
            useTime = DateTimeUtils.compareMinsecond(getResponseTime(), getRequestTime());
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
