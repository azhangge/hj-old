package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.KnowledgeLabelType;


public interface IKnowledgeLabelTypeDAO {
    
  public abstract void addKnowledgeLabelType(KnowledgeLabelType paramKnowledgeLabelType);

  public abstract KnowledgeLabelType findKnowledgeLabelType(String paramString);

  public abstract void updateKnowledgeLabelType(KnowledgeLabelType paramKnowledgeLabelType);

  public abstract List<KnowledgeLabelType> findAllKnowledgeLabelType();

  public abstract void deleteKnowledgeLabelType(String paramString);
}
