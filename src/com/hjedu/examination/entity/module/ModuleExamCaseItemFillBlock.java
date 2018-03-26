package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.UUID;


public class ModuleExamCaseItemFillBlock  implements Serializable{
    private String id = UUID.randomUUID().toString();
    private String preStr;
    private String rightAnswer;
    private String userAnswer;

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
