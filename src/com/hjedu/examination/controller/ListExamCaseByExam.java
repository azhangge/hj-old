package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamCaseByExam implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
    Examination exam;
    List<ExamCaseFacet> cases;
    //以下属性用于绑定页面计算部门或考试的平均分
    String departmentId;
    String examinationId;
    boolean selectAll = false;
    boolean ifSingle = true;//是否每人仅显示最高分
    boolean ifPubAll = false;

    public boolean isIfPubAll() {
		return ifPubAll;
	}

	public void setIfPubAll(boolean ifPubAll) {
		this.ifPubAll = ifPubAll;
	}

	public List<ExamCaseFacet> getCases() {
        return cases;
    }
    
    public void setCases(List<ExamCaseFacet> cases) {
        this.cases = cases;
    }
    
    public Examination getExam() {
        return exam;
    }
    
    public void setExam(Examination exam) {
        this.exam = exam;
    }
    
    public boolean isIfSingle() {
        return ifSingle;
    }
    
    public void setIfSingle(boolean ifSingle) {
        this.ifSingle = ifSingle;
    }
    
    public boolean isSelectAll() {
        return selectAll;
    }
    
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
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
    
    @PostConstruct
    public void init() {
        this.examinationId = JsfHelper.getRequest().getParameter("id");
        if(examinationId!=null){
        	this.exam = this.examDAO.findExamination(examinationId);
        }
        this.synDB();
    }
    
    public void synDB() {
        try {
            this.buildCases();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void buildCases() {
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        AdminInfo ai = asb.getAdmin();
        if (this.ifSingle) {
            this.cases = this.examCaseDAO.findSingleExamCaseForEachUserByExamAndAdmin(examinationId, ai, 0, 10000);
        } else {
            this.cases = this.examCaseDAO.findExamCaseByExamAndAdmin(examinationId, ai, 0, 10000);
        }
    }

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
        List<ExamCaseFacet> ccs = this.cases;
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
        List<ExamCaseFacet> ccs = this.cases;
        List<ExamCaseFacet> targets = new LinkedList();
        this.logger.log("批量删除了考试成绩。");
        for (ExamCaseFacet ec : ccs) {
            if (ec.isSelected()) {
                targets.add(ec);
            }
        }
        this.examCaseDAO.deleteBulkExamCaseFacet(targets);
        this.synDB();
        return null;
    }

    /**
     * 批量发布考试成绩
     *
     * @return
     */
    public String batchPub() {
    	String score = this.exam.getiPassScore();
    	double s = this.exam.getQualified();
    	if(score!=null){
    		s = Double.parseDouble(score); 
    	}
        List<ExamCaseFacet> ccs = this.cases;
        List<String> targets = new LinkedList<>();
        List<String> passExams = new LinkedList<>();
        this.logger.log("批量发布了考试成绩。");
        for (ExamCaseFacet ec : ccs) {
        	double i = ec.getScore();
        	if(i>=s){
        		passExams.add(ec.getId());
        	}
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
        this.examCaseDAO.publishExamCases(ts2);
        this.examCaseDAO.setExamCasesPass(passExams);
        JsfHelper.info("批量发布成绩完成！");
        return null;
    }

    /**
     * 发布全部考试成绩
     *
     * @return
     */
    public String pubAll() {
        List<ExamCase> ccs = examCaseDAO.findAllUnpubExamCaseByExam(this.examinationId);
        this.logger.log("发布了全部考试成绩。");
        List<String> targets = new LinkedList();
        for (ExamCase ec : ccs) {
            if (!ec.isIfPub()) {
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
        JsfHelper.info("批量发布全部成绩完成！");
        return null;
    }

    /**
     * 清除所有成绩
     *
     * @return
     */
    public String deleteAll() {
        try {
            this.cacheService.reBuildCache(true);//清空缓存数据
            this.logger.log("清空了所有考试成绩。");
            //启动新线程进行删除操作，可能耗较长时间
            final String tempId = this.examinationId;
            Runnable runner = new Runnable() {
                @Override
                public void run() {;
                    examCaseDAO.deleteBulkExamCaseByExam(tempId);
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
    
    public void publishResults(){
    	if(ifPubAll){//批量发布成绩
    		this.batchPub();
    	}else{//发布全部成绩
    		this.pubAll();
    	}
    }
    
    public void setType(){
    	HttpServletRequest request = JsfHelper.getRequest();
    	String type = request.getParameter("type");
    	if(type.equals("0")){//批量发布成绩
    		this.ifPubAll = false;
    	}else if(type.equals("1")){//发布全部成绩
    		this.ifPubAll = true;
    	}
    }
    
}
