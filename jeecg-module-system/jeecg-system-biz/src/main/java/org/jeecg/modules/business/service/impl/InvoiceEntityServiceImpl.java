package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.InvoiceEntity;
import org.jeecg.modules.business.mapper.InvoiceEntityMapper;
import org.jeecg.modules.business.service.IInvoiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceEntityServiceImpl extends ServiceImpl<InvoiceEntityMapper, InvoiceEntity> implements IInvoiceEntityService {

    @Autowired
    private InvoiceEntityMapper invoiceEntityMapper;

    @Override
    public List<InvoiceEntity> selectByMainId(String mainId) {
        return invoiceEntityMapper.selectByMainId(mainId);
    }

    @Override
    public List<InvoiceEntity> selectActiveByClientId(String clientId) {
        return invoiceEntityMapper.selectActiveByClientId(clientId);
    }

    @Override
    public boolean save(InvoiceEntity entity) {
        if ("1".equals(entity.getIsDefault()) && entity.getClientId() != null) {
            invoiceEntityMapper.clearDefaultByClientId(entity.getClientId());
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(InvoiceEntity entity) {
        if ("1".equals(entity.getIsDefault())) {
            String clientId = entity.getClientId();
            if (clientId == null) {
                InvoiceEntity existing = invoiceEntityMapper.selectById(entity.getId());
                clientId = existing == null ? null : existing.getClientId();
            }
            if (clientId != null) {
                invoiceEntityMapper.clearDefaultByClientId(clientId);
            }
        }
        return super.updateById(entity);
    }
}
