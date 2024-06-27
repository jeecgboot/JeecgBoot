package org.jeecg.modules.business.domain.api.yd;

import com.alibaba.fastjson.JSONObject;

public class YDTrackingNumberRequestBody extends YDRequestBody {

    private static final String SERVICE_METHOD = "gettrackingnumber";
    private static final String REFERENCE_NO = "reference_no";

    public YDTrackingNumberRequestBody(String platformOrderId) {
        super(SERVICE_METHOD, generateJsonString(platformOrderId));
    }

    private static String generateJsonString(String platformOrderId) {
        JSONObject param = new JSONObject();
        param.put(REFERENCE_NO, platformOrderId);
        return param.toJSONString();
    }
}
