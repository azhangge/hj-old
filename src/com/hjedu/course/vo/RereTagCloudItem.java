package com.hjedu.course.vo;

import java.util.UUID;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;

/**
 * 在DefaultTagCloudItem基础上增加ID属性，以例选择时能够区分标签
 * @author huajie
 */
public class RereTagCloudItem extends DefaultTagCloudItem {

    private String id = UUID.randomUUID().toString();

    public RereTagCloudItem(String label, int strength) {
        super(label, strength);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
