package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.Response;

public class ClearLogisticResponse extends Response {


    private ClearLogisticResponse(Code status) {
        super(status);
    }

    public static ClearLogisticResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        if (code.equals("200")) {
            return new ClearLogisticResponse(Code.SUCCESS);
        } else {
            return new ClearLogisticResponse(Code.ERROR);
        }
    }
}
