package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;

/**
 * 本类为考试提供一种缓存机制，使对考试的访问不必每次都访问数据库
 *
 * @author Administrator
 */
public class IntensiveExamService implements Serializable {

    IExaminationDAO examDAO;
    //所有考试作为一个整体存储
    private static IRereCacheInstance<List<Examination>> ins = null;
    

    public IExaminationDAO getExamDAO() {
        return examDAO;
    }

    public void setExamDAO(IExaminationDAO examDAO) {
        this.examDAO = examDAO;
    }

    public IRereCacheInstance getInstance() {
        if (ins == null) {
            ins = RereCacheManager.getReplicatedInstance(CookieUtils.getBusinessId(JsfHelper.getRequest())+"_exams");
            RereCacheInstanceReplicated ci = (RereCacheInstanceReplicated) ins;
            List<Examination> exams = examDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            if (exams != null) {
                //所有的考试作为一个整体存储，同生同灭
                //若单条存储，可能造成部分考试被清出缓存，返回的条目不完整
                ci.addWithoutMsg(CookieUtils.getBusinessId(JsfHelper.getRequest())+"_exams", exams);
            }
            //ci.setLifeLen(0);//缓存数据永不过期
        }
        return ins;
    }

    public void reBuildCache() {
        IRereCacheInstance<Examination> ci = getInstance();
        ci.destroy();
        ins = null;
    }

    /**
     * 将考试列表存回缓存
     *
     * @param exams
     */
    private void storeExams(List<Examination> exams) {
        IRereCacheInstance<List<Examination>> ci = getInstance();
        RereCacheInstanceReplicated cii = (RereCacheInstanceReplicated) ci;
        cii.addWithoutMsg(CookieUtils.getBusinessId(JsfHelper.getRequest())+"_exams", exams);
    }

    public List<Examination> findAllExamination() {
        IRereCacheInstance<List<Examination>> ci = getInstance();
        List<Examination> list = ci.fetchObject(CookieUtils.getBusinessId(JsfHelper.getRequest())+"_exams");
        List<Examination> exams = new LinkedList<>();
        for(Examination e : list){
        	if(e.getExamType()!=null&&e.getExamType().equals("1")){
        		exams.add(e);
        	}
        }
        if (exams == null) {
            exams = examDAO.findAllntensiveExamination();
            this.storeExams(exams);
        }
        return exams;
    }

    /**
     * 返回所有克隆数据，避免对原数据的操作
     * @return 
     */
    public List<Examination> findAllClones() {
        List<Examination> list = this.findAllExamination();
        return this.cloneList(list);
    }

    public List<Examination> findAllShowedExamination() {
        List<Examination> exams1 = this.findAllExamination();
        //返回一个新的列表
        List<Examination> exams2 = new ArrayList();
        for (Examination exam : exams1) {
            if (exam.isIfShow()) {
                exams2.add(exam);
            }
        }
        return exams2;
    }

    public List<Examination> findExaminationByLabel(String lid) {
        List<Examination> exams1 = this.findAllExamination();
        List<Examination> exams2 = new ArrayList();
        for (Examination exam : exams1) {
            if (exam.getLabelStr() != null) {
                if (exam.getLabelStr().contains(lid)) {
                    exams2.add(exam);
                }
            }
        }
        return exams2;
    }

    public Examination findExamination(String examId) {
        List<Examination> exams1 = this.findAllExamination();
        for (Examination exam : exams1) {
            if (exam.getId().equals(examId)) {
                return exam;
            }
        }
        return this.examDAO.findExamination(examId);
    }

    public void updateExamination(Examination exam) {
        List<Examination> exams1 = this.findAllExamination();
        Examination theOne = null;
        for (Examination ex : exams1) {
            if (ex.getId().equals(exam.getId())) {
                theOne = ex;
                break;
            }
        }
        //从列表中找出修改的目标实例
        if (theOne != null) {
            //将原目标实例删除
            exams1.remove(theOne);
            //将新目标实例加入
            exams1.add(exam);
        }
        this.storeExams(exams1);
        this.examDAO.updateExamination(exam);
    }

    public void addExamination(Examination exam) {
        List<Examination> exams1 = this.findAllExamination();
        //将新目标实例加入
        exams1.add(exam);
        this.storeExams(exams1);
        this.examDAO.addExamination(exam);
    }

    public void deleteExamination(String id) {
        List<Examination> exams1 = this.findAllExamination();
        Examination theOne = null;
        for (Examination ex : exams1) {
            if (ex.getId().equals(id)) {
                theOne = ex;
                break;
            }
        }
        //从列表中找出修改的目标实例
        if (theOne != null) {
            //将原目标实例删除
            exams1.remove(theOne);
        }
        this.storeExams(exams1);
        this.examDAO.deleteExamination(id);
    }

    public List<Examination> cloneList(List<Examination> list) {
        List<Examination> newList = new ArrayList();
        if (list != null) {
            for (Examination d : list) {
                newList.add((Examination) d.clone());
            }
        }
        return newList;
    }

}
