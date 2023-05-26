package org.jeecg.modules.business.domain.api.shopify;

import org.jeecg.modules.business.entity.PlatformOrderShopSync;

public class GetFulfillmentRequestBody extends ShopifyRequestBody {

    public static final String ENDPOINT = "orders/%s/fulfillment_orders.json";

    private final String platformOrderId;



    public GetFulfillmentRequestBody(String sitePrefix, String platformOrderId, String shopToken) {
        super(sitePrefix, shopToken);
        this.platformOrderId = platformOrderId;
    }

    public GetFulfillmentRequestBody(PlatformOrderShopSync platformOrderShopSync) {
        this(platformOrderShopSync.getShopifyPrefix(), platformOrderShopSync.getPlatformOrderId(), platformOrderShopSync.getShopifyToken());
    }

    @Override
    public String endpoint() {
        return String.format(ENDPOINT, platformOrderId);
    }
}
