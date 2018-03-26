/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.ejb.impl;

import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.ip.IpHelper;


public class IPSeekerService3 implements IPSeekerServiceRemote{

    @Override
    public String seek(String ip) {
        return IpHelper.seek(ip);
    }

    @Override
    public String getCountry(String ip) {
        return IpHelper.getCountry(ip);
    }

    @Override
    public String getArea(String ip) {
        return IpHelper.getArea(ip);
    }
    
}
