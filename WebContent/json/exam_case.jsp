<%-- 
    Document   : exam_case
    Created on : 2015-11-16, 13:59:03
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.huajie.util.SpringHelper"%>

<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page import="com.huajie.exam.model.ExamCase,com.huajie.exam.model.*,com.huajie.service.*,com.huajie.service.IUserSessionStateService,com.huajie.exam.dao.IExamCaseLogDAO"%>
<%@page import="com.huajie.exam.pool.ExamPaperPool"%>
<%
    String eid = request.getParameter("eid");
    String uid = request.getParameter("uid");

    if (eid != null && uid != null) {
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser bu = userDAO.findBbsUser(uid);
        if (bu != null) {

            ExamCase ec = ExamPaperPool.takePaper(eid);//从试卷池中取一份试卷
            if (ec != null) {
                ec.setUser(bu);
                ec.setIp(request.getRemoteAddr());

                //修改填空题的blocks
                java.util.List<VirtualExamPart> vparts = ec.getVparts();
                if (vparts != null) {
                    for (VirtualExamPart part : vparts) {
                        java.util.List<ExamCaseItemAdapter> adapters = part.getAdapters();
                        if (adapters != null) {
                            for (ExamCaseItemAdapter ada : adapters) {
                                String qtype = ada.getQtype();
                                if ("fill".equals(qtype)) {
                                    ExamCaseItemFill fill = ada.getFillItem();
                                    if (fill != null) {
                                        ExamCaseServiceUtils.buildItemFillBlocks(fill);
                                    }
                                }
                            }
                        }
                    }
                }

                //记录考试状态
                IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
                iussService.loginExam(ec, request);
                //消耗积分
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("考试消耗积分", (int) (-1 * ec.getExamination().getScorePaid()));
                //加入抽题记录
                ExamCaseLog log = new ExamCaseLog();
                log.setExamination(ec.getExamination());
                log.setUser(bu);
                log.setIp(request.getRemoteAddr());
                IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
                logDAO2.addExamCaseLog(log);

//                ExamCaseSaver ecs = new ExamCaseSaver(ec, false);
//                ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//                exec.execute(ecs);
                //将考试实例封装为JSON
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(ec);
                out.print(json);
            }
        }
    }

%>
