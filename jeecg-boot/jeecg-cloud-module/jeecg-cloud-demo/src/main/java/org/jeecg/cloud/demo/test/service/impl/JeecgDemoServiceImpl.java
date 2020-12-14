package org.jeecg.cloud.demo.test.service.impl;

import org.jeecg.cloud.demo.test.service.JeecgDemoService;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class JeecgDemoServiceImpl implements JeecgDemoService {
    @Override
    public Result<String> getMessage(String name) {
        return Result.OK("Hello" + name);
    }
}
