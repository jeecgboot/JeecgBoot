package com.vone.mq.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "pay_setting")
public class Setting implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /**
     * 订单有效期，公用
     */
    private String close;

    /** 监控状态 */
    private String jkstate;

    /**
     * 密钥（仅限于已登录用户在PC端的操作）：创建订单、补单、关闭订单
     * 接口调用不落地保存：回调（不支持，去掉了key，）、sign校验（定时任务）
     */
    private String md5key;

    /** 心跳 */
    private String lastheart;

    /** 最后收款时间 */
    private String lastpay;

    /** 异步回调 */
    private String notifyUrl;

    /** 区分方式 */
    private String payQf;

    /** 同步回调 */
    private String returnUrl;

    /** 微信二维码 */
    private String wxpay;

    /** 支付宝二维码 */
    private String zfbpay;

}
