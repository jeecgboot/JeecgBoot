package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.springframework.http.ResponseEntity;

/**
 * 调用马帮 order-do-change-order 接口，将订单标记为作废（orderStatus=5）。
 * 与 CancelOrderRequestBody 配套使用。
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
