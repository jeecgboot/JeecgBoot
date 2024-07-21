package com.bomaos.settings.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付配置
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
@Data
@ToString
@TableName("sys_pays")
public class Pays implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 驱动
     */
    private String driver;

    /**
     * 配置
     */
    private String config;

    /**
     * 说明
     */
    private String comment;

    /**
     * 移动端显示开关
     */
    private Integer isMobile;

    /**
     * 电脑端显示开关
     */
    private Integer isPc;

    /**
     * 手续费tag
     */
    private Integer isHandlingFee;

    /**
     * 手续费
     */
    private Integer handlingFee;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}
