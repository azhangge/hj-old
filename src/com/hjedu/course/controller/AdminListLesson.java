package com.hjedu.course.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.lazy.LazyLesson;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AdminListLesson implements Serializable {
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
    LazyLesson lessonVOs;

	public LazyLesson getLessonVOs() {
		return lessonVOs;
	}

	public void setLessonVOs(LazyLesson lessonVOs) {
		this.lessonVOs = lessonVOs;
	}

	@PostConstruct
    public void init() {
    	loadData();
    }
    
    private void loadData(){
    	lessonVOs = new LazyLesson();
    }

    public void delete(String id) {
        this.lessonDAO.deleteLesson(id);
        this.loadData();
    }

    public String editOrd(String id) {
        for (LessonVO cq : this.lessonVOs) {
            if (id.equals(cq.getId())) {
                this.lessonDAO.updateLessonOrd(id,cq.getOrd());
                break;
            }
        }
        return null;
    }
}
