package com.huajie.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringHelper2 {

    private static ApplicationContext ac = null;//对单例进行滞后初始化

    /**
     * 返回Spring中的Bean
     * @param <T>
     * @param name
     * @return 
     */
    public static <T> T getSpringBean(String name) {
        synchronized (SpringHelper.class) {//《Java并发编程》[Lea,2000]一书建议使用属于当前类的锁进行同步
            if (ac == null) {
                ac = getSpringApplicationCtx();
            }
        }
        T bean = (T) ac.getBean(name);
        return bean;
    }

    public static ApplicationContext getSpringApplicationCtx() {
        //ApplicationContext ctx = getSpringWebCtx();
        ApplicationContext ctx = null;
        //ctx = getSpringWebCtx();
        if (ctx == null) {
            ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        }
        return ctx;
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
        ac = WebApplicationContextUtils.getWebApplicationContext(sc);
        System.out.println("WebApplicationContext:"+ac);
    }

    public static void main(String args[]) {
    }
}
