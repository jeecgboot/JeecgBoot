package org.jeecg.modules.airag.llm.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: MCP
 * @Author: jeecg-boot
 * @Date:   2025-10-20
 * @Version: V1.0
 */
public interface IAiragMcpService extends IService<AiragMcp> {

    Result<String> edit(AiragMcp airagMcp);

    Result<?> sync(String id);


    Result<?> toggleStatus(String id, String action);

    /**
     * 保存插件工具（仅更新tools字段）
     * for [QQYUN-12453]【AI】支持插件
     * @param id 插件ID
     * @param tools 工具列表JSON字符串
     * @return 操作结果
     * @author chenrui
     * @date 2025/10/30
     */
    Result<String> saveTools(String id, String tools);
}
