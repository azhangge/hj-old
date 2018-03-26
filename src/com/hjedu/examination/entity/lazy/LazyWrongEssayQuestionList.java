package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.EssayQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongEssayQuestionList extends LazyDataModel<EssayQuestion> {

    private List<EssayQuestion> models = new ArrayList();
    private final IEssayQuestionDAO questionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongEssayQuestionList(String uid) {
        this.uid = uid;
    }

    public List<EssayQuestion> getModels() {
        return models;
    }

    public void setModels(List<EssayQuestion> models) {
        this.models = models;
    }

    @Override
    public EssayQuestion getRowData(String rowKey) {
        for (EssayQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("essay", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongEssayQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<EssayQuestion> tempC = wrongDAO.findWrongEssayQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (EssayQuestion cq : tempC) {
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("essay", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongEssayQuestionByUsr(uid, first, pageSize);
            data.addAll(models);
            Collections.sort(data, new LazySorter<EssayQuestion>(EssayQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<EssayQuestion> tempC = wrongDAO.findWrongEssayQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (EssayQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<EssayQuestion>(EssayQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
