package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie
 */
public class ExamCaseFacet implements Serializable{

    private String id;
    private String name;
    private BbsUser user;
    private Examination examination;
    private Date genTime;
    private Date submitTime;
    private double score;
    private boolean ifPub;
    private String ip;
    private long timePassed;
    private String stat;
    private int ranking;
    private double totalFullScore = 0D;
    private long bbsScore;
    private boolean ifSimulate;
    //以下字段无需从实体ExamCase中取，由运行时动态加载
    private String ipAddr;
    private boolean selected;
    private ExamRoom room;
    /**
     * 是否通过
     */
    private boolean ifPassed;

	public ExamCaseFacet(String id, String name, BbsUser user, Examination examination, Date genTime, Date submitTime, double score, boolean ifPub, String ip, long timePassed, String stat, int ranking,double totalFullScore,long bbsScore,boolean ifSimulate,boolean ifPassed) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.examination = examination;
        this.genTime = genTime;
        this.submitTime = submitTime;
        this.score = score;
        this.ifPub = ifPub;
        this.ip = ip;
        this.timePassed = timePassed;
        this.stat = stat;
        this.ranking = ranking;
        this.totalFullScore = totalFullScore;
        this.bbsScore=bbsScore;
        this.ifSimulate=ifSimulate;
        this.ifPassed=ifPassed;
    }
    
    public ExamCaseFacet(ExamCase ec) {
        this.id = ec.getId();
        this.name = ec.getName();
        this.user = ec.getUser();
        this.examination = ec.getExamination();
        this.genTime = ec.getGenTime();
        this.submitTime = ec.getSubmitTime();
        this.score = ec.getScore();
        this.ifPub = ec.isIfPub();
        this.ip = ec.getIp();
        this.timePassed = ec.getTimePassed();
        this.stat = ec.getStat();
        this.ranking = ec.getRanking();
        this.totalFullScore = ec.getTotalFullScore();
        this.bbsScore=ec.getBbsScore();
        this.ifSimulate=ec.isIfSimulate();
        this.ifPassed=ec.isIfPassed();
    }
    
    public boolean isIfPassed() {
		return ifPassed;
	}

	public void setIfPassed(boolean ifPassed) {
		this.ifPassed = ifPassed;
	}
    
    public boolean isIfSimulate() {
		return ifSimulate;
	}

	public void setIfSimulate(boolean ifSimulate) {
		this.ifSimulate = ifSimulate;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBbsScore() {
        return bbsScore;
    }

    public void setBbsScore(long bbsScore) {
        this.bbsScore = bbsScore;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public double getTotalFullScore() {
        return totalFullScore;
    }

    public void setTotalFullScore(double totalFullScore) {
        this.totalFullScore = totalFullScore;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isIfPub() {
        return ifPub;
    }

    public void setIfPub(boolean ifPub) {
        this.ifPub = ifPub;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(long timePassed) {
        this.timePassed = timePassed;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

     public ExamRoom getRoom() {
        IExamCaseService s = SpringHelper.getSpringBean("ExamCaseService");
        room = s.confirmExamRoom(ip);
        return room;
    }

    public void setRoom(ExamRoom room) {
        this.room = room;
    }
    
    public String getIpAddr() {
        if (this.ip != null) {
            IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
            ipAddr = ips.seek(ip);
        }
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
    
}
