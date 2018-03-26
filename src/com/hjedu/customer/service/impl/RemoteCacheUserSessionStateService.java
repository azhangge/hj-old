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
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheItem;
import com.huajie.cache.RereCacheManager;
import com.huajie.cache.client.RereCacheClient;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class RemoteCacheUserSessionStateService implements IUserSessionStateService, Serializable {
    
    String instance = "user_sessions";
    RereCacheClient client;
    ISystemConfigDAO configDAO;
    
    public ISystemConfigDAO getConfigDAO() {
        return configDAO;
    }
    
    public void setConfigDAO(ISystemConfigDAO configDAO) {
        this.configDAO = configDAO;
    }
    
    public RereCacheClient getClient() {
        return client;
    }
    
    public void setClient(RereCacheClient client) {
        this.client = client;
    }
    
    @Override
    public void sessionCreated(final HttpSession session, final HttpServletRequest request) {
        //集群环境下session可能重复创建
        new Thread(new Runnable() {
            public void run() {
                UserSessionState uss = findSession(session.getId());
                if (uss == null) {
                    uss = buildState(session, request);
                    updateSession(uss);
                    MyLogger.echo("session created");
                }
            }
        }).start();
        
    }
    
    private UserSessionState findSession(String sid) {
        UserSessionState uss = null;
        RereCacheItem it = client.find(instance, sid, UserSessionState.class);
        if (it != null) {
            uss = (UserSessionState) it.getObject();
        }
        return uss;
    }
    
    @Override
    public List<UserSessionState> findAllSession() {
        List<UserSessionState> all = new ArrayList();
        List<RereCacheItem> its = client.findAll(instance, UserSessionState.class);
        for (RereCacheItem it : its) {
            all.add((UserSessionState) it.getObject());
        }
        return all;
    }
    
    private void updateSession(UserSessionState uss) {
        client.put(instance, uss.getSessionId(), uss);
    }
    
    private UserSessionState buildState(HttpSession session, HttpServletRequest request) {
        UserSessionState uss = new UserSessionState();
        String ip = request.getRemoteAddr();
        uss.setIp(ip);
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        String ipAddr = ips.seek(ip);
        uss.setIpAddr(ipAddr);
        uss.setAgent(request.getHeader("user-agent"));
        uss.setSessionId(session.getId());
        return uss;
    }
    
    /**
     * 
     * @param bu 
     */
    @Override
    public void login(final BbsUser bu) {
        final HttpServletRequest request = JsfHelper.getRequest();
        final String sid = request.getSession().getId();
        new Thread(new Runnable() {
            public void run() {
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(request.getSession(), request);
                }
                //如果只允许一个用户在一个终端登录
                if (configDAO.getSystemConfig().isSingleLogin()) {
                    String ip = request.getRemoteAddr();
                    kickUser(bu.getId(), sid, ip);//将已经登录的相同用户踢出
                }
                removeFromKickedSessionList(sid);//从踢出列表中移除本SESSION，确保已经登录用户不是踢出状态

                u.setUser(bu);
                u.setKicked(false);
                u.setLoginTime(new Date());
                updateSession(u);
            }
        }).start();
        
    }

    /**
     * 踢出已经登录的用户
     *
     * @param uid
     * @param sessionId
     * @param kickerIp
     */
    public void kickUser(final String uid, final String sessionId, final String kickerIp) {
        new Thread(new Runnable() {
            public void run() {
                List<RereCacheItem> us = client.queryEqual(instance, "userId", uid, UserSessionState.class);//所有session
                List<UserSessionState> us2 = new ArrayList();//存储该用户的所有session
                //找出所有的该用户的session_state
                for (RereCacheItem uu : us) {
                    try {
                        UserSessionState uss = (UserSessionState) uu.getObject();
                        if (uss != null) {
                            us2.add(uss);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (UserSessionState u : us2) {//在本用户的所有session中查找非当前session并踢出
                    String sid = u.getSessionId();
                    if (!sessionId.equals(sid)) {
                        if (!u.isKicked()) {
                            u.setKicked(true);
                            u.setKickerIp(kickerIp);
                            MyLogger.println("Kicked one user.");
                            updateSession(u);
                            getKickedSessionList().add(u);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     *
     * @return
     */
    @Override
    public List<UserSessionState> getKickedSessionList() {
        IRereCacheInstance<List<UserSessionState>> ci = RereCacheManager.getReplicatedInstance("userSessions");
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
    public void removeFromKickedSessionList(final String sid) {
        new Thread(new Runnable() {
            public void run() {
                List<UserSessionState> ss = getKickedSessionList();
                Iterator<UserSessionState> iter = ss.iterator();
                while (iter.hasNext()) {
                    UserSessionState s = iter.next();
                    if (sid.equals(s.getSessionId())) {
                        MyLogger.println("Kicked Session deleted:" + s.getId());
                        iter.remove();
                    }
                }
            }
        }).start();
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
        final HttpServletRequest request = JsfHelper.getRequest();
        new Thread(new Runnable() {
            public void run() {
                String sid = request.getSession().getId();
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(request.getSession(), JsfHelper.getRequest());
                }
                u.setUser(null);
                u.setExam(null);
                updateSession(u);
                removeFromKickedSessionList(sid);//确保登出用户的SESSION ID不在登出状态
            }
        }).start();
    }
    
    @Override
    public void loginModuleExam(final ModuleExamCase exam) {
        final HttpServletRequest request = JsfHelper.getRequest();
        new Thread(new Runnable() {
            public void run() {
                String sid = request.getSession().getId();
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(request.getSession(), request);
                }
                if (u.getUser() != null) {
                    u.setModuleExam(exam.getExamination());
                    u.setModuleExamTime(new Date());
                    updateSession(u);
                }
            }
        }).start();
    }
    
    @Override
    public void logoutModuleExam() {
        final HttpServletRequest request = JsfHelper.getRequest();
        new Thread(new Runnable() {
            public void run() {
                String sid = request.getSession().getId();
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(request.getSession(), request);
                }
                u.setModuleExam(null);
                u.setEndModuleExamTime(new Date());
                updateSession(u);
            }
        }).start();
    }
    
    @Override
    public void loginExam(ExamCase exam) {
        
        this.loginExam(exam, JsfHelper.getRequest());
    }
    
    @Override
    public void loginExam(final ExamCase exam, final HttpServletRequest req) {
        new Thread(new Runnable() {
            public void run() {
                String sid = req.getSession().getId();
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(req.getSession(), req);
                }
                if (u.getUser() != null) {
                    u.setExam(exam.getExamination());
                    u.setExamTime(new Date());
                    updateSession(u);
                }
            }
        }).start();
    }
    
    @Override
    public void logoutExam() {
        this.logoutExam(JsfHelper.getRequest());
    }
    
    @Override
    public void logoutExam(final HttpServletRequest req) {
        new Thread(new Runnable() {
            public void run() {
                String sid = req.getSession().getId();
                UserSessionState u = findSession(sid);
                if (u == null) {
                    u = buildState(req.getSession(), req);
                }
                u.setExam(null);
                u.setEndExamTime(new Date());
                updateSession(u);
            }
        }).start();
    }
    
    @Override
    public void deleteAllOld() {
        float hours = SpringHelper.getSpringBean("session_max_hours");
        Date d = new Date(System.currentTimeMillis() - (long) (hours * 3600 * 1000));
        List<UserSessionState> all = findAllSession();
        List<UserSessionState> olds = new ArrayList();
        for (UserSessionState us : all) {
            if (us.getGenTime().getTime() < d.getTime() && us.getLoginTime().getTime() < d.getTime() && us.getExamTime().getTime() < d.getTime() && us.getEndModuleExamTime().getTime() < d.getTime()) {
                olds.add(us);
            }
        }
        List<RereCacheItem> items = new ArrayList();
        for (UserSessionState u : olds) {
            RereCacheItem rc = new RereCacheItem();
            rc.setObject(u);
            rc.setId(u.getId());
            items.add(rc);
        }
        client.batchDelete(instance, items);
    }
    
    @Override
    public void sessionDestroyed(final HttpSession session) {
        new Thread(new Runnable() {
            public void run() {
                client.delete(instance, session.getId());
                removeFromKickedSessionList(session.getId());//确保登出用户的SESSION ID不在登出状态
                MyLogger.echo("session destroyed:" + session.getId());
            }
        }).start();
        
    }
}
