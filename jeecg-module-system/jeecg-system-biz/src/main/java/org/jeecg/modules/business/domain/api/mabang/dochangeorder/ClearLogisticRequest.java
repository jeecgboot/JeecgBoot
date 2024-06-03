package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import org.jeecg.modules.business.domain.api.mabang.Request;

public class ClearLogisticRequest extends Request {
    public ClearLogisticRequest(ClearLogisticRequestBody body) {
        super(body);
    }

    @Override
    public ClearLogisticResponse send() {
        String jsonString = rawSend().getBody();
        return ClearLogisticResponse.parse(jsonString);
    }
}
