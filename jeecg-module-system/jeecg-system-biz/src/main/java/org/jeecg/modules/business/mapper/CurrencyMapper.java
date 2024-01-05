package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Currency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 货币
 * @Author: jeecg-boot
 * @Date:   2023-10-17
 * @Version: V1.0
 */
@Repository
public interface CurrencyMapper extends BaseMapper<Currency> {
    String getCodeById(@Param("currencyId") String currencyId);

    String getIdByCode(@Param("code") String code);
}
