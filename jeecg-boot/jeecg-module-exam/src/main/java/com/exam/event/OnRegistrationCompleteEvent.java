package com.exam.event;

import com.exam.domain.User;
import org.springframework.context.ApplicationEvent;

/**
 * @version 3.5.0
 * @description: The type On registration complete event.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {


    private final User user;


    /**
     * Instantiates a new On registration complete event.
     *
     * @param user the user
     */
    public OnRegistrationCompleteEvent(final User user) {
        super(user);
        this.user = user;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

}