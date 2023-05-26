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
    private Integer page = 1;

    @Override
    public String api() {
        return "order-get-order-list";
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
        putNonNull(json, "page", page);
        return json;
    }

    void nextPage() {
        setPage(this.page + 1);
    }

    int getPage() {
        return page;
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

    public OrderListRequestBody setPage(int page) {
        this.page = page;
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
