package org.jeecg.modules.business.entity;

import lombok.Data;

import java.math.BigDecimal;

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
    private String contentRecRegex;
    private String contentExtRegex;
    private BigDecimal price;
    private String imageUrl;
    private String sku;

    public ShoumanOrderContent(String shopErpCode, String platformOrderId, String platformOrderNumber, String postcode,
                               String recipient, String city, String country, String platformOrderContentId,
                               String customizationData, Integer quantity, String productName, String remark,
                               String contentRecRegex, String contentExtRegex, BigDecimal price, String imageUrl,
                               String sku) {
        this.shopErpCode = shopErpCode;
        this.platformOrderId = platformOrderId;
        this.platformOrderNumber = platformOrderNumber;
        this.postcode = postcode;
        this.recipient = recipient;
        this.city = city;
        this.country = country;
        this.platformOrderContentId = platformOrderContentId;
        this.customizationData = customizationData;
        this.quantity = quantity;
        this.productName = productName;
        this.remark = remark;
        this.contentRecRegex = contentRecRegex;
        this.contentExtRegex = contentExtRegex;
        this.price = price;
        this.imageUrl = imageUrl;
        this.sku = sku;
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
                ", contentRecRegex='" + contentRecRegex + '\'' +
                ", contentExtRegex='" + contentExtRegex + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
