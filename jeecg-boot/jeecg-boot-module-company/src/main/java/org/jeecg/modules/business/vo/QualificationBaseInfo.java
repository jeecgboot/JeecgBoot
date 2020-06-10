package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class QualificationBaseInfo {
    /**
     * 资质类型
     */
    private String type;
    /**
     * 资质文件路径
     */
    private String filepath;
    /**
     * 资质文件名称
     */
    private String filename;


}
