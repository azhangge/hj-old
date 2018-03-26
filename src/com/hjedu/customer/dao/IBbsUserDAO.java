package com.hjedu.customer.dao;

import java.util.Date;
import java.util.List;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;

public interface IBbsUserDAO {
	public abstract void addBbsUser(BbsUser bbsUser);

    public abstract void updateBbsUser(BbsUser user);

    public abstract void deleteBbsUser(String id);
    
    public abstract void deleteBbsUserByUrn(String urn);

    public abstract BbsUser findBbsUser(String id);
    
    public List<BbsUser> findBbsUserByDeptNotInExam(String departId, String examId, Date startDate, Date endDate);

    public abstract BbsUser findBbsUserByUrn(String paramString);
    
    public abstract List<BbsUser> findBbsUsersLikeUrn(String urn);

    public abstract BbsUser findBbsUserByEmail(String paramString);
    
    public abstract BbsUser findBbsUserByPid(String pid);
    
    public abstract BbsUser findBbsUserByPhone(String phone);
    
    public BbsUser findBbsUserByExtid(String extid);
    
    public abstract BbsUser findBbsUserByPhoneAppNotNull(String phone);
    
    public abstract BbsUser findBbsUserByPhoneAppNull(String phone);
    
    public BbsUser findBbsUserByCid(String cid);
    
    public BbsUser findSysUser();
    
    public long getBbsUserNum();
    
    public long getBbsUserNumByBusinessId(String businessId);

    public abstract List<BbsUser> findAllBbsUser(String businessId);

    public abstract void enableUser(String id);

    public abstract void disableUser(String id);

    public abstract void activateUser(String id);

    public abstract void enMarkDel(String id);

    public abstract void deMarkDel(String id);
    
    public void check(String id);
    
    public void uncheck(String id);

	List<BbsUser> findAllBbsUserOrderByDept2();

	List<BbsUser> findBbsUserByDept(String departId);

	List<BbsUser> findTeacherOpt(String businessId);

	List<BbsUser> findBbsUserByLessonTypeId(String tid);

	public abstract void addBbsUser2(BbsUser user);
	
	List<BbsUser> findBbsUserByPhones(String phone);
	
	BbsUser findBbsUserByPhoneBusinessId(String phone,String businessId);
}