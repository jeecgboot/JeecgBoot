package com.shop.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class PaysVo {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付类型名称
     */
    private String name;

    /**
     * 支付驱动类型
     */
    private String driver;

    /**
     * id
     */
    private String appid;

    /**
     * id
     */
    private String mchid;

    /**
     * 密钥
     */
    private String key;

    /**
     * 支付宝官方密钥
     */
    private String mpKey;

    /**
     * 通知地址
     */
    private String createUrl;

    /**
     * 通知地址
     */
    private String notifyUrl;

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

    /**
     * 索引
     */
    private Integer andIncrement;

}
