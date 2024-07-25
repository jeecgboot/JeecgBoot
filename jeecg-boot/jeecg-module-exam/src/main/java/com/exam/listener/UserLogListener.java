package com.exam.listener;

import com.exam.event.UserEvent;
import com.exam.service.UserEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @version 3.5.0
 * @description: The type User log listener.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
@Component
public class UserLogListener implements ApplicationListener<UserEvent> {

    private final UserEventLogService userEventLogService;

    /**
     * Instantiates a new User log listener.
     *
     * @param userEventLogService the user event log service
     */
    @Autowired
    public UserLogListener(UserEventLogService userEventLogService) {
        this.userEventLogService = userEventLogService;
    }

    @Override
    public void onApplicationEvent(UserEvent userEvent) {
        userEventLogService.insertByFilter(userEvent.getUserEventLog());
    }

}
