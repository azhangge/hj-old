package com.huajie.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huajie.util.MyLogger;

public final class DateUtil {
	/**
	 * 时间date减一天
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);//-1今天的时间减一天
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 从end到begin之间的天数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long getDayNumBetweenTwoDate(Date begin,Date end){
		long lo = end.getTime()-begin.getTime();
		if(lo<0){
			return 0;
		}
		return lo/ (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 时间date加一天
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 将Date类型转为24小时制String类型yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formateDateToString(Date date){
		if(date!=null){
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  
			String dateString = sdformat.format(date);
			return dateString;
		}else{
			return "";
		}
	}
	
	/**
	 * 将Date类型转为24小时制String类型yyyy年MM月dd日HH点mm分ss秒，bo为true时不显示时分秒
	 * @param date
	 * @param bo
	 * @return
	 */
	public static String formateDateToCNString(Date date,boolean bo){
		if(date!=null){
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");//24小时制  
			if(bo){
				sdformat = new SimpleDateFormat("yyyy年MM月dd日");
			}
			String dateString = sdformat.format(date);
			return dateString;
		}else{
			return "";
		}
	}
	
	/**
	 * 将Date类型转为24小时制String类型yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String formateDateToString2(Date date){
		if(date!=null){
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");//24小时制  
			String dateString = sdformat.format(date);
			return dateString;
		}else{
			return "";
		}
	}
	
	/**
	 * 将24小时制String类型yyyy-MM-dd HH:mm:ss转为Date类型
	 * @param date
	 * @return
	 */
	public static Date formateStringToDate(String date){
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  
		Date datetime = null;
		try {
			datetime = sdformat.parse(date);
		} catch (ParseException e) {
			MyLogger.log(e);
			e.printStackTrace();
		}
		return datetime;
	}
	
	/**
	 * 将24小时制String类型yyyy-MM-dd转为Date类型
	 * @param date
	 * @return
	 */
	public static Date formateStringToDate2(String date){
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");//24小时制  
		Date datetime = null;
		try {
			datetime = sdformat.parse(date);
		} catch (ParseException e) {
			MyLogger.log(e);
			e.printStackTrace();
		}
		return datetime;
	}
	
	public static Date formateMillisecondToDate(String millisecond){
		Calendar calendar = Calendar.getInstance();
		long mi=Long.valueOf(millisecond);
		calendar.setTimeInMillis(mi);
        return calendar.getTime();
	}
	
	public static int getYear(){
    	Calendar cal = Calendar.getInstance();
    	return cal.get(Calendar.YEAR); 
    }
	
	public static int getMonth(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH); 
	}
	
	public static int getDay(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH); 
	}
	
	public static int getWeek(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_YEAR); 
	}
	
	//可以定义一个函数，函数的参数有小时、分、秒、相比今天的日期，今天就输入0，明天输入1，昨天输入-1，以此类推
	//(毫秒是可选参数，可以输入也可以不输入，毫秒的取值范围是0到999)
	public static Date getNeedTime(int hour,int minute,int second,int addDay,int ...args){
	    Calendar calendar = Calendar.getInstance();
	    if(addDay != 0){
	        calendar.add(Calendar.DATE,addDay);
	    }
	    calendar.set(Calendar.HOUR_OF_DAY,hour);
	    calendar.set(Calendar.MINUTE,minute);
	    calendar.set(Calendar.SECOND,second);
	    if(args.length==1){
	        calendar.set(Calendar.MILLISECOND,args[0]);
	    }
	    return calendar.getTime();
	}
	
	/**
	 * 将24小时制String类型yyyy-MM-dd转为Date类型
	 * @param date
	 * @return
	 */
	public static Date foramteStringToData2(String date){
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");//24小时制  
		Date datetime = null;
		try {
			datetime = sdformat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datetime;
	}
}
