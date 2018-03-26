/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.service.impl;

import com.alipay.sign.AlipayMD5;
import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.OnlinePayConfig;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.NetworkReader;
import com.huajie.util.SpringHelper;

import com.alipay.config.AlipayConfig;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author Administrator
 */
public class AlipayService {

    private VelocityEngine velocityEngine;//spring配置中定义
    private IOnlinePayConfigDAO configDAO;

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public IOnlinePayConfigDAO getConfigDAO() {
        return configDAO;
    }

    public void setConfigDAO(IOnlinePayConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    public String buildAlipay1Param(SaleOrder order, String returnUrl, String notifyUrl) {
        try {
            String md5 = this.genRequestSign(order, returnUrl, notifyUrl);
            OnlinePayConfig config = this.configDAO.findOnlinePayConfig();

            returnUrl = URLEncoder.encode(returnUrl, AlipayConfig.input_charset);
            notifyUrl = URLEncoder.encode(notifyUrl, AlipayConfig.input_charset);
            Map map = new HashMap();

            String partner = config.getAlipay1Partner();
            String sellerEmail = config.getAlipaySellerEmail();
            sellerEmail = URLEncoder.encode(sellerEmail, AlipayConfig.input_charset);
            String service = config.getAlipayType();
            map.put("payType", service);
            map.put("partner", partner);
            map.put("sellerId", partner);
            map.put("sellerEmail", sellerEmail);
            map.put("returnUrl", returnUrl);
            map.put("notifyUrl", notifyUrl);

            if (order != null) {
                String oid = order.getOid();
                String body = order.getBody();
                double price = order.getPayMoney();
                int num = 1;
                String subject = order.getName();
                if (order.getTotalMoney() != order.getPayMoney()) {
                    subject += "（余款）";
                }
                subject = URLEncoder.encode(subject, AlipayConfig.input_charset);
                body = URLEncoder.encode(body, AlipayConfig.input_charset);

                map.put("body", body);
                map.put("orderNo", oid);
                map.put("price", price);
                map.put("quantity", num);
                map.put("totalFee", num*price);
                map.put("goodsName", subject);
            }

            String result = null;
            StringBuilder sb = new StringBuilder();

            result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), "alipay1_param.vm", "UTF-8", map);
            String params[] = result.split("&");
            List<String> paramList = Arrays.asList(params);
            Collections.sort(paramList);
            int size = paramList.size();
            for (int i = 0; i < size; i++) {
                String s = paramList.get(i);
                if (s == null || "".equals(s)) {
                    continue;
                }
                sb.append(s);
                if (size != i + 1) {
                    sb.append("&");
                }
            }

            //下三行运算去除回车符等特殊字符
            result = sb.toString();
            Pattern pat = Pattern.compile("\\s*|\n|\r|\t");
            Matcher mat = pat.matcher(result);
            result = mat.replaceAll("");

            result = result + "&sign_type=MD5&sign=" + md5;
            System.out.println("传出的参数字符串：" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String genRequestSign(SaleOrder order, String returnUrl, String notifyUrl) {
        OnlinePayConfig config = this.configDAO.findOnlinePayConfig();

        Map map = new HashMap();
        String partner = config.getAlipay1Partner();
        String sellerEmail = config.getAlipaySellerEmail();
        String key = config.getAlipay1Key();
        String service = config.getAlipayType();
        map.put("payType", service);
        map.put("partner", partner);
        map.put("sellerId", partner);
        map.put("sellerEmail", sellerEmail);
        map.put("returnUrl", returnUrl);
        map.put("notifyUrl", notifyUrl);

        if (order != null) {
            String oid = order.getOid();
            String body = order.getBody();
            double price = order.getPayMoney();
            int num = 1;
            String subject = order.getName();
            if (order.getTotalMoney() != order.getPayMoney()) {
                subject += "（余款）";
            }

            map.put("body", body);
            map.put("orderNo", oid);
            map.put("price", price);
            map.put("quantity", num);
            map.put("totalFee", num*price);
            map.put("goodsName", subject);
        }

        String result = null;
        StringBuilder sb = new StringBuilder();

        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), "alipay1_param.vm", "UTF-8", map);
            String params[] = result.split("&");
            List<String> paramList = Arrays.asList(params);
            Collections.sort(paramList);
            int size = paramList.size();
            for (int i = 0; i < size; i++) {
                String s = paramList.get(i);
                if (s == null || "".equals(s)) {
                    continue;
                }
                sb.append(s);
                if (size != i + 1) {
                    sb.append("&");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //下三行运算去除回车符等特殊字符
        result = sb.toString();
        Pattern pat = Pattern.compile("\\s*|\n|\r|\t");
        Matcher mat = pat.matcher(result);
        result = mat.replaceAll("");
        //msg为待签名字符串
        String msg = result;
        System.out.println("待签名串：" + msg);
        String md5 = AlipayMD5.sign(msg, key, AlipayConfig.input_charset);
        System.out.println("签名结果：" + md5);
        return md5;
    }

    public boolean checkReturn(List<String> paramList, String md5, String payType) {

        if ("create_direct_pay_by_user".equals(payType)) {
            return true;
        }

        OnlinePayConfig config = this.configDAO.findOnlinePayConfig();
        String key = config.getAlipay1Key();
        StringBuilder sb = new StringBuilder();

        try {

            Collections.sort(paramList);
            int size = paramList.size();
            for (int i = 0; i < size; i++) {
                String s = paramList.get(i);
                if (s == null || "".equals(s)) {
                    continue;
                }
                if (s.contains("sign_type=") || s.contains("sign=")) {
                    continue;
                }
                sb.append(s);
                if (size != i + 1) {
                    sb.append("&");
                }
            }
            //msg为待签名字符串
            String msg = sb.toString();
            System.out.println("原字符串：" + msg);
            //msg = URLDecoder.decode(msg, AlipayConfig.input_charset);
            System.out.println("待签名串：" + msg);
            String md51 = AlipayMD5.sign(msg, key, AlipayConfig.input_charset);
            System.out.println("签名结果：" + md51);
            return md5.equalsIgnoreCase(md51);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String buildReturnUrl1(HttpServletRequest request) {
        String path = "http://";
        String host = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();
        String returnPage = "/talk/alipay1_return.jsp";
        path = path + host + ":" + port + contextPath + returnPage;
        return path;
    }

    public String buildNotifierUrl1(HttpServletRequest request) {
        String path = "http://";
        String host = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();
        String returnPage = "/servlet/Alipay1Notifier";
        path = path + host + ":" + port + contextPath + returnPage;
        return path;
    }

    public boolean sendGoods(String oid, String tradeNo) {
        try {
            ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");

            OnlinePayConfig config = this.configDAO.findOnlinePayConfig();

            Map map = new HashMap();
            String partner = config.getAlipay1Partner();
            map.put("partner", partner);
            map.put("tradeNo", tradeNo);

            String result = null;
            StringBuilder sb = new StringBuilder();

            result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), "alipay_send_goods.vm", "UTF-8", map);
            String params[] = result.split("&");
            List<String> paramList = Arrays.asList(params);
            Collections.sort(paramList);
            int size = paramList.size();
            for (int i = 0; i < size; i++) {
                String s = paramList.get(i);
                if (s == null || "".equals(s)) {
                    continue;
                }
                sb.append(s);
                if (size != i + 1) {
                    sb.append("&");
                }
            }

            //下三行运算去除回车符等特殊字符
            result = sb.toString();
            Pattern pat = Pattern.compile("\\s*|\n|\r|\t");
            Matcher mat = pat.matcher(result);
            result = mat.replaceAll("");

            String md5 = AlipayMD5.sign(result, config.getAlipay1Key(), AlipayConfig.input_charset);
            System.out.println("待签名串：" + result);
            result = result + "&sign_type=MD5&sign=" + md5;
            System.out.println("传出的参数字符串：" + result);
            String path = configDAO.findOnlinePayConfig().getAlipay1Url() + "?" + result;
            String xml = NetworkReader.readUrl(path, AlipayConfig.input_charset);
            System.out.println("返回XML：" + xml);
            Document doc = DocumentHelper.parseText(xml);
            String code = doc.selectSingleNode("/alipay/is_success").getText();
            if (code == null) {
                return false;
            } else {
                code = code.trim().toUpperCase();
                if ("T".equals(code)) {
                    SaleOrder so = orderDAO.findSaleOrderByOid(oid);
                    if (so != null) {
                        so.setIfSendGoods(true);
                        orderDAO.updateSaleOrder(so);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String args[]) {

        String str = "_input_charset=utf-8&logistics_fee=0&logistics_payment=SELLER_PAY&logistics_type=EXPRESS&notify_url=http%3A%2F%2Flocalhost%3A8080%2Fmycms2%2Fservlet%2FAlipay1Notifier&out_trade_no=2014082300667&partner=2088002054778535&payment_type=1&price=1.0&quantity=1&return_url=http%3A%2F%2Flocalhost%3A8080%2Fmycms2%2Fc%2Falipay1_return.jsp&seller_email=lteb2002%40163.com&service=create_partner_trade_by_buyer&subject=%F2%F9%F2%F0%D4%DA%CF%DF%BF%BC%CA%D4%CF%B5%CD%B3%A3%A8%C6%F3%D2%B5%B0%E6%A3%A9";
        try {
            AlipayService as = SpringHelper.getSpringBean("AlipayService");
            String oid = "2014082300675";
            String tradeNo = "2014082315954853";
            as.sendGoods(oid, tradeNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
