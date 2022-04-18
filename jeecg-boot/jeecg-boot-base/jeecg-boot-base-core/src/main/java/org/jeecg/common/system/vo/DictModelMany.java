package org.jeecg.common.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询多个字典时用到
 * @author: jeecg-boot
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictModelMany extends DictModel {

    /**
     * 字典code，根据多个字段code查询时才用到，用于区分不同的字典选项
     */
    private String dictCode;

}
