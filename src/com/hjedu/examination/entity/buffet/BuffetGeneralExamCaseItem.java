package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
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
 * 练习模块
 * 练习父类
 * @author h j
 *
 */
@Entity
@Table(name = "b_general_exam_case_item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BuffetGeneralExamCaseItem implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    String id = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "case_id")
    BuffetExamCase examCase;

    @Transient
            @Expose
    boolean done = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BuffetExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(BuffetExamCase examCase) {
        this.examCase = examCase;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
