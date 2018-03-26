/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.huajie.ip;

public class IPEntry {

	public IPEntry() {
		beginIp = endIp = country = area = "";
	}

	public String toString() {
		return (new StringBuilder()).append(area).append("  ").append(country).append("IP\u8303\u56F4:").append(beginIp)
				.append("-").append(endIp).toString();
	}

	public String beginIp;
	public String endIp;
	public String country;
	public String area;
}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\workspaces-HJ\exam\WebContent\WEB-INF\lib\rereip1.1.jar
	Total time: 37 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/