package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.vo.InvoiceKpi;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@Repository
public interface ShippingInvoiceMapper extends BaseMapper<ShippingInvoice> {
    String fetchShippingInvoiceId(@Param("invoiceNumber") String invoiceNumber);
    ShippingInvoice fetchShippingInvoice(@Param("invoiceNumber") String invoiceNumber);
    List<PlatformOrder> fetchPlatformOrder(@Param("invoiceNumber") String invoiceNumber);
    List<PlatformOrderContent> fetchPlatformOrderContent(@Param("platformOrderId") String platformOrderId);
    Client fetchShopOwnerFromInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
    Currency fetchInvoiceCurrencyByCode(@Param("invoiceNumber") String invoiceCode);
    InvoiceKpi countShippingInvoices(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("showAllData") boolean showAllData, @Param("username") String username);
    void setPaid(@Param("invoiceNumbers") List<String> invoiceNumbers);
    Client getClientByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
}
