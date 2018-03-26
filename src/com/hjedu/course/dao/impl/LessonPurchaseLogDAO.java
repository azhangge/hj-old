package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.activemq.util.GenerateJDBCStatements;

import com.hjedu.course.dao.ILessonPurchaseLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.vo.PurchaseLog;
import com.hjedu.customer.entity.AdminInfo;

public class LessonPurchaseLogDAO  implements ILessonPurchaseLogDAO, Serializable {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public List<PurchaseLog> findPurchaseLogByAdmin(AdminInfo admin,Date startTime,Date endTime,String businessId) {
		String q = "select yis from LessonType yis where :admin member of yis.admins order by yis.genTime desc";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("admin", admin);
		List<LessonType> ps = qu.getResultList();
		List<PurchaseLog> list=new ArrayList<PurchaseLog>();
		for(LessonType p:ps){
			PurchaseLog pl=new PurchaseLog();
			pl.setCourseName(p.getName());
			String q1 = "select sum(yis.scorePaid) from LessonLog yis where yis.lesson.lessonType.id=:id and yis.genTime>=:date1 and yis.genTime<=:date2";
			Query qu1=entityManager.createQuery(q1);
			qu1.setParameter("id", p.getId());
			qu1.setParameter("date1", startTime);
			qu1.setParameter("date2", endTime);
			List list1 = qu1.getResultList();
	        //使用sum函数要注意这里要先判断是否为null，不然会出空指针异常，count函数不用判断null
	        if(list1.get(0) == null){
	            pl.setMoney(0);
	        }
	        else{
	            pl.setMoney((double) list1.get(0));
	        }
	        String q2 = "select count(yis) from LessonLog yis where yis.lesson.lessonType.id=:id and yis.genTime>=:date1 and yis.genTime<=:date2";
			Query qu2=entityManager.createQuery(q2);
			qu2.setParameter("id", p.getId());
			qu2.setParameter("date1", startTime);
			qu2.setParameter("date2", endTime);
			List list2 = qu2.getResultList();
	        pl.setNum(((Long) list2.get(0)).longValue());
			list.add(pl);
		}
		return list;
	}

	@Override
	public List<PurchaseLog> findAllLessonLog(Date startTime,Date endTime,String businessId) {
		String q = "select yis from LessonType yis where yis.businessId = :businessId order by yis.genTime desc";
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		List<LessonType> ps = qu.getResultList();
		List<PurchaseLog> list=new ArrayList<PurchaseLog>();
		for(LessonType p:ps){
			PurchaseLog pl=new PurchaseLog();
			pl.setCourseName(p.getName());
			String q1 = "select sum(yis.scorePaid) from LessonLog yis where yis.lesson.lessonType.id=:id and yis.genTime>=:date1 and yis.genTime<=:date2";
			Query qu1=entityManager.createQuery(q1);
			qu1.setParameter("date1", startTime);
			qu1.setParameter("date2", endTime);
			qu1.setParameter("id", p.getId());
			List list1 = qu1.getResultList();
	        //使用sum函数要注意这里要先判断是否为null，不然会出空指针异常，count函数不用判断null
	        if(list1.get(0) == null){
	            pl.setMoney(0);
	        }
	        else{
	            pl.setMoney((double) list1.get(0));
	        }
	        String q2 = "select count(yis) from LessonLog yis where yis.lesson.lessonType.id=:id and yis.genTime>=:date1 and yis.genTime<=:date2";
			Query qu2=entityManager.createQuery(q2);
			qu2.setParameter("id", p.getId());
			qu2.setParameter("date1", startTime);
			qu2.setParameter("date2", endTime);
			List list2 = qu2.getResultList();
	        pl.setNum(((Long) list2.get(0)).longValue());
			list.add(pl);
		}
		return list;
	}

}
