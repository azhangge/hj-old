package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListTeacher implements Serializable {
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    List<Teacher> casusList = new ArrayList<>();
    List<Teacher> outCasusList = new ArrayList<>();
    List<Teacher> innerCasusList = new ArrayList<>();
    ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public List<Teacher> getInnerCasusList() {
		return innerCasusList;
	}

	public void setInnerCasusList(List<Teacher> innerCasusList) {
		this.innerCasusList = innerCasusList;
	}

	public List<Teacher> getOutCasusList() {
		return outCasusList;
	}

	public void setOutCasusList(List<Teacher> outCasusList) {
		this.outCasusList = outCasusList;
	}

	public List<Teacher> getCasusList() {
        return casusList;
    }

    public void setCasusList(List<Teacher> casusList) {
        this.casusList = casusList;
    }

    @PostConstruct
    public void init() {
    	this.casusList = this.teacherDAO.findAllTeacher(bussinessId);
    	for(Teacher t : casusList){
    		if(t.getTeacherType()==0&&t.isIfShow()){
    			outCasusList.add(t);
    		}else if(t.getTeacherType()==1&&t.isIfShow()){
    			innerCasusList.add(t);
    		}
    	}
    }

    public void batDeleteUser() {
        for (Teacher bu : this.casusList) {
            if (bu.isSelected()) {
                this.logger.log("删除了教师：" + bu.getName());
                this.teacherDAO.deleteTeacher(bu.getId());
            }
        }
        init();
    }
    public String editOrd(String id) {
        for (Teacher cq : this.casusList) {
            if (id.equals(cq.getId())) {
                this.teacherDAO.updateTeacher(cq);
                break;
            }
        }
        return null;
    }
}
