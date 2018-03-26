package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListContestCase implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    
    List<ContestCase> cases;
    //List<ModuleExamCase> cases2 = new ArrayList();
    //以下属性用于绑定页面计算部门或考试的平均分
    
    boolean selectAll = false;
    
    public List<ContestCase> getCases() {
        return cases;
    }
    
    public void setCases(List<ContestCase> cases) {
        this.cases = cases;
    }
    
    public boolean isSelectAll() {
        return selectAll;
    }
    
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    
    @PostConstruct
    public void init() {
        this.synDB();
        //this.buildLazy();
    }
    
    public void synDB() {
        try {
            this.cases = this.examCaseDAO.findAllContestCase();
            
        } catch (Exception e) {
        }
    }
    
    public void deleteReport(String id) {
        ContestCase ec = this.examCaseDAO.findContestCase(id);
        this.logger.log("删除了竞赛成绩，" + "，用户为：" + ec.getUser().getUsername());
        this.examCaseDAO.deleteContestCase(id);
        this.synDB();
    }
    
    public String selectAllOrNot() {
        for (ContestCase ec : this.cases) {
            if (this.isSelectAll()) {
                ec.setSelected(true);
            } else {
                ec.setSelected(false);
            }
        }
        return "";
    }
    
    public String batchDelete() {
        for (ContestCase ec : this.cases) {
            if (ec.isSelected()) {
                try {
                    String nname = ec.getUser() == null ? "null" : ec.getUser().getUsername();
                    this.logger.log("删除了竞赛成绩:" + ec.getName() + "，用户为：" + nname);
                    this.examCaseDAO.deleteContestCase(ec.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        this.synDB();
        return null;
    }
    
    public String deleteAll() {
        try {
            this.logger.log("清空了所有竞赛成绩。");
            //this.examCaseDAO.deleteAllContestCase();
            List<ContestCase> ccs = this.examCaseDAO.findAllContestCase();
            for (ContestCase cc : ccs) {
                this.examCaseDAO.deleteContestCase(cc.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.synDB();
        return null;
    }
    
}
