package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ExtraFee;
import org.jeecg.modules.business.mapper.ExtraFeeMapper;
import org.jeecg.modules.business.service.IExtraFeeService;
import org.jeecg.modules.business.vo.ExtraFeeParam;
import org.jeecg.modules.business.vo.ExtraFeeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: extra fees content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Service
public class ExtraFeeServiceImpl extends ServiceImpl<ExtraFeeMapper, ExtraFee> implements IExtraFeeService {
    @Autowired
    private ExtraFeeMapper extraFeeMapper;

    @Override
    public List<ExtraFeeResult> listWithFilters(String shop, String status, Integer pageNo, Integer pageSize, String column, String order) {
        int offset = (pageNo - 1) * pageSize;
        return extraFeeMapper.listWithFilters(shop, status, offset, pageSize, column, order);
    }
    @Override
    public Integer countAllFees(String shop, String status) {
        return extraFeeMapper.countAllFees(shop, status);
    }

    @Override
    public void updateFee(ExtraFeeParam feeParam) throws Exception {
        ExtraFee fee = getById(feeParam.getId());
        if(fee == null) {
            throw new Exception("Fee not found");
        }
        extraFeeMapper.updateFee(feeParam.getId(), feeParam.getDescription(), feeParam.getQuantity(), feeParam.getUnitPrice());
    }

    @Override
    public List<ExtraFeeResult> findNotInvoicedByShops(List<String> shopIds) {
        return extraFeeMapper.findNotInvoicedByShops(shopIds);
    }

    @Override
    public void updateInvoiceNumberByIds(List<String> feeIds, String invoiceCode) {
        extraFeeMapper.updateInvoiceNumberByIds(feeIds, invoiceCode);
    }

    @Override
    public List<ExtraFeeResult> findByInvoiceNumber(String invoiceCode) {
        return extraFeeMapper.findByInvoiceNumber(invoiceCode);
    }

    @Override
    public void cancelInvoice(String invoiceNumber, String clientId) {
        extraFeeMapper.cancelInvoice(invoiceNumber, clientId);
    }
}
