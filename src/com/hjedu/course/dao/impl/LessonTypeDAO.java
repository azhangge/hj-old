package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class LessonTypeDAO extends BaseDAOImpl<LessonType> implements ILessonTypeDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addLessonType(LessonType paramLessonType) {
        this.entityManager.persist(paramLessonType);
    }

    @Override
    public void deleteLessonType(String paramString) {
        LessonType yp = (LessonType) this.entityManager.find(LessonType.class, paramString);
        this.entityManager.remove(yp);
        ILessonTypeLogDAO dao = SpringHelper.getSpringBean("LessonTypeLogDAO");
        dao.deleteLogByLessonType(paramString);
        ILessonLogDAO dao2 = SpringHelper.getSpringBean("LessonLogDAO");
        dao2.deleteLogByType(paramString);
    }

    @Override
    public int deleteLessonTypes(List<String> idlist) { 
    	if(idlist!=null && idlist.size()>0){
    		String q = "delete from LessonType lt where lt.id in :list";
    		Query qu=entityManager.createQuery(q);
    		qu.setParameter("list", idlist);
    		return qu.executeUpdate();
    	}
    	return 0;
    }
    
    @Override
    public List<LessonType> findAllLessonType(String businessId) {
        String q = "select yis from LessonType yis where yis.businessId=:businessId order by yis.ord";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<LessonType> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<LessonType> findLessonTypeByAdminAndTime(AdminInfo admin,String businessId) {
    	String q="";
    	if(admin.getGrp().equals("company")){
    		q = "select yis from LessonType yis where yis.businessId=:businessId and :admin member of yis.admins order by yis.genTime desc";
    	}else{
    		q = "select yis from LessonType yis where yis.businessId=:businessId order by yis.genTime desc";
    	}
    	Query qu=entityManager.createQuery(q);
    	qu.setParameter("businessId", businessId);
    	if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        List<LessonType> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<LessonType> findLessonTypeByAdmin(AdminInfo admin,String businessId) {
    	String q="";
    	if(admin.getGrp().equals("company")){
    		q = "select yis from LessonType yis where yis.businessId=:businessId and :admin member of yis.admins order by yis.ord";
    	}else{
    		q = "select yis from LessonType yis where yis.businessId=:businessId order by yis.ord";
    	}
    	Query qu=entityManager.createQuery(q);
    	qu.setParameter("businessId", businessId);
    	if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        List<LessonType> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<LessonType> findAllEnableLessonType(String businessId) {
//        String q = "select yis from LessonType yis where :admin member of yis.admins and yis.enabled=true order by yis.ord";
        String q = "select yis from LessonType yis where yis.enabled=true and yis.secreted<>1 and yis.businessId=:businessId order by yis.ord";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
//        qu.setParameter("admin", admin);
        List<LessonType> ps = qu.getResultList();
        return ps;
    }
    
    //所有课程，包括私密课程
    public List<LessonType> findAllEnableLessonType2(String businessId) {
      String q = "select yis from LessonType yis where yis.enabled=true and yis.businessId=:businessId order by yis.ord";
      Query qu=entityManager.createQuery(q);
      qu.setParameter("businessId", businessId);
      List<LessonType> ps = qu.getResultList();
      return ps;
  }
   
	@Override
	public List<LessonType> findAllLessonTypeByTagId(String lid,String businessId) {
		List<LessonType> ll = this.findAllEnableLessonType(businessId);
		List<LessonType> result = new LinkedList<>();
        for (LessonType lt : ll) {
            if (lt.getLabelStr() != null) {
                if (lt.getLabelStr().contains(lid)) {
                	result.add(lt);
                }
            }
        }
        return result;
	}

	//登录后搜索分类
	public List<LessonType> findAllLessonTypeByTagId2(String lid,BbsUser user,String businessId) {
		List<LessonType> ll = this.findAllEnableLessonType2(businessId);
		List<LessonType> result = new LinkedList<>();
        for (LessonType lt : ll) {
            if (lt.getLabelStr() != null) {
                if (lt.getLabelStr().contains(lid)) {
                	if(!lt.isSecreted()){//不是私密课程
                		result.add(lt);
                	}else if(lt.isSecreted() && StringUtil.isNotEmpty(user.getLessonTypeStr()) &&  user.getLessonTypeStr().contains(lt.getId())){//是私密课程
                		result.add(lt);
                	}
                }
            }
        }
        return result;
	}
    
	@Override
	public List<LessonType> findLessonTypesByName(String name,String businessId) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.secreted<>1 and yis.name like '%"+name+"%' and yis.businessId= :businessId order by yis.ord";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<LessonType> ps = qu.getResultList();
        
        q = "select yis from LessonType yis where yis.enabled=true and yis.secreted<>1 and yis.teacher.name like '%"+name+"%' and yis.businessId= :businessId order by yis.ord";
		qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<LessonType> ps2 = qu.getResultList();
        for(LessonType lt : ps){
        	if(!ps2.contains(lt)){
        		ps2.add(lt);
        	}
        }
        return ps2;
	}
	
	//用户登录后搜索课程
	public List<LessonType> findLessonTypesByName2(String name,BbsUser user, String businessId) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.name like '%"+name+"%' and yis.businessId = :businessId order by yis.ord";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<LessonType> ps = qu.getResultList();
        List<LessonType> lts = new ArrayList<LessonType>();
        for(LessonType lt:ps){
        	if(!lt.isSecreted()){//不是私密课程
        		lts.add(lt);
        	}else if(lt.isSecreted() && StringUtil.isNotEmpty(user.getLessonTypeStr()) && user.getLessonTypeStr().contains(lt.getId())){//是私密课程
        		lts.add(lt);
        	}
        }

        q = "select yis from LessonType yis where yis.enabled=true and yis.teacher.name like '%"+name+"%' and yis.businessId= :businessId order by yis.ord";
		qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<LessonType> ps2 = qu.getResultList();
        List<LessonType> lts2 = new ArrayList<LessonType>();
        for(LessonType lt:ps2){
        	if(!lt.isSecreted()){//不是私密课程
        		lts2.add(lt);
        	}else if(lt.isSecreted() && StringUtil.isNotEmpty(user.getLessonTypeStr()) && user.getLessonTypeStr().contains(lt.getId())){//是私密课程
        		lts2.add(lt);
        	}
        }
        
        for(LessonType lt : lts){
        	if(!lts2.contains(lt)){
        		lts2.add(lt);
        	}
        }
        return lts2;
	}
	
    @Override
    public List<LessonType> findAllSecretedLessonType() {
        String q = "select yis from LessonType yis where yis.enabled=true and yis.secreted=1 order by yis.ord";
        Query qu=entityManager.createQuery(q);
        List<LessonType> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<LessonType> findLessonTypeByExam(String eid) {
        String q = "select yis from LessonType yis where yis.enabled=true and yis.examStr like '%"+eid+"%' order by yis.ord";
        Query qu=entityManager.createQuery(q);
        List<LessonType> ps = qu.getResultList();
        return ps;
    }

    @Override
    public LessonType findLessonType(String paramString) {
        LessonType p = (LessonType) this.entityManager.find(LessonType.class, paramString);
        return p;
    }
    
    @Override
    public void updateLessonType(LessonType paramLessonType) {
        this.entityManager.merge(paramLessonType);
    }


	
	@Override
	public List<LessonType> findLessonTypesByTagName(String id, int beginnum, int endnum, String businessId) {
		List<LessonType> result = findAllLessonTypeByTagId(id,businessId);
		List<LessonType> result2 = new LinkedList<>();
		if(result.size()<endnum){
			endnum=result.size();
		}
		if(beginnum==0&&endnum==0){
			return result;
		}
		for(int j=beginnum;j<endnum;j++){
			result2.add(result.get(j));
		}
		return result2;
	}

	@Override
	public List<LessonType> findLessonTypesByNumAndBusinessId(int parseInt, int parseInt2, String businessId) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.businessId= :businessId order by yis.ord ";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(parseInt);
        qu.setMaxResults(parseInt2);
        List<LessonType> ps = qu.getResultList();
        return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypesByTeacher(String name) {
		String q = "select yis from LessonType yis where yis.teacher.name ='"+name+"' ";
        Query qu=entityManager.createQuery(q);
        List<LessonType> ps = qu.getResultList();
        return ps;
	}

	@Override
	public List<LessonType> findLessonTypesByQueryMap(Map<String, String> map) {
		String name = map.get("name");
		String tname = map.get("tname");
		String q = "select yis from LessonType yis where 1=1 ";
		if(!StringUtil.isEmpty(name)){
			q = q + "and yis.name like '%"+name+"%'";
		}
		if(!StringUtil.isEmpty(tname)){
			q = q + "and yis.teacher.name like '%"+tname+"%'";
		}
		q = q +" order by yis.ord";
        Query qu=entityManager.createQuery(q);
        List<LessonType> ps = qu.getResultList();
        return ps;
	}

	@Override
	public List<LessonType> findfreeLessonType() {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.totalScorePaid=0 and yis.secreted<>1";
		Query qu=entityManager.createQuery(q);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypeByIdList(List<String> ids) {
		List<LessonType> ps = new LinkedList<>();
		if(ids!=null&&ids.size()>0){//过滤未上架的课程
			String q = "select yis from LessonType yis where yis.id in :ids and yis.secreted<>1 and yis.enabled = 1";
			Query qu=entityManager.createQuery(q).setParameter("ids", ids);
			ps = qu.getResultList();
		}
		return ps;
	}

	@Override
	public List<LessonType> findSecretLessonTypeByIdList(List<String> ids) {
		List<LessonType> ps = new LinkedList<>();
		if(ids!=null&&ids.size()>0){
			String q = "select yis from LessonType yis where  yis.enabled=true and  yis.id in :ids and yis.secreted=1 and yis.enabled=true";
			Query qu=entityManager.createQuery(q).setParameter("ids", ids);
			ps = qu.getResultList();
		}
		return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypesByTeacherId(String id) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.teacher.id = :id";
		Query qu=entityManager.createQuery(q).setParameter("id", id);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypesHasPractice(List<String> idlist,int firstSize,int pageSize,boolean ifIn, String businessId) {
		String q = "select yis from LessonType yis where yis.businessId=:businessId and yis.enabled=true and yis.practiceNum>0 ";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		Query qu=entityManager.createQuery(q);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		qu.setParameter("businessId", businessId);
		QueryUtil.setQuerySize(qu, firstSize, pageSize);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public long getNumOfLessonTypesHasPractice(List<String> idlist,boolean ifIn, String businessId) {
		String q = "select count(yis) from LessonType yis where yis.enabled=true and yis.businessId=:businessId and yis.enabled=true and yis.practiceNum>0";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		return Long.parseLong(String.valueOf(qu.getResultList().get(0)));
		
	}
	
	@Override
	public List<LessonType> findLessonTypesByOrder(String orderStr, String businessId, String ifenabled,int firstSize,int pageSize,boolean ifDesc,List<String> idlist,boolean ifIn,String queryStr) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.businessId = :businessId";
		q = q + " and yis.secreted<>1 ";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		if(StringUtil.isNotEmpty(ifenabled)){
			q = q+" and yis.enabled="+ifenabled;
		}
		if(StringUtil.isNotEmpty(queryStr)){
			q += queryStr;
		}
		if(StringUtil.isNotEmpty(orderStr)){
			q = q+" order by yis."+orderStr;
			if(ifDesc){
				q = q+" desc ";
			}
		}
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		QueryUtil.setQuerySize(qu, firstSize, pageSize);
		@SuppressWarnings("unchecked")
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypesByOrder2(String orderStr,String ifenabled,int firstSize,int pageSize,boolean ifDesc,List<String> idlist,boolean ifIn,String queryStr) {
		String q = "select yis from LessonType yis where yis.enabled=true";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		if(StringUtil.isNotEmpty(ifenabled)){
			q = q+" and yis.enabled="+ifenabled;
		}
		if(StringUtil.isNotEmpty(queryStr)){
			q += queryStr;
		}
		if(StringUtil.isNotEmpty(orderStr)){
			q = q+" order by yis."+orderStr;
			if(ifDesc){
				q = q+" desc ";
			}
		}
		Query qu=entityManager.createQuery(q);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		QueryUtil.setQuerySize(qu, firstSize, pageSize);
		@SuppressWarnings("unchecked")
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<LessonType> findLessonTypesByOrder3(String orderStr,String businessId,String ifenabled,int firstSize,int pageSize,boolean ifDesc,List<String> idlist,boolean ifIn,String queryStr,BbsUser user) {
		String q = "select yis from LessonType yis where yis.enabled=true and yis.businessId = :businessId";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		if(StringUtil.isNotEmpty(ifenabled)){
			q = q+" and yis.enabled="+ifenabled;
		}
		if(StringUtil.isNotEmpty(queryStr)){
			q += queryStr;
		}
		if(StringUtil.isNotEmpty(orderStr)){
			q = q+" order by yis."+orderStr;
			if(ifDesc){
				q = q+" desc ";
			}
		}
		Query qu=entityManager.createQuery(q);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		
		qu.setParameter("businessId", businessId);
		QueryUtil.setQuerySize(qu, firstSize, pageSize);
		@SuppressWarnings("unchecked")
		List<LessonType> ps = qu.getResultList();
        List<LessonType> lts = new ArrayList<LessonType>();
        for(LessonType lt:ps){
        	if(!lt.isSecreted()){//不是私密课程
        		lts.add(lt);
        	}else if(lt.isSecreted() && StringUtil.isNotEmpty(user.getLessonTypeStr()) && user.getLessonTypeStr().contains(lt.getId())){//是私密课程
        		lts.add(lt);
        	}
        }
		return lts;
	}
	
	@Override
	public long getNumByOrder(String ifenabled,List<String> idlist,boolean ifIn){
		String q = "select count(yis) from LessonType yis where yis.enabled=true ";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		if(StringUtil.isNotEmpty(ifenabled)){
			q = q+" and yis.enabled="+ifenabled;
		}
		Query qu=entityManager.createQuery(q);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		long l = Long.parseLong(String.valueOf(qu.getResultList().get(0)));
		return l;
	}
	
	@Override
	public long getLessonTypesNum(String queryStr, String businessId) {
		String q = "select count(yis) from LessonType yis where yis.enabled=true and yis.businessId=:businessId ";
		if(queryStr!=null){
			q = q+" and "+queryStr;
		}
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		long l = Long.parseLong(String.valueOf(qu.getResultList().get(0)));
		return l;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonType> findLessonTypesByQueryStr(String queryStr,int firstSize,int pageSize) {
		String q = "select yis from LessonType yis where yis.enabled=true ";
		if(queryStr!=null){
			q = q+" and "+queryStr;
		}
		Query qu=entityManager.createQuery(q);
		if(pageSize>0){
			if(firstSize>=0){
				qu.setFirstResult(firstSize);
			}
			qu.setMaxResults(pageSize);
		}
		List<LessonType> ps = new LinkedList<>();
		try {
			QueryUtil.setQuerySize(qu, firstSize, pageSize);
			ps = qu.getResultList();
		} catch (Exception e) {
			MyLogger.error(e);
		}
		return ps;
	}
	
	/**
	 * 待审批和该审批讲师权限内的课程
	 */
	@Override
	public List<LessonType> findOnApprovalPendingLessonTypesByCourseType(List<String> idlist){
		String q = "select lt from LessonType lt where lt.enabled=0 and lt.approved=2 and lt.id in :list";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("list", idlist);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	/**
	 * 审批中和该审批讲师权限内的课程
	 */
	@Override
	public List<LessonType> findOnApprovalInLessonTypesByCourseType(List<String> idlist){
		String q = "select lt from LessonType lt where lt.enabled=0 and lt.approved=3 and lt.id in :list";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("list", idlist);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<LessonType> findOnApprovalLessonTypesByCourseType(List<String> idlist,String opt){
		String q = "select lt from LessonType lt where lt.enabled=0";//  and lt.id in :list";
		if(opt.equals("approvalPending")){
			q = q+" and lt.approved=2";
		}
		if(opt.equals("approvalIn")){
			q = q+" and lt.approved=3";
		}
		q = q+" and lt.id in :list";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("list", idlist);
		List<LessonType> ps = qu.getResultList();
		return ps;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonType> findLessonTypesByBeApprover(String teacherId , int teacherType, int approvalStatus, int enable, int firstSize,
			int pageSize) {
		String q = "select ca from LessonType ca where 1=1 ";
		if(StringUtil.isNotEmpty(teacherId)){
			if(teacherType==0){
				q = q+" and ca.teacher.id=:teacherId ";
			}else if(teacherType==1){
				q = q+" and ca.approveTeacher.id=:teacherId ";
			}
		}
		if(approvalStatus>=0&&approvalStatus<=5){
			q = q+" and ca.approved=:approvalStatus ";
		}
		if(enable==0){
			q = q+" and ca.enabled=false ";
		}else if(enable==1){
			q = q+" and ca.enabled=true ";
		}
		Query query = entityManager.createQuery(q);
		if(StringUtil.isNotEmpty(teacherId)){
			query.setParameter("teacherId", teacherId);
		}
		if(approvalStatus>=0&&approvalStatus<=5){
			query.setParameter("approvalStatus", approvalStatus);
		}
		QueryUtil.setQuerySize(query, firstSize, pageSize);
		List<LessonType> results = query.getResultList();
		return results;
	}
	
	@Override
	public long findLessonTypeNumByBeApprover(String teacherId , int teacherType, int approvalStatus, int enable) {
		String q = "select count(ca) from LessonType ca where 1=1 ";
		if(StringUtil.isNotEmpty(teacherId)){
			if(teacherType==0){
				q = q+" and ca.teacher.id=:teacherId ";
			}else if(teacherType==1){
				q = q+" and ca.approveTeacher.id=:teacherId ";
			}
		}
		if(approvalStatus>=0&&approvalStatus<=5){
			q = q+" and ca.approved=:approvalStatus ";
		}
		if(enable==0){
			q = q+" and ca.enabled=false ";
		}else if(enable==1){
			q = q+" and ca.enabled=true ";
		}
		Query query = entityManager.createQuery(q);
		if(StringUtil.isNotEmpty(teacherId)){
			query.setParameter("teacherId", teacherId);
		}
		if(approvalStatus>=0&&approvalStatus<=5){
			query.setParameter("approvalStatus", approvalStatus);
		}
		return Long.parseLong(String.valueOf(query.getResultList().get(0)));
	}
	
	@Override
	public List<LessonType> findLessonTypesByCondition(String whereSql,Map<String, Object> filterMap,int firstSize,int pageSize,String field,int type) {
        String q = "select l from LessonType l where "+whereSql+" ";
		if(filterMap!=null){
        	q += getQueryStrByFilter(filterMap, "l");
        }
        if(StringUtil.isNotEmpty(field)){
        	if(field.equals("courseName")){
        		field = field.replace("courseName", "lessonType.name");
        	}
        	q += " and l.secreted<>1 ";
        	q += QueryUtil.getOrderStrByOrder(field, "l", type);
        }
        Query qu = this.entityManager.createQuery(q);
        QueryUtil.setQuerySize(qu, firstSize, pageSize);
        @SuppressWarnings("unchecked")
		List<LessonType> cqs = qu.getResultList();
        return cqs;
    }
	
	@Override
	public long getNumByCondition(String whereSql,Map<String, Object> filterMap) {
        String q = "select count(l) from LessonType l where "+whereSql+" ";
		q += getQueryStrByFilter(filterMap, "l");
        Query qu = this.entityManager.createQuery(q);
        return QueryUtil.getNumByQuery(qu);
    }
	
	private String getQueryStrByFilter(Map<String, Object> filterMap,String tableName){
		String q = "";
		if(filterMap!=null){
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				if(entry!=null){
					String filterProperty = entry.getKey();
					String filterValue = entry.getValue().toString();
					if (StringUtil.isNotEmpty(filterValue)) {
						q = q + " and "+tableName+"." + filterProperty + " like '%" + filterValue + "%' ";
					}
				}
			}
		}
		return q;
	}
}
