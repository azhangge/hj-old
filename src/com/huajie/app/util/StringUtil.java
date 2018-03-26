package com.huajie.app.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import com.huajie.util.MyLogger;

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 若为空返回空字符串；不为空返回原值
     * @param str
     * @return
     */
    public static String returnString(String str) {
    	if(isEmpty(str)||str.equals("null")){
    		return "";
    	}else{
    		return str;
    	}
    }
    
    /**
     * 将异常信息转为String
     * @param e
     * @return
     */
    public static String eToString(Throwable e){   
        StringWriter sw = new StringWriter();   
        PrintWriter pw = new PrintWriter(sw, true);   
        e.printStackTrace(pw);   
        pw.flush();   
        sw.flush();   
        return sw.toString();   
	} 
    
    /**
     * 将分号隔开的id字符串转为List（去掉重复的id）
     * @param ids
     * @return
     */
    public static List<String> idsToIdList(String ids){
    	List<String> result = new LinkedList<>();
    	if(isNotEmpty(ids)){
    		String[] ids1 = ids.split(";");
    		if(ids1!=null&&ids1.length>0){
    			for(int i=0;i<ids1.length;i++){
    				if(!result.contains(ids1[i])){
    					result.add(ids1[i]);
    				}
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * 获取字符串str1中str2后的字符
     * @param str
     * @return
     */
    public static String getStringAfter(String str1,String str2) {
    	if(!isEmpty(str1)&&!isEmpty(str2)){
    		int i = str1.indexOf(str2)+str2.length();
    		return str1.substring(i, str1.length());
    	}else{
    		return "";
    	}
    }
    public static void main(String[] args) {
    	String str="dc862df4-768c-40c1-b1f4-1b5b5be378d7;8b0a6b33-7c60-47f3-a8a9-c17b6fde9fd4;4c5aac71-b168-40c1-a6d3-4d0045f56aad;";
    	List<String> lst= idsToIdList(str);
    	System.out.println(lst.size());
    	for(String l:lst){
    		System.out.println(l);
    	}
    	
	}
    
    /**
     * 返回a/b,c为保留多少位有效数字,四舍五入
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static String returnRate(double a,double b,int c){
    	// 创建一个数值格式化对象
    	NumberFormat numberFormat = NumberFormat.getInstance();
    	// 设置精确到小数点后2位 
    	numberFormat.setMaximumFractionDigits(c); 
    	if(b==0||a>=b){
    		return "1";
    	}
    	return numberFormat.format(a / b);
    }
    
    /**
     * 将用split分隔开的字符串转('a','b')形式
     * @param acs
     * @param split
     * @return
     */
    public static String getInSqlString(String acs,String split){
		String acs2 = "('";
		String[] acsarray = acs.split(split);
		for(String a : acsarray){
			acs2 = acs2+a+"','";
		}
		acs2 = acs2.substring(0, acs2.length()-2)+")";
		return acs2;
	}
}
