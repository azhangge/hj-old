package com.hjedu.examination.service.impl;

import java.util.List;

import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;

/**
 *
 * @author huajie
 */
public class MultiChoiceQuestionService {

    IMultiChoiceQuestionDAO qDAO;

    private static IRereCacheInstance<MultiChoiceQuestion> ins = null;

    public IRereCacheInstance getInstance() {
        if (ins == null) {
            ins = RereCacheManager.getReplicatedInstance("multi_choice_questions");
        }
        return ins;
    }

    public IMultiChoiceQuestionDAO getqDAO() {
        return qDAO;
    }

    public void setqDAO(IMultiChoiceQuestionDAO qDAO) {
        this.qDAO = qDAO;
    }

    public void reBuildCache(boolean ifPublish) {
        IRereCacheInstance<List<MultiChoiceQuestion>> ci = getInstance();
        ci.destroy();

    }

    public void addMultiMultiChoiceQuestion(MultiChoiceQuestion ec) {
        IRereCacheInstance<MultiChoiceQuestion> ci = getInstance();
        ci.add(ec.getId(), ec);
    }

    public void deleteMultiMultiChoiceQuestion(String id) {
        IRereCacheInstance<MultiChoiceQuestion> ci = getInstance();
        ci.remove(id);
    }

    public MultiChoiceQuestion findMultiChoiceQuestion(String id) {
        IRereCacheInstance<MultiChoiceQuestion> ci = getInstance();
        MultiChoiceQuestion cq = ci.fetchObject(id);
        if (cq == null) {
            cq = qDAO.findMultiChoiceQuestion(id);
            if (cq != null) {
                ci.add(id, cq);
            }
        }
        return cq;
    }

}
