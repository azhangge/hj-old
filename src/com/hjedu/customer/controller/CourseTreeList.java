package com.hjedu.customer.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.persistence.jpa.rs.util.list.LinkList;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.controller.AAStudyPlan;
import com.hjedu.course.controller.ChangeTypeDir;
import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.entity.CourseOfPlan;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.TreeUtil;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CourseTreeList implements Serializable {
	private static final long serialVersionUID = 1L;
	ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
	ILogService logger = SpringHelper.getSpringBean("LogService");
	ICourseTypeDAO CourseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
	ILessonTypeDAO LessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    boolean flag = false;
    CourseType courseType = new CourseType();
    List<SelectItem> ss = new ArrayList<>();
    TreeNode root;
    TreeNode root3 = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    List<CourseType> modules = new LinkedList<>();
    private List<TreeNode> nodes = new LinkedList<>();
    
    IStudyPlanDAO studyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");

	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TreeNode getRoot3() {
        return root3;
    }

    public void setRoot3(TreeNode root3) {
        this.root3 = root3;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
       this.loadStructure();
    }

    public void buildTreeNode(CourseType ct, TreeNode node, String businessId) {
        node.setExpanded(false);
        List<CourseType> cts =  CourseTypeDAO.findAllSonCourseType(ct.getId(),businessId);
        Collections.sort(cts);
        ct.setSons(cts);
        if (cts.isEmpty()) {
        	List<LessonType> lts = LessonTypeDAO.findAllLessonTypeByTagId(ct.getId(),businessId);
        	AAStudyPlan aAStudyPlan = JsfHelper.getBean("aAStudyPlan");
        	StringBuffer sbu = new StringBuffer();
        	if(aAStudyPlan.getStudyPlan()!=null&&aAStudyPlan.getStudyPlan().getCourses()!=null){
        		for(CourseOfPlan cop : aAStudyPlan.getStudyPlan().getCourses()){
        			sbu.append(cop.getLessonType().getId());
        			sbu.append(";");
        		}
        	}
        	String ids = sbu.toString();
        	for (LessonType lt : lts) {
        		CourseOfPlan cop = new CourseOfPlan();
        		cop.setName(lt.getName());
        		cop.setTotalClassNum(lt.getTotalClassNum());
        		cop.setLessonType(lt);
        		TreeNode c = new DefaultTreeNode("course",cop, node);
                if(ids.contains(lt.getId())){
                	c.setSelected(true);
                	TreeUtil.setParentExpanded(c, root);
                }
            }
            return;
        } else {
            for (CourseType cty : cts) {
                TreeNode t = new DefaultTreeNode(cty, node);
                buildTreeNode(cty, t, businessId);
            }
        }
    }
    
    public void addType(){
    	//新增
        this.flag = false;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = req.getParameter("fid");
        if (fid != null) {
        	this.courseType = new CourseType();
        	courseType.setFatherId(fid);
            System.out.println("add begin.fid:" + fid);
        }
    }
    
    public void delCt(String id){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	CourseType ct = CourseTypeDAO.findCourseType(id);
        logger.log("删除了课程分类：" + ct.getName());
        CourseTypeDAO.deleteCourseTypeAndAllDescendants(id,businessId);
        this.loadStructure();
        this.refreshChangePanel();
    }
    
    public void editCt(String id){
    	this.flag = true;
//        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String id = req.getParameter("id");
        if (id != null) {
            this.courseType = CourseTypeDAO.findCourseType(id);
            System.out.println("edit begain.id:" + id);
        }
    }
    
    public void addOrEditOk(){
      if (!flag) {
          ChangeTypeDir lcf = JsfHelper.getBean("changeTypeDir");
          courseType.setAncestors(lcf.getAncestors(courseType));
          this.CourseTypeDAO.addCourseType(courseType);
          logger.log("添加了课程分类：" + this.courseType.getName());
      } else {
          this.CourseTypeDAO.updateCourseType(courseType);
          logger.log("修改了课程分类：" + this.courseType.getName());
      }
      String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
      this.loadStructure();
      this.refreshChangePanel();
    }
    
    /**
     * 更新 移动 模块时显示的目录结构
     */
    private void refreshChangePanel() {
    	ChangeTypeDir change = JsfHelper.getBean("changeTypeDir");
        if (change != null) {
            change.init();
        }
    }

	public void loadStructure() {
		String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		root = new DefaultTreeNode();
		CourseType rootCT = CourseTypeDAO.findCourseType(businessId);
		List<CourseType> cts = this.CourseTypeDAO.findAllCourseTypeWithoutRoot(businessId);
		List<LessonType> lts = this.LessonTypeDAO.findAllEnableLessonType(businessId);
//		buildTreeNode(rootCT, root);
		buildTreeNode(rootCT, root , cts , lts, businessId);
	}
	

    private void buildTreeNode(CourseType ct, TreeNode node, List<CourseType> ourseTypes, List<LessonType> lessonTypes, String businessId) {
        node.setExpanded(false);
//        List<CourseType> cts =  CourseTypeDAO.findAllSonCourseType(ct.getId());
        List<CourseType> cts = this.findAllSonCourseType(ct.getId(),ourseTypes);
        Collections.sort(cts);
        ct.setSons(cts);
        if (cts.isEmpty()) {
//        	List<LessonType> lts = LessonTypeDAO.findAllLessonTypeByTagId(ct.getId());
        	List<LessonType> lts = this.findAllLessonTypeByTagId(ct.getId(),lessonTypes);
        	AAStudyPlan aAStudyPlan = JsfHelper.getBean("aAStudyPlan");
        	StringBuffer sbu = new StringBuffer();
        	if(aAStudyPlan.getStudyPlan()!=null&&aAStudyPlan.getStudyPlan().getCourses()!=null){
        		for(CourseOfPlan cop : aAStudyPlan.getStudyPlan().getCourses()){
        			sbu.append(cop.getLessonType().getId());
        			sbu.append(";");
        		}
        	}
        	String ids = sbu.toString();
        	for (LessonType lt : lts) {
        		CourseOfPlan cop = new CourseOfPlan();
        		cop.setName(lt.getName());
        		cop.setTotalClassNum(lt.getTotalClassNum());
        		cop.setLessonType(lt);
        		TreeNode c = new DefaultTreeNode("course",cop, node);
                if(ids.contains(lt.getId())){
                	c.setSelected(true);
                	TreeUtil.setParentExpanded(c, root);
                }
            }
            return;
        } else {
            for (CourseType cty : cts) {
                TreeNode t = new DefaultTreeNode(cty, node);
                buildTreeNode(cty, t, businessId);
            }
        }
    
	}

	private List<LessonType> findAllLessonTypeByTagId(String id, List<LessonType> lessonTypes) {
		List<LessonType> lts = new LinkedList<>();
		for(LessonType lt : lessonTypes){
			if(lt.getCourseTypeStr().contains(id)){
				lts.add(lt);
			}
		}
		return lts;
	}

	private List<CourseType> findAllSonCourseType(String id, List<CourseType> ourseTypes) {
		List<CourseType> cts = new LinkedList<>();
		for(CourseType ct : ourseTypes){
			if(ct.getFatherId().equals(id)){
				cts.add(ct);
			}
		}
		return cts;
	}

	public List<CourseType> fetchModules() {
        this.modules.clear();
        this.getSelectedDataList(this.modules, root);
        return this.modules;
    }
    
    /**
     * 获取root节点下的所有被选中的子数据
     * @param ms 存放数据的list
     * @param root 
     */
    public void getSelectedDataList(List<CourseType> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
            	CourseType em = (CourseType) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                getSelectedDataList(ms, d);
            }
        }
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
			this.courseType.setPicture(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
    
    public void addCourseOfPlan(){
    	AAStudyPlan aAStudyPlan = JsfHelper.getBean("aAStudyPlan");
    	List<CourseOfPlan> cops = new LinkedList<>();
    	StringBuffer sbf = new StringBuffer();
    	for(TreeNode node : selectedNodes){
			if(node!=null&&(node.getData() instanceof CourseOfPlan)){
				CourseOfPlan cop = (CourseOfPlan) node.getData();
				LessonType lt = cop.getLessonType();
				if(lt!=null){
					cop.setStudyPlan(aAStudyPlan.getStudyPlan());
					if(!sbf.toString().contains(lt.getId())){
						cops.add(cop);
						sbf.append(lt.getId());
						sbf.append(";");
					}
				}
			}
    	}
    	Double i = 0d;
    	Double j = 0d;
    	BigDecimal i1 = new BigDecimal(Double.toString(i));
    	BigDecimal j1 = new BigDecimal(Double.toString(j));
        for(CourseOfPlan cop : cops){
        	if(cop.isIfRequired()){
        		i1=i1.add(new BigDecimal(Double.toString(cop.getTotalClassNum())));
        	}else{
        		j1=j1.add(new BigDecimal(Double.toString(cop.getTotalClassNum())));
        	}
        }
        aAStudyPlan.getStudyPlan().setMinClassNum(Double.valueOf(i1.toString()));
        aAStudyPlan.getStudyPlan().setFinishPlanNum(Double.valueOf(i1.toString()));
        aAStudyPlan.getStudyPlan().setPlanTotalNum(Double.valueOf(i1.add(j1).toString()));
        if(cops!=null&&cops.size()>0){
        	aAStudyPlan.getStudyPlan().setIfFinishNum(true);
        }else{
        	aAStudyPlan.getStudyPlan().setIfFinishNum(false);
        }
    	aAStudyPlan.getStudyPlan().setCourses(cops);
    }
}
