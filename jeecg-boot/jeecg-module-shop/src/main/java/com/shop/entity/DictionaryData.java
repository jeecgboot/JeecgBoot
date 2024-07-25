package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典项
 * 2020-03-14 11:29:04
 */
@Data
@TableName("sys_dictionary_data")
public class DictionaryData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字典项id
     */
    @TableId(value = "dict_data_id", type = IdType.AUTO)
    private Integer dictDataId;
    /**
     * 字典id
     */
    private Integer dictId;
    /**
     * 字典项标识
     */
    private String dictDataCode;
    /**
     * 字典项名称
     */
    private String dictDataName;
    /**
     * 排序号
     */
    private Integer sortNumber;
    /**
     * 备注
     */
    private String comments;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否删除,0否,1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 字典代码
     */
    @TableField(exist = false)
    private String dictCode;
    /**
     * 字典名称
     */
    @TableField(exist = false)
    private String dictName;


}
