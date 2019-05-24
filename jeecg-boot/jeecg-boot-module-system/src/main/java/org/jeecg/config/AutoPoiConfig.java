package org.jeecg.config;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Scott
 * @Date: 2018/2/7
 * @description: autopoi 配置类
 */

@Configuration
public class AutoPoiConfig {
	
	/**
	 * excel注解字典参数支持(导入导出字典值，自动翻译)
	 * 举例： @Excel(name = "性别", width = 15, dicCode = "sex")
	 * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
	 * 2、导入的时候，会把男、女翻译成1,2存进数据库;
	 * @return
	 */
	@Bean
	public ApplicationContextUtil applicationContextUtil() {
		return new org.jeecgframework.core.util.ApplicationContextUtil();
	}

}
