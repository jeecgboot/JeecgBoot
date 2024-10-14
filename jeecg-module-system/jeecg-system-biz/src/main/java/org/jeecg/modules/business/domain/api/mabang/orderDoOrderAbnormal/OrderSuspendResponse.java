package org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
@Getter
public class OrderSuspendResponse extends Response {

    OrderSuspendResponse(Code successCode) {
        super(successCode);
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws OrderSuspendRequestErrorException if response code represents error.
     */
    public static OrderSuspendResponse parse(JSONObject json) throws OrderSuspendRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals("200"))
            return new OrderSuspendResponse(Code.SUCCESS);
        else
            return new OrderSuspendResponse(Code.ERROR);
    }

    @Override
    public String toString() {
        return "OrderSuspendResponse{" +
                ", code=" + this.success() +
                '}';
    }
}
