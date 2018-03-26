package com.hjedu.customer.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.IFinanceLogDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.FinanceLog;
import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsTalk;
import com.hjedu.platform.entity.BbsZone;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MD5;
import com.huajie.util.SpringHelper;

public class ComplexBbsUserService implements Serializable {

    ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsThreadDAO bbsThreadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
    IBbsTalkDAO bbsTalkDAO = SpringHelper.getSpringBean("BbsTalkDAO");
    IBbsScoreLogDAO bbsScoreLogDAO = SpringHelper.getSpringBean("BbsScoreLogDAO");
    IFinanceLogDAO financeLogDAO = SpringHelper.getSpringBean("FinanceLogDAO");
    IBbsMessageDAO bbsMessageDAO = SpringHelper.getSpringBean("BbsMessageDAO");
    IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
    IBbsZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseLogDAO examCaseLogDAO = SpringHelper.getSpringBean("ExamCaseLogDAO");
    IWrongQuestionDAO wrongQuesstionDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    IWrongKnowledgeDAO wrongKnowledgeDAO = SpringHelper.getSpringBean("WrongKnowledgeDAO");
    ILessonLogDAO lessonLogDAO=SpringHelper.getSpringBean("LessonLogDAO");

    IModuleExamCaseDAO mexamCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    IBuffetExamCaseDAO bexamCaseDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");
    IContestCaseDAO cexamCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    IExamChoiceStatisticDAO statisticCaseDAO = SpringHelper.getSpringBean("ExamChoiceStatisticDAO");
    //IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    String pwdEncoder = SpringHelper.getSpringBean("pwd_encoder");

    public void deleteBbsUserSafely(String id) {
//        this.bbsFileDAO.delClientFilebyUsr(id);
        this.bbsScoreLogDAO.deleteByUsr(id);
        this.examCaseLogDAO.deleteExamCaseLogByUser(id);
        this.fitBbsTalk(id);
        this.fitBbsZone(id);
        this.bbsMessageDAO.deleteMsgByReceiver(id);
        this.bbsMessageDAO.deleteMsgBySender(id);
        this.fitFinanceLog(id);
        this.fitExamCase(id);
        this.fitMExamCase(id);
        this.fitBExamCase(id);
        this.fitCExamCase(id);
        this.wrongQuesstionDAO.deleteWrongQuestionByUsr(id);
        this.wrongKnowledgeDAO.deleteWrongKnowledgeByUsr(id);
        this.statisticCaseDAO.deleteStatisticByUser(id);
        this.lessonLogDAO.deleteLogByUser(id);
        this.bbsUserDAO.deleteBbsUser(id);

    }

    private void fitFinanceLog(String uid) {
        List<FinanceLog> cases = this.financeLogDAO.findFinanceLogByUsr(uid);
        for (FinanceLog ec : cases) {
            this.financeLogDAO.deleteFinanceLog(ec.getId());
        }
    }

    private void fitBbsTalk(String uid) {
        BbsUser bu = this.bbsUserDAO.findBbsUser(uid);
        List<BbsTalk> bus = this.bbsTalkDAO.findByGenBy(bu, 0, 999999999);
        for (BbsTalk t : bus) {
            this.bbsTalkDAO.delete(t.getId());
        }
    }

    private void fitExamCase(String uid) {
        List<ExamCase> cases = this.examCaseDAO.findExamCaseByUser(uid);
        for (ExamCase ec : cases) {
            this.examCaseDAO.deleteExamCase(ec.getId());
        }
    }

    private void fitMExamCase(String uid) {
        List<ModuleExamCase> cases = this.mexamCaseDAO.findModuleExamCaseByUser(uid);
        for (ModuleExamCase ec : cases) {
            this.mexamCaseDAO.deleteModuleExamCase(ec.getId());
        }
    }

    private void fitBExamCase(String uid) {
        List<BuffetExamCase> cases = this.bexamCaseDAO.findBuffetExamCaseByUser(uid);
        for (BuffetExamCase ec : cases) {
            this.bexamCaseDAO.deleteBuffetExamCase(ec.getId());
        }
    }

    private void fitCExamCase(String uid) {
        List<ContestCase> cases = this.cexamCaseDAO.findContestCaseByUser(uid);
        for (ContestCase ec : cases) {
            this.cexamCaseDAO.deleteContestCase(ec.getId());
        }
    }

    private void fitBbsZone(String uid) {
        List<BbsZone> zones = this.bbsZoneDAO.findAll(0, 99999);
        for (BbsZone zone : zones) {
            BbsUser u = null;
            List<BbsUser> us = zone.getManagers();
            for (BbsUser b : us) {
                if (uid.equals(b.getId())) {
                    u = b;
                    break;
                }
            }
            if (u != null) {
                us.remove(u);
                this.bbsZoneDAO.update(zone);
            }

        }
    }

    //将密码根据需要加密
    public void handlePwd(BbsUser user) {
        user.setPwdEncoder(pwdEncoder);
        if ("md5_16".equals(this.pwdEncoder)) {
            user.setPassword(MD5.bit16(user.getPassword()));
        } else if ("md5_32".equals(this.pwdEncoder)) {
            user.setPassword(MD5.bit32(user.getPassword()));
        } else if ("des".equals(this.pwdEncoder)) {
            DESTool tool = new DESTool();
            String code = tool.encrypt(user.getPassword());
            user.setPassword(code);
        }
    }

    //将密码根据需要解密
    public String recoverPwd(BbsUser user) {
        String pwdEncoder1 = user.getPwdEncoder();
        if ("md5_16".equals(pwdEncoder1)) {
            String pwd = scDAO.getSystemConfig().getInitPwd();
            return pwd;
        } else if ("md5_32".equals(pwdEncoder1)) {
            String pwd = scDAO.getSystemConfig().getInitPwd();
            return pwd;
        } else if ("des".equals(pwdEncoder1)) {
            DESTool tool = new DESTool();
            String code = tool.decrypt(user.getPassword());
            return code;
        } else {
            return user.getPassword();
        }
    }

    //将密码找回,如果是MD5,密码无法找回，只能重设
    public String findBackPwd(BbsUser user) {
        String pwdEncoder1 = user.getPwdEncoder();
        if ("md5_16".equals(pwdEncoder1)) {
            String pwd = scDAO.getSystemConfig().getInitPwd();
            user.setPassword(pwd);
            this.handlePwd(user);
            this.bbsUserDAO.updateBbsUser(user);
            return pwd;
        } else if ("md5_32".equals(pwdEncoder1)) {
            String pwd = scDAO.getSystemConfig().getInitPwd();
            user.setPassword(pwd);
            this.handlePwd(user);
            this.bbsUserDAO.updateBbsUser(user);
            return pwd;
        } else if ("des".equals(pwdEncoder1)) {
            DESTool tool = new DESTool();
            String code = tool.decrypt(user.getPassword());
            return code;
        } else {
        	DESTool tool = new DESTool();
            String code = tool.decrypt(user.getPassword());
            return code;
        }
    }

    //传入的pwd为明文，用于与数据库中的password作比较
    public boolean checkPwd(BbsUser user, String pwd) {
        if (pwd == null) {
            return false;
        }
       DESTool tool = new DESTool();
       String origin = tool.decrypt(user.getPassword());
       return pwd.equalsIgnoreCase(origin);
    }

    /**
     * 从第三方接入或者从UCENTER登入
     * @param request
     * @param username
     * @param email
     * @param name
     * @param group
     * @return 
     */
    public String loginFromOutside(HttpServletRequest request, String username, String email, String name, String group) {
        String ip = request.getRemoteAddr();
        ClientSession cs = new ClientSession();
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        BbsUser bu = null;
        Date nowt = new Date();
        if (username == null) {
            return null;
        } else {
            bu = bbsUserDAO.findBbsUserByUrn(username);
            if (bu == null) {
                bu = new BbsUser();
                bu.setUsername(username);
                bu.setPassword(scDAO.getSystemConfig().getInitPwd());
                if (email != null) {
                    bu.setEmail(email);
                }
                if (name != null) {
                    bu.setName(name);
                }
                if (group != null) {
                    /*--检验多个部门的情况--*/
                    StringBuilder sbb = new StringBuilder();
                    String[] groups = group.split(";");
                    for (String g : groups) {
                        if (g != null) {
                            DictionaryModel dm = dicDAO.findDictionaryModelByName(group);
                            if (dm != null) {
                                sbb.append(dm.getId());
                                sbb.append(";");
                            }
                        }
                    }
                    String ids=sbb.toString();
                    /*多个部门处理完毕*/
                    if (!ids.equals("")) {
                        bu.setGroupStr(ids);
                    } else {
                        List<DictionaryModel> dms = dicDAO.findAllDefaultDepartments();
                        if (!dms.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (DictionaryModel dd : dms) {
                                sb.append(dd.getId());
                                sb.append(";");
                            }
                            bu.setGroupStr(sb.toString());
                        } else {
                            bu.setGroupStr(businessId+ ";");
                        }
                    }
                } else {
                    List<DictionaryModel> dms = dicDAO.findAllDefaultDepartments();

                    if (!dms.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (DictionaryModel dd : dms) {
                            sb.append(dd.getId());
                            sb.append(";");
                        }
                        bu.setGroupStr(sb.toString());
                    } else {
                        bu.setGroupStr(businessId + ";");
                    }
                }
                bu.setRegIp(ip);
                bu.setLastIp(ip);
                bu.setRegTime(nowt);
                bu.setLastTime(nowt);
                bbsUserDAO.addBbsUser(bu);
            } else {
                if (email != null) {
                    bu.setEmail(email);
                }
                if (name != null) {
                    bu.setName(name);
                }
                if (group != null) {
                    DictionaryModel dm = dicDAO.findDictionaryModelByName(group);
                    if (dm != null) {
                        bu.setGroupStr(dm.getId() + ";");
                    }
                }
                bu.setLastIp(ip);
                bu.setLastTime(nowt);
                bbsUserDAO.updateBbsUser(bu);
            }
            cs.setUsr(bu);
            cs.setIfLogin(true);
            request.getSession().setAttribute("clientSession", cs);

            return "ok";
        }
    }
    
    
    
    
}
