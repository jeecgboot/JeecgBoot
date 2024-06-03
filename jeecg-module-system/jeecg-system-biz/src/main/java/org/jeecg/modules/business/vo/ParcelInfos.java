package org.jeecg.modules.business.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ParcelInfos {
    @JSONField(name = "tracking_number")
    private String trackingNumbers;
    @JSONField(name = "platform_order_id")
    private String platformOrderIds;
    @JSONField(name = "status")
    private String status;
}
