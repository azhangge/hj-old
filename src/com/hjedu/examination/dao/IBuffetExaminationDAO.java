package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.buffet.BuffetExamination;

public interface IBuffetExaminationDAO {

    public abstract void addExamination(BuffetExamination m);

    public abstract void updateExamination(BuffetExamination m);

    public abstract void deleteExamination(String id);

    public abstract BuffetExamination findExamination(String id);

    public abstract List<BuffetExamination> findAllExamination(String businessId);
    
    public List<BuffetExamination> findAllShowedExaminationByBusinessId(String businessId);
    
    public List<BuffetExamination> findExaminationByLabelAndBusinessId(String labelId, String businessId);

	List<BuffetExamination> findAllExaminationByAdmin(String businessId);

}
