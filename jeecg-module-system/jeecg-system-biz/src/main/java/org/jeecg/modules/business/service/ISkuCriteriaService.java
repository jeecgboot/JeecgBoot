package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuCriteria;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: SKU erpCode criteria
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
public interface ISkuCriteriaService extends IService<SkuCriteria> {

    List<SkuCriteria> listByCategoryCode(String categoryCode);
}
