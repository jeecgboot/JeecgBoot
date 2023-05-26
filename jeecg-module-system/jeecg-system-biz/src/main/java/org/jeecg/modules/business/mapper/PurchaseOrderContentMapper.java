package org.jeecg.modules.business.mapper;

import java.util.List;

import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoiceEntry;
import org.jeecg.modules.business.entity.PurchaseOrderSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.vo.OrderContentEntry;
import org.springframework.stereotype.Repository;

/**
 * @Description: 商品采购订单SKU
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Repository
public interface PurchaseOrderContentMapper extends BaseMapper<PurchaseOrderSku> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<PurchaseOrderSku> selectByMainId(@Param("mainId") String mainId);

    void addAll(@Param("creator") String creator, @Param("purchaseID") String purchaseID, @Param("data") List<OrderContentEntry> orderContent);

    List<PurchaseInvoiceEntry> selectInvoiceDataByID(@Param("purchaseID") String purchaseID);
}
