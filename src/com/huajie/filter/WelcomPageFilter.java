package com.huajie.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;

@WebFilter(urlPatterns = {"/HJ/index.jspx","/HJ/index.xhtml","/JT/JTindex.jspx","/JT/JTindex.xhtml","/welcome.jspx","/welcome.xhtml"})
public class WelcomPageFilter
        implements Filter {

    private FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	String businessId = CookieUtils.getBusinessId((HttpServletRequest) request);
    	String path = ((HttpServletRequest)request).getRequestURI();
    	if(StringUtil.isNotEmpty(path)&&!path.contains(FileUtil.getWelcomePageByBusinessId(businessId))){
    		((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath()+FileUtil.getWelcomePageByBusinessId(businessId));
    	}
//    	ISystemInfoDAO SystemInfoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
//    	SystemInfo systemInfo = SystemInfoDAO.findSystemInfo();
//    	if(systemInfo!=null){
//    		String name = systemInfo.getSiteName();
//    		if(name!=null&&name.contains("交通运输行政执法人员")){
//    			((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath()+"/welcome.jspx");
//    		}
//    	}
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