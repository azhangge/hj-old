package com.hjedu.examination.dao.impl;

import java.io.Serializable;
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
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.entity.WrongKnowledge;

public class WrongKnowledgeDAO implements IWrongKnowledgeDAO, Serializable {
    
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
    public void addWrongKnowledge(WrongKnowledge m) {
        this.entityManager.persist(m);
    }
    
    @Override
    public void updateWrongKnowledge(WrongKnowledge m) {
        this.entityManager.merge(m);
    }
    
    @Override
    public void wrongTimesPlusOne(String id, String userId) {
        WrongKnowledge wk = this.findWrongKnowledgeByKnowledgeAndUsr(id,userId);
        if (wk == null) {
            wk = new WrongKnowledge();
            wk.setKnowledgeId(id);
            wk.setUserId(userId);
            wk.setWrongTimes(1L);
            this.addWrongKnowledge(wk);
        } else {
            wk.setWrongTimes(wk.getWrongTimes() + 1L);
            this.updateWrongKnowledge(wk);
        }
    }
    
    @Override
    public void recordWrong(String id, String userId) {
        WrongKnowledge wk = this.findWrongKnowledgeByKnowledgeAndUsr(id,userId);
        if (wk == null) {
            wk = new WrongKnowledge();
            wk.setKnowledgeId(id);
            wk.setUserId(userId);
            wk.setWrongTimes(1L);
            this.addWrongKnowledge(wk);
        } 
    }
    
    @Override
    public void deleteWrongKnowledge(String id) {
        WrongKnowledge c = this.entityManager.find(WrongKnowledge.class, id);
        this.entityManager.remove(c);
    }
    
    @Override
    public WrongKnowledge findWrongKnowledge(String id) {
        WrongKnowledge c = this.entityManager.find(WrongKnowledge.class, id);
        return c;
    }
    
    @Override
    public List<WrongKnowledge> findAllWrongKnowledge() {
        String q = "Select cq from WrongKnowledge cq order by cq.genTime desc";
        List<WrongKnowledge> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<WrongKnowledge> findWrongKnowledgeByUsr(String userId) {
        String q = "Select cq from WrongKnowledge cq where cq.userId=:userId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("userId", userId);
        List<WrongKnowledge> cqs = qu.getResultList();
        return cqs;
    }
    
    public WrongKnowledge findWrongKnowledgeByKnowledgeAndUsr(String kid,String userId) {
        String q = "Select cq from WrongKnowledge cq where cq.knowledgeId=:kid and cq.userId=:userId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("kid", kid);
        qu.setParameter("userId", userId);
        List<WrongKnowledge> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public void deleteWrongKnowledgeByKnowledge(String id) {
        String q = "delete from WrongKnowledge ms where ms.knowledgeId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteWrongKnowledgeByUsr(String id) {
        String q = "delete from WrongKnowledge ms where ms.userId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }
}
