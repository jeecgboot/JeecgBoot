package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SavRefund;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-12
 * @Version: V1.0
 */
@Repository
public interface SavRefundMapper extends BaseMapper<SavRefund> {
    List<BigDecimal> fetchRefundAmount(@Param("invoiceNumber") String invoiceNumber);
}
