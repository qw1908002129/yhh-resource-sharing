package com.yhh.common.result;

/**
 * 返回结果枚举
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    USER_NOT_EXIST(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USERNAME_EXISTS(1003, "用户名已存在"),
    EMAIL_EXISTS(1004, "邮箱已存在"),
    PHONE_EXISTS(1005, "手机号已存在"),
    RESOURCE_NOT_EXIST(2001, "资源不存在"),
    COMMENT_NOT_EXIST(3001, "评论不存在"),
    CATEGORY_NOT_EXIST(4001, "分类不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 