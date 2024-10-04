package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuCriteriaValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: SKU criteria value
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
public interface ISkuCriteriaValueService extends IService<SkuCriteriaValue> {

    List<SkuCriteriaValue> listByCriteria(String criteriaId);

    String translateValueByCriteria(String criteria, String field, String value);
}
