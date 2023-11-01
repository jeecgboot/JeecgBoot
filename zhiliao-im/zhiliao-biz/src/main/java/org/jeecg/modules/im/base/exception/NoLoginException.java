package org.jeecg.modules.im.base.exception;
public class NoLoginException extends BusinessException{
    public NoLoginException() {
        super();
    }

    public NoLoginException(String message) {
        super(message);
    }

    public NoLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
