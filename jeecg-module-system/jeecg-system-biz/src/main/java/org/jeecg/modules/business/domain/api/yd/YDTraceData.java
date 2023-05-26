package org.jeecg.modules.business.domain.api.yd;

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
public class YDTraceData {


    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    @JsonProperty("server_hawbcode")
    private String thirdBillCode;

    @JsonProperty("destination_country")
    private String country;

    private String productCode;

    private String orderNo;

    @JsonProperty("details")
    private List<YDTraceDetail> traceDetails;

    public YDTraceData(String id, String thirdBillCode, String country, List<YDTraceDetail> traceDetails) {
        this.id = id;
        this.thirdBillCode = thirdBillCode;
        this.country = country;
        this.traceDetails = traceDetails;
    }


    public YDTraceData() {
    }
}
