package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.codeGeneration.CompleteInvoiceCodeRule;
import org.jeecg.modules.business.domain.codeGeneration.CreditInvoiceCodeRule;
import org.jeecg.modules.business.domain.codeGeneration.PurchaseInvoiceCodeRule;
import org.jeecg.modules.business.domain.codeGeneration.ShippingInvoiceCodeRule;
import org.jeecg.modules.business.entity.InvoiceNumberReservation;
import org.jeecg.modules.business.mapper.CreditMapper;
import org.jeecg.modules.business.mapper.InvoiceNumberReservationMapper;
import org.jeecg.modules.business.mapper.PurchaseOrderMapper;
import org.jeecg.modules.business.service.IInvoiceNumberReservationService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.entity.Invoice.InvoiceType;
import org.jeecg.modules.business.service.IShippingInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * @Description: invoice number reservation
 * @Author: jeecg-boot
 * @Date:   2025-03-28
 * @Version: V1.0
 */
@Service
@Slf4j
public class InvoiceNumberReservationServiceImpl extends ServiceImpl<InvoiceNumberReservationMapper, InvoiceNumberReservation> implements IInvoiceNumberReservationService {
    @Autowired
    private CreditMapper creditMapper;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private IShippingInvoiceService shippingInvoiceService;

    /**
     * Generates a new invoice number based on the current date and the specified invoice type.
     * This method will create a new invoice number reservation by committing the transaction regardless of the current transaction state of the caller.
     * @param invoiceType 1: Purchase Invoice, 2: Shipping Invoice, 7: Complete Invoice, 8: Credit Invoice
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String getLatestInvoiceNumberByType(int invoiceType) {
        String latestInvoiceCode, newInvoiceCode;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String yearStr = String.valueOf(year);
        String monthStr = String.format("%02d", month);
        int newNumber;
        InvoiceNumberReservation latestInvoiceNumberReservation = this.getOne(
                new QueryWrapper<InvoiceNumberReservation>().eq("type", invoiceType));
        if (latestInvoiceNumberReservation != null) {
            if(latestInvoiceNumberReservation.getYear().equals(yearStr) && latestInvoiceNumberReservation.getMonth().equals(monthStr)) {
                newNumber = latestInvoiceNumberReservation.getNumber() + 1;
            } else {
                newNumber = 1;
            }
            newInvoiceCode = yearStr + "-" + monthStr + "-" + invoiceType + String.format("%03d", newNumber);

            latestInvoiceNumberReservation.setNumber(newNumber);
            latestInvoiceNumberReservation.setYear(yearStr);
            latestInvoiceNumberReservation.setMonth(monthStr);
            update(latestInvoiceNumberReservation, new QueryWrapper<InvoiceNumberReservation>().eq("type", invoiceType));
        } else {
            if(invoiceType == InvoiceType.SHIPPING.getType()) {
                latestInvoiceCode = shippingInvoiceService.findLatestInvoiceNumber();
                newInvoiceCode = new ShippingInvoiceCodeRule().next(latestInvoiceCode);
            } else if(invoiceType == InvoiceType.COMPLETE.getType()) {
                latestInvoiceCode = shippingInvoiceService.findLatestCompleteInvoiceNumber();
                newInvoiceCode = new CompleteInvoiceCodeRule().next(latestInvoiceCode);
            } else if(invoiceType == InvoiceType.PURCHASE.getType()) {
                latestInvoiceCode = purchaseOrderMapper.lastInvoiceNumber();
                newInvoiceCode = new PurchaseInvoiceCodeRule().next(latestInvoiceCode);
            } else if(invoiceType == InvoiceType.CREDIT.getType()) {
                latestInvoiceCode = creditMapper.getLatestInvoiceNumber();
                newInvoiceCode = new CreditInvoiceCodeRule().next(latestInvoiceCode);
            }
            else {
                throw new IllegalArgumentException("Invalid invoice type: " + invoiceType);

            }
            newNumber = Integer.parseInt(newInvoiceCode.substring(newInvoiceCode.lastIndexOf("-") + 2));
            InvoiceNumberReservation newNumberReservation = new InvoiceNumberReservation();
            newNumberReservation.setNumber(newNumber);
            newNumberReservation.setType(invoiceType);
            newNumberReservation.setYear(yearStr);
            newNumberReservation.setMonth(monthStr);

            save(newNumberReservation);
        }
        return newInvoiceCode;
    }
}
