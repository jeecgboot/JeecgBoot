package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典
 * 2020-03-14 11:29:03
 */
@Data
@TableName("sys_dictionary")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Integer dictId;
    /**
     * 字典标识
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
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

}
