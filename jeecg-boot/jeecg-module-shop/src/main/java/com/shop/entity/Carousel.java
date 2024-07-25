package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图管理
 * 2021-11-10 02:54:31
 */
@Data
@TableName("sys_carousel")
public class Carousel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 对应的链接地址
     */
    private String link;

    /**
     * 图片地址
     */
    private String imageLogo;

    /**
     * 简介
     */
    private String summary;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户名
     */
    private String username;

}
