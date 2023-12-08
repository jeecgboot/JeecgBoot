package org.jeecg.modules.business.domain.api.mabang.skuShippingQty;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

public class SkuShippingQtyRequestBody implements RequestBody {

    private String stockSkus;
    private LocalDate updateTime = LocalDate.now();
    private String warehouseName;
    private Integer page = 1;
    @Override
    public String api() {
        return "stock-get-stock-quantity";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSkus", stockSkus);
        putNonNull(json, "updateTime", updateTime, LocalDate::toString);
        return json;
    }

    public SkuShippingQtyRequestBody setStockSkus(String stockSkus) {
        this.stockSkus = stockSkus;
        return this;
    }
    void nextPage() {
        setPage(this.page + 1);
    }

    private SkuShippingQtyRequestBody setPage(int i) {
        this.page = i;
        return this;
    }

    int getPage() {
        return page;
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
