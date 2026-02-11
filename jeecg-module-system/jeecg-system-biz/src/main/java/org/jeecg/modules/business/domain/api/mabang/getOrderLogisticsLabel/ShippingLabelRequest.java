package org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalResponse;
import org.springframework.http.ResponseEntity;

/**
 * This class contains some key information and necessary procedures
 * to send a request to mabang "wl-get-order-logistics-label" API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 * <p>
 * One can use static method {@code sendRequest} to send request with body,
 * and then get respective response. Or use instance of this class, see below.
 * <p>
 */
@Slf4j
public class ShippingLabelRequest extends Request {

    public ShippingLabelRequest(ShippingLabelRequestBody body) {
        super(body);
    }


    @Override
    public ShippingLabelResponse send() {
        ResponseEntity<String> res = rawSend();
        return ShippingLabelResponse.parse(JSON.parseObject(res.getBody()));
    }
}
