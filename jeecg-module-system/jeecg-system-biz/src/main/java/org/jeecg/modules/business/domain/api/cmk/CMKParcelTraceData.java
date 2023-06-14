package org.jeecg.modules.business.domain.api.cmk;

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
public class CMKParcelTraceData {


    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    /**
     * 单号，与查询单号一致
     */
    @JsonProperty("no")
    private String thirdBillCode;

    private String errorMsg;

    private CMKParcelDetail detail;

    @JsonProperty("trackList")
    private List<CMKParcelTrace> traceList;

    private String country;

    public CMKParcelTraceData(String thirdBillCode, String errorMsg, CMKParcelDetail detail, List<CMKParcelTrace> traceList) {
        this.thirdBillCode = thirdBillCode;
        this.errorMsg = errorMsg;
        this.detail = detail;
        this.traceList = traceList;
    }

    public CMKParcelTraceData() {
    }
}
