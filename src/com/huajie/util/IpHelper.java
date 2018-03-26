package com.huajie.util;

import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public class IpHelper {

    /**
     *
     * @param o 原IP
     * @param target 目标IP
     * @return 如果原IP小于等于目标IP，则返回true，否则返回false
     */
    public static Boolean compareIp(String o, String target) {//not big than
        try {
            byte[] oo = InetAddress.getByName(o).getAddress();
            byte[] tt = InetAddress.getByName(target).getAddress();
            for (int i = 0; i < oo.length; i++) {
                int ooo = (oo[i] < 0) ? 256 + oo[i] : oo[i];
                int ttt = (tt[i] < 0) ? 256 + tt[i] : tt[i];
                //System.out.println(ooo+":"+ttt);
                if (ooo < ttt) {
                    return true;
                }
                if (ooo > ttt) {
                    return false;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获得客户IP地址,解决从NGINX或APACHE转发过来后无法获得真实客户IP的问题
     * @param request
     * @return 
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ipFromNginx = getHeader(request, "X-Real-IP");
        if (!StringUtils.isEmpty(ipFromNginx)) {
            return ipFromNginx;
        }
        String ipFromApache = getHeader(request, "x-forwarded-for");
        if (!StringUtils.isEmpty(ipFromApache)) {
            return ipFromApache;
        }
        return  request.getRemoteAddr();
    }

    private static String getHeader(HttpServletRequest request, String headName) {
        String value = request.getHeader(headName);
        return !StringUtils.isBlank(value) && !"unknown".equalsIgnoreCase(value) ? value : "";
    }

}
