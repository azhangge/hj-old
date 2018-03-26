package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.FillQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongFillQuestionList extends LazyDataModel<FillQuestion> {

    private List<FillQuestion> models = new ArrayList();
    private final IFillQuestionDAO questionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongFillQuestionList(String uid) {
        this.uid = uid;
    }

    public List<FillQuestion> getModels() {
        return models;
    }

    public void setModels(List<FillQuestion> models) {
        this.models = models;
    }

    @Override
    public FillQuestion getRowData(String rowKey) {
        for (FillQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(FillQuestion car) {
        return car.getId();
    }

    @Override
    public List<FillQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<FillQuestion> data = new ArrayList();
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("fill", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongFillQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<FillQuestion> tempC = wrongDAO.findWrongFillQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (FillQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter && sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("fill", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongFillQuestionByUsr(uid, first, pageSize);
            data.addAll(models);
            Collections.sort(data, new LazySorter<FillQuestion>(FillQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<FillQuestion> tempC = wrongDAO.findWrongFillQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (FillQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<FillQuestion>(FillQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
