package org.jeecg.modules.business.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PlatformOrderOption {
    /**
     * PlatformOrder.PlatformOrderNumber
     */
    @JSONField(name = "value")
    private String value;
    /**
     * PlatformOrder.PlatformOrderNumber
     */
    @JSONField(name = "label")
    private String label;
    /**
     * PlatformOrder.ErpStatus
     */
    @JSONField(name = "erp_status")
    private String erpStatus;
    /**
     * PlatformOrder.ErpStatus = 1 or 2 ? false : true
     */
    @JSONField(name = "disabled")
    private boolean disabled;
}
