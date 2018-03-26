package com.huajie.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONObject;

import com.huajie.app.service.UserAppService;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/app/UploadFiles"})
public class UploadFiles extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
        Map<String, Object> map=new HashMap<>();
        try {
            UserAppService bbsUserAppService = SpringHelper.getSpringBean("UserAppService");
            map = bbsUserAppService.uploadFiles(this.getServletConfig(),request,response);
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