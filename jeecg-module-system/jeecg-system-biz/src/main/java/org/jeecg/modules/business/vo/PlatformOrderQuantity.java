package org.jeecg.modules.business.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlatformOrderQuantity {
    private final LocalDate date;

    private final int quantity;
}
