package com.yhh.common.result;

import lombok.Data;

/**
 * 统一返回结果
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {}

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message);
    }

    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> Result<T> failed(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message);
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }

    public static <T> Result<T> forbidden() {
        return new Result<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
    }

    public static <T> Result<T> validateFailed() {
        return new Result<>(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage());
    }

    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ResultCode.VALIDATE_FAILED.getCode(), message);
    }
} 