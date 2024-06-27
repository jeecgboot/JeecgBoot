package org.jeecg.modules.business.domain.api.yd;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class YDParcelTraceRequestBody extends YDRequestBody {

    private static final String SERVICE_METHOD = "gettrack";
    private static final String TRACKING_NUMBER = "tracking_number";

    public YDParcelTraceRequestBody(List<String> billCodes) {
        super(SERVICE_METHOD, generateJsonString(billCodes));
    }

    private static String generateJsonString(List<String> billCodes) {
        JSONObject param = new JSONObject();
        String billCodesWithComas = String.join(",", billCodes);
        param.put(TRACKING_NUMBER, billCodesWithComas);
        return param.toJSONString();
    }
}
