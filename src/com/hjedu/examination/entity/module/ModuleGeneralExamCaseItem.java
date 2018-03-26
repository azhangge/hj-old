package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hjedu.examination.dao.IModuleExamPartDAO;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.huajie.util.SpringHelper;
/**
 * 章节练习模块
 * 章节练习父类
 * @author h j
 *
 */
@Entity
@Table(name = "module_general_exam_case_item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ModuleGeneralExamCaseItem implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    String id = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "case_id")
    ModuleExamCase examCase;
    
    @Column(name = "part_id")
    private String partId;
    
    @Column(name = "done")
    boolean done = false;
    
    @Column(name = "if_right")
    boolean ifRight;

    @Transient
    private ModuleExam2Part part;
    
    
    //在整张试卷内的标号
    @Column(name = "index2")
    int index=0;
    
    @Transient
    String partName;
    @Transient
    String qtype="choice";
    @Transient
    String caseType="";
    @Transient
    double score=1.0D;

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModuleExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        //System.out.println("done set");
        this.done = done;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Boolean getIfRight() {
        return ifRight;
    }

    public void setIfRight(Boolean ifRight) {
        this.ifRight = ifRight;
    }

    public ModuleExam2Part getPart() {
        if (partId != null) {
            IModuleExamPartDAO pDAO = SpringHelper.getSpringBean("ModuleExamPartDAO");
            part = pDAO.findModuleExamPart(partId);
        }
        return part;
    }

    public void setPart(ModuleExam2Part part) {
        this.part = part;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    
    
}
