package com.hjedu.platform.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/BizImage"})
public class BizImage extends HttpServlet
{
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String idt = null;
    int bufLen = 128;

    InputStream is = null;
    byte[] buf = new byte[bufLen];

    boolean ifnull = true;

    idt = request.getParameter("id").trim();

    if (idt != null) {
      String fp = request.getRealPath("resources/sys/") + "/" + idt + ".jpg";
      File f = new File(fp);
      if (f.exists()) {
        is = new FileInputStream(f);
        response.setContentType("image/gif");
        ServletOutputStream out = response.getOutputStream();
        while (is.read(buf, 0, bufLen) != -1) {
          out.write(buf);
        }
        is.close();
        out.flush();
        out.close();
        ifnull = false;
      }
    }

    if (ifnull) {
      String fp = request.getRealPath("resources/sys/default.png");
      File f = new File(fp);
      is = new FileInputStream(f);
      response.setContentType("image/gif");
      ServletOutputStream out = response.getOutputStream();
      while (is.read(buf, 0, bufLen) != -1) {
        out.write(buf);
      }
      is.close();
      out.flush();
      out.close();
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }

  public String getServletInfo()
  {
    return "Short description";
  }
}