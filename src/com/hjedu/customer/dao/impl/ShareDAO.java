package com.hjedu.customer.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.ShareModel;
import com.hjedu.platform.dao.IShareDAO;
import com.hjedu.platform.entity.BbsFileModel;

public class ShareDAO implements IShareDAO {

    private IBbsUserDAO cu;
    private IBbsFileDAO cf;
    @PersistenceContext
    private EntityManager entityManager;
    

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IBbsUserDAO getCu() {
        return cu;
    }

    public void setCu(IBbsUserDAO cu) {
        this.cu = cu;
    }

    public IBbsFileDAO getCf() {
        return cf;
    }

    public void setCf(IBbsFileDAO cf) {
        this.cf = cf;
    }

    @Override
    public List<BbsUser> findSharedUsersByFile(final String fileId) {

        String q = "Select us from ShareModel us where us.fid='" + fileId + "'";
        List<ShareModel> ais = this.entityManager.createQuery(q).getResultList();

        List pms = new ArrayList();
        for (ShareModel c : ais) {
            BbsUser p = cu.findBbsUser(c.getUid());
            if (p != null) {
                pms.add(p);
            }
        }
        Collections.sort(pms);
        return pms;
    }

    @Override
    public void updateFileShareUser(final String fileId, List<BbsUser> users) {

        String q = "delete from ShareModel cus where cus.fid='" + fileId + "'";
        this.entityManager.createQuery(q).executeUpdate();

        for (BbsUser c : users) {
            ShareModel s = new ShareModel();
            s.setFid(fileId);
            s.setUid(c.getId());
            this.entityManager.persist(s);
        }
    }

    @Override
    public List<BbsFileModel> findSharedBbsFileByUsr(final String userId) {

        String q = "Select us from ShareModel us where us.uid='" + userId + "'";
        List<ShareModel> ais = this.entityManager.createQuery(q).getResultList();

        List pms = new ArrayList();
        for (ShareModel c : ais) {
            BbsFileModel p = cf.findClientFile(c.getFid());
            if (p != null) {
                pms.add(p);
            }
        }
        Collections.sort(pms);
        Collections.reverse(pms);
        return pms;
    }
}
