package org.jeecg.modules.demo.cloud.service;

import org.jeecg.common.api.vo.Result;

public interface JcloudDemoService {
    Result<String> getMessage(String name);
}
