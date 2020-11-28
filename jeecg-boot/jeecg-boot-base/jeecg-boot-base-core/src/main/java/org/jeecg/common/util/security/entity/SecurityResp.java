package org.jeecg.common.util.security.entity;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class SecurityResp {
    private Boolean success;
    private JSONObject data;
}
