package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class SkuQuantity {
    private final String ID;
    private final Integer quantity;

    @Override
    public String toString(){
        return String.format("|Sku ID %s -- %d|", ID, quantity);
    }
}
