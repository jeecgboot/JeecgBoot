package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.SkuLogisticChoice;

import java.util.Date;
import java.util.List;

/**
 * @Description: SKU物流选择
 * @Author: jeecg-boot
 * @Date: 2021-10-07
 * @Version: V1.0
 */
public interface ISkuLogisticChoiceService extends IService<SkuLogisticChoice> {

    List<SkuLogisticChoice> findAllChoices(String skuId, String countryCode);

    SkuLogisticChoice findLatestChoice(String skuId, String countryCode);

    SkuLogisticChoice findClosestChoice(String skuId, String countryCode, Date date);
}
