package com.hjedu.platform.dao;

import com.hjedu.platform.entity.OnlinePayConfig;

public interface IOnlinePayConfigDAO {

    public abstract void updateOnlinePayConfig(OnlinePayConfig m);

    public abstract OnlinePayConfig findOnlinePayConfig();


}
