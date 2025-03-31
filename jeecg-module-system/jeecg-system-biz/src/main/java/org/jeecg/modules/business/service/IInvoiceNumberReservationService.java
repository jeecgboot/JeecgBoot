package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.InvoiceNumberReservation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: invoice number reservation
 * @Author: jeecg-boot
 * @Date:   2025-03-28
 * @Version: V1.0
 */
public interface IInvoiceNumberReservationService extends IService<InvoiceNumberReservation> {

    @Transactional
    String getLatestInvoiceNumberByType(int invoiceType);
}
