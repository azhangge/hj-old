package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.ISendCodeFrequencyDAO;
import com.huajie.app.model.SendCodeFrequency;
import com.huajie.app.util.DateUtil;

public class SendCodeFrequencyDAO implements ISendCodeFrequencyDAO, Serializable {
	
	@PersistenceContext
    private EntityManager entityManager;
	
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public void addSendCodeFrequency(SendCodeFrequency sendCodeFrequency) {
		this.entityManager.persist(sendCodeFrequency);
	}

	@Override
	public void updateSendCodeFrequency(SendCodeFrequency sendCodeFrequency) {
		this.entityManager.merge(sendCodeFrequency);
	}

	@Override
	public SendCodeFrequency getByPhone(String phone) {
		String q = "Select scf from SendCodeFrequency scf where scf.phone=:phone order by scf.sendLastTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        List<SendCodeFrequency> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
        	SendCodeFrequency ai = (SendCodeFrequency) as.get(0);
            return ai;
        }
	}
	
	@Override
	public SendCodeFrequency getByPhoneCode(String phone,String code) {
		String q = "Select scf from SendCodeFrequency scf where scf.phone=:phone and scf.code=:code order by scf.sendLastTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        qu.setParameter("code", code);
        List<SendCodeFrequency> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
        	SendCodeFrequency ai = (SendCodeFrequency) as.get(0);
            return ai;
        }
	}
	
	@Override
	public int getCountByPhoneOneDay(String phone,Date todayTime,Date tomorrowTime) {//此手机号24小时内发送的次数
		String q = "Select count(scf) from SendCodeFrequency scf where scf.phone=:phone and scf.sendLastTime>:todayTime and scf.sendLastTime<:tomorrowTime";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("phone", phone);
        Date date = new Date();
        qu.setParameter("todayTime", todayTime);
        qu.setParameter("tomorrowTime", tomorrowTime);
        List<Long> as = qu.getResultList();
        return Integer.valueOf(String.valueOf(as.get(0)));
	}
	
	@Override
	public int getCountByIPOneDay(String ip,Date todayTime,Date tomorrowTime) {//此IP段24小时内发送的次数
		String q = "Select count(scf) from SendCodeFrequency scf where scf.ip=:ip and scf.sendLastTime>:todayTime and scf.sendLastTime<:tomorrowTime";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("ip", ip);
        Date date = new Date();
        qu.setParameter("todayTime", todayTime);
        qu.setParameter("tomorrowTime", tomorrowTime);
        List<Long> as = qu.getResultList();
        return Integer.valueOf(String.valueOf(as.get(0)));
	}

}