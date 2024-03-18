package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.mapper.InvoiceMapper;
import org.jeecg.modules.business.service.InvoiceService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

}
