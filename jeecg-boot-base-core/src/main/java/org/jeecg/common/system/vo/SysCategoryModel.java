package org.jeecg.common.system.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Author qinfeng
 * @Date 2020/2/19 12:01
 * @Description:
 * @Version 1.0
 */
public class SysCategoryModel {
    /**主键*/
    private java.lang.String id;
    /**父级节点*/
    private java.lang.String pid;
    /**类型名称*/
    private java.lang.String name;
    /**类型编码*/
    private java.lang.String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
