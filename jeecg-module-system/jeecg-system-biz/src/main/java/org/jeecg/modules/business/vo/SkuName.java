package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sku_product_name")
public class SkuName {
    private final String id;
    private final String erpCode;
    private final String zhName;
    private final String enName;

    public String getErpCode() {
        if (erpCode == null) {
            return "no erp code";
        }
        return erpCode;
    }
}
