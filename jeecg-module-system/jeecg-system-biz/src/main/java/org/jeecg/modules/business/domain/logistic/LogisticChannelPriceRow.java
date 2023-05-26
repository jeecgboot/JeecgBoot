package org.jeecg.modules.business.domain.logistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * This class represent a row of the table in price page
 */
@Data
public class LogisticChannelPriceRow {
    /**
     * id
     */
    private String id;
    /**
     * 物流渠道ID
     */
    private String logisticsChannelId;

    /**
     * 物流渠道ID
     */
    private String logisticsChannelName;

    /**
     * 生效日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    /**
     * 适用国家
     */
    private String effectiveCountry;
    /**
     * 重量范围起始
     */
    private Integer weightRangeStart;
    /**
     * 重量范围截止
     */
    private Integer weightRangeEnd;
    /**
     * 首重
     */
    private Integer minimumWeight;
    /**
     * 首重价格
     */
    private Double minimumWeightPrice;
    /**
     * 续重计费单位
     */
    private Integer calUnit;
    /**
     * 续重单价
     */
    private Double calUnitPrice;
    /**
     * 操作附加费
     */
    private Double additionalCost;
    /**
     * 挂号费
     */
    private Double registrationFee;

    public LogisticChannelPriceRow() {

    }

    public LogisticChannelPriceRow(String id, String logisticsChannelId, String logisticsChannelName,
                                   Date effectiveDate, String effectiveCountry, Integer weightRangeStart,
                                   Integer weightRangeEnd, Integer minimumWeight, Double minimumWeightPrice,
                                   Integer calUnit, Double calUnitPrice, Double additionalCost, Double registrationFee) {
        this.id = id;
        this.logisticsChannelId = logisticsChannelId;
        this.logisticsChannelName = logisticsChannelName;
        this.effectiveDate = effectiveDate;
        this.effectiveCountry = effectiveCountry;
        this.weightRangeStart = weightRangeStart;
        this.weightRangeEnd = weightRangeEnd;
        this.minimumWeight = minimumWeight;
        this.minimumWeightPrice = minimumWeightPrice;
        this.calUnit = calUnit;
        this.calUnitPrice = calUnitPrice;
        this.additionalCost = additionalCost;
        this.registrationFee = registrationFee;
    }

    public LogisticChannelPriceRow(LogisticChannelPrice price, LogisticChannel channel) {
        this(price.getId(), channel.getId(), channel.getZhName(), price.getEffectiveDate(), price.getEffectiveCountry(),
                price.getWeightRangeStart(), price.getWeightRangeEnd(), price.getMinimumWeight(),
                price.getMinimumWeightPrice().doubleValue(), price.getCalUnit(), price.getCalUnitPrice().doubleValue(),
                price.getAdditionalCost().doubleValue(), price.getRegistrationFee().doubleValue());
    }
}
