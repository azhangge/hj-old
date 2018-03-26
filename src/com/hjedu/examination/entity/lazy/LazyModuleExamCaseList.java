package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class LazyModuleExamCaseList extends LazyDataModel<ModuleExamCase> {

    private List<ModuleExamCase> models;
    private final IModuleExamCaseDAO questionDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");

    public LazyModuleExamCaseList() {
    }

    public List<ModuleExamCase> getModels() {
        return models;
    }

    public void setModels(List<ModuleExamCase> models) {
        this.models = models;
    }

    @Override
    public ModuleExamCase getRowData(String rowKey) {
        for (ModuleExamCase car : models) {
            if (car.getId().equals(rowKey)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(ModuleExamCase car) {
        return car.getId();
    }

    @Override
    public List<ModuleExamCase> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        List<ModuleExamCase> data = new ArrayList();
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
            int num = 0;
            num = (int) this.questionDAO.getExamCaseNum(businessId);
            this.setRowCount(num);
            //paginate  
            models = questionDAO.findLotsOfExamCase(first, pageSize,businessId);
            //Collections.sort(questions);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findExamCaseByLike(filters,businessId);
            this.setRowCount(models.size());
            //Collections.sort(questions);
            data.addAll(models);
        } //sort  
        else if (!ifFilter && sortField != null) {
            //System.out.println("no filter  sort");
            int num = 0;
            num = (int) this.questionDAO.getExamCaseNum(businessId);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedExamCase(first, pageSize, sortField, ot,businessId);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<ExamCase>(ExamCase.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findExamCaseByLike(filters,businessId);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<ModuleExamCase>(ModuleExamCase.class, sortField, sortOrder));
        }
        return data;
    }
}
