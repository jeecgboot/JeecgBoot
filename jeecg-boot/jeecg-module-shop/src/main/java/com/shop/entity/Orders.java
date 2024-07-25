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
 * 订单表
 * 2021-03-29 16:24:28
 */
@Data
@TableName("sys_orders")
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单号
     */
    private String member;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 买家联系方式
     */
    private String contact;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 发货模式
     */
    private Integer shipType;

    /**
     * 订单数量
     */
    private Integer number;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 支付用户的id（如果有）
     */
    private Integer guestId;

    /**
     * 购买设备
     */
    private String device;

    /**
     * 买家ip
     */
    private String ip;

    /**
     * 流水号
     */
    private String payNo;

    /**
     * 付款金额
     */
    private BigDecimal money;

    /**
     * 提交金额
     */
    private BigDecimal price;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 云端id
     */
    private String cloudPayid;

    /**
     * 邮件通知
     */
    private String email;

    /**
     * 标记为使用优惠券
     */
    private Integer isCoupon;

    /**
     * 优惠券id
     */
    private Integer couponId;


    /**
     * 订单密码
     */
    private String password;

    /**
     * 卡密信息
     */
    private String cardsInfo;

    /**
     * 附加信息
     */
    private String attachInfo;

    /**
     * 手续费
     */
    private BigDecimal handlingFee;

    /**
     * 用户名
     */
    private String username;
}
