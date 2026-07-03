package org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

@Slf4j
@Getter
public class OrderDeliverResponse extends Response {

    private final String message;

    OrderDeliverResponse(Code successCode, String message) {
        super(successCode);
        this.message = message;
    }

    public static OrderDeliverResponse parse(JSONObject json) throws OrderDeliverRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        String message = json.getString("message");
        if (Code.SUCCESS.value.equals(code)) {
            return new OrderDeliverResponse(Code.SUCCESS, message);
        }
        return new OrderDeliverResponse(Code.ERROR, message);
    }

    @Override
    public String toString() {
        return "OrderDeliverResponse{" +
                ", code=" + this.success() +
                ", message='" + message + '\'' +
                '}';
    }
}
