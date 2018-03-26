package com.hjedu.platform.controller;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.customer.dao.CertificateDAO;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Certificate;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.examination.dao.GeneralQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.SystemInfo;
import com.huajie.app.util.CastUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class HJExamTest implements Serializable {

	private static final long serialVersionUID = 1L;
	IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
    ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
	ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	ICourseTypeDAO courseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
	ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
	IBuyCourseService buyCourseService = SpringHelper.getSpringBean("BuyCourseService");
	ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
	IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
	IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
	IStudyPlanDAO studyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");
	
	String businessId;
	
    private SystemInfo info;
    private static Log logger = LogFactory.getLog(HJExamTest.class);

    public static void main(String[] args) {
        System.out.println(CastUtil.castInt("w"));
    }
    
    public SystemInfo getInfo() {
        return info;
    }

    public void setInfo(SystemInfo info) {
        this.info = info;
    }

    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.info = this.infoDAO.findSystemInfoByBusinessId(this.businessId);
    }

    public void createImageOfMp4(){
		List<BbsFileModel> bfms = bbsFileDAO.findFilesByEXT(".mp4");
		for(BbsFileModel bf : bfms){
			if(StringUtil.isEmpty(bf.getFileFullPath())){
				NewFile nf = new NewFile();
				nf.getImageOfVideo(bf);
				bf.setFileFullPath("/servlet/ShowImage?id="+bf.getId());
				bbsFileDAO.updateClientFileInfo(bf);
			}
		}
		JsfHelper.info("完成！");
    }
    
    public void updateVideoTimeOfMp4(){
		List<BbsFileModel> bfms = bbsFileDAO.findFilesByEXT(".mp4");
		for(BbsFileModel bf : bfms){
			if(StringUtil.isEmpty(bf.getTotal_time()+"")){
				long ls = JsonUtil.getVedioTotalTime(bf,getClass().getResource("/").getFile().toString());
				bf.setTotal_time(ls);
				bbsFileDAO.updateClientFileInfo(bf);
			}
		}
		JsfHelper.info("完成！");
	}
    
    public void updateMD5OfFile(){
		List<BbsFileModel> bfms = bbsFileDAO.findAllClientFile();
		for(BbsFileModel cfm : bfms){
			if(StringUtil.isEmpty(cfm.getMd5())){
				String path = "";
				try {
					path = FileUtil.getFileRealPath(cfm);
					File mf = new File(path);
					if(mf.exists()){
						FileInputStream fis = new FileInputStream(path);
						String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
						IOUtils.closeQuietly(fis);
						cfm.setMd5(md5);
						bbsFileDAO.updateClientFileInfo(cfm);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		JsfHelper.info("完成！");
	}
    
    public void updateLessonTypeScore(){
		List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
		for(LessonType lt : lts){
			if(lt!=null){
				String typeName = "";
				String courseTypeStr=lt.getCourseTypeStr();
				if(StringUtil.isNotEmpty(courseTypeStr)){
					List<String> idList =  StringUtil.idsToIdList(courseTypeStr);
					List<CourseType> ctlist = this.courseTypeDAO.findSomeCourseType(idList);
					for(CourseType ct:ctlist){
						typeName=typeName+ct.getName()+";";
					}
				}
				List<Lesson> ls = lessonDAO.findAllLessonByType(lt.getId());
				long num = 0;
				float keshi = 0f;
				BigDecimal keshi1 = new BigDecimal(Float.toString(keshi));
				long totalTime = 0;
				for(Lesson l : ls){
					num = num+l.getScorePaid();
					keshi1.add(new BigDecimal(Double.toString(l.getClassNum())));
					totalTime = totalTime + l.getTotalTime();
				}
				lt.setTotalScorePaid(num);
				lt.setTotalClassNum(Double.valueOf(keshi1.toString()));
				
				lt.setTotalTime(totalTime);
				lt.setCourseTypeCN(typeName);
				lessonTypeDAO.updateLessonType(lt);
			}
		}
		JsfHelper.info("完成！");
	}
    
    public void updateLessonTypeUserNum(){
    	List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonType lt : lts){
    		List<LessonTypeLog> ltls = lessonTypeLogDAO.findLessonTypeLogByLessonType(lt.getId());
    		if(ltls!=null){
    			lt.setUserNum(ltls.size());
    			this.lessonTypeDAO.updateLessonType(lt);
    		}
    	}
    }
    
    public void createLessonTypeLog(){
		List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
		List<BbsUser> users = bbsUserDAO.findAllBbsUser(this.businessId);
		lessonTypeLogDAO.deleteAll();
		for(BbsUser user : users){
			if(StringUtil.isNotEmpty(user.getLessonTypeStr())){
				//创建用户的所有课程记录
				List<LessonType> ult = new LinkedList<>();
				for(LessonType lt : lts){
					if(user.getLessonTypeStr().contains(lt.getId())){
						ult.add(lt);
					}
				}
				if(ult.size()>0){
					for(LessonType ltp : ult){
						LessonTypeLog ltl = new LessonTypeLog(ltp, user);
						//更新课程完成学时数
						List<LessonLog> llogs = this.lessonLogDAO.findLessonLogByTypeAndUsr(user.getId(), ltp.getId());
						if(llogs!=null && llogs.size()>0){
							Double num = 0d;
							for(LessonLog llog : llogs){
								if(llog.isFinished()){
									num+=llog.getLesson().getClassNum();
								}
								if(llog.isFinished()){
									llog.getTimeFinished();
								}
							}
							ltl.setFinishedClassNum(num);
							ltl.setBusinessId(this.businessId);
						}
						lessonTypeLogDAO.addLessonTypeLog(ltl);
					}
				}
			}
		}
		JsfHelper.info("完成！");
	}
    
    public void updateLessonTypeLaberStr2(){
    	List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonType lt : lts){
    		String[] types = null;
    		if(StringUtil.isEmpty(lt.getCourseTypeStr())){
    			if(StringUtil.isEmpty(lt.getLabelStr())){
    				continue;
    			}
    			types = lt.getLabelStr().split(";");
    		}else{
    			types = lt.getCourseTypeStr().split(";");
    		} 
    		String labStr = "";
    		String cTypeStr = "";
    		for(int i=0;i<types.length;i++){
    			CourseType ct = courseTypeDAO.findCourseType(types[i]);
    			if(ct==null){
    				continue;
    			}
    			List<CourseType> cts = courseTypeDAO.findAllSonCourseType(ct.getId(),this.businessId);
    			if(!(cts!=null&&cts.size()>0&&cts.get(0)!=null)){
    				labStr = labStr+ct.getAncestors();
    				cTypeStr = cTypeStr+ct.getId()+";";
    			}
    		}
    		lt.setLabelStr(labStr);
    		lt.setCourseTypeStr(cTypeStr);
    		lessonTypeDAO.updateLessonType(lt);
    	}
    	JsfHelper.info("完成！");
    }
    
    /**
     * 将免费课程加入
     */
    public void updateUserLessonTypeStr(){
    	List<LessonType> lts = lessonTypeDAO.findfreeLessonType();
    	List<BbsUser> users = bbsUserDAO.findAllBbsUser(this.businessId);
    	for(BbsUser u : users){
    		String ltstr = u.getLessonTypeStr();
    		StringBuffer strb = new StringBuffer();
    		for(LessonType lt : lts){
    			if(!ltstr.contains(lt.getId())){
    				strb.append(lt.getId());
    				strb.append(";");
    			}
    		}
    		u.setLessonTypeStr(ltstr+strb.toString());
    		bbsUserDAO.updateBbsUser(u);
    	}
    	JsfHelper.info("完成！");
    }
    
    public void createTeacherBylessonType(){
    	List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonType lt : lts){
    		if(lt!=null&&StringUtil.isNotEmpty(lt.getTeacherName())){
    			String name = lt.getTeacherName();
    			Teacher t = teacherDAO.findTeacherByUrn(name,this.businessId);
    			lt.setTeacher(t);
    			lessonTypeDAO.updateLessonType(lt);
//    			if(t==null){
//    				Teacher te = new Teacher();
//    				te.setName(name);
//    				te.setIntroduction("待编辑");
//    				te.setJobTitle("讲师");
//    				teacherDAO.addTeacher(te);
//    			}
    		}
    	}
    	JsfHelper.info("完成！");
    }
    
    public void copyEpubFile(){
    	List<BbsFileModel> files = this.bbsFileDAO.findFilesByEXT(".epub");
    	for(BbsFileModel bfm : files){
    		String path = "";
    		if(bfm!=null){
    			path = bfm.getUserId();
    			if(StringUtil.isEmpty(path)){
    				if(bfm.getUser()==null){
    					continue;
    				}
    				path = bfm.getUser().getId();
    				if(StringUtil.isEmpty(path)){
    					continue;
    				}
    			}
    			File file = new File("C:/huajie_exam/"+path+"/"+bfm.getId());
    			if(!file.exists()){
    				continue;
    			}
    			File epubPath = new File("C:/huajie_exam/epub");
    			if(!epubPath.exists()){
    				epubPath.mkdirs();
    			}
    			File epubFile = new File(epubPath.getPath()+"/"+bfm.getId()+bfm.getFileExt());
    			try {
					FileInputStream fis = new FileInputStream(file);
					FileOutputStream fos = new FileOutputStream(epubFile);
					BufferedInputStream is = new BufferedInputStream(fis);
		            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
		            int len = 0;
		            byte[] bb = new byte[1024];
		            while ((len = is.read(bb)) != -1) {
		                os.write(bb, 0, len);
		            }
		            is.close();
		            os.close();
		            fis.close();
		            fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	JsfHelper.info("完成！");
    }
    
    /**
     * 去掉rerebbs_user中LessonTypeStr字段里重复的id
     */
    public void updateUserLessonTypeStr2(){
    	List<BbsUser> users = bbsUserDAO.findAllBbsUser(this.businessId);
    	for(BbsUser u : users){
    		String ids = u.getLessonTypeStr();
    		String[] ids2 = ids.split(";");
    		Set<String> idSet = new HashSet<>();
    		for(int i=0;i<ids2.length;i++){
    			idSet.add(ids2[i]);
    		}
    		StringBuffer strb = new StringBuffer();
    		for(String id : idSet){
    			strb.append(id);
    			strb.append(";");
    		}
    		u.setLessonTypeStr(strb.toString());
    		bbsUserDAO.updateBbsUser(u);
    	}
    	JsfHelper.info("完成！");
    }
    
    public void updateLessonTypeToExam(){
    	List<LessonType> lts = this.lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonType lt : lts){
    		List<String> list =  StringUtil.idsToIdList(lt.getExamStr());
    		if(list!=null&&list.size()>0){
    			lt.setExams(this.examinationDAO.findExamsByIdList(list));
    		}
    		lessonTypeDAO.updateLessonType(lt);
    	}
    	JsfHelper.info("完成！");
    }
    
    public void updateExamCaseIfPassed(){
    	List<ExamCase> exams = examCaseDAO.findAllExamCase();
    	for(ExamCase exam : exams){
    		Examination en = exam.getExamination();
    		if(en!=null){
    			if(exam.getScore()>=en.getQualified()){
    				exam.setIfPassed(true);
    			}else{
    				exam.setIfPassed(false);
    			}
    			examCaseDAO.updateExamCase(exam);
    		}
    	}
    	List<LessonTypeLog> ltlogs = lessonTypeLogDAO.findAllLessonTypeLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonTypeLog lt : ltlogs){
    		BbsUser user = lt.getUser();
    		LessonType ltype = lt.getLessonType();
    		if(user!=null&&ltype!=null){
//    			if(lt.getFinishedClassNum()>=ltype.getOpenExamNum()){
    			if(lt.getFinishedClassNum()>=ltype.getTotalClassNum()){
    				lt.setFinished(true);
    			}else{
    				lt.setFinished(false);
    			}
    			List<LessonLog> logs = lessonLogDAO.findLessonLogByTypeAndUsr(user.getId(), ltype.getId());
    			Double i = 0d;
    			BigDecimal i1 = new BigDecimal(Double.toString(i));
    			for(LessonLog log : logs){
    				if(log.isFinished()){
    					i1.add(new BigDecimal(Double.toString(log.getLesson().getClassNum())));
    				}
    			}
    			lt.setFinishedClassNum(Double.valueOf(i1.toString()));
    			lessonTypeLogDAO.updateLessonTypeLog(lt);
    		}
    	}
    	JsfHelper.info("完成！");
    }
    
    public void test(){
//    	List<BbsUser> users = this.bbsUserDAO.findAllBbsUser();
//    	for(BbsUser user : users){
//    		if(user!=null){
//    			this.bbsUserDAO.updateBbsUser(user);
//    		}
//    	}
//    	List<StudyPlan> sps = this.studyPlanDAO.findAllStudyPlan();
//    	for(StudyPlan sp : sps){
//    		List<String> cs = StringUtil.idsToIdList(sp.getCourseStr());
//    		if(CollectionUtil.isNotEmpty(cs)){
//    			sp.setCourseNum(cs.size());
//    		}
//    		List<String> us = StringUtil.idsToIdList(sp.getUserStr());
//    		if(CollectionUtil.isNotEmpty(us)){
//    			sp.setUserNum(us.size());
//    		}
//    		studyPlanDAO.updateStudyPlan(sp);
//    	}
    	CertificateDAO dao = SpringHelper.getSpringBean("CertificateDAO");
    	Certificate c = new Certificate();
    	dao.add(c);
//    	c.setPicture("11111");
//    	dao.update(c);
//    	dao.delete("6f07f73d-0956-49f3-98a0-d8c66e9a2273");
    	long i = dao.getAmount();
    	JsfHelper.info("完成！"+i);
    }
    
    public void test2(){
		IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
		List<ChoiceQuestion> cqs = questionDAO.findAllChoiceQuestion();
		IMultiChoiceQuestionDAO questionDAO2 = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
		List<MultiChoiceQuestion> mcqs = questionDAO2.findAllMultiChoiceQuestion();
		IJudgeQuestionDAO questionDAO3 = SpringHelper.getSpringBean("JudgeQuestionDAO");
		List<JudgeQuestion> jqs = questionDAO3.findAllJudgeQuestion();
		GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
		for(ChoiceQuestion cq : cqs){
			GeneralQuestion gq = new GeneralQuestion();
			HashMap<String, Object> map = JsonUtil.getQuestionNameAnswer(cq);
			gq.setId(cq.getId());
			gq.setName(cq.getName());
			gq.setGenTime(cq.getGenTime());
			gq.setModule(cq.getModule());
			gq.setRightStr(cq.getRightStr());
			gq.setCleanName(cq.getCleanName());
			gq.setQtype(cq.getQtype());
			gq.setType(map.get("type").toString());
			gq.setAnswers(map.get("answer").toString());
			gq.setQuestion(map.get("question").toString());
			gq.setRealoption(map.get("realoption").toString());
			gqdao.add(gq);
		}
		for(MultiChoiceQuestion cq : mcqs){
			GeneralQuestion gq = new GeneralQuestion();
			HashMap<String, Object> map = JsonUtil.getQuestionNameAnswer(cq);
			gq.setId(cq.getId());
			gq.setName(cq.getName());
			gq.setGenTime(cq.getGenTime());
			gq.setModule(cq.getModule());
			gq.setRightStr(cq.getRightStr());
			gq.setCleanName(cq.getCleanName());
			gq.setQtype(cq.getQtype());
			gq.setType(map.get("type").toString());
			gq.setAnswers(map.get("answer").toString());
			gq.setQuestion(map.get("question").toString());
			gq.setRealoption(map.get("realoption").toString());
			gqdao.add(gq);
		}
		for(JudgeQuestion cq : jqs){
			GeneralQuestion gq = new GeneralQuestion();
			HashMap<String, Object> map = JsonUtil.getQuestionNameAnswer(cq);
			gq.setId(cq.getId());
			gq.setName(cq.getName());
			gq.setGenTime(cq.getGenTime());
			gq.setModule(cq.getModule());
			gq.setRightStr(cq.getRightStr());
			gq.setCleanName(cq.getCleanName());
			gq.setQtype(cq.getQtype());
			gq.setType(map.get("type").toString());
			gq.setAnswers(map.get("answer").toString());
			gq.setQuestion(map.get("question").toString());
			gq.setRealoption(map.get("realoption").toString());
			gqdao.add(gq);
		}
    }
    
    public void updatePracticeNum(){
    	List<Lesson> ls = lessonDAO.findAllLesson();
    	for(Lesson l : ls){
    		if(l!=null){
    			int newNum = 0;
    			//学习资料更新：课程原有练习数目-该资料原来关联数目+现在关联数目
    	    	List<ModuleExamination2> mets = l.getModuleExaminations();
    	    	if(mets!=null&&mets.size()>0){
    	    		//避免有空数据，计算当前关联练习数目
    	    		for(ModuleExamination2 met : mets){
    	    			if(met!=null){
    	    				ExamModuleModel mod = met.getModule();
    	    				if(mod!=null){
    	    					newNum = (int) (newNum + mod.getTotalNum());
    	    				}
    	    			}
    	    		}
    	    	}
    	    	l.setPracticeNum(newNum);
    	    	lessonDAO.updateLesson(l);
    		}
    	}
    	List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	for(LessonType lt : lts){
    		if(lt!=null){
    			List<Lesson> lessons = lessonDAO.findAllLessonByType(lt.getId());
    			int i = 0;
    			if(lessons!=null&&lessons.size()>0){
    				for(Lesson l : lessons){
    					i = i + l.getPracticeNum();
    				}
    			}
    			if(i!=0){
    				lt.setPracticeNum(i);
    				lessonTypeDAO.updateLessonType(lt);
    			}
    		}
    	}
    	JsfHelper.info("完成！");
    }
}
