package com.hjedu.customer.service.impl;

import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.fivestars.interfaces.bbs.util.XMLHelper;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class LDAPUserService implements IThirdPartyUserService {

    private String server;
    private int port;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * LDAP帐号密码检查
     *
     * @param zgh 职工号
     * @param passwd 密码
     * @return 0成功 -1帐号不存在 -2帐号存在，密码错误 -3内部错误
     */
    public UcUserCode loginCheck(String zgh, String passwd) throws Exception {
        int ret = -3;
        String req = "s:26:\"do_login::ldap_login_check\";a:2:{i:0;s:" + zgh.length() + ":\"" + zgh + "\";i:1;s:" + passwd.length() + ":\"" + passwd + "\";}";
        req = req.length() + "," + req;
        Socket socket = null;
        String rsp = "";
        try {
            socket = new Socket(server, port);
            socket.getOutputStream().write(req.getBytes());
            byte[] buffer = new byte[1024];
            int length = socket.getInputStream().read(buffer);
            if (length > 0) {
                rsp = new String(buffer, 0, length).trim();
            }
        } catch (Exception e) {
            MyLogger.echo("调用LDAP接口异常", e);
            throw e;
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        if (rsp.startsWith("Si")) {
            try {
                ret = Integer.parseInt(rsp.substring("Si:".length(), rsp.length() - 1));
            } catch (Exception e) {
                MyLogger.echo("解析LDAP接口返回值异常", e);
                throw e;
            }
        }
        UcUserCode code = new UcUserCode();
        code.setCode(ret);
        BbsUser bu = new BbsUser();
        bu.setUsername(zgh);
        bu.setPassword(passwd);
        code.setUser(bu);
        return code;
    }

    @Override
    public void synUserFromThirdParty(final String urn, final String pwd) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
                    BbsUser us = bbsUserDAO.findBbsUserByUrn(urn);
                    if (us == null) {//如果此用户第三方验证不存在，则在系统数据库中添加此用户
                        us = new BbsUser();
                        us.setUsername(urn);
                        us.setPassword(pwd);
                        bbsUserDAO.addBbsUser(us);
                    } else {
                        us.setPassword(pwd);
                        bbsUserDAO.updateBbsUser(us);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(run).start();
    }

    /**
     * 从UCENTER调用此用户的信息并同步本地数据库
     *
     * @param code
     * @return
     */
    @Override
    public BbsUser synUserFromUcUserCode(final UcUserCode code) {
        BbsUser us = null;
        try {
            String id = String.valueOf(code.getCode());
            String userName = code.getUser().getUsername();
            String email = code.getUser().getEmail();
            String pwd = code.getUser().getPassword();

            IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
            us = bbsUserDAO.findBbsUserByUrn(userName);
            if (us == null) {//如果此用户第三方验证不存在，则在系统数据库中添加此用户
                us = new BbsUser();
                us.setUsername(userName);
                us.setPassword(pwd);
                //us.setEmail(email);
                //us.setExternalId(id);
                cbus.handlePwd(us);
                bbsUserDAO.addBbsUser(us);
            } else {
                us.setPassword(pwd);
                us.setEmail(email);
                //us.setExternalId(id);
                cbus.handlePwd(us);
                bbsUserDAO.updateBbsUser(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return us;

    }

    @Override
    public String synlogout() {
        return null;
    }

    @Override
    public String edit(String $username, String $oldpw, String $newpw, String $email, int $ignoreoldpw, String $questionid, String $answer) {
        return null;
    }

    @Override
    public String register(String $username, String $password, String $email) {
        return null;
    }

    public static void main(String args[]) {
        String urn = "test";
        String pwd = "123456";
        try {
            IThirdPartyUserService uchecker = SpringHelper.getSpringBean("ThirdPartyUserService");
            int result = uchecker.loginCheck(urn, pwd).getCode();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
