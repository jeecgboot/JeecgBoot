package com.vmq.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "pay_other_setting")
public class OtherSetting implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** 商户id */
    private String appId;

    /** 用户名 */
    private String username;

    /** 类型 */
    private String type;

    /** 邮箱 */
    private String email;

    /** 密钥 */
    private String appKey;

    /** 异步回调 */
    private String notifyUrl;

    /** 同步回调 */
    private String returnUrl;

    /** 官网地址 */
    private String createUrl;

}
