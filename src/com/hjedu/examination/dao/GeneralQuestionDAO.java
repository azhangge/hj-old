package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.examination.entity.GeneralQuestion;

public interface GeneralQuestionDAO extends BaseDAO<GeneralQuestion> {

	Map<String, Object> getGeneralQuestionsByModuleIds(List<String> ids, int firstindex, int maxresult,int type);

	void syncGeneralQuestion(GeneralQuestion cq);
	
}
