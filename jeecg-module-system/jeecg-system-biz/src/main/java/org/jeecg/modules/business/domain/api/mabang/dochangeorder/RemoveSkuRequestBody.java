package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.HashSet;

@Data
public class RemoveSkuRequestBody implements RequestBody {


    private String platformOrderId;
    private final HashSet<Pair<String, Integer>> virtualSkus;
    private final static String DEFAULT_WAREHOUSE_NAME = "SZBA宝安仓";

    public RemoveSkuRequestBody(String platformOrderId, HashSet<Pair<String, Integer>> virtualSkus) {
        this.platformOrderId = platformOrderId;
        this.virtualSkus = virtualSkus;
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        JSONArray stockDataArray = new JSONArray();
        if (!virtualSkus.isEmpty()) {
            for (Pair<String, Integer> virtualSku : virtualSkus) {
                JSONObject stockData = new JSONObject();
                stockData.put("warehouseName", DEFAULT_WAREHOUSE_NAME);
                stockData.put("stockSku", virtualSku.getKey());
                stockData.put("quantity", virtualSku.getValue());
                stockData.put("type", 2);
                stockDataArray.add(stockData);
            }
        }
        json.put("stockData", stockDataArray.toJSONString());
        return json;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
