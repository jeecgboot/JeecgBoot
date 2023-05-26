package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.ShippingInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
public interface IShippingInvoiceService extends IService<ShippingInvoice> {

    /**
     * 添加一对多
     */
    public void saveMain(ShippingInvoice shippingInvoice);

    /**
     * 修改一对多
     */
    public void updateMain(ShippingInvoice shippingInvoice);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);
    public String getShippingInvoiceNumber(String invoiceID);
    public ShippingInvoice getShippingInvoice(String invoiceNumber);
    public List<PlatformOrder> getPlatformOrder(String invoiceNumber);
    public List<PlatformOrderContent> getPlatformOrderContent(String platformOrderId);
    public Client getShopOwnerFromInvoiceNumber(String invoiceNumber);
}
