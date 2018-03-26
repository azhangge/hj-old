package com.hjedu.examination.service.impl;

import java.util.List;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;

/**
 *
 * @author huajie
 */
public class ChoiceQuestionService {

    IChoiceQuestionDAO qDAO;

    private static IRereCacheInstance<ChoiceQuestion> ins = null;

    public IRereCacheInstance getInstance() {
        if (ins == null) {
            ins = RereCacheManager.getReplicatedInstance("choice_questions");
        }
        return ins;
    }

    public IChoiceQuestionDAO getqDAO() {
        return qDAO;
    }

    public void setqDAO(IChoiceQuestionDAO qDAO) {
        this.qDAO = qDAO;
    }

    public void reBuildCache(boolean ifPublish) {
        IRereCacheInstance<List<ChoiceQuestion>> ci = getInstance();
        ci.destroy();
    }

    public void addChoiceQuestion(ChoiceQuestion ec) {
        IRereCacheInstance<ChoiceQuestion> ci = getInstance();
        ci.add(ec.getId(), ec);
        this.qDAO.addChoiceQuestion(ec);
    }

    public void deleteChoiceQuestion(String id) {
        IRereCacheInstance<ChoiceQuestion> ci = getInstance();
        ci.remove(id);
        this.qDAO.deleteChoiceQuestion(id);
    }

    public ChoiceQuestion findChoiceQuestion(String id) {
        IRereCacheInstance<ChoiceQuestion> ci = getInstance();
        ChoiceQuestion cq = ci.fetchObject(id);
        if (cq == null) {
            cq = qDAO.findChoiceQuestion(id);
            if (cq != null) {
                ci.add(id, cq);
            }
        }

        return cq;
    }

}
