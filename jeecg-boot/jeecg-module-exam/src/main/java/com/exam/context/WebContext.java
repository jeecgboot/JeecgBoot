package com.exam.context;

import com.exam.domain.User;
import com.exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @version 3.3.0
 * @description: The enum System code.
 * Copyright (C), 2020-2024
 * @date 2021/5/25 10:45
 */
@Slf4j
@Component
public class WebContext {
    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
    private final UserService userService;

    /**
     * Instantiates a new Web context.
     *
     * @param userService the user service
     */
    @Autowired
    public WebContext(UserService userService) {
        this.userService = userService;
    }


    /**
     * Sets current user.
     *
     * @param user the user
     */
    public void setCurrentUser(User user) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        if (null != user) {
            return user;
        } else {
            Object springUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null == springUser) {
                return null;
            }
            log.info("getCurrentUser: {}",springUser);
            try {
                if (springUser instanceof UserDetails) {
                    user = userService.getUserByUserName(((org.springframework.security.core.userdetails.User) springUser).getUsername());
                    if (null != user) {
                        setCurrentUser(user);
                    }
                }
            } catch (Exception e) {
                log.error("获取登录用户失败：",e);
            }
            return user;
        }
    }
}
