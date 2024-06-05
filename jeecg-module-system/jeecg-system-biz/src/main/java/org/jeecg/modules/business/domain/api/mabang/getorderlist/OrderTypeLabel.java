package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class OrderTypeLabel {
    @JSONField(name = "typeId")
    private String typeId;
    @JSONField(name = "name")
    private String name;
}
