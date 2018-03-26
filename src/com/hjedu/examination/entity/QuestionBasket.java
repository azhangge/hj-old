package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class QuestionBasket implements Serializable{
    
    String id=UUID.randomUUID().toString();
    List<ChoiceQuestion> choices=new LinkedList();
    List<MultiChoiceQuestion> multiChoices=new LinkedList();
    List<FillQuestion> fills=new LinkedList();
    List<JudgeQuestion> judges=new LinkedList();
    List<EssayQuestion> essaies=new LinkedList();
    List<FileQuestion> files=new LinkedList();
    List<CaseQuestion> cases=new LinkedList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMultiChoices() {
        return multiChoices;
    }

    public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public List<FillQuestion> getFills() {
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public List<FileQuestion> getFiles() {
        return files;
    }

    public void setFiles(List<FileQuestion> files) {
        this.files = files;
    }

    public List<CaseQuestion> getCases() {
        return cases;
    }

    public void setCases(List<CaseQuestion> cases) {
        this.cases = cases;
    }

   
    
    
    
    
}
