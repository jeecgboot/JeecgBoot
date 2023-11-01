package org.jeecg.modules.im.base.exception;
public class NoPermissionException extends BusinessException{
    public NoPermissionException() {
        super();
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
