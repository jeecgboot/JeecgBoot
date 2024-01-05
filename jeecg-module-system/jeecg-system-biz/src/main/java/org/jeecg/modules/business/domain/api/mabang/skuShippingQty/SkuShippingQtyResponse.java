package org.jeecg.modules.business.domain.api.mabang.skuShippingQty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.Response;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuListRequestErrorException;

/**
 * Immutable object
 */
@Slf4j
public class SkuShippingQtyResponse extends Response {
    /**
     * Current page
     * 当前页；当页上限100
     */
    @Getter
    private final int page;
    /**
     * Total data number
     * 总记录数
     */
    @Getter
    private final int count;
    /**
     * Current page data
     * 结果集合
     */
    @Getter
    private final JSONArray data;
    /**
     * Total page
     * 总页数
     */
    @Getter
    private final int totalPage;
    /**
     * Max data per page
     * 当页上限100
     */
    @Getter
    private final int maxDataPerPage = 100;

    SkuShippingQtyResponse(Code code, int page, int count, JSONArray data) {
        super(code);
        this.page = page;
        this.count = count;
        this.data = data;
        this.totalPage = (int) Math.ceil((double) count / maxDataPerPage);
    }

    /**
     * Make an instance by parsing json, it only checks validity of code.
     * if json is not valid, return null
     *
     * @param json the json to parse
     * @return Instance
     * @throws SkuListRequestErrorException if response code represents error.
     */
    public static SkuShippingQtyResponse parse(JSONObject json) throws SkuShippingQtyRequestErrorException {
        log.debug("Constructing a response by json.");
        String code = json.getString("code");
        if (code.equals(Code.ERROR.value))
            throw new SkuListRequestErrorException(json.getString("message"));
        JSONObject data = json.getJSONObject("data");
        int page = Integer.parseInt(data.getString("page"));
        int count = Integer.parseInt(data.getString("count"));
        JSONArray realData = data.getJSONArray("data");
        if(realData != null) {
            log.info("Constructed response: data contained {}, total data {}", realData.size(), count);
        }
        else {
            log.info("Data is null");
        }
        return new SkuShippingQtyResponse(Code.SUCCESS, page, count, realData);
    }
    @Override
    public String toString() {
        return "SkuListResponse{" +
                ", dataCount=" + getCount() +
                '}';
    }
}
