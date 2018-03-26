package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.entity.BbsTalk;
import com.hjedu.platform.entity.BbsThread;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class UserTalkList  implements Serializable{
	
	List<BbsThread> threads;
	List<BbsTalk> talks;
	IBbsUserDAO userDAO=SpringHelper.getSpringBean("BbsUserDAO");
	IBbsThreadDAO threadDAO=SpringHelper.getSpringBean("BbsThreadDAO");
	IBbsTalkDAO talkDAO=SpringHelper.getSpringBean("BbsTalkDAO");
	
	
	
	
	public List<BbsThread> getThreads() {
		return threads;
	}




	public void setThreads(List<BbsThread> threads) {
		this.threads = threads;
	}




	public List<BbsTalk> getTalks() {
		return talks;
	}




	public void setTalks(List<BbsTalk> talks) {
		this.talks = talks;
	}




	@PostConstruct
	public void init(){
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id=request.getParameter("id");
		BbsUser user=userDAO.findBbsUser(id);
		this.threads=threadDAO.findByGenBy(user, 0,1000);
		this.talks=talkDAO.findByGenBy(user, 0,1000);
	}

}
