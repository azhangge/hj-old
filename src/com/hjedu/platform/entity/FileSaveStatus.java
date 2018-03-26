package com.hjedu.platform.entity;

import com.huajie.util.Cat;


public class FileSaveStatus {

    String id = Cat.getUniqueId();
    long total = 0;
    long readed = 0;
    float percent = 0f;
    boolean saving=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPercent() {
        if (this.total == 0) {
            return 0;
        } else {
            this.percent = this.readed / this.total;
            return percent;
        }
    }

    public void setPercent(float percent) {
        //System.out.println("percent:"+percent);
        this.percent = percent;
    }

    public long getReaded() {
        return readed;
    }

    public void setReaded(long readed) {
        //System.out.println(readed+" readed.");
        this.readed = readed;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }

    public void reset() {
        id = Cat.getUniqueId();
        total = 0;
        readed = 0;
        percent = 0;
        saving=false;
    }
}
