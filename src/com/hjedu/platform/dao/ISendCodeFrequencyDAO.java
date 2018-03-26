package com.hjedu.platform.dao;

import java.util.Date;

import com.huajie.app.model.SendCodeFrequency;

public abstract interface ISendCodeFrequencyDAO {
	
	public abstract void addSendCodeFrequency(SendCodeFrequency sendCodeFrequency);

	public abstract void updateSendCodeFrequency(SendCodeFrequency sendCodeFrequency);

	public abstract int getCountByPhoneOneDay(String phoneno, Date todayTime, Date tomorrowTime);
	
	public abstract int getCountByIPOneDay(String ip, Date todayTime, Date tomorrowTime);
	
	public abstract SendCodeFrequency getByPhone(String phone);
	
	public SendCodeFrequency getByPhoneCode(String phone,String code);
	
}
