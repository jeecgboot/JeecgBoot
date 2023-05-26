package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SavRefund;
import org.jeecg.modules.business.entity.SavRefundWithDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-16
 * @Version: V1.0
 */
@Repository
public interface SavRefundWithDetailMapper extends BaseMapper<SavRefundWithDetail> {
    List<SavRefundWithDetail> findUnprocessedRefundsByClient(@Param("clientId") String clientId);
}
