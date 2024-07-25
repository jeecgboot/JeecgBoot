package com.exam.event;

import com.exam.domain.UserEventLog;
import org.springframework.context.ApplicationEvent;

/**
 * @version 3.5.0
 * @description: The type User event.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class UserEvent extends ApplicationEvent {

    private final UserEventLog userEventLog;

    /**
     * Instantiates a new User event.
     *
     * @param userEventLog the user event log
     */
    public UserEvent(final UserEventLog userEventLog) {
        super(userEventLog);
        this.userEventLog = userEventLog;
    }

    /**
     * Gets user event log.
     *
     * @return the user event log
     */
    public UserEventLog getUserEventLog() {
        return userEventLog;
    }
}
