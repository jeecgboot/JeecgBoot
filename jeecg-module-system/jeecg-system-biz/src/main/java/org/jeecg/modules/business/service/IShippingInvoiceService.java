package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.Period;

import java.io.Serializable;
import java.nio.file.Path;
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
     void saveMain(ShippingInvoice shippingInvoice);

    /**
     * 修改一对多
     */
     void updateMain(ShippingInvoice shippingInvoice);

    /**
     * 删除一对多
     */
     void delMain(String id);

    /**
     * 批量删除一对多
     */
    void delBatchMain(Collection<? extends Serializable> idList);
     String getShippingInvoiceId(String invoiceNumber);
     ShippingInvoice getShippingInvoice(String invoiceNumber);
     List<PlatformOrder> getPlatformOrder(String invoiceNumber);
     List<PlatformOrderContent> getPlatformOrderContent(String platformOrderId);
     Client getShopOwnerFromInvoiceNumber(String invoiceNumber);
    Currency getInvoiceCurrencyByCode(String invoiceCode);

    // Utils
     List<Path> getPath(String dirPath, String invoiceNumber);
     List<Path> getPath(String dirPath, String invoiceNumber, String invoiceEntity);
    boolean deleteAttachmentFile(String filename);

    void setPaid(List<String> invoiceNumbers);

    Period getInvoicePeriod(List<String> shopIdList);

    void cancelInvoice(String invoiceId);

    String findLatestInvoiceNumber();

    String findLatestCompleteInvoiceNumber();
}
