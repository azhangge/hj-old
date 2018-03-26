/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.huajie.seller.model.lazy;

import java.lang.reflect.Method;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author 铁铁
 */
class LazySorter<T> implements Comparator<T> {

    private String sortField;
    private SortOrder sortOrder;
    Class cl;

    public LazySorter(Class cl, String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.cl = cl;
    }

    public int compare(T car1, T car2) {
        try {

            //Object value1 = cl.getField(this.sortField).get(car1);
            Object value1 = getter(car1, this.sortField);
            //Object value2 = cl.getField(this.sortField).get(car2);
            Object value2 = getter(car2, this.sortField);
            int value = ((Comparable) value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* 
     *@param obj 操作的对象 
     *@param att 操作的属性 
     *@param value 设置的值 
     *@param type 参数的类型 
     */
    public static void setter(Object obj, String att, Object value, Class<?> type) {
        try {
            Method met = obj.getClass().
                    getMethod("set" + initStr(att), type);
            met.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getter(Object obj, String att) {
        try {
            Method met = obj.getClass().getMethod("get" + initStr(att));
            Object vv = met.invoke(obj);
            return vv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String initStr(String old) {   // 将单词的首字母大写  
        String str = old.substring(0, 1).toUpperCase() + old.substring(1);
        return str;
    }

}
