package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.vo.InvoiceAmountDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceMapper extends BaseMapper<Invoice> {
    int findEarliestInvoiceYear(@Param("clientId") String clientId, @Param("currency") String currency);
    InvoiceAmountDTO getInvoiceAmounts(@Param("invoiceNumber") String invoiceNumber);
}
