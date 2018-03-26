package com.huajie.app.util;

import java.util.Properties;

public final class SmsCountHelper {
	private static Properties props = PropsUtil.loadProps("sms_count.properties");
	public  static int ip_count=PropsUtil.getInt(props, "ip_count");
	public  static int phone_count=PropsUtil.getInt(props, "phone_count");

	public static void main(String[] args) {
		System.out.println(SmsCountHelper.ip_count);
		System.out.println(SmsCountHelper.phone_count);
	}
	
}
