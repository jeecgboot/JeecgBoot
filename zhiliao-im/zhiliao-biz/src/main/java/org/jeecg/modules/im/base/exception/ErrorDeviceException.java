package org.jeecg.modules.im.base.exception;
public class ErrorDeviceException extends BusinessException{
    public ErrorDeviceException() {
        super();
    }

    public ErrorDeviceException(String message) {
        super(message);
    }

    public ErrorDeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}
