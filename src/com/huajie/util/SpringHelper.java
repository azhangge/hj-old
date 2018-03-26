package com.huajie.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringHelper implements ApplicationContextAware{

    private static ApplicationContext applicationContext;//对单例进行滞后初始化

    /**
     * 返回Spring中的Bean
     * @param <T>
     * @param name
     * @return 
     */
    public static <T> T getSpringBean(String name) {
        synchronized (SpringHelper.class) {//《Java并发编程》[Lea,2000]一书建议使用属于当前类的锁进行同步
            if (applicationContext == null) {
            	applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
            }
        }
        T bean = (T) applicationContext.getBean(name);
        return bean;
    }

    public static ApplicationContext getSpringWebCtx() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            ServletContext sc = (ServletContext) fc.getExternalContext().getContext();
            if (sc != null) {
                WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
                return wac;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void buildSpringWebCtx(ServletContext sc) {
    	applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        System.out.println("WebApplicationContext:"+applicationContext);
    }

    public static void main(String args[]) {
    }

    /*
     * 实现了ApplicationContextAware 接口，必须实现该方法；
     *通过传递applicationContext参数初始化成员变量applicationContext
     */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringHelper.applicationContext = applicationContext;
	}
	
    public static ApplicationContext getSpringApplicationCtx() {
    	return applicationContext;
    }
}
