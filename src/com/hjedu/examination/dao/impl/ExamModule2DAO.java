package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class ExamModule2DAO implements IExamModule2DAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    
    IModuleBuffetPartDAO mbuffetPartDAO;
    IModuleRandom2PartDAO mrandom2PartDAO;
    IChoiceQuestionDAO choiceDAO;
    IMultiChoiceQuestionDAO mchoiceDAO;
    IFillQuestionDAO fillDAO;
    IJudgeQuestionDAO judgeDAO;
    IEssayQuestionDAO essayDAO;
    IFileQuestionDAO fileDAO;
    ICaseQuestionDAO caseDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public IModuleBuffetPartDAO getMbuffetPartDAO() {
        return mbuffetPartDAO;
    }

    public void setMbuffetPartDAO(IModuleBuffetPartDAO mbuffetPartDAO) {
        this.mbuffetPartDAO = mbuffetPartDAO;
    }

    public IModuleRandom2PartDAO getMrandom2PartDAO() {
        return mrandom2PartDAO;
    }

    public void setMrandom2PartDAO(IModuleRandom2PartDAO mrandom2PartDAO) {
        this.mrandom2PartDAO = mrandom2PartDAO;
    }

    public IChoiceQuestionDAO getChoiceDAO() {
        return choiceDAO;
    }

    public void setChoiceDAO(IChoiceQuestionDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    public IMultiChoiceQuestionDAO getMchoiceDAO() {
        return mchoiceDAO;
    }

    public void setMchoiceDAO(IMultiChoiceQuestionDAO mchoiceDAO) {
        this.mchoiceDAO = mchoiceDAO;
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

    public ICaseQuestionDAO getCaseDAO() {
        return caseDAO;
    }

    public void setCaseDAO(ICaseQuestionDAO caseDAO) {
        this.caseDAO = caseDAO;
    }

    @Override
    public void addExamModuleModel(ExamModuleModel p) {
        entityManager.persist(p);
    }

    @Override
    public void deleteExamModuleModel(String pid) {
        this.mbuffetPartDAO.deleteModuleBuffetPartByModule(pid);
        this.mrandom2PartDAO.deleteModuleBuffetPartByModule(pid);
        //手动调用试题DAO删除，虽然用级联方式也可删除，但和试题相关的其它东西不一定都能被级联到
        choiceDAO.deleteChoiceQuestionByModule(pid);
        mchoiceDAO.deleteMultiChoiceQuestionByModule(pid);
        fillDAO.deleteFillQuestionByModule(pid);
        judgeDAO.deleteJudgeQuestionByModule(pid);
        essayDAO.deleteEssayQuestionByModule(pid);
        fileDAO.deleteFileQuestionByModule(pid);
        caseDAO.deleteCaseQuestionByModule(pid);
        //删除简单随机试卷、自助练习、随机试卷、章节练习下的模块对应数据
        String q1="delete from ModuleRandomPaper mm where mm.module.id='"+pid+"'";
        //String q2="delete from ModuleBuffetPart mm where mm.module.id='"+pid+"'";
        //String q3="delete from ModuleRandom2Part mm where mm.module.id='"+pid+"'";
        String q4="delete from ModuleModule2Part mm where mm.module.id='"+pid+"'";
        entityManager.createQuery(q1).executeUpdate();
        //entityManager.createQuery(q2).executeUpdate();
        //entityManager.createQuery(q3).executeUpdate();
        entityManager.createQuery(q4).executeUpdate();
        
        //删除模块下的数据
        ExamModuleModel p = entityManager.find(ExamModuleModel.class, pid);
        entityManager.remove(p);
    }

    @Override
    public void deleteExamModuleModelAndAllDescendants(String id) {
        List<ExamModuleModel> sons = new ArrayList();
        this.loadAllDescendants(id, sons);

        for (ExamModuleModel s : sons) {
            try {
                //this.mbuffetPartDAO.deleteModuleBuffetPartByModule(s.getId());
                //this.mrandom2PartDAO.deleteModuleBuffetPartByModule(s.getId());
                this.deleteExamModuleModel(s.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //entityManager.remove(s);
        }
        this.deleteExamModuleModel(id);
    }

    @Override
    public List<ExamModuleModel> loadAllDescendants(String fid) {
        List<ExamModuleModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }

    @Override
    public void loadAllDescendants(String fid, List<ExamModuleModel> sons) {
        List<ExamModuleModel> ls = this.findAllSonExamModuleModel(fid, CookieUtils.getBusinessId(JsfHelper.getRequest()));
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }

    @Override
    public List<ExamModuleModel> findAllExamModuleModel(String businessId) {
        String q = "Select ass from ExamModuleModel ass where ass.businessId=:businessId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<ExamModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<ExamModuleModel> findRootExamModuleModelByRootId(String rootId) {
        String q = "Select ass from ExamModuleModel ass where ass.fatherId=:rootId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("rootId", rootId);
        List<ExamModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<ExamModuleModel> findExamModuleModelByUrlType(final String type) {
        String q = "Select ass from ExamModuleModel ass where ass.urlType=:type order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("type", type);
        List<ExamModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<ExamModuleModel> findAllSonExamModuleModel(final String fatherID,String businessId) {
        String q = "Select ass from ExamModuleModel ass where ass.fatherId=:id and ass.businessId=:businessId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        qu.setParameter("businessId", businessId);
        List<ExamModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public ExamModuleModel findExamModuleByName(String name) {
        String q = "Select cq from ExamModuleModel cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        List<ExamModuleModel> modules = qu.getResultList();
        if (!modules.isEmpty()) {
            ExamModuleModel m = (ExamModuleModel) modules.get(0);
            return m;
        } else {
            return null;
        }
    }
    
    @Override
    public List<ExamModuleModel> findExamModuleBySimilarName(String name) {
        String q = "Select cq from ExamModuleModel cq where cq.name like '%"+name+"%'";
        Query qu = this.entityManager.createQuery(q);
        List<ExamModuleModel> modules = qu.getResultList();
        return modules;
    }
    
    @Override
	public List<ExamModuleModel> findExamModuleByIdList(List<String> ids) {
    	List<ExamModuleModel> ps = new LinkedList<>();
    	if(ids!=null&&ids.size()>0){
    		String q = "select yis from ExamModuleModel yis where yis.id in :ids";
    		Query qu=entityManager.createQuery(q).setParameter("ids", ids);
    		ps = qu.getResultList();
    	}
		return ps;
	}

    @Override
    public void deleteAllExamModuleExceptRoot(String rootId) {
        List<ExamModuleModel> ms = this.findAllExamModuleModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        for (ExamModuleModel e : ms) {
            if (!e.getId().equals(rootId)) {
                this.deleteExamModuleModel(e.getId());
            }
        }
    }

    @Override
    public ExamModuleModel findExamModuleModel(String pid) {
        if (pid == null) {
            return null;
        } else {
            return entityManager.find(ExamModuleModel.class, pid);
        }
    }

    @Override
    public void updateExamModuleModel(ExamModuleModel p) {
        entityManager.merge(p);
    }
    
    public List<ExamModuleModel> findAllSonExamModuleModelAndAdmin(final String fatherID,AdminInfo admin,String businessId) {
        String q = "Select ass from ExamModuleModel ass where ass.fatherId=:id and ass.businessId=:businessId and :admin member of ass.admins order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        qu.setParameter("businessId", businessId);
        qu.setParameter("admin", admin);
        List<ExamModuleModel> ys = qu.getResultList();
        return ys;
    }
}
