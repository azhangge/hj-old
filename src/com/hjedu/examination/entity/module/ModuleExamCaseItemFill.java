package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.huajie.util.SpringHelper;
/**
 * 章节练习模块
 * 章节练习填空题记录
 * @author h j
 *
 */
@Entity
@Table(name = "m_exam_case_item_fill")
public class ModuleExamCaseItemFill extends ModuleGeneralExamCaseItem implements Serializable , Comparable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ModuleExamCaseItemCase caseItem;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillQuestion question;
    @Column(name = "user_answer_str")
    private String userAnswerStr;
    @Column(name = "right_answer_str")
    private String rightAnswerStr;
    @Column(name = "right_num")
    private int rightNum;
    @Transient
    private int totalNum=0;
    @Transient
    private List<ModuleExamCaseItemFillBlock> blocks = null;
    @Transient
    private String lastStr;
    @Column(name = "marked")
    private boolean marked = false;
    

    public ModuleExamCaseItemFill() {
    }

    public FillQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FillQuestion question) {
        this.question = question;
    }

    public String getRightAnswerStr() {
        return rightAnswerStr;
    }

    public void setRightAnswerStr(String rightAnswerStr) {
        this.rightAnswerStr = rightAnswerStr;
    }

    public ModuleExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ModuleExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public int getRightNum() {
        return rightNum;
    }

    public void setRightNum(int rightNum) {
        this.rightNum = rightNum;
    }

    public String getUserAnswerStr() {
        return userAnswerStr;
    }

    public void setUserAnswerStr(String userAnswerStr) {
        this.userAnswerStr = userAnswerStr;
    }

    public List<ModuleExamCaseItemFillBlock> getBlocks() {
        if (this.blocks == null) {
            IModuleExamCaseService ecs = SpringHelper.getSpringBean("ModuleExamCaseService");
            ecs.buildItemFillBlocks(this);
        }
        return blocks;
    }

    public void setBlocks(List<ModuleExamCaseItemFillBlock> blocks) {
        this.blocks = blocks;
    }

    public String getLastStr() {
        return lastStr;
    }

    public void setLastStr(String lastStr) {
        this.lastStr = lastStr;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getTotalNum() {
        if (this.blocks != null) {
            totalNum = this.blocks.size();
        }
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public int compareTo(Object o) {
        ModuleExamCaseItemFill cq = (ModuleExamCaseItemFill) o;
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
        if (!(object instanceof ModuleExamCaseItemFill)) {
            return false;
        }
        ModuleExamCaseItemFill other = (ModuleExamCaseItemFill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemFill[ id=" + id + " ]";
    }
}
