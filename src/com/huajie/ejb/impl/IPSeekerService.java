/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.ejb.impl;

import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com
 */
public class IPSeekerService implements IPSeekerServiceRemote {

    IPSeekerServiceRemote remoteAgent = SpringHelper.getSpringBean("remoteIpSeeker");

    @Override
    public String seek(String ip) {
        //System.out.println("开始查询IP:"+ip);
        String address = "未知";
        try {
            address = remoteAgent.seek(ip);
        } catch (Exception e) {
        }
        //System.out.println(address);
        return address;
    }

    @Override
    public String getCountry(String ip) {
        String c = "未知";
        try {
            c = remoteAgent.getCountry(ip);
        } catch (Exception e) {
        }
        return c;
    }

    @Override
    public String getArea(String ip) {
        String c = "未知";
        try {
            c = remoteAgent.getArea(ip);
        } catch (Exception e) {
        }
        return c;
    }
}
