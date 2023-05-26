package org.jeecg.modules.business.domain.api.wia;

import lombok.Data;

import java.util.List;

@Data
public class Parcel {

    private String destination;
    private String trackingNumber;
    private String origin;
    private List<ParcelTrace> traces;

    public Parcel() {
    }
}
