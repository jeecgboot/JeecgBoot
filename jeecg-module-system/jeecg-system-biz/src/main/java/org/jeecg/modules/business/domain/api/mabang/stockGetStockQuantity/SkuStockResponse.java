package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuListRequestErrorException;

/**
 * Immutable object
 */
@Slf4j
@Getter
public class SkuStockResponse extends Response {
    /**
     * Current page data
     */
    private final JSONArray data;

    private final JSONObject rawData;

    SkuStockResponse(Code code, JSONArray data, JSONObject rawData) {
        super(code);
        this.data = data;
        this.rawData = rawData;
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws SkuListRequestErrorException if response code represents error.
     */
    public static SkuStockResponse parse(JSONObject json) throws SkuListRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new SkuListRequestErrorException(json.getString("message"));

        JSONObject data = json.getJSONObject("data");
        System.out.println(data);
        JSONArray realData = data.getJSONArray("data");
        if(realData != null) {
            log.info("Constructed response: data contained {}", realData.size());
        }
        else {
            log.info("Data is null");
        }
        return new SkuStockResponse(Code.SUCCESS, realData, json);
    }

    public JSONObject getRawDate() {
        return rawData;
    }

    @Override
    public String toString() {
        return "SkuStockResponse{" +
                ", data=" + data +
                '}';
    }
}
