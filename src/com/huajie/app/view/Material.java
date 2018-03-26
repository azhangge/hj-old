package com.huajie.app.view;

import java.util.LinkedList;
import java.util.List;

import org.apache.activemq.console.Main;

//学习资料对象
public class Material {
	private String material_id = "";
	private String material_name = "";
	private String material_type = "";
	private String material_version = "";
	private String material_url = "";
	private String material_md5 = "";
	private String knowledge_point_id = "";
	private List<Practice> practiceList;
	private String material_download_url = "";
	private String material_score = "";
	private String material_leastTime = "";
	private String material_classNum = "";
	private String material_size = "";
	private String vedio_totalTime = "";
	private String screenshot = "";
	private String file_id = "";
	private String ord = "";
	private String parent_id = "";
	private int chapter_type = 0;
	private String chapter_index="";

	public String getParentId() {
		return parent_id;
	}
	public void setParentId(String parent_id) {
		this.parent_id = parent_id;
	}
	public int getChapterType() {
		return chapter_type;
	}
	public void setChapterType(int chapter_type) {
		this.chapter_type = chapter_type;
	}
	public String getChapter_index() {
		return chapter_index;
	}
	public void setChapter_index(String chapter_index) {
		this.chapter_index = chapter_index;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	public String getVedio_totalTime() {
		return vedio_totalTime;
	}
	public void setVedio_totalTime(String vedio_totalTime) {
		this.vedio_totalTime = vedio_totalTime;
	}
	public String getMaterial_size() {
		return material_size;
	}
	public void setMaterial_size(String material_size) {
		this.material_size = material_size;
	}
	public String getMaterial_score() {
		return material_score;
	}
	public void setMaterial_score(String material_score) {
		this.material_score = material_score;
	}
	public String getMaterial_leastTime() {
		return material_leastTime;
	}
	public void setMaterial_leastTime(String material_leastTime) {
		this.material_leastTime = material_leastTime;
	}
	public String getMaterial_classNum() {
		return material_classNum;
	}
	public void setMaterial_classNum(String material_classNum) {
		this.material_classNum = material_classNum;
	}
	public String getMaterial_download_url() {
		return material_download_url;
	}
	public void setMaterial_download_url(String material_download_url) {
		this.material_download_url = material_download_url;
	}
	public List<Practice> getPracticeList() {
		return practiceList;
	}
	public void setPracticeList(List<Practice> practiceList) {
		this.practiceList = practiceList;
	}
	public String getMaterial_id() {
		return material_id;
	}
	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}
	public String getMaterial_name() {
		return material_name;
	}
	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
	}
	public String getMaterial_type() {
		return material_type;
	}
	public void setMaterial_type(String material_type) {
		this.material_type = material_type;
	}
	public String getMaterial_version() {
		return material_version;
	}
	public void setMaterial_version(String material_version) {
		this.material_version = material_version;
	}
	public String getMaterial_url() {
		return material_url;
	}
	public void setMaterial_url(String material_url) {
		this.material_url = material_url;
	}
	public String getMaterial_md5() {
		return material_md5;
	}
	public void setMaterial_md5(String material_md5) {
		this.material_md5 = material_md5;
	}
	public String getKnowledge_point_id() {
		return knowledge_point_id;
	}
	public void setKnowledge_point_id(String knowledge_point_id) {
		this.knowledge_point_id = knowledge_point_id;
	}
	public String getOrd() {
		return ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}
	
}
