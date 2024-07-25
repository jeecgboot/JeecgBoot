package com.exam.configuration.spring.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;


/**
 * @version 3.5.0
 * @description: 记住我，Cookie
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class RestTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
    /**
     * Instantiates a new Rest token based remember me services.
     *
     * @param key                the key
     * @param userDetailsService the user details service
     */
    public RestTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return (boolean) request.getAttribute(DEFAULT_PARAMETER);
    }

}
