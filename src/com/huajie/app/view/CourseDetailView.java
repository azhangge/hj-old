package com.huajie.app.view;

import java.util.List;

public class CourseDetailView {
	private String result;
	private List<Exam> examList;
	private List<Material> materialList;
	private List<Practice> practiceList;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<Exam> getExamList() {
		return examList;
	}
	public void setExamList(List<Exam> examList) {
		this.examList = examList;
	}
	public List<Material> getMaterialList() {
		return materialList;
	}
	public void setMaterialList(List<Material> materialList) {
		this.materialList = materialList;
	}
	public List<Practice> getPracticeList() {
		return practiceList;
	}
	public void setPracticeList(List<Practice> practiceList) {
		this.practiceList = practiceList;
	}
	
}
