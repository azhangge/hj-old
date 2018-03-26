package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.buffet.BuffetExaminationPart;

public interface IBuffetExaminationPartDAO {

    public abstract void addBuffetExaminationPart(BuffetExaminationPart m);

    public abstract void updateBuffetExaminationPart(BuffetExaminationPart m);

    public abstract void deleteBuffetExaminationPart(String id);

    public abstract BuffetExaminationPart findBuffetExaminationPart(String id);

    public abstract List<BuffetExaminationPart> findAllBuffetExaminationPart();
    
    public List<BuffetExaminationPart> findAllBuffetExaminationPartByPaper(String pid);

}
