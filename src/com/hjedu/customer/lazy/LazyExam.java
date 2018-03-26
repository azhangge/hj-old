package com.hjedu.customer.lazy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.customer.vo.ExamVO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.huajie.util.SpringHelper;

public class LazyExam extends LazyDataModel<ExamVO> {
	private static final long serialVersionUID = 1L;
	IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");

    @Override
    public List<ExamVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    	boolean ifFilter = false;
        List<ExamVO> models = new LinkedList<>();
        int num = 0;
        int type = SortOrder.ASCENDING.equals(sortOrder) ? 0 : 1;
        for (String filterProperty : filters.keySet()) {
            String filterValue = filters.get(filterProperty).toString();
            if (filterValue != null) {
                ifFilter = true;
                break;
            }
        }
//        if (!ifFilter && sortField == null) {
//            num = (int) examDAO.getNumByCondition(null);
//            models = examDAO.findLessonVOsByCondition(null, first, pageSize, null, 0);
//        } else if (ifFilter && sortField == null) {
//        	num = (int) examDAO.getNumByCondition(filters);
//            models = examDAO.findLessonVOsByCondition(filters, first, pageSize, null, 0);
//        }
//        else if (!ifFilter&&sortField != null) {
//            num = (int)examDAO.getNumByCondition(null);
//            models = examDAO.findLessonVOsByCondition(null, first, pageSize, sortField, type);
//        }
//        else if (ifFilter&&sortField != null) {
//        	num = (int)examDAO.getNumByCondition(filters);
//            models = examDAO.findLessonVOsByCondition(filters, first, pageSize, sortField, type);
//        }
        this.setRowCount(num);
        return models;
    }
}
