package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LogisticInsurance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: logistic insurance
 * @Author: jeecg-boot
 * @Date:   2025-03-04
 * @Version: V1.0
 */
@Repository
public interface LogisticInsuranceMapper extends BaseMapper<LogisticInsurance> {

    List<LogisticInsurance> getInsuranceFeesBySkuIds(@Param("skuIds") List<String> skuIds, @Param("logisticChannelId")String logisticChannelId);
}
