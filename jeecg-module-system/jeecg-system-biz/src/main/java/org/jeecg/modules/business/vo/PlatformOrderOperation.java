package org.jeecg.modules.business.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PlatformOrderOperation {
    @JSONField(name = "shopId")
    private String shopId;
    @JSONField(name = "orderIds")
    private String orderIds;
    @JSONField(name = "action")
    private String action;
    @JSONField(name = "reason")
    private String reason;

    public enum Action {
        CANCEL("cancel"),
        SUSPEND("suspend");

        private final String action;

        Action(String value) {
            this.action = value;
        }

        public String getValue() {
            return action;
        }
    }
}
