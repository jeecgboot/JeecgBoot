package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "UnpairedSku", description = "Unpaired SKU")
public class UnpairedSku {
    private String stockSku;
    private String platformOrderId;
}
