package org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase;

import org.jeecg.modules.business.domain.api.mabang.Request;

public class ChangePurchaseOrderRequest extends Request {
    public ChangePurchaseOrderRequest(ChangePurchaseOrderRequestBody body) {
        super(body);
    }

    @Override
    public ChangePurchaseOrderResponse send() {
        String jsonString = rawSend().getBody();
        return ChangePurchaseOrderResponse.parse(jsonString);
    }
}
