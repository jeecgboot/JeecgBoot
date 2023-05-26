package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
public class OrderListResponse extends Response {
    /**
     * total page number
     */
    private final int pageCount;
    /**
     * Total data number
     */
    private final int dataCount;
    /**
     * Current page data
     */
    private final JSONArray data;

    private final JSONObject rawData;

    OrderListResponse(Code code, int pageCount, int dataCount, JSONArray data, JSONObject rawData) {
        super(code);
        this.pageCount = pageCount;
        this.dataCount = dataCount;
        this.data = data;
        this.rawData = rawData;
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws OrderListRequestErrorException if response code represents error.
     */
    public static OrderListResponse parse(JSONObject json) throws OrderListRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new OrderListRequestErrorException(json.getString("message"));

        JSONObject data = json.getJSONObject("data");
        int pageCount = Integer.parseInt(data.getString("pageCount"));
        int dataCount = Integer.parseInt(data.getString("dataCount"));
        JSONArray realData = data.getJSONArray("data");
        log.info("Constructed response: data contained {}, total page {}, total data {}", realData.size(), pageCount, dataCount);
        return new OrderListResponse(Code.SUCCESS, pageCount, dataCount, realData, json);
    }

    public int getTotalPage() {
        return pageCount;
    }

    public JSONArray getData() {
        return data;
    }

    public JSONObject getRawDate() {
        return rawData;
    }

    public int getDataCount() {
        return dataCount;
    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "pageCount=" + pageCount +
                ", dataCount=" + dataCount +
                '}';
    }
}
