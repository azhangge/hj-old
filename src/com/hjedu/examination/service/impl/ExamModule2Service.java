package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;

/**
 *
 * @author Administrator
 */
public class ExamModule2Service implements Serializable {

    IExamModule2DAO examModule2DAO;

    private static IRereCacheInstance ins = null;
    
    public IExamModule2DAO getExamModule2DAO() {
        return examModule2DAO;
    }

    public void setExamModule2DAO(IExamModule2DAO examModule2DAO) {
        this.examModule2DAO = examModule2DAO;
    }

    public IRereCacheInstance getInstance() {
        if (ins == null) {
            this.reBuildCache();
        }
        return ins;
    }


    /**
     * 重新装载数据
     */
    public void reBuildCache() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        //确保ins不为Null
        ins = RereCacheManager.getReplicatedInstance(businessId);
        RereCacheInstanceReplicated ci = (RereCacheInstanceReplicated) ins;
        ci.removeAllWithoutMsg();
        //将所有的章节数据作为List装入缓存
        
        List<ExamModuleModel> exams = examModule2DAO.findAllExamModuleModel(businessId);
        ci.addWithoutMsg(businessId, exams);
        //将所有的章节数据作为Map装入缓存
        Map idsMap = new HashMap();
        for (ExamModuleModel em : exams) {
            idsMap.put(em.getId(), em);
        }
        ci.addWithoutMsg("idsMap_"+businessId, idsMap);
        //将所有的章节数据的子节点作为MAP装入缓存
        Map sonsMap = new HashMap();
        for (ExamModuleModel em : exams) {
            List<ExamModuleModel> exams2 = new ArrayList();
            for (ExamModuleModel exam : exams) {
                if (em.getId().equals(exam.getFatherId())) {
                    exams2.add(exam);
                }
            }
            sonsMap.put(em.getId(), exams2);
        }
        ci.addWithoutMsg("sonsMap_"+businessId, sonsMap);
    }

    public List<ExamModuleModel> findAllExamModuleModel() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<List<ExamModuleModel>> ci = getInstance();
        List<ExamModuleModel> exams = ci.fetchObject(businessId);
        if (exams == null) {
            this.reBuildCache();
            exams = ci.fetchObject(businessId);
        }
        return exams;
    }

    public List<ExamModuleModel> findAllSonExamModuleModel(String id) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<Map> ci = getInstance();
        Map<String, List<ExamModuleModel>> exams = ci.fetchObject("sonsMap_"+businessId);
        //若实例已经被逐出缓存，则重新构建
        if (exams == null) {
            this.reBuildCache();
            exams = ci.fetchObject("sonsMap_"+businessId);
        }
        if (exams != null) {
            return exams.get(id);
        } else {
            return null;
        }
    }

    public ExamModuleModel findExamModuleModel(String id) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<Map> ci = getInstance();
        Map<String, ExamModuleModel> exams = ci.fetchObject("idsMap_"+businessId);
        if (exams == null) {
            this.reBuildCache();
            exams = ci.fetchObject("idsMap_"+businessId);
        }
        if (exams != null) {
            return exams.get(id);
        }
        return null;
    }

    public ExamModuleModel findExamModuleByName(String name) {
        if (name != null) {
            List<ExamModuleModel> ems = this.findAllExamModuleModel();
            for (ExamModuleModel em : ems) {
                if (name.equals(em.getName())) {
                    return em;
                }
            }
        }
        return null;
    }

    public List<ExamModuleModel> loadAllDescendants(String fid) {
        List<ExamModuleModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }

    public void loadAllDescendants(String fid, List<ExamModuleModel> sons) {
        List<ExamModuleModel> ls = this.findAllSonExamModuleModel(fid);
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }

    public void updateExamModuleModel(ExamModuleModel exam) {
        examModule2DAO.updateExamModuleModel(exam);
        this.reBuildCache();
    }

    public void addExamModuleModel(ExamModuleModel exam) {
        examModule2DAO.addExamModuleModel(exam);
        this.reBuildCache();
    }

    public void deleteExamModuleModelAndAllDescendants(String id) {
        examModule2DAO.deleteExamModuleModelAndAllDescendants(id);
        this.reBuildCache();
    }

    public void deleteAllExamModule() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        examModule2DAO.deleteAllExamModuleExceptRoot(businessId);
        this.reBuildCache();
    }

}
