package org.jeecg.modules.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: chenrui
 * @Date: 2025/8/21 17:19
 */
@Configuration
public class McpConfiguration {

    @Bean
    public ToolCallbackProvider weatherTools(UserMcpTool userMcpTool) {
        return MethodToolCallbackProvider.builder().toolObjects(userMcpTool).build();
    }
}
