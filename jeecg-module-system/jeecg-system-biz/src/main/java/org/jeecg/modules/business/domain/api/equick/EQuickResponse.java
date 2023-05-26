package org.jeecg.modules.business.domain.api.equick;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class EQuickResponse {

    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    @JSONField(deserialize = false)
    private String country;

    @JSONField(deserialize = false)
    private String productCode;

    @JSONField(deserialize = false)
    private String billCode;

    @JSONField(deserialize = false)
    private String thirdBillCode;

    /**
     * 返回值
     */
    @JsonProperty("returnValue")
    private Integer returnValue;

    /**
     * 返回信息
     */
    @JsonProperty("returnMessage")
    private String returnMessage;

    /**
     * Equick单号
     */
    @JsonProperty("equickWBNo")
    private String equickWBNo;

    /**
     * 国外标签单号 (大部分时间无内容)
     */
    @JsonProperty("trackTraceLabelNo")
    private String trackTraceLabelNo;

    @JsonProperty("traceDataSet")
    private List<EQuickTraceData> traceDataSet;

    public EQuickResponse(Integer returnValue, String returnMessage, String equickWBNo, String trackTraceLabelNo, List<EQuickTraceData> traceDataSet) {
        this.returnValue = returnValue;
        this.returnMessage = returnMessage;
        this.equickWBNo = equickWBNo;
        this.trackTraceLabelNo = trackTraceLabelNo;
        this.traceDataSet = traceDataSet;
    }

    public EQuickResponse() {
    }
}
