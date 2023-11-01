package org.jeecg.modules.im.base.exception;
public class NoBindingException extends BusinessException{
    public NoBindingException() {
        super();
    }

    public NoBindingException(String message) {
        super(message);
    }

    public NoBindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
