package org.jeecg.modules.airag.llm.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 知识库配置
 *
 * @Author: chenrui
 * @Date: 2025-04-01 14:19
 */
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = KnowConfigBean.PREFIX)
public class KnowConfigBean {
    public static final String PREFIX = "jeecg.airag.know";

    /**
     * 开启MinerU解析
     */
    private boolean enableMinerU = false;

    /**
     * conda的环境(默认不使用conda)
     */
    private String condaEnv = null;

}
