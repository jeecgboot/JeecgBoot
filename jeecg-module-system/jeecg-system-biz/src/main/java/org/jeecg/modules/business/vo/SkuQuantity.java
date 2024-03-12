package org.jeecg.modules.business.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SkuQuantity {
    @JSONField(name = "id")
    private final String ID;
    @JSONField(name = "erpCode")
    private final String erpCode;
    @JSONField(name = "quantity")
    private final Integer quantity;

    public SkuQuantity(String ID, String erpCode, Integer quantity){
        this.ID = ID;
        this.erpCode = erpCode;
        this.quantity = quantity;
    }
    public SkuQuantity(String ID, Integer quantity){
        this.ID = ID;
        this.quantity = quantity;
        this.erpCode = null;
    }
    @Override
    public String toString(){
        return String.format("|Sku ID %s -- %d|", ID, quantity);
    }
}
