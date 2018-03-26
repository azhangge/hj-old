package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.huajie.util.SpringHelper;

public class ModuleExamCaseDAO implements IModuleExamCaseDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IFileQuestionDAO fqDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IFileQuestionDAO getFqDAO() {
        return fqDAO;
    }

    public void setFqDAO(IFileQuestionDAO fqDAO) {
        this.fqDAO = fqDAO;
    }

    @Override
    public void addModuleExamCase(ModuleExamCase m) {
        this.entityManager.persist(m);
    }

    

    @Override
    public List<ModuleExamCase> findAllModuleExamCase() {
        String q = "Select cq from ModuleExamCase cq order by cq.genTime desc";
        List<ModuleExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ModuleExamCase> findAllSubmittedModuleExamCase() {
        String q = "Select cq from ModuleExamCase cq where cq.examination.id!='7' and cq.stat='submitted' order by cq.genTime desc";
        List<ModuleExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ModuleExamCase findModuleExamCase(String id) {
        ModuleExamCase c = this.entityManager.find(ModuleExamCase.class, id);
        return c;
    }

    @Override
    public List<ModuleExamCase> findModuleExamCaseByExamination(String id) {
        String q = "Select cq from ModuleExamCase cq where cq.examination.id=:qid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ModuleExamCase> findSubmittedModuleExamCaseByExamination(String id) {
        String q = "Select cq from ModuleExamCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ModuleExamCase findModuleExamCaseByExaminationAndUser(String questionId, String uid) {
        String q = "Select cq from ModuleExamCase cq where cq.examination.id=:qid and cq.user.id=:uid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", questionId);
        qu.setParameter("uid", uid);
        List<ModuleExamCase> mecs = qu.getResultList();
        if (mecs != null) {
            if (!mecs.isEmpty()) {
                return mecs.get(0);
            } else {
                return null;
            }
        }
        return null;

    }

    /**
     * 按用户返回章节随机练习
     *
     * @param userId
     * @return
     */
    @Override
    public List<ModuleExamCase> findModuleExamCaseByUser(String userId) {
        String q = "Select cq from ModuleExamCase cq where cq.user.id=:id and cq.moduleType='random' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    /**
     * 按用户返回章节逐题练习
     *
     * @param userId
     * @return
     */
    @Override
    public List<ModuleExamCase> findModuleExamCase2ByUser(String userId) {
        String q = "Select cq from ModuleExamCase cq where cq.user.id=:id and cq.moduleType='single' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateModuleExamCase(ModuleExamCase m) {
        this.entityManager.merge(m);
    }

    /**
     * 返回章节逐题练习的成绩数量
     *
     * @param examId
     * @return
     */
    @Override
    public long getParticipateNumByExam2(String examId) {
        String q = "Select count(ms) from ModuleExamCase ms where ms.examination.id=:examId and ms.moduleType='single'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public long getParticipateNumByExamAndUser(String examId, String uid) {
        String q = "Select count(ms) from ModuleExamCase ms where ms.examination.id=:examId and ms.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("uid", uid);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public long getExamCaseNum(String businessId) {
        String q = "Select count(ms) from ModuleExamCase ms where ms.businessId=:businessId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public List<ModuleExamCase> findLotsOfExamCase(int offSet, int num,String businessId) {
        String q = "Select cq from ModuleExamCase cq where cq.businessId=:businessId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        qu.setParameter("businessId", businessId);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ModuleExamCase> findOrderedExamCase(int offSet, int num, String field, String type,String businessId) {
        String q = "Select cq from ModuleExamCase cq where cq.businessId=:businessId order by cq." + field + " " + type;
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        qu.setParameter("businessId", businessId);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ModuleExamCase> findExamCaseByLike(Map<String, Object> fms,String businessId) {
        String q1 = "Select cq from ModuleExamCase cq where 1=1 and cq.businessId=:businessId ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("businessId", businessId);
        List<ModuleExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void deleteAllModuleExamCase() {

        //删除所有成绩条目
        String q1 = "delete from m_exam_case_item_choice";
        this.entityManager.createNativeQuery(q1).executeUpdate();
        String q2 = "delete from m_exam_case_item_multi_choice";
        this.entityManager.createNativeQuery(q2).executeUpdate();
        String q3 = "delete from m_exam_case_item_fill";
        this.entityManager.createNativeQuery(q3).executeUpdate();
        String q4 = "delete from m_exam_case_item_judge";
        this.entityManager.createNativeQuery(q4).executeUpdate();
        String q5 = "delete from m_exam_case_item_essay";
        this.entityManager.createNativeQuery(q5).executeUpdate();
        String q6 = "delete from m_exam_case_item_file";
        this.entityManager.createNativeQuery(q6).executeUpdate();
        String q7 = "delete from m_exam_case_item_case";
        this.entityManager.createNativeQuery(q7).executeUpdate();

        String q = "delete from ModuleExamCase cq";
        this.entityManager.createQuery(q).executeUpdate();
    }

    @Override
    public void deleteModuleExamCaseByExam(String eid) {
//        String q = "delete from ModuleExamCase cq where cq.examination.id=:eid";
//        Query qu = this.entityManager.createQuery(q);
//        qu.setParameter("eid", eid);
//        qu.executeUpdate();

        List<ModuleExamCase> cases = this.findModuleExamCaseByExamination(eid);
        for (ModuleExamCase m : cases) {
            this.deleteRelatedItems(m.getId());
            this.entityManager.remove(m);
        }

    }
    @Override
    public void deleteModuleExamCase(String id) {
        ModuleExamCase c = this.entityManager.find(ModuleExamCase.class, id);
        List<ModuleExamCaseItemFile> files = c.getFileItems();
        for (ModuleExamCaseItemFile f : files) {
            this.fqDAO.delFile(f.getId());
        }
        this.deleteRelatedItems(id);
        this.entityManager.remove(c);
    }

    /**
     * 删除某成绩前前其对应的成绩条目全部删除
     * @param caseId 
     */
    private void deleteRelatedItems(String caseId) {
        //删除所有成绩条目
        String q1 = "delete from m_exam_case_item_choice where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q1).executeUpdate();
        String q2 = "delete from m_exam_case_item_multi_choice where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q2).executeUpdate();
        String q3 = "delete from m_exam_case_item_fill where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q3).executeUpdate();
        String q4 = "delete from m_exam_case_item_judge where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q4).executeUpdate();
        String q5 = "delete from m_exam_case_item_essay where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q5).executeUpdate();
        String q6 = "delete from m_exam_case_item_file where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q6).executeUpdate();
        String q7 = "delete from m_exam_case_item_case where case_id='" +caseId+ "'";
        this.entityManager.createNativeQuery(q7).executeUpdate();

    }

    public static void main(String args[]) {
        IModuleExamCaseDAO dao = SpringHelper.getSpringBean("ModuleExamCaseDAO");
        ModuleExamCase cc = dao.findModuleExamCaseByExaminationAndUser("ae5f6959-1be0-4d41-aedc-4726db5877c3", "7fda545b-96fd-482f-ada0-c48cb6cd1b21");
        ModuleExamination2 exam = cc.getExamination();
        System.out.println(exam.getParts().size());
    }

}
