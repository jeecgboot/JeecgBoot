package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ShopOptionsAddParam {
    private List<String> shopIds;
    private Boolean useBalance;
    private Boolean showBalance;
    private Integer balanceThreshold;
    private Boolean isAutoInvoice;
    private Boolean isChronologicalOrder;
    private Boolean isBreakdownInvoice;
    private Boolean isCompleteInvoice;
    private Boolean canSelfInvoice;
    private Boolean canSelfP;
    private Boolean canSelfL;
    private Boolean canSelfPL;
    private Boolean isSelfIgnoreStock;
    private Boolean hasStock;
    private Boolean hasShippingInvoiceRemark;

    public ShopOptionsAddParam(@JsonProperty("shopIds") List<String> shopIds,
                               @JsonProperty("useBalance") Boolean useBalance,
                               @JsonProperty("showBalance") Boolean showBalance,
                               @JsonProperty("balanceThreshold") Integer balanceThreshold,
                               @JsonProperty("isAutoInvoice") Boolean isAutoInvoice,
                               @JsonProperty("isChronologicalOrder") Boolean isChronologicalOrder,
                               @JsonProperty("isBreakdownInvoice") Boolean isBreakdownInvoice,
                               @JsonProperty("isCompleteInvoice") Boolean isCompleteInvoice,
                               @JsonProperty("canSelfInvoice") Boolean canSelfInvoice,
                               @JsonProperty("canSelfP") Boolean canSelfP,
                               @JsonProperty("canSelfL") Boolean canSelfL,
                               @JsonProperty("canSelfPL") Boolean canSelfPL,
                               @JsonProperty("isSelfIgnoreStock") Boolean isSelfIgnoreStock,
                               @JsonProperty("hasStock") Boolean hasStock,
                               @JsonProperty("hasShippingInvoiceRemark") Boolean hasShippingInvoiceRemark
    ) {
        this.shopIds = shopIds;
        this.useBalance = useBalance;
        this.showBalance = showBalance;
        this.balanceThreshold = balanceThreshold;
        this.isAutoInvoice = isAutoInvoice;
        this.isChronologicalOrder = isChronologicalOrder;
        this.isBreakdownInvoice = isBreakdownInvoice;
        this.isCompleteInvoice = isCompleteInvoice;
        this.canSelfInvoice = canSelfInvoice;
        this.canSelfP = canSelfP;
        this.canSelfL = canSelfL;
        this.canSelfPL = canSelfPL;
        this.isSelfIgnoreStock = isSelfIgnoreStock;
        this.hasStock = hasStock;
        this.hasShippingInvoiceRemark = hasShippingInvoiceRemark;
    }
}
