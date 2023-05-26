package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import org.jeecg.modules.business.domain.api.mabang.Request;

public class ArchiveOrderRequest extends Request {
    public ArchiveOrderRequest(ArchiveOrderRequestBody body) {
        super(body);
    }

    @Override
    public ChangeOrderResponse send() {
        String jsonString = rawSend().getBody();
        return ChangeOrderResponse.parse(jsonString);
    }
}
