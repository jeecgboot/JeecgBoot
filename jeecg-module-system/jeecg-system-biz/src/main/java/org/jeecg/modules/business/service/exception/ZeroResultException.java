package org.jeecg.modules.business.service.exception;

public class ZeroResultException extends Exception {
    public ZeroResultException(String message, Object... args) {
        super(String.format(message, args));
    }
}
