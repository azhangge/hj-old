/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.service.impl.FinanceService;
import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.service.impl.AlipayService;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.SpringHelper;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Alipay1Notifier", urlPatterns = {"/servlet/Alipay1Notifier"})
public class Alipay1Notifier extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        AlipayService aliService = SpringHelper.getSpringBean("AlipayService");
        ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");
        FinanceService financeService = SpringHelper.getSpringBean("FinanceService");
        IOnlinePayConfigDAO aliConfigDAO= SpringHelper.getSpringBean("OnlinePayConfigDAO");
        Enumeration enums = request.getParameterNames();
        List<String> strs = new ArrayList();
        String md5 = "";
        if (enums != null) {
            while (enums.hasMoreElements()) {
                String key = enums.nextElement().toString();
                String value = request.getParameter(key);
                strs.add(key + "=" + value);
                if ("sign".equalsIgnoreCase(key)) {
                    md5 = value;
                }
            }
        }
        boolean re = aliService.checkReturn(strs, md5,aliConfigDAO.findOnlinePayConfig().getAlipayType());
        if (!re) {
            out.print("cheat");
            System.out.println("淘宝发来了通知消息，但认证失败~~");
            return;
        }

        String orderId = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");//淘宝交易号
        String tradeStatus = request.getParameter("trade_status");//交易状态
        String money = request.getParameter("total_fee");
        double newMoney = 0d;
        
        if (money != null) {
            newMoney = Double.parseDouble(money);
        }

        if ("WAIT_SELLER_SEND_GOODS".equalsIgnoreCase(tradeStatus)) {
            if (orderId != null) {
                SaleOrder so = orderDAO.findSaleOrderByOid(orderId);
                so.setStatus("paid");
                orderDAO.updateSaleOrder(so);
                financeService.processOrder(orderId, newMoney, so.getUser().getId());
                aliService.sendGoods(orderId, tradeNo);
                out.print("success");
            } else {
                out.print("error");
            }
        } else if ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus)||"TRADE_SUCCESS".equalsIgnoreCase(tradeStatus)) {
            if (orderId != null) {
                SaleOrder so = orderDAO.findSaleOrderByOid(orderId);
                so.setStatus("paid");
                orderDAO.updateSaleOrder(so);
                financeService.processOrder(orderId, newMoney, so.getUser().getId());
                out.print("success");
            } else {
                out.print("error");
            }

            ClientSession cs = (ClientSession) request.getSession().getAttribute("clientSession");
            if (cs != null) {
                cs.refreshUser();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
