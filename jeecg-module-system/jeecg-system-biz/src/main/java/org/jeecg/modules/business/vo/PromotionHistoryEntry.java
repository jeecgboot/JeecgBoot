package org.jeecg.modules.business.vo;

/**
 * Data object that records the purchase that activates
 * the promotion and the number of activation.
 */
public class PromotionHistoryEntry {
    final String promotionID;

    final int count;

    public PromotionHistoryEntry(String promotionID, int count) {
        this.promotionID = promotionID;
        this.count = count;
    }
}
