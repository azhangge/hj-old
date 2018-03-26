
package com.hjedu.examination.entity;

import java.util.UUID;

import com.hjedu.customer.entity.BbsUser;

/**
 *
 * @author huajie
 */
public class AbsentModel {
    private String id=UUID.randomUUID().toString();
    private BbsUser user;
    private Examination exam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }
    
    
    
}
