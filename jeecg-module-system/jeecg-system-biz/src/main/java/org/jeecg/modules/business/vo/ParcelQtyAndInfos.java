package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.List;

@Data
public class ParcelQtyAndInfos {
    private Integer quantity;
    private List<ParcelInfos> parcelInfos;
}
