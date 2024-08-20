package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SkuWeight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
@Repository
public interface SkuWeightMapper extends BaseMapper<SkuWeight> {
    SkuWeight getBySkuId(@Param("skuId") String skuId);

    String searchFirstEmptyWeightSku(@Param("skuIds") List<String> skuIds);
}
