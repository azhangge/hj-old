package com.hjedu.customer.dao;


import java.util.List;

import com.hjedu.platform.entity.BbsUserGrade;


public interface IBbsUserGradeDAO {
    public abstract void addUserGrade(BbsUserGrade grade);

  public abstract void updateUserGrade(BbsUserGrade grade);

  public abstract void deleteUserGrade(String id);

  public abstract BbsUserGrade findUserGrade(String id);

  public abstract List<BbsUserGrade> findAllUserGrade();
}
