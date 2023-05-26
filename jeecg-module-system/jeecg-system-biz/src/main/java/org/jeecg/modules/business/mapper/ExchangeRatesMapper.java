package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ExchangeRates;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @Description: 汇率表
 * @Author: jeecg-boot
 * @Date: 2021-06-26
 * @Version: V1.0
 */
@Repository
public interface ExchangeRatesMapper extends BaseMapper<ExchangeRates> {

    BigDecimal getLatestExchangeRate(@Param("original") String from, @Param("target") String to);
}
