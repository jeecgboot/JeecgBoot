package org.jeecg.modules.im.base.exception;

public class LockedException extends BusinessException{
    public LockedException() {
        super();
    }

    public LockedException(String message) {
        super(message);
    }

    public LockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
