package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.entity.EssayQuestion;
import com.huajie.util.SpringHelper;
 
public class LazyEssayQuestionList extends LazyDataModel<EssayQuestion> {

    private List<EssayQuestion> models;
    private final IEssayQuestionDAO questionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    private String mid;

    public LazyEssayQuestionList(String mid) {
        this.mid = mid;
    }

    public List<EssayQuestion> getModels() {
        return models;
    }

    public void setModels(List<EssayQuestion> models) {
        this.models = models;
    }

    
    
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public EssayQuestion getRowData(String rowKey) {
        for (EssayQuestion car : models) {
            if (car.getId().equals(rowKey)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(EssayQuestion car) {
        return car.getId();
    }

    @Override
    public List<EssayQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<EssayQuestion> data = new ArrayList();
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
            models = questionDAO.findEssayQuestionByModule(mid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findEssayQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedEssayQuestionByModule(mid, first, pageSize, sortField, ot);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<EssayQuestion>(EssayQuestion.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findEssayQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<EssayQuestion>(EssayQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
