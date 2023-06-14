package org.jeecg.modules.business.domain.api.cmk;

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
public class CMKResponse {

    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    @JSONField(deserialize = false)
    private Integer statusCode;

    @JSONField(deserialize = false)
    private Boolean success;

    @JsonProperty("data")
    private List<CMKParcelTraceData> parcelData;

    public CMKResponse(Integer statusCode, Boolean success, List<CMKParcelTraceData> parcelData) {
        this.statusCode = statusCode;
        this.success = success;
        this.parcelData = parcelData;
    }

    public CMKResponse() {
    }
}
