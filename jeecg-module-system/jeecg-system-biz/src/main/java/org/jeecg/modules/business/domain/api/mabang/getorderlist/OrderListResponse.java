package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

import static cn.hutool.core.util.StrUtil.trim;

/**
 * Immutable object
 */
@Slf4j
public class OrderListResponse extends Response {
    /**
     * Response message
     */
    private String message;
    /**
     * Has next page
     */
    private boolean hasNext;
    /**
     *  下一页页码（加密串）
     */
    private String nextCursor;
    /**
     * Total data number
     */
    private final int dataCount;
    /**
     * Current page data
     */
    private final JSONArray data;

    private final JSONObject rawData;

    OrderListResponse(Code code, String message, String hasNext, String nextCursor, int dataCount, JSONArray data, JSONObject rawData) {
        super(code);
        setMessage(message);
        setHasNext(hasNext);
        setNextCursor(nextCursor);
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
        String message = json.getString("message");
        if (code.equals(Code.ERROR.value))
            throw new OrderListRequestErrorException(message);
        JSONObject data = json.getJSONObject("data");
        String hasNext = data.getString("hasNext");
        String nextCursor = data.getString("nextCursor");
        int dataCount = Integer.parseInt(data.getString("total"));
        JSONArray realData = data.getJSONArray("data");
        log.info("Constructed response: data contained {}, total data {}", realData.size(), dataCount);
        return new OrderListResponse(Code.SUCCESS, message, hasNext, nextCursor, dataCount, realData, json);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = trim(message);
    }
    public boolean getHasNext() {
        return hasNext;
    }
    public void setHasNext(String hasNext) {
        this.hasNext = hasNext.equals("true");
    }
    public String getNextCursor() {
        return this.nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
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
                ", dataCount=" + dataCount +
                '}';
    }
}
