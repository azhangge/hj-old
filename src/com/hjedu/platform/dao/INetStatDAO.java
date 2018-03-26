package com.hjedu.platform.dao;

import com.hjedu.platform.entity.NetStat;


public interface INetStatDAO {
    public void addNetStat(NetStat stat);
    public NetStat findRecentStat();
}
