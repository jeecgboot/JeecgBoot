package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.DateType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Getter
@Setter
public class SkuStockRequestBody implements RequestBody {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // max 100, seperated by comma
    private String stockSkus = "";
    // day format
    private LocalDateTime updateTime;
    private static final String DEFAULT_WAREHOUSE_NAME = "SZBA宝安仓";
    private Integer page = 1;
    private Integer total;

    @Override
    public String api() {
        return "stock-get-stock-quantity";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSkus", stockSkus);
        putNonNull(json, "updateTime", updateTime, formatter::format);
        putNonNull(json, "warehouseName", DEFAULT_WAREHOUSE_NAME);
        return json;
    }

    void nextPage() {
        setPage(this.page + 1);
    }

    int getPage() {
        return page;
    }
    public SkuStockRequestBody setStockSkus(String stockSkus) {
        this.stockSkus = stockSkus;
        return this;
    }
    public SkuStockRequestBody setPage(int page) {
        this.page = page;
        return this;
    }
    public SkuStockRequestBody setTotal(int total) {
        this.total = total;
        return this;
    }
    public int getTotalPages() {
        return (int) Math.ceil((double) total / 100);
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
