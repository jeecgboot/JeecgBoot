package com.vmq.entity;

import com.vmq.constant.PayTypeEnum;
import com.vmq.utils.StringUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pay_setting")
public class VmqSetting implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

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

    /** 回调密钥 */
    private String secret;

    private String aliUserId;

    /** 支付宝cookie */
    @Column(columnDefinition = "text")
    private String aliCookie;

    /** 多合一轮询扫码 */
    private Integer allInOneQr;

    /** 是否发送付款审核 */
    private Integer isApprove;

    /** 是否发送付款通知 */
    private Integer isNotice;

    /** 区分方式 */
    private Integer payQf;

    /** 异步回调 */
    private String notifyUrl;

    /** 同步回调 */
    private String returnUrl;

    /** 心跳 */
    private String lastheart;

    /** 最后收款时间 */
    private String lastpay;

    /** 微信二维码 */
    private String wxpay;

    /** 微信赞赏码 */
    @Column(columnDefinition = "mediumtext")
    private String wxzspay;

    /** 支付宝二维码 */
    private String zfbpay;

    private String qqpay;

    public Integer[] getUsableTypes() {
        List<Integer> types = new ArrayList<>();
        if (StringUtils.isNotBlank(wxpay)){
            types.add(PayTypeEnum.WX.getCode());
        }
        if (StringUtils.isNotBlank(zfbpay)){
            types.add(PayTypeEnum.ZFB.getCode());
        }
        if (StringUtils.isNotBlank(wxzspay)){
            types.add(PayTypeEnum.ZSM.getCode());
        }
        if (StringUtils.isNotBlank(qqpay)){
            types.add(PayTypeEnum.QQ.getCode());
        }
        if (StringUtils.isNotBlank(aliUserId)){
            types.add(PayTypeEnum.ZFBTR.getCode());
        }
        return types.stream().toArray(Integer[]::new);
    }
}
