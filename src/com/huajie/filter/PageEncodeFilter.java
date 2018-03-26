package com.huajie.filter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.huajie.util.MyLogger;


@WebFilter(urlPatterns = {"/*"})
public class PageEncodeFilter
        implements Filter {

    private FilterConfig filterConfig = null;
    private static final boolean debug = true;

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) request;
        HttpSession session = r.getSession();//避免“Cannot create a session after the response has been committed”错误
        r.setCharacterEncoding("UTF-8");
        
        if (!(request instanceof HttpServletRequest)) {  
           chain.doFilter(request, response);  
           return;  
       }  
  
       HttpServletRequest httpRequest = (HttpServletRequest) request;  
       HttpServletResponse httpResponse = (HttpServletResponse) response;  
  
       // clear session if session id in URL  
       if (httpRequest.isRequestedSessionIdFromURL()) {  
           HttpSession session1 = httpRequest.getSession();  
           if (session != null) session1.invalidate();  
       }  
  
       // wrap response to remove URL encoding  
       HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {  
           @Override  
           public String encodeRedirectUrl(String url) {  
               return url;  
           }  
  
           @Override  
           public String encodeRedirectURL(String url) {  
               return url;  
           }  
  
           @Override  
           public String encodeUrl(String url) {  
               return url;  
           }  
  
           @Override  
           public String encodeURL(String url) {  
               return url;  
           }  
       };  
        
        
        try {
            chain.doFilter(request, wrappedResponse);
        } catch (Throwable t) {
        	MyLogger.error(t);
        	Runtime run = Runtime.getRuntime();
        	long max = run.maxMemory();
        	long total = run.totalMemory();
        	long free = run.freeMemory();
        	long usable = max - total + free;
        	System.out.println("剩余内存"+usable/1024/1024+"M线程数："+Thread.getAllStackTraces().size());
            t.printStackTrace();
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

    public void sessionLog(HttpServletRequest request, HttpSession session) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ddd = df.format(date);


        String ip = request.getRemoteAddr();


        String str = "Time:" + ddd + ",IP:" + ip;

        Calendar c = Calendar.getInstance();
        int y = c.get(c.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(c.DAY_OF_MONTH);
        String fileName = y + "_" + m + "_" + d + ".log";
        String path = session.getServletContext().getRealPath("logs/" + fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path,
                    true));
            PrintWriter out = new PrintWriter(bw);
            out.println(str);
            bw.flush();
            bw.close();
            out.close();
            //System.out.println(str);
        } catch (Exception e) {
        	MyLogger.error(e);
            e.printStackTrace();
        }
    }
}
