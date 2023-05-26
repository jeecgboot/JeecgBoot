package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.Promotion;
import org.jeecg.modules.business.entity.SkuPrice;

import java.util.Objects;

@Data
public class SkuDetail {

    private final String skuId;

    private final String erpCode;

    private final String imageSource;

    private final String namEn;
    private final String nameZh;

    private final SkuPrice price;

    private final Promotion promotion;

    public SkuDetail(String skuId, String erpCode, String imageSource, String namEn, String nameZh, SkuPrice price, Promotion promotion) {
        this.skuId = Objects.requireNonNull(skuId);
        this.erpCode = erpCode;
        this.imageSource = imageSource;
        this.namEn = namEn;
        this.nameZh = nameZh;
        this.price = Objects.requireNonNull(price);
        this.promotion = promotion == null ? Promotion.ZERO_PROMOTION : promotion;
    }

    @Override
    public String toString(){
        return String.format(
                "ID: %s, ERP: %s, product: %s, price: %s, promotion: %s",
                skuId, erpCode, namEn,
                price,
                promotion
        );
    }
}
