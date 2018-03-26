package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ManualPartItem;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.entity.random2.ModuleRandom2Part;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.agent.ContestAgent;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AARandom2Paper implements Serializable {

    ExamPaperRandom2 exam;
    IExamPaperRandom2DAO manualDAO = SpringHelper.getSpringBean("ExamPaperRandom2DAO");
    IRandom2PaperPartDAO partDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
    ExamModule2Service module2DAO = SpringHelper.getSpringBean("ExamModule2Service");
    IModuleRandom2PartDAO mpartDAO = SpringHelper.getSpringBean("ModuleRandom2PartDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    private boolean flag = false;

    Random2PaperPart newPart = new Random2PaperPart();

    TreeNode root2 = new DefaultTreeNode();
    List<ExamModuleModel> modules = new ArrayList();
    //TreeNode root = new DefaultTreeNode();
    List<ModuleRandom2Part> mparts = new ArrayList();//存放PART和MODULE的对应关系，其中包含了每个模块各种题型所应的大题及试题数量
    String businessId;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamPaperRandom2 getExam() {
        return exam;
    }

    public void setExam(ExamPaperRandom2 exam) {
        this.exam = exam;
    }

    public List<ExamModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }

    public Random2PaperPart getNewPart() {
        return newPart;
    }

    public void setNewPart(Random2PaperPart newPart) {
        this.newPart = newPart;
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public List<ModuleRandom2Part> getMparts() {
        return mparts;
    }

    public void setMparts(List<ModuleRandom2Part> mparts) {
        this.mparts = mparts;
    }

    @PostConstruct
    public void init() {
        String idt = JsfHelper.getRequest().getParameter("id");
        this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if (idt != null) {
            this.flag = true;
            this.exam = this.manualDAO.findExamPaperRandom2(idt);
        } else {
            //至少保证试卷中有一个PART
            exam = new ExamPaperRandom2();
            List<Random2PaperPart> parts = new ArrayList();
            Random2PaperPart tempPart = new Random2PaperPart();
            tempPart.setName("单选题");
            tempPart.setPaper(exam);
            parts.add(tempPart);
            Random2PaperPart tempPart2 = new Random2PaperPart();
            tempPart2.setName("多选题");
            tempPart2.setPaper(exam);
            parts.add(tempPart2);
            exam.setParts(parts);
            //将新部分的关联试卷设为本试卷

        }
        newPart.setPaper(exam);
        //newPart.setItems(new ArrayList());
        this.loadStructure2();
    }

    public String addNewPart() {
        exam.getParts().add(newPart);
        newPart = new Random2PaperPart();
        newPart.setPaper(exam);
        //newPart.setItems(new ArrayList());
        return null;
    }

    public String deletePart(String id) {
        Random2PaperPart pp = null;
        for (Random2PaperPart p : exam.getParts()) {
            if (p.getId().equals(id)) {
                pp = p;
                break;
            }
        }
        if (pp != null) {
            this.exam.getParts().remove(pp);
            this.partDAO.deleteRandom2PaperPart(id);
        }
        return null;
    }

    private void loadStructure2() {
        this.root2 = new DefaultTreeNode();
        ExamModuleModel dm = module2DAO.findExamModuleModel(this.businessId);
        test2(dm, root2);
        this.modules = null;
        this.modules = dm.getSons();

    }

    public void test2(ExamModuleModel dd, TreeNode node) {
        if (!flag) {
            //如果是新增考试，则新建ModuleExamination并绑定exam

        } else {
            //如果是修改考试，则在已经存在的ModuleExamination绑定exam

        }
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        List<ExamModuleModel> ks = module2DAO.loadAllDescendants(dd.getId());
        node.setExpanded(true);
        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
        //
        List<ExamModuleModel> ls = module2DAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (ExamModuleModel d : ls) {
                if (MenuTree.testIfShow(d)) {//验证是否此管理员对本模块有权限
                    ModuleRandom2Part mp = this.buildAModuleRandom2Part(d);
                    TreeNode t = new DefaultTreeNode(mp, node);
                    test2(d, t);
                }
            }
        }
    }

    /**
     *
     * @param d 传入一个试题模块对象
     * @return 获得一个试题模块和分段的连接中间对象
     */
    private ModuleRandom2Part buildAModuleRandom2Part(ExamModuleModel d) {
        if (!flag) {
            ModuleRandom2Part mp = new ModuleRandom2Part();
            mp.setModule(d);
            mp.setPaper(exam);
            this.mparts.add(mp);
            return mp;
        } else {
            ModuleRandom2Part mp = mpartDAO.findModuleRandom2PartByExamAndModule(this.exam.getId(), d.getId());
            //mp.setModule(d);
            if (mp == null) {
                mp = new ModuleRandom2Part();
                mp.setModule(d);
                mp.setPaper(exam);
            }
            this.mparts.add(mp);
            return mp;
        }
    }

    private int computePartItemNum(String pid, List<ModuleRandom2Part> mparts) {
        int num = 0;
        for (ModuleRandom2Part p : mparts) {
            if (pid.equals(p.getChoicePartId())) {
                num += p.getChoiceWeight();
            }
            if (pid.equals(p.getMchoicePartId())) {
                num += p.getMultiChoiceWeight();
            }
            if (pid.equals(p.getFillPartId())) {
                num += p.getFillWeight();
            }
            if (pid.equals(p.getJudgePartId())) {
                num += p.getJudgeWeight();
            }
            if (pid.equals(p.getEssayPartId())) {
                num += p.getEssayWeight();
            }
            if (pid.equals(p.getFilePartId())) {
                num += p.getFileWeight();
            }
            if (pid.equals(p.getCasePartId())) {
                num += p.getCaseWeight();
            }
        }
        return num;
    }

    private void resetMParts() {
        List<Random2PaperPart> parts = exam.getParts();
        for (Random2PaperPart p : parts) {
            int num = computePartItemNum(p.getId(), mparts);
            p.setItemNum(num);
            //p.setTotalScore(num*p.getScore());
            //System.out.println(p.getPaper());
        }
        //以下代码彻底杜绝items里边有重复试题的可能
        //System.out.println("待保存题目总数："+items.size());

        if (!flag) {
            //mpart与exam关联，在保存mpart前应该先保存exam
            this.manualDAO.addExamPaperRandom2(exam);
        }
        for (ModuleRandom2Part p : mparts) {
            if (flag) {
                //this.manualDAO.updateExamination(exam);
                this.mpartDAO.updateModuleRandom2Part(p);
            } else {
                try {

                    this.mpartDAO.addModuleRandom2Part(p);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            //System.out.println(p.getPaper());
        }
        for (Random2PaperPart p : parts) {

            //System.out.println("大题"+p.getName()+"中小题数："+p.getItemNum());
        }
    }

    public String done() {
        this.resetMParts();
        //this.paper.setModulePapers(mes);
        double a = getTotalScore(mparts, exam);
        exam.setTotalScore(a);
        System.out.println("总分为："+a);
        if (flag) {
            this.manualDAO.updateExamPaperRandom2(exam);
            ExamPaperPool.refreshPaper(exam.getId(), "random2");
            this.logger.log("修改了随机试卷2：" + exam.getName());
        } else {
        	List list = new ArrayList<>();
        	list.add(ExternalUserUtil.getAdminBySession());
        	exam.setAdmins(list);
            this.logger.log("添加了随机试卷2：" + exam.getName());
            exam.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.manualDAO.updateExamPaperRandom2(exam);//因为在resetParts中已经将examination保存了，此处只更新
        }
        
        //将竞赛中的所有随机试卷更新
        ContestAgent.refreshStub(exam);
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        //更新考试缓存
        ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
        examService.reBuildCache();
        return "ListRandom2Paper?faces-redirect=true";

    }
    
    public double getTotalScore(List<ModuleRandom2Part> mparts,ExamPaperRandom2 exam){
    	 //计算总分
        double a = 0;
        //大题对象（包含各种题型的分值）
        for(ModuleRandom2Part mp : mparts){
        	for(Random2PaperPart mi : exam.getParts()){
        		//单选
        		if(mi.getId().equals(mp.getChoicePartId())){
        			a += mi.getChoiceScore()*mp.getChoiceWeight();
        		}
        		//多选
        		if(mi.getId().equals(mp.getMchoicePartId())){
        			a += mi.getMultiChoiceScore()*mp.getMultiChoiceWeight();
        		}
        		//填空
        		if(mi.getId().equals(mp.getFillPartId())){
        			a += mi.getFillScore()*mp.getFillWeight();
        		}
        		//判断
        		if(mi.getId().equals(mp.getJudgePartId())){
        			a += mi.getJudgeScore()*mp.getJudgeWeight();
        		}
        		//问答
        		if(mi.getId().equals(mp.getEssayPartId())){
        			a += mi.getEssayScore()*mp.getEssayWeight();
        		}
        		//文件
        		if(mi.getId().equals(mp.getFilePartId())){
        			a += mi.getFileScore()*mp.getFileWeight();
        		}
        		//综合
//        		if(mi.getId().equals(mp.getCasePartId())){
//        			a += mi.getcas()*mp.getCaseWeight();
//        		}
        	}
        }
        return a;
    }
}
