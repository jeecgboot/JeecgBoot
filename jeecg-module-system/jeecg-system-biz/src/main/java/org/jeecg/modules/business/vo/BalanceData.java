package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.Client;

import java.math.BigDecimal;

@Data
public class BalanceData {
    private final Client client;
    private final String currency;
    private final BigDecimal balance;
}
