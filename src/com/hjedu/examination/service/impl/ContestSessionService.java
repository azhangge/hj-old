package com.hjedu.examination.service.impl;

import java.util.Date;
import java.util.List;

import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IContestSessionCaseDAO;
import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestSessionCase;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com
 */
//本类用于竞赛的会话（实例）逻辑实现，主要功能包括竞赛实例创建的处理，竞赛结束的处理，竞赛结束后的奖励核算
public class ContestSessionService {
    
    IExamContestDAO contestDAO;//竞赛DAO
    IContestSessionCaseDAO sessionDAO;//竞赛会话周期实例DAO
    IContestCaseDAO caseDAO;//成绩实例DAO

    public IExamContestDAO getContestDAO() {
        return contestDAO;
    }
    
    public void setContestDAO(IExamContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }
    
    public IContestSessionCaseDAO getSessionDAO() {
        return sessionDAO;
    }
    
    public void setSessionDAO(IContestSessionCaseDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }
    
    public IContestCaseDAO getCaseDAO() {
        return caseDAO;
    }
    
    public void setCaseDAO(IContestCaseDAO caseDAO) {
        this.caseDAO = caseDAO;
    }
    
    public void begainContestSession(ExamContestSession contest) {
        ContestSessionCase session = sessionDAO.findContestSessionCaseByContestAndSessionStr(contest.getId(), contest.getSessionStr());
        if (session == null) {
            session = new ContestSessionCase();
            session.setExamination(contest);
            session.setSessionStr(contest.getSessionStr());
            session.setStat("processing");
            this.sessionDAO.addContestSessionCase(session);
        } else {
            session.setStat("processing");
            this.sessionDAO.updateContestSessionCase(session);
        }
        contest.setSessionCase(session);//设置此竞赛的当前会话实例，只保存在内存中，不会被持久化
    }

    //按竞赛结束单个会话
    public void endContestSession(ExamContestSession contest) {
        ContestSessionCase session = sessionDAO.findContestSessionCaseByContestAndSessionStr(contest.getId(), contest.getSessionStr());
        if (session != null) {
            this.endContestSession(session);
        }
    }

    //按竞赛会话结束会话
    public void endContestSession(ContestSessionCase session) {
        if (session != null) {
            this.CommitAwardBySession(session);
            session.setStat("submitted");
            session.setSubmitTime(new Date());
            this.sessionDAO.updateContestSessionCase(session);
        }
    }

    //处理某竞赛的所有未提交竞赛会话
    public void endAllContestSessionByContest(ExamContestSession contest) {
        List<ContestSessionCase> sessions = sessionDAO.findUnSubmittedContestSessionCaseByContest(contest.getId());
        if (sessions != null) {
            for (ContestSessionCase session : sessions) {
                this.endContestSession(session);
            }
        }
    }

    //对参加此次竞赛实例的用户发放奖励
    public void CommitAwardBySession(ContestSessionCase session) {
        List<ContestCase> ccs = this.caseDAO.findUnAwardedContestCaseByContestSession(session.getId());
        if (ccs != null) {
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            ExamContestSession s = session.getExamination();
            int s1 = (int) s.getFirstScore();
            int s2 = (int) s.getSecondScore();
            int s3 = (int) s.getThirdScore();
            int s4 = (int) s.getFourthScore();
            int s5 = (int) s.getFifthScore();
            int s6 = (int) s.getParticipateScore();
            String name = s.getName() + "（" + s.getSessionStr() + "）";
            for (ContestCase cc : ccs) {
                int rank = cc.getRanking();
                if (rank == 1 && s1 != 0) {
                    bsl.log("参加竞赛 " + name + " 取得第1名，获得奖励", s1, cc.getUser());
                } else if (rank == 2 && s2 != 0) {
                    bsl.log("参加竞赛 " + name + " 取得第2名，获得奖励", s2, cc.getUser());
                } else if (rank == 3 && s3 != 0) {
                    bsl.log("参加竞赛 " + name + " 取得第3名，获得奖励", s3, cc.getUser());
                } else if (rank == 4 && s4 != 0) {
                    bsl.log("参加竞赛 " + name + " 取得第4名，获得奖励", s4, cc.getUser());
                } else if (rank == 5 && s5 != 0) {
                    bsl.log("参加竞赛 " + name + " 取得第5名，获得奖励", s5, cc.getUser());
                } 
                //所有人都有参与奖
                if (s6 != 0) {
                    bsl.log("参加竞赛 " + name + " 获得参与奖", s6, cc.getUser());
                }
                cc.setIfAwarded(true);
                this.caseDAO.updateContestCase(cc);
            }
        }
        
    }
    
    public static void main(String args[]) {
        
    }
    
}
