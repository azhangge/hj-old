package com.hjedu.examination.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.huajie.cache.CacheItemLifeListener;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;

/**
 *
 * @author huajie
 */
public class ExamCaseCacheService implements IExamCaseCacheService {

    private static IRereCacheInstance<ExamCase> ins = null;

    /**
     * 检查缓存实例是否存在，若不存在则创建并添加生命回调，存在则返回
     *
     * @return
     */
    private static IRereCacheInstance<ExamCase> getLocalInstance() {
        if (ins == null) {
            ins = RereCacheManager.getLocalInstance("exam_cases");
            CacheItemLifeListener li = new ExamCaseCacheLifeListener();
            ins.addLifeListener(li);
        }
        return ins;
    }

    @Override
    public void reBuildCache(boolean ifPublish) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        if (ci != null) {
            ci.removeAll();
        }
    }

    @Override
    public void addExamCase(ExamCase ec) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        ci.setLifeLen(3600);
        ci.add(ec.getId(), ec);
    }

    @Override
    public void publishExamCases(List<String> ids) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        for (String id : ids) {
            ExamCase ec = ci.fetchObject(id);
            if (ec != null) {
                ec.setIfPub(true);
            }
        }
    }

    @Override
    public void updateExamCase(ExamCase ec) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        ci.add(ec.getId(), ec);
    }

    @Override
    public void updateExamCaseIfExists(ExamCase ec) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        ExamCase ecc = ci.fetchObject(ec.getId());
        if (ecc != null) {
            ci.add(ec.getId(), ec);
        }
    }

    @Override
    public void deleteExamCase(String id) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        ci.remove(id);
    }

    @Override
    public ExamCase findExamCase(String id) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        return ci.fetchObject(id);
    }

    /**
     * 找出缓存中某用户刚才的考试成绩
     *
     * @param uid
     * @return
     */
    @Override
    public List<ExamCase> findRecentExamCasesByUser(String uid) {
        IRereCacheInstance<ExamCase> ci = getLocalInstance();
        List<ExamCase> cases = ci.queryByIndexEqual("userId", uid);
        List<ExamCase> temp = new ArrayList();
        for (ExamCase ec : cases) {
            if (!"".equals(ec.getStat())) {
                temp.add(ec);
            }
        }
        return temp;
    }

}
