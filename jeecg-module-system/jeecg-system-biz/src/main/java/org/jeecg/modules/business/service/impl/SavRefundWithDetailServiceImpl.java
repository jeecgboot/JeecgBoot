package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.SavRefundWithDetail;
import org.jeecg.modules.business.mapper.SavRefundWithDetailMapper;
import org.jeecg.modules.business.service.ISavRefundWithDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-16
 * @Version: V1.0
 */
@Service
public class SavRefundWithDetailServiceImpl extends ServiceImpl<SavRefundWithDetailMapper, SavRefundWithDetail> implements ISavRefundWithDetailService {

    private final SavRefundWithDetailMapper savRefundMapper;
    @Autowired
    public SavRefundWithDetailServiceImpl(SavRefundWithDetailMapper savRefundMapper) {
        this.savRefundMapper = savRefundMapper;
    }

    public List<SavRefundWithDetail> findUnprocessedRefundsByClient(String clientId) {
        return savRefundMapper.findUnprocessedRefundsByClient(clientId);
    }
    @Override
    public List<SavRefundWithDetail> fetchRefundsWhere(String shop, String orderID, String invoiceNumber, String column, String order) {
        String invoiceCode = invoiceNumber;
        if(invoiceNumber.equals("%null%"))
            invoiceCode = "null";
        if(invoiceNumber.equals("%notNull%"))
            invoiceCode = "notNull";
        System.out.println("invoiceCode : "+invoiceCode);
        return savRefundMapper.fetchRefundsWhere(shop, orderID, invoiceCode, column, order);
    }

    @Override
    public List<SavRefundWithDetail> getRefundsByInvoiceNumber(String invoiceNumber) {
        return savRefundMapper.getRefundsByInvoiceNumber(invoiceNumber);
    }
}
