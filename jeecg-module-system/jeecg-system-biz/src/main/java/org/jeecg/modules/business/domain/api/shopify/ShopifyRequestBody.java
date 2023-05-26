package org.jeecg.modules.business.domain.api.shopify;

public abstract class ShopifyRequestBody {

    protected String sitePrefix;

    protected String shopToken;

    public ShopifyRequestBody(String sitePrefix, String shopToken) {
        this.sitePrefix = sitePrefix;
        this.shopToken = shopToken;
    }

    public String getSitePrefix() {
        return sitePrefix;
    }

    public String getShopToken() {
        return shopToken;
    }

    public abstract String endpoint();

}
