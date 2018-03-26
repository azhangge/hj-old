package com.hjedu.common.dao;

import java.util.Map;

import com.hjedu.common.entity.Suggestion;

public interface SuggestionDAO extends BaseDAO<Suggestion> {

	Map<String, Object> getSuggestionsByUser(String userId,int firstindex,int maxresult);
}
