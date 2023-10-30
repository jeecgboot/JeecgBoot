package org.jeecg.modules.business.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Repository
public interface BalanceMapper extends BaseMapper<Balance> {

    BigDecimal getBalanceByClientIdAndCurrency(@Param("clientId")String clientId, @Param("currency")String currency);

    void deleteBalance(@Param("operationId") String operationId, @Param("operationType") String operationType);

    void deleteBatchBalance(@Param("operationIds") List<String> operationIds, @Param("operationType") String operationType);

    void editBalance(@Param("operationId") String operationId,
                     @Param("operationType") String operationType,
                     @Param("username") String username,
                     @Param("clientId") String clientId,
                     @Param("amount") BigDecimal amount,
                     @Param("currencyId") String currencyId);
}
