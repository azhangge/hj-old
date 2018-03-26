package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.UUID;

public class AuthorityWraper implements Serializable {

    private String id=UUID.randomUUID().toString();
    private String name = "";
    private String cnName = "";
    private boolean selected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
