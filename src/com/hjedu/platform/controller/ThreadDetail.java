package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.IThreadBookMarkDAO;
import com.hjedu.platform.dao.IThreadTradeDAO;
import com.hjedu.platform.entity.BbsTalk;
import com.hjedu.platform.entity.BbsThread;
import com.hjedu.platform.entity.BbsThreadBookMark;
import com.hjedu.platform.entity.BbsThreadTrade;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ThreadDetail implements Serializable {

    BbsTalk newTalk = new BbsTalk();
    BbsZone zone = null;
    BbsThread thread = new BbsThread();
    IBbsThreadDAO bbsThreadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
    IThreadTradeDAO bbsThreadTradeDAO = SpringHelper.getSpringBean("ThreadTradeDAO");
    IThreadBookMarkDAO bbsThreadBookMarkDAO = SpringHelper.getSpringBean("ThreadBookMarkDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IBbsTalkDAO bbsTalkDAO = SpringHelper.getSpringBean("BbsTalkDAO");
    @ManagedProperty(value = "#{clientSession}")
    ClientSession cs;
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    int pageShow = 1;
    BbsTalk talk_temp = new BbsTalk();
    int pageCount = 0;
    int talkRows = 10;
    List<BbsTalk> talks = new ArrayList();

    public int getPageShow() {
        return pageShow;
    }

    public void setPageShow(int pageShow) {
        this.pageShow = pageShow;
    }

    public BbsTalk getNewTalk() {
        return newTalk;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTalkRows() {
        return talkRows;
    }

    public void setTalkRows(int talkRows) {
        this.talkRows = talkRows;
    }

    public void setNewTalk(BbsTalk newTalk) {
        this.newTalk = newTalk;
    }

    public BbsThread getThread() {
        return thread;
    }

    public void setThread(BbsThread thread) {
        this.thread = thread;
    }

    public BbsTalk getTalk_temp() {
        return talk_temp;
    }

    public void setTalk_temp(BbsTalk talk_temp) {
        this.talk_temp = talk_temp;
    }

    public List<BbsTalk> getTalks() {
        return talks;
    }

    public void setTalks(List<BbsTalk> talks) {
        this.talks = talks;
    }

    public BbsZone getZone() {
        return zone;
    }

    public void setZone(BbsZone zone) {
        this.zone = zone;
    }

    public ClientSession getCs() {
        return cs;
    }

    public void setCs(ClientSession cs) {
        this.cs = cs;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String temp = request.getParameter("id");
        if (temp != null) {
            this.thread = bbsThreadDAO.findById(temp.trim());
            thread.setReadCount(thread.getReadCount() + 1);
            bbsThreadDAO.update(thread);
            this.zone=this.thread.getZone();
            this.synDB();
            this.countPage();
        }
        this.checkCheat();
    }
    
    public String checkCheat() {
        //String result = "ZoneList?faces-redirect=true";
        boolean cheat = false;
        if (this.zone != null) {
            ClientSession cs = JsfHelper.getBean("clientSession");
            BbsUser bu = cs.getUsr();
            if (bu == null && zone.getCheckUser()) {
                cheat = true;
            }
            if (bu != null && zone.getCheckUser()) {
                if (!bu.testIfIn(zone.getGroupStr())) {
                    cheat = true;
                }
            }

            if (cheat) {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                try {
                    HttpServletRequest request1 = JsfHelper.getRequest();
                    String url = request1.getContextPath() + "/talk/ZoneList.jspx";
                    //MyLogger.echo(url);
                    response.sendRedirect(url);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    private void countPage() {
        int c = this.talks.size();
        int p = c / this.talkRows;
        int m = c % this.talkRows;
        if (m == 0) {
            this.pageCount = p;
        } else {
            this.pageCount = p + 1;
        }
    }

    private void synDB() {
        //this.talks = null;
        this.talks = this.bbsTalkDAO.findByThread(thread.getId());
    }

    public String addNewTalk() {
        this.addNewTalk(null);
        return null;
    }

    public void addNewTalk(AjaxBehaviorEvent event) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + this.scoreRegularDAO.findScoreRegular().getNewTalk());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("发表回复", this.scoreRegularDAO.findScoreRegular().getNewTalk());
        //this.userDAO.updateBbsUser(bu);
        this.newTalk.setGenTime(new Date());
        this.newTalk.setGenBy(bu);
        this.newTalk.setThread(this.thread);
        this.newTalk.setLastEditTime(new Date());
        this.newTalk.setIp(IpHelper.getRemoteAddr(request));
        bbsTalkDAO.save(this.newTalk);
        this.thread.setLatestTalk(newTalk);
        this.bbsThreadDAO.update(this.thread);
        this.talks.add(this.newTalk);
        this.newTalk = new BbsTalk();
        this.countPage();
    }

    public String toSomeone(String u) {
        this.newTalk.setContent("To " + u + " #&nbsp;<br/>");
        return null;
    }

    public String quote(String t) {
        t = HTMLCleaner.delHTMLTag(t);
        if (t.length() > 50) {
            t = t.substring(0, 49) + "...";
        }
        t = t.replaceAll("<p>", "");
        t = t.replaceAll("</p>", "");
        String tt = t + "<br/>--------------------<br/><br/>";
        this.newTalk.setContent(tt);
        MyLogger.echo("quote:" + tt);
        return null;
    }

    public String block(String id) {
        BbsTalk t = bbsTalkDAO.findById(id);
        t.setIfPub(false);
        this.bbsTalkDAO.update(t);
        for (BbsTalk s : this.talks) {
            if (s.getId().equals(t.getId())) {
                s.setIfPub(false);
                break;
            }
        }
        return null;
    }

    public String unblock(String id) {
        BbsTalk t = bbsTalkDAO.findById(id);
        t.setIfPub(true);
        this.bbsTalkDAO.update(t);
        for (BbsTalk s : this.talks) {
            if (s.getId().equals(t.getId())) {
                s.setIfPub(true);
                break;
            }
        }
        return null;
    }

    public String del(String id) {
        this.bbsTalkDAO.delete(id);
        this.synDB();
        return null;
    }

//    public void del(AjaxBehaviorEvent event) {
//
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String id = request.getParameter("tid");
//        // String id=event.getComponent().getAttributes().get("tid").toString();
//        long idt=Long.parseLong(id);
//        this.bbsTalkDAO.delete(idt);
//        MyLogger.echo(this.talks.size());
//        // this.synDB();
//    }
    public void edit(String id) {
        BbsTalk t = bbsTalkDAO.findById(id);
        this.setTalk_temp(t);
    }

    public String edit_ok() {
        this.talk_temp.setLastEditTime(new Date());
        this.bbsTalkDAO.update(this.talk_temp);

        this.setTalks(bbsTalkDAO.findByThread(this.thread.getId()));
        return null;
    }

    public String buyThread(String id) {
        BbsUser buyer = cs.getUsr();
        if (buyer == null) {
            JsfHelper.error("请先登录!");
            return null;
        }
        if (buyer.getScore() > thread.getScore()) {
            //购买者减积分，售出者加积分
            
            BbsUser seller = thread.getGenBy();
            

            BbsThreadTrade trade = new BbsThreadTrade();
            trade.setThread(thread);
            trade.setUser(cs.getUsr());
            this.bbsThreadTradeDAO.addThreadTrade(trade);
            JsfHelper.info("购买成功!");

            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("购买帖子", -thread.getScore());
            bsl.log("售出帖子：“" + thread.getTitle() + "”，购买者：" + buyer.getName() + "（" + buyer.getUsername() + "）", thread.getScore(), seller);
        } else {
            JsfHelper.error("您的积分不够!");
        }
        return null;
    }

    public String bookMark(String id) {
        //BbsThreadBookMark b = this.bbsThreadBookMarkDAO.findThreadBookMark(id);
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            List<BbsThreadBookMark> bs = this.bbsThreadBookMarkDAO.findThreadBookMarkByThread(id);
            if (bs.isEmpty()) {
                BbsThreadBookMark b = new BbsThreadBookMark();
                b.setThread(thread);
                b.setUser(bu);
                this.bbsThreadBookMarkDAO.addThreadBookMark(b);
                JsfHelper.info("收藏成功!");
            } else {
                JsfHelper.error("您已经收藏过本贴!");
            }
        } else {
            JsfHelper.error("请先登录!");
        }

        return null;
    }
}
