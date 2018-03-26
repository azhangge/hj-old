package com.fivestars.interfaces.bbs.client;

/**
 *
 * @author huajie.com
 */
public class UCenterClientAgent {

    Client client = new Client();

    /**
     * 返回一个实例
     * @return 
     */
    public static UCenterClientAgent newInstance() {
        return new UCenterClientAgent();
    }

    /**
     * 用户注册
     *
     * @param $username 用户名
     * @param $password 密码
     * @param $email	Email
     * @return int -1 : 用户名不合法 -2 : 包含不允许注册的词语 -3 : 用户名已经存在 -4 : email 格式有误 -5 :
     * email 不允许注册 -6 : 该 email 已经被注册 >1 : 表示成功，数值为 UID
     */
    public String register(String $username, String $password, String $email) {
        return client.uc_user_register($username, $password, $email, "", "");
    }

    public String register(String $username, String $password, String $email, String $questionid, String $answer) {
        return client.uc_user_register($username, $password, $email, $questionid, $answer);
    }

    /**
     * 用户登陆检查
     *
     * @param $username	用户名/uid
     * @param $password	密码
     * @return array (uid/status, username, password, email) 数组第一项 1 : 成功 -1
     * :用户不存在,或者被删除 -2 : 密码错
     */
    public String login(String $username, String $password) {
        return client.uc_user_login($username, $password, 0, 0);
    }

    /**
     * 用户登陆检查
     *
     * @param $username	用户名/uid
     * @param $password	密码
     * @param $isuid	是否为uid
     * @param $checkques	是否使用检查安全问答
     * @return array (uid/status, username, password, email) 数组第一项 1 : 成功 -1 :
     * 用户不存在,或者被删除 -2 : 密码错
     */
    public String login(String $username, String $password, int $isuid, int $checkques) {
        return client.uc_user_login($username, $password, $isuid, $checkques, "", "");
    }

    /**
     * 用户登陆检查
     *
     * @param $username	用户名/uid
     * @param $password	密码
     * @param $isuid	是否为uid
     * @param $checkques	是否使用检查安全问答
     * @param $questionid	安全提问
     * @param $answer 安全提问答案
     * @return array (uid/status, username, password, email) 数组第一项 1 : 成功 -1 :
     * 用户不存在,或者被删除 -2 : 密码错
     */
    public String login(String $username, String $password, int $isuid, int $checkques, String $questionid, String $answer) {
        return client.uc_user_login($username, $password, $isuid, $checkques, $questionid, $answer);
    }

    /**
     * 进入同步登录代码
     *
     * @param $uid	用户ID
     * @return string HTML代码
     */
    public String synlogin(int $uid) {
        return client.uc_user_synlogin($uid);
    }

    /**
     * 进入同步登出代码
     *
     * @return string HTML代码
     */
    public String synlogout() {
        return client.uc_user_synlogout();
    }

    /**
     * 取得用户数据
     *
     * @param $username	用户名
     * @param $isuid	是否为UID
     * @return array (uid, username, email)
     */
    public String getUser(String $username, int $isuid) {
        return client.uc_get_user($username, $isuid);
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
    public String edit(String $username, String $oldpw, String $newpw, String $email, int $ignoreoldpw, String $questionid, String $answer) {
        return client.uc_user_edit($username, $oldpw, $newpw, $email, $ignoreoldpw, $questionid, $answer);
    }

    /**
     * 删除用户
     *
     * @param $uid	用户的 UID
     * @return int >0 : 成功 0 : 失败
     */
    public String delete(String $uid) {
        return client.uc_user_delete($uid);
    }

    /**
     * 删除用户头像
     *
     * @param $uid	用户的 UID
     */
    public String deleteavatar(String $uid) {
        return client.uc_user_deleteavatar($uid);
    }

}
