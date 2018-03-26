package com.huajie.corss.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.json.JSONObject;
import com.huajie.corss.json.CorssJson;
import com.huajie.corss.service.CorssService;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/corss/addUserToSubSystem"})
public class AddUserToSubSystem extends HttpServlet{
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
        try {
        	CorssService corssService = SpringHelper.getSpringBean("CorssService");
        	CorssJson corssJson = corssService.addUserToSubSystem(request);
            JSONObject jsonObject = new JSONObject(corssJson);
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
