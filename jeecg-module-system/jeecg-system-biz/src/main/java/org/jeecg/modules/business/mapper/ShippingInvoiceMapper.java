package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.ShippingInvoice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@Repository
public interface ShippingInvoiceMapper extends BaseMapper<ShippingInvoice> {
    String fetchShippingInvoiceNumber(@Param("invoiceID") String invoiceID);
    ShippingInvoice fetchShippingInvoice(@Param("invoiceNumber") String invoiceNumber);
    List<PlatformOrder> fetchPlatformOrder(@Param("invoiceNumber") String invoiceNumber);
    List<PlatformOrderContent> fetchPlatformOrderContent(@Param("platformOrderId") String platformOrderId);
    Client fetchShopOwnerFromInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
}
