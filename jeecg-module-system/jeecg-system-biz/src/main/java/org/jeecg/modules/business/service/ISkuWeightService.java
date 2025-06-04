package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuWeight;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.SkuWeightPage;

import java.util.List;

/**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
public interface ISkuWeightService extends IService<SkuWeight> {
    SkuWeight getBySkuId(String skuId);

    List<SkuWeight> findBySkuId(String skuId);

    String searchFirstEmptyWeightSku(List<String> skuIds);

    List<SkuWeight> exportToExcel(List<String> skuIds);
    /**
     * used to export all latest weights for front, so instead of fetching skuId, we fetch erpCode
     * @return
     */
    List<SkuWeightPage> listLatestWeights();

    List<SkuWeightPage> listLatestWeightForSkus(List<String> skuIds);
}
