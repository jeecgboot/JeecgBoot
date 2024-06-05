package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class OrderTypeNew {
    @JSONField(name = "normalLabels")
    private OrderTypeLabel[] normalLabels;
    @JSONField(name = "abnormalLabels")
    private OrderTypeLabel[] abnormalLabels;
}
