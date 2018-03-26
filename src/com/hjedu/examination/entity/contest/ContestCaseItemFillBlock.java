package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.UUID;


public class ContestCaseItemFillBlock  implements Serializable{
    @Expose
    private String id = UUID.randomUUID().toString();
    @Expose
    private String preStr="";
    @Expose
    private String rightAnswer="";
    @Expose
    private String userAnswer="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    public String getPreStr() {
        return preStr;
    }

    public void setPreStr(String preStr) {
        this.preStr = preStr;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
    
    
    
}
