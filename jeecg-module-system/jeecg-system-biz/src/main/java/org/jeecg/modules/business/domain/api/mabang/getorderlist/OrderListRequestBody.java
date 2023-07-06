package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class OrderListRequestBody implements RequestBody {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private OrderStatus status;
    private List<String> platformOrderIds;
    private DateType datetimeType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    // 1.Normal 2.Abnormal 3.All
    private final static String CAN_SEND = "3";
    private final String maxRows = "1000";
    private String cursor = "";
    private Integer page = 1;
    private boolean hasNext = true;

    @Override
    public String api() {
        return "order-get-order-list-new";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "status", status, OrderStatus::getCode);
        putNonNull(json, "platformOrderIds", platformOrderIds, (ids) -> String.join(",", ids));
        if(datetimeType != null && platformOrderIds == null){
            putNonNull(json, datetimeType.text() + "Start", startDate, formatter::format);
            putNonNull(json, datetimeType.text() + "End", endDate, formatter::format);
        }
        putNonNull(json, "canSend", CAN_SEND);
        putNonNull(json, "cursor", cursor);
        putNonNull(json, "maxRows", maxRows);
        return json;
    }

    String getCursor() {
        return cursor;
    }
    void setCursor(String cursor) {
        this.cursor = cursor;
    }
    int getPage() {
        return page;
    }
    void setPage(int page) {
        this.page = page;
    }
    void nextPage() {
        this.page += 1;
    }
    boolean getHasNext() { return hasNext; }
    void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    public OrderListRequestBody setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderListRequestBody setPlatformOrderIds(List<String> platformOrderIds) {
        this.platformOrderIds = platformOrderIds;
        return this;
    }

    public OrderListRequestBody setDatetimeType(DateType datetimeType) {
        this.datetimeType = datetimeType;
        return this;
    }

    public OrderListRequestBody setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public OrderListRequestBody setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public OrderListRequestBody setPage(String cursor) {
        this.cursor = cursor;
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
