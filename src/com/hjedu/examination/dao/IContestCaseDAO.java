package com.hjedu.examination.dao;


import java.util.List;
import java.util.Map;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.contest.ContestCase;

public interface IContestCaseDAO {

    public abstract void addContestCase(ContestCase m);

    public abstract void updateContestCase(ContestCase m);

    public abstract void deleteContestCase(String id);

    public abstract ContestCase findContestCase(String id);

    public abstract List<ContestCase> findAllContestCase();
    
    public abstract List<ContestCase> findAllSubmittedContestCase();

    //public abstract List<ContestCase> findContestCaseByContest(String examId);
    
    public List<ContestCase> findContestCaseByContestAndSession(String id,String str);
    
    public List<ContestCase> findSubmittedContestCaseByContest(String id);
    
    public List<ContestCase> findLotsOfContestCase(int offSet, int num);
    
    public List<ContestCase> findOrderedContestCase(int offSet, int num,String field,String type);
    
    public List<ContestCase> findContestCaseByLike( Map<String, Object> fms);
    
    public long getContestCaseNum() ;
    
    public long getContestCaseNumByAdmin(AdminInfo admin);
    
    public List<ContestCase> findLotsOfContestCaseByAdmin(AdminInfo admin,int offSet, int num);
    
    public List<ContestCase> findOrderedContestCaseByAdmin(AdminInfo admin,int offSet, int num,String field,String type);
    
    public List<ContestCase> findContestCaseByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms) ;
    
    public abstract List<ContestCase> findContestCaseByUser(String userId);
    
    public List<ContestCase> findContestCaseByContestAndUser(String questionId,String uid);
    
    public long getParticipateNumByContestAndUser(String examId,String sessionId,String uid);
    
    public long getParticipateNumByContest(String examId);
    
    public long getParticipateNumByContestAndSession(String examId,String str);
    
    public void deleteAllContestCase();
    
    public List<ContestCase> findContestCaseByContestSession(String sid);
    
    public List<ContestCase> findUnAwardedContestCaseByContestSession(String sid);
}
