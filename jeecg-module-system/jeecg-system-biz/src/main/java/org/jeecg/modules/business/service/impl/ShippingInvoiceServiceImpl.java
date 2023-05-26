package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.ShippingInvoice;
import org.jeecg.modules.business.mapper.ShippingInvoiceMapper;
import org.jeecg.modules.business.service.IShippingInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@Service
@Slf4j
public class ShippingInvoiceServiceImpl extends ServiceImpl<ShippingInvoiceMapper, ShippingInvoice> implements IShippingInvoiceService {

    @Autowired
    private ShippingInvoiceMapper shippingInvoiceMapper;

    @Autowired
    public ShippingInvoiceServiceImpl(ShippingInvoiceMapper shippingInvoiceMapper) {
        this.shippingInvoiceMapper = shippingInvoiceMapper;
    }

    @Override
    @Transactional
    public void saveMain(ShippingInvoice shippingInvoice) {
        shippingInvoiceMapper.insert(shippingInvoice);
    }

    @Override
    @Transactional
    public void updateMain(ShippingInvoice shippingInvoice) {
        shippingInvoiceMapper.updateById(shippingInvoice);

        //1.先删除子表数据

        //2.子表数据重新插入
    }

    @Override
    @Transactional
    public void delMain(String id) {
        shippingInvoiceMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            shippingInvoiceMapper.deleteById(id);
        }
    }

    @Override
    @Transactional
    public Client getShopOwnerFromInvoiceNumber(String invoiceNumber) {
        return shippingInvoiceMapper.fetchShopOwnerFromInvoiceNumber(invoiceNumber);
    }

    @Override
    @Transactional
    public String getShippingInvoiceNumber(String invoiceID) {
        return shippingInvoiceMapper.fetchShippingInvoiceNumber(invoiceID);
    }
    @Override
    @Transactional
    public ShippingInvoice getShippingInvoice(String invoiceNumber) {
        return shippingInvoiceMapper.fetchShippingInvoice(invoiceNumber);
    }

    @Override
    @Transactional
    public List<PlatformOrder> getPlatformOrder(String invoiceNumber) {
        return shippingInvoiceMapper.fetchPlatformOrder(invoiceNumber);
    }

    @Override
    @Transactional
    public List<PlatformOrderContent> getPlatformOrderContent(String platformOrderId) {
        return shippingInvoiceMapper.fetchPlatformOrderContent(platformOrderId);
    }

}
