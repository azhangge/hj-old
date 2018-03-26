package com.hjedu.customer.dao;

import java.util.List;

import com.hjedu.customer.entity.ExternalUser;



public interface IExternalUserDAO {
	public abstract void addExternalUser(ExternalUser paramExternalUser);

    public abstract void updateExternalUser(ExternalUser user);

    public abstract void deleteExternalUser(String id);

    public abstract ExternalUser findExternalUser(String id);

    public abstract ExternalUser findExternalUserByNo(String paramString);
    
    public abstract List<ExternalUser> findExternalUsersLikeUrn(String urn);
    
    public List<ExternalUser> findAllExternalUserOrderByDept() ;

    public abstract ExternalUser findExternalUserByEmail(String paramString);
    
    public abstract ExternalUser findExternalUserByPid(String pid);
    
    public ExternalUser findExternalUserByCid(String cid);
    
    public long getExternalUserNum();

    public abstract List<ExternalUser> findAllExternalUser();

   
}