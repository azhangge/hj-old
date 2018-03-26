
<%@page import="java.net.URLDecoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.service.AlipayService,com.huajie.util.*,com.huajie.rerebbs.dao.IOnlinePayConfigDAO,java.util.*,com.huajie.seller.dao.ISaleOrderDAO,com.huajie.service.FinanceService,com.huajie.seller.model.SaleOrder,com.huajie.exam.web.mb.ClientSession" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            boolean ifOk=false;
            IOnlinePayConfigDAO aliConfigDAO= SpringHelper.getSpringBean("OnlinePayConfigDAO");
            AlipayService aliService = SpringHelper.getSpringBean("AlipayService");
            ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");
            FinanceService financeService = SpringHelper.getSpringBean("FinanceService");
            Enumeration enums = request.getParameterNames();
            List<String> strs = new ArrayList();
            String md5 = "";
            if (enums != null) {
                while (enums.hasMoreElements()) {
                    String key = enums.nextElement().toString();
                    String value = request.getParameter(key);
                    //value=new String(value.getBytes("iso8859-1"),"utf-8");
                    strs.add(key + "=" + value);
                    if ("sign".equalsIgnoreCase(key)) {
                        md5 = value;
                    }
                }
            }
            boolean re = aliService.checkReturn(strs, md5,aliConfigDAO.findOnlinePayConfig().getAlipayType());
            System.out.println(md5);
            if (!re) {
                out.print("cheat");
                System.out.println("淘宝发来了通知消息，但认证失败~~");
                return;
            }

            String orderId = request.getParameter("out_trade_no");
            String tradeNo = request.getParameter("trade_no");//淘宝交易号
            String buyerEmail = request.getParameter("buyer_email");//付款方支付宝帐号
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
                    ifOk=true;
                    //out.print("success");
                } else {
                    out.print("error");
                }
            } else if ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus)||"TRADE_SUCCESS".equalsIgnoreCase(tradeStatus)) {
                if (orderId != null) {
                    SaleOrder so = orderDAO.findSaleOrderByOid(orderId);
                    so.setStatus("paid");
                    orderDAO.updateSaleOrder(so);
                    financeService.processOrder(orderId, newMoney, so.getUser().getId());
                    ifOk=true;
                    //out.print("success");
                } else {
                    out.print("error");
                }
            }
            ClientSession cs = (ClientSession) request.getSession().getAttribute("clientSession");
            if (cs != null) {
                cs.refreshUser();
            }

        %>
    </body>
    
    <%if(ifOk){
    response.sendRedirect("CashPayResult.jspx");
    }else{
    %>
    
    
    支付完成，但业务处理出错。<br/>
    TradeNo:<%=orderId%><br/>
    TradeStatus:<%=tradeNo%><br/>
    BuyerEmail:<%=buyerEmail%><br/>
    TotalFee:<%=money%><br/>
    
    
    <%}%>
    
</html>
