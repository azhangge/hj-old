package com.hjedu.platform.entity;

import com.hjedu.customer.entity.BbsUser;
import com.huajie.util.Cat;

public class CooperatorWrapper {

    String id = Cat.getUniqueId();
    boolean selected;
    BbsUser user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

}
