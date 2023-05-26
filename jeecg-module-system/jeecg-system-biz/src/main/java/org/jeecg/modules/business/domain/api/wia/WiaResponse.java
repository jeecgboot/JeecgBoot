package org.jeecg.modules.business.domain.api.wia;

import lombok.Data;

import java.util.List;

@Data
public class WiaResponse {

    private boolean success;

    private List<Parcel> parcels;

    public WiaResponse() {
    }
}
