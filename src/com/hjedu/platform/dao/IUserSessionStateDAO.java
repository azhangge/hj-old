
package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.UserSessionState;


public interface IUserSessionStateDAO {

    public void updateUserSessionState(UserSessionState comModel);

    public void addUserSessionState(UserSessionState partnerModel);

    public void deleteAll();
    
    public void deleteAllOld();

    public void deleteUserSessionState(String id);
    
    public void deleteUserSessionStateBySessionId(String id);

    public List<UserSessionState> findAllUserSessionState();
    
    public List<UserSessionState> findUserSessionStateByUsr(final String uid);

    public UserSessionState findUserSessionState(String id);
    
    public UserSessionState findUserSessionStateBySessionId(final String sid);
}
