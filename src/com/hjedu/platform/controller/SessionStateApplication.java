package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.util.SpringHelper;

/**
 * 因为以application方式保存登录状态不能支持集群，此类已改为数据库方式
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class SessionStateApplication implements Serializable{

    List<UserSessionState> sessionList = new LinkedList<UserSessionState>();
    List<UserSessionState> userSessionList = new LinkedList<UserSessionState>();
    List<UserSessionState> examSessionList = new LinkedList<UserSessionState>();
    List<UserSessionState> moduleExamList = new LinkedList<UserSessionState>();
    List<UserSessionState> idleList = new LinkedList<UserSessionState>();

    public List<UserSessionState> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<UserSessionState> sessionList) {
        this.sessionList = sessionList;
    }

    public List<UserSessionState> getUserSessionList() {
        if (sessionList != null) {
            this.buildUserSessionList();
        }
        return userSessionList;
    }

    public void setUserSessionList(List<UserSessionState> userSessionList) {
        this.userSessionList = userSessionList;
    }

    public List<UserSessionState> getExamSessionList() {
        if (sessionList != null) {
            this.buildExamSessionList();
        }
        return examSessionList;
    }

    public void setExamSessionList(List<UserSessionState> examSessionList) {
        this.examSessionList = examSessionList;
    }

    public List<UserSessionState> getIdleList() {
        if (sessionList != null) {
            this.buildIdleList();
        }
        return idleList;
    }

    public void setIdleList(List<UserSessionState> idleList) {
        this.idleList = idleList;
    }

    public List<UserSessionState> getModuleExamList() {
        if (sessionList != null) {
            this.buildModuleExamList();
        }
        return moduleExamList;
    }

    public void setModuleExamList(List<UserSessionState> moduleExamList) {
        this.moduleExamList = moduleExamList;
    }

    @PostConstruct
    public void init() {
        IUserSessionStateService stateDAO = SpringHelper.getSpringBean("UserSessionStateService");
        stateDAO.deleteAllOld();
        this.sessionList = stateDAO.findAllSession();
        
    }

    public List<UserSessionState> buildUserSessionList() {
        this.userSessionList.clear();
        for (UserSessionState u : this.sessionList) {
            if (u.getUser() != null) {
                this.userSessionList.add(u);
            }
        }
        Collections.sort(userSessionList);
        return userSessionList;
    }

    public List<UserSessionState> buildExamSessionList() {
        this.examSessionList.clear();
        for (UserSessionState u : this.sessionList) {
            if (u.getExam() != null) {
                this.examSessionList.add(u);
            }
        }
        Collections.sort(examSessionList);
        return examSessionList;
    }

    public List<UserSessionState> buildIdleList() {
        this.idleList.clear();
        for (UserSessionState u : this.sessionList) {
            if (u.getUser() == null) {
                this.idleList.add(u);
            }
        }
        Collections.sort(idleList);
        return idleList;
    }
    
    public List<UserSessionState> buildModuleExamList() {
        this.moduleExamList.clear();
        for (UserSessionState u : this.sessionList) {
            if (u.getModuleExam() != null) {
                this.moduleExamList.add(u);
            }
        }
        Collections.sort(moduleExamList);
        return moduleExamList;
    }

}
