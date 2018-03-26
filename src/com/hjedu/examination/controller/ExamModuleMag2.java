package com.hjedu.examination.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IModuleExamPartDAO;
import com.hjedu.examination.dao.IModuleExaminationDAO;
import com.hjedu.examination.dao.IModuleModule2PartDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.entity.module2.ModuleModule2Part;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.platform.controller.NewFile;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.impl.ImageService;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamModuleMag2 implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO dicDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
    List<ExamModuleModel> dics = new ArrayList();
    ExamModuleModel dic = new ExamModuleModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    List<ExamModuleModel> selectedModules = new LinkedList();
    List<AdminInfo> admins = new LinkedList();

    //主要用于设置开放的部门
    TreeNode root3 = new DefaultTreeNode();
    IDictionaryDAO dicDAO3 = SpringHelper.getSpringBean("DictionaryDAO");
    //transient ExamDepartmentService dicDAO3 = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics3 = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();
    
    private String rootId;
    
    public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public ExamModuleModel getDic() {
        return dic;
    }

    public void setDic(ExamModuleModel dic) {
        this.dic = dic;
    }

    public List<ExamModuleModel> getDics() {
        return dics;
    }

    public void setDics(List<ExamModuleModel> dics) {
        this.dics = dics;
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

    public List<DictionaryModel> getDics3() {
        return dics3;
    }

    public void setDics3(List<DictionaryModel> dics3) {
        this.dics3 = dics3;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<DictionaryModel> getSelectedNodes2() {
        return selectedNodes2;
    }

    public void setSelectedNodes2(List<DictionaryModel> selectedNodes2) {
        this.selectedNodes2 = selectedNodes2;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public List<ExamModuleModel> getSelectedModules() {
        return selectedModules;
    }

    public void setSelectedModules(List<ExamModuleModel> selectedModules) {
        this.selectedModules = selectedModules;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	this.rootId = businessId;
    	//加载试题模块
        this.loadStructure();
        //是否加载所有管理员？
        this.loadAdmins();
        //加载部门
        this.loadStructure3();
    }

    public void loadAdmins() {
        this.admins = this.adminDAO.findAllAdmin();
        List<AdminInfo> exs = this.dic.getAdmins();
        if (exs != null && this.admins != null) {
            for (AdminInfo e : admins) {
                for (AdminInfo ex : exs) {
                    if (ex.getId().equals(e.getId())) {
                        e.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    public List<AdminInfo> fetchAdmins() {
        List<AdminInfo> exxs = new ArrayList();
        if (this.admins != null) {
            for (AdminInfo ex : this.admins) {
                if (ex.isSelected()) {
                    exxs.add(ex);
                }
            }
        }
        return exxs;
    }

    private boolean testIfSelected(DictionaryModel dd) {
        if (this.dic.getGroupStr() != null) {
            if (this.dic.getGroupStr().trim().contains(dd.getId())) {
                dd.setSelected(true);
                return true;
            }
        }
        return false;
    }

    private void loadStructure3() {
        this.root3 = new DefaultTreeNode();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        DictionaryModel dm = dicDAO3.findDictionaryModel(businessId);
        this.dics3 = null;
        if (dm != null) {
			test5(dm, root3);
			this.dics3 = dm.getSons();
		}

    }

    public void test5(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;
        List<DictionaryModel> ks = dicDAO3.loadAllDescendants(dd.getId());
        for (DictionaryModel ddm : ks) {
            if (this.testIfSelected(ddm)) {
                k = true;
                break;
            }
        }
        if (k) {
            node.setExpanded(true);
        }
        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
        if (this.testIfSelected(dd)) {
            //System.out.println(dd.getId() + dd.getName());
            //node.setSelected(true);
            node.setExpanded(true);
        } else {
            node.setSelected(false);
        }
        //
        List<DictionaryModel> ls = dicDAO3.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test5(d, t);
            }
        }
    }

    //以下三个方法用于上级节点选取时级联到子节点
    public void checkSons(String id, boolean b) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        DictionaryModel emm = this.dicDAO3.findDictionaryModel(businessId);
        List<DictionaryModel> dics1 = this.dicDAO3.loadAllDescendants(id);
        dics1.add(emm);
        this.test3(dics1, root3, b);
    }

    /**
     * 将root3中所有符合条件的叶子元素选中
     *
     * @param dds
     * @param tn
     * @param b
     */
    public void test3(List<DictionaryModel> dds, TreeNode tn, boolean b) {
        //List<ExamModuleModel> mos = this.role.getModules();
        DictionaryModel top = (DictionaryModel) tn.getData();
        if (this.testIfContain(dds, top)) {
            top.setSelected(b);
        }
        List<TreeNode> tns = tn.getChildren();
        if (tns == null) {
            return;
        } else {
            for (TreeNode t : tns) {
                test3(dds, t, b);
            }
        }
    }

    /**
     * 测试本列表中是否包含某对象
     *
     * @param dds
     * @param emm
     * @return
     */
    private boolean testIfContain(List<DictionaryModel> dds, DictionaryModel emm) {
        for (DictionaryModel em : dds) {
            if (em != null && emm != null) {
                if (em.getId().equals(emm.getId())) {
                    //break;
                    return true;
                }
            }
        }
        return false;
    }
    //级联结束

    public List<ExamModuleModel> fetchModules() {
        this.selectedModules.clear();
        this.test2(this.selectedModules, root);
        return this.selectedModules;
    }

    public void loadStructure() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.root = new DefaultTreeNode();
        ExamModuleModel dm = dicDAO.findExamModuleModel(businessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }

    public void test(ExamModuleModel dd, TreeNode node) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        node.setExpanded(true);
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        List<ExamModuleModel> ls = null;
        if(asb.isIfSuper()){
        	ls = dicDAO.findAllSonExamModuleModel(dd.getId(),businessId);
        }else{
        	ls = dicDAO.findAllSonExamModuleModelAndAdmin(dd.getId(),ExternalUserUtil.getAdminBySession(),businessId);
        }
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    public void test2(List<ExamModuleModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                ExamModuleModel em = (ExamModuleModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                test2(ms, d);
            }
        }
    }

    public String addDic() {
        this.flag = false;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = req.getParameter("fid");
        if (fid != null) {
            this.dic = new ExamModuleModel();
            dic.setFatherId(fid);
            System.out.println("add begain.fid:" + fid);
        }
        return null;
    }
    
    /**
     * 点击编辑后动作
     *
     * @return
     */
    public String editDic() {
        this.flag = true;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        if (id != null) {
            this.dic = dicDAO.findExamModuleModel(id);
            System.out.println("edit begain.id:" + id);
        }
        this.loadAdmins();
        this.loadStructure3();
        return null;
    }

    public String addOrEditOk() {
//        this.dic.setAdmins(this.fetchAdmins());
    	//默认当前用户用该模块权限
    	List list = new ArrayList<>();
    	list.add(ExternalUserUtil.getAdminBySession());
    	this.dic.setAdmins(list);
        this.dic.setGroupStr(this.checkDepartment());

        if (!flag) {
        	dic.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.dicDAO.addExamModuleModel(dic);//如果不保存module，则无法寻找其祖先节点
            dic.setAncestors(complex.genAncestors(dic.getId()));
            logger.log("添加了试题模块：" + this.dic.getName());
        } else {
            this.dicDAO.updateExamModuleModel(dic);
            logger.log("修改了试题模块：" + this.dic.getName());
        }
        //this.dicDAO.updateExamModuleModel(dic);
        this.loadStructure();
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        asb.refreshAdmin();
        //Menu menu = JsfHelper.getBean("menu");
        //menu.loadStructure();
        this.refreshCache();
        this.refreshChangePanel();

        return null;
    }

    //以下三方法用于提交考试前获取考试开放的部门
    public List<DictionaryModel> fetchDictionaryModels() {
        this.selectedNodes2.clear();
        this.testDictionaryModel2(this.selectedNodes2, root3);
        return this.selectedNodes2;
    }

    /**
     * 加载所有选中元素到ms
     *
     * @param ms
     * @param root3
     */
    public void testDictionaryModel2(List<DictionaryModel> ms, TreeNode root3) {
        List<TreeNode> ls = root3.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                DictionaryModel em = (DictionaryModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                testDictionaryModel2(ms, d);
            }
        }
    }

    /**
     * 获取模块对应的部门
     */
    private String checkDepartment() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.fetchDictionaryModels();//全部选中项加载进入selectedNodes2
        StringBuilder sb = new StringBuilder();
        Set<DictionaryModel> set = new HashSet();
        if (this.selectedNodes2 != null) {
            for (DictionaryModel ed : this.selectedNodes2) {
                set.add(ed);
                //set.addAll(this.dicDAO.loadAllDescendants(d.getId()));
            }
        }
        for (DictionaryModel dm : set) {
            sb.append(dm.getId());
            sb.append(";");
        }
        String sbb = sb.toString().trim();
        if ("".equals(sbb)) {
            String root123 = businessId;
            sbb = root123;
        }
        //this.dic.setGroupStr(sbb);
        //MyLogger.println(sbb);
        return sbb;
    }
    //获取结束

    

    public String delDic(String id) {
        this.dic = dicDAO.findExamModuleModel(id);
        logger.log("删除了试题模块：" + this.dic.getName());
        dicDAO.deleteExamModuleModelAndAllDescendants(id);

        System.out.println("dic deleted.id:" + id);
        this.loadStructure();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        //Menu menu = JsfHelper.getBean("menu");
        //menu.loadStructure();
        this.refreshCache();
        this.refreshChangePanel();

        return null;
    }
    
    /**
     * 生成模块题目json文件
     * @param id
     * @return
     */
    public String createJsonFileOfModule(ExamModuleModel em) {
    	FacesMessage fm = new FacesMessage();
    	fm.setSeverity(FacesMessage.SEVERITY_INFO);
    	if(JsonUtil.createJsonFileByModule(JsfHelper.getRequest(),em)){
    		List<ExamModuleModel> le = new LinkedList<>();
    		le.add(em);
    		createAllModuleExamination(le);
    		fm.setSummary("生成文件成功！");
    	}else{
    		fm.setSummary("生成文件失败，请联系后台人员查看原因！");
    	}
    	FacesContext.getCurrentInstance().addMessage("", fm);
        return null;
    }
    
    public void createJsonFileOfAllModule(){
    	FacesMessage fm = new FacesMessage();
    	fm.setSeverity(FacesMessage.SEVERITY_INFO);
    	try {
    		List<ExamModuleModel> le = dicDAO.findAllExamModuleModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        	for(ExamModuleModel e : le){
        		JsonUtil.createJsonFileByModule(JsfHelper.getRequest(),e);
        	}
        	createAllModuleExamination(le);
        	fm.setSummary("生成文件成功！");
		} catch (Exception e) {
			e.printStackTrace();
			fm.setSummary("生成文件失败，请联系后台人员查看原因！");
		}
    	FacesContext.getCurrentInstance().addMessage("", fm);
    }

    /**
     * 更新 移动 模块时显示的目录结构
     */
    private void refreshChangePanel() {
        ChangeModuleDir change = JsfHelper.getBean("changeModuleDir");
        if (change != null) {
            change.init();
        }
    }

    private void refreshCache() {
        ExamModule2Service moduleService = SpringHelper.getSpringBean("ExamModule2Service");
        moduleService.reBuildCache();
    }
    
    public void createAllModuleExamination(List<ExamModuleModel> le){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	IModuleModule2PartDAO mpartDAO = SpringHelper.getSpringBean("ModuleModule2PartDAO");
    	IModuleExamPartDAO partDAO = SpringHelper.getSpringBean("ModuleExamPartDAO");
    	IModuleExaminationDAO manualDAO = SpringHelper.getSpringBean("ModuleExaminationDAO");
    	IDictionaryDAO dictionaryDAO = SpringHelper.getSpringBean("DictionaryDAO");
    	List<DictionaryModel> ld = dictionaryDAO.findLeafDictionaryModel(businessId);
    	ExamModule2Service moduleService = SpringHelper.getSpringBean("ExamModule2Service");
    	String groupStr = "";
    	for(DictionaryModel l : ld){
    		groupStr +=l.getId()+";";
    	}
    	for(ExamModuleModel em : le){
    		//至少保证试卷中有一个PART
    		ModuleExamination2 exam = null;
    		List<ModuleExam2Part> parts = new ArrayList<>();
    		List<ModuleModule2Part> parts2 = new ArrayList<>();
    		ModuleExam2Part tempPart = null;
    		ModuleModule2Part mp = null;
    		if(em.getExam()==null){
    			exam = new ModuleExamination2();
    			tempPart = new ModuleExam2Part();
    			mp = new ModuleModule2Part();
    			exam.setIfOpen(true);
    			exam.setTotalNum((int)em.getTotalNum());
    			exam.setScorePaid(0);
    			exam.setGroupStr(groupStr);
    			exam.setDisplayMode("multiple");
    			exam.setModule(em);
    			tempPart.setName("第一部分");
    			tempPart.setExam(exam);
    			parts.add(tempPart);
    			exam.setParts(parts);
    			mp.setModule(em);
    			mp.setExam(exam);
    			em.setExam(exam);
    			parts2.add(mp);
    			exam.setMparts(parts2);
    			mp.setChoicePartId(tempPart.getId());
    			mp.setMchoicePartId(tempPart.getId());
    			mp.setJudgePartId(tempPart.getId());
    			manualDAO.addExamination(exam);
    			partDAO.addModuleExamPart(tempPart);
//    			mpartDAO.addModuleModule2Part(mp);
    			dicDAO.updateExamModuleModel(em);
    		}else{
    			exam = manualDAO.findExaminationByModule(em.getId());
    			if(exam != null){
    				for(ModuleModule2Part mpart : exam.getMparts()){
        				if(mpart.getChoicePartId()==null||mpart.getChoicePartId().equals("")){
        					mpart.setChoicePartId(exam.getParts().get(0).getId());
        					mpart.setMchoicePartId(exam.getParts().get(0).getId());
        					mpart.setJudgePartId(exam.getParts().get(0).getId());     					
//        	    			if(exam.getMparts().size()<1){
//            				parts2 = mpartDAO.findModuleModule2PartByExam(exam.getId());
//            				exam.setMparts(parts2);
//            			}
//            			if(exam.getParts().size()<1){
//            				parts = partDAO.findAllModuleExamPartByExam(exam.getId());
//            				exam.setParts(parts);
//            			}
            			exam.setIfOpen(true);
            			exam.setDisplayMode("multiple");
            			exam.setGroupStr(groupStr);
            			exam.setTotalNum((int)em.getTotalNum());
            			exam.setScorePaid(0);
            			manualDAO.updateExamination(exam);
        				}
        			}
    			}
    		}
    	}
    	moduleService.reBuildCache();
    }

}
