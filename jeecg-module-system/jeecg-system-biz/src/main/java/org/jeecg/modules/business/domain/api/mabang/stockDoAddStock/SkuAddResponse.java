package org.jeecg.modules.business.domain.api.mabang.stockDoAddStock;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
@Getter
public class SkuAddResponse extends Response {
    /**
     * Current page data
     */
    private final JSONObject data;

    private final String stockId;

    private final String stockSku;

    public SkuAddResponse(Code code, JSONObject data, String stockId, String stockSku) {
        super(code);
        this.data = data;
        this.stockId = stockId;
        this.stockSku = stockSku;
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws SkuAddRequestErrorException if response code represents error.
     */
    public static SkuAddResponse parse(JSONObject json) throws SkuAddRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new SkuAddRequestErrorException(json.getString("message"));

        JSONObject data = json.getJSONObject("data");
        String stockId = data.getString("stockId");
        String stockSku = data.getString("stockSku");
        if(data != null) {
            log.info("Constructed response: data contained {}", data);
        }
        else {
            log.info("Data is null");
        }
        return new SkuAddResponse(Code.SUCCESS, data, stockId, stockSku);
    }



    @Override
    public String toString() {
        return "SkuListResponse{" +
                ", data=" + data +
                '}';
    }
}
