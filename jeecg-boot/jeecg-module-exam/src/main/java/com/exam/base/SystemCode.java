package com.exam.base;

/**
 * @version 3.3.0
 * @description: The enum System code.
 * Copyright (C), 2020-2024
 * @date 2021/5/25 10:45
 */
public enum SystemCode {
    /**
     * OK
     */
    OK(1, "成功"),
    /**
     * AccessTokenError
     */
    AccessTokenError(400, "用户登录令牌失效"),
    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, "用户未登录"),
    /**
     * UNAUTHORIZED
     */
    AuthError(402, "用户名或密码错误"),
    /**
     * InnerError
     */
    InnerError(500, "系统内部错误"),
    /**
     * ParameterValidError
     */
    ParameterValidError(501, "参数验证错误"),

    /**
     * AccessDenied
     */
    AccessDenied(502, "用户没有权限访问");

    /**
     * The Code.
     */
    int code;
    /**
     * The Message.
     */
    String message;

    SystemCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
