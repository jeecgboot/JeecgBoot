package org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Getter
@Setter
public class SkuListRequestBody implements RequestBody {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String stockSku = null;
    // 50 skus max
    private String stockSkuList = null;
    private DateType datetimeType = DateType.CREATE;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer page = 1;
    private Integer total;
    private String cursor = "1";
    private Integer maxRows = null;
    private Integer showVirtualSku = null;
    private Integer showProvider = null;
    private Integer showWarehouse = null;
    private Integer showLabel = null;
    private Integer showAttributes = null;
    private Integer showMachining = null;

    @Override
    public String api() {
        return "stock-do-search-sku-list-new";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSku", stockSku);
        putNonNull(json, "stockSkuList", stockSkuList);
        putNonNull(json, datetimeType.text() + "Start", startDate, formatter::format);
        putNonNull(json, datetimeType.text() + "End", endDate, formatter::format);
        putNonNull(json, "cursor", cursor);
        putNonNull(json, "maxRows", maxRows);
        putNonNull(json, "showVirtualSku", showVirtualSku);
        putNonNull(json, "showProvider", showProvider);
        putNonNull(json, "showWarehouse", String.valueOf(showWarehouse));
        putNonNull(json, "showLabel", String.valueOf(showLabel));
        putNonNull(json, "showAttributes", showAttributes);
        return json;
    }

    void nextPage() {
        setPage(this.page + 1);
    }

    int getPage() {
        return page;
    }

    public SkuListRequestBody setDatetimeType(DateType datetimeType) {
        this.datetimeType = datetimeType;
        return this;
    }
    public SkuListRequestBody setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }
    public SkuListRequestBody setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }
    public SkuListRequestBody setPage(int page) {
        this.page = page;
        return this;
    }
    public SkuListRequestBody setRowsPerPage(int rowsPerPage) {
        this.page = rowsPerPage;
        return this;
    }
    public SkuListRequestBody setSkuStockList(String skuStockList) {
        this.stockSkuList = skuStockList;
        return this;
    }
    public SkuListRequestBody setShowProvider(int showProvider) {
        this.showProvider = showProvider;
        return this;
    }
    public SkuListRequestBody setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }
    public SkuListRequestBody setTotal(int total) {
        this.total = total;
        return this;
    }
    public int getTotalPages() {
        return (int) Math.ceil((double) total / maxRows);
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
