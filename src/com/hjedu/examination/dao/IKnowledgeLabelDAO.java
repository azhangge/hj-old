package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.KnowledgeLabel;

public interface IKnowledgeLabelDAO {

    public abstract void addKnowledgeLabel(KnowledgeLabel m);

    public abstract void updateKnowledgeLabel(KnowledgeLabel m);

    public abstract void deleteKnowledgeLabel(String id);

    public abstract KnowledgeLabel findKnowledgeLabel(String id);

    public abstract List<KnowledgeLabel> findAllKnowledgeLabel();
    
    public List<KnowledgeLabel> findAllShowedKnowledgeLabel();
    
    public List<KnowledgeLabel> findAllShowedKnowledgeLabelByType(String typeId);
    
    public List<KnowledgeLabel> findMostKnowledgeLabelByType(String typeId);
    
    

}
