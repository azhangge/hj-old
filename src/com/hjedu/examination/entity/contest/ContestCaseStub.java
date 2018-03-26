
package com.hjedu.examination.entity.contest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.random2.ModuleRandom2Part;

/**
 *
 * @author huajie.com
 * 此类为ExamContest保存试题存根，以在随机试卷情况下仍能让所有考生题目一样，但不同的周期题目不一样
 */
public class ContestCaseStub implements Serializable{
    
    private String id=UUID.randomUUID().toString();
    //保存ModuleRandom2Part
    private List<ModuleRandom2Part> mps=Collections.synchronizedList(new ArrayList());
    //在ModuleRandom2Part和ChoiceQuestion列表之前建立列表关系
    private Map<String,List<ChoiceQuestion>> choices=Collections.synchronizedMap(new HashMap());
    private Map<String,List<MultiChoiceQuestion>> mchoices=Collections.synchronizedMap(new HashMap());
    private Map<String,List<FillQuestion>> fills=Collections.synchronizedMap(new HashMap());
    private Map<String,List<JudgeQuestion>> judges=Collections.synchronizedMap(new HashMap());
    private Map<String,List<EssayQuestion>> essaies=Collections.synchronizedMap(new HashMap());
    private Map<String,List<FileQuestion>> files=Collections.synchronizedMap(new HashMap());
    private Map<String,List<CaseQuestion>> cases=Collections.synchronizedMap(new HashMap());

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ModuleRandom2Part> getMps() {
        return mps;
    }

    public void setMps(List<ModuleRandom2Part> mps) {
        this.mps = mps;
    }

    public Map<String, List<ChoiceQuestion>> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, List<ChoiceQuestion>> choices) {
        this.choices = choices;
    }

    public Map<String, List<MultiChoiceQuestion>> getMchoices() {
        return mchoices;
    }

    public void setMchoices(Map<String, List<MultiChoiceQuestion>> mchoices) {
        this.mchoices = mchoices;
    }

    public Map<String, List<FillQuestion>> getFills() {
        return fills;
    }

    public void setFills(Map<String, List<FillQuestion>> fills) {
        this.fills = fills;
    }

    public Map<String, List<JudgeQuestion>> getJudges() {
        return judges;
    }

    public void setJudges(Map<String, List<JudgeQuestion>> judges) {
        this.judges = judges;
    }

    public Map<String, List<EssayQuestion>> getEssaies() {
        return essaies;
    }

    public void setEssaies(Map<String, List<EssayQuestion>> essaies) {
        this.essaies = essaies;
    }

    public Map<String, List<FileQuestion>> getFiles() {
        return files;
    }

    public void setFiles(Map<String, List<FileQuestion>> files) {
        this.files = files;
    }

    public Map<String, List<CaseQuestion>> getCases() {
        return cases;
    }

    public void setCases(Map<String, List<CaseQuestion>> cases) {
        this.cases = cases;
    }
    
    
    
    
}
