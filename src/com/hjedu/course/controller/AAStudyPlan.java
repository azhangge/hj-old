package com.hjedu.course.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.springframework.ui.context.Theme;

import com.hjedu.course.dao.ICourseOfPlanDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.entity.CourseOfPlan;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.entity.StudyPlanChangeLog;
import com.hjedu.course.entity.StudyPlanLog;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.course.service.IStudyPlanLogService;
import com.hjedu.customer.controller.CourseTreeList;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.controller.AdminNewFile;
import com.huajie.app.util.TreeUtil;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAStudyPlan implements Serializable {
	private static final long serialVersionUID = 1L;
	IStudyPlanDAO studyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");
	IStudyPlanLogDAO studyPlanLogDAO = SpringHelper.getSpringBean("StudyPlanLogDAO");
	ICourseOfPlanDAO courseOfPlanDAO = SpringHelper.getSpringBean("CourseOfPlanDAO");
	IBuyCourseService buyCourseService = SpringHelper.getSpringBean("BuyCourseService");
	IStudyPlanLogService studyPlanLogService = SpringHelper.getSpringBean("StudyPlanLogService");
	private DualListModel<String> cities;
    private DualListModel<Theme> themes;
    private DualListModel<LessonType> courses;
    private DualListModel<Examination> exams;
    private List<DictionaryModel> departments;
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private boolean flag;
    private StudyPlan studyPlan;
    private List<CourseOfPlan> cops;
    String businessId;
    
	public List<CourseOfPlan> getCops() {
		return cops;
	}

	public void setCops(List<CourseOfPlan> cops) {
		this.cops = cops;
	}

	public StudyPlan getStudyPlan() {
		return studyPlan;
	}

	public void setStudyPlan(StudyPlan studyPlan) {
		this.studyPlan = studyPlan;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IDictionaryDAO dictionaryDAO = SpringHelper.getSpringBean("DictionaryDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
     
    public DualListModel<LessonType> getCourses() {
		return courses;
	}

	public void setCourses(DualListModel<LessonType> courses) {
		this.courses = courses;
	}

	public DualListModel<Examination> getExams() {
		return exams;
	}

	public void setExams(DualListModel<Examination> exams) {
		this.exams = exams;
	}

	public List<DictionaryModel> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DictionaryModel> departments) {
		this.departments = departments;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public DualListModel<String> getCities() {
        return cities;
    }
 
    public void setCities(DualListModel<String> cities) {
        this.cities = cities;
    }
 
    public DualListModel<Theme> getThemes() {
        return themes;
    }
 
    public void setThemes(DualListModel<Theme> themes) {
        this.themes = themes;
    }
    
    @PostConstruct
    public void init() {
    	HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	this.businessId = CookieUtils.getBusinessId(req);
    	String id = req.getParameter("id");
        if (id != null) {
            this.studyPlan = this.studyPlanDAO.findStudyPlan(id);
            this.flag = true;
        }else{
        	this.studyPlan = new StudyPlan();
        }
//        loadCourses();
        if(this.studyPlan!=null){
        	loadExams();
        	loadStructure();
        }
    }
    
    private void loadCourses() {
    	List<LessonType> lts = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	List<LessonType> source = new ArrayList<LessonType>();
    	List<LessonType> target = new ArrayList<LessonType>();
    	if(flag){
    		String[] str = this.studyPlan.getCourseStr().split(";");
    		if(str!=null){
    			Set<String> set = new HashSet<>(Arrays.asList(str));
    			for(LessonType lt : lts){
    				if(set.contains(lt.getId())){
    					target.add(lt);
    				}else{
    					source.add(lt);
    				}
    			}
    		}
    	}else{
    		source.addAll(lts);
    	}
        courses = new DualListModel<LessonType>(source, target);
	}
    
    private void loadExams() {
    	List<Examination> lts = examinationDAO.findAvailableExamination();
        List<Examination> source = new ArrayList<Examination>();
        List<Examination> target = new ArrayList<Examination>();
        if(flag){
        	if(this.studyPlan.getExamsStr()!=null){
        		String[] str = this.studyPlan.getExamsStr().split(";");
        		if(str!=null){
        			Set<String> set = new HashSet<>(Arrays.asList(str));
        			for(Examination lt : lts){
        				if(set.contains(lt.getId())){
        					target.add(lt);
        				}else{
        					source.add(lt);
        				}
        			}
        		}
        	}else{
        		source.addAll(lts);
        	}
    	}else{
    		source.addAll(lts);
    	}
        exams = new DualListModel<Examination>(source, target);
	}

	private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dictionaryDAO.findDictionaryModel(this.businessId);
        if(dm!=null){
        	test(dm, root);
        }
    }
	
//	public void setSelected(TreeNode tn){
//		TreeNode ptn = tn.getParent();
//		if(ptn!=root){
//			ptn.setExpanded(true);
//			ptn.setSelected(true);
//			setSelected(ptn);
//		}else{
//			return;
//		}
//	}
    
    public void test(DictionaryModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<DictionaryModel> ls = dictionaryDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        Set<String> set = null;
        if(flag){
        	if(this.studyPlan.getUserStr()!=null){
        		String[] str = this.studyPlan.getUserStr().split(";");
        		if(str!=null){
        			set = new HashSet<>(Arrays.asList(str));
        		}
        	}
        }
        if (ls.isEmpty()) {
        	List<BbsUser> users = userDAO.findBbsUserByDept(dd.getId());
        	for(BbsUser u:users){
        		TreeNode un = new DefaultTreeNode(u, node);
        		if(flag&&set!=null){
        			if(set.contains(u.getId())){
        				un.setSelected(true);
        				TreeUtil.setParentExpanded(un, root);
        			}
        		}
        	}
            return;
        } else {
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }
    
    public String done(){
    	Date begin = this.studyPlan.getAvailableBegain();
    	Date end = this.studyPlan.getAvailableEnd();
    	if(begin.getTime()>end.getTime()){
    		JsfHelper.warn("学习计划开始时间不能大于结束时间！");
        	return "#";
    	}
    	Double min = this.studyPlan.getMinClassNum();
    	Double max = this.studyPlan.getPlanTotalNum();
    	if(min == null || max == null){
    		JsfHelper.warn("请选择要学习的课程");
        	return "#";
    	}
        if(max<this.studyPlan.getFinishPlanNum()||
        		this.studyPlan.getFinishPlanNum()<min){
        	JsfHelper.warn("学习计划完成学时数应该在"+min+"到"+max+"之间！");
        	return "#";
        }
        if(flag){
        	this.courseOfPlanDAO.deleteByPlanId(studyPlan.getId());
        }
    	List<Examination> les = exams.getTarget();
    	StringBuffer examStr = new StringBuffer();
    	StringBuffer examCN = new StringBuffer();
    	int j = 0;
    	if(les!=null&&les.size()>0){
    		for(Examination lt : les){
    			if(!examStr.toString().contains(lt.getId())){
    				examStr.append(lt.getId());
    				examStr.append(";");
    				examCN.append(lt.getName());
    				examCN.append(";");
    				j = j+1;
    			}
    		}
    		this.studyPlan.setIfFinishExam(true);
    	}
    	this.studyPlan.setRequiredExamNum(j);
    	this.studyPlan.setExamsStr(examStr.toString());
    	this.studyPlan.setExamsStrCN(examCN.toString());
    	List<CourseOfPlan> cops = new LinkedList<>();
    	int i = 0;
    	StringBuffer ltStr = new StringBuffer();
    	StringBuffer ltCN = new StringBuffer();
    	int courseNum = 0;
    	if(studyPlan.getCourses()!=null&&studyPlan.getCourses().size()>0){
    		for(CourseOfPlan lt : this.studyPlan.getCourses()){
    			if(!ltStr.toString().contains(lt.getLessonType().getId())){
    				cops.add(lt);
    				ltStr.append(lt.getLessonType().getId());
    				ltStr.append(";");
    				ltCN.append(lt.getName());
    				ltCN.append(";");
    				courseNum = courseNum +1;
//    				this.courseOfPlanDAO.addCourseOfPlan(lt);
    				if(lt.isIfRequired()){
    					i=i+1;
    				}
    			}
    		}
    	}
    	this.studyPlan.setCourseStr(ltStr.toString());
    	this.studyPlan.setCourseStrCN(ltCN.toString());
    	this.studyPlan.setCourseNum(courseNum);
    	this.studyPlan.setRequiredCourseNum(i);
    	this.studyPlan.setCourses(cops);
    	updateStudyPlanClassNum();
    	List<BbsUser> users = new LinkedList<>();
    	StringBuffer userStr = new StringBuffer();
    	StringBuffer userCN = new StringBuffer();
    	int userNum = 0;
    	if(selectedNodes!=null&&selectedNodes.length>0){
    		for(TreeNode node : selectedNodes){
    			if(node.getData() instanceof BbsUser){
    				BbsUser user = (BbsUser)node.getData();
    				if(!userStr.toString().contains(user.getId())){
    					userStr.append(user.getId());
    					userStr.append(";");
    					userCN.append(user.getName());
    					userCN.append(";");
    					users.add(user);
    					userNum = userNum+1;
    				}
    			}
    		}
    	}
    	this.studyPlan.setUserStr(userStr.toString());
    	this.studyPlan.setUserStrCN(userCN.toString());
    	this.studyPlan.setUserNum(userNum);
    	if(flag){
    		this.studyPlanDAO.updateStudyPlan(studyPlan);
    		//如果是修改，先清除之前的记录
    		this.studyPlanLogDAO.deleteStudyPlanLogByStudyPlanId(studyPlan.getId());
    	}else{
    		studyPlan.setBusinessId(this.businessId);
    		this.studyPlanDAO.addStudyPlan(studyPlan);
    	}
		for(BbsUser user : users){
			studyPlanLogService.createStudyPlanLog(this.studyPlan,user);
			if(studyPlan.isIfBuyCourses()){
				for(CourseOfPlan lt : this.studyPlan.getCourses()){
					buyCourseService.buyCourse(lt.getLessonType(), user , studyPlan ,lt.isIfRequired());
				}
			}
    	}
    	return "ListStudyPlan?faces-redirect=true";
    }
     
    public void onTransfer() {
//        StringBuilder builder = new StringBuilder();
//        for(Object item : event.getItems()) {
//            builder.append(((Theme) item).getName()).append("<br />");
//        }
//        FacesMessage msg = new FacesMessage();
//        msg.setSeverity(FacesMessage.SEVERITY_INFO);
//        msg.setSummary("Items Transferred");
//        msg.setDetail(builder.toString());
//         
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        List<LessonType> lts =courses.getTarget();
//        int i = 0;
//        for(LessonType lt : lts){
//        	i += lt.getTotalClassNum();
//        }
//        this.studyPlan.setPlanTotalNum(i);
//        if(lts!=null&&lts.size()>0){
//        	this.studyPlan.setIfFinishNum(true);
//        }else{
//        	this.studyPlan.setIfFinishNum(false);
//        }
    	Double i = 0d;
    	BigDecimal i1 = new BigDecimal(Double.toString(i));
        for(CourseOfPlan cop : studyPlan.getCourses()){
        	if(cop.isIfRequired()){
        		i1 = i1.add (new BigDecimal(Double.toString(cop.getTotalClassNum())));
        	}
        }
        this.studyPlan.setFinishPlanNum(Double.valueOf(i1.toString()));
        this.studyPlan.setMinClassNum(Double.valueOf(i1.toString()));
    }
    
    public void onTransfer2(TransferEvent event) {
    	List<Examination> les = exams.getTarget();
        if(les!=null&&les.size()>0){
        	this.studyPlan.setIfFinishExam(true);
        }else{
        	this.studyPlan.setIfFinishExam(false);
        }
    }
    
    public void onblur() {
    	Double min = this.studyPlan.getMinClassNum();
    	Double max = this.studyPlan.getPlanTotalNum();
        if(max<this.studyPlan.getFinishPlanNum()||
        		this.studyPlan.getFinishPlanNum()<min){
        	JsfHelper.warn("学习计划完成学时数应该在"+min+"到"+max+"之间！");
        }
    }
 
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
     
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    } 
    
    public void up_action(FileUploadEvent event) {
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String imgId = Cat.getUniqueId();
			
			AdminNewFile.saveFile(item, ext,imgId);
			MyLogger.echo("upload executed.");
			String picUrl = "servlet/ShowImage?id=" + imgId;
			this.studyPlan.setPicture(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
    
    public void editNum(String id){
    	for(CourseOfPlan cop : studyPlan.getCourses()){
    		if(cop.getId().equals(id)){
    			cop.setTotalClassNum(cop.getTotalClassNum());
    		}
    	}
    	updateStudyPlanClassNum();
    }
    
    private void updateStudyPlanClassNum(){
    	Double i = 0d;
    	Double j = 0d;
    	BigDecimal i1 = new BigDecimal(Double.toString(i));
    	BigDecimal j1 = new BigDecimal(Double.toString(j));
    	for(CourseOfPlan cop : studyPlan.getCourses()){
        	if(cop.isIfRequired()){
        		i1=i1.add(new BigDecimal(Double.toString(cop.getTotalClassNum())));
        	}else{
        		j1=j1.add(new BigDecimal(Double.toString(cop.getTotalClassNum())));
        	}
        }
        this.studyPlan.setMinClassNum(Double.valueOf(i1.toString()));
        this.studyPlan.setPlanTotalNum(Double.valueOf(i1.add(j1).toString()));
    }
    
    public void remove(String id){
    	List<CourseOfPlan> cops = studyPlan.getCourses();
    	List<CourseOfPlan> cops2 = new LinkedList<>();
    	for(CourseOfPlan cop : cops){
    		if(!cop.getId().equals(id)){
    			cops2.add(cop);
    		}
    	}
    	Double i = 0d;
    	Double j = 0d;
        for(CourseOfPlan cop : cops2){
        	if(cop.isIfRequired()){
        		i += cop.getTotalClassNum();
        	}else{
        		j += cop.getTotalClassNum();
        	}
        }
        this.studyPlan.setCourses(cops2);
        this.studyPlan.setMinClassNum(i);
        this.studyPlan.setFinishPlanNum(i);
        this.studyPlan.setPlanTotalNum(i+j);
        CourseTreeList courseTreeList = JsfHelper.getBean("courseTreeList");
        courseTreeList.init();
    }
}
