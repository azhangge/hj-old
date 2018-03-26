package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.service.ILogService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class Welcome implements Serializable{

    long userNum = 0;
    long caseNum = 0;
    long examNum = 0;
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
    //IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");

    LineChartModel lineModel;
    int days = 20;//统计的天数
    int maxExams=10;//最多列表的考试数
    
    private String businessId;

    public long getUserNum() {
        return userNum;
    }

    public void setUserNum(long userNum) {
        this.userNum = userNum;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMaxExams() {
        return maxExams;
    }

    public void setMaxExams(int maxExams) {
        this.maxExams = maxExams;
    }

    public long getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(long caseNum) {
        this.caseNum = caseNum;
    }

    public long getExamNum() {
        return examNum;
    }

    public void setExamNum(long examNum) {
        this.examNum = examNum;
    }
    
    

    @PostConstruct
    public void init() {
    	/* 此处代码登录时候已经操作 */
    	/*String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    	AdminInfo admin = adminDAO.findAdminByUrnByBusinessId("admin",businessId);
    	AdminSessionBean asb=new AdminSessionBean();
    	asb.setAdmin(admin);
    	asb.setIfLogin(true);
//            boolean t = admin.getGrp().equals("admin");
    	boolean t = !admin.getGrp().equals("company");
    	asb.setIfSuper(t);
    	asb.setLtime(admin.getLtime());
    	JsfHelper.getRequest().getSession().setAttribute("adminSessionBean", asb);
    	
    	admin.setLtime(new Date());
    	adminDAO.updateAdmin(admin);
    	IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
    	String ip = JsfHelper.getRequest().getRemoteAddr();
    	String addr = ips.seek(ip);
    	try {
    		ILogService logger = SpringHelper.getSpringBean("LogService");
    		logger.log("登录了系统（IP：" + ip + "，" + addr + "）。");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}*/
    	JsfHelper.getRequest().getRequestDispatcher("/m/Welcome?faces-redirect=true");
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	userNum=userDAO.getBbsUserNumByBusinessId(this.businessId);
        caseNum = caseDAO.getExamCaseNum(this.businessId);
        examNum=this.examService.findAllShowedExamination().size();
        this.createDateModel();
    }
    
    
    
    
    /**
     * 创建一个成绩的线图
     */
    private void createDateModel() {

        List<Examination> exams = this.examService.findAllShowedExamination();
        Collections.sort(exams);
        Collections.reverse(exams);

        lineModel = new LineChartModel();
        lineModel.setShowPointLabels(true);
        lineModel.setZoom(true);
        lineModel.setLegendPosition("nw");
        lineModel.setTitle("综合考试趋势图");
        lineModel.setAnimate(true);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int max = maxExams, j = 0;

        for (Examination ex : exams) {
            j++;
            LineChartSeries series = new LineChartSeries();
            series.setLabel(ex.getName());
            for (int i = 0; i < this.days; i++) {
                Date dd = new Date(System.currentTimeMillis() - (3600L * 24L * 1000L * i));
                long num = this.caseDAO.getParticipateNumByExamAndDate(ex.getId(), dd);
                String sid = sdf.format(dd);
                series.set(sid, num);
            }
            lineModel.addSeries(series);
            if (j >= max) {
                break;
            }
        }

        
        lineModel.getAxis(AxisType.Y).setLabel("成绩数");
        
        Axis axis = new DateAxis();
        axis.setTickAngle(-30);
        axis.setMax(sdf.format(new Date(System.currentTimeMillis()))+" 05:30");
        axis.setTickFormat("%#m.%#d");
        lineModel.getAxes().put(AxisType.X, axis);
        
        Axis axisY=lineModel.getAxis(AxisType.Y);
        axisY.setTickFormat("%d");
        axisY.setMin(0);
        //axisY.setTickInterval("20");
    }


}
