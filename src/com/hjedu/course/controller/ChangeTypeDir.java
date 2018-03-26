package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ChangeTypeDir implements Serializable {
	private static final long serialVersionUID = 1L;
	ICourseTypeDAO CourseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
	ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
    TreeNode root;
    TreeNode selectedNode;
    String errStr = "";
    String businessId;

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    @PostConstruct
    public void init() {
    	this.businessId= CookieUtils.getBusinessId(JsfHelper.getRequest());
        root = new DefaultTreeNode("root", null);
        CourseType root1 = new CourseType();
        root1.setId(businessId);
        root1.setName("根分类");
        TreeNode r = new DefaultTreeNode(root1, root);
        r.setExpanded(true);
        this.buildDirTree(businessId, r);
    }

    private void buildDirTree(String id, TreeNode node) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        List<CourseType> ls = CourseTypeDAO.findAllSonCourseType(id,businessId);
        //MyLogger.echo("顶级文件夹数量：" + ls.size());
        if (ls.isEmpty()) {
            return;
        } else {
            for (CourseType d : ls) {
                TreeNode tn = new DefaultTreeNode(d, node);
                tn.setExpanded(true);
                buildDirTree(d.getId(), tn);
            }
        }
    }

    public void doChange() {
        errStr = "";
        if(this.selectedNode==null){
        	return;
        }
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        CourseType cf = (CourseType) this.selectedNode.getData();
        CourseTypeList lcf = JsfHelper.getBean("courseTypeList");
        List<CourseType> emms = lcf.fetchModules();
        if ((cf != null) && (emms != null)) {
            for (CourseType c : emms) {
                if (cf.getAncestors().contains(c.getId())) {
                    errStr = "逻辑错误，课程分类无法移动向自身或子分类！";
                    JsfHelper.error(errStr);
                    return;
                }
                String iddx = cf.getId();
                if (!c.getId().equals(iddx)&&iddx!=null) {
                    c.setFatherId(iddx);
                    this.CourseTypeDAO.updateCourseType(c);//先保存后生成祖先结构才能生效
                    c.setAncestors(getAncestors(c));
                    this.CourseTypeDAO.updateCourseType(c);
                    //更新关联课程的laberStr字段
                    this.updateLessonTypeLaberStr(c, businessId);
                }
            }
            this.init();//更新本页显示的目录结构
//            ExamModuleMag2 lcf2 = JsfHelper.getBean("courseTypeList");
            if (lcf != null) {
                lcf.loadStructure();
            }
        }
    }
    
    private void updateLessonTypeLaberStr(CourseType c, String businessId) {
    	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    	List<LessonType> lts = lessonTypeDAO.findAllLessonTypeByTagId(c.getId(),businessId);
    	for(LessonType lt : lts){
    		String[] types = lt.getCourseTypeStr().split(";");
    		String labStr = "";
    		for(int i=0;i<types.length;i++){
    			CourseType ct = CourseTypeDAO.findCourseType(types[i]);
    			labStr = labStr+ct.getAncestors();
    		}
    		lt.setLabelStr(labStr);
    		lessonTypeDAO.updateLessonType(lt);
    	}
	}

	public String getAncestors(CourseType ct) {
        List<CourseType> cfms = this.getFamilyTree(ct);
        StringBuffer sb = new StringBuffer();
        for (CourseType c : cfms) {
            sb.append(c.getId());
            sb.append(';');
        }
        return sb.toString();
    }
    
    public List<CourseType> getFamilyTree(CourseType c) {
        List<CourseType> cs = new ArrayList<>();
        if (!this.businessId.equals(c.getId())) {
            if (c != null) {
                cs.add(c);
                String tt = c.getFatherId();
                //循环找出祖先节点
                while (!this.businessId.equals(tt)) {
                    c = this.CourseTypeDAO.findCourseType(tt);
                    if (c != null) {
                        cs.add(c);
                        if (tt.equals(c.getFatherId())) {
                            break;
                        }
                        tt = c.getFatherId();
                    } else {
                        break;
                    }
                }

            }
        }
        CourseType top = new CourseType();
        top.setId(this.businessId);
        top.setName("根分类");
        cs.add(top);
        Collections.reverse(cs);
        return cs;
    }
}
