package org.jeecg.common.exception;

import lombok.Data;

/**
 * @author kezhijie@wuhandsj.com
 * @date 2024/1/2 11:38
 */
@Data
public class JeecgCaptchaException extends RuntimeException{

    private Integer code;

    private static final long serialVersionUID = -9093410345065209053L;

    public JeecgCaptchaException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public JeecgCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public JeecgCaptchaException(Throwable cause) {
        super(cause);
    }
}
