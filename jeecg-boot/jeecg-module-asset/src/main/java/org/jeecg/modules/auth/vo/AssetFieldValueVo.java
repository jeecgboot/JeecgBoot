package org.jeecg.modules.auth.vo;

import lombok.Data;

@Data
public class AssetFieldValueVo {
    private String fieldKey;
    private Object fieldValue;
    private Integer sort;
}
