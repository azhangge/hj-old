/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.customer.service;

import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.hjedu.customer.entity.BbsUser;

/**
 *
 * @author huajie
 */
public interface IThirdPartyUserService {
    
    public void synUserFromThirdParty(String urn,String pwd) ;
    
    public BbsUser synUserFromUcUserCode(final UcUserCode code);
    
    /**
     * @return 0成功 -1帐号不存在 -2帐号存在，密码错误 -3内部错误
     */
    public UcUserCode loginCheck(String urn, String pwd) throws Exception;
    
    public String synlogout();
    
    public String edit(String $username, String $oldpw, String $newpw, String $email, int $ignoreoldpw, String $questionid, String $answer);
    
    public String register(String $username, String $password, String $email) ;
    
}
