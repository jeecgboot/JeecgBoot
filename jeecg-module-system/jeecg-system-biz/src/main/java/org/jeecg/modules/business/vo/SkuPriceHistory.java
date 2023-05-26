package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SkuPriceHistory {

    public final static SkuPriceHistory EMPTY = new SkuPriceHistory(null, null, null, null);

    private final String id;

    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final Date effectiveDate;

    private final BigDecimal registrationFee;

    private final BigDecimal shippingFee;
}
