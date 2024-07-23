package com.shop.dto;

import com.shop.common.excel.Excel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Excel("卡券导出信息")
public class CardsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel("ID")
    private Integer id;

    /**
     * 对应商品id
     */
    @Excel("对应商品ID")
    private String productName;

    /**
     * 卡密
     */
    @Excel("卡密")
    private String cardInfo;

    /**
     * 卡密状态
     */
    @Excel("卡密状态")
    private String status;

    /**
     * 创建时间
     */
    @Excel("创建时间")
    private String createdAt;

}
