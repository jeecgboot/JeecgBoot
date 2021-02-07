package org.jeecg.handler;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.redis.listener.JeecgRedisListerer;
import org.jeecg.common.base.BaseMap;
import org.jeecg.loader.DynamicRouteLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 路由刷新监听
 */
@Slf4j
@Component
public class LoderRouderHandler implements JeecgRedisListerer {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;


    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh();
    }

}