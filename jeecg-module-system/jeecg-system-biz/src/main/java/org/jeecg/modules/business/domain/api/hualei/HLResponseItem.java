package org.jeecg.modules.business.domain.api.hualei;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@TableName("parcel")
public class HLResponseItem {

    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    @JsonProperty("trackingNumber")
    private String trackingNumber;

    @JsonProperty("consigneeCountry")
    private String country;

    @JsonProperty("productKindName")
    private String productCode;

    @JsonProperty("business_seqinvoicecode")
    private String billCode;

    @JsonProperty("referenceNumber")
    private String orderNumber;

    @JsonProperty("trackDetails")
    private List<HLParcelTraceDetail> tracesList;

    public HLResponseItem(String id, String trackingNumber, String country, String productCode, String billCode,
                          String orderNumber, List<HLParcelTraceDetail> tracesList) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.country = country;
        this.productCode = productCode;
        this.billCode = billCode;
        this.orderNumber = orderNumber;
        this.tracesList = tracesList;
    }

    public HLResponseItem() {
    }
}
