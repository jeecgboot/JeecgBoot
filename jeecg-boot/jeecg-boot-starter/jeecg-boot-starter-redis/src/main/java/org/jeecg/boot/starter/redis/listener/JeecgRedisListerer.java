package org.jeecg.boot.starter.redis.listener;

import org.jeecg.common.base.BaseMap;

/**
 * 自定义消息监听
 */
public interface JeecgRedisListerer {

    void onMessage(BaseMap message);

}
