package org.jeecg.modules.airag.llm.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 向量存储库配置
 *
 * @Author: chenrui
 * @Date: 2025/2/18 14:24
 */
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = EmbedStoreConfigBean.PREFIX)
public class EmbedStoreConfigBean {
    public static final String PREFIX = "jeecg.airag.embed-store";

    /**
     * host
     */
    private String host = "127.0.0.1";
    /**
     * 端口
     */
    private int port = 5432;
    /**
     * 数据库
     */
    private String database = "postgres";
    /**
     * 用户名
     */
    private String user = "postgres";
    /**
     * 密码
     */
    private String password = "postgres";

    /**
     * 存储向量的表
     */
    private String table = "embeddings";

}
