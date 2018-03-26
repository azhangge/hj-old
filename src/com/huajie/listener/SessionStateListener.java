package com.huajie.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.hjedu.customer.service.IUserSessionStateService;
import com.huajie.util.SpringHelper;

@WebListener()
public class SessionStateListener
        implements HttpSessionListener, ServletRequestListener {

    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    private HttpServletRequest request;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        iussService.sessionCreated(event.getSession(), request);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        iussService.sessionDestroyed(event.getSession());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        request = (HttpServletRequest) sre.getServletRequest();
    }
}