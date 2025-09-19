package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.springframework.http.ResponseEntity;

/**
 * CancelOrderRequest  represents a request to cancel an order（orderStatus=5）
 * using the MaBang API (order-do-change-order).
 */
@Slf4j
public class CancelOrderRequest extends Request {

    public CancelOrderRequest(CancelOrderRequestBody body) {
        super(body);
    }

    @Override
    public ChangeOrderResponse send() {
        ResponseEntity<String> res = rawSend();
        if (res == null || res.getBody() == null) {
            log.error("CancelOrderRequest rawSend() returned null or empty body");
            return ChangeOrderResponse.parse("{\"code\":\"500\",\"message\":\"no_response\"}");
        }
        try {
            return ChangeOrderResponse.parse(res.getBody());
        } catch (Exception e) {
            log.error("CancelOrderRequest parse failed. body={}", res.getBody(), e);
            return ChangeOrderResponse.parse("{\"code\":\"500\",\"message\":\"invalid_json\"}");
        }
    }
}
