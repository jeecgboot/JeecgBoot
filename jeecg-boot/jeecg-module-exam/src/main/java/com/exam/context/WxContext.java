package com.exam.context;

import com.exam.domain.User;
import com.exam.domain.UserToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @version 3.3.0
 * @description: The enum System code.
 * Copyright (C), 2020-2024
 * @date 2021/5/25 10:45
 */
@Component
public class WxContext {

    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
    private static final String USER_TOKEN_ATTRIBUTES = "USER_TOKEN_ATTRIBUTES";


    /**
     * Sets context.
     *
     * @param user      the user
     * @param userToken the user token
     */
    public void setContext(User user, UserToken userToken) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_TOKEN_ATTRIBUTES, userToken, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Gets current user token.
     *
     * @return the current user token
     */
    public UserToken getCurrentUserToken() {
        return (UserToken) RequestContextHolder.currentRequestAttributes().getAttribute(USER_TOKEN_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }
}
