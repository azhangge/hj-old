
package com.hjedu.platform.service;

import com.hjedu.customer.entity.BbsUser;


public interface IBbsScoreLogService {

    public void log(String str,long score);
    public void log(String str,long score,BbsUser bu);
    public void log(String str,int score);
    public void log(String str,int score,BbsUser bu);
}
