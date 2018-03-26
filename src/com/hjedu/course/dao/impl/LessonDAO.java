package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.hazelcast.util.CollectionUtil;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.SpringHelper;

public class LessonDAO implements ILessonDAO, Serializable {
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    @PersistenceContext
    private EntityManager entityManager;
    
    private String simpleHQLPart = "select new "+LessonVO.class.toString().substring(6)+"(l.id,l.name,lt.name,l.classNum,l.ord,l.availableBegain,l.availableEnd) from Lesson l left join LessonType lt on l.lessonType.id=lt.id where 1=1 ";
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addLesson(Lesson m) {
    	this.entityManager.persist(m);
    	updatePracticeNum(m, 1);
        updateLessonType(m);
    }

    @Override
    public void deleteLesson(String id) {
        Lesson c = findLesson(id);
        if(c!=null){
        	this.entityManager.remove(c);
        	this.logger.log("删除了课程：" + c.getName());
        	updatePracticeNum(c, 3);
        	updateLessonType(c);
        }
    }

    @Override
    public int deleteLessonsByLessonTypeId(List<String> idlist) {
    	if(idlist!=null && idlist.size()>0){
    		String q = "delete from Lesson ls where ls.lessonType.id in :list";
    		Query qu=entityManager.createQuery(q);
    		qu.setParameter("list", idlist);
    		return qu.executeUpdate();
    	}
    	return 0;
    }
    
    @Override
    public List<Lesson> findChapterLesson(String businessId, int chapterType){
    	String q = "select yis from Lesson yis where yis.businessId=:businessId and yis.chapterType=:chapterType order by yis.genTime desc";
    	Query qu=entityManager.createQuery(q);
    	qu.setParameter("businessId", businessId);
    	qu.setParameter("chapterType", chapterType);
        List<Lesson> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<Lesson> findChapterLessonss(String businessId, String typeId, int chapterType){
    	String q = "select yis from Lesson yis where yis.businessId=:businessId and yis.lessonType.id=:typeId and yis.chapterType=:chapterType";
    	Query qu=entityManager.createQuery(q);
    	qu.setParameter("businessId", businessId);
    	qu.setParameter("chapterType", chapterType);
    	qu.setParameter("typeId", typeId);
        List<Lesson> ps = qu.getResultList();
        return ps;
    }
    
    public List<Lesson> findChilds(String businessId, String parentId){
    	String q = "select yis from Lesson yis where yis.businessId=:businessId and yis.parentId=:parentId";
    	Query qu=entityManager.createQuery(q);
    	qu.setParameter("businessId", businessId);
    	qu.setParameter("parentId", parentId);
        List<Lesson> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<Lesson> findAllLesson() {
        String q = "Select cq from Lesson cq order by cq.ord desc";
        List<Lesson> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<Lesson> findLessonByAdmin(AdminInfo admin) {
    	String q="";
    	if(admin.getGrp().equals("company")){
    		q = "select yis from Lesson yis where :admin member of yis.lessonType.admins order by yis.ord";
    	}else{
    		return findAllLesson();
    	}
        Query qu=entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        List<Lesson> ps = qu.getResultList();
        return ps;
    }

    @Override
    public List<Lesson> findAllShowedLesson() {
        String q = "Select cq from Lesson cq where cq.ifShow=true order by cq.ord";
        List<Lesson> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<Lesson> findAllShowedLessonByType(String typeId) {
        String q = "Select cq from Lesson cq where cq.ifShow=true and cq.lessonType.id=:typeId order by cq.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<Lesson> findAllLessonByType(String typeId) {
        String q = "Select cq from Lesson cq where  cq.lessonType.id=:typeId order by cq.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<Lesson> findAllLessonByTypes(List<String> ids) {
        String q = "Select cq from Lesson cq where  cq.lessonType.id in :ids";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("ids", ids);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<Lesson> findAllUntypedLesson() {
        String q = "Select cq from Lesson cq where cq.lessonType = null order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getLessonNumByType(String id) {
        String q = "Select count(cq) from Lesson cq where cq.lessonType.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public long getShowedLessonNumByType(String id) {
        String q = "Select count(cq) from Lesson cq where cq.ifShow=true and cq.lessonType.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public List<Lesson> findMostLessonByType(String typeId) {
        String q = "Select cq from Lesson cq where cq.ifShow=true and cq.lessonType.id=:typeId order by cq.readCount desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        qu.setMaxResults(5);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<Lesson> findLessonByType(String typeId) {
        String q = "Select cq from Lesson cq where cq.lessonType.id=:typeId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        List<Lesson> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public Lesson findLesson(String id) {
    	Lesson c = null;
    	if(StringUtil.isNotEmpty(id)){
    		c = this.entityManager.find(Lesson.class, id);
    	}
        return c;
    }

    @Override
    public void updateLesson(Lesson m) {
    	this.entityManager.merge(m);
    	updatePracticeNum(m,2);
    	updateLessonType(m);
    }
    
    private void updateLessonType(Lesson lesson) {
    	if(lesson!=null){
    		ILessonTypeDAO LessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    		LessonType lt =  lesson.getLessonType();
    		if(lt!=null){
    			List<Lesson> ls = findAllLessonByType(lt.getId());
    			if(CollectionUtil.isNotEmpty(ls)){
    				long num = 0;
    				float keshi = 0f;
    				BigDecimal keshi1 = new BigDecimal(Float.toString(keshi));
    				
    				long totalTime = 0;
    				for(Lesson l : ls){
    					num = num+l.getScorePaid();
    					keshi1 = keshi1.add(new BigDecimal(Double.toString(l.getClassNum())));
    					totalTime = totalTime + l.getTotalTime();
    				}
    				lt.setTotalScorePaid(num);
    				lt.setTotalClassNum(Double.valueOf(keshi1.toString()));
    				lt.setTotalTime(totalTime);
    				LessonTypeDAO.updateLessonType(lt);
    			}
    		}
    	}
    }
    
    /**
     * 更新资料以及资料所属课程练习数目，1：新增资料;2:修改资料；3：删除资料
     * @param m
     * @param type
     */
    private void updatePracticeNum(Lesson m,int type){
    	int newNum = 0,oldNum=0;
    	//学习资料更新：课程原有练习数目-该资料原来关联数目+现在关联数目
    	List<ModuleExamination2> mets = m.getModuleExaminations();
    	if(mets!=null&&mets.size()>0){
    		//避免有空数据，计算当前关联练习数目
    		for(ModuleExamination2 met : mets){
    			if(met!=null){
    				ExamModuleModel mod = met.getModule();
    				if(mod!=null){
    					newNum = (int) (newNum + mod.getTotalNum());
    				}
    			}
    		}
    	}
    	oldNum = m.getPracticeNum();
    	if(type==3){
    		newNum = 0;
    	}
    	LessonType lt = m.getLessonType();
    	if(lt!=null){
    		lt.setPracticeNum(lt.getPracticeNum()-oldNum+newNum);
    		ILessonTypeDAO LessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    		LessonTypeDAO.updateLessonType(lt);
    	}
    	m.setPracticeNum(newNum);
    }
    
    @Override
	public List<Lesson> findLessonByIdList(List<String> ids) {
    	List<Lesson> ps = new LinkedList<>();
    	if(ids!=null&&ids.size()>0){
    		String q = "select yis from Lesson yis where yis.id in :ids";
    		Query qu=entityManager.createQuery(q).setParameter("ids", ids);
    		ps = qu.getResultList();
		}
		return ps;
	}
    
    @Override
    public List<LessonVO> findLessonVO(int firstSize,int pageSize){
        String q = this.simpleHQLPart;
        Query qu = this.entityManager.createQuery(q);
        @SuppressWarnings("unchecked")
		List<LessonVO> cqs = qu.getResultList();
        return cqs;
    }

	@Override
	public int getLessonsNumByOrder(Object object, boolean b) {
		return 0;
	}
	
	@Override
	public List<LessonVO> findLessonVOsByCondition(Map<String, Object> filterMap,int firstSize,int pageSize,String field,int type,String businessId) {
        String q = this.simpleHQLPart;
        q = "select new "+LessonVO.class.toString().substring(6)+"(l.id,l.name,lt.name,l.classNum,l.ord,l.availableBegain,l.availableEnd) from Lesson l left join l.lessonType lt where 1=1 and l.businessId=:businessId ";
        if(filterMap!=null){
        	q += getLessonVOQueryStrByFilter(filterMap, "l");
        }
        if(StringUtil.isNotEmpty(field)){
        	if(field.equals("courseName")){
        		field = field.replace("courseName", "lessonType.name");
        	}
        	q += QueryUtil.getOrderStrByOrder(field, "l", type);
        }
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        QueryUtil.setQuerySize(qu, firstSize, pageSize);
        @SuppressWarnings("unchecked")
		List<LessonVO> cqs = qu.getResultList();
        return cqs;
    }
	
	@Override
	public long getNumByCondition(Map<String, Object> fms,String businessId) {
        String q = "select count(l) from Lesson l where 1=1 and l.businessId=:businessId";
        q += getLessonVOQueryStrByFilter(fms, "l");
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        return QueryUtil.getNumByQuery(qu);
    }

	@Override
	public void updateLessonOrd(String id, int ord) {
		if(StringUtil.isNotEmpty(id)){
			String q = "update Lesson l set l.ord=:ord where l.id=:id";
			Query qu = this.entityManager.createQuery(q);
			qu.setParameter("ord", ord);
			qu.setParameter("id", id);
			qu.executeUpdate();
		}
	}
	
	private String getLessonVOQueryStrByFilter(Map<String, Object> filterMap,String tableName){
		String q = "";
		if(filterMap!=null){
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				if(entry!=null){
					String filterProperty = entry.getKey();
					String filterValue = entry.getValue().toString();
					if (StringUtil.isNotEmpty(filterValue)) {
						if(filterProperty.equals("courseName")){
							q = q + " and "+tableName+"." + "lessonType.name" + " like '%" + filterValue + "%' ";
						}else{
							q = q + " and "+tableName+"." + filterProperty + " like '%" + filterValue + "%' ";
						}
					}
				}
			}
		}
		return q;
	}
}
