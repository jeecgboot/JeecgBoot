package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Period {
    @JsonProperty("start")
    private final Date start;
    @JsonProperty("end")
    private final Date end;

    @JsonProperty("type")
    private final String type;

    public Period(Date start, Date end, String type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }

    public boolean isValid() {
        return start != null && end != null;
    }
}
