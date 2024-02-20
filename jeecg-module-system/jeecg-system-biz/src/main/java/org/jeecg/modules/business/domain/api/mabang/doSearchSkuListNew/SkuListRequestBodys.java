package org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew;

import java.time.LocalDateTime;

public class SkuListRequestBodys {

    public static SkuListRequestBody allSkuOfDateType(LocalDateTime start, LocalDateTime end, DateType dateType) {
        return new SkuListRequestBody()
                .setDatetimeType(dateType)
                .setStartDate(start)
                .setEndDate(end);
    }
}
