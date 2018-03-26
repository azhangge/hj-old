package com.huajie.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.json.JSONObject;

import com.huajie.app.util.DateUtil;
import com.huajie.app.util.SmsHelper;
import com.huajie.app.util.StringUtil;
import com.huajie.util.MyLogger;

@WebServlet(urlPatterns = {"/servlet/app/*"})
public class AppServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AppServlet.class);

	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		Date begin = new Date();
		long b = begin.getTime();
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        int i = request.getRequestURI().lastIndexOf("/");
        String methodName = request.getRequestURI().substring(i+1);
        methodName = methodName.substring(0, 1).toLowerCase()+methodName.substring(1);
        JSONObject jsonObject = null;
		try {
			Class<?> clazz = Class.forName("com.huajie.app.service.UserAppService");
			Method met = clazz.getMethod(methodName,HttpServletRequest.class);
			map = (Map<String, Object>) met.invoke(clazz.newInstance(), request);
			Date end = new Date();
			long e = end.getTime();
			long time = (e-b)/1000;
			System.out.println("调用接口方法："+methodName+",耗时"+time+"秒");
		} catch (Exception e) {
			map.put("result", 2);
			MyLogger.log(e);
			logger.error("调用接口方法："+methodName+"失败"+StringUtil.eToString(e));
			e.printStackTrace();
		} finally {
			jsonObject = new JSONObject(map);
        	out.print(jsonObject);
			out.close();
		}
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
