package com.hjedu.examination.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import jxl.Sheet;
import jxl.Workbook;

public class ExamImportService implements Serializable {

    IChoiceQuestionDAO question1DAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO question2DAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO question3DAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO question4DAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO question5DAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO question6DAO = SpringHelper.getSpringBean("FileQuestionDAO");
    ICaseQuestionDAO question7DAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    ExamModule2Service moduleDAO = SpringHelper.getSpringBean("ExamModule2Service");
    HashCodeService hashCodeService = SpringHelper.getSpringBean("HashCodeService");

    Map<Integer, CaseQuestion> caseMap = new HashMap();//记录综合题

    public void import1(String nfn) throws Exception {
        try {
            Workbook book = Workbook.getWorkbook(new File(nfn));
            this.importCase(book);
            this.importChoice(book);
            this.importMultiChoice(book);
            this.importFill(book);
            this.importJudge(book);
            this.importEssay(book);
            this.importFile(book);
            book.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 导入综合题
     *
     * @param book
     * @throws Exception
     */
    private void importCase(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(6);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            CaseQuestion cq = new CaseQuestion();
            cq.setBusinessId(businessId);
            //获得试题的次序
            int ord = 0;
            String ord0 = sheet.getCell(0, i).getContents().trim();
            try {
                ord = Integer.parseInt(ord0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cq.setOrd(ord);
            //模块
            String muduleName = sheet.getCell(1, i).getContents().trim();
            if ("".equals(muduleName)) {
                muduleName = "未命名";
            }
            ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
            if (em == null) {
                em = new ExamModuleModel();
                em.setName(muduleName);
                em.setFatherId(businessId);
                em.setBusinessId(businessId);
                ExternalUserUtil.setAdminBySession(em);
                moduleDAO.addExamModuleModel(em);
            } else if (em.getSons() != null) {
                if (!em.getSons().isEmpty()) {
                    String s = em.getName() + System.nanoTime();
                    em = new ExamModuleModel();
                    em.setName(s);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                }
            }
            cq.setModule(em);
            String questionName = sheet.getCell(2, i).getContents().trim();
            cq.setName(questionName);
            String answer = sheet.getCell(3, i).getContents().trim().toLowerCase();
            cq.setContent(answer);
            String innerName = sheet.getCell(4, i).getContents().trim();
            cq.setInnerName(innerName);
            String nickName = sheet.getCell(5, i).getContents().trim();
            cq.setNickName(nickName);
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            if (null == question5DAO.findEssayQuestionByHashCode(hashCode)) {
                cq.setHashCode(hashCode);
                question7DAO.addCaseQuestion(cq);
            }
            this.caseMap.put(ord, cq);//将综合题保存入MAP，以供其它题型调用
        }
    }

    private void importChoice(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(0);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            try {
                ChoiceQuestion cq = new ChoiceQuestion();
                cq.setBusinessId(businessId);
                //获得试题的次序
                int ord = 0;
                String ord0 = sheet.getCell(0, i).getContents().trim();
                try {
                    ord = Integer.parseInt(ord0);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                cq.setOrd(ord);
                //获得试题所在的模块，如果模块存在，则将试题放入，若不存在，则新建此模块
                String muduleName = sheet.getCell(1, i).getContents().trim();
                if ("".equals(muduleName)) {
                    muduleName = "未命名";
                }
                ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
                if (em == null) {
                    em = new ExamModuleModel();
                    em.setName(muduleName);
                    em.setFatherId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    em.setBusinessId(businessId);
                    moduleDAO.addExamModuleModel(em);
                } else if (em.getSons() != null) {
                    if (!em.getSons().isEmpty()) {
                        String s = em.getName() + System.nanoTime();
                        em = new ExamModuleModel();
                        em.setName(s);
                        em.setFatherId(businessId);
                        ExternalUserUtil.setAdminBySession(em);
                        em.setBusinessId(businessId);
                        moduleDAO.addExamModuleModel(em);
                    }
                }
                cq.setModule(em);
                String questionName = sheet.getCell(2, i).getContents().trim();
                cq.setName(questionName);
                String answer = sheet.getCell(3, i).getContents().trim();
                if (answer != null) {
                    answer = answer.toUpperCase();//将答案转换为大写
                    answer = answer.replace("Ａ", "A");//防止全码符号
                    answer = answer.replace("Ｂ", "B");
                    answer = answer.replace("Ｃ", "C");
                    answer = answer.replace("Ｄ", "D");
                    cq.setAnswer(answer);
                }
                List examChoices = new LinkedList();
                int l = 0;
                for (int j = 4; j < 10; j++) {
                    String temp = sheet.getCell(j, i).getContents().trim();
                    if (temp.equals("")) {
                        break;
                    } else {
                        ExamChoice ec = new ExamChoice();
                        char la = (char) ('A' + l);
                        ec.setLabel(String.valueOf(la));
                        ec.setName(temp);
                        ec.setQuestion(cq);
                        examChoices.add(ec);
                        l++;
                    }
                }
                cq.setChoices(examChoices);
                String rightStr = sheet.getCell(10, i).getContents().trim();

                cq.setRightStr(rightStr);
                //根据哈希码查找此题是否已经存在，若存在，则不再重复导入
                //System.out.println("HashCode begain.");
                String hashCode = this.hashCodeService.fetchHashCode(cq);
                if (null == question1DAO.findChoiceQuestionByHashCode(hashCode)) {
                    //System.out.println("New question added.");
                    cq.setHashCode(hashCode);
                    question1DAO.addChoiceQuestion(cq);
                }
                //System.out.println("HashCode end.");
                //Set the CaseQuestion
                int caseOrd = -1;
                String caseOrd0 = sheet.getCell(11, i).getContents().trim();
                if (caseOrd0 != null && !caseOrd0.equals("")) {
                    try {
                        caseOrd0 = caseOrd0.trim();
                        caseOrd = Integer.parseInt(caseOrd0);
                        CaseQuestion ccq = this.caseMap.get(caseOrd);
                        if (ccq != null) {
                            cq.setCaseQuestion(ccq);
                            cq.setModule(null);
                            cq.setHashCode(null);
                            question1DAO.updateChoiceQuestion(cq);
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void importMultiChoice(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(1);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            try {
                MultiChoiceQuestion cq = new MultiChoiceQuestion();
                cq.setBusinessId(businessId);
                //获得试题的次序
                int ord = 0;
                String ord0 = sheet.getCell(0, i).getContents().trim();
                try {
                    ord = Integer.parseInt(ord0);
                } catch (Exception e) {
                }
                cq.setOrd(ord);
                //模块
                String muduleName = sheet.getCell(1, i).getContents().trim();
                if ("".equals(muduleName)) {
                    muduleName = "未命名";
                }
                ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
                if (em == null) {
                    em = new ExamModuleModel();
                    em.setName(muduleName);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                } else if (em.getSons() != null) {
                    if (!em.getSons().isEmpty()) {
                        String s = em.getName() + System.nanoTime();
                        em = new ExamModuleModel();
                        em.setName(s);
                        em.setFatherId(businessId);
                        em.setBusinessId(businessId);
                        ExternalUserUtil.setAdminBySession(em);
                        moduleDAO.addExamModuleModel(em);
                    }
                }
                cq.setModule(em);
                String questionName = sheet.getCell(2, i).getContents().trim();
                cq.setName(questionName);
                String answer = sheet.getCell(3, i).getContents().trim();
                if (answer != null) {
                    answer = answer.toUpperCase();//将答案转换为大写
                    answer = answer.replace("Ａ", "A");//防止全码符号
                    answer = answer.replace("Ｂ", "B");
                    answer = answer.replace("Ｃ", "C");
                    answer = answer.replace("Ｄ", "D");
                    //以下代码重新对多选题答案的字符顺序进行排序
                    char[] chars = answer.toCharArray();
                    List<Character> cs = new ArrayList();
                    for (char c : chars) {
                        cs.add(c);
                    }
                    Collections.sort(cs);
                    StringBuilder sb = new StringBuilder();
                    for (Character c : cs) {
                        sb.append(c);
                    }
                    //排序完成，重新组合成字符串
                    cq.setAnswer(sb.toString());

                }
                List examChoices = new LinkedList();
                int l = 0;
                for (int j = 4; j < 10; j++) {
                    String temp = sheet.getCell(j, i).getContents().trim();
                    if (temp.equals("")) {
                        break;
                    } else {
                        ExamMultiChoice ec = new ExamMultiChoice();
                        char la = (char) ('A' + l);
                        ec.setLabel(String.valueOf(la));
                        ec.setName(temp);
                        ec.setQuestion(cq);
                        examChoices.add(ec);
                        l++;
                    }
                }
                cq.setChoices(examChoices);
                String rightStr = sheet.getCell(10, i).getContents().trim();
                //将选择题答案确保转换为大写
                if (rightStr != null) {
                    rightStr = rightStr.toUpperCase();
                }
                cq.setRightStr(rightStr);
                String hashCode = this.hashCodeService.fetchHashCode(cq);
                if (null == question2DAO.findMultiChoiceQuestionByHashCode(hashCode)) {
                    cq.setHashCode(hashCode);
                    question2DAO.addMultiChoiceQuestion(cq);
                }
                //Set the CaseQuestion
                int caseOrd = -1;
                String caseOrd0 = sheet.getCell(11, i).getContents().trim();
                if (caseOrd0 != null && !caseOrd0.equals("")) {
                    try {
                        caseOrd0 = caseOrd0.trim();
                        caseOrd = Integer.parseInt(caseOrd0);
                        CaseQuestion ccq = this.caseMap.get(caseOrd);
                        if (ccq != null) {
                            cq.setCaseQuestion(ccq);
                            cq.setModule(null);
                            cq.setHashCode(null);
                            question2DAO.updateMultiChoiceQuestion(cq);
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void importFill(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(2);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            FillQuestion cq = new FillQuestion();
            cq.setBusinessId(businessId);
            //获得试题的次序
            int ord = 0;
            String ord0 = sheet.getCell(0, i).getContents().trim();
            try {
                ord = Integer.parseInt(ord0);
            } catch (Exception e) {
            }
            cq.setOrd(ord);
            //模块
            String muduleName = sheet.getCell(1, i).getContents().trim();
            if ("".equals(muduleName)) {
                muduleName = "未命名";
            }
            ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
            if (em == null) {
                em = new ExamModuleModel();
                em.setName(muduleName);
                em.setFatherId(businessId);
                em.setBusinessId(businessId);
                ExternalUserUtil.setAdminBySession(em);
                moduleDAO.addExamModuleModel(em);
            } else if (em.getSons() != null) {
                if (!em.getSons().isEmpty()) {
                    String s = em.getName() + System.nanoTime();
                    em = new ExamModuleModel();
                    em.setName(s);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                }
            }
            cq.setModule(em);
            String questionName = sheet.getCell(2, i).getContents().trim();
            cq.setName(questionName);
            String rightStr = sheet.getCell(3, i).getContents().trim();

            cq.setRightStr(rightStr);
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            if (null == question3DAO.findFillQuestionByHashCode(hashCode)) {
                cq.setHashCode(hashCode);
                question3DAO.addFillQuestion(cq);
            }
            //Set the CaseQuestion
            int caseOrd = -1;
            String caseOrd0 = sheet.getCell(4, i).getContents().trim();
            if (caseOrd0 != null && !caseOrd0.equals("")) {
                try {
                    caseOrd0 = caseOrd0.trim();
                    caseOrd = Integer.parseInt(caseOrd0);
                    CaseQuestion ccq = this.caseMap.get(caseOrd);
                    if (ccq != null) {
                        cq.setCaseQuestion(ccq);
                        cq.setModule(null);
                        cq.setHashCode(null);
                        question3DAO.updateFillQuestion(cq);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    private void importJudge(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(3);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            JudgeQuestion cq = new JudgeQuestion();
            cq.setBusinessId(businessId);
            //获得试题的次序
            int ord = 0;
            String ord0 = sheet.getCell(0, i).getContents().trim();
            try {
                ord = Integer.parseInt(ord0);
            } catch (Exception e) {
            }
            cq.setOrd(ord);
            //模块
            String muduleName = sheet.getCell(1, i).getContents().trim();
            if ("".equals(muduleName)) {
                muduleName = "未命名";
            }
            ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
            if (em == null) {
                em = new ExamModuleModel();
                em.setName(muduleName);
                em.setFatherId(businessId);
                em.setBusinessId(businessId);
                ExternalUserUtil.setAdminBySession(em);
                moduleDAO.addExamModuleModel(em);
            } else if (em.getSons() != null) {
                if (!em.getSons().isEmpty()) {
                    String s = em.getName() + System.nanoTime();
                    em = new ExamModuleModel();
                    em.setName(s);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                }
            }
            cq.setModule(em);
            String questionName = sheet.getCell(2, i).getContents().trim();
            cq.setName(questionName);
            String answer = sheet.getCell(3, i).getContents().trim().toLowerCase();
            if (answer != null) {
                answer = answer.trim();
            }
            boolean atemp = false;
            try {
                atemp = Boolean.parseBoolean(answer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cq.setAnswer(atemp);
            String rightStr = sheet.getCell(4, i).getContents().trim();
            cq.setRightStr(rightStr);
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            if (null == question4DAO.findJudgeQuestionByHashCode(hashCode)) {
                cq.setHashCode(hashCode);
                question4DAO.addJudgeQuestion(cq);
            }
            //Set the CaseQuestion
            int caseOrd = -1;
            String caseOrd0 = sheet.getCell(5, i).getContents().trim();
            if (caseOrd0 != null && !caseOrd0.equals("")) {
                try {
                    caseOrd0 = caseOrd0.trim();
                    caseOrd = Integer.parseInt(caseOrd0);
                    CaseQuestion ccq = this.caseMap.get(caseOrd);
                    if (ccq != null) {
                        cq.setCaseQuestion(ccq);
                        cq.setModule(null);
                        cq.setHashCode(null);
                        question4DAO.updateJudgeQuestion(cq);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    private void importEssay(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(4);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            EssayQuestion cq = new EssayQuestion();
            cq.setBusinessId(businessId);
            //获得试题的次序
            int ord = 0;
            String ord0 = sheet.getCell(0, i).getContents().trim();
            try {
                ord = Integer.parseInt(ord0);
            } catch (Exception e) {
            }
            cq.setOrd(ord);
            //模块
            String muduleName = sheet.getCell(1, i).getContents().trim();
            if ("".equals(muduleName)) {
                muduleName = "未命名";
            }
            ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
            if (em == null) {
                em = new ExamModuleModel();
                em.setName(muduleName);
                em.setFatherId(businessId);
                em.setBusinessId(businessId);
                ExternalUserUtil.setAdminBySession(em);
                moduleDAO.addExamModuleModel(em);
            } else if (em.getSons() != null) {
                if (!em.getSons().isEmpty()) {
                    String s = em.getName() + System.nanoTime();
                    em = new ExamModuleModel();
                    em.setName(s);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                }
            }
            cq.setModule(em);
            String questionName = sheet.getCell(2, i).getContents().trim();
            cq.setName(questionName);
            String answer = sheet.getCell(3, i).getContents().trim().toLowerCase();
            cq.setAnswer(answer);
            String rightStr = sheet.getCell(4, i).getContents().trim();
            cq.setRightStr(rightStr);
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            if (null == question5DAO.findEssayQuestionByHashCode(hashCode)) {
                cq.setHashCode(hashCode);
                question5DAO.addEssayQuestion(cq);
            }
            //Set the CaseQuestion
            int caseOrd = -1;
            String caseOrd0 = sheet.getCell(5, i).getContents().trim();
            if (caseOrd0 != null && !caseOrd0.equals("")) {
                try {
                    caseOrd0 = caseOrd0.trim();
                    caseOrd = Integer.parseInt(caseOrd0);
                    CaseQuestion ccq = this.caseMap.get(caseOrd);
                    if (ccq != null) {
                        cq.setCaseQuestion(ccq);
                        cq.setModule(null);
                        cq.setHashCode(null);
                        question5DAO.updateEssayQuestion(cq);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    private void importFile(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(5);
        int total = sheet.getRows();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (int i = 1; i < total; i++) {
            FileQuestion cq = new FileQuestion();
            cq.setBusinessId(businessId);
            //获得试题的次序
            int ord = 0;
            String ord0 = sheet.getCell(0, i).getContents().trim();
            try {
                ord = Integer.parseInt(ord0);
            } catch (Exception e) {
            }
            cq.setOrd(ord);
            //模块
            String muduleName = sheet.getCell(1, i).getContents().trim();
            if ("".equals(muduleName)) {
                muduleName = "未命名";
            }
            ExamModuleModel em = moduleDAO.findExamModuleByName(muduleName);
            if (em == null) {
                em = new ExamModuleModel();
                em.setName(muduleName);
                em.setFatherId(businessId);
                em.setBusinessId(businessId);
                ExternalUserUtil.setAdminBySession(em);
                moduleDAO.addExamModuleModel(em);
            } else if (em.getSons() != null) {
                if (!em.getSons().isEmpty()) {
                    String s = em.getName() + System.nanoTime();
                    em = new ExamModuleModel();
                    em.setName(s);
                    em.setFatherId(businessId);
                    em.setBusinessId(businessId);
                    ExternalUserUtil.setAdminBySession(em);
                    moduleDAO.addExamModuleModel(em);
                }
            }
            cq.setModule(em);
            String questionName = sheet.getCell(2, i).getContents().trim();
            cq.setName(questionName);
            String answer = sheet.getCell(3, i).getContents().trim().toLowerCase();
            cq.setAnswer(answer);
            String rightStr = sheet.getCell(4, i).getContents().trim();
            cq.setRightStr(rightStr);
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            if (null == question6DAO.findFileQuestionByHashCode(hashCode)) {
                cq.setHashCode(hashCode);
                question6DAO.addFileQuestion(cq);
            }
        }
    }
}
