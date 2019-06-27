package com.ktp.project.logic;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author djcken
 * @date 2018/6/1
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }


    public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }


    public static <T> T getBean(Class<T> clz) {
        return (T) appCtx.getBean(clz);
    }

}