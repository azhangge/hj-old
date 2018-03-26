package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.List;

import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.MD5;

public class HashCodeService implements Serializable {

    public String fetchHashCode(ChoiceQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        sb.append(cq.getName());
        sb.append(cq.getAnswer());
        List<ExamChoice> cs = cq.getChoices();
        for (ExamChoice e : cs) {
            sb.append(e.getName());
        }
        String result = MD5.bit32(sb.toString());
        //System.out.println(sb.toString());
        //System.out.println(result);
        return result;
    }

    public String fetchHashCode(MultiChoiceQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        sb.append(cq.getName());
        sb.append(cq.getAnswer());
        List<ExamMultiChoice> cs = cq.getChoices();
        for (ExamMultiChoice e : cs) {
            sb.append(e.getName());
        }
        String result = MD5.bit32(sb.toString());
        return result;
    }

    public String fetchHashCode(FillQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        String name = cq.getName();
        sb.append(name);
        String result = MD5.bit32(sb.toString());
        return result;
    }

    public String fetchHashCode(JudgeQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        String name = cq.getName();
        sb.append(name);
        String result = MD5.bit32(sb.toString());
        return result;
    }

    public String fetchHashCode(EssayQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        String name = cq.getName();
        sb.append(name);
        String result = MD5.bit32(sb.toString());
        return result;
    }

    public String fetchHashCode(FileQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        String name = cq.getName();
        sb.append(name);
        String result = MD5.bit32(sb.toString());
        return result;
    }
    
    public String fetchHashCode(CaseQuestion cq) {
        StringBuilder sb = new StringBuilder();
        ExamModuleModel em = cq.getModule();
        if (em != null) {
            sb.append(cq.getModule().getId());
        }
        String name = cq.getName();
        sb.append(name);
        String content=cq.getContent();
        sb.append(content);
        String innerName=cq.getInnerName();
        sb.append(innerName);
        String nickName=cq.getNickName();
        sb.append(nickName);
        String result = MD5.bit32(sb.toString());
        return result;
    }
    
}
