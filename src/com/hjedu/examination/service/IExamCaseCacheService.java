package com.hjedu.examination.service;

import java.io.Serializable;
import java.util.List;

import com.hjedu.examination.entity.ExamCase;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;

/**
 *
 * @author huajie
 */
public interface IExamCaseCacheService extends Serializable {

    public void reBuildCache(boolean ifPublish);

    public void addExamCase(ExamCase ec);
    
    public void publishExamCases(List<String> ids);

    public void updateExamCase(ExamCase ec);

    public void deleteExamCase(String id);

    public ExamCase findExamCase(String id);

    public void updateExamCaseIfExists(ExamCase ec);
    
    /**
     * 找出缓存中某用户刚才的考试成绩
     *
     * @param uid
     * @return
     */
    public List<ExamCase> findRecentExamCasesByUser(String uid);

}
