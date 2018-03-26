package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.entity.BbsFileModel;


public interface IShareDAO {
    
    public List<BbsUser> findSharedUsersByFile(String fileId);
    public List<BbsFileModel> findSharedBbsFileByUsr(String userId);
    public void updateFileShareUser(String fileId,List<BbsUser> users);
    
}
