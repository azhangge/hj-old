
package com.hjedu.course.vo;

import java.util.UUID;
import org.primefaces.model.mindmap.DefaultMindmapNode;

/**
 *  在DefaultMindmapNode基础上增加ID等属性，以例选择时能够区分标签
 * @author huajie
 */
public class RereMindmapNode extends DefaultMindmapNode {

    private String id = UUID.randomUUID().toString();
    private String type;

    public RereMindmapNode(String label, Object data, String fill, boolean selectable) {
        super(label, data, fill, selectable);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
