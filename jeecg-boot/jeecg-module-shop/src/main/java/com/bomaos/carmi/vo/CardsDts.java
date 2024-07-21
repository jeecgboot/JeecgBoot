package com.bomaos.carmi.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CardsDts {

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 销售类型
     */
    private Integer sellType;

    /**
     * 卡密信息
     */
    private String cardInfo;

    /**
     * 销售次数
     */
    private Integer sellNumber;

    /**
     * 过滤重复
     */
    private Integer repeat;

}
