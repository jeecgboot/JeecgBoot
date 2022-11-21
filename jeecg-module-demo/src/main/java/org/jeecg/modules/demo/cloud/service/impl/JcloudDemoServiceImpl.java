package org.jeecg.modules.demo.cloud.service.impl;

import org.jeecg.modules.demo.cloud.service.JcloudDemoService;
import org.springframework.stereotype.Service;

/**
 * @Description: JcloudDemoServiceImpl实现类
 * @author: jeecg-boot
 */
@Service
public class JcloudDemoServiceImpl implements JcloudDemoService {
    @Override
    public String getMessage(String name) {
        return "Hello，我是jeecg-demo服务节点，收到你的消息：【 "+ name +" 】";
    }
}
