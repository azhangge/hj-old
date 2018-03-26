package com.hjedu.examination.dao;


import java.util.List;
import java.util.Map;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.buffet.BuffetExamCase;

public interface IBuffetExamCaseDAO {

    public abstract void addBuffetExamCase(BuffetExamCase m);

    public abstract void updateBuffetExamCase(BuffetExamCase m);

    public abstract void deleteBuffetExamCase(String id);

    public abstract BuffetExamCase findBuffetExamCase(String id);

    public abstract List<BuffetExamCase> findAllBuffetExamCase(String businessId);
    
    public abstract List<BuffetExamCase> findAllSubmittedBuffetExamCase();

    public abstract List<BuffetExamCase> findBuffetExamCaseByExamination(String examId);
    
    public List<BuffetExamCase> findSubmittedBuffetExamCaseByExamination(String id);
    
    public List<BuffetExamCase> findLotsOfBuffetExamCase(int offSet, int num);
    
    public List<BuffetExamCase> findOrderedBuffetExamCase(int offSet, int num,String field,String type);
    
    public List<BuffetExamCase> findBuffetExamCaseByLike( Map<String, String> fms);
    
    public long getBuffetExamCaseNum() ;
    
    public long getBuffetExamCaseNumByAdmin(AdminInfo admin);
    
    public List<BuffetExamCase> findLotsOfBuffetExamCaseByAdmin(AdminInfo admin,int offSet, int num);
    
    public List<BuffetExamCase> findOrderedBuffetExamCaseByAdmin(AdminInfo admin,int offSet, int num,String field,String type);
    
    public List<BuffetExamCase> findBuffetExamCaseByLikeAndAdmin(AdminInfo admin, Map<String, String> fms) ;
    
    public abstract List<BuffetExamCase> findBuffetExamCaseByUser(String userId);
    
    public List<BuffetExamCase> findBuffetExamCaseByExaminationAndUser(String questionId,String uid);
    
    public long getParticipateNumByExamAndUser(String examId,String uid);
    
    public void deleteAllBuffetExamCase();
}
