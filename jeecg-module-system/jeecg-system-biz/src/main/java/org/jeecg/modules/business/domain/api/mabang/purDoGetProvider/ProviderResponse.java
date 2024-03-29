package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
public class ProviderResponse extends Response {
    /**
     * total page number
     */
    @Getter
    private final int totalPage;
    /**
     * rows per page
     */
    @Getter
    public final int rowsPerPage = 50;
    /**
     * Total data number
     */
    private final int total;
    /**
     * Current page data
     */
    @Getter
    private final JSONArray data;

    private final JSONObject rawData;

    ProviderResponse(Code code, int totalPage, int total, JSONArray data, JSONObject rawData) {
        super(code);
        this.totalPage = totalPage;
        this.total = total;
        this.data = data;
        this.rawData = rawData;
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws ProviderRequestErrorException if response code represents error.
     */
    public static ProviderResponse parse(JSONObject json) throws ProviderRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new ProviderRequestErrorException(json.getString("message"));

        JSONObject data = json.getJSONObject("data");
        System.out.println("data from API request : " + data);
        int total = Integer.parseInt(data.getString("count"));
        int totalPage = (int)Math.ceil((double) total /50);
        JSONArray realData = data.getJSONArray("data");
        if(realData != null) {
            log.info("Constructed response: data contained {}, total page {}, rows per page {} total data {}", realData.size(), totalPage, 50, total);
        }
        else {
            log.info("Data is null");
        }
        return new ProviderResponse(Code.SUCCESS, totalPage, total, realData, json);
    }

    public JSONObject getRawDate() {
        return rawData;
    }

    public int getDataCount() {
        return total;
    }

    @Override
    public String toString() {
        return "ProviderResponse{" +
                "pageCount=" + totalPage +
                ", rowPerPage=" + rowsPerPage +
                ", dataCount=" + total +
                '}';
    }
}
