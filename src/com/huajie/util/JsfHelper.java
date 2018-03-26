package com.huajie.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsfHelper {

    public static <T> T getBean(String beanName) {
        Object bean = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            bean = context.getApplication().getVariableResolver().resolveVariable(context, beanName);
        }
        return (T) bean;
    }

    public static <T> T getBean(FacesContext fc, String beanName) {
        Object bean = null;
        bean = fc.getApplication().getVariableResolver().resolveVariable(fc, beanName);
        return (T) bean;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest req = null;
		if (FacesContext.getCurrentInstance()!= null) {
			req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		}
        return req;
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse req = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        return req;
    }

    public static <T> T resolveVariable(FacesContext context, String v) {
        T bean = null;
        bean = (T) context.getApplication().createValueBinding(v).getValue(context);
        return bean;
    }

    public static void info(String str) {
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        fm.setSummary(str);
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    public static void warn(String str) {
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_WARN);
        fm.setSummary(str);
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    public static void error(String str) {
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        fm.setSummary(str);
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    /**
     * 获得flash，一般用于在页面跳转间保存临时变量
     * @return 
     */
    public static Flash getFlash() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }
}
