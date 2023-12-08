package org.jeecg.modules.business.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ClientCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: client category
 * @Author: jeecg-boot
 * @Date:   2023-10-19
 * @Version: V1.0
 */
@Repository
public interface ClientCategoryMapper extends BaseMapper<ClientCategory> {
    BigDecimal getBalanceThresholdByCategoryId(@Param("id") String id);

    String getClientCategoryByClientId(@Param("clientId") String clientId);
}
