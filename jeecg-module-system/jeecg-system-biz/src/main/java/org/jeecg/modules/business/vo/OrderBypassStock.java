package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class OrderBypassStock {
    private String orderId;
    private Boolean isSelfIgnoreStock;
}
