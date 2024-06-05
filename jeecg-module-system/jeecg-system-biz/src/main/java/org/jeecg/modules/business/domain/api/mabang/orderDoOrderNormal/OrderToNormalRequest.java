package org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.springframework.http.ResponseEntity;

/**
 * This class contains some key information and necessary procedures
 * to send a request to mabang "order-do-order-abnormal" API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 * <p>
 * One can use static method {@code sendRequest} to send request with body,
 * and then get respective response. Or use instance of this class, see below.
 * <p>
 */
@Slf4j
public class OrderToNormalRequest extends Request {

    public OrderToNormalRequest(OrderToNormalRequestBody body) {
        super(body);
    }


    @Override
    public OrderToNormalResponse send() {
        ResponseEntity<String> res = rawSend();
        return OrderToNormalResponse.parse(JSON.parseObject(res.getBody()));
    }
}
