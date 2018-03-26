package com.hjedu.course.lazy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.huajie.util.SpringHelper;

public class LazyLessonType extends LazyDataModel<LessonType> {
	private static final long serialVersionUID = 1L;
	ILessonTypeDAO LessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	private String whereSql;

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	
	public LazyLessonType(String whereSql){
		this.whereSql = whereSql;
	}
	
	public LazyLessonType(){}

	@Override
	public List<LessonType> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		boolean ifFilter = false;
		List<LessonType> models = new LinkedList<>();
		int num = 0;
		int type = SortOrder.ASCENDING.equals(sortOrder) ? 0 : 1;
		if(filters!=null){
			for (String filterProperty : filters.keySet()) {
				String filterValue = filters.get(filterProperty).toString();
				if (filterValue != null) {
					ifFilter = true;
					break;
				}
			}
		}
		if (!ifFilter && sortField == null) {
			num = (int) LessonTypeDAO.getNumByCondition(whereSql,null);
			models = LessonTypeDAO.findLessonTypesByCondition(whereSql,null, first, pageSize, null, 0);
		} else if (ifFilter && sortField == null) {
			num = (int) LessonTypeDAO.getNumByCondition(whereSql,filters);
			models = LessonTypeDAO.findLessonTypesByCondition(whereSql,filters, first, pageSize, null, 0);
		} else if (!ifFilter && sortField != null) {
			num = (int) LessonTypeDAO.getNumByCondition(whereSql,null);
			models = LessonTypeDAO.findLessonTypesByCondition(whereSql,null, first, pageSize, sortField, type);
		} else if (ifFilter && sortField != null) {
			num = (int) LessonTypeDAO.getNumByCondition(whereSql,filters);
			models = LessonTypeDAO.findLessonTypesByCondition(whereSql,filters, first, pageSize, sortField, type);
		}
		this.setRowCount(num);
		return models;
	}
}
