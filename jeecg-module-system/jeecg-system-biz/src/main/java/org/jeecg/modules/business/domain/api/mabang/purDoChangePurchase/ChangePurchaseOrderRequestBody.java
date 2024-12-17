package org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.List;
import java.util.function.Function;

public class ChangePurchaseOrderRequestBody implements RequestBody {
    private final String employeeName;
    private final String groupId;
    private final String actionType;
    private final String scrapOrder;

    public ChangePurchaseOrderRequestBody(String employeeName, String groupId, String actionType, String scrapOrder) {
        this.employeeName = employeeName;
        this.groupId = groupId;
        this.actionType = actionType;
        this.scrapOrder = scrapOrder;
    }

    @Override
    public String api() {
        return "pur-do-change-purchase";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "employeeName", employeeName);
        putNonNull(json, "groupId", groupId);
        putNonNull(json, "actionType", actionType);
        putNonNull(json, "scrapOrder", scrapOrder);
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

    /**
     * 操作类型:
     * 1标记完成采购单;
     * 2作废采购单;
     * 3修改采购单基础信息;
     * 4修改采购单商品信息;
     * 5添加采购单商品；
     * 6：修改采购单拓展属性（仅限“自定义分类”）
     */
    public static enum ActionType {
        MARK_COMPLETE("1"),
        CANCEL("2"),
        CHANGE_BASIC_INFO("3"),
        CHANGE_PRODUCT_INFO("4"),
        ADD_PRODUCT("5"),
        CHANGE_EXPAND_PROPERTY("6");

        private final String value;

        ActionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    /**
     * 作废采购单商品类型：1.全部作废 2.部分作废 操作类型为2时必传
     */
    public static enum ScrapOrder {
        ALL("1"),
        PART("2");

        private final String value;

        ScrapOrder(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
