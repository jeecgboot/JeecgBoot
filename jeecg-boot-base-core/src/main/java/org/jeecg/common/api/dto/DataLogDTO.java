package org.jeecg.common.api.dto;

import lombok.Data;

/**
 * @Author taoYan
 * @Date 2022/7/26 14:44
 **/
@Data
public class DataLogDTO {

    private String tableName;

    private String dataId;

    private String content;

    private String type;

    private String createName;

    public DataLogDTO(){

    }

    public DataLogDTO(String tableName, String dataId, String content, String type) {
        this.tableName = tableName;
        this.dataId = dataId;
        this.content = content;
        this.type = type;
    }

    public DataLogDTO(String tableName, String dataId, String type) {
        this.tableName = tableName;
        this.dataId = dataId;
        this.type = type;
    }
}
