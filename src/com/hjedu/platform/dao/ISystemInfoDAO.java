package com.hjedu.platform.dao;

import com.hjedu.platform.entity.SystemInfo;

public interface ISystemInfoDAO {

    public abstract void updateSystemInfo(SystemInfo m);
    
    public abstract SystemInfo findSystemInfoByBusinessId(String businessId);


}
