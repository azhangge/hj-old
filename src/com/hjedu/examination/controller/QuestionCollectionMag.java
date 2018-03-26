/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IQuestionCollectionDAO;
import com.hjedu.examination.entity.QuestionCollection;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class QuestionCollectionMag {

    public void collect(String qid, String qtype) {
        IQuestionCollectionDAO selectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                QuestionCollection qc = new QuestionCollection();
                qc.setBbsUser(bu);
                qc.setQid(qid);
                qc.setQtype(qtype);
                selectDAO.addQuestionCollection(qc);
            }
        }
        JsfHelper.info("试题收藏成功！");
    }

    public void cancel(String qid) {
        IQuestionCollectionDAO selectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                QuestionCollection qc = selectDAO.findQuestionCollectionByQandU(qid, bu.getId());
                if (qc != null) {
                    selectDAO.deleteQuestionCollection(qc.getId());
                }
            }
        }
        JsfHelper.info("试题取消收藏成功！");
    }

    public void collectOrCancel(String qid, String qtype) {
        IQuestionCollectionDAO selectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                QuestionCollection qc = selectDAO.findQuestionCollectionByQandU(qid, bu.getId());
                if (qc != null) {
                    selectDAO.deleteQuestionCollection(qc.getId());
                    JsfHelper.info("试题取消收藏成功！");
                } else {
                    qc = new QuestionCollection();
                    qc.setBbsUser(bu);
                    qc.setQid(qid);
                    qc.setQtype(qtype);
                    selectDAO.addQuestionCollection(qc);
                    JsfHelper.info("试题收藏成功！");
                }
            }
        }
    }

}
