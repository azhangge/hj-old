package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.IExternalUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.ExternalUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class BbsUserDAO implements IBbsUserDAO, Serializable {
	private static final Logger logger = Logger.getLogger(BbsUserDAO.class);
    @PersistenceContext
    private EntityManager entityManager;

    IExternalUserDAO externalDAO;

	public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public IExternalUserDAO getExternalDAO() {
        return externalDAO;
    }

    public void setExternalDAO(IExternalUserDAO externalDAO) {
        this.externalDAO = externalDAO;
    }

    public void addBbsUser(BbsUser user) {
    	this.entityManager.persist(user);
    }

    public void addBbsUser2(BbsUser user) {
    	this.entityManager.persist(user);
    	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    	IBuyCourseService buyCourseService = SpringHelper.getSpringBean("BuyCourseService");
    	List<LessonType> lts = lessonTypeDAO.findfreeLessonType();
    	for(LessonType lt : lts){
    		buyCourseService.buyCourse2(lt, user);
    	}
    }
    
    @Override
    public void updateBbsUser(BbsUser user) {
        this.entityManager.merge(user);
        ClientSession ab = JsfHelper.getBean("clientSession");
        if(ab!=null){
        	ab.refreshUser();
        }
    }
    
    public void updateBbsUser2(BbsUser user) {
        this.entityManager.merge(user);
        ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        IBuyCourseService buyCourseService = SpringHelper.getSpringBean("BuyCourseService");
    	List<LessonType> lts = lessonTypeDAO.findfreeLessonType();
    	for(LessonType lt : lts){
    		buyCourseService.buyCourse2(lt, user);
    	}
        ClientSession ab = JsfHelper.getBean("clientSession");
        if(ab!=null){
        	ab.refreshUser();
        }
    }
    
    public void deleteBbsUser(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        if(u!=null){
        	this.entityManager.remove(u);
        }
        
    }

    @Override
    public List<BbsUser> findAllBbsUser(String businessId) {
        String q = "Select us from BbsUser us where us.businessId=:businessId and us.type is null";
        Query qu = entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<BbsUser> bul = qu.getResultList();
        return bul;
    }
    
    @Override
    public List<BbsUser> findAllBbsUserOrderByDept2() {
    	logger.debug("开始查询管理员管理下的用户");
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
    	logger.debug("获取管理员对象");
    	String q="Select us from BbsUser us order by us.groupId, us.enabled desc ,us.activated  desc";
    	if(!ai.getGrp().trim().equals("admin")){
    		 q = "Select us from BbsUser us where us.id<>'a427a963-6edd-41a8-a527-7c39db6df5e7' order by us.groupId, us.enabled desc ,us.activated  desc";
    	}
        List<BbsUser> ais = entityManager.createQuery(q).getResultList();
        logger.debug("查询结束");
        return ais;
    }

    @Override
    public List<BbsUser> findBbsUserByDeptNotInExam(String departId, String examId, Date startDate, Date endDate) {
        String q = "Select us from BbsUser us where us.groupStr like '%" + departId + "%' and us.id NOT IN (select ec.user.id from ExamCase ec where ec.examination.id=:examId and ec.genTime between :startDate and :endDate)";
        Query qu = entityManager.createQuery(q);
        //qu.setParameter("departId", departId);
        qu.setParameter("examId", examId);
        qu.setParameter("startDate", startDate);
        qu.setParameter("endDate", endDate);
        List<BbsUser> ais = qu.getResultList();
        return ais;
    }
    
    @Override
    public List<BbsUser> findBbsUserByDept(String departId) {
        String q = "Select us from BbsUser us where us.groupStr like '%" + departId + "%'  and us.type is null";
        Query qu = entityManager.createQuery(q);
        List<BbsUser> ais = qu.getResultList();
        return ais;
    }

    public BbsUser findBbsUserBySessionId(final String sid) {
        String q = "Select us from BbsUser us where us.sessionId=:sid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("sid", sid);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
    }

    public List<BbsUser> findBbsUserLogined() {
        String q = "Select us from BbsUser us where us.sessionId <> null";
        List<BbsUser> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public List<BbsUser> findBbsUserModule2ing() {
        String q = "Select us from BbsUser us where us.module2Id <> null";
        List<BbsUser> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public List<BbsUser> findBbsUserExaming() {
        String q = "Select us from BbsUser us where us.examId <> null";
        List<BbsUser> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    @Override
    public BbsUser findBbsUser(String id) {
        if (id != null) {
            BbsUser user = this.entityManager.find(BbsUser.class, id);
            return user;
        } else {
            return null;
        }

    }

    @Override
    public BbsUser findSysUser() {
        BbsUser user = this.findBbsUserByUrn("admin");
        if (user == null) {
            user = new BbsUser();
            user.setUsername("admin");
            user.setName("系统管理员");
            user.setPassword(String.valueOf(System.nanoTime()));
            user.setRegTime(new Date(System.currentTimeMillis() - ((long) 10) * 60 * 60 * 1000 * 24 * 365));
            this.addBbsUser(user);
        }
        return user;

    }

    @Override
    public List<BbsUser> findTeacherOpt(String businessId) {
    	 String q = "select u from BbsUser u where u.id not in (select t.user.id from Teacher t where t.user.id is not null and t.user.businessId=:businessId1) and u.businessId=:businessId2 and u.type is null";
    	 Query qu = this.entityManager.createQuery(q);
    	 qu.setParameter("businessId1", businessId);
    	 qu.setParameter("businessId2", businessId);
    	 List<BbsUser> ais = qu.getResultList();
         return ais;
    }
    
    public BbsUser findBbsUserByUrn(final String urn) {
        String q = "Select us from BbsUser us where us.username=:urn";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", urn);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
    }

    @Override
    public List<BbsUser> findBbsUsersLikeUrn(final String urn) {
        String q = "Select us from BbsUser us where us.username like '%" + urn + "%'";
        List<BbsUser> as = entityManager.createQuery(q).getResultList();
        return as;
    }

    public BbsUser findBbsUserByEmail(final String email) {
        String q = "Select us from BbsUser us where us.email=:email";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("email", email);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
    }

    @Override
    public BbsUser findBbsUserByPid(String pid) {
        String q = "Select us from BbsUser us where us.pid=:pid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
    }

	@Override
	public BbsUser findBbsUserByPhone(String phone) {
		BbsUser bs= null;
		String q = "Select us from BbsUser us where us.tel=:phone";
		Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        List<BbsUser> as = qu.getResultList();
        if(as.size()>0){
        	bs = as.get(0);
        }
        return bs;
	}

	@Override
	public List<BbsUser> findBbsUserByPhones(String phone) {
		String q = "Select us from BbsUser us where us.tel=:phone";
		Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        List<BbsUser> as = qu.getResultList();
        return as;
	}

	public BbsUser findBbsUserByExtid(String extid) {
		String q = "Select us from BbsUser us where us.externalId=:extid";
		Query qu = this.entityManager.createQuery(q);
        qu.setParameter("extid", extid);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
	}
	
	@Override
	public BbsUser findBbsUserByPhoneAppNotNull(String phone) {//完全注册完毕
		String q = "Select us from BbsUser us where us.tel=:phone and us.appRegTime is not null";
		Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
	}
	
	@Override
	public BbsUser findBbsUserByPhoneAppNull(String phone) {//后台有导入，但没有注册完毕
		String q = "Select us from BbsUser us where us.tel=:phone and us.appRegTime is null";
		Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
	}
	
    @Override
    public BbsUser findBbsUserByCid(String cid) {
        String q = "Select us from BbsUser us where us.cid=:cid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("cid", cid);
        List<BbsUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            BbsUser ai = (BbsUser) as.get(0);
            return ai;
        }
    }

    @Override
    public long getBbsUserNum() {
        String q = "Select count(ms) from BbsUser ms";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public void enableUser(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setEnabled(true);
        this.updateBbsUser(u);
    }

    @Override
    public void disableUser(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setEnabled(false);
        MyLogger.echo(u.getUsername() + " disabled.");
        this.updateBbsUser(u);
    }

    @Override
    public void deleteBbsUserByUrn(String urn) {
        if (urn != null) {
            BbsUser u = this.findBbsUserByUrn(urn);
            if (u != null) {
                this.entityManager.remove(u);
            }
        }
    }

    @Override
    public void activateUser(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setActivated(true);
        this.updateBbsUser(u);
    }

    @Override
    public void enMarkDel(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setMarkDel(true);
        this.updateBbsUser(u);
    }

    @Override
    public void deMarkDel(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setMarkDel(false);
        this.updateBbsUser(u);
    }

    @Override
    public void check(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setChecked(true);
        this.updateBbsUser(u);
    }

    @Override
    public void uncheck(String id) {
        BbsUser u = this.entityManager.find(BbsUser.class, id);
        u.setChecked(false);
        this.updateBbsUser(u);
    }

    @Override
    public List<BbsUser> findBbsUserByLessonTypeId(String tid) {
        String q = "Select us from BbsUser us where us.lessonTypeStr like '%"+tid+"%'";
        Query qu = this.entityManager.createQuery(q);
        List<BbsUser> as = qu.getResultList();
        return as;
    }

	@Override
	public BbsUser findBbsUserByPhoneBusinessId(String phone, String businessId) {
		BbsUser bs = null;
		String q = "Select us from BbsUser us where us.tel =:phone and us.businessId = :businessId";
		Query qu = this.entityManager.createQuery(q);
		qu.setParameter("phone", phone);
		qu.setParameter("businessId", businessId);
        List<BbsUser> as = qu.getResultList();
        if(as.size()>0){
        	bs=as.get(0);
        }
		return bs;
	}

	@Override
	public long getBbsUserNumByBusinessId(String businessId) {
		String q = "Select count(ms) from BbsUser ms where ms.businessId = :businessId and ms.type is null";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
	}
}
