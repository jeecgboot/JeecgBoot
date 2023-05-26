package org.jeecg.modules.business.service.exception;

public class MultipleMatchException extends Exception {
    public MultipleMatchException(String message, Object... args) {
        super(String.format(message, args));
    }
}
