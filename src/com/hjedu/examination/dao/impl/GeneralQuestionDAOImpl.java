package com.hjedu.examination.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.examination.dao.GeneralQuestionDAO;
import com.hjedu.examination.entity.GeneralQuestion;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;

public class GeneralQuestionDAOImpl extends BaseDAOImpl<GeneralQuestion> implements GeneralQuestionDAO {
	@Override
	public Map<String, Object> getGeneralQuestionsByModuleIds(List<String> ids,int firstindex,int maxresult,int type){
		String wheresql = " o.module.id in('";
		for(String id : ids){
			if(StringUtil.isNotEmpty(id)){
				wheresql = wheresql+id+ "','";
			}
		}
		wheresql = wheresql.substring(0, wheresql.length()-2)+")";
		if(type==1){
			wheresql = wheresql+" and o.qtype='choice'";
		}else if(type==2){
			wheresql = wheresql+" and o.qtype='mchoice'";
		}else if(type==3){
			wheresql = wheresql+" and o.qtype='judge'";
		}
		return this.getScrollData(GeneralQuestion.class, firstindex, maxresult, wheresql, null, null);
	}
	@Override
	public void syncGeneralQuestion(GeneralQuestion cq){
		GeneralQuestion gq = this.findById(cq.getId());
		//0修改；1新增
		int type = 0;
		if(gq==null){
			gq = new GeneralQuestion();
			gq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
			type = 1;
		}
		HashMap<String, Object> map = JsonUtil.getQuestionNameAnswer(cq);
		gq.setId(cq.getId());
		gq.setName(cq.getName());
		gq.setGenTime(cq.getGenTime());
		gq.setModule(cq.getModule());
		gq.setRightStr(cq.getRightStr());
		gq.setCleanName(cq.getCleanName());
		gq.setHashCode(cq.getHashCode());
		gq.setQtype(cq.getQtype());
		gq.setType(map.get("type").toString());
		gq.setAnswers(map.get("answer").toString());
		gq.setQuestion(map.get("question").toString());
		gq.setRealoption(map.get("realoption").toString());
		
		if(type==1){
			this.add(gq);
		}else{
			this.update(gq);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public GeneralQuestion findById(String id){
		GeneralQuestion gq = null;
		Object[] ob = {id};
		Map<String, Object> map = this.getScrollData(GeneralQuestion.class, 0, 1, " o.id=?1", ob , null);
		if(null!=map&&map.size()>0){
			List<GeneralQuestion> gqs = (List<GeneralQuestion>)map.get("list");
			if(CollectionUtil.isNotEmpty(gqs)){
				gq = gqs.get(0);
			}
		}
		return gq;
	}
}
