package com.huajie.app.util;

import java.util.regex.Pattern;

public class Validator {
	/**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
 
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
 
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1\\d{10}$";
 
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
 
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
 
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
 
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
 
    /**
     * 校验用户名
     * 
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }
 
    /**
     * 校验密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
 
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
    	//手机号码长度
    	int mobileLength=mobile.length();
    	//第一位
    	String phoneOne=mobile.substring(0,1);
    	if(mobileLength>11){
	    	if(phoneOne.equals("+")){
	    		if(mobile.substring(1,3).equals("86")){
	    			return Pattern.matches(REGEX_MOBILE, mobile.substring(3, mobile.length()));
	    		};
	    	}else if(phoneOne.equals("8")){
	    		if(mobile.substring(1,2).equals("6")){
	    			return Pattern.matches(REGEX_MOBILE, mobile.substring(2, mobile.length()));
	    		};
	    	}else if(phoneOne.equals("0")){
	    		if(mobile.substring(1,3).equals("86")){
	    			return Pattern.matches(REGEX_MOBILE, mobile.substring(3, mobile.length()));
	    		}else if(mobile.substring(1,4).equals("086")){
	    			return Pattern.matches(REGEX_MOBILE, mobile.substring(4, mobile.length()));
	    		}
	    	}
    	} 
    	return Pattern.matches(REGEX_MOBILE, mobile);
    }
    
    public static String trueMobile(String mobile) {
    	//手机号码长度
    	int mobileLength=mobile.length();
    	//第一位
    	String phoneOne=mobile.substring(0,1);
    	if(mobileLength>11){
	    	if(phoneOne.equals("+")){
	    		if(mobile.substring(1,3).equals("86")){
	    			return mobile.substring(3, mobile.length());
	    		};
	    	}else if(phoneOne.equals("8")){
	    		if(mobile.substring(1,2).equals("6")){
	    			return mobile.substring(2, mobile.length());
	    		};
	    	}else if(phoneOne.equals("0")){
	    		if(mobile.substring(1,3).equals("86")){
	    			return mobile.substring(3, mobile.length());
	    		}else if(mobile.substring(1,4).equals("086")){
	    			return mobile.substring(4, mobile.length());
	    		}
	    	}
    	}
    	return mobile;
    }
    
    /**
     * 校验邮箱
     * 
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
 
    /**
     * 校验汉字
     * 
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }
 
    /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
 
    /**
     * 校验URL
     * 
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
 
    /**
     * 校验IP地址
     * 
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
    
    public static void main(String[] args) {
        String mobile1 = "18086491535";
        String mobile2 = "008618086491535";
        String mobile3 = "08618086491535";
        String mobile4 = "8618086491535";
        String mobile5 = "+8618086491535";
        System.out.println(isMobile(mobile1));
        System.out.println(isMobile(mobile2));
        System.out.println(isMobile(mobile3));
        System.out.println(isMobile(mobile4));
        System.out.println(isMobile(mobile5));
        System.out.println(trueMobile(mobile1));
        System.out.println(trueMobile(mobile2));
        System.out.println(trueMobile(mobile3));
        System.out.println(trueMobile(mobile4));
        System.out.println(trueMobile(mobile5));
    }

}
