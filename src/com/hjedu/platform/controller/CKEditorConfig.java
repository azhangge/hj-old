/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class CKEditorConfig {
    
        String config1="[\n" +
"                        { name: 'document', items : [ 'Source'] },\n" +
"                        { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },\n" +
"                        { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },\n" +
"                        { name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar' ] },\n" +
"                        '/',\n" +
"                        { name: 'basicstyles2', items : ['FontSize','TextColor','BGColor'] },\n" +
"                        { name: 'basicstyles', items : [ 'Bold','Italic','Strike','underline','RemoveFormat' ] },\n" +
"                        { name: 'paragraph', items : [ 'JustifyLeft','JustifyCenter','JustifyRight','NumberedList','BulletedList','Outdent','Indent' ] },\n" +
"                        { name: 'links', items : [ 'Link','Unlink'] },\n" +
"                        ]";

    public String getConfig1() {
        return config1;
    }

    public void setConfig1(String config1) {
        this.config1 = config1;
    }
        
        
        
    
    
}
