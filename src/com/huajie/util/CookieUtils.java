package com.huajie.util;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.business.service.impl.*;

public class CookieUtils {
	
	static IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
	
	static BusinessUserService businessUserService = SpringHelper.getSpringBean("BusinessUserService");

	static Logger logger = LoggerFactory.getLogger(CookieUtils.class);
	
	public static void writeCookie(String businessId, HttpServletRequest request, HttpServletResponse response){
		String domain = getDomain(request);
		Cookie businessCookie = new Cookie(domain, businessId);
        businessCookie.setPath("/");
        businessCookie.setMaxAge(-1);
        response.addCookie(businessCookie);
    }

//	public static String getBusinessId(HttpServletRequest request){
//		if(request==null){
//			WebApplicationContext wac = (WebApplicationContext)SpringHelper.getSpringApplicationCtx();
//			ServletContext sc= wac.getServletContext();
//			if(sc.getAttribute("businessId")!=null){
//				return sc.getAttribute("businessId").toString();
//			}
//			return null;
//		}
//		
//		String domain = getDomain(request);
//        Cookie[] cookies = request.getCookies();
//        if (cookies!=null && cookies.length > 1){
//        	for (Cookie cookie:cookies){
//                if (domain.equals(cookie.getName())){
//                    return cookie.getValue();
//                }
//            }
//        }
//        BusinessUser businessUser = businessUserDao.findBussinessUserByDomain(getDomain(request));
//        if(businessUser!=null){
//        	return businessUser.getId();
//        }
//        return null;
//    }
	
	public static String getBusinessId(HttpServletRequest request){
		if(request==null){
			BusinessUser businessUser = businessUserService.findDefaultBusinessUser();
			return businessUser.getId();
		}
		
		String domain = getDomain(request);
		BusinessUser businessUser = businessUserService.findBusinessUserByDomain(domain);
		if(businessUser != null) return businessUser.getId();
		logger.info("domain:"+domain+",没有找到企业信息。使用默认企业!");
		return businessUserService.findDefaultBusinessUser().getId();
    }
	
	public static String getDomain(HttpServletRequest request){
		String host = request.getHeader("host");
		System.out.println("host:---------------->"+host);
		logger.info("host:"+host);
		if(host.indexOf(":")>1){
			return host.substring(0,host.lastIndexOf(":"));
		}
        return host;
	}
}
