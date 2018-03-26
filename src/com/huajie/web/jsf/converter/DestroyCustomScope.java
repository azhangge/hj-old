
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
public class DestroyCustomScope implements ActionListener {

    @Override
    public void processAction(final ActionEvent event) throws AbortProcessingException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        

        RereScoped customScope = (RereScoped) sessionMap.get(RereScoped.SCOPE_NAME);
        customScope.notifyDestroy(facesContext);

        sessionMap.remove(RereScoped.SCOPE_NAME);

    }
}
