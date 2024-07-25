package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券
 * 2021-06-23 07:43:23
 */
@Data
@TableName("sys_coupon")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer classifysId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 类型-0一次性，1重复使用
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 优惠券代码
     */
    private String coupon;

    /**
     * 面额或者百分比
     */
    private Integer discountType;

    /**
     * 面额、折扣 价格和百分比
     */
    private BigDecimal discountVal;

    /**
     * 已使用次数
     */
    private Integer countUsed;

    /**
     * 可用次数
     */
    private Integer countAll;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 满减金额
     */
    private BigDecimal fullReduction;

    /**
     * 用户名
     */
    private String username;

}
