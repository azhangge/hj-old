package com.hjedu.course.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;

/**
 * 学习计划记录
 * @author h j
 *
 */
@Entity
@Table(name = "plan_log")
public class StudyPlanLog {
	StudyPlanLog(){}
	public StudyPlanLog(BbsUser user,StudyPlan studyPlan){
		this.user = user;
		this.studyPlan = studyPlan;
	}
	
	@Id
    @Column(name = "id")
	@Expose
    private String id = UUID.randomUUID().toString();
	
	@ManyToOne
	@JoinColumn(name = "study_plan_id")
	private StudyPlan studyPlan;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private BbsUser user;
	
	@Basic(optional = false)
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date updateTime = new Date();
	
	@Basic(optional = false)
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date createTime = new Date();
	
	/**
	 * 学习计划完成时间
	 */
	@Basic(optional = false)
	@Column(name = "finished_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date finishedTime;
	
	/**
	 * 由StudyPlanChangeLog类的studyPlanLog属性进行维护（级联更新），级联删除StudyPlanChangeLog
	 * (mappedBy="studyPlanLog",cascade={CascadeType.REMOVE})
	 */
	@OneToMany(mappedBy="studyPlanLog",cascade={CascadeType.ALL})
	private List<StudyPlanChangeLog> changeLogs;
	
	/**
	 * 完成必修课学时数目
	 */
	@Column(name = "finished_rclass_num")
	@Expose
    private Double finishedRClassNum;
	
	/**
	 * 完成选修课学时数目
	 */
	@Column(name = "finished_class_num")
	@Expose
    private Double finishedClassNum;
	
	/**
	 * 完成必修课程数目
	 */
	@Column(name = "finished_required_num")
	@Expose
    private int finishedRequiredNum;
	
	/**
	 * 完成考试数目
	 */
	@Column(name = "finished_exam_num")
	@Expose
    private int finishedExamNum;
	
	/**
	 * 完成进度
	 */
	@Column(name = "completed_progress")
	@Expose
    private String completedProgress;
	
	/**
	 * 计划是否完成
	 */
	@Column(name = "if_finished")
	@Expose
    private boolean ifFinished=false;
	
	/**
	 * 完成计划排名（按完成时间算）
	 */
	@Column(name = "ranking")
	@Expose
    private int ranking;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StudyPlan getStudyPlan() {
		return studyPlan;
	}

	public void setStudyPlan(StudyPlan studyPlan) {
		this.studyPlan = studyPlan;
	}

	public BbsUser getUser() {
		return user;
	}

	public void setUser(BbsUser user) {
		this.user = user;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<StudyPlanChangeLog> getChangeLogs() {
		return changeLogs;
	}

	public void setChangeLogs(List<StudyPlanChangeLog> changeLogs) {
		this.changeLogs = changeLogs;
	}

	public Double getFinishedClassNum() {
		return finishedClassNum;
	}

	public void setFinishedClassNum(Double finishedClassNum) {
		this.finishedClassNum = finishedClassNum;
	}

	public int getFinishedRequiredNum() {
		return finishedRequiredNum;
	}

	public void setFinishedRequiredNum(int finishedRequiredNum) {
		this.finishedRequiredNum = finishedRequiredNum;
	}

	public int getFinishedExamNum() {
		return finishedExamNum;
	}

	public void setFinishedExamNum(int finishedExamNum) {
		this.finishedExamNum = finishedExamNum;
	}
	
	public Date getFinishedTime() {
		return finishedTime;
	}
	
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	
	public String getCompletedProgress() {
		return completedProgress;
	}
	public void setCompletedProgress(String completedProgress) {
		this.completedProgress = completedProgress;
	}
	
	public boolean isIfFinished() {
		return ifFinished;
	}
	public void setIfFinished(boolean ifFinished) {
		this.ifFinished = ifFinished;
	}
	
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public Double getFinishedRClassNum() {
		return finishedRClassNum;
	}
	public void setFinishedRClassNum(Double finishedRClassNum) {
		this.finishedRClassNum = finishedRClassNum;
	}
	/**
	 * 检查学习计划是否完成，并返回完成比率
	 * @return
	 */
	public double checkStudyPlanIfFinished(){
		//学时完成度
		double result = 0;
		//考试完成度
		double examRate = 0;
		//要求完成学时
		double shouldFinishNum = this.studyPlan.getFinishPlanNum();
		//如果不需要完成考试，而且需要完成学时为0，返回0
		if(!this.studyPlan.isIfFinishExam()&&shouldFinishNum==0){
			return result;
		}
		//要求完成必修学时
		double requiredNum = this.studyPlan.getMinClassNum();
		//判断是否需要完成学时
		if(shouldFinishNum!=0&&this.studyPlan.isIfFinishNum()){//为0，学时完成百分比为0,
			if(requiredNum<=shouldFinishNum){
				//必修课完成百分比
				double rRate = 0;
				//选修课完成百分比
				double rate = 0;
				//必修课权重
				double rweight = requiredNum/shouldFinishNum;
				if(requiredNum==0){
					rRate = rweight;
				}else{
					double fr = finishedRClassNum/requiredNum;
					rRate = fr*rweight;
				}
				double weight = 1-rweight;
				//要求完成选修课学时
				double num = shouldFinishNum-requiredNum;
				if(num==0){
					rate = weight;
				}else{
					double ff = 0;
					if(finishedClassNum!=null){
						ff = finishedClassNum/num;
					}
					rate = ff*weight>weight?weight:ff*weight;
				}
				result = rRate+rate;
			}
		}
		//判断是否需要完成考试
		if(this.studyPlan.isIfFinishExam()){
			//如果需要完成学时，考试占百分比变为0.1
			if(shouldFinishNum!=0&&this.studyPlan.isIfFinishNum()){
				examRate = 0.1;
			}else{
				examRate = 1;
			}
			result = result*0.9;
			if(this.finishedExamNum>=this.studyPlan.getRequiredExamNum()){
				result = result+examRate;
			}
		}
//		//判断是否必修课修满
//		if(this.finishedRequiredNum<this.studyPlan.getRequiredCourseNum()){
//			result = false;
//		}
		return result;
	}
	
	public static void main(String[] args) {
		double a = 4;
		double b = 2;
		double c = 7;
		double d = a/c;
		double e = b/c;
		
		String f = d+e+"";
		System.out.println(f.equals("1.0"));
	}
}
