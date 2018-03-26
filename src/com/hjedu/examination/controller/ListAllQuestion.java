package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;

@ManagedBean
@ViewScoped
public class ListAllQuestion  implements Serializable{
    
    String activeId="0";

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("active_id");
        if (id != null||!"".equals(id)) {
            this.activeId=id;
        } 
    }
    
    public void onChange(TabChangeEvent event) {
        TabView accordionPanel1 = (TabView) event.getTab().getParent();
        int t = accordionPanel1.getActiveIndex();
        this.activeId = String.valueOf(t);
        //...
    }
    
}
