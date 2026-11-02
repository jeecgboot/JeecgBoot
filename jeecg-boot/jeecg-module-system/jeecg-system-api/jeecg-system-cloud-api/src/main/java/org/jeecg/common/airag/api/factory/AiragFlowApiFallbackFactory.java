package org.jeecg.common.airag.api.factory;

import org.jeecg.common.airag.api.IAiragFlowApi;
import org.jeecg.common.airag.api.fallback.AiragFlowApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * airag工作流运行API 降级工厂
 *
 * @author zhang
 * @date 2026-09-10
 */
@Component
public class AiragFlowApiFallbackFactory implements FallbackFactory<IAiragFlowApi> {

	@Override
	public IAiragFlowApi create(Throwable cause) {
		AiragFlowApiFallback fallback = new AiragFlowApiFallback();
		fallback.setCause(cause);
		return fallback;
	}

}
