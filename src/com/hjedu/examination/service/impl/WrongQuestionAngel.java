package com.hjedu.examination.service.impl;

import java.io.Serializable;
import org.aspectj.lang.JoinPoint;

import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IWrongExamCaseService;
import com.huajie.util.SpringHelper;

public class WrongQuestionAngel implements Serializable {

    

    /*在考试结束提交后计算试卷中的错题
     */
    public void checkAndSaveWrongQuestion(JoinPoint jp, ExamCase ec) {
        IWrongExamCaseService service=SpringHelper.getSpringBean("WrongExamCaseService");
        boolean addWrong = false;
        try {
            addWrong = ec.getExamination().isAddWrong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!addWrong) {
            return;
        }
        //检查错题
        service.checkWrongItems(ec);

    }
}
