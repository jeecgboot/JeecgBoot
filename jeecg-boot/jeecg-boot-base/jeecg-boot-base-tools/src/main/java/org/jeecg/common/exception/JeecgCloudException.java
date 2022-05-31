package org.jeecg.common.exception;

/**
 * @Description: jeecg-cloud自定义异常
 * @Author: zyf
 * @Date: 2022-05-30
 */
public class JeecgCloudException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JeecgCloudException(String message) {
        super(message);
    }

}
