package org.jeecg.modules.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Sku Document
 * Represents a Sku in MongoDB, includes sensitive attributes, latest weight, price and declared value
 * A new Sku Document is created when a new Sku is created in MySQL through the MabangSkuJob, or using online form (SkuModifiedEvent, SkuDeclaredValueModifiedEvent, SkuPriceModifiedEvent in jeecg-boot-base-core)
 * Sku Document is updated when a Sku is updated in MySQL through the MabangSkuSyncJob, MabangSkuStockUpdateJob and also when using online form
 * Warning: if an SKU is modified in other services, it is necessary to implement a mechanism to update the Sku Document in MongoDB
 */
@Document("sku")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuDocument {
    @Id
    private String id;
    /**
     * ID in MySQL
     */
    private String skuId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    @TextIndexed // Text index for full text search
    private String erpCode;
    @TextIndexed
    private String zhName;
    @TextIndexed
    private String enName;
    private String invoiceName;
    private Integer availableAmount;
    private Integer purchasingAmount;
    private String imageSource;
    private BigDecimal shippingDiscount;
    private BigDecimal serviceFee;
    /**
     * Status
     * 1:自动创建;2:待开发;3:正常;4:清仓;5:停止销售"
     * default : 3
     */
    private Integer status;
    private Integer moq;
    /**
     * 是否是赠品 1是 2否
     */
    private Integer isGift;

    /**
     * 敏感属性
     */
    private SensitiveAttribute sensitiveAttribute;
    private LatestSkuWeight latestSkuWeight;
    private LatestSkuPrice latestSkuPrice;
    private LatestDeclaredValue latestDeclaredValue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SensitiveAttribute {
        private String zhName;
        private String enName;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LatestSkuWeight {
        private Integer weight;
        private Date effectiveDate;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LatestSkuPrice {
        private BigDecimal price;
        /**
         * alias discount moq
         */
        private Integer threshold;
        private BigDecimal discountedPrice;
        /**
         * effective date
         */
        private Date date;
        private BigDecimal priceRmb;
        private BigDecimal discountedPriceRmb;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LatestDeclaredValue {
        private BigDecimal declaredValue;
        private Date effectiveDate;
    }

}
