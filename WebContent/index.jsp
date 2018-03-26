
<%@page import="com.huajie.app.util.FileUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page  import="com.hjedu.platform.dao.ISystemConfigDAO"%>
<%

    ISystemConfigDAO ts = com.huajie.util.SpringHelper.getSpringBean("SystemConfigDAO");
    boolean b = ts.getSystemConfig().getOpenLogin2();

    String wap = "/mobile/Default.jspx";

    String agent = request.getHeader("user-agent");
    //String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/534.51.22 (KHTML, like Gecko) Version/5.1.1 Safari/534.51.22";  
    String agentcheck = agent.trim().toLowerCase();
    boolean isWAP = false;
    String[] keywords = {"mobile", "android",
        "symbianos", "iphone", "wp\\d*", "windows phone",
        "mqqbrowser", "nokia", "samsung", "midp-2",
        "untrusted/1.0", "windows ce", "blackberry", "ucweb",
        "brew", "j2me", "yulong", "coolpad", "tianyu", "ty-",
        "k-touch", "haier", "dopod", "lenovo", "huaqin", "aigo-",
        "ctc/1.0", "ctc/2.0", "cmcc", "daxian", "mot-",
        "sonyericsson", "gionee", "htc", "zte", "huawei", "webos",
        "gobrowser", "iemobile", "wap2.0", "WAPI"};
    java.util.regex.Pattern pf = java.util.regex.Pattern.compile("wp\\d*");
    java.util.regex.Matcher mf = pf.matcher(agentcheck);
    if (agentcheck != null && (agentcheck.indexOf("windows nt") == -1 && agentcheck
            .indexOf("Ubuntu") == -1)
            || (agentcheck.indexOf("windows nt") > -1 && mf.find())) {
        for (int i = 0; i < keywords.length; i++) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(keywords[i]);
            java.util.regex.Matcher m = p.matcher(agentcheck);
            //排除 苹果桌面系统 和ipad 、iPod
            if (m.find() && agentcheck.indexOf("ipad") == -1
                    && agentcheck.indexOf("ipod") == -1
                    && agentcheck.indexOf("macintosh") == -1) {
                isWAP = true;
                break;
            }
        }
    }

    String path = "";
/*     if (b) {
        path = "/talk/PidClientLogin.jspx";
    } else {
        path = "/talk/Default.jspx";
    } */
	if(request.getServerPort()==7080){
		path = "/welcome.jspx";
	}else{
		/* path = "/HJ/index.jspx"; */
		/* path = "/JT/JTindex.jspx"; */
		String businessId = com.huajie.util.CookieUtils.getBusinessId(request);
		path = FileUtil.getWelcomePageByBusinessId(businessId);
	}
    RequestDispatcher dispatcher;
/*     if (isWAP) {
        dispatcher = this.getServletContext().getRequestDispatcher(wap);
    } else {
        dispatcher = this.getServletContext().getRequestDispatcher(path);
    } */
    response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+
            request.getServerPort()+request.getContextPath()+path);
	/* dispatcher = this.getServletContext().getRequestDispatcher(path);
    dispatcher.forward(request, response); */
    //return;
%>
