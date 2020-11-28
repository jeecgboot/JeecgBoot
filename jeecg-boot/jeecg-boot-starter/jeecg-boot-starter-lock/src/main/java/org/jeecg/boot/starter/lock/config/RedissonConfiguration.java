package org.jeecg.boot.starter.lock.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.boot.starter.lock.core.RedissonManager;
import org.jeecg.boot.starter.lock.prop.RedissonProperties;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


/**
 * Redisson自动化配置
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {


	@Bean
	@ConditionalOnMissingBean
	@Order(value = 1)
	public RedissonManager redissonManager(RedissonProperties redissonProperties) {
		RedissonManager redissonManager = new RedissonManager(redissonProperties);
		log.info("RedissonManager初始化完成,当前连接方式:" + redissonProperties.getType() + ",连接地址:" + redissonProperties.getAddress());
		return redissonManager;
	}

	@Bean
	@ConditionalOnMissingBean
	@Order(value = 2)
	public RedissonLockClient redissonLock(RedissonManager redissonManager) {
		RedissonLockClient redissonLock = new RedissonLockClient();
		redissonLock.setRedissonManager(redissonManager);
		log.info("RedissonLock初始化完成");
		return redissonLock;
	}
}
