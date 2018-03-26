/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function   KeyDown() { //屏蔽鼠标右键、Ctrl+n、shift+F10、F5刷新、退格键  
    if ((window.event.altKey) &&
            ((window.event.keyCode == 37) || //屏蔽   Alt+   方向键   ←  
                    (window.event.keyCode == 39))) {     //屏蔽   Alt+   方向键   →  
        alert("不准你使用ALT+方向键前进或后退网页！");
        event.returnValue = false;
    }
    if ((event.keyCode == 8) || //屏蔽退格删除键  
            (event.keyCode == 116) || //屏蔽   F5   刷新键  
            (event.keyCode == 112) || //屏蔽   F1   刷新键  
            (event.ctrlKey && event.keyCode == 82)) {   //Ctrl   +   R  
        event.keyCode = 0;
        event.returnValue = false;
    }
    if ((event.ctrlKey) && (event.keyCode == 78))       //屏蔽   Ctrl+n  
        event.returnValue = false;
    if ((event.shiftKey) && (event.keyCode == 121))   //屏蔽   shift+F10  
        event.returnValue = false;
    if (window.event.srcElement.tagName == "A" && window.event.shiftKey)
        window.event.returnValue = false;     //屏蔽   shift   加鼠标左键新开一网页  
    if ((window.event.altKey) && (window.event.keyCode == 115)) {   //屏蔽Alt+F4  
        window.showModelessDialog("about:blank", "", "dialogWidth:1px;dialogheight:1px");
        return   false;
    }
    if ((window.event.altkey) && (window.event.keyCode == 27)) {
        alert("认真答题！");
    }
}
function   Showhelp() {
    alert("认真答题！");
    return   false;
}
function closeWindow() {
    window.onbeforeunload = null;
    alert('试卷提交成功，点[确定]后窗口将关闭！');
    var browserName = navigator.appName;
    //window.opener = null; //禁止关闭窗口的提示
    if (browserName == "Netscape")
    {
        window.open('', '_parent', '');
        window.close();
    }
    else if (browserName == "Microsoft Internet Explorer")
    {
        window.opener = "whocares";
        window.close();
    }

    //window.close(); //自动关闭本窗口
}

function closeWindow2() {
    window.onbeforeunload = null;
    //alert('试卷提交成功，点[确定]后窗口将关闭！');
    var browserName = navigator.appName;
    //window.opener = null; //禁止关闭窗口的提示
    if (browserName == "Netscape")
    {
        window.open('', '_parent', '');
        window.close();
    }
    else if (browserName == "Microsoft Internet Explorer")
    {
        window.opener = "whocares";
        window.close();
    }

    //window.close(); //自动关闭本窗口
}

function CountDown()
{
    //alert(maxtime);
    if (maxtime >= 0)
    {
        minutes = Math.floor(maxtime / 60);
        seconds = Math.floor(maxtime % 60);
        msg = minutes + " 分 " + seconds + " 秒";
        jQuery("#remain").html(msg);
        maxtime--;
        passtime++;

    }
    else
    {
        //PF('presubcase11').disable();
        //alert('交卷时间已到，点[确定]后试卷将自动提交！');
        //jQuery("#myForm\\:subcase").trigger("click");
        //clearInterval(timer);
    }
}


function testRetry() {
    var bl = confirm("由于网络问题，未能提交当前考试！！\n系统将尝试再次提交，是否继续？");
    if (bl) {
        jQuery("#myForm\\:subcase").trigger("click");
        clearInterval(timer);
    }
}

function testPreRetry() {
    var bl = confirm("由于网络问题，未能提交当前考试！！\n系统将尝试再次提交，是否继续？");
    if (bl) {
        jQuery("#myForm\\:presubcase11").trigger("click");
        clearInterval(timer);
    }
}


function testClose() {
    window.onbeforeunload = function(e) {
        return "确认试卷提交过才可以关闭窗口，否则考试信息将丢失，自负后果哦！";
    }
}

function reply(cidd) {
    jQuery("html,body").animate({scrollTop: jQuery(cidd).offset().top - 50}, 500);
}



function blur11() {
    alert("考试期间不准离开！");
}

function testSelectDial(id, value) {
    //alert(value);
    var dom1 = jQuery('#dial-' + id);
    if (value == null || value == '') {
        dom1.removeClass('dialA');
        dom1.addClass('dialB');
    } else {
        dom1.removeClass('dialB');
        dom1.addClass('dialA');
    }
}

function testMultiSelectDial(id) {
    //alert('#multi-select-' +id);
    var flag = false;
    var dom1 = jQuery('#multi-select-' + id+' input:checked');
    var value = dom1.length;
    var dom2 = jQuery('#dial-' + id);
    //alert(value);
    if (value == 0) {
        dom2.removeClass('dialA');
        dom2.addClass('dialB');
    } else {
        dom2.removeClass('dialB');
        dom2.addClass('dialA');
    }
}

function testJudgeDial(id, value) {
    //alert(value);
    var dom1 = jQuery('#dial-' + id);
    if (value == null || value == '' || value == 'null') {
        dom1.removeClass('dialA');
        dom1.addClass('dialB');
    } else {
        dom1.removeClass('dialB');
        dom1.addClass('dialA');
    }
}
function testEssayDial(id, value) {
    //alert(value);
    var dom1 = jQuery('#dial-' + id);
    if (value.trim() == '') {
        dom1.removeClass('dialA');
        dom1.addClass('dialB');
    } else {
        dom1.removeClass('dialB');
        dom1.addClass('dialA');
    }
}

function testFileDial(id) {
    //alert(value);
    //var suc = jQuery('#success-' + id);

    var dom1 = jQuery('#dial-' + id);

    dom1.removeClass('dialB');
    dom1.addClass('dialA');

}


function testFillDial(id) {
    //alert('#fill-' + id);
    var flag = false;
    var dom1 = jQuery('#fill-' + id + ' > input');
    var num1 = dom1.length;
    //alert(num1);
    var num2 = 0;
    var dom2 = jQuery('#dial-' + id);
    //alert(num1);
    dom1.each(function() {
        var tm = jQuery(this).val();
        //alert(tm);
        if (tm.trim() != '') {
            num2++;
        }
    });
    if (num1 != num2) {
        dom2.removeClass('dialA');
        dom2.addClass('dialB');
    } else {
        dom2.removeClass('dialB');
        dom2.addClass('dialA');
    }
}

function markQuestion(id, value) {
    //alert(value);
    var dom2 = jQuery('#dial-' + id);
    if (value) {
        dom2.addClass('dialC');
    } else {
        dom2.removeClass('dialC');
    }
}



