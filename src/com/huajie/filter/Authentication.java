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

@WebFilter(urlPatterns = {"/m/*"})
public class Authentication
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
        r.setCharacterEncoding("UTF-8");

        boolean ifpasscheck = true;
        HttpSession session = ((HttpServletRequest) request).getSession();

        AdminSessionBean asb = (AdminSessionBean) session.getAttribute("adminSessionBean");

        if (asb == null) {
            ifpasscheck = false;
        } else if (!asb.isIfLogin()) {
            ifpasscheck = false;
        }

        if (!ifpasscheck) {
            try {
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/AdminLogin.jspx");
                chain.doFilter(request, response);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                chain.doFilter(request, response);
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