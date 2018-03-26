package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.random2.Random2PaperPart;

public interface IRandom2PaperPartDAO {

    public abstract void addRandom2PaperPart(Random2PaperPart m);

    public abstract void updateRandom2PaperPart(Random2PaperPart m);

    public abstract void deleteRandom2PaperPart(String id);

    public abstract Random2PaperPart findRandom2PaperPart(String id);

    public abstract List<Random2PaperPart> findAllRandom2PaperPart();
    
    public List<Random2PaperPart> findAllRandom2PaperPartByPaper(String pid);

}
