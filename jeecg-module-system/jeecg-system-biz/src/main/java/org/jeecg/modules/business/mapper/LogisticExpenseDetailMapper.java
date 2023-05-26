package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LogisticExpenseDetail;
import org.jeecg.modules.business.vo.PlatformOrderLogisticExpenseDetail;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 物流开销明细
 * @Author: jeecg-boot
 * @Date: 2021-07-22
 * @Version: V1.0
 */
@Repository
public interface LogisticExpenseDetailMapper extends BaseMapper<LogisticExpenseDetail> {

    /**
     * Find expense details that corresponds to platform orders
     *
     * @param trackingNumbers tracking numbers of platform orders
     * @return list of expense details
     */
    List<LogisticExpenseDetail> findBy(@Param("trackingNumbers") Collection<String> trackingNumbers);

    List<BigDecimal> findExpenseByInvoiceCodes(@Param("codes") Collection<String> codes);

    List<PlatformOrderLogisticExpenseDetail> findBetween(@Param("start") Date start, @Param("end") Date stop,
                                                         @Param("country") List<String> country, @Param("channelName") List<String> channelName);


    void insertOrMerge(@Param("data") Collection<LogisticExpenseDetail> expenseDetail);
}
