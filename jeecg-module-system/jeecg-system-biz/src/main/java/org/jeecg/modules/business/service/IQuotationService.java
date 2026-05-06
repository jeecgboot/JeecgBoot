package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Quotation;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IQuotationService extends IService<Quotation> {

    IPage<Quotation> pageByStatus(Page<Quotation> page, Quotation q);
    void exportCustomerQuotes(Quotation q, String selections, HttpServletResponse response) throws IOException;

    Quotation getByIdAndStatus(String id, String status);
    int updateInquiryFields(Quotation q);
    int addQuoteBasedOnInquiry(Quotation q);
    int updateQuoteFields(Quotation q);
    Quotation estimateQuote(Quotation input);
    void normalizeCountryFields(Quotation quotation);
    void fillInquirySalesByClient(Quotation quotation);
    int revokeQuoteById(String id);
    int revokeQuoteBatch(List<String> ids);
    Result<String> applyClientScope(Quotation quotation);
    Result<String> checkInquiryOwnership(String quotationId);
}
