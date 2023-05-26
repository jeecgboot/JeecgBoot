package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.SkuDeclaredValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: SKU申报价格
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.0
 */
public interface ISkuDeclaredValueService extends IService<SkuDeclaredValue> {

    List<SkuDeclaredValue> selectByMainId(String mainId);

    BigDecimal getDeclaredValueForDate(String skuId, Date date);

    List<SkuDeclaredValue> getLatestDeclaredValues();
}
