package com.exam.configuration.application;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @version 3.5.0
 * @description: The type Application context provider.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    private ApplicationContextProvider() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Gets bean.
     *
     * @param <T>    the type parameter
     * @param aClass the a class
     * @return the bean
     */
    public static <T> T getBean(Class<T> aClass) {
        return context.getBean(aClass);
    }

    /**
     * Gets bean.
     *
     * @param <T>  the type parameter
     * @param name the name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
}

