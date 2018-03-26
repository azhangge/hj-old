package com.huajie.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.entity.SystemInfo;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

//@WebFilter(urlPatterns = {"/talk/*"})
public class FrontPageFilter
        implements Filter {

    private FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	HttpServletRequest r = (HttpServletRequest) request;
    	ClientSession cs = (ClientSession) r.getSession().getAttribute("clientSession");
    	if(cs==null||cs.getUsr()==null){
    		String path = r.getServletPath();
    		if(path!=null&&!path.contains("/talk/LessonList.jspx")&&!path.contains("talk/LessonTypeList2.jspx")
    				&&!path.contains("/talk/Register")&&!path.contains("/talk/GetPWD")&&!path.contains("ShowImage")&&!path.contains("ShowAbstractImage")&&!path.contains("Agreement")){
    			String redirectPath = r.getScheme()+"://"+r.getServerName()+":"+ r.getServerPort()+r.getContextPath();
    			((HttpServletResponse) response).sendRedirect(redirectPath);
    		}
    	}
    	chain.doFilter(request, response);
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig == null) {
            return;
        }
    }

    public String toString() {
        if (this.filterConfig == null) {
            return "Authentication()";
        }
        StringBuffer sb = new StringBuffer("Authentication(");
        sb.append(this.filterConfig);
        sb.append(")");
        return sb.toString();
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception localException) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        this.filterConfig.getServletContext().log(msg);
    }
}