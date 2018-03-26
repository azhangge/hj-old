package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module.ModuleExamInfo;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class PreModuleExamCase implements Serializable {

    
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IModuleExamInfoDAO examInfoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
    //IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    ClientSession cs = JsfHelper.getBean("clientSession");
    ModuleExamInfo examInfo;
    ExamModuleModel module;
    int choiceNum=10;
    int multiChoiceNum=10;
    int fillNum=10;
    int judgeNum=10;
    int essayNum=2;
    int fileNum=2;
    int caseNum=1;

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }


    public ModuleExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ModuleExamInfo examInfo) {
        this.examInfo = examInfo;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public int getMultiChoiceNum() {
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(int multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
    }

    public int getFillNum() {
        return fillNum;
    }

    public void setFillNum(int fillNum) {
        this.fillNum = fillNum;
    }

    public int getJudgeNum() {
        return judgeNum;
    }

    public void setJudgeNum(int judgeNum) {
        this.judgeNum = judgeNum;
    }

    public int getEssayNum() {
        return essayNum;
    }

    public void setEssayNum(int essayNum) {
        this.essayNum = essayNum;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
    }


    @PostConstruct
    public void init() {
        this.examInfo=this.examInfoDAO.findModuleExamInfo();
        HttpServletRequest request = JsfHelper.getRequest();
        String module_id = request.getParameter("module_id");
        //String id = module_id;
        if (module_id != null) {
            this.module=this.moduleDAO.findExamModuleModel(module_id);
        }

    }
    
    public String begainTest(){
    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ModuleExamCase.jspx?module_id=" + this.module.getId()+"&choice_num="+choiceNum+"&multi_choice_num="+multiChoiceNum+"&fill_num="+fillNum+"&judge_num="+judgeNum+"&essay_num="+essayNum+"&file_num="+fileNum+"&case_num="+caseNum;
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
    
    return null;
    }


}
