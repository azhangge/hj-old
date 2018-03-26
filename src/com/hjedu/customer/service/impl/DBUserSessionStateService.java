package com.hjedu.customer.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.IUserSessionStateDAO;
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class DBUserSessionStateService implements IUserSessionStateService, Serializable {

    IUserSessionStateDAO stateDAO;
    ISystemConfigDAO configDAO;

    public IUserSessionStateDAO getStateDAO() {
        return stateDAO;
    }

    public void setStateDAO(IUserSessionStateDAO stateDAO) {
        this.stateDAO = stateDAO;
    }

    public ISystemConfigDAO getConfigDAO() {
        return configDAO;
    }

    public void setConfigDAO(ISystemConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    @Override
    public void sessionCreated(HttpSession session, HttpServletRequest request) {
        //集群环境下session可能重复创建
        UserSessionState uss = this.stateDAO.findUserSessionStateBySessionId(session.getId());
        if (uss == null) {
            uss = this.buildState(session, request);
            stateDAO.addUserSessionState(uss);
            MyLogger.echo("session created");
        }
    }

    private UserSessionState buildState(HttpSession session, HttpServletRequest request) {
        UserSessionState uss = new UserSessionState();
        String ip = IpHelper.getRemoteAddr(request);
        uss.setIp(ip);
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        String ipAddr = ips.seek(ip);
        uss.setIpAddr(ipAddr);
        uss.setAgent(request.getHeader("user-agent"));
        uss.setSessionId(session.getId());
        //stateDAO.addUserSessionState(uss);
        return uss;
    }

    @Override
    public void login(BbsUser bu) {
        String sid = JsfHelper.getRequest().getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(JsfHelper.getRequest().getSession(), JsfHelper.getRequest());
        }
        //如果只允许一个用户在一个终端登录
        if (configDAO.getSystemConfig().isSingleLogin()) {
            String ip = JsfHelper.getRequest().getRemoteAddr();
            this.kickUser(bu.getId(), sid, ip);//将已经登录的相同用户踢出
        }
        this.removeFromKickedSessionList(sid);//从踢出列表中移除本SESSION，确保已经登录用户不是踢出状态

        u.setUser(bu);
        u.setKicked(false);
        u.setLoginTime(new Date());
        this.stateDAO.updateUserSessionState(u);
    }

    @Override
    public List<UserSessionState> findAllSession() {
        return this.stateDAO.findAllUserSessionState();
    }

    
    
    /**
     * 踢出已经登录的用户
     *
     * @param uid
     * @param sessionId
     * @param kickerIp
     */
    public void kickUser(String uid, String sessionId, String kickerIp) {
        List<UserSessionState> us = this.stateDAO.findUserSessionStateByUsr(uid);
        for (UserSessionState u : us) {
            String sid = u.getSessionId();
            if (!sessionId.equals(sid)) {
                if (!u.isKicked()) {
                    u.setKicked(true);
                    u.setKickerIp(kickerIp);
                    MyLogger.println("Kicked one user.");
                    this.stateDAO.updateUserSessionState(u);
                    this.getKickedSessionList().add(u);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<UserSessionState> getKickedSessionList() {
        IRereCacheInstance<List<UserSessionState>> ci = RereCacheManager.getLocalInstance("userSessions");
        List<UserSessionState> exams = ci.fetchObject("kickedSessions");
        if (exams == null) {
            exams = new ArrayList();
            ci.add("kickedSessions", exams);
        }
        return exams;
    }

    /**
     *
     * @param sid sessionId
     */
    @Override
    public void removeFromKickedSessionList(String sid) {
        List<UserSessionState> ss = this.getKickedSessionList();
        Iterator<UserSessionState> iter = ss.iterator();
        while (iter.hasNext()) {
            UserSessionState s = iter.next();
            if (sid.equals(s.getSessionId())) {
                MyLogger.println("Kicked Session deleted:" + s.getId());
                iter.remove();
            }
        }
    }

    /**
     * 按session id找出被踢的session
     *
     * @param sid sessionId
     * @return
     */
    @Override
    public UserSessionState findKickedSession(String sid) {
        List<UserSessionState> ss = this.getKickedSessionList();
        for (UserSessionState s : ss) {
            if (sid.equals(s.getSessionId())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void logout() {
        String sid = JsfHelper.getRequest().getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(JsfHelper.getRequest().getSession(), JsfHelper.getRequest());
        }
        u.setUser(null);
        u.setExam(null);
        this.stateDAO.updateUserSessionState(u);
        this.removeFromKickedSessionList(sid);//确保登出用户的SESSION ID不在登出状态
    }

    @Override
    public void loginModuleExam(ModuleExamCase exam) {
        String sid = JsfHelper.getRequest().getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(JsfHelper.getRequest().getSession(), JsfHelper.getRequest());
        }
        if (u.getUser() != null) {
            u.setModuleExam(exam.getExamination());
            u.setModuleExamTime(new Date());
            this.stateDAO.updateUserSessionState(u);
        }

    }

    @Override
    public void logoutModuleExam() {
        String sid = JsfHelper.getRequest().getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(JsfHelper.getRequest().getSession(), JsfHelper.getRequest());
        }
        u.setModuleExam(null);
        u.setEndModuleExamTime(new Date());
        this.stateDAO.updateUserSessionState(u);

    }

    @Override
    public void loginExam(ExamCase exam) {
        this.loginExam(exam, JsfHelper.getRequest());
    }

    @Override
    public void loginExam(ExamCase exam, HttpServletRequest req) {
        String sid = req.getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(req.getSession(), req);
        }
        if (u.getUser() != null) {
            u.setExam(exam.getExamination());
            u.setExamTime(new Date());
            this.stateDAO.updateUserSessionState(u);
        }
    }

    @Override
    public void logoutExam() {
        this.logoutExam(JsfHelper.getRequest());

    }

    @Override
    public void logoutExam(HttpServletRequest req) {
        String sid = req.getSession().getId();
        UserSessionState u = this.stateDAO.findUserSessionStateBySessionId(sid);
        if (u == null) {
            u = this.buildState(req.getSession(), req);
        }
        u.setExam(null);
        u.setEndExamTime(new Date());
        this.stateDAO.updateUserSessionState(u);
    }

    @Override
    public void deleteAllOld() {
        this.stateDAO.deleteAllOld();
    }

    @Override
    public void sessionDestroyed(HttpSession session) {
        this.stateDAO.deleteUserSessionStateBySessionId(session.getId());
        this.removeFromKickedSessionList(session.getId());//确保登出用户的SESSION ID不在登出状态
        MyLogger.echo("session destroyed:" + session.getId());
    }
}
