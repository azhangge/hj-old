package com.huajie.seller.dao.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.huajie.seller.dao.IGoodsModule2DAO;
import com.huajie.seller.model.GoodsModuleModel;
import com.huajie.util.SpringHelper;

public class GoodsModule2DAO implements IGoodsModule2DAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }


    @Override
    public void addGoodsModuleModel(GoodsModuleModel p) {
        entityManager.persist(p);
    }

    @Override
    public void deleteGoodsModuleModel(String pid) {
        //this.mbuffetPartDAO.deleteModuleBuffetPartByModule(pid);
        //this.mrandom2PartDAO.deleteModuleBuffetPartByModule(pid);
        GoodsModuleModel p = entityManager.find(GoodsModuleModel.class, pid);
        entityManager.remove(p);
    }

    @Override
    public void deleteGoodsModuleModelAndAllDescendants(String id) {
        List<GoodsModuleModel> sons = new ArrayList();
        this.loadAllDescendants(id, sons);

        for (GoodsModuleModel s : sons) {
            try {
                //this.mbuffetPartDAO.deleteModuleBuffetPartByModule(s.getId());
                //this.mrandom2PartDAO.deleteModuleBuffetPartByModule(s.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            entityManager.remove(s);
        }
        this.deleteGoodsModuleModel(id);
    }

    @Override
    public List<GoodsModuleModel> loadAllDescendants(String fid) {
        List<GoodsModuleModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }

    @Override
    public void loadAllDescendants(String fid, List<GoodsModuleModel> sons) {
        List<GoodsModuleModel> ls = this.findAllSonGoodsModuleModel(fid);
        if (ls.isEmpty()) {
            return;
        } else {
            for (GoodsModuleModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }

    @Override
    public List<GoodsModuleModel> findAllGoodsModuleModel() {
        String q = "Select ass from GoodsModuleModel ass order by ass.ord";
        List<GoodsModuleModel> ys = entityManager.createQuery(q).getResultList();
        return ys;
    }

    @Override
    public List<GoodsModuleModel> findRootGoodsModuleModel() {
        String q = "Select ass from GoodsModuleModel ass where ass.fatherId=:id order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", SpringHelper.getSpringBean("goods_module_root_id").toString());
        List<GoodsModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<GoodsModuleModel> findGoodsModuleModelByUrlType(final String type) {
        String q = "Select ass from GoodsModuleModel ass where ass.urlType=:type order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("type", type);
        List<GoodsModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<GoodsModuleModel> findAllSonGoodsModuleModel(final String fatherID) {
        String q = "Select ass from GoodsModuleModel ass where ass.fatherId=:id order by ass.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        List<GoodsModuleModel> ys = qu.getResultList();
        return ys;
    }

    @Override
    public GoodsModuleModel findExamModuleByName(String name) {
        String q = "Select cq from GoodsModuleModel cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        List<GoodsModuleModel> modules = qu.getResultList();
        if (!modules.isEmpty()) {
            GoodsModuleModel m = (GoodsModuleModel) modules.get(0);
            return m;
        } else {
            return null;
        }
    }

    @Override
    public void deleteAllExamModule() {
        List<GoodsModuleModel> ms = this.findAllGoodsModuleModel();
        String s = SpringHelper.getSpringBean("goods_module_root_id").toString();
        for (GoodsModuleModel e : ms) {
            if (!e.getId().toString().equals(s)) {
                this.deleteGoodsModuleModel(e.getId());
            }
        }
    }

    @Override
    public GoodsModuleModel findGoodsModuleModel(String pid) {
        if (pid == null) {
            return null;
        } else {
            return entityManager.find(GoodsModuleModel.class, pid);
        }
    }

    @Override
    public void updateGoodsModuleModel(GoodsModuleModel p) {
        entityManager.merge(p);
    }
}
