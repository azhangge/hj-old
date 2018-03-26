package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.huajie.util.SpringHelper;

public class LazyChoiceQuestionList extends LazyDataModel<ChoiceQuestion> {

    private List<ChoiceQuestion> models;
    private final IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    private String mid;

    public LazyChoiceQuestionList(String mid) {
        this.mid = mid;
    }

    public List<ChoiceQuestion> getModels() {
        return models;
    }

    public void setModels(List<ChoiceQuestion> models) {
        this.models = models;
    }

    
    
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public ChoiceQuestion getRowData(String rowKey) {
        for (ChoiceQuestion c: models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(ChoiceQuestion car) {
        return car.getId();
    }

    @Override
    public List<ChoiceQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<ChoiceQuestion> data = new ArrayList();
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
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            //paginate  
            models = questionDAO.findChoiceQuestionByModule(mid, first, pageSize);
           Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findChoiceQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedChoiceQuestionByModule(mid, first, pageSize, sortField, ot);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<ChoiceQuestion>(ChoiceQuestion.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findChoiceQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<ChoiceQuestion>(ChoiceQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
