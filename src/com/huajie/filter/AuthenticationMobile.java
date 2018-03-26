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
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.util.MyLogger;
import com.huajie.util.PartialRedirectHelper;
import com.huajie.util.SpringHelper;

@WebFilter(urlPatterns = {"/mobile/*", "/servlet/mobile/*"})
public class AuthenticationMobile
        implements Filter {

    private FilterConfig filterConfig = null;
    private static final boolean debug = true;

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        //log("Authentication:DoBeforeProcessing");
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        //log("Authentication:DoAfterProcessing");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //log("Authentication:doFilter()");

        Throwable problem = null;
        HttpServletRequest r = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        r.setCharacterEncoding("UTF-8");
        HttpSession session = ((HttpServletRequest) request).getSession();

        //如果是登录页面，则不验证是否登录
        String url = r.getRequestURI();
        if (url.contains("MobileLogin") || url.contains("Register") || url.contains("WaitingCheck") || url.contains("RegEmailOut")|| url.contains("MobileKickedOut")||url.contains("MobileExamModule2List2")||url.contains("MobileExamCaseModule22")) {
            //((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/mobile/MobileLogin.jspx");
            chain.doFilter(request, response);
            return;
        }

        boolean filtered = false;

        //开始检查是否被踢出
        String sid = r.getSession().getId();
        boolean ifPassKick = true;
        ifPassKick = this.checkKickedSession(sid);

        if (!ifPassKick) {
            try {
                //将被踢出的用户清理出SESSION登录状态
                ClientSession cs = (ClientSession) r.getSession().getAttribute("clientSession");
                boolean login = false;//记录是否在登录状态，只有在登录状态的用户才需要跳转
                if (cs != null) {
                    if (cs.isIfLogin()) {
                        login = true;
                    }
                    cs.setUsr(null);
                    cs.setIfLogin(false);
                }
                if (login) {//在登录状态跳转才有意义
                    try {
                        if (PartialRedirectHelper.isAjax(r)) {//AJAX跳转
                            MyLogger.println("Repeated login, partial redirect.");

                            resp.getWriter().print(PartialRedirectHelper.xmlPartialRedirectToPage(r, r.getContextPath() + "/mobile/MobileKickedOut.jspx"));
                            resp.flushBuffer();
                        } else {//非AJAX跳转
                            MyLogger.println("Repeated login, non-partial redirect.");
                            resp.sendRedirect(r.getContextPath() + "/mobile/MobileKickedOut.jspx");
                            chain.doFilter(request, response);
                        }
                    } catch (Exception e) {
                        // redirect to error page
                        r.getSession().setAttribute("lastException", e);
                        r.getSession().setAttribute("lastExceptionUniqueId", e.hashCode());
                        MyLogger.println("Ajax Redirect Error.");
                        if (!PartialRedirectHelper.isAjax(r)) {
                            resp.sendRedirect(r.getContextPath() + r.getServletPath() + "/error");
                        } else {
                            // let's leverage jsf2 partial response
                            resp.getWriter().print(PartialRedirectHelper.xmlPartialRedirectToPage(r, "/error"));
                            resp.flushBuffer();
                        }
                    }
                    filtered = true;
                    return;//不再检查后续操作
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //检查踢出结束

        boolean ifpasscheck = true;
        ClientSession asb = (ClientSession) session.getAttribute("clientSession");

        if (asb == null) {
            ifpasscheck = false;
        } else if (!asb.isIfLogin()) {
            ifpasscheck = false;
        }

        if (!ifpasscheck) {
            try {
                if (!filtered) {
                    resp.sendRedirect(r.getContextPath() + "/mobile/MobileLogin.jspx");
                    chain.doFilter(request, response);
                    filtered = true;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (!filtered) {
                    chain.doFilter(request, response);
                    filtered = true;
                }
                return;
            } catch (Throwable t) {
                problem = t;
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

    /**
     *
     * @param sid
     * @return
     */
    public boolean checkKickedSession(String sid) {
        IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
        UserSessionState us = iussService.findKickedSession(sid);
        if (us != null) {
            if (us.isKicked()) {
                return false;//当前用户已经被踢出
            }
        }
        return true;
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
