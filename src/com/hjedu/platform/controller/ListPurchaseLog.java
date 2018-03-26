package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.chart.PieChartModel;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonPurchaseLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.vo.PurchaseLog;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListPurchaseLog implements Serializable {
	
	private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    
    ILessonPurchaseLogDAO logDAO = SpringHelper.getSpringBean("LessonPurchaseLogDAO");
    List<PurchaseLog> logs;

    int top = 5;//统计的数量
    
    private Date startTime = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 365L );

	private Date endTime = new Date();
	
	String businessId;

    public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<PurchaseLog> getLogs() {
        return logs;
    }

    public void setLogs(List<PurchaseLog> logs) {
        this.logs = logs;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.businessId = CookieUtils.getBusinessId(request);
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        if (asb != null) {
            this.logs = this.logDAO.findAllLessonLog(startTime,endTime,this.businessId);
        }
        
        createPieModels();
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }
     
    public PieChartModel getPieModel2() {
        return pieModel2;
    }
     
    private void createPieModels() {
        createPieModel1();
        createPieModel2();
    }
 
    private void createPieModel1() {
        pieModel1 = new PieChartModel();
        Collections.sort(logs, new Comparator<PurchaseLog>(){  
            public int compare(PurchaseLog o1,PurchaseLog o2){  
            	return  (Long)o1.getNum()<(Long)o2.getNum()?1:( (Long)o1.getNum()==(Long)o2.getNum()?0:-1);
            }  
        });  
        if(logs.size()<top){
        	for(int i=0;i<logs.size();i++){
            	pieModel1.set(logs.get(i).getCourseName(), logs.get(i).getNum());
            }
        }else{
	        for(int i=0;i<top;i++){
	        	pieModel1.set(logs.get(i).getCourseName(), logs.get(i).getNum());
	        }
        }
        pieModel1.setTitle("课程销量");
        pieModel1.setLegendPosition("w");
        pieModel1.setShowDataLabels(true);
    }
     
    private void createPieModel2() {
        pieModel2 = new PieChartModel();
        Collections.sort(logs, new Comparator<PurchaseLog>(){  
            public int compare(PurchaseLog o1,PurchaseLog o2){  
            	return  (double)o1.getMoney()<(double)o2.getMoney()?1:( (double)o1.getMoney()==(double)o2.getMoney()?0:-1);
            }  
        });  
        if(logs.size()<top){
        	for(int i=0;i<logs.size();i++){
        		pieModel2.set(logs.get(i).getCourseName(), logs.get(i).getMoney());
        	}
        }else{
        	for(int i=0;i<top;i++){
	        	pieModel2.set(logs.get(i).getCourseName(), logs.get(i).getMoney());
	        }
        }
        pieModel2.setTitle("课程销售情况");
        pieModel2.setLegendPosition("e");
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
    }
    
}
