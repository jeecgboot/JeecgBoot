package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章表
 * Created by Panyoujie on 2021-11-08 04:44:45
 */
@ToString
@Data
@TableName("sys_article")
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章图片
     */
    private String picture;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 摘要
     */
    private String excerpt;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 查看数量
     */
    private Integer seeNumber;

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

    /**
     * 是否启用
     */
    private Integer enabled;

}
