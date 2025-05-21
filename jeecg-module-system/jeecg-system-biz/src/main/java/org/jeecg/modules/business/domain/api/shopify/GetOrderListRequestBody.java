package org.jeecg.modules.business.domain.api.shopify;

import java.util.List;

public class GetOrderListRequestBody extends ShopifyRequestBody {

    public static final String ENDPOINT = "orders.json?status=open&limit=250&fields=id,note,discount_codes&ids=%s";

    private final List<String> ids;

    public GetOrderListRequestBody(String sitePrefix, String shopToken, List<String> ids) {
        super(sitePrefix, shopToken);
        this.ids = ids;
    }

    @Override
    public String endpoint() {
        return String.format(ENDPOINT, String.join(",", ids));
    }
}
