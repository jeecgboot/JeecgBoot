package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;

/**
 * Immutable object
 */
@Slf4j
public class SkuListResponse extends Response {
    /**
     * Current page
     */
    private final int page;
    /**
     * total page number
     */
    private final int totalPage;
    /**
     * rows per page
     */
    private final int rowsPerPage;
    /**
     * Total data number
     */
    private final int total;
    /**
     * Current page data
     */
    private final JSONArray data;

    private final JSONObject rawData;

    SkuListResponse(Code code, int page, int totalPage, int rowsPerPage, int total, JSONArray data, JSONObject rawData) {
        super(code);
        this.page = page;
        this.totalPage = totalPage;
        this.rowsPerPage = rowsPerPage;
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
     * @throws SkuListRequestErrorException if response code represents error.
     */
    public static SkuListResponse parse(JSONObject json) throws SkuListRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new SkuListRequestErrorException(json.getString("message"));

        JSONObject data = json.getJSONObject("data");
        int page = Integer.parseInt(data.getString("page"));
        int totalPage = Integer.parseInt(data.getString("totalPage"));
        int rowPerPage = Integer.parseInt(data.getString("rowsPerPage"));
        int total = Integer.parseInt(data.getString("total"));
        JSONArray realData = data.getJSONArray("data");
        if(realData != null) {
            log.info("Constructed response: data contained {}, total page {}, rows per page {} total data {}", realData.size(), totalPage, rowPerPage, total);
        }
        else {
            log.info("Data is null");
        }
        return new SkuListResponse(Code.SUCCESS, page, totalPage, rowPerPage, total, realData, json);
    }

    public int getCurrentPage() {return page;}
    public int getTotalPage() {
        return totalPage;
    }

    public JSONArray getData() {
        return data;
    }

    public JSONObject getRawDate() {
        return rawData;
    }

    public int getDataCount() {
        return total;
    }

    @Override
    public String toString() {
        return "SkuListResponse{" +
                "pageCount=" + totalPage +
                ", rowPerPage=" + rowsPerPage +
                ", dataCount=" + total +
                '}';
    }
}
