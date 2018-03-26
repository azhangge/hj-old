/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class CKEditorConfig2 {
    
        String config1="[\n" +
"                        { name: 'document', items : [ 'Source'] },\n" +
"                        { name: 'basicstyles2', items : ['FontSize','TextColor','BGColor'] },\n" +
"                        { name: 'insert', items : [ 'Image','Flash','SpecialChar' ] },\n" +
"                        { name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] },\n" +
"                        { name: 'links', items : [ 'Link','Unlink'] },\n" +
"                        ]";

    public String getConfig1() {
        return config1;
    }

    public void setConfig1(String config1) {
        this.config1 = config1;
    }
        
        
        
    
    
}
