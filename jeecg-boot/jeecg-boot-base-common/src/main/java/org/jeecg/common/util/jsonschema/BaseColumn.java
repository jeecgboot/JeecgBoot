package org.jeecg.common.util.jsonschema;

import lombok.Data;

/**
 * 列 配置基本信息
 */
@Data
public class BaseColumn {

    /**
     * 列配置 描述 -对应数据库字段描述
     */
    private String title;

    /**
     * 列配置 名称 -对应数据库字段名
     */
    private String field;

    public BaseColumn(){}

    public BaseColumn(String title,String field){
        this.title = title;
        this.field = field;
    }

}
