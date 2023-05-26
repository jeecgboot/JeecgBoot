package org.jeecg.modules.business.controller;

public class UserException extends Exception {
    public UserException(String message, Object... args) {
        super(String.format(message, args));
    }

}
