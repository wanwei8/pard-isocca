package com.pard.common.message;

/**
 * Created by wawe on 17/5/5.
 */
public enum ResultStatus {
    EXCEPTION(0, "发生异常"),
    SAVE_SUCCESS(100, "保存成功"),
    IS_DEMO_MODE(-1000, "演示模式，不允许操作"),
    USERNAME_OR_PASSWORD_ERROR(-1001, "用户名或密码错误"),
    SAVE_FAILD(-1002, "保存失败"),
    DELETE_FAILD(-1003, "删除失败"),
    NODE_HAS_CHILDREN_FAILD(-1004, "此节点下含有子节点"),
    USERNAME_EXISTS_FAILD(-1005, "用户名已存在"),
    PARAMETER_ERROR(-2001, "参数错误");

    private int code;

    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
