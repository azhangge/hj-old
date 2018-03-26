package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.lazy.LazyModuleExamCaseList;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListModuleExamCase implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    LazyModuleExamCaseList lcqs;
    //List<ModuleExamCase> cases;
    //List<ModuleExamCase> cases2 = new ArrayList();
    //以下属性用于绑定页面计算部门或考试的平均分

    boolean selectAll = false;

//    public List<ModuleExamCase> getCases() {
//        return cases;
//    }
//
//    public void setCases(List<ModuleExamCase> cases) {
//        this.cases = cases;
//    }
    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public LazyModuleExamCaseList getLcqs() {
        return lcqs;
    }

    public void setLcqs(LazyModuleExamCaseList lcqs) {
        this.lcqs = lcqs;
    }

//    public List<ModuleExamCase> getCases2() {
//        return cases2;
//    }
//
//    public void setCases2(List<ModuleExamCase> cases2) {
//        this.cases2 = cases2;
//    }
    @PostConstruct
    public void init() {
        //this.synDB();
        this.buildLazy();
    }

//    public void synDB() {
//        try {
//            this.cases = this.examCaseDAO.findAllModuleExamCase();
//            this.buildCase2s();
//        } catch (Exception e) {
//        }
//    }
    public void buildLazy() {
        if (lcqs == null) {
            this.lcqs = new LazyModuleExamCaseList();

        }
    }

//    public void buildCase2s() {
//        this.cases2.clear();
//        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
//        AdminInfo ai = asb.getAdmin();
//        if (ai != null) {
//            List<Examination> exs = ai.getExams();
//            if (exs != null) {
//                for (ModuleExamCase ec : this.cases) {
//                    for (Examination ex : exs) {
//                        if (ex.getId().equals(ec.getExamination().getId())) {
//                            this.cases2.add(ec);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
    public void deleteReport(String id) {
        ModuleExamCase ec = this.examCaseDAO.findModuleExamCase(id);
        this.logger.log("删除了章节练习成绩，" + "，用户为：" + ec.getUser().getUsername());
        this.examCaseDAO.deleteModuleExamCase(id);
        //this.synDB();
    }

    public String selectAllOrNot() {
        List<ModuleExamCase> ccs = this.lcqs.getModels();
        for (ModuleExamCase ec : ccs) {
            if (this.isSelectAll()) {
                ec.setSelected(true);
            } else {
                ec.setSelected(false);
            }
        }
        return "";
    }

    public String batchDelete() {
        List<ModuleExamCase> ccs = this.lcqs.getModels();
        for (ModuleExamCase ec : ccs) {
            if (ec.isSelected()) {
                try {
                    String nname = ec.getUser() == null ? "null" : ec.getUser().getUsername();
                    this.logger.log("删除了章节练习成绩:" + ec.getName() + "，用户为：" + nname);
                    this.examCaseDAO.deleteModuleExamCase(ec.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        //this.synDB();
        return null;
    }

    public String deleteAll() {
        try {
            this.logger.log("清空了所有考试成绩。");
            //this.examCaseDAO.deleteAllModuleExamCase();
            List<ModuleExamCase> ccs = this.examCaseDAO.findAllModuleExamCase();
            for (ModuleExamCase cc : ccs) {
                this.examCaseDAO.deleteModuleExamCase(cc.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //this.synDB();
        return null;
    }

}
