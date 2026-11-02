package org.jeecg.common.airag.api.fallback;

import lombok.Setter;
import org.jeecg.common.airag.api.IAiragFlowApi;
import org.jeecg.common.api.dto.AiragFlowDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * airag工作流运行API 降级实现
 *
 * @author zhang
 * @date 2026-09-10
 */
public class AiragFlowApiFallback implements IAiragFlowApi {

	@Setter
	private Throwable cause;

	@Override
	public Object runFlow(AiragFlowDTO airagFlowDTO) {
		return null;
	}

	@Override
	public SseEmitter runFlowStream(AiragFlowDTO airagFlowDTO) {
		return null;
	}

}
