package org.jeecg.modules.im.base.exception;
public class UploadFileException extends BusinessException{
    public UploadFileException() {
        super();
    }

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
