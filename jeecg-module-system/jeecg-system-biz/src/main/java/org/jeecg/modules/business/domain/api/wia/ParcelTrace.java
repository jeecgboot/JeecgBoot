package org.jeecg.modules.business.domain.api.wia;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ParcelTrace {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scanTime;
    private String scanType;
    private String description;

    public ParcelTrace() {
    }
}
