/*
 Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function (config)
{

    // Declare the additional plugin 


    // Add the button to toolbar


    config.toolbar = [
        {name: 'document', items: ['Source']},
        {name: 'basicstyles2', items: ['FontSize', 'TextColor', 'BGColor']},
        {name: 'basicstyles', items: ['Bold', 'Underline', 'Italic', 'Strike', 'RemoveFormat']},
        {name: 'insert', items: ['Image', 'Flash', 'Table', 'HorizontalRule']},
        {name: 'maths', items: ['Subscript', 'Superscript']}
    ]
// 换行方式  
    config.enterMode = CKEDITOR.ENTER_BR;

    // 当输入：shift+Enter是插入的标签  
    config.shiftEnterMode = CKEDITOR.ENTER_BR;//   
    //
    // 当提交包含有此编辑器的表单时，是否自动更新元素内的数据
    config.autoUpdateElement = false;
    config.allowedContent = true;

    //图片处理  
    config.pasteFromWordRemoveStyles = true;

    // 去掉ckeditor“保存”按钮  
    config.removePlugins = 'save';
    //去掉状态栏，并不允许调整编辑框大小
    config.removePlugins = 'elementspath';
    config.resize_enabled = false;

    // 编辑器样式，有2种：'moono'（默认）、'kama'
    config.skin = 'moono';

};



CKEDITOR.plugins.add("texzilla", {
    icons: "texzilla",
    init: function (editor) {
        CKEDITOR.dialog.add("texzillaDialog",
                this.path + "dialogs/texzilla.js");

        editor.addCommand("texzillaDialog",
                new CKEDITOR.dialogCommand("texzillaDialog"));
        editor.ui.addButton("texzilla", {
            label: "Insert MathML based on (La)TeX",
            command: "texzillaDialog",
            icon: this.path + "icons/texzilla.png",
            toolbar: "insert"
        });
    }
});