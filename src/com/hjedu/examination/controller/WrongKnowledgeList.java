package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.WrongKnowledge;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class WrongKnowledgeList implements Serializable {

    ClientSession cs = JsfHelper.getBean("clientSession");
    IWrongKnowledgeDAO wrongDAO = SpringHelper.getSpringBean("WrongKnowledgeDAO");
    IExamKnowledgeDAO knowDAO = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    List<WrongKnowledge> knows;
    ExamKnowledge nowCq;

    public List<WrongKnowledge> getKnows() {
        return knows;
    }

    public void setKnows(List<WrongKnowledge> knows) {
        this.knows = knows;
    }

    public ExamKnowledge getNowCq() {
        return nowCq;
    }

    public void setNowCq(ExamKnowledge nowCq) {
        this.nowCq = nowCq;
    }

   

    @PostConstruct
    public void init() {
        this.synDB();
    }

    private void synDB() {
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            this.knows = this.wrongDAO.findWrongKnowledgeByUsr(bu.getId());
        }
    }

    public String delKnowledge(String id) {
        this.wrongDAO.deleteWrongKnowledge(id);
        this.synDB();
        return null;
    }

    public String selectKnowledge(String id) {
        this.nowCq = this.knowDAO.findExamKnowledge(id);
        return null;
    }
}
