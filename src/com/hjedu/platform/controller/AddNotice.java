package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.dao.INoticeDAO;
import com.hjedu.platform.dao.INoticeUserDAO;
import com.hjedu.platform.entity.CasusModel;
import com.hjedu.platform.entity.NoticeModel;
import com.hjedu.platform.entity.NoticeUser;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.IMessageService;
import com.huajie.exam.thread.EmailSender;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddNotice implements Serializable{
    
    NoticeModel noticeModel;
    INoticeDAO noticeDAO = SpringHelper.getSpringBean("NoticeDAO");
    INoticeUserDAO noticeUserDAO = SpringHelper.getSpringBean("NoticeUserDAO");
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    String nothing;
    private boolean flag = false;
  //主要用于设置开放的部门
    TreeNode root = new DefaultTreeNode();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    
	IBbsUserDAO cud = SpringHelper.getSpringBean("BbsUserDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    
    List<DictionaryModel> dics = new ArrayList<DictionaryModel>();
    List<DictionaryModel> selectedNodes = new ArrayList<DictionaryModel>();

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
	public List<DictionaryModel> getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(List<DictionaryModel> selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public List<DictionaryModel> getDics() {
		return dics;
	}

	public void setDics(List<DictionaryModel> dics) {
		this.dics = dics;
	}

	public String getNothing() {
        return Cat.getUniqueId();
    }
    
    public void setNothing(String nothing) {
        this.nothing = nothing;
    }
    
    public NoticeModel getNotice() {
        return this.noticeModel;
    }
    
    public void setNotice(NoticeModel noticeModel) {
        this.noticeModel = noticeModel;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    @PostConstruct
    public void init() {
        this.noticeModel = new NoticeModel();
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.noticeModel = this.noticeDAO.findNotice(idt);
            this.flag = true;
        }
        this.loadStructure();
    }
    
    public String submit_action() {
    	this.checkDepartment();
        this.noticeModel.setAdmin(ExternalUserUtil.getAdminBySession());
        this.noticeModel.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        try {
        	NoticeModel nm = this.noticeModel;
            if (!this.flag) {
                this.noticeDAO.addNotice(nm);
                this.logger.log("添加了新通知："+noticeModel.getTitle());
            } else {
                this.noticeDAO.updateNotice(nm);
                this.logger.log("修改了通知："+noticeModel.getTitle());
            }
//            List<DictionaryModel> departments = nm.getDepartments();
//            for(DictionaryModel dm : departments){
//            	System.out.println(dm.getName());
//            }
//            List<BbsUser> uslist = bbsUserDAO.findBbsUserByAdminOrderByDept(ExternalUserUtil.getAdminBySession());
//            for(BbsUser us : uslist){
//            	us.getGroupStr();
//            }
            
	        Set<String> set = null;
            if(this.noticeModel.getGroupStr()!=null){
                String[] str = this.noticeModel.getGroupStr().split(",");
        		if(str!=null){
        			set = new HashSet<>(Arrays.asList(str));
        		}
        	}
	        
	      
	        Set<BbsUser> userset = new HashSet<BbsUser>();
	        Set<String> set2 = null;
	        List<BbsUser> uslist = bbsUserDAO.findAllBbsUser(bussinessId);
	        for(BbsUser us : uslist){
	        	if(us.getGroupStr()!=null){
	        		String[] str2 = us.getGroupStr().split(";");
	            	if(str2!=null){
	            		set2 = new HashSet<>(Arrays.asList(str2));
	            		for(String str:set){
	            			if(set2.contains(str)){
	            				userset.add(us);
	            			}
	            		}
	            	} 
	        	}
	        }
	      

    	  for(BbsUser bu:userset){
    		  List<NoticeUser> nulist = noticeUserDAO.findByUserId(bu.getId(), 0, 0, null);
    		  if(nulist.size()>0){
    			  for(NoticeUser nu:nulist){
        			  if(nu.getNoticeId().equals(this.noticeModel.getId())){
        				  NoticeUser noticeUser =  nu;
        	        	  noticeUser.setNoticeId(this.noticeModel.getId());
        	        	  noticeUser.setUserId(bu.getId());
        	        	  noticeUser.setIsReaded(false);
        	        	  noticeUserDAO.updateNoticeUser(noticeUser);
            		  }else{
            			  NoticeUser noticeUser =  new NoticeUser();
                    	  noticeUser.setNoticeId(this.noticeModel.getId());
                    	  noticeUser.setUserId(bu.getId());
                    	  noticeUser.setIsReaded(false);
                    	  noticeUserDAO.addNoticeUser(noticeUser);
            		  }
    			  }
    		  
    		  }else{
    			  NoticeUser noticeUser =  new NoticeUser();
            	  noticeUser.setNoticeId(this.noticeModel.getId());
            	  noticeUser.setUserId(bu.getId());
            	  noticeUser.setIsReaded(false);
            	  noticeUserDAO.addNoticeUser(noticeUser);
    		  }
    		  
        	  
          }
	      
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ListNotice?faces-redirect=true";
    }
    
    private boolean testIfSelected(DictionaryModel dd) {
//    	List<DictionaryModel> departments = this.noticeMopdel.getDepartments();
//    	if(departments!=null){
//    		for(DictionaryModel dm : departments){
//    			if(dm.getId().equals(dd.getId())){
//    				dd.setSelected(true);
//                    return true;
//    			}else{
//    				dd.setSelected(false);
//                    return false;
//    			}
//    		}
//    	}
        if (this.noticeModel.getGroupStr() != null) {
            if (this.noticeModel.getGroupStr().trim().contains(String.valueOf(dd.getId()))) {
                dd.setSelected(true);
                return true;
            } else {
                dd.setSelected(false);
                return false;
            }
        }
    	return false;
    }
    
    public void test(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;
        List<DictionaryModel> ks = dicDAO.loadAllDescendants(dd.getId());
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
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }
    
    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.bussinessId,bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }
    
    //以下三个方法用于上级节点选取时级联到子节点
    public void checkSons(String id, boolean b) {
        DictionaryModel emm = this.dicDAO.findDictionaryModel(id,bussinessId);
        List<DictionaryModel> dics1 = this.dicDAO.loadAllDescendants(id);
        dics1.add(emm);
        this.test(dics1, root, b);
    }

    public void test(List<DictionaryModel> dds, TreeNode tn, boolean b) {
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
                test(dds, t, b);
            }
        }
    }
    
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
    
    private void checkDepartment() {
        this.fetchDictionaryModels();
        StringBuilder sb = new StringBuilder();
        Set<DictionaryModel> set = new HashSet();
        if (this.selectedNodes != null) {
            for (DictionaryModel ed : this.selectedNodes) {
                set.add(ed);
                //set.addAll(this.dicDAO.loadAllDescendants(d.getId()));
            }
        }
        for (DictionaryModel dm : set) {
            sb.append(dm.getId());
            sb.append(",");
        }
        String sbb = sb.toString().trim();
        if ("".equals(sbb)) {
            String root123 = this.bussinessId;
            sbb = root123;
        }
        this.noticeModel.setGroupStr(sbb);
//        List departments = new ArrayList<>();
//        departments.addAll(set);
//        this.noticeModel.setDepartments(departments);
    }
    
    public List<DictionaryModel> fetchDictionaryModels() {
        this.selectedNodes.clear();
        this.testDictionaryModel(this.selectedNodes, root);
        return this.selectedNodes;
    }
    
    public void testDictionaryModel(List<DictionaryModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                DictionaryModel em = (DictionaryModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                testDictionaryModel(ms, d);
            }
        }
    }
    
}
