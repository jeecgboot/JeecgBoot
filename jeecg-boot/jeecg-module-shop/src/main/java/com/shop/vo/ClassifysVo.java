package com.shop.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ClassifysVo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 商品数量
     */
    private Long productsMember;

    /**
     * 索引
     */
    private Integer andIncrement;
}
