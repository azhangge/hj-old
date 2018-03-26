/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 *
 * @author huajie
 */
    public class JsonKit implements ExclusionStrategy {  
    String[] keys;  
  
    public JsonKit(String[] keys) {  
        this.keys = keys;  
    }  
  
    @Override  
    public boolean shouldSkipClass(Class<?> arg0) {  
        return false;  
    }  
  
    @Override  
    public boolean shouldSkipField(FieldAttributes arg0) {  
        for (String key : keys) {  
            if (key.equals(arg0.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
}  

