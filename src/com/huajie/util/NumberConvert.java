package com.huajie.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class NumberConvert implements Converter{
	public String getAsString(FacesContext arg0, UIComponent arg1,Object obj){
		String dValue = (String)obj.toString();
	    if (null == dValue ){
	    		return "没有取得value!";
	    }
	    Integer i=Integer.parseInt(dValue);
	    Integer hour=i/3600;
	    Integer min=(i-(hour*3600))/60;
	    Integer mm=(i-(hour*3600))%60;
	    if(min==0&&hour==0){
	    	return  String.valueOf(mm)+"秒";
	    }
	    if(hour==0){
	    	return  String.valueOf(min)+"分"+String.valueOf(mm)+"秒";
	    }
	    return  String.valueOf(hour)+"小时"+String.valueOf(min)+"分"+String.valueOf(mm)+"秒";
	}

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String str) {
		String dValue = (String)str;
	    if (null == dValue ){
	    		return "没有取得value!";
	    }
	    Integer i=Integer.parseInt(dValue);
	    Integer hour=i/3600;
	    Integer min=(i-(hour*3600))/60;
	    Integer mm=(i-(hour*3600))%60;
	    if(min==0&&hour==0){
	    	return  String.valueOf(mm)+"秒";
	    }
	    if(hour==0){
	    	return  String.valueOf(min)+"分"+String.valueOf(mm)+"秒";
	    }
	    return  String.valueOf(hour)+"小时"+String.valueOf(min)+"分"+String.valueOf(mm)+"秒";
	}
}
