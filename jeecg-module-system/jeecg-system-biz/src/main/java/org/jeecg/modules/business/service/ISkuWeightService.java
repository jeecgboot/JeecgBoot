package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuWeight;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
public interface ISkuWeightService extends IService<SkuWeight> {
    SkuWeight getBySkuId(String skuId);

    String searchFirstEmptyWeightSku(List<String> skuIds);
}
