package org.jeecg.common.util.security.entity;

import lombok.Data;

/**
 * @Description: SecuritySignResp
 * @author: jeecg-boot
 */
@Data
public class SecuritySignResp {
    private String data;
    private String signData;
    private String aesKey;
}
