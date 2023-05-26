package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.DateType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class SkuListRequestBody implements RequestBody {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String stockSku = "";
    private DateType datetimeType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer page = 1;
    private Integer rowsPerPage;
    private Integer showVirtualSku = 0;
    private Integer showProvider = 0;
    private Integer showWarehouse = 0;
    private Integer showLabel = 0;
    private Integer showAttributes = 0;

    @Override
    public String api() {
        return "stock-do-search-sku-list";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSku", stockSku);
        putNonNull(json, datetimeType.text() + "Start", startDate, formatter::format);
        putNonNull(json, datetimeType.text() + "End", endDate, formatter::format);
        putNonNull(json, "page", page);
        putNonNull(json, "rowsPerPage", rowsPerPage);
        putNonNull(json, "showVirtualSku", showVirtualSku);
        putNonNull(json, "showProvider", showProvider);
        putNonNull(json, "showWarehouse", showWarehouse);
        putNonNull(json, "showLabel", showLabel);
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
