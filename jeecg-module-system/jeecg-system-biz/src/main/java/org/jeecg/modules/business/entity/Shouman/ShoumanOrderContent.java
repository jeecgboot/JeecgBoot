package org.jeecg.modules.business.entity.Shouman;

import lombok.Data;
import org.jeecg.modules.business.entity.ShoumanRegex;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoumanOrderContent {

    private String platformOrderContentId;
    private String skuId;
    private String customizationData;
    private String customizationUrl;
    private Integer quantity;
    private String productName;
    private String remark;
    private BigDecimal price;
    private String imageUrl;
    private String sku;
    private Boolean isNecklace;
    private Boolean isGem;
    private Boolean isMemo;
    private List<ShoumanRegex> regexList;

    public ShoumanOrderContent() {
    }

    @Override
    public String toString() {
        return "ShoumanOrderContent{" +
                ", platformOrderContentId='" + platformOrderContentId + '\'' +
                ", customizationData='" + customizationData + '\'' +
                ", customizationUrl='" + customizationUrl + '\'' +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", remark='" + remark + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", sku='" + sku + '\'' +
                ", isNecklace='" + isNecklace + '\'' +
                ", isGem='" + isGem + '\'' +
                ", isMemo='" + isMemo + '\'' +
                ", regexList='" + regexList + '\'' +
                '}';
    }
}
