package com.huajie.util;

import com.huajie.ip.IpHelper;

class IpTest
{
  public static void main(String[] args)
  {
    String ip = "0:0:0:0:0:0:0:1";
    String c = IpHelper.seek(ip);
    System.out.println(c);
  }
}