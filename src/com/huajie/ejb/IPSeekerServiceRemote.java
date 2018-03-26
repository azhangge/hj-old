/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.ejb;

/**
 *
 * @author huajie.com
 */

public interface IPSeekerServiceRemote {

    String seek(String ip);

    String getCountry(String ip);

    String getArea(String ip);
    
}
