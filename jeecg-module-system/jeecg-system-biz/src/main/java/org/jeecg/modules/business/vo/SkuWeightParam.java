package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Data
public class SkuWeightParam {
    private final List<String> ids;
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;
    private final int weight;
    public SkuWeightParam(@JsonProperty("ids") List<String> ids, @JsonProperty("weight") int weight, @JsonProperty("effectiveDate") Date effectiveDate){
        this.ids = ids;
        this.weight = weight;
        this.effectiveDate = effectiveDate;
    }
}
