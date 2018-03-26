package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.platform.dao.INoticeInformationDAO;
import com.hjedu.platform.entity.NoticeInformation;
import com.hjedu.platform.entity.NoticeUser;

public class NoticeInformationDAO extends BaseDAOImpl<NoticeInformation> implements INoticeInformationDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<NoticeInformation> getNoticeInformations(int firstindex,int maxresult){
    	String sql = "select new com.hjedu.platform.entity.NoticeInformation(a.id,a.isReaded,a.userId,b.createDate,b.modifyDate,b.title,b.content) from "
    			+ " NoticeUser a left join NoticeModel b on a.noticeId = b.id";
//    	String q = "select new com.sys.entity.GradeStudent(s.name,s.idCard,g.gradeName) "  
//                + "from Student s,Grade g where s.idGrade=g.id order by g.gradeName"; 
		Query query = this.getEntityManager().createNativeQuery(sql,NoticeInformation.class);
		query.setFirstResult(firstindex);
		query.setMaxResults(maxresult);
        return query.getResultList();  
    }
}