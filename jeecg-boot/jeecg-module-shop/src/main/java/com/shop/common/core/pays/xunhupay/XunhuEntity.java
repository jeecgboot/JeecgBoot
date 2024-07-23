package com.shop.common.core.pays.xunhupay;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class XunhuEntity {
    private Integer errcode;
    private String errmsg;
    private String hash;
    private String openid;
    private String url_qrcode;
    private String url;
}
