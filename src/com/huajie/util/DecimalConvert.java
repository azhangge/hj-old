package com.huajie.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class DecimalConvert implements Converter{
	public String getAsString(FacesContext arg0, UIComponent arg1,Object obj){
		String dValue = String.valueOf(obj.toString());
	    if (null == dValue ){
	    		return "0";
	    }
	    if(dValue.contains(".0")){
	    	dValue = dValue.substring(0, dValue.lastIndexOf("."));
	    }
	    return  dValue;
	}

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String str) {
		String dValue = (String)str;
	    if (null == dValue ){
    		return "0";
	    }
	    if(dValue.contains(".0")){
	    	dValue = dValue.substring(0,dValue.lastIndexOf("."));
	    }
	    return  dValue;
	}
	
}
