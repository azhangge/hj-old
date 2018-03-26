package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.ICustomerServiceDAO;
import com.hjedu.platform.entity.CustomerService;

public class CustomerServiceDAO implements ICustomerServiceDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public CustomerService getCustomerService() {
        String q = "Select ais from CustomerService ais";
        CustomerService ais = null;
        try {
            ais = (CustomerService) entityManager.createQuery(q).getSingleResult();
        } catch (Exception e) {
        }
        if (ais == null) {
            return new CustomerService();
        } else {
            return ais;
        }
    }

    @Override
    public void updateCustomerService(CustomerService n) {
        this.entityManager.merge(n);
    }

}
