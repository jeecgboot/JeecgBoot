package org.jeecg.modules.business.domain.credit;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Credit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class CreditInvoiceFactory {
    private final SimpleDateFormat SUBJECT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public CreditInvoice createInvoice(Credit credit, Client client, BigDecimal exchangeRate, BigDecimal balance, String currency) {
        String subject = String.format("Credit funds added to balance executed on %s ", SUBJECT_FORMAT.format(credit.getCreateTime()));
        return new CreditInvoice(client, credit, subject, exchangeRate, balance, currency);
    }
}
