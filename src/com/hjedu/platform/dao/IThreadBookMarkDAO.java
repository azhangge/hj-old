package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsThreadBookMark;
import com.hjedu.platform.entity.BbsThreadTrade;


public interface IThreadBookMarkDAO {

    public abstract List<BbsThreadBookMark> findThreadBookMarkByThread(String tid);
    
    public abstract List<BbsThreadBookMark> findThreadBookMarkByUsr(String tid);

    public abstract void updateThreadBookMark(BbsThreadBookMark tt);

    public abstract BbsThreadBookMark findThreadBookMark(String id);
    
    public void addThreadBookMark(BbsThreadBookMark mark);
    
    public void deleteThreadBookMark(String id) ;
}
