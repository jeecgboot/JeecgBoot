package org.jeecg.modules.business.domain.api.shouman;

import lombok.Data;

@Data
public class OrderStatusResponse {

    private Integer status;
    private OrderStatusResultBody resultBody;
}
