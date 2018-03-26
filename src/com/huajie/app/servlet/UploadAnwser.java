package com.huajie.app.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import com.huajie.app.service.UserAppService;
import com.huajie.app.util.StringUtil;
import com.huajie.app.view.CarouselGetView;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/app/UploadAnwser"})
public class UploadAnwser extends HttpServlet{
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuffer json = new StringBuffer();   
        String line = null;
        Map<String, Object> map = new HashMap<>();
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
            JSONObject myJsonObject = new JSONObject(json.toString());
            if(StringUtil.isNotEmpty(myJsonObject.getString("token"))||StringUtil.isNotEmpty(myJsonObject.getString("paper_id"))){
            	String token = myJsonObject.getString("token");
            	String paper_id = myJsonObject.getString("paper_id");
            	JSONArray questionResultList = myJsonObject.getJSONArray("questionResultList");
            	UserAppService bbsUserAppService = SpringHelper.getSpringBean("UserAppService");
                String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
                map = bbsUserAppService.uploadAnwser(token,paper_id,questionResultList);
            }else{//没有获取到token
            	map.put("result","2");
            }
            
            JSONObject jsonObject = new JSONObject(map);
            out.print(jsonObject);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
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
