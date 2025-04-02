package org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UpdateResult {
    @JSONField(name = "platformOrderId")
    private String platformOrderId;
    @JSONField(name = "platformOrderNumber")
    private String platformOrderNumber;
    @JSONField(name = "reason")
    private String reason;
}