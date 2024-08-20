package org.jeecg.modules.business.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("sku")
@Data
public class Sku {
    @Id
    private String id;
    /**
     * Id in MySQL
     */
    private String skuId;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String erpCode;
    private String zhName;
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
    /**
     * 敏感属性 en_name
     */
    private String sensitiveAttribute;
    private Integer moq;
    private Integer isGift;
    private Integer weight;
    /**
     * 重量生效日期
     */
    private Date effectiveDate;
    private String clientCode;
    private BigDecimal sku_price;
    private Integer discount_moq;
    private BigDecimal discounted_price;
    /**
     * dynamically calculated fields
     */
    private Integer sales_last_week;
    private Integer sales_four_weeks;
    private Integer sales_six_weeks;
    private Integer qtyInOrdersNotShipped;
}
