package org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase;

import org.jeecg.modules.business.domain.api.mabang.Request;

public class AddPurchaseOrderRequest extends Request {
    public AddPurchaseOrderRequest(AddPurchaseOrderRequestBody body) {
        super(body);
    }

    @Override
    public AddPurchaseOrderResponse send() {
        String jsonString = rawSend().getBody();
        return AddPurchaseOrderResponse.parse(jsonString);
    }
}
