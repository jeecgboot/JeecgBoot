package com.exam.configuration.spring.security;

import com.exam.base.SystemCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 3.5.0
 * @description: 未登录
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
@Component
public final class LoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    /**
     * Instantiates a new Login authentication entry point.
     */
    public LoginAuthenticationEntryPoint() {
        super("/api/user/login");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        RestUtil.response(response, SystemCode.UNAUTHORIZED);
    }

}
