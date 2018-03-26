package com.hjedu.course.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.course.lazy.LazyLessonTypeLog;

@ManagedBean
@ViewScoped
public class ListTypeLogList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	LazyLessonTypeLog lessonTypeLogs;

	public LazyLessonTypeLog getLessonTypeLogs() {
		return lessonTypeLogs;
	}

	public void setLessonTypeLogs(LazyLessonTypeLog lessonTypeLogs) {
		this.lessonTypeLogs = lessonTypeLogs;
	}

	@PostConstruct
    public void init() {
    	loadData();
    }	
	
	private void loadData(){
		lessonTypeLogs = new LazyLessonTypeLog();
	}
}
