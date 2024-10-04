package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class SkuCriteriaData {
    private final String id;
    private final String zhName;
    private final String enName;
    private final Integer ranking;
    private final Integer isMultiple;
}
