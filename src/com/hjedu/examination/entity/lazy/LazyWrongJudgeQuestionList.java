package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.JudgeQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongJudgeQuestionList extends LazyDataModel<JudgeQuestion> {

    private List<JudgeQuestion> models = new ArrayList();
    private final IJudgeQuestionDAO questionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongJudgeQuestionList(String uid) {
        this.uid = uid;
    }

    public List<JudgeQuestion> getModels() {
        return models;
    }

    public void setModels(List<JudgeQuestion> models) {
        this.models = models;
    }

    @Override
    public JudgeQuestion getRowData(String rowKey) {
        for (JudgeQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(JudgeQuestion car) {
        return car.getId();
    }

    @Override
    public List<JudgeQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<JudgeQuestion> data = new ArrayList();
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("judge", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongJudgeQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<JudgeQuestion> tempC = wrongDAO.findWrongJudgeQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (JudgeQuestion cq : tempC) {
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("judge", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongJudgeQuestionByUsr(uid, first, pageSize);
            data.addAll(models);
            Collections.sort(data, new LazySorter<JudgeQuestion>(JudgeQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<JudgeQuestion> tempC = wrongDAO.findWrongJudgeQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (JudgeQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<JudgeQuestion>(JudgeQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
