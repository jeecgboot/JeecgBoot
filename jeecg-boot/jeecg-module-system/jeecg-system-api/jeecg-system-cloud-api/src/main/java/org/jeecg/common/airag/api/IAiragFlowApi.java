package org.jeecg.common.airag.api;

import org.jeecg.common.airag.api.fallback.AiragFlowApiFallback;
import org.jeecg.common.api.dto.AiragFlowDTO;
import org.jeecg.common.constant.ServiceNameConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * airag工作流运行API（微服务Feign版）
 * 单体模式下由airag-flow模块的AiragFlowApiImpl直接实现，本Feign接口不生效；
 * 微服务模式下通过Feign远程调用airag-flow服务暴露的HTTP端点
 *
 * @author zhang
 * @date 2026-09-10
 */
@Component
@FeignClient(contextId = "airagFlowRemoteApi", value = ServiceNameConstants.SERVICE_SYSTEM, fallbackFactory = AiragFlowApiFallback.class)
@ConditionalOnMissingClass("org.jeecg.modules.airag.flow.api.AiragFlowApiImpl")
public interface IAiragFlowApi {

	/**
	 * 运行AI工作流
	 *
	 * @param airagFlowDTO 流程运行参数
	 * @return 流程执行结果
	 */
	@PostMapping("/airag/flow/api/runFlow")
	Object runFlow(@RequestBody AiragFlowDTO airagFlowDTO);

	/**
	 * 流式运行AI工作流
	 *
	 * @param airagFlowDTO 流程运行参数
	 * @return SSE流
	 */
	@PostMapping("/airag/flow/api/runFlowStream")
	SseEmitter runFlowStream(@RequestBody AiragFlowDTO airagFlowDTO);
}
