package com.hjedu.course.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.examination.entity.lazy.LazySorter;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class LazyStudyPlan extends LazyDataModel<StudyPlan> {

    IStudyPlanDAO StudyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");

    @Override
    public List<StudyPlan> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	Long begin = new Date().getTime();
    	boolean ifFilter = false;
        List<StudyPlan> models = new LinkedList<>();
        List<StudyPlan> data = new ArrayList<>();
        for (String filterProperty : filters.keySet()) {
            String filterValue = filters.get(filterProperty).toString();
            if (filterValue != null) {
                ifFilter = true;
                break;
            }
        }

        if (!ifFilter && sortField == null) {
            //System.out.println("no filter no sort");
            //rowCount  
            int num = (int) this.StudyPlanDAO.getStudyPlansNumByOrder(null, false,businessId);
            this.setRowCount(num);
            //paginate  
            models = StudyPlanDAO.findStudyPlansByOrder(null, first, pageSize, false,businessId);
            if(models!=null)
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = StudyPlanDAO.findStudyPlansByFilter(filters,businessId);
            this.setRowCount(models.size());
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.StudyPlanDAO.getStudyPlansNumByOrder(null, false,businessId);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = StudyPlanDAO.findOrderedStudyPlans(first, pageSize, sortField, ot,businessId);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<StudyPlan>(StudyPlan.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = StudyPlanDAO.findStudyPlansByFilter(filters,businessId);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<StudyPlan>(StudyPlan.class, sortField, sortOrder));
        }
        Long end = new Date().getTime();
        System.out.println(end-begin);
        return data;
    }
}
