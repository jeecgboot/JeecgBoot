package org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalRequestErrorException;

/**
 * Immutable object
 */
@Slf4j
@Getter
public class ShippingLabelResponse extends Response {
    private final String message;

    ShippingLabelResponse(Code successCode, String message) {
        super(successCode);
        this.message = message;
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws OrderToNormalRequestErrorException if response code represents error.
     */
    public static ShippingLabelResponse parse(JSONObject json) {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        String message = json.getString("message");
        if (code.equals("SUCCESS"))
            return new ShippingLabelResponse(Code.SUCCESS, message);
        else
            return new ShippingLabelResponse(Code.ERROR, message);
    }
}
