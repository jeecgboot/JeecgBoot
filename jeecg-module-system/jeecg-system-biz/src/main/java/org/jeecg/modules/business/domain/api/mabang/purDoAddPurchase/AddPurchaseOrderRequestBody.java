package org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import java.util.List;
import java.util.function.Function;

public class AddPurchaseOrderRequestBody implements RequestBody {
    private String providerName;
    private String employeeName;
    private String content;
    private List<SkuStockData> stockData;

    private final static String DEFAULT_WAREHOUSE_NAME = "SZBA宝安仓";

    public AddPurchaseOrderRequestBody(String employeeName, String providerName, String content, List<SkuStockData> stockData) {
        this.stockData = stockData;
        this.providerName = providerName;
        this.employeeName = employeeName;
        this.content = content;
    }

    @Override
    public String api() {
        return "pur-do-add-purchase";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "warehouseName", DEFAULT_WAREHOUSE_NAME);
        putNonNull(json, "providerName", providerName);
        putNonNull(json, "employeeName", employeeName);
        JSONArray stockDataArray = new JSONArray();
        if (!stockData.isEmpty()) {
            for (SkuStockData s : stockData) {
                JSONObject stockData = new JSONObject();
                stockData.put("stockSku", s.getStockSku());
                stockData.put("price", s.getPrice());
                stockData.put("purchaseNum", s.getPurchaseNum());
                stockDataArray.add(stockData);
            }

        }
        json.put("stockList", stockDataArray.toJSONString());
        return json;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }

    private <E, T> void putNonNull(JSONObject json, String key, E value, Function<E, T> mapper) {
        if (value != null) {
            json.put(key, mapper.apply(value));
        }
    }


}
