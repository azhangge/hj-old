package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.WrongKnowledge;

public interface IWrongKnowledgeDAO {

    public abstract void addWrongKnowledge(WrongKnowledge m);

    public abstract void updateWrongKnowledge(WrongKnowledge m);
    
    public abstract void wrongTimesPlusOne(String knowId,String userId);
    
    public void recordWrong(String id, String userId);

    public abstract void deleteWrongKnowledge(String id);
    
    public WrongKnowledge findWrongKnowledgeByKnowledgeAndUsr(String kid,String uid);
    
    public abstract void deleteWrongKnowledgeByKnowledge(String id);
    
    public void deleteWrongKnowledgeByUsr(String id) ;

    public abstract WrongKnowledge findWrongKnowledge(String id);

    public abstract List<WrongKnowledge> findAllWrongKnowledge();

    public abstract List<WrongKnowledge> findWrongKnowledgeByUsr(String userId);

}
