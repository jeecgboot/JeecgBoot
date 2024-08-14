package org.jeecg.modules.business.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoumanOrderContent {

    private String shopErpCode;
    private String platformOrderId;
    private String platformOrderNumber;
    private String postcode;
    private String recipient;
    private String city;
    private String country;
    private String platformOrderContentId;
    private String customizationData;
    private Integer quantity;
    private String productName;
    private String remark;
    private BigDecimal price;
    private String imageUrl;
    private String sku;
    private Boolean isNecklace;
    private Boolean isGem;
    private List<ShoumanRegex> regexList;

    public ShoumanOrderContent() {
    }

    @Override
    public String toString() {
        return "ShoumanOrderContent{" +
                "shopErpCode='" + shopErpCode + '\'' +
                "platformOrderId='" + platformOrderId + '\'' +
                "platformOrderNumber='" + platformOrderNumber + '\'' +
                ", postcode='" + postcode + '\'' +
                ", recipient='" + recipient + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", platformOrderContentId='" + platformOrderContentId + '\'' +
                ", customizationData='" + customizationData + '\'' +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", remark='" + remark + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", sku='" + sku + '\'' +
                ", isNecklace='" + isNecklace + '\'' +
                ", isGem='" + isGem + '\'' +
                ", regexList='" + regexList + '\'' +
                '}';
    }
}
