package org.jeecg.handler;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.modules.redis.listener.JeecgRedisListener;
import org.jeecg.loader.DynamicRouteLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 路由刷新监听（实现方式：redis监听handler）
 */
@Slf4j
@Component(GlobalConstants.LODER_ROUDER_HANDLER)
public class LoderRouderHandler implements JeecgRedisListener {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;


    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh(message);
    }

}