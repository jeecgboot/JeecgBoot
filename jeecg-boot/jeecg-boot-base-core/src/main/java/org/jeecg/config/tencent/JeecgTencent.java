package org.jeecg.config.tencent;

import lombok.Data;

/**
* @Description: 腾讯短信配置
*
* @author: wangshuai
* @date: 2025/10/30 18:22
*/
@Data
public class JeecgTencent {

    /**
     * 接入域名
     */
    private String endpoint;
    
    /**
     * api秘钥id
     */
    private String secretId;
    
    /**
     * api秘钥key
     */
    private String secretKey;
    
    /**
     * 应用id
     */
    private String sdkAppId;

    /**
     * 地域信息
     */
    private String region;
}
