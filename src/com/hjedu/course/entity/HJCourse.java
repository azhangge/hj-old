package com.hjedu.course.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.hjedu.platform.entity.BbsFileModel;

/**
 * 课程模块 课程
 */
@Entity
@Table(name = "hj_course")
@PrimaryKeyJoinColumn(name = "hj_course_id")
public class HJCourse extends BaseCourse {
	private static final long serialVersionUID = 1L;
	/**
	 * 课程所属一级分类id
	 */
	@Column(name = "first_type_id")
	@Expose
	private String firstTypeId;

	/**
	 * 课程所属二级分类id
	 */
	@Column(name = "second_type_id")
	@Expose
	private String secondTypeId;

	/**
	 * 消耗积分
	 */
	@Column(name = "price")
	@Expose
	private int price;

	/**
	 * 附件
	 */
	@OneToMany(mappedBy = "course", cascade = { CascadeType.ALL })
	private List<BbsFileModel> files;

	/**
	 * 章节
	 */
	@OneToMany(mappedBy = "course", cascade = { CascadeType.ALL })
	private List<Chapter> chapters;

	public String getFirstTypeId() {
		return firstTypeId;
	}

	public void setFirstTypeId(String firstTypeId) {
		this.firstTypeId = firstTypeId;
	}

	public String getSecondTypeId() {
		return secondTypeId;
	}

	public void setSecondTypeId(String secondTypeId) {
		this.secondTypeId = secondTypeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<BbsFileModel> getFiles() {
		return files;
	}

	public void setFiles(List<BbsFileModel> files) {
		this.files = files;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

}
