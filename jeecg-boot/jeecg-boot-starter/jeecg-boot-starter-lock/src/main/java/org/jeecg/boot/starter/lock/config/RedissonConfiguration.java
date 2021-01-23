package org.jeecg.boot.starter.lock.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.core.RedissonManager;
import org.jeecg.boot.starter.lock.prop.RedissonProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Redisson自动化配置
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedissonProperties.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {



	@Bean
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redissonClient(RedissonProperties redissonProperties) {
		RedissonManager redissonManager = new RedissonManager(redissonProperties);
		log.info("RedissonManager初始化完成,当前连接方式:" + redissonProperties.getType() + ",连接地址:" + redissonProperties.getAddress());
		return redissonManager.getRedisson();
	}

}
