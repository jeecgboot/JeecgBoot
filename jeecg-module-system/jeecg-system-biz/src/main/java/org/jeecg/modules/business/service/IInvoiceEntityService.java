package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.InvoiceEntity;

import java.util.List;

public interface IInvoiceEntityService extends IService<InvoiceEntity> {

    List<InvoiceEntity> selectByMainId(String mainId);

    List<InvoiceEntity> selectActiveByClientId(String clientId);
}
