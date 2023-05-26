package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Period {
    @JsonProperty
    private final Date start;
    @JsonProperty
    private final Date end;

    public Period(Date start, Date end) {
        this.start = start;
        this.end = end;
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
