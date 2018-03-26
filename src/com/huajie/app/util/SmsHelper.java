package com.huajie.app.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;



public class SmsHelper {
	private static final Logger logger = Logger.getLogger(SmsHelper.class);
	private static Properties props = PropsUtil.loadProps("sms.properties");
	private static final Map<String, String> SMS_FIX_MAP = new HashMap<String, String>();
	private static final String SMS_URL = PropsUtil.getString(props, "sms.url");

	static {
		SMS_FIX_MAP.put("SpCode", PropsUtil.getString(props, "sms.spcode"));
		SMS_FIX_MAP.put("LoginName", PropsUtil.getString(props, "sms.loginname"));
		SMS_FIX_MAP.put("Password", PropsUtil.getString(props, "sms.password"));
		SMS_FIX_MAP.put("f","1");
	}
	/**
	 * 发送单条短信
	 * @param smsContent 短信内容
	 * @param number 发送号码
	 * @param sendTime 发送时间,若为空，则立即发送 yyyyMMddHHmmss
	 */
	public void sendOneSms(String number,String smsContent,String sendTime ){
		Map<String,String> paramMap=new HashMap<String, String>();
		paramMap.putAll(SMS_FIX_MAP);
		paramMap.put("UserNumber", number);
		paramMap.put("MessageContent", smsContent);
		paramMap.put("SerialNumber", System.currentTimeMillis()+CodeUtils.generateVerifyCode(4));
		paramMap.put("ScheduleTime", sendTime);
		String returnMsg=HttpClientUtil.post(SMS_URL, paramMap);
		logger.info("++++++++++"+returnMsg);
	}
	/**
	 * 群发短信
	 * @param smsContent 短信内容
	 * @param numberList 发送号码List
	 * @param sendTime 发送时间
	 */
	public void sendMutiSms(String smsContent,List<String> numberList,String sendTime ){
		Map<String,String> paramMap=new HashMap<String, String>();
		paramMap.putAll(SMS_FIX_MAP);
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<numberList.size();i++){
			if(i==0)continue;
			sb.append(",");
			sb.append(numberList.get(i));
		}
		paramMap.put("UserNumber", sb.toString());
		paramMap.put("MessageContent", smsContent);
		paramMap.put("SerialNumber", System.currentTimeMillis()+CodeUtils.generateVerifyCode(4));
		paramMap.put("ScheduleTime", sendTime);
		String returnMsg=HttpClientUtil.post(SMS_URL, paramMap);
		if(handleReturnMsg(returnMsg).equals("0")){
			logger.info("发送成功");
		}else{
			logger.info("发送失败");
		}
	}
	@Test
	public void test(){
		sendOneSms("18220533324","尊敬的夏慧文您好，您已经确认参加2016年4月15日上午11时0分在公共资源交易中心7楼评标区进行的评标，预计评标时间4小时，请持身份证准时参加，十分感谢！如因故不能参加或迟到，请在工作时间联系027-83600666。",null);
	}
	
	//短信返回简单解析
	private String handleReturnMsg(String returnMsg){
		//String returnMsg="result=5&description=IP不合法";
		String[] tmp=returnMsg.split("&");
		return tmp[0].split("=")[1];
	}
	
}
