package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CourseType;
import com.huajie.util.SpringHelper;

public class CourseTypeDAO implements ICourseTypeDAO, Serializable {
	private static final long serialVersionUID = 1L;

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
    public void addCourseType(CourseType p) {
        entityManager.persist(p);
    }

    @Override
    public void deleteCourseType(String pid) {
        //删除模块下的数据
        CourseType p = entityManager.find(CourseType.class, pid);
        entityManager.remove(p);
    }

    @Override
    public void deleteCourseTypeAndAllDescendants(String id, String businessId) {
        List<CourseType> sons = new ArrayList<>();
        this.loadAllDescendants(id, sons, businessId);
        for (CourseType s : sons) {
            try {
                this.deleteCourseType(s.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.deleteCourseType(id);
    }

    @Override
    public List<CourseType> loadAllDescendants(String fid, String businessId) {
        List<CourseType> sons = new ArrayList<>();
        this.loadAllDescendants(fid, sons,businessId);
        return sons;
    }

    @Override
    public void loadAllDescendants(String fid, List<CourseType> sons, String businessId) {
        List<CourseType> ls = this.findAllSonCourseType(fid,businessId);
        if (ls.isEmpty()) {
            return;
        } else {
            for (CourseType d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons, businessId);
            }
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<CourseType> findAllCourseTypeWithoutRoot(String rootId) {
        String q = "Select ass from CourseType ass where ass.id!= :rootId and ass.businessId=:businessId order by ass.ord";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("rootId", rootId);
        qu.setParameter("businessId", rootId);
        List<CourseType> ys = qu.getResultList();
        return ys;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<CourseType> findFirstCourseType(String rootId) {
        String q = "Select ass from CourseType ass where ass.fatherId= :rootId order by ass.ord";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("rootId", rootId);
        List<CourseType> ys = qu.getResultList();
        return ys;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<CourseType> findAllSonCourseType(final String fatherID,String businessId) {
        String q = "Select ass from CourseType ass where ass.fatherId=:id and ass.businessId=:businessId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setParameter("id", fatherID);//qu.setParameter("id", fatherID);
        List<CourseType> ys = qu.getResultList();
        return ys;
    }

    @SuppressWarnings("unchecked")
	@Override
    public CourseType findCourseTypeByNameAndBusinessId(String name, String businessId) {
        String q = "Select cq from CourseType cq where cq.name=:name and cq.businessId= :businessId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        qu.setParameter("businessId", businessId);
        List<CourseType> modules = qu.getResultList();
        if (!modules.isEmpty()) {
            CourseType m = (CourseType) modules.get(0);
            return m;
        } else {
            return null;
        }
    }

    @Override
    public void deleteAllCourseTypeWithoutRoot(String rootId) {
        List<CourseType> ms = this.findAllCourseTypeWithoutRoot(rootId);
        for (CourseType e : ms) {
            if (!e.getId().equals(rootId)) {
                this.deleteCourseType(e.getId());
            }
        }
    }

    @Override
    public CourseType findCourseType(String pid) {
        if (pid == null) {
            return null;
        } else {
            return entityManager.find(CourseType.class, pid);
        }
    }

    @Override
    public List<CourseType> findSomeCourseType(List<String> idList) {
        String q = "Select ass from CourseType ass where ass.id in :ids";
        Query qu = this.entityManager.createQuery(q).setParameter("ids", idList);
        List<CourseType> ys = qu.getResultList();
        return ys;
    }
    
    @Override
    public void updateCourseType(CourseType p) {
        entityManager.merge(p);
    }
    
    @SuppressWarnings("unchecked")
	public List<CourseType> findAllSonCourseTypeAndAdmin(final String fatherID,AdminInfo admin) {
        String q = "Select ass from CourseType ass where ass.fatherId=:id and :admin member of ass.admins order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        qu.setParameter("admin", admin);
        List<CourseType> ys = qu.getResultList();
        return ys;
    }

    
	@Override
	public List<CourseType> findNavigationCourseTypeByFatherId(String fatherId) {
        String q = "Select ass from CourseType ass where ass.navigationShow=1 and ass.fatherId= :fatherId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("fatherId", fatherId);
        @SuppressWarnings("unchecked")
        List<CourseType> ys = qu.getResultList();
        return ys;
    }
	
	@Override
	public List<CourseType> findFrontShowCourseTypeByFatherId(String fatherId) {
        String q = "Select ass from CourseType ass where ass.frontShow=1 and ass.fatherId= :fatherId order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("fatherId", fatherId);
        @SuppressWarnings("unchecked")
        List<CourseType> ys = qu.getResultList();
        return ys;
    }
}
