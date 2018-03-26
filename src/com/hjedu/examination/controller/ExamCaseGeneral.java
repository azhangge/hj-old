package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemFillBlock;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.examination.util.ExamCaseSubmitUtil;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.thread.ExamCaseRanker;
import com.huajie.exam.thread.ExamCaseSaver;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseGeneral implements Serializable {
 ExaminationService examinationDAO = SpringHelper.getSpringBean("ExaminationService");

    //IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    ExamCase examCase = null;
    Date begainTime;
    long initialPassed = 0L;//记录当前考试本来已经耗掉的时间
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    int redirectIndex = -1;
    int total = 0;
    private boolean submited = false;
    boolean flag = false;
    
    int currentIndex = 1;
    ExamCaseItemAdapter currentAdapter;
    Map<Integer, ExamCaseItemAdapter> adapterMap = new HashMap();
    boolean curentIfRight = false;
    
    String secretUrn = "";//用于记录当前考生用户名，用以防止用户名万一丢失
    String secretCaseId = "";
    
    public String getSecretUrn() {
        return secretUrn;
    }
    
    public void setSecretUrn(String secretUrn) {
        this.secretUrn = secretUrn;
    }
    
    public String getSecretCaseId() {
        return secretCaseId;
    }
    
    public void setSecretCaseId(String secretCaseId) {
        this.secretCaseId = secretCaseId;
    }
    
    public Date getBegainTime() {
        return begainTime;
    }
    
    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public int getRedirectIndex() {
        return redirectIndex;
    }
    
    public void setRedirectIndex(int redirectIndex) {
        this.redirectIndex = redirectIndex;
    }
    
    public ExamCase getExamCase() {
        return examCase;
    }
    
    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }
    
    public long getRemainingTime() {
        return remainingTime;
    }
    
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
    
    public ExamCaseItemAdapter getCurrentAdapter() {
        return currentAdapter;
    }
    
    public void setCurrentAdapter(ExamCaseItemAdapter currentAdapter) {
        this.currentAdapter = currentAdapter;
    }
    
    public Map<Integer, ExamCaseItemAdapter> getAdapterMap() {
        return adapterMap;
    }
    
    public void setAdapterMap(Map<Integer, ExamCaseItemAdapter> adapterMap) {
        this.adapterMap = adapterMap;
    }
    
    public boolean isCurentIfRight() {
        return curentIfRight;
    }
    
    public void setCurentIfRight(boolean curentIfRight) {
        this.curentIfRight = curentIfRight;
    }
    
    public String getErrStr() {
        return errStr;
    }
    
    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }
    
    public String getRedirectId() {
        return redirectId;
    }
    
    public void setRedirectId(String redirectId) {
        this.redirectId = redirectId;
    }
    
    /**
     * 初始化
     * 以下情形会被执行：1、带参数正常进入；2、view state丢失时会试图重建
     * 交卷失败时应尝试从request map中直接解析数据
     */
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String exam_type = request.getParameter("exam_type");//考试类型
        String case_id = request.getParameter("case_id");//恢复的考试应该有此参数
        String exam_id = request.getParameter("exam_id");//新考试应该有此参数
        //若这两个参数都为0，说明是因为各种原因view_state丢失而造成本类再次初始化
        //本类再次初始化时JSF会尝试建立一个新的空的对象，考生作答信息丢失殆尽，因该由人工处理
        //此时应该从REQUEST尽量调取参数数据再次提交考试,原考试应该可以从缓存调出
        if (case_id == null && exam_id == null) {
            Map<String, String[]> reqMap = request.getParameterMap();
            ExamCaseSubmitUtil.submitFromRequestMap(reqMap);
            return;
        } 
        //未登录用户的请求拒绝继续执行（view state丢失的用户已经进入上述处理流程）
        //由于任何生成的考试实例都会保存入缓存，此步也可以防止机器刷题导致内存崩溃
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs.getUsr() == null) {
            return;
        } else {
            //设置保险用户名
            this.secretUrn = cs.getUsr().getUsername();
        }
        
        begainTime = new Date();
        this.adapterMap = new HashMap();
        if (case_id != null) {//恢复老考试
            IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
            examCase = examCaseService.retrieveExamCase(case_id);

            //this.examCaseService.resumeExamCase(examCase);
            //examCase.setGenTime(this.begainTime);
            this.flag = true;
            this.checkDoneQuestion();//只针对恢复的保存过的考试
            this.initialPassed = examCase.getTimePassed();//记录本来已经消耗掉的时间
        } else {//设置新考试

            String id = exam_id;
            if (exam_id == null) {
                return;
            }
            
            Examination examt = this.examinationDAO.findExamination(id);

            //试卷池中是否有试题，是否应该新生成或从实例池中取出等逻辑由试卷池负责
            this.examCase = ExamPaperPool.takePaper(id);//从试卷池中取一份试卷
            
            if (examt.getManualPaper() !=null) {
            	this.examCase.setTotalFullScore(examt.getManualPaper().getTotalScore());
			}else if (examt.getRandomPaper() !=null) {
            	this.examCase.setTotalFullScore(examt.getRandomPaper().getTotalScore());
			}else if (examt.getRandom2Paper() !=null) {
            	this.examCase.setTotalFullScore(examt.getRandom2Paper().getTotalScore());
			}

            examCase.setDisplayMode(examt.getDisplayMode());
            this.examCase.setUser(cs.getUsr());
            
            MyLogger.echo("ExamCase initialized.");
            
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                //bu.setScore(bu.getScore() - examCase.getExamination().getScorePaid());
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("考试消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
                //this.userDAO.updateBbsUser(bu);
            }
            
        }
        this.computeRemainingTime();
        this.examCase.setIfSimulate(exam_type!=null&&exam_type.equals("1")?true:false);
        this.examCase.setIp(IpHelper.getRemoteAddr(request));
        iussService.loginExam(examCase);
        this.secretCaseId = this.examCase.getId();//设置秘密caseId
        this.loadItemIndex();//如果是新考试，则加载试题的编号
        this.currentAdapter = this.adapterMap.get(currentIndex);
        //加载完成后直接加入缓存，未保存过的成绩stat为''
        this.cacheService.addExamCase(examCase);
    }

    /**
     * 加载试题的编号
     */
    public void loadItemIndex() {
        //以下代码加载试题编号
        List<VirtualExamPart> parts = this.examCase.getVparts();
        for (VirtualExamPart part : parts) {
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    adapter.getChoiceItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("mchoice")) {
                    adapter.getMultiChoiceItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("fill")) {
                    adapter.getFillItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("judge")) {
                    adapter.getJudgeItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("essay")) {
                    adapter.getEssayItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("file")) {
                    adapter.getFileItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("case")) {
                    adapter.getCaseItem().setIndex(total + 1);//加综合题的顺序设置其中小题的第一个，以方便恢复顺序
                    //综合题的试题编号保存在item中，adapter不能说明问题
                    for (ExamCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                }
            }
        }
    }

    /**
     * 计算剩余时间，主要用于保存的试卷恢复与交卷时验证
     */
    public void computeRemainingTime() {
        long n = System.currentTimeMillis();//取得当前时间
        long o = this.begainTime.getTime();//取得本次考试开始时间
        long len = this.examCase.getExamination().getTimeLen() * 60;//
        long past = (long) (n - o) / 1000;
        this.remainingTime = len - past - examCase.getTimePassed();
    }

    /**
     * 保存试卷
     *
     * @param str
     * @return
     */
    public String saveExamCase(String str) {
        this.flag = true;
        if (!this.submited) {
            long n = System.currentTimeMillis();
            long o = this.begainTime.getTime();
            long past = (long) (n - o) / 1000;
            examCase.setTimePassed(this.initialPassed + past);//记录本来已经耗掉的时间+本次考试耗掉的时间
            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
            if ("auto".equals(str)) {
                JsfHelper.info("自动保存考试成功，如果考试异常中断，请在综合考试成绩查询中恢复！");
            } else {
                JsfHelper.info("考试保存成功，如果考试异常中断，请在综合考试成绩查询中恢复！");
            }
        }
        return null;
    }

    /**
     * 恢复页面左边做题进度的显示情况
     *
     * @return
     */
    public String checkDoneQuestion() {
        //System.out.println(type + ":" + id);

        List<ExamCaseItemChoice> chs1 = this.examCase.getChoiceItems();
        for (ExamCaseItemChoice c : chs1) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<ExamCaseItemMultiChoice> chs2 = this.examCase.getMultiChoiceItems();
        for (ExamCaseItemMultiChoice c : chs2) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
            }
        }
        
        List<ExamCaseItemFill> chs3 = this.examCase.getFillItems();
        for (ExamCaseItemFill c : chs3) {
            List<ExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (ExamCaseItemFillBlock e : ls) {
                if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                    tt = true;
                    break;
                }
            }
            if (!tt) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            } else {
                c.setDone(false);
            }
        }
        
        List<ExamCaseItemJudge> chs4 = this.examCase.getJudgeItems();
        for (ExamCaseItemJudge c : chs4) {
            if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<ExamCaseItemEssay> chs5 = this.examCase.getEssayItems();
        for (ExamCaseItemEssay c : chs5) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<ExamCaseItemFile> chs6 = this.examCase.getFileItems();
        for (ExamCaseItemFile c : chs6) {
            if (c.isAttached()) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());

            }
        }
        
        List<ExamCaseItemCase> chs7 = this.examCase.getCaseItems();
        if (chs7 != null) {
            for (ExamCaseItemCase cc : chs7) {
                List<ExamCaseItemChoice> chs11 = cc.getChoiceItems();
                for (ExamCaseItemChoice c : chs11) {
                    if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    }
                }
                
                List<ExamCaseItemMultiChoice> chs21 = cc.getMultiChoiceItems();
                for (ExamCaseItemMultiChoice c : chs21) {
                    if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                    }
                }
                
                List<ExamCaseItemFill> chs31 = cc.getFillItems();
                for (ExamCaseItemFill c : chs31) {
                    List<ExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder us = new StringBuilder();
                    boolean tt = false;
                    for (ExamCaseItemFillBlock e : ls) {
                        if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                            tt = true;
                            break;
                        }
                    }
                    if (!tt) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                }
                
                List<ExamCaseItemJudge> chs41 = cc.getJudgeItems();
                for (ExamCaseItemJudge c : chs41) {
                    if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    }
                }
                
                List<ExamCaseItemEssay> chs51 = cc.getEssayItems();
                for (ExamCaseItemEssay c : chs51) {
                    if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    }
                }
            }
        }
        
        return null;
    }

    /**
     * 本就去用于文件题上传文件
     *
     * @param event
     * @return
     */
    public List up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String str = "";
            Long tl = item.getSize();
            //this.project2.setRealLength(tl);
            Float k = 1024F;
            Float m = 1024 * 1024F;
            Float g = 1024 * 1024 * 1024F;
            if (tl < k) {
                str = tl.toString() + "B";
            } else if (k <= tl && tl < m) {
                Float te = Math.round(tl / k * 100) / 100.00F;
                str = te.toString() + "KB";
            } else if (tl >= m && tl < g) {
                Float te = Math.round(tl / m * 100) / 100.00F;
                str = te.toString() + "MB";
            } else {
                Float te = Math.round(tl / g * 100) / 100.00F;
                str = te.toString() + "GB";
            }
            //this.project2.setFileSize(str);
            String n = item.getFileName();
            int l1 = n.lastIndexOf("\\");
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String name = n.substring(l1 + 1, l2);
            //HttpServletRequest request = JsfHelper.getRequest();
            String imgId = UUID.randomUUID().toString();
            
            UIComponent ui = event.getComponent();
            ExamCaseItemFile nm = null;
            while ((ui != null) && (!(ui instanceof UIData))) {
                ui = ui.getParent();
            }
            if ((ui != null) && (ui instanceof UIData)) {
                Object rowData = ((UIData) ui).getRowData();
                if (rowData instanceof ExamCaseItemFile) {
                    nm = (ExamCaseItemFile) rowData;
                }
            }
            if (nm != null) {
                imgId = nm.getId();
            } else {
                MyLogger.echo("未取得正在操作的ExamCaseItemFile的ID");
            }
            
            this.fileQuestionDAO.saveFile(item.getInputstream(), imgId);
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * 提交试卷 开启交卷队列时加入交卷队列，未开启时先由service处理，再加入存储队列
     * 当各种原因导致view state丢失时，本方法将不会被执行到，应直接从init处理尝试恢复request map
     *
     * @return
     */
    public String submitExamCase() {
        String result = "ExamCaseResult2?faces-redirect=true";
        try {
            HttpServletRequest request = JsfHelper.getRequest();
            
            Map<String, String[]> reqMap = request.getParameterMap();
            //MyLogger.explainMap(reqMap);

            ClientSession cs = JsfHelper.getBean("clientSession");
            System.out.println(this.secretUrn + " begain to submit the examcase.");
            if (cs == null) {//说明session已经丢失
                IBbsUserDAO userDAO2 = SpringHelper.getSpringBean("BbsUserDAO");
                BbsUser bu2 = userDAO2.findBbsUserByUrn(this.secretUrn);
                examCase.setUser(bu2);
            }
            examCase.setStat("submitted");
            //计算消耗的时间
            long n = System.currentTimeMillis();
            long o = this.begainTime.getTime();
            long past = (long) (n - o) / 1000;
            examCase.setTimePassed(examCase.getTimePassed() + past);
            //计算消耗时间结束
            this.submited = true;
            
            examCase.setSubmitTime(new Date());
            examCase.setIfPub(examCase.getExamination().isIfDirect());
            
            ApplicationBean appBean = JsfHelper.getBean("applicationBean");
            //是否异步提交试卷
            if (appBean.getSystemConfig().getAsyncSubmit()) {
//            ExamCaseSubmitter ecs = new ExamCaseSubmitter(examCase);
//            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//            exec.execute(ecs);
                ExamCaseController.getSubmitQueue().add(examCase);
            } else {
                String paperType = examCase.getExamination().getPaperType();
                IExamCaseService examCaseService = null;
                if ("random".equals(paperType)) {
                    examCaseService = SpringHelper.getSpringBean("ExamCaseService");
                } else if ("random2".equals(paperType)) {
                    examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
                } else {
                    examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
                }

                this.examCase.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
                
                this.examCase = examCaseService.computeExamCase(examCase);
              
                //若不在交卷队列中处理，此处则需要手动加入缓存
                cacheService.addExamCase(examCase);
                //处理好后加入存储队列
                ExamCaseSaver.saveProcessedExamCase(examCase);
                
//                ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//                exec.execute(new ExamCaseRanker(examCase.getExamination().getId()));
                
//                JsonUtil.Ranking(examCase.getExamination().getId());
                
                
            }
            try {
                iussService.logoutExam();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //考试后给用户加积分
            //BbsUser bu = cs.getUsr();
            //bu.setScore(bu.getScore() + examCase.getBbsScore());
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
            //this.userDAO.updateBbsUser(bu);

            //将考试结果暂时保存在SESSION中并跳转
            //JsfHelper.getRequest().getSession().setAttribute("tempExamCase", examCase.getId());
            JsfHelper.getFlash().put("tempExamCase", examCase.getId());
        } catch (Exception e) {
            MyLogger.println("发生重大事故！！！考试提交出错，尝试从REQUEST MAP交卷");
            //提交失败时，将尝试直接从request map中解析
            Map reqMap=JsfHelper.getRequest().getParameterMap();
            ExamCaseSubmitUtil.submitFromRequestMap(reqMap);
            e.printStackTrace();
            return null;
        }
        return result;
        
    }
    
    public String reserveSession() {
        MyLogger.echo("reserveSession sucess.");
        return null;
    }
    
    @PreDestroy
    public void destroy11() {
        MyLogger.echo("ExamCase destroyed.");
        iussService.logoutExam();
    }
}
