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
     * md5通讯密钥，订单相关接口会用到
     */
    private String md5key;

    /**
     * 支付宝转账
     */
    private String aliUserId;

    /** 支付宝cookie */
    @Column(columnDefinition = "text")
    private String aliCookie;

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

    public Integer[] getEnableTypes() {
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

    public String getEnableTypeName() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(wxpay)){
            builder.append(PayTypeEnum.WX.getName()).append("、");
        }else if (StringUtils.isNotBlank(wxzspay)){
            builder.append(PayTypeEnum.WX.getName()).append("、");
        }
        if (StringUtils.isNotBlank(zfbpay)){
            builder.append(PayTypeEnum.ZFB.getName()).append("、");
        }else if (StringUtils.isNotBlank(aliUserId)){
            builder.append(PayTypeEnum.ZFB.getName()).append("、");
        }
        if (StringUtils.isNotBlank(qqpay)){
            builder.append(PayTypeEnum.QQ.getName()).append("、");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
