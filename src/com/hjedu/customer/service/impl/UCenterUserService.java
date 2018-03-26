package com.hjedu.customer.service.impl;

import com.fivestars.interfaces.bbs.client.UCenterClientAgent;
import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.fivestars.interfaces.bbs.util.XMLHelper;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.huajie.util.SpringHelper;

import java.util.List;

/**
 *
 * @author huajie.com
 */
public class UCenterUserService implements IThirdPartyUserService {

    UCenterClientAgent agent = UCenterClientAgent.newInstance();

    /**
     * 用户注册
     *
     * @param $username 用户名
     * @param $password 密码
     * @param $email	Email
     * @return int -1 : 用户名不合法 -2 : 包含不允许注册的词语 -3 : 用户名已经存在 -4 : email 格式有误 -5 :
     * email 不允许注册 -6 : 该 email 已经被注册 >1 : 表示成功，数值为 UID
     */
    @Override
    public String register(final String $username, final String $password, final String $email) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                agent.register($username, $password, $email, "", "");
            }
        };
        new Thread(run).start();
        return null;
    }

    /**
     * 进入同步登出代码
     *
     * @return string HTML代码
     */
    @Override
    public String synlogout() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                agent.synlogout();
            }
        };
        new Thread(run).start();
        return null;
    }

    /**
     * 编辑用户
     *
     * @param $username	用户名
     * @param $oldpw	旧密码
     * @param $newpw	新密码
     * @param $email	Email
     * @param $ignoreoldpw 是否忽略旧密码, 忽略旧密码, 则不进行旧密码校验.
     * @param $questionid	安全提问
     * @param $answer 安全提问答案
     * @return int 1 : 修改成功 0 : 没有任何修改 -1 : 旧密码不正确 -4 : email 格式有误 -5 : email
     * 不允许注册 -6 : 该 email 已经被注册 -7 : 没有做任何修改 -8 : 受保护的用户，没有权限修改
     */
    @Override
    public String edit(final String $username, final String $oldpw, final String $newpw, final String $email, final int $ignoreoldpw, final String $questionid, final String $answer) {
        new Thread(new Runnable() {
            public void run() {
                agent.edit($username, $oldpw, $newpw, $email, $ignoreoldpw, $questionid, $answer);
            }
        }).start();
        return null;
    }

    /**
     * 从UCENTER调用此用户的信息并同步本地数据库
     *
     * @param urn
     * @param pwd 查询数据时ucenter不返回密码，因此需要自己提供
     */
    @Override
    public void synUserFromThirdParty(final String urn, final String pwd) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String str = agent.getUser(urn, 0);
                System.out.print("str:" + str);
                List<String> list = XMLHelper.uc_unserialize(str);
                try {
                    String id = list.get(0).trim();
                    String userName = list.get(1);
                    String email = list.get(2);

                    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
                    ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
                    BbsUser us = bbsUserDAO.findBbsUserByUrn(userName);
                    if (us == null) {//如果此用户第三方验证不存在，则在系统数据库中添加此用户
                        us = new BbsUser();
                        us.setUsername(userName);
                        us.setPassword(pwd);
                        us.setEmail(email);
                        us.setExternalId(id);
                        cbus.handlePwd(us);
                        bbsUserDAO.addBbsUser(us);
                    } else {
                        us.setPassword(pwd);
                        us.setEmail(email);
                        us.setExternalId(id);
                        cbus.handlePwd(us);
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
                us.setEmail(email);
                us.setExternalId(id);
                cbus.handlePwd(us);
                bbsUserDAO.addBbsUser(us);
            } else {
                us.setPassword(pwd);
                us.setEmail(email);
                us.setExternalId(id);
                cbus.handlePwd(us);
                bbsUserDAO.updateBbsUser(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return us;
    
}

/**
 * LDAP帐号密码检查
 *
 * @param zgh 用户名
 * @param passwd 密码
 * @return 0成功 -1帐号不存在 -2帐号存在，密码错误 -3内部错误
 */
@Override
        public UcUserCode loginCheck(String zgh, String passwd) throws Exception {
        UcUserCode code = new UcUserCode();
        try {
            String result = agent.login(zgh, passwd);
            List<String> list = XMLHelper.uc_unserialize(result);
            int ret = Integer.parseInt(list.get(0).trim());
            code.setCode(ret);
            if (ret >= 0) {
                String userName = list.get(1);
                String pwd = list.get(2).trim();
                String email = list.get(3);
                BbsUser bu = new BbsUser();
                bu.setUsername(userName);
                bu.setPassword(pwd);
                bu.setEmail(email);
                code.setUser(bu);
            }
        } catch (Exception e) {
            code.setCode(-3);
        }
        return code;
    }

    public static void main(String args[]) {
        String urn = "lteb2002";
        String pwd = "122128148";
        try {
            IThirdPartyUserService uchecker = SpringHelper.getSpringBean("ThirdPartyUserService");
            uchecker.synUserFromThirdParty(urn, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.exit(0);
    }

}
