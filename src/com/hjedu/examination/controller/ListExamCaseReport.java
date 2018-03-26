package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;
import com.hjedu.examination.entity.lazy.LazyExamCaseList;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamCaseReport implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
    LazyExamCaseList lcqs;
    //List<ExamCase> cases;
    //List<ExamCase> cases2 = new ArrayList();
    //以下属性用于绑定页面计算部门或考试的平均分
    String departmentId;
    String examinationId;
    boolean selectAll = false;

//    public List<ExamCase> getCases() {
//        return cases;
//    }
//
//    public void setCases(List<ExamCase> cases) {
//        this.cases = cases;
//    }
    public boolean isSelectAll() {
        return selectAll;
    }
    
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    
    public LazyExamCaseList getLcqs() {
        return lcqs;
    }
    
    public void setLcqs(LazyExamCaseList lcqs) {
        this.lcqs = lcqs;
    }
    
    public String getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getExaminationId() {
        return examinationId;
    }
    
    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

//    public List<ExamCase> getCases2() {
//        return cases2;
//    }
//
//    public void setCases2(List<ExamCase> cases2) {
//        this.cases2 = cases2;
//    }
    @PostConstruct
    public void init() {
        //this.synDB();
        this.buildLazy();
    }

//    public void synDB() {
//        try {
//            this.cases = this.examCaseDAO.findAllExamCase();
//            this.buildCase2s();
//        } catch (Exception e) {
//        }
//    }
    public void buildLazy() {
        if (lcqs == null) {
                AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
                if (asb != null) {
                    AdminInfo ai = asb.getAdmin();
                    if (ai != null) {
                        this.lcqs = new LazyExamCaseList(ai);
                    }
                
            }
        }
    }

//    public void buildCase2s() {
//        this.cases2.clear();
//        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
//        AdminInfo ai = asb.getAdmin();
//        if (ai != null) {
//            List<Examination> exs = ai.getExams();
//            if (exs != null) {
//                for (ExamCase ec : this.cases) {
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
    /**
     * 删除一个考试成绩
     *
     * @param id 成绩ID
     */
    public void deleteReport(String id) {
        ExamCase ec = this.examCaseDAO.findExamCase(id);
        this.logger.log("删除了考试成绩，考试为:" + ec.getExamination().getName() + "，用户为：" + ec.getUser().getUsername());
        this.examCaseDAO.deleteExamCase(id);
        //this.synDB();
    }

    /**
     * 选中所有或取消选中所有，由AJAX事件触发 新版本中选中事件由JS触发，不与后台交互，此方法已过期
     *
     * @return
     */
    @Deprecated
    public String selectAllOrNot() {
        List<ExamCaseFacet> ccs = this.lcqs.getModels();
        for (ExamCaseFacet ec : ccs) {
            if (this.isSelectAll()) {
                ec.setSelected(true);
            } else {
                ec.setSelected(false);
            }
        }
        return "";
    }

    /**
     * 批量删除考试成绩
     *
     * @return
     */
    public String batchDelete() {
        List<ExamCaseFacet> ccs = this.lcqs.getModels();
        List<ExamCaseFacet> targets = new LinkedList();
        this.logger.log("批量删除了考试成绩。");
        for (ExamCaseFacet ec : ccs) {
            if (ec.isSelected()) {
                targets.add(ec);
            }
        }
        this.examCaseDAO.deleteBulkExamCaseFacet(targets);
        //this.synDB();
        this.buildLazy();
        return null;
    }

    /**
     * 批量发布考试成绩
     *
     * @return
     */
    public String batchPub() {
        List<ExamCaseFacet> ccs = this.lcqs.getModels();
        List<String> targets = new LinkedList();
        this.logger.log("批量发布了考试成绩。");
        for (ExamCaseFacet ec : ccs) {
            if (ec.isSelected()) {
                ec.setIfPub(true);
                targets.add(ec.getId());
            }
        }
        final List<String> ts2 = targets;
        new Thread(new Runnable() {
            public void run() {
                cacheService.publishExamCases(ts2);
            }
        }).start();
        this.examCaseDAO.publishExamCases(targets);
        JsfHelper.info("批量发布成绩完成！");
        return null;
    }

    /**
     * 发布全部考试成绩
     *
     * @return
     */
    public String pubAll() {
        List<ExamCase> ccs = examCaseDAO.findAllUnpubExamCase();
        this.logger.log("发布了全部考试成绩。");
        List<String> targets = new LinkedList();
        for (ExamCase ec : ccs) {
            if (!ec.isIfPub()) {
                ec.setIfPub(true);
                targets.add(ec.getId());
            }
        }
        final List<String> ts2 = targets;
        new Thread(new Runnable() {
            public void run() {
                cacheService.publishExamCases(ts2);
            }
        }).start();
        this.examCaseDAO.publishExamCases(ts2);
        JsfHelper.info("发布全部成绩完成！");
        return null;
    }

    /**
     * 清除所有成绩
     *
     * @return
     */
    public String deleteAll() {
        try {
            this.logger.log("清空了所有考试成绩。");
            //启动新线程进行删除操作，可能耗较长时间
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    examCaseDAO.deleteAllExamCase();
                }
            };
            new Thread(runner).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //this.synDB();
        JsfHelper.info("清除任务已经添加，系统正在处理，请稍候前来查看结果，消耗时间视成绩总数而定");
        return null;
    }
    
}
