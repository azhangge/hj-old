package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsScoreLog;


public interface IBbsScoreLogDAO {

    public void updateBbsScoreLog(BbsScoreLog comModel);

    public void addBbsScoreLog(BbsScoreLog partnerModel);

    public void deleteAll();

    public void deleteBbsScoreLog(String id);
    
    public void deleteByUsr(String id);

    public List<BbsScoreLog> findAllBbsScoreLog();
    
    public List<BbsScoreLog> findBbsScoreLogByUsr(final String uid);

    public BbsScoreLog findBbsScoreLog(String id);
}
