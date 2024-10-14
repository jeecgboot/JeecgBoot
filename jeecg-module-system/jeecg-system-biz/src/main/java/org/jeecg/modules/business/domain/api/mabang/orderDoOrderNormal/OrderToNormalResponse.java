package org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
@Getter
public class OrderToNormalResponse extends Response {
    private final String message;

    OrderToNormalResponse(Code successCode, String message) {
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
    public static OrderToNormalResponse parse(JSONObject json) throws OrderToNormalRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        String message = json.getString("message");
        if (code.equals("200"))
            return new OrderToNormalResponse(Code.SUCCESS, message);
        else
            return new OrderToNormalResponse(Code.ERROR, message);
    }

    @Override
    public String toString() {
        return "OrderToNormalResponse{" +
                ", code=" + this.success() +
                '}';
    }
}
