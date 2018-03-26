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

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.course.dao.IRereAskTalkDAO;
import com.hjedu.course.entity.RereAsk;
import com.hjedu.course.entity.RereAskTalk;
import com.hjedu.course.entity.RereAskZone;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AskDetail implements Serializable {

    RereAskTalk newTalk = new RereAskTalk();
    RereAskZone zone = null;
    RereAsk ask = new RereAsk();
    IRereAskDAO bbsThreadDAO = SpringHelper.getSpringBean("RereAskDAO");

    IRereAskTalkDAO bbsTalkDAO = SpringHelper.getSpringBean("RereAskTalkDAO");
    @ManagedProperty(value = "#{clientSession}")
    ClientSession cs;
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    int pageShow = 1;
    RereAskTalk talk_temp = new RereAskTalk();
    int pageCount = 0;
    int talkRows = 10;
    List<RereAskTalk> talks = new ArrayList();

    public int getPageShow() {
        return pageShow;
    }

    public void setPageShow(int pageShow) {
        this.pageShow = pageShow;
    }

    public RereAskTalk getNewTalk() {
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

    public void setNewTalk(RereAskTalk newTalk) {
        this.newTalk = newTalk;
    }

    public RereAsk getThread() {
        return ask;
    }

    public void setThread(RereAsk ask) {
        this.ask = ask;
    }

    public RereAskTalk getTalk_temp() {
        return talk_temp;
    }

    public void setTalk_temp(RereAskTalk talk_temp) {
        this.talk_temp = talk_temp;
    }

    public List<RereAskTalk> getTalks() {
        return talks;
    }

    public void setTalks(List<RereAskTalk> talks) {
        this.talks = talks;
    }

    public RereAskZone getZone() {
        return zone;
    }

    public void setZone(RereAskZone zone) {
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
            this.ask = bbsThreadDAO.findById(temp.trim());
            ask.setReadCount(ask.getReadCount() + 1);
            bbsThreadDAO.update(ask);
            this.zone = this.ask.getZone();
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
        this.talks = this.bbsTalkDAO.findByAsk(ask.getId());
    }

    public String addNewTalk() {
        this.addNewTalk(null);
        return null;
    }

    public void addNewTalk(AjaxBehaviorEvent event) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        BbsUser bu = cs.getUsr();
        
        //this.userDAO.updateBbsUser(bu);
        this.newTalk.setGenTime(new Date());
        this.newTalk.setGenBy(bu);
        this.newTalk.setAsk(this.ask);
        this.newTalk.setLastEditTime(new Date());
        this.newTalk.setIp(IpHelper.getRemoteAddr(request));
        bbsTalkDAO.save(this.newTalk);
        this.ask.setLatestTalk(newTalk);
        this.bbsThreadDAO.update(this.ask);
        this.talks.add(this.newTalk);
        this.newTalk = new RereAskTalk();
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
        RereAskTalk t = bbsTalkDAO.findById(id);
        t.setIfPub(false);
        this.bbsTalkDAO.update(t);
        for (RereAskTalk s : this.talks) {
            if (s.getId().equals(t.getId())) {
                s.setIfPub(false);
                break;
            }
        }
        return null;
    }

    public String unblock(String id) {
        RereAskTalk t = bbsTalkDAO.findById(id);
        t.setIfPub(true);
        this.bbsTalkDAO.update(t);
        for (RereAskTalk s : this.talks) {
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


    public void edit(String id) {
        RereAskTalk t = bbsTalkDAO.findById(id);
        this.setTalk_temp(t);
    }

    public String edit_ok() {
        this.talk_temp.setLastEditTime(new Date());
        this.bbsTalkDAO.update(this.talk_temp);

        this.setTalks(bbsTalkDAO.findByAsk(this.ask.getId()));
        return null;
    }

    public String acceptTalk(String id) {
        BbsUser buyer = cs.getUsr();
        BbsUser seller;
        if (buyer == null) {
            JsfHelper.error("请先登录!");
            return null;
        }
        RereAskTalk talk = this.bbsTalkDAO.findById(id);
        if (talk != null) {
            
            seller = talk.getGenBy();
            JsfHelper.info("采纳成功!");

            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            //bsl.log("购买帖子", -ask.getScore());
            //被采纳者加积分
            bsl.log("对问答：“" + ask.getTitle() + "”的回答被采纳，采纳者：" + buyer.getName() + "（" + buyer.getUsername() + "）", ask.getScore(), seller);
        }
        return null;
    }

    public String bookMark(String id) {
        //RereAskBookMark b = this.bbsThreadBookMarkDAO.findThreadBookMark(id);
        BbsUser bu = cs.getUsr();
        if (bu != null) {
//            List<RereAskBookMark> bs = this.bbsThreadBookMarkDAO.findThreadBookMarkByThread(id);
//            if (bs.isEmpty()) {
//                RereAskBookMark b = new RereAskBookMark();
//                b.setThread(ask);
//                b.setUser(bu);
//                this.bbsThreadBookMarkDAO.addThreadBookMark(b);
//                JsfHelper.info("收藏成功!");
//            } else {
//                JsfHelper.error("您已经收藏过本贴!");
//            }
        } else {
            JsfHelper.error("请先登录!");
        }

        return null;
    }
}
