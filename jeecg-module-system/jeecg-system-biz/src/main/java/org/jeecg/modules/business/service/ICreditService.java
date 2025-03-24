package org.jeecg.modules.business.service;

import org.jeecg.modules.business.domain.credit.CreditInvoice;
import org.jeecg.modules.business.entity.Credit;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CreditPage;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.Response;

import java.io.IOException;
import java.util.List;

/**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
public interface ICreditService extends IService<Credit> {
    Credit getLastCredit(String clientId, String currencyId);

    String getLatestInvoiceNumber();

    Response<String, String> addCredit(Credit credit);

    Response<InvoiceMetaData, String> makeInvoice(String creditId) throws IOException;

    InvoiceMetaData getInvoiceMetaData(CreditInvoice invoice) throws IOException;

    InvoiceMetaData generateInvoiceExcel(String invoiceNumber) throws IOException;

    Credit getByInvoiceNumber(String invoiceNumber);

    void updateCredit(Credit credit, boolean isAmountUpdated, String username) throws Exception;

    List<CreditPage> listWithFilters(String clientId, String invoiceNumber, String currencyId, int pageNo, int pageSize);

    int countAllWithFilters(String clientId, String invoiceNumber, String currencyId);

    void cancelCredit(String id);
}
