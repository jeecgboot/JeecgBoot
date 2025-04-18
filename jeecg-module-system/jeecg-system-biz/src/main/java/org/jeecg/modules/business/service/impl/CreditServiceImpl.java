package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.domain.credit.CreditInvoice;
import org.jeecg.modules.business.domain.credit.CreditInvoiceFactory;
import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Credit;
import org.jeecg.modules.business.mapper.CreditMapper;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.CreditPage;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.jeecg.modules.business.entity.Invoice.InvoiceType.CREDIT;

/**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Service
@Slf4j
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements ICreditService {
    @Autowired
    private IClientService clientService;
    @Autowired
    private CreditMapper creditMapper;
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ExchangeRatesMapper exchangeRatesMapper;
    @Autowired
    private CreditInvoiceFactory factory;
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private IInvoiceNumberReservationService invoiceNumberReservationService;

    @Override
    public Credit getLastCredit(String clientId,String currencyId) {
        return creditMapper.getLastCredit(clientId, currencyId);
    }

    @Override
    public String getLatestInvoiceNumber() {
        return creditMapper.getLatestInvoiceNumber();
    }

    private final SimpleDateFormat CREATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${jeecg.path.creditTemplatePath_EU}")
    private String INVOICE_TEMPLATE_EU;
    @Value("${jeecg.path.creditTemplatePath_US}")
    private String INVOICE_TEMPLATE_US;
    @Value("${jeecg.path.creditTemplatePath_balance_EU}")
    private String INVOICE_TEMPLATE_BALANCE_EU;
    @Value("${jeecg.path.creditTemplatePath_balance_US}")
    private String INVOICE_TEMPLATE_BALANCE_US;

    @Value("${jeecg.path.creditInvoiceDir}")
    private String INVOICE_DIR;

    @Transactional
    @Override
    public Response<String, String> addCredit(Credit credit) {
        Response<String, String> res = new Response<>();
        Client client = clientService.getById(credit.getClientId());
        if(client == null) {
            res.setStatus(HttpStatus.SC_NOT_FOUND);
            return res;
        }
        String invoiceNumber =  invoiceNumberReservationService.getLatestInvoiceNumberByType(CREDIT.getType());
        credit.setInvoiceNumber(invoiceNumber);
        save(credit);
        balanceService.updateBalance(credit.getClientId(), credit.getId(), credit.getAmount(), credit.getCurrencyId());
        res.setData(credit.getId());
        res.setStatus(HttpStatus.SC_CREATED);
        return res;
    }

    @Override
    public Response<InvoiceMetaData, String> makeInvoice(String creditId) throws IOException {
        Response<InvoiceMetaData, String> res = new Response<>();
        if(!securityService.checkIsEmployee()) {
            res.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return res;
        }
        Credit credit = getById(creditId);
        if(credit == null) {
            res.setStatus(HttpStatus.SC_NOT_FOUND);
            return res;
        }
        Client client = clientService.getById(credit.getClientId());
        BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");
        String currency = currencyService.getCodeById(credit.getCurrencyId());
        BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), currency);
        CreditInvoice invoice = factory.createInvoice(credit, client, eurToUsd, balance, currency);

        InvoiceMetaData metaData = getInvoiceMetaData(invoice);
        res.setData(metaData);
        res.setStatus(HttpStatus.SC_CREATED);
        return res;
    }

    @Override
    public InvoiceMetaData getInvoiceMetaData(CreditInvoice invoice) throws IOException {
        Path template;
        if(invoice.getCurrency().equals("USD")) {
            if(invoice.getCredit().getShowBalance() == 0)
                template = Paths.get(INVOICE_TEMPLATE_US);
            else template = Paths.get(INVOICE_TEMPLATE_BALANCE_US);
        } else {
            if(invoice.getCredit().getShowBalance() == 0)
                template = Paths.get(INVOICE_TEMPLATE_EU);
            else
                template = Paths.get(INVOICE_TEMPLATE_BALANCE_EU);
        }
        String filename = "Invoice N°" + invoice.getCode() + "(" + invoice.getTargetClient().getInvoiceEntity() + ").xlsx";
        Path out = Paths.get(INVOICE_DIR, filename);
        Files.copy(template, out, StandardCopyOption.REPLACE_EXISTING);
        invoice.toExcelFile(out);
        return new InvoiceMetaData(filename, invoice.getCode(), invoice.getTargetClient().getInternalCode(), invoice.getTargetClient().getInvoiceEntity(), "");
    }

    @Override
    public InvoiceMetaData generateInvoiceExcel(String invoiceNumber) throws IOException {
        Credit credit = getByInvoiceNumber(invoiceNumber);
        Client client = clientService.getById(credit.getClientId());
        BigDecimal eurToUsd = exchangeRatesMapper.getExchangeRateFromDate("EUR", "USD", CREATE_TIME_FORMAT.format(credit.getCreateTime()));
        String currency = currencyService.getCodeById(credit.getCurrencyId());
        BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), currency);
        CreditInvoice invoice = factory.createInvoice(credit, client, eurToUsd, balance, currency);

        return getInvoiceMetaData(invoice);
    }

    @Override
    public Credit getByInvoiceNumber(String invoiceNumber) {
        return creditMapper.getByInvoiceNumber(invoiceNumber);
    }
    @Transactional
    @Override
    public void updateCredit(Credit credit, boolean isAmountUpdated, String username) throws Exception {
        if(isAmountUpdated) {
            updateById(credit);
            balanceService.editBalance(credit.getId(), Balance.OperationType.Credit.name(), credit.getClientId(), credit.getAmount(), credit.getCurrencyId());
        } else {
            creditMapper.updateButAmount(credit, username);
        }
    }

    @Override
    public List<CreditPage> listWithFilters(String clientId, String invoiceNumber, String currencyId, int pageNo, int pageSize) {
        int offset = (pageNo - 1) * pageSize;
        return creditMapper.listWithFilters(clientId, invoiceNumber, currencyId, offset, pageSize);
    }

    @Override
    public int countAllWithFilters(String clientId, String invoiceNumber, String currencyId) {
        return creditMapper.countAllWithFilters(clientId, invoiceNumber, currencyId);
    }

    @Override
    public void cancelCredit(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        creditMapper.cancelCredit(id, sysUser.getUsername());
    }
}
