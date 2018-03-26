package com.hjedu.common.dao.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hjedu.common.dao.SuggestionDAO;
import com.hjedu.common.entity.Suggestion;

public class SuggestionDAOImpl extends BaseDAOImpl<Suggestion> implements SuggestionDAO {
	@Override
	public Map<String, Object> getSuggestionsByUser(String userId,int firstindex,int maxresult){
		String wheresql = " o.user.id=?1 ";
		Object[] queryParams = {userId};
		LinkedHashMap<String, String> orderby = new LinkedHashMap<>();
		orderby.put("createTime", "desc");
		return this.getScrollData(Suggestion.class, firstindex, maxresult, wheresql, queryParams, orderby);
	}
}
