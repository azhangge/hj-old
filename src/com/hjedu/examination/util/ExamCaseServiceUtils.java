package com.hjedu.examination.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.hjedu.examination.entity.ExamCaseItemFillBlock;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.VirtualExamPart;
import com.huajie.util.SpringHelper;

/**
 * 本类辅助实现考试的通用功能
 *
 * @author Administrator
 */
public class ExamCaseServiceUtils {

    /**
     * 将填空题装载为试卷适用模型，主要是根据‘[’、']'构建填空，以及每个空的前置、结尾语句
     *
     * @param fill 填空题实例
     */
    public static void buildItemFillBlocks(ExamCaseItemFill fill) {
        String answerB = SpringHelper.getSpringBean("fillBegain");
        String answerE = SpringHelper.getSpringBean("fillEnd");
        List<ExamCaseItemFillBlock> blocks = new LinkedList();
        blocks.clear();
        String t = fill.getQuestion().getName();
        //MyLogger.echo(t);
        int b = -1;
        int e = -1;
        while (true) {
            try {
                b = t.indexOf(answerB);
                e = t.indexOf(answerE);
                if (b < 0 || e < 0) {
                    break;
                }
                String pre = t.substring(0, b);
                String ans = t.substring(b, e + 1);
                //MyLogger.echo(pre);
                //MyLogger.echo(ans);
                //t = t.replace(pre, "");
                //t = t.replace(ans, "");
                t = t.substring(e + 1);
                ExamCaseItemFillBlock block = new ExamCaseItemFillBlock();
                block.setPreStr(pre);
                ans = ans.replace(answerB, "");
                ans = ans.replace(answerE, "");
                block.setRightAnswer(ans);
                blocks.add(block);
            } catch (Exception eee) {
                eee.printStackTrace();
            }
        }
        fill.setBlocks(blocks);
        fill.setLastStr(t);
    }

    /**
     * 单选题选项随机化
     *
     * @param choices 单选题选项
     */
    public static void choiceOrderAdapter(List<ExamChoice> choices) {
        Collections.shuffle(choices);
        char be = 'A';
        int i = 0;
        for (ExamChoice c : choices) {
            char ee = (char) (be + i);
            c.setLabelRendered(String.valueOf(ee));
            i++;
        }
    }

    /**
     * 多选题选项随机化
     *
     * @param choices 多选题选项
     */
    public static void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        Collections.shuffle(choices);
        char be = 'A';
        int i = 0;
        for (ExamMultiChoice c : choices) {
            char ee = (char) (be + i);
            c.setLabelRendered(String.valueOf(ee));
            i++;
        }
    }

    /**
     * 计算每个大题的成绩得分，并计算大题内总分，试卷总分
     *
     * @param ec
     */
    public static void computePartScore(ExamCase ec) {
        List<VirtualExamPart> vparts = ec.getVparts();

        if (vparts == null) {
            return;
        }
        double paperTotal = 0d;
        
        double total = 0.0d;
        Map<String,Double> totalScoreMap = new HashMap<String,Double>();
        //计算每个大题及试卷总分
        for (VirtualExamPart part : vparts) {
            List<ExamCaseItemAdapter> ads2 = part.getAdapters();
            double partScore = 0d;//计算本大题分数
            double vScore = 0d;
            for (ExamCaseItemAdapter ad : ads2) {
                if (ad.getQtype().equals("choice")) {
                    ExamCaseItemChoice ei = ad.getChoiceItem();
                    if (ei.isIfRight()) {
                        vScore += part.getChoiceScore();
                    }
                    totalScoreMap.put("choice", part.getTotalScore());
                    //ec.getChoiceItems().add(ei);
                    //totalScore += part.getChoiceScore();
                    //partScore += part.getChoiceScore();
                }
                if (ad.getQtype().equals("mchoice")) {
                    ExamCaseItemMultiChoice ei = ad.getMultiChoiceItem();
                    if (ei.isIfRight()) {
                        vScore += part.getMultiChoiceScore();
                    }
                    totalScoreMap.put("mchoice", part.getTotalScore());
                    //ec.getMultiChoiceItems().add(ei);
                    //totalScore += part.getMultiChoiceScore();
                    //partScore += part.getMultiChoiceScore();
                }
                if (ad.getQtype().equals("fill")) {
                    ExamCaseItemFill ei = ad.getFillItem();
                    if (ei.isIfRight()) {
                        vScore += part.getFillScore();
                    }
                    totalScoreMap.put("fill", part.getTotalScore());
                    //ec.getFillItems().add(ei);
                    //totalScore += part.getFillScore();
                    //partScore += part.getFillScore();
                }
                if (ad.getQtype().equals("judge")) {
                    ExamCaseItemJudge ei = ad.getJudgeItem();
                    if (ei.isIfRight()) {
                        vScore += part.getJudgeScore();
                    }
                    totalScoreMap.put("judge", part.getTotalScore());
                    //ec.getJudgeItems().add(ei);
                    //totalScore += part.getJudgeScore();
                    //partScore += part.getJudgeScore();
                }
                if (ad.getQtype().equals("essay")) {
                    ExamCaseItemEssay ei = ad.getEssayItem();
                    if (ei.isIfRight()) {
                        vScore += part.getEssayScore();
                    }
                    totalScoreMap.put("essay", part.getTotalScore());
                    //ec.getEssayItems().add(ei);
                    //totalScore += part.getEssayScore();
                    //partScore += part.getEssayScore();
                }
                if (ad.getQtype().equals("file")) {
                    ExamCaseItemFile ei = ad.getFileItem();
                    if (ei.isIfRight()) {
                        vScore += part.getFileScore();
                    }
                    totalScoreMap.put("file", part.getTotalScore());
                    //ec.getFileItems().add(ei);
                    //totalScore += part.getFileScore();
                    //partScore += part.getFileScore();
                }
                if (ad.getQtype().equals("case")) {
                    ExamCaseItemCase ei = ad.getCaseItem();
                    if (ei.isIfRight()) {
                        vScore += ei.getScore();
                    }
                    totalScoreMap.put("case", part.getTotalScore());
                    //ec.getCaseItems().add(ei);
                    //totalScore += ei.getQuestion().getTotalScore();
                    //partScore += ei.getQuestion().getTotalScore();
                }
            }

            part.setRealScore(vScore);
            part.setTotalScore(partScore);
            paperTotal = paperTotal+partScore;
        }
        if(totalScoreMap.get("choice")!=null){
        	total += totalScoreMap.get("choice");
        }
        if(totalScoreMap.get("mchoice")!=null){
        	total += totalScoreMap.get("mchoice");
        }
        if(totalScoreMap.get("fill")!=null){
        	total += totalScoreMap.get("fill");
        }
        if(totalScoreMap.get("judge")!=null){
        	total += totalScoreMap.get("judge");
        }
        if(totalScoreMap.get("essay")!=null){
        	total += totalScoreMap.get("essay");
        }
        if(totalScoreMap.get("file")!=null){
        	total += totalScoreMap.get("file");
        }
        if(totalScoreMap.get("case")!=null){
        	total += totalScoreMap.get("case");
        }
        ec.setTotalFullScore(ec.getTotalFullScore());

    }

    /**
     * 对综合题中的小题项进行排序
     *
     * @param j
     */
    public static void orderCaseQuestion(CaseQuestion j) {
        if (j == null) {
            return;
        }
        List<ChoiceQuestion> cqscc = j.getChoices();
        List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
        List<FillQuestion> fqscc = j.getFills();
        List<JudgeQuestion> jqscc = j.getJudges();
        List<EssayQuestion> eqscc = j.getEssaies();
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

}
