package com.hjedu.examination.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamRoomDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.SpringHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 本抽象类规范了一场“综合考试”需要的各种功能
 * 本类的已知子类包括：ExamCaseService（简单随机试卷考试）,ManualExamCaseService（人考试试卷考试）,ExamCaseRandom2Service（随机试卷考试）
 *
 * @author www.wbdwl.com
 */
public abstract class IExamCaseService {

    /**
     * 为考试实例装配试题并排序等 考试实例应该在考试的托管BEAN中构建
     *
     * @param ec 考试实例
     */
    public abstract void buildExamCase(ExamCase ec);

    /**
     * 计算单个适配单元的正确与否及得分，主要用于触屏设备中的单题验证
     *
     * @param adapter 试题适配器
     * @return
     */
    public abstract boolean computeSingleAdapter(ExamCaseItemAdapter adapter);

    /**
     * 计算一个考试实例的成绩和各条目得分、作答详情，计算完成后返回
     *
     * @param ec 考试实例
     * @return 考试实例
     */
    public abstract ExamCase computeExamCase(ExamCase ec);

    /**
     * 对考试实例得分情况进行再计算，主要用于后台阅卷后对分数重算
     *
     * @param ec 考试实例
     * @return 考试实例
     */
    public abstract double computeTotalScore(ExamCase ec);

    /**
     * 保存一个考试实例，主要用于考试界面的‘试卷保存’
     *
     * @param ec
     * @return
     */
    public abstract ExamCase preSaveExamCase(ExamCase ec);

    /**
     * 恢复一个考试实例，主要为填空题及多选题恢复作答情况，用于中断的考试续考恢复与作答详情中的恢复
     *
     * @param ec
     * @return
     */
    public abstract ExamCase resumeExamCase(ExamCase ec);

    /**
     * 将填空题装载为试卷适用模型，主要是根据‘[’、']'构建填空，以及每个空的前置、结尾语句
     *
     * @param fill 填空题实例
     */
    public void buildItemFillBlocks(ExamCaseItemFill fill) {
        ExamCaseServiceUtils.buildItemFillBlocks(fill);
    }

    /**
     * 单选题选项随机化 本方法主要供子类内部使用
     *
     * @param choices 单选题选项
     */
    protected void choiceOrderAdapter(List<ExamChoice> choices) {
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    /**
     * 多选题选项随机化 本方法主要供子类内部使用
     *
     * @param choices 多选题选项
     */
    protected void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
    }

    /**
     * 装载综合题的各小题，根据综合题中的各小题构建各小题作答实例 本方法主要供子类内部使用
     *
     * @param j 综合题
     * @param ec 考试实例
     * @return 返回装载好的综合题作答实例
     */
    protected ExamCaseItemCase buildCaseItem(CaseQuestion j, ExamCase ec) {
        
        ExamCaseItemCase ecie = new ExamCaseItemCase();
        ecie.setExamCase(ec);
        ecie.setQuestion(j);
        //将选择题加入综合题中
        List<ExamCaseItemChoice> ecicqcs = new LinkedList();
        List<ChoiceQuestion> cqscc = j.getChoices();
        for (ChoiceQuestion c : cqscc) {
            ExamCaseItemChoice ecic = new ExamCaseItemChoice();
            //ecic.setExamCase(ec);
            ecic.setCaseItem(ecie);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecicqcs.add(ecic);
        }
        ecie.setChoiceItems(ecicqcs);

        //将多选题加入综合题中
        List<ExamCaseItemMultiChoice> mcieqs = new LinkedList();
        List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
        for (MultiChoiceQuestion c : mqscc) {
            ExamCaseItemMultiChoice eciee = new ExamCaseItemMultiChoice();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            eciee.setQuestion(c);
            mcieqs.add(eciee);
        }
        ecie.setMultiChoiceItems(mcieqs);

        //将填空题加入综合题中
        List<ExamCaseItemFill> fcieqs = new LinkedList();
        List<FillQuestion> fqscc = j.getFills();
        for (FillQuestion c : fqscc) {
            ExamCaseItemFill eciee = new ExamCaseItemFill();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            fcieqs.add(eciee);
        }
        ecie.setFillItems(fcieqs);

        //将判断题加入综合题中
        List<ExamCaseItemJudge> jcieqs = new LinkedList();
        List<JudgeQuestion> jqscc = j.getJudges();
        for (JudgeQuestion c : jqscc) {
            ExamCaseItemJudge eciee = new ExamCaseItemJudge();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            jcieqs.add(eciee);
        }
        ecie.setJudgeItems(jcieqs);

        //将问答题加入综合题中
        List<ExamCaseItemEssay> ecieqs = new LinkedList();
        List<EssayQuestion> eqscc = j.getEssaies();
        for (EssayQuestion c : eqscc) {
            ExamCaseItemEssay eciee = new ExamCaseItemEssay();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            ecieqs.add(eciee);
        }
        ecie.setEssayItems(ecieqs);
        //向临时变量中加入综合题
        return ecie;
    }

    /**
     * 对综合题实例中的小题项进行排序
     *
     * @param j
     */
    public void orderCaseItems(ExamCaseItemCase j) {
        List<ExamCaseItemChoice> cqscc = j.getChoiceItems();
        List<ExamCaseItemMultiChoice> mqscc = j.getMultiChoiceItems();
        List<ExamCaseItemFill> fqscc = j.getFillItems();
        List<ExamCaseItemJudge> jqscc = j.getJudgeItems();
        List<ExamCaseItemEssay> eqscc = j.getEssayItems();
        if (cqscc != null) {
            Collections.sort(cqscc);
        }
        if (mqscc != null) {
            Collections.sort(mqscc);
        }
        if (fqscc != null) {
            Collections.sort(fqscc);
        }
        if (jqscc != null) {
            Collections.sort(jqscc);
        }
        if (eqscc != null) {
            Collections.sort(eqscc);
        }
    }

    /**
     * 确定考场，辅助功能，可选择性实现
     *
     * @param ip
     * @return
     */
    public ExamRoom confirmExamRoom(String ip) {
        IExamRoomDAO roomDAO = SpringHelper.getSpringBean("ExamRoomDAO");
        List<ExamRoom> rooms = roomDAO.findAllExamRoom();
        for (ExamRoom er : rooms) {
            if (compareIp(er.getBeginIp(), ip) && (compareIp(ip, er.getEndIp()))) {
                return er;
            }
        }
        return null;
    }

    /**
     * 比较IP
     *
     * @param o
     * @param target
     * @return
     */
    public Boolean compareIp(String o, String target) {//如果原IP小于等于目标IP，则返回true，否则返回false
        return IpHelper.compareIp(o, target);
    }

    /**
     * 判断是否为补考
     *
     * @param examId
     * @param userId
     * @return
     */
    public Boolean checkIfSupplementaryExamination(String examId, String userId) {
        IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCase> cases = examCaseDAO.findExamCaseByExaminationAndUser(examId, userId);
        if (cases == null) {
            return false;
        }
        boolean ifUnPass = false;
        boolean ifPass = false;
        int unPassTimes = 0;
        int passTimes = 0;
        int totalTimes = 0;
        for (ExamCase ec : cases) {
            double ratio = ec.getScore() / ec.getTotalFullScore();
            if (ratio < 0.6D) {
                ifUnPass = true;
                unPassTimes++;
                totalTimes++;
            } else {
                ifPass = true;
                passTimes++;
                totalTimes++;
            }
            
        }
        //检测是否为补考的规则为：考试次数大于考试通过次数
        System.out.println(totalTimes + "" + passTimes);
        if (totalTimes > passTimes) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将实例适配为JSON
     *
     * @param ec
     * @return
     */
    public String adaptToJSON(ExamCase ec) {
        try {
            GsonBuilder gb = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                    .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                    .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                    .setPrettyPrinting() //对json结果格式化.  
                    .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
            //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
            //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

            Gson gson = gb.create();
            String json = gson.toJson(ec.getVparts());
            ec.setVpartsJson(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从VPARTS JSON字符串中恢复
     *
     * @param ec
     * @return
     */
    public ExamCase restoreVpartsFromJSON(ExamCase ec) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        List<VirtualExamPart> vs = gson.fromJson(ec.getVpartsJson(), new TypeToken<List<VirtualExamPart>>() {
        }.getType());
        ec.setVparts(vs);
        ec = this.restoreItemsFromJSON(ec);
        return ec;
    }

    /**
     * 将从APK传来的GSON恢复为可有效保存的ExamCase 主要把vparts中的数据恢复为ExamCaseItems
     *
     * @param ec
     * @return
     */
    public ExamCase restoreItemsFromJSON(ExamCase ec) {
        if (ec == null) {
            return null;
        }
        IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
        IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
        IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
        IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
        List<ExamCaseItemChoice> cqs = new LinkedList();
        List<ExamCaseItemMultiChoice> mcqs = new LinkedList();
        List<ExamCaseItemFill> fqs = new LinkedList();
        List<ExamCaseItemJudge> jqs = new LinkedList();
        List<ExamCaseItemEssay> eqs = new LinkedList();
        List<ExamCaseItemFile> ffqs = new LinkedList();
        List<VirtualExamPart> vparts = ec.getVparts();
        if (vparts != null) {
            for (VirtualExamPart vpart : vparts) {
                List<ExamCaseItemAdapter> adapters = vpart.getAdapters();
                if (adapters != null) {
                    for (ExamCaseItemAdapter ada : adapters) {
                        if ("choice".equals(ada.getQtype())) {
                            ExamCaseItemChoice ei = ada.getChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(choiceQuestionDAO.findChoiceQuestion(ada.getQuestion().getId()));
                            cqs.add(ei);
                        }
                        if ("mchoice".equals(ada.getQtype())) {
                            ExamCaseItemMultiChoice ei = ada.getMultiChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(multiChoiceQuestionDAO.findMultiChoiceQuestion(ada.getQuestion().getId()));
                            mcqs.add(ada.getMultiChoiceItem());
                        }
                        if ("fill".equals(ada.getQtype())) {
                            ExamCaseItemFill ei = ada.getFillItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(fillQuestionDAO.findFillQuestion(ada.getQuestion().getId()));
                            fqs.add(ada.getFillItem());
                        }
                        if ("judge".equals(ada.getQtype())) {
                            ExamCaseItemJudge ei = ada.getJudgeItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(judgeQuestionDAO.findJudgeQuestion(ada.getQuestion().getId()));
                            jqs.add(ada.getJudgeItem());
                        }
                        if ("essay".equals(ada.getQtype())) {
                            ExamCaseItemEssay ei = ada.getEssayItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(essayQuestionDAO.findEssayQuestion(ada.getQuestion().getId()));
                            eqs.add(ada.getEssayItem());
                        }
                        if ("file".equals(ada.getQtype())) {
                            ExamCaseItemFile ei = ada.getFileItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(fileQuestionDAO.findFileQuestion(ada.getQuestion().getId()));
                            ffqs.add(ada.getFileItem());
                        }
                    }
                }
            }
        }
        ec.setChoiceItems(cqs);
        ec.setMultiChoiceItems(mcqs);
        ec.setFillItems(fqs);
        ec.setJudgeItems(jqs);
        ec.setEssayItems(eqs);
        ec.setFileItems(ffqs);
        return ec;
    }

    /**
     * 本方法从缓存或者数据库中取回考试实例 首先从缓存中检查是否存在实例，不存在再从数据库查找
     *
     * @param id
     * @return
     */
    public ExamCase retrieveExamCase(String id) {
        ExamCase examCase = null;
        IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
        IExamCaseService examCaseService = null;
        examCase = cacheService.findExamCase(id);
        if (examCase == null) {//从数据库中查找成绩
            IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
            examCase = examCaseDAO.findExamCase(id);
            if (examCase != null) {
                if ("random".equals(examCase.getExamination().getPaperType())) {
                    examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
                } else if ("random2".equals(examCase.getExamination().getPaperType())) {
                    examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
                } else if ("manual".equals(examCase.getExamination().getPaperType())) {
                    examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
                }
                if (examCaseService != null) {
                    //examCase = examCaseService.restoreVpartsFromJSON(examCase);
                    examCase = examCaseService.resumeExamCase(examCase);
                }
            }
        }
        return examCase;
    }
    
    public void recomputeExamCase(ExamCase examCase) {
        IExamCaseService examCaseService = null;
        if ("random".equals(examCase.getExamination().getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
        } else if ("random2".equals(examCase.getExamination().getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
        } else if ("manual".equals(examCase.getExamination().getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
        }
        if (examCaseService != null) {
            IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
//            examCaseService.computeTotalScore(examCase);
            IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
            cacheService.updateExamCaseIfExists(examCase);
            examCaseDAO.updateExamCase(examCase);
        }
    }
    
    public void deleteExamCase(String id) {
        IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
        cacheService.deleteExamCase(id);//从缓存删除成绩
        examCaseDAO.deleteExamCase(id);//从数据库删除成绩
    }
    
}
