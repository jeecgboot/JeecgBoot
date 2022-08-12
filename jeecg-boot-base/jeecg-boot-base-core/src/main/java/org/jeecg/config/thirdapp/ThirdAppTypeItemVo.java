package org.jeecg.config.thirdapp;

import lombok.Data;

/**
 * 第三方App对接
 * @author: jeecg-boot
 */
@Data
public class ThirdAppTypeItemVo {

    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 应用Key
     */
    private String clientId;
    /**
     * 应用Secret
     */
    private String clientSecret;
    /**
     * 应用ID
     */
    private String agentId;
    /**
     * 目前仅企业微信用到：自建应用Secret
     */
    private String agentAppSecret;

    public int getAgentIdInt() {
        return Integer.parseInt(agentId);
    }

}
