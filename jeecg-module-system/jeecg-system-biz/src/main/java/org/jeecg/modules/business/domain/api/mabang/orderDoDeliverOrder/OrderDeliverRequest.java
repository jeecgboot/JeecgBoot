package org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Request;
import org.springframework.http.ResponseEntity;

@Slf4j
public class OrderDeliverRequest extends Request {

    public OrderDeliverRequest(OrderDeliverRequestBody body) {
        super(body);
    }

    @Override
    public OrderDeliverResponse send() {
        ResponseEntity<String> res = rawSend();
        return OrderDeliverResponse.parse(JSON.parseObject(res.getBody()));
    }
}
