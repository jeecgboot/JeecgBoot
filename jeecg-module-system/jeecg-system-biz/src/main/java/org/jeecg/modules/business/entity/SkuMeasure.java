package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sku_weight_volume")
public class SkuMeasure {
    private final String id;

    private final Integer weight;

    private final Integer volume;

    public Integer getVolume() {
        if (null == volume)
            return 0;
        return volume;
    }

    public Integer getWeight() {
        if (null == weight)
            return 0;
        return weight;
    }
}
