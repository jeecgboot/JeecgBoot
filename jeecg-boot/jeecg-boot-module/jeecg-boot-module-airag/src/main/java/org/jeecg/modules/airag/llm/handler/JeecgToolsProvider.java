package org.jeecg.modules.airag.llm.handler;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.Getter;

import java.util.Map;

/**
 * for [QQYUN-13565]【AI助手】新增创建用户和查询用户的工具扩展
 * @Description: jeecg llm工具提供者
 * @Author: chenrui
 * @Date: 2025/8/26 18:06
 */
public interface JeecgToolsProvider {

    /**
     * 获取默认的工具列表
     * @return
     * @author chenrui
     * @date 2025/8/27 09:49
     */
    public Map<ToolSpecification, ToolExecutor> getDefaultTools();

    /**
     * jeecgLlm工具类
     * @author chenrui
     * @date 2025/8/27 09:49
     */
    @Getter
    class JeecgLlmTools{
        ToolSpecification toolSpecification;
        ToolExecutor toolExecutor;

        public JeecgLlmTools(ToolSpecification toolSpecification, ToolExecutor toolExecutor) {
            this.toolSpecification = toolSpecification;
            this.toolExecutor = toolExecutor;
        }


    }
}
