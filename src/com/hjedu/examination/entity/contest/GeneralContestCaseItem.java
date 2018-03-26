package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.huajie.util.SpringHelper;

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

/**
 * 竞赛模块
 * 竞赛父类
 * @author h j
 *
 */
@Entity
@Table(name = "general_contest_case_item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GeneralContestCaseItem implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    String id = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "case_id")
    ContestCase examCase;

    @Column(name = "part_id")
    @Expose
    private String partId;
    
    @Column(name = "index2")
    int index=0;

    @Transient
    private Random2PaperPart part;

    @Transient
    boolean done = false;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContestCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ContestCase examCase) {
        this.examCase = examCase;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Random2PaperPart getPart() {
        if (partId != null) {
            IRandom2PaperPartDAO pDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
            part = pDAO.findRandom2PaperPart(partId);
        }
        return part;
    }

    public void setPart(Random2PaperPart part) {
        this.part = part;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    

}
