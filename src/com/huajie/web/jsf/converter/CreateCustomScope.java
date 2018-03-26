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

/**
 *
 * @author huajie
 */
public class CreateCustomScope implements ActionListener {

	@Override
	public void processAction(final ActionEvent event) throws AbortProcessingException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();

		RereScoped customScope = new RereScoped();
		sessionMap.put(RereScoped.SCOPE_NAME, customScope);

		customScope.notifyCreate(facesContext);

	}

}
