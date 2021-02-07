package org.jeecg.boot.starter.redis.client;

import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * redis客户端
 */
@Configuration
public class JeecgRedisClient {

    @Resource(name = "starterRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送消息
     *
     * @param handlerName
     * @param params
     */
    public void sendMessage(String handlerName, BaseMap params) {
        params.put(GlobalConstants.HANDLER_NAME, handlerName);
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }


    /**
     * 根据key查询缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }


}
