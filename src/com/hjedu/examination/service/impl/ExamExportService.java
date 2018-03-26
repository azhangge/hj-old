package com.hjedu.examination.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExamExportService implements Serializable {

    IChoiceQuestionDAO question1DAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO question2DAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO question3DAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO question4DAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO question5DAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO question6DAO = SpringHelper.getSpringBean("FileQuestionDAO");
    ICaseQuestionDAO question7DAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    ExamModule2Service moduleDAO = SpringHelper.getSpringBean("ExamModule2Service");

    Map<String, CaseQuestion> caseMap = new HashMap();//记录综合题

    public void export(String nfn) {
        try {

            WritableWorkbook book = Workbook.createWorkbook(new File(nfn));

            this.exportChoice(book);
            this.exportMultiChoice(book);
            this.exportFill(book);
            this.exportJudge(book);
            this.exportEssay(book);
//            this.exportFile(book);
//            this.exportCase(book);
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportCase(WritableWorkbook book) {
        try {
            // 生成名为“问答题”的作业表
            WritableSheet sheet1 = book.createSheet("综合题", 6);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目引导");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "题目材料");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 100);
            Label label4 = new Label(4, 0, "内部标识");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 20);
            Label label5 = new Label(5, 0, "题目别称");
            sheet1.addCell(label5);
            sheet1.setColumnView(5, 20);

            /*
             int i1 = 1;
             List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
             for (ExamModuleModel em : modules) {
             //导出本模块中所有问答题
             List<CaseQuestion> cqs = question7DAO.findCaseQuestionByModule(em.getId());
             for (CaseQuestion cq : cqs) {
             String moduleName = cq.getModule().getName();
             String questionName = cq.getName();
             String content = cq.getContent();
             String innerName = cq.getInnerName();
             String nickName = cq.getNickName();

             cq.setOrd(i1);//暂时设置综合题次序为il，以备其它题型设置“综合题序号”

             Label labelS = new Label(0, i1, String.valueOf(i1));
             sheet1.addCell(labelS);
             Label labelM = new Label(1, i1, moduleName);
             sheet1.addCell(labelM);
             Label labelQ = new Label(2, i1, questionName);
             sheet1.addCell(labelQ);
             Label labelA = new Label(3, i1, content);
             sheet1.addCell(labelA);
             Label labelR = new Label(4, i1, innerName);
             sheet1.addCell(labelR);
             Label labelN = new Label(5, i1, nickName);
             sheet1.addCell(labelN);
             i1++;

             this.caseMap.put(cq.getId(), cq);//将综合题保存以供其它题型调用

             }
             //导出本模块中所有综合题完成
             }
             MyLogger.echo("综合题：" + sheet1.getRows());

             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportChoice(WritableWorkbook book) {
        try {
            // 生成名为“单选题”的作业表，参数0表明这是单选题
            WritableSheet sheet1 = book.createSheet("单选题", 0);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "正确答案");
            sheet1.addCell(label3);
            Label label4 = new Label(4, 0, "选项A");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 20);
            Label label5 = new Label(5, 0, "选项B");
            sheet1.addCell(label5);
            sheet1.setColumnView(5, 20);
            Label label6 = new Label(6, 0, "选项C");
            sheet1.addCell(label6);
            sheet1.setColumnView(6, 20);
            Label label7 = new Label(7, 0, "选项D");
            sheet1.addCell(label7);
            sheet1.setColumnView(7, 20);
            Label label8 = new Label(8, 0, "选项E");
            sheet1.addCell(label8);
            sheet1.setColumnView(8, 20);
            Label label9 = new Label(9, 0, "选项F");
            sheet1.addCell(label9);
            sheet1.setColumnView(9, 20);
            Label label10 = new Label(10, 0, "试题解析");
            sheet1.addCell(label10);
            sheet1.setColumnView(10, 40);

            Label label11 = new Label(11, 0, "综合题序号");
            sheet1.addCell(label11);
            sheet1.setColumnView(11, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有单选题
                List<ChoiceQuestion> cqs = question1DAO.findChoiceQuestionByModule(em.getId());
                for (ChoiceQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String answer = cq.getAnswer();
                    String rightStr = cq.getRightStr();
                    List<ExamChoice> ecs = cq.getChoices();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelA = new Label(3, i1, answer);
                    sheet1.addCell(labelA);
                    int cn = ecs.size();
                    for (int ci = 0; ci < cn && ci <= 5; ci++) {
                        Label labelC = new Label(ci + 4, i1, ecs.get(ci).getName());
                        sheet1.addCell(labelC);
                    }
                    Label labelR = new Label(10, i1, rightStr);
                    sheet1.addCell(labelR);

                    //设置综合题
//                    if (cq.getCaseQuestion() != null) {
//                        CaseQuestion ccq = this.caseMap.get(cq.getCaseQuestion().getId());
//                        if (ccq != null) {
//                            Label labelCC = new Label(11, i1, String.valueOf(ccq.getOrd()));
//                            sheet1.addCell(labelCC);
//                        }
//                    }
                    i1++;
                }
                //导出本模块中所有单选题完成
            }
            MyLogger.echo("单选题：" + sheet1.getRows());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportMultiChoice(WritableWorkbook book) {
        try {
            // 生成名为“多选题”的作业表，参数0表明这是多选题
            WritableSheet sheet1 = book.createSheet("多选题", 1);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "正确答案");
            sheet1.addCell(label3);
            Label label4 = new Label(4, 0, "选项A");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 20);
            Label label5 = new Label(5, 0, "选项B");
            sheet1.addCell(label5);
            sheet1.setColumnView(5, 20);
            Label label6 = new Label(6, 0, "选项C");
            sheet1.addCell(label6);
            sheet1.setColumnView(6, 20);
            Label label7 = new Label(7, 0, "选项D");
            sheet1.addCell(label7);
            sheet1.setColumnView(7, 20);
            Label label8 = new Label(8, 0, "选项E");
            sheet1.addCell(label8);
            sheet1.setColumnView(8, 20);
            Label label9 = new Label(9, 0, "选项F");
            sheet1.addCell(label9);
            sheet1.setColumnView(9, 20);
            Label label10 = new Label(10, 0, "试题解析");
            sheet1.addCell(label10);
            sheet1.setColumnView(10, 40);

            Label label11 = new Label(11, 0, "综合题序号");
            sheet1.addCell(label11);
            sheet1.setColumnView(11, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有多选题
                List<MultiChoiceQuestion> cqs = question2DAO.findMultiChoiceQuestionByModule(em.getId());
                for (MultiChoiceQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String answer = cq.getAnswer();
                    String rightStr = cq.getRightStr();
                    List<ExamMultiChoice> ecs = cq.getChoices();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelA = new Label(3, i1, answer);
                    sheet1.addCell(labelA);
                    int cn = ecs.size();
                    for (int ci = 0; ci < cn && ci <= 5; ci++) {
                        Label labelC = new Label(ci + 4, i1, ecs.get(ci).getName());
                        sheet1.addCell(labelC);
                    }
                    Label labelR = new Label(10, i1, rightStr);
                    sheet1.addCell(labelR);

                    //设置综合题
//                    if (cq.getCaseQuestion() != null) {
//                        CaseQuestion ccq = this.caseMap.get(cq.getCaseQuestion().getId());
//                        if (ccq != null) {
//                            Label labelCC = new Label(11, i1, String.valueOf(ccq.getOrd()));
//                            sheet1.addCell(labelCC);
//                        }
//                    }
                    i1++;
                }
                //导出本模块中所有多选题完成
            }
            MyLogger.echo("多选题：" + sheet1.getRows());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportFill(WritableWorkbook book) {
        try {
            // 生成名为“填空题”的作业表，参数0表明这是填空题
            WritableSheet sheet1 = book.createSheet("填空题", 2);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目（填空内容写在[]中）");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "试题解析");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 40);

            Label label11 = new Label(4, 0, "综合题序号");
            sheet1.addCell(label11);
            sheet1.setColumnView(4, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有填空题
                List<FillQuestion> cqs = question3DAO.findFillQuestionByModule(em.getId());
                for (FillQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String rightStr = cq.getRightStr();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelR = new Label(3, i1, rightStr);
                    sheet1.addCell(labelR);

                    //设置综合题
//                    if (cq.getCaseQuestion() != null) {
//                        CaseQuestion ccq = this.caseMap.get(cq.getCaseQuestion().getId());
//                        if (ccq != null) {
//                            Label labelCC = new Label(4, i1, String.valueOf(ccq.getOrd()));
//                            sheet1.addCell(labelCC);
//                        }
//                    }
                    i1++;
                }
                //导出本模块中所有填空题完成
            }
            MyLogger.echo("填空题：" + sheet1.getRows());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportJudge(WritableWorkbook book) {
        try {
            // 生成名为“判断题”的作业表，参数0表明这是判断题
            WritableSheet sheet1 = book.createSheet("判断题", 3);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "正确答案（答案为true或false）");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 30);
            Label label4 = new Label(4, 0, "试题解析");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 40);

            Label label11 = new Label(5, 0, "综合题序号");
            sheet1.addCell(label11);
            sheet1.setColumnView(5, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有判断题
                List<JudgeQuestion> cqs = question4DAO.findJudgeQuestionByModule(em.getId());
                for (JudgeQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String answer = String.valueOf(cq.getAnswer());
                    String rightStr = cq.getRightStr();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelA = new Label(3, i1, answer);
                    sheet1.addCell(labelA);
                    Label labelR = new Label(4, i1, rightStr);
                    sheet1.addCell(labelR);

                    //设置综合题
//                    if (cq.getCaseQuestion() != null) {
//                        CaseQuestion ccq = this.caseMap.get(cq.getCaseQuestion().getId());
//                        if (ccq != null) {
//                            Label labelCC = new Label(5, i1, String.valueOf(ccq.getOrd()));
//                            sheet1.addCell(labelCC);
//                        }
//                    }
                    i1++;
                }
                //导出本模块中所有判断题完成
            }
            MyLogger.echo("判断题：" + sheet1.getRows());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportEssay(WritableWorkbook book) {
        try {
            // 生成名为“问答题”的作业表
            WritableSheet sheet1 = book.createSheet("问答题", 4);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "正确答案");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 50);
            Label label4 = new Label(4, 0, "试题解析");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 40);

            Label label11 = new Label(5, 0, "综合题序号");
            sheet1.addCell(label11);
            sheet1.setColumnView(5, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有问答题
                List<EssayQuestion> cqs = question5DAO.findEssayQuestionByModule(em.getId());
                for (EssayQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String answer = cq.getAnswer();
                    String rightStr = cq.getRightStr();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelA = new Label(3, i1, answer);
                    sheet1.addCell(labelA);
                    Label labelR = new Label(4, i1, rightStr);
                    sheet1.addCell(labelR);

                    //设置综合题
//                    if (cq.getCaseQuestion() != null) {
//                        CaseQuestion ccq = this.caseMap.get(cq.getCaseQuestion().getId());
//                        if (ccq != null) {
//                            Label labelCC = new Label(5, i1, String.valueOf(ccq.getOrd()));
//                            sheet1.addCell(labelCC);
//                        }
//                    }
                    i1++;
                }
                //导出本模块中所有问答题完成
            }
            MyLogger.echo("问答题：" + sheet1.getRows());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportFile(WritableWorkbook book) {
        try {
            // 生成名为“文件题”的作业表
            WritableSheet sheet1 = book.createSheet("文件题", 5);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "模块名称");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "题目");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 50);
            Label label3 = new Label(3, 0, "试题备注");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 50);
            Label label4 = new Label(4, 0, "试题解析");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 40);

            int i1 = 1;
            List<ExamModuleModel> modules = moduleDAO.findAllExamModuleModel();
            for (ExamModuleModel em : modules) {
                //导出本模块中所有文件题
                List<FileQuestion> cqs = question6DAO.findFileQuestionByModule(em.getId());
                for (FileQuestion cq : cqs) {
                    String moduleName = cq.getModule().getName();
                    String questionName = cq.getName();
                    String answer = cq.getAnswer();
                    String rightStr = cq.getRightStr();

                    Label labelS = new Label(0, i1, String.valueOf(i1));
                    sheet1.addCell(labelS);
                    Label labelM = new Label(1, i1, moduleName);
                    sheet1.addCell(labelM);
                    Label labelQ = new Label(2, i1, questionName);
                    sheet1.addCell(labelQ);
                    Label labelA = new Label(3, i1, answer);
                    sheet1.addCell(labelA);
                    Label labelR = new Label(4, i1, rightStr);
                    sheet1.addCell(labelR);
                    i1++;
                }
                //导出本模块中所有文件题完成
            }
            MyLogger.echo("文件题：" + sheet1.getRows());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExamExportService ees = new ExamExportService();
        ees.export("C:\\test.xls");
    }
}
