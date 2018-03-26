package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;

/**
 *
 * @author Administrator
 */
public class ExamDepartmentService implements Serializable {

    IDictionaryDAO dicDAO;
    
    private static Map<String, IRereCacheInstance> cacheMap = new HashMap<String, IRereCacheInstance>();

//    private static IRereCacheInstance ins = null;

    public IRereCacheInstance getInstance(String businessId) {
    	IRereCacheInstance ins = cacheMap.get(businessId);
        if (ins == null) {
        	ins = this.reBuildCache();
        	cacheMap.put(businessId, ins);
        }
        return ins;
    }

    public IDictionaryDAO getDicDAO() {
        return dicDAO;
    }

    public void setDicDAO(IDictionaryDAO dicDAO) {
        this.dicDAO = dicDAO;
    }

    public IRereCacheInstance reBuildCache() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	IRereCacheInstance ins = RereCacheManager.getReplicatedInstance(businessId+"_departments");
        RereCacheInstanceReplicated ci = (RereCacheInstanceReplicated) ins;
        ci.removeAllWithoutMsg();
        //将所有的章节数据作为List装入缓存
        List<DictionaryModel> dics = dicDAO.findAllDictionaryModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        ci.addWithoutMsg(businessId+"_departments", dics);
        //将所有的章节数据作为Map装入缓存
        Map idsMap = new HashMap();
        List<DictionaryModel> defaultDepartments = new ArrayList();
        for (DictionaryModel em : dics) {
            idsMap.put(em.getId(), em);
            if (em.isAsDefault()) {
                defaultDepartments.add(em);
            }
        }
        ci.addWithoutMsg(businessId+"_idsMap", idsMap);
        ci.addWithoutMsg(businessId+"_defaultDepartments", defaultDepartments);
        //将所有的章节数据的子节点装入缓存
        Map sonsMap = new HashMap();
        for (DictionaryModel em : dics) {
            List<DictionaryModel> dics2 = new ArrayList();
            for (DictionaryModel dic : dics) {
                if (em.getId().equals(dic.getFatherId())) {
                    dics2.add(dic);
                }
            }
            sonsMap.put(em.getId(), dics2);
        }
        ci.addWithoutMsg(businessId+"_sonsMap", sonsMap);
        return ins;
    }

    public List<DictionaryModel> findAllDictionaryModel() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	
        IRereCacheInstance<List<DictionaryModel>> ci = getInstance(businessId);
        List<DictionaryModel> exams = ci.fetchObject(businessId+"_departments");
        //若实例已经被逐出缓存，则重新构建
        if (exams == null) {
            this.reBuildCache();
            exams = ci.fetchObject(businessId+"_departments");
        }
        return exams;
    }

    public List<DictionaryModel> findAllSonDictionaryModel(String id) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<Map> ci = getInstance(businessId);
        Map<String, List<DictionaryModel>> dics = ci.fetchObject(businessId+"_sonsMap");
        if (dics == null) {
            this.reBuildCache();
            dics = ci.fetchObject(businessId+"_sonsMap");
        }
        if (dics != null) {
        	//过滤掉非此用户下的部门
        	List<DictionaryModel> dicList = dics.get(id);
        	return ExternalUserUtil.filterByAdmin(dicList);
        }
        return dics.get(id);
    }
    
    public List<DictionaryModel> findAllSonDictionaryModel2(String id) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<Map> ci = getInstance(businessId);
        Map<String, List<DictionaryModel>> dics = ci.fetchObject(businessId+"_sonsMap");
        if (dics == null) {
            this.reBuildCache();
            dics = ci.fetchObject(businessId+"_sonsMap");
        }
        return dics.get(id);
    }

    public List<DictionaryModel> findAllDefaultDepartments() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        IRereCacheInstance<List<DictionaryModel>> ci = getInstance(businessId);
        List<DictionaryModel> dics = ci.fetchObject(businessId+"_defaultDepartments");
        if (dics == null) {
            this.reBuildCache();
            dics = ci.fetchObject(businessId+"_defaultDepartments");
        }
        return ExternalUserUtil.filterByAdmin(dics);
    }

    public String findAllDefaultDepartmentStr() {
        StringBuilder sb = new StringBuilder();
        List<DictionaryModel> ys = this.findAllDefaultDepartments();
        for (DictionaryModel dm : ys) {
            sb.append(dm.getId());
            sb.append(";");
        }
        return sb.toString();
    }

    public DictionaryModel findDictionaryModel(String id,String businessId) {
        IRereCacheInstance<Map> ci = getInstance(businessId);
        if(ci==null) return null;
        Map<String, DictionaryModel> exams = ci.fetchObject(businessId+"_idsMap");
        if (exams == null) {
            this.reBuildCache();
            exams = ci.fetchObject(businessId+"_idsMap");
        }
        if (exams != null) {
            return exams.get(id);
        }
        return null;
    }

    public DictionaryModel findDictionaryModelByName(String name) {
        if (name != null) {
            List<DictionaryModel> ems = this.findAllDictionaryModel();
            for (DictionaryModel em : ems) {
                if (name.equals(em.getName())) {
                    return em;
                }
            }
        }
        return null;
    }

    public List<DictionaryModel> loadAllDescendants(String fid) {
        List<DictionaryModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }

    public void loadAllDescendants(String fid, List<DictionaryModel> sons) {
        List<DictionaryModel> ls = this.findAllSonDictionaryModel(fid);
        if (ls.isEmpty()) {
            return;
        } else {
            for (DictionaryModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }

    /**
     * 更新代价非常高，全面重建缓存
     *
     * @param exam
     */
    public void updateDictionaryModel(DictionaryModel exam) {
        dicDAO.updateDictionaryModel(exam);
        this.reBuildCache();
    }

    /**
     * 添加代价非常高，全面重建缓存
     *
     * @param exam
     */
    public void addDictionaryModel(DictionaryModel exam) {
        dicDAO.addDictionaryModel(exam);
        this.reBuildCache();
    }

    /**
     * 删除代价非常高，全面重建缓存
     *
     * @param id
     */
    public void deleteDictionaryModelAndAllDescendants(String id,String bussinessId) {
        dicDAO.deleteDictionaryModelAndAllDescendants(id,bussinessId);
        this.reBuildCache();
    }

    public void deleteAllDictionary(String bussinessId) {
        this.dicDAO.deleteAllDictionary(bussinessId);
        this.reBuildCache();
    }

    public List<DictionaryModel> cloneList(List<DictionaryModel> list) {
        List<DictionaryModel> newList = new ArrayList();
        if (list != null) {
            for (DictionaryModel d : list) {
                newList.add((DictionaryModel) d.clone());
            }
        }
        return newList;
    }

}
