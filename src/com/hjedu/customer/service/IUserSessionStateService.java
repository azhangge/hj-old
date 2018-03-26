package com.hjedu.customer.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.platform.entity.UserSessionState;


public interface IUserSessionStateService {
    
    public void sessionCreated(HttpSession session,HttpServletRequest  request);
    
    public List<UserSessionState> findAllSession();
    
    public void login(BbsUser bu);
    
    public void logout();
    
    public void loginExam(ExamCase exam);
    
    public void logoutExam();
    
    public void loginModuleExam(ModuleExamCase exam);
    
    public void logoutModuleExam();
    
    public void sessionDestroyed(HttpSession session);
    
    public List<UserSessionState> getKickedSessionList();
    
    public void removeFromKickedSessionList(String sid);
    
    public UserSessionState findKickedSession(String sid);
    
    public void loginExam(ExamCase exam, HttpServletRequest req);
    
    public void logoutExam( HttpServletRequest req);
    
    public void deleteAllOld();
    
}
