package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class UserSku {
    private final String userId;
    private final String skuId;
    private final String erpCode;
    private final String zhName;
    private final String enName;
}
