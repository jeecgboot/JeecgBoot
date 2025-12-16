package org.jeecg.modules.business.vo.clientPlatformOrder;

import lombok.Data;

@Data
public class PendingOrderVO {
    private String platformOrderId;
    private String shopName;
    private String clientInternalCode;
    private long waitingDays;
}