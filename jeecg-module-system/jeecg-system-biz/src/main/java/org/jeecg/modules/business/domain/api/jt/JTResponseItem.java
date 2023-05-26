package org.jeecg.modules.business.domain.api.jt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class JTResponseItem {

    @JsonProperty("success")
    private String success;

    @JsonProperty("tracesList")
    private List<JTParcelTrace> tracesList;

    public JTResponseItem(String success, List<JTParcelTrace> tracesList) {
        this.success = success;
        this.tracesList = tracesList;
    }

    public JTResponseItem() {
    }
}
