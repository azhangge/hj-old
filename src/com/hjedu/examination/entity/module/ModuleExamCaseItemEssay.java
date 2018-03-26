package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.examination.entity.EssayQuestion;
/**
 * 章节练习模块
 * 章节练习简单题记录
 * @author h j
 *
 */
@Entity
@Table(name = "m_exam_case_item_essay")
public class ModuleExamCaseItemEssay extends ModuleGeneralExamCaseItem implements Serializable , Comparable {

    private static final long serialVersionUID = 1L;
   
    @ManyToOne
    @JoinColumn(name = "question_id")
    private EssayQuestion question;
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ModuleExamCaseItemCase caseItem;
    @Lob
    @Column(name = "user_answer")
    private String userAnswer="";
    @Column(name = "right_ratio")
    private float rightRatio=0f;
    @Lob
    @Column(name = "right_answer")
    private String rightAnswer="";
    @Column(name = "marked")
    private boolean marked = false;
    

    public ModuleExamCaseItemEssay() {
    }

    public ModuleExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ModuleExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }


    public EssayQuestion getQuestion() {
        return question;
    }

    public void setQuestion(EssayQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public float getRightRatio() {
        //System.out.println("ratio:"+rightRatio);
        long rt=(long)(rightRatio*1000L);
        //System.out.println("now:"+rt);
        rightRatio=((float)rt)/1000.0F;
        //System.out.println("after:"+rightRatio);
        return rightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.rightRatio = rightRatio;
    }



    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    
    
    @Override
    public int compareTo(Object o) {
        ModuleExamCaseItemEssay cq = (ModuleExamCaseItemEssay) o;
        if (this.getQuestion() == null || cq.getQuestion() == null) {
            return 1;
        }
        return this.getQuestion().getId().compareTo(cq.getQuestion().getId());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModuleExamCaseItemEssay)) {
            return false;
        }
        ModuleExamCaseItemEssay other = (ModuleExamCaseItemEssay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemEssay[ id=" + id + " ]";
    }
}
