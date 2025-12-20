package org.jeecg.config.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Shiro缓存清理
 * 在应用启动时清除所有的Shiro授权缓存
 * 主要用于解决重启项目，用户未重新登录，按钮权限不生效的问题
 */
@Component
public class ShiroCacheClearRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(ShiroCacheClearRunner.class);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) {
        // 清空所有授权redis缓存
        log.info("——— Service restart, clearing all user shiro authorization cache ——— ");
        redisUtil.removeAll(CommonConstant.PREFIX_USER_SHIRO_CACHE);

    }
}