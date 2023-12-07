package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonOrderCreationRequestBody extends OrderCreationRequestBody {

    private final String jsonString;
    public JsonOrderCreationRequestBody(String jsonString) {
        super(null, null);
        this.jsonString = jsonString;
    }

    @Override
    public JSONObject parameters() {
        return JSON.parseObject(jsonString);
    }
}
