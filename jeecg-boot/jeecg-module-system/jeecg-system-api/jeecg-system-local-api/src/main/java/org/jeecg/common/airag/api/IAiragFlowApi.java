package org.jeecg.common.airag.api;

import org.jeecg.common.api.dto.AiragFlowDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * airag工作流运行API
 * 供system等基础模块软依赖AI工作流能力，实现由jeecg-boot-module-airag-flow模块提供；
 * 未集成AI模块时无实现bean，注入方使用ObjectProvider判空即可正常启动
 *
 * @author zhang
 * @date 2026-09-10
 */
public interface IAiragFlowApi {

	/**
	 * 运行AI工作流
	 *
	 * @param airagFlowDTO 流程运行参数
	 * @return 流程执行结果
	 */
	Object runFlow(AiragFlowDTO airagFlowDTO);

	/**
	 * 流式运行AI工作流
	 *
	 * @param airagFlowDTO 流程运行参数
	 * @return SSE流
	 */
	SseEmitter runFlowStream(AiragFlowDTO airagFlowDTO);
}
