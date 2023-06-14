package org.jeecg.modules.business.domain.api.cmk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CMKParcelDetail {

    @JsonProperty("seventeenNo")
    private String orderNo;

    @JsonProperty("guoJia")
    private String country;

    @JsonProperty("shouHuoQuDao")
    private String productCode;

    public CMKParcelDetail(String orderNo, String country, String productCode) {
        this.orderNo = orderNo;
        this.country = country;
        this.productCode = productCode;
    }

    public CMKParcelDetail() {
    }
}
