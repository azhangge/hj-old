<%-- 
    Document   : exam_case
    Created on : 2015-11-16, 13:59:03
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.rerebbs.dao.IBbsScoreRegularDAO,com.fivestars.interfaces.bbs.client.UcUserCode"%>
<%@page import="com.huajie.util.*"%>
<%@page import="com.huajie.service.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page import="com.huajie.exam.model.ExamCase,com.huajie.exam.model.*,com.huajie.service.IBbsScoreLogService,com.huajie.service.IUserSessionStateService,com.huajie.exam.dao.IExamCaseLogDAO"%>

<%
    
    JsonLoginResult jsonResult=new JsonLoginResult();
    String authStr = "urn";
    final String tempId = request.getParameter("urn");
    String pwd = request.getParameter("pwd");

    final IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");

    BbsUser us = null;
    Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
    if (ifLDAP) {//使用第三方验证的情况
        try {
            IThirdPartyUserService third = SpringHelper.getSpringBean("ThirdPartyUserService");
            UcUserCode code = third.loginCheck(tempId, pwd);
            int result = code.getCode();
            if (result == -2) {//验证不正确
                jsonResult.setIfPass(false);
                jsonResult.setResult("密码不正确");
            } else if (result == -1) {
                jsonResult.setIfPass(false);
                jsonResult.setResult("无此用户");
                //如果远程无此用户，本地也应该删除此用户
                new Thread(new Runnable() {
                    public void run() {
                        bbsUserDAO.deleteBbsUserByUrn(tempId);
                    }
                }).start();
            } else if (result >= 0) {//验证成功
                //与第三方同步用户信息
                us = third.synUserFromUcUserCode(code);
            }
            MyLogger.echo(String.valueOf(result));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setIfPass(false);
            jsonResult.setResult("第三方验证已开启，但第三方平台发生内部错误。");
        }
    } else {//不使用第三方验证的情况
        if ("pid".equals(authStr)) {
            us = bbsUserDAO.findBbsUserByPid(tempId);
        } else if ("cid".equals(authStr)) {
            us = bbsUserDAO.findBbsUserByCid(tempId);
        } else if ("urn".equals(authStr)) {
            us = bbsUserDAO.findBbsUserByUrn(tempId);
        }
        if (us == null) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("无此用户");
        }
        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
        boolean b = cbus.checkPwd(us, pwd);
        if (!b) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("密码不正确");
        }
        if (!us.getEnabled()) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("此用户已被禁用！");
        }
        if (!us.isIfInValidTime()) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("此用户不在有效期内！");
        }
        if (us.getGroup() != null) {
            if (!us.getGroup().isIfInValidTime()) {
                jsonResult.setIfPass(false);
                jsonResult.setResult("此用户所在的部门不在有效期内！");
            }
        }
        if (!us.getActivated()) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("此用户尚未激活！");
            return;
        }
        if (!us.getChecked()) {
            jsonResult.setIfPass(false);
            jsonResult.setResult("此用户尚未被审核！");
        }
    }
    if (us == null) {//防止US空指针引用
        return;
    }

    us.setLastIp(request.getRemoteAddr());
    java.util.Date old = us.getLastTime();

    boolean tooShort = true;
    if ((new Date().getTime() - old.getTime()) > 1000L * 60L * 60L * 24L) {
        tooShort = false;
    }

    us.setLastTime(new Date());

    //iussService.login(us);
    if (!tooShort) {
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("每天第一次登录系统", scoreRegularDAO.findScoreRegular().getLogin());
    }
    bbsUserDAO.updateBbsUser(us);
    //JsfHelper.info("登录成功，获得积分"+BBSScoreRegular.LOGIN);

    GsonBuilder gb = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
            .serializeNulls().setDateFormat("yyyy-MM-dd")//时间转化为特定格式      
            .setPrettyPrinting() //对json结果格式化.  
            .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
    //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
    //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

    jsonResult.setUser(us);
    
    Gson gson = gb.create();
    String json = gson.toJson(jsonResult);
    out.print(json);

%>
