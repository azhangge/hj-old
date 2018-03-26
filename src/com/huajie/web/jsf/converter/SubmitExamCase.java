/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.web.jsf.converter;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletRequest;

import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;

/**
 *
 * @author huajie
 */
public class SubmitExamCase implements ActionListener {
    
    @Override
    public void processAction(final ActionEvent event) throws AbortProcessingException {
        MyLogger.println("Begin submitting...");
        HttpServletRequest request = JsfHelper.getRequest();
        
        Map<String, String[]> reqMap = request.getParameterMap();
        MyLogger.explainMap(reqMap);
        
    }
}
