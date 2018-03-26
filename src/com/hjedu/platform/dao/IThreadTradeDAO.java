package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsThreadTrade;


public interface IThreadTradeDAO {

    public abstract List<BbsThreadTrade> findThreadTradeByThread(String tid);
    
    public abstract List<BbsThreadTrade> findThreadTradeByUsr(String tid);

    public abstract void updateThreadTrade(BbsThreadTrade tt);

    public abstract BbsThreadTrade findThreadTrade(String id);
    
    public void addThreadTrade(BbsThreadTrade trade);
    
    public void deleteThreadTrade(String id) ;
}
