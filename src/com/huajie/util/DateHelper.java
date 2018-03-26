/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class DateHelper {

    public static Date getBeginningOfThisMonth() {
        Date date = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            String str = year + "-" + month + "-1";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(str);
            System.out.println(str);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }
    
    public static Date getBeginningOfThisYear() {
        Date date = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            String str = year + "-1-1" ;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(str);
            System.out.println(str);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }
    
    public static void main(String[] args) {
        try {
            
            Date date1=DateHelper.getBeginningOfThisMonth();
            Date date2=DateHelper.getBeginningOfThisYear();
            System.out.println(date1);
            System.out.println(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
