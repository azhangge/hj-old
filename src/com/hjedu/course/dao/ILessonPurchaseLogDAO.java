package com.hjedu.course.dao;

import java.util.Date;
import java.util.List;

import com.hjedu.course.vo.PurchaseLog;
import com.hjedu.customer.entity.AdminInfo;

public interface ILessonPurchaseLogDAO {
	public List<PurchaseLog> findPurchaseLogByAdmin(AdminInfo admin, Date startTime, Date endTime,String businessId) ;
    
	public List<PurchaseLog> findAllLessonLog(Date startTime, Date endTime,String businessId);
}
