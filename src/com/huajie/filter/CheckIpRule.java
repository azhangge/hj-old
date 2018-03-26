package com.huajie.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.platform.entity.FireWall;
import com.hjedu.platform.entity.IpRule;
import com.hjedu.platform.service.impl.FireWallService;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;
import javax.servlet.http.Cookie;

@WebFilter(urlPatterns = {"/*"})
public class CheckIpRule implements Filter {
    
    private FilterConfig filterConfig = null;
    private static final boolean debug = true;
    FireWallService fireService = SpringHelper.getSpringBean("FireWallService");
    
    IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        //log("Authentication:DoBeforeProcessing");
    }
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        //log("Authentication:DoAfterProcessing");
    }
    
    
    private void handleCookieByNameAndDomain(HttpServletRequest request, HttpServletResponse response){
    	String businessId = CookieUtils.getBusinessId(request);
    	if(!StringUtil.isEmpty(businessId)){
    		return;
    	}
    	String host = request.getHeader("host");
        String domain = host.substring(0,host.lastIndexOf(":"));
        BusinessUser businessUser = businessUserDao.findBussinessUserByDomain(domain);
    	if(businessUser != null) {
    		CookieUtils.writeCookie(businessUser.getId(), request, response);
    	}
    }
    
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //log("Authentication:doFilter()");
    	
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	HttpServletResponse httpResponse = (HttpServletResponse)response;
//    	handleCookieByNameAndDomain(httpRequest, httpResponse);
        
        Throwable problem = null;
        HttpServletRequest r = (HttpServletRequest) request;
        r.setCharacterEncoding("UTF-8");
        
        boolean ifpasscheck = true;
        FireWall fire = fireService.findFireWall();
        //验证端口号
        try {
            int port = request.getServerPort();
            if (port != fire.getPort()) {
                fire.setPort(port);
                fireService.updateWall(fire);
            }
        } catch (Exception e) {
        	MyLogger.error(e);
            e.printStackTrace();
        }
        //检查防火墙规则
        if (fire.isIfInUse()) {
            String ip = request.getRemoteAddr();
            //System.out.println("防火墙验证客户地址："+ip);
            if (fire.isIfBlack()) {
                //黑名单判断规则：一旦IP处在黑名单中，则拒绝访问
                List<IpRule> rs = fireService.findAllBlackIpRule();
                for (IpRule rr : rs) {
                    if (IpHelper.compareIp(rr.getFromIp(), ip) && IpHelper.compareIp(ip, rr.getToIp())) {
                        ifpasscheck = false;
                        break;
                    }
                }
            } else {
                //白名单判断规则：一旦IP处在白名单中，则允许访问
                ifpasscheck = false;
                List<IpRule> rs = fireService.findAllWhiteIpRule();
                for (IpRule rr : rs) {
                    if (IpHelper.compareIp(rr.getFromIp(), ip) && IpHelper.compareIp(ip, rr.getToIp())) {
                        ifpasscheck = true;
                        break;
                    }
                }
            }
        }
        
        if (r.getRequestURI().contains("NotAllowed") || r.getRequestURI().contains("javax.faces.resource") || r.getRequestURI().contains(".jpg") || r.getRequestURI().contains(".css") || r.getRequestURI().contains("AdminLogin.jspx") || r.getRequestURI().contains("/m/") || r.getRequestURI().contains("/servlet/")) {
            //允许JS和图片被访问
            ifpasscheck = true;
        }
        
        if (!ifpasscheck) {
            
            try {
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/NotAllowed.jspx");
                chain.doFilter(request, response);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {
            try {
                chain.doFilter(request, response);
            } catch (Throwable t) {
                problem = t;
                MyLogger.error(t);
                t.printStackTrace();
            }
            
        }
        
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw ((ServletException) problem);
            }
            if (problem instanceof IOException) {
                throw ((IOException) problem);
            }
            sendProcessingError(problem, response);
        }
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
        //log("Authentication:Initializing filter");
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
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);
        
        if ((stackTrace != null) && (!stackTrace.equals(""))) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");
                
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>");
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception localException) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception localException1) {
            }
        }
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
