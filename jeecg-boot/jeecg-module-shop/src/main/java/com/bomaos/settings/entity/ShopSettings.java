package com.bomaos.settings.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商店设置
 * Created by Panyoujie on 2021-07-04 03:54:31
 */
@Data
@ToString
@TableName("sys_shop_settings")
public class ShopSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否开启弹窗
     */
    private Integer isWindow;

    /**
     * 弹窗内容
     */
    private String windowText;

    /**
     * 全局背景图
     */
    private String isBackground;

    /**
     * 商店详情
     */
    private String storeDetails;

    /**
     * 是否开启微信通知
     */
    private Integer isWxpusher;

    /**
     * wxpusher token
     */
    private String appToken;

    /**
     * wxpusher uid
     */
    private String wxpushUid;

    /**
     * 邮件通知
     */
    private Integer isEmail;

    /**
     * 客服QQ号码
     */
    private String qqCustomerService;

    /**
     * QQ群二维码
     */
    private String qqGroupQrcode;

    /**
     * 是否开启在线客服
     */
    private Integer isClient;

    /**
     * crisp密钥
     */
    private String crispKey;

    /**
     * 首页模版
     */
    private Integer isModel;

}
