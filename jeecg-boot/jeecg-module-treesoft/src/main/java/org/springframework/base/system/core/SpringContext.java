package org.springframework.base.system.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取ApplicationContext和Bean的工具类
 *
 * @author 00fly
 * @version [版本号, 2018年9月26日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param clazz
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
