package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SkuLogisticChoice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description: SKU物流选择
 * @Author: jeecg-boot
 * @Date: 2021-10-07
 * @Version: V1.0
 */
@Repository
public interface SkuLogisticChoiceMapper extends BaseMapper<SkuLogisticChoice> {

    List<SkuLogisticChoice> findAllChoices(@Param("skuId") String skuId, @Param("countryCode") String countryCode);

    SkuLogisticChoice findLatestChoice(@Param("skuId") String skuId, @Param("countryCode") String countryCode);

    SkuLogisticChoice findClosestChoice(@Param("skuId") String skuId, @Param("countryCode") String countryCode, @Param("date") Date date);
}
