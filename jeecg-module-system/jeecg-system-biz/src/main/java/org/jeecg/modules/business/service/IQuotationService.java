package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Quotation;

import java.util.List;

public interface IQuotationService extends IService<Quotation> {

    IPage<Quotation> pageByStatus(Page<Quotation> page, Quotation q);

    Quotation getByIdAndStatus(String id, String status);
    int updateInquiryFields(Quotation q);
    int addQuoteBasedOnInquiry(Quotation q);
    int updateQuoteFields(Quotation q);
    Quotation estimateQuote(Quotation input);
    int revokeQuoteById(String id);
    int revokeQuoteBatch(List<String> ids);
}
