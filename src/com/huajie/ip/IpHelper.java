/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.huajie.ip;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Referenced classes of package com.reremouse.rereip:
//            IPSeeker

public class IpHelper {

	public IpHelper() {
	}

	public static String seek(String str) {
		String ipv6 = "0:0:0:0:0:0:0:1";
		if (ipv6.equals(str))
			return "IPV6\u672C\u673A\u5730\u5740";
		try {
			if (ipSeeker == null)
				ipSeeker = new IPSeeker();
			InetAddress ia = InetAddress.getByName(str);
			String ip = ia.getHostAddress();
			return ipSeeker.getAddress(ip);
		} catch (UnknownHostException unknownhostexception) {
			return "\u672A\u77E5\u533A\u57DF\uFF0C\u6709\u53EF\u80FD\u67E5\u8BE2\u4E32\u683C\u5F0F\u9519\u8BEF";
		}
	}

	public static String getCountry(String str) {
		String ipv6 = "0:0:0:0:0:0:0:1";
		if (ipv6.equals(str))
			return "IPV6\u672C\u673A\u5730\u5740";
		try {
			if (ipSeeker == null)
				ipSeeker = new IPSeeker();
			InetAddress ia = InetAddress.getByName(str);
			String ip = ia.getHostAddress();
			return ipSeeker.getCountry(ip);
		} catch (UnknownHostException unknownhostexception) {
			return "\u672A\u77E5\u56FD\u5BB6\uFF0C\u6709\u53EF\u80FD\u67E5\u8BE2\u4E32\u683C\u5F0F\u9519\u8BEF";
		}
	}

	public static String getArea(String str) {
		String ipv6 = "0:0:0:0:0:0:0:1";
		if (ipv6.equals(str))
			return "IPV6\u672C\u673A\u5730\u5740";
		try {
			if (ipSeeker == null)
				ipSeeker = new IPSeeker();
			InetAddress ia = InetAddress.getByName(str);
			String ip = ia.getHostAddress();
			return ipSeeker.getArea(ip);
		} catch (UnknownHostException unknownhostexception) {
			return "\u672A\u77E5\u533A\u57DF\uFF0C\u6709\u53EF\u80FD\u67E5\u8BE2\u4E32\u683C\u5F0F\u9519\u8BEF";
		}
	}

	public static void main(String args[]) {
		String ip = "0:0:0:0:0:0:0:1";
		String c = seek(ip);
		System.out.println(c);
	}

	private static IPSeeker ipSeeker = null;

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\workspaces-HJ\exam\WebContent\WEB-INF\lib\rereip1.1.jar
	Total time: 109 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/