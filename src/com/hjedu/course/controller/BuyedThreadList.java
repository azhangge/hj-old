package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IThreadBookMarkDAO;
import com.hjedu.platform.dao.IThreadTradeDAO;
import com.hjedu.platform.entity.BbsThreadBookMark;
import com.hjedu.platform.entity.BbsThreadTrade;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class BuyedThreadList implements Serializable {

    List<BbsThreadTrade> threadTrades;
    List<BbsThreadBookMark> threadBookMarks;
    IThreadTradeDAO bbsThreadTradeDAO = SpringHelper.getSpringBean("ThreadTradeDAO");
    IThreadBookMarkDAO bbsThreadBookMarkDAO = SpringHelper.getSpringBean("ThreadBookMarkDAO");

    public List<BbsThreadTrade> getThreadTrades() {
        return threadTrades;
    }

    public void setThreadTrades(List<BbsThreadTrade> threadTrades) {
        this.threadTrades = threadTrades;
    }

    public List<BbsThreadBookMark> getThreadBookMarks() {
        return threadBookMarks;
    }

    public void setThreadBookMarks(List<BbsThreadBookMark> threadBookMarks) {
        this.threadBookMarks = threadBookMarks;
    }

    @PostConstruct
    public void init() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.getUsr();
        this.threadTrades = this.bbsThreadTradeDAO.findThreadTradeByUsr(cs.getUsr().getId());
        this.threadBookMarks = this.bbsThreadBookMarkDAO.findThreadBookMarkByUsr(cs.getUsr().getId());
    }

    public String deleteTrade(String id) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.getUsr();
        this.bbsThreadTradeDAO.deleteThreadTrade(id);
        this.threadTrades = this.bbsThreadTradeDAO.findThreadTradeByUsr(cs.getUsr().getId());
        return null;
    }
    
    public String deleteBookMark(String id) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.getUsr();
        this.bbsThreadBookMarkDAO.deleteThreadBookMark(id);
        this.threadBookMarks = this.bbsThreadBookMarkDAO.findThreadBookMarkByUsr(cs.getUsr().getId());
        return null;
    }
}
