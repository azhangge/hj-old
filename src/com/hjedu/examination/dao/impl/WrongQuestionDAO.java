package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.WrongQuestion;

public class WrongQuestionDAO implements IWrongQuestionDAO, Serializable {

    IChoiceQuestionDAO choiceDAO;
    IMultiChoiceQuestionDAO multiChoiceDAO;
    IFillQuestionDAO fillDAO;
    IJudgeQuestionDAO judgeDAO;
    IEssayQuestionDAO essayDAO;
    IFileQuestionDAO fileDAO;
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IChoiceQuestionDAO getChoiceDAO() {
        return choiceDAO;
    }

    public void setChoiceDAO(IChoiceQuestionDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    public IMultiChoiceQuestionDAO getMultiChoiceDAO() {
        return multiChoiceDAO;
    }

    public void setMultiChoiceDAO(IMultiChoiceQuestionDAO multiChoiceDAO) {
        this.multiChoiceDAO = multiChoiceDAO;
    }

    public IFillQuestionDAO getFillDAO() {
        return fillDAO;
    }

    public void setFillDAO(IFillQuestionDAO fillDAO) {
        this.fillDAO = fillDAO;
    }

    public IJudgeQuestionDAO getJudgeDAO() {
        return judgeDAO;
    }

    public void setJudgeDAO(IJudgeQuestionDAO judgeDAO) {
        this.judgeDAO = judgeDAO;
    }

    public IEssayQuestionDAO getEssayDAO() {
        return essayDAO;
    }

    public void setEssayDAO(IEssayQuestionDAO essayDAO) {
        this.essayDAO = essayDAO;
    }

    public IFileQuestionDAO getFileDAO() {
        return fileDAO;
    }

    public void setFileDAO(IFileQuestionDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    @Override
    public void addWrongQuestion(WrongQuestion m) {
        this.entityManager.persist(m);
    }

    @Override
    public void updateWrongQuestion(WrongQuestion m) {
        this.entityManager.merge(m);
    }

    @Override
    public void deleteWrongQuestion(String id) {
        WrongQuestion c = this.entityManager.find(WrongQuestion.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public WrongQuestion findWrongQuestion(String id) {
        WrongQuestion c = this.entityManager.find(WrongQuestion.class, id);
        return c;
    }

    @Override
    public void wrongTimesPlusOne(String id, String userId, String type) {
        WrongQuestion wk = this.findWrongQuestionByQuestionAndUsr(id, userId);
        if (wk == null) {
            wk = new WrongQuestion();
            wk.setQuestionId(id);
            wk.setUserId(userId);
            wk.setQuestionType(type);
            wk.setWrongTimes(1L);
            this.addWrongQuestion(wk);
        } else {
            wk.setWrongTimes(wk.getWrongTimes() + 1L);
            this.updateWrongQuestion(wk);
        }
    }
    
    /**
     * 本方法与wrongTimesPlusOne的区别是本方法只记录错误，不进行错误次数+1操作
     * @param id
     * @param userId
     * @param type 
     */
    @Override
    public void recordWrong(String id, String userId, String type) {
        WrongQuestion wk = this.findWrongQuestionByQuestionAndUsr(id, userId);
        if (wk == null) {
            wk = new WrongQuestion();
            wk.setQuestionId(id);
            wk.setUserId(userId);
            wk.setQuestionType(type);
            wk.setWrongTimes(1L);
            this.addWrongQuestion(wk);
        } 
    }

    @Override
    public WrongQuestion findWrongQuestionByQuestionAndUsr(String kid, String userId) {
        String q = "Select cq from WrongQuestion cq where cq.questionId=:kid and cq.userId=:userId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("kid", kid);
        qu.setParameter("userId", userId);
        List<WrongQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    public List<WrongQuestion> findWrongQuestionByTypeAndUsr(String type, String userId, int first, int pageSize) {
        String q = "Select cq from WrongQuestion cq where cq.questionType=:kid and cq.userId=:userId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("kid", type);
        qu.setParameter("userId", userId);
        qu.setFirstResult(first);
        qu.setMaxResults(pageSize);
        List<WrongQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<WrongQuestion> findAllWrongQuestion() {
        String q = "Select cq from WrongQuestion cq order by cq.genTime desc";
        List<WrongQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<WrongQuestion> findWrongQuestionByUsr(String userId, int first, int pageSize) {
        String q = "Select cq from WrongQuestion cq where cq.userId=:userId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("userId", userId);
        qu.setFirstResult(first);
        qu.setMaxResults(pageSize);
        List<WrongQuestion> cqs = qu.getResultList();
        return cqs;
    }

    public long getWrongChoiceQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='choice'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public long getWrongMultiChoiceQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='mchoice'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public long getWrongFillQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='fill'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public long getWrongJudgeQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='judge'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public long getWrongEssayQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='essay'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public long getWrongFileQuestionNumByUsr(String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:id and ms.questionType='file'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public List<ChoiceQuestion> findWrongChoiceQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("choice", userId, first, pageSize);
        List<ChoiceQuestion> css = new LinkedList<ChoiceQuestion>();
        for (WrongQuestion w : cqs) {
            ChoiceQuestion cq = this.choiceDAO.findChoiceQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }
            }
        }
        return css;
    }

    @Override
    public List<MultiChoiceQuestion> findWrongMultiChoiceQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("mchoice", userId, first, pageSize);
        List<MultiChoiceQuestion> css = new LinkedList<MultiChoiceQuestion>();
        for (WrongQuestion w : cqs) {
            MultiChoiceQuestion cq = this.multiChoiceDAO.findMultiChoiceQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }
            }
        }
        return css;
    }

    @Override
    public List<FillQuestion> findWrongFillQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("fill", userId, first, pageSize);
        List<FillQuestion> css = new LinkedList<FillQuestion>();
        for (WrongQuestion w : cqs) {
            FillQuestion cq = this.fillDAO.findFillQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }

            }
        }
        return css;
    }

    @Override
    public List<JudgeQuestion> findWrongJudgeQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("judge", userId, first, pageSize);
        List<JudgeQuestion> css = new LinkedList<JudgeQuestion>();
        for (WrongQuestion w : cqs) {
            JudgeQuestion cq = this.judgeDAO.findJudgeQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }
            }
        }
        return css;
    }

    @Override
    public List<EssayQuestion> findWrongEssayQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("essay", userId, first, pageSize);
        List<EssayQuestion> css = new LinkedList<EssayQuestion>();
        for (WrongQuestion w : cqs) {
            EssayQuestion cq = this.essayDAO.findEssayQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }
            }
        }
        return css;
    }

    @Override
    public List<FileQuestion> findWrongFileQuestionByUsr(String userId, int first, int pageSize) {
        List<WrongQuestion> cqs = this.findWrongQuestionByTypeAndUsr("file", userId, first, pageSize);
        List<FileQuestion> css = new LinkedList<FileQuestion>();
        for (WrongQuestion w : cqs) {
            FileQuestion cq = this.fileDAO.findFileQuestion(w.getQuestionId());
            if (cq != null) {
                if (!css.contains(cq)) {
                    cq.setWrongTimes(w.getWrongTimes());
                    css.add(cq);
                }
            }
        }
        return css;
    }

    @Override
    public void deleteWrongQuestionByQuestion(String id) {
        String q = "delete from WrongQuestion ms where ms.questionId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }

    @Override
    public void deleteWrongQuestionByUsr(String id) {
        String q = "delete from WrongQuestion ms where ms.userId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }

    @Override
    public void deleteWrongQuestionByUsrAndType(String uid, String type) {
        String q = "delete from WrongQuestion ms where ms.userId=:id and ms.questionType=:type";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        qu.setParameter("type", type);
        qu.executeUpdate();
    }

    @Override
    public long getQuestionNumByTypeAndUsr(String type, String userId) {
        String q = "Select count(ms) from WrongQuestion ms where ms.userId=:userId and ms.questionType=:type";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("userId", userId);
        qu.setParameter("type", type);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
}
