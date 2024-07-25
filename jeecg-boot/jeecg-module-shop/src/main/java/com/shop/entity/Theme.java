package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 主题配置
 * 2021-06-28 00:36:29
 */
@Data
@TableName("sys_theme")
public class Theme implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 说明
     */
    private String description;

    /**
     * 主题驱动
     */
    private String driver;

    /**
     * 是否设置
     */
    private Integer enable;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 创建时间
     */
    private Date createDate;

}
