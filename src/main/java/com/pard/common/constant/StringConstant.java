package com.pard.common.constant;

/**
 * Created by wawe on 17/6/4.
 */
public interface StringConstant {
    /**
     * 显示／隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是／否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对／错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 账户正常
     */
    public static final String ACCOUNT_NORMAL = "0";
    /**
     * 账户被锁定
     */
    public static final String ACCOUNT_LOCKED = "1";
    /**
     * 账户不可用
     */
    public static final String ACCOUNT_DISABLED = "2";

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "/userfiles/";

    /**
     * session验证码
     */
    public static final String SESSION_VALIDATE_CODE = "session-validate-code";
}
