<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page  import="com.hjedu.platform.dao.ISystemInfoDAO"%>
<%
    String path = "";
    path = request.getContextPath();
    ISystemInfoDAO ts = com.huajie.util.SpringHelper.getSpringBean("SystemInfoDAO");
    String name = ts.findSystemInfo().getSiteName();
%>
<!DOCTYPE html>
<html lang="zh-CN"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <title>请升级你的浏览器</title>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">

        <!--<base target="_blank">--><base href="." target="_blank">
        <style type="text/css">
            body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0}
            a{text-decoration:none;color:#0072c6;}a:hover{text-decoration:none;color:#004d8c;}
            body{width:960px;margin:0 auto;padding:10px;font-size: 13px;line-height:20px;color:#454545;font-family:'Microsoft YaHei UI','Microsoft YaHei',DengXian,SimSun,'Segoe UI',Tahoma,Helvetica,sans-serif;}
            h1{font-size:40px;line-height:80px;font-weight:200;margin-bottom:10px;}
            h2{font-size:20px;line-height:25px;font-weight:500;margin:10px 0;}
            p{margin-bottom:10px;}
            .line{clear:both;width:100%;height:1px;overflow:hidden;line-height:1px;border:0;background:#ccc;margin:10px 0 30px;}
            img{width:34px;height:34px;border:0;float:left;margin-right:10px;}
            span{display:block;font-size:12px;line-height:12px;}
            .clean{clear:both;}
            .browser{padding:10px 0;}
            .browser li{width:180px;float:left;list-style:none;}
        </style>
    </head>
    <body>
        <h1>是时候升级您的浏览器了</h1>
        <p>您正在使用 Internet Explorer 的过期版本（IE6、IE7、IE8 或使用该内核的浏览器，或其它伪装为IE6的非法软件，<b>以上浏览器存在重大功能性错误与安全隐患，已经不被包括微软在内的所有厂商和底层技术支持</b>）。这意味着在升级浏览器前，你将无法访问本系统。</p>
        <div class="line"></div>
        <h2>请注意：Windows XP 及 IE6、IE7、IE8 的支持已经结束</h2>
        <p>自 2014 年 4 月 8 日起，Microsoft 不再为 Windows XP 和 Internet Explorer 8 及以下版本提供相应支持和更新。如果你继续使用这些，你将可能受到病毒、间谍软件和其他恶意软件的攻击，损坏本机及网络内的其它硬件设备，并且造成严重精神伤害，无法确保个人信息安全。请参阅 <a href="http://windows.microsoft.com/zh-cn/windows/end-support-help">Microsoft 关于 Windows XP 支持已经结束的说明</a> 。</p>
        <div class="line"></div>
        <h2>更先进的浏览器</h2>

        <p>推荐使用以下浏览器的最新版本。如果你的电脑已有以下浏览器的最新版本则直接使用该浏览器访问<b id="referrer">&nbsp;<a href="<%=path%>" target="_blank"><%=name%>&nbsp;</a></b>即可。</p>
        <ul class="browser">
            <li><a href="http://www.google.com/chrome/eula.html?system=true&standalone=1"><img src="<%=path%>/resources/images/chrome.jpg" alt="谷歌浏览器"> 谷歌浏览器<span>Google Chrome</span></a></li>
            <li><a href="http://www.firefox.com.cn/download/"><img src="<%=path%>/resources/images/firefox.jpg" alt="火狐浏览器"> 火狐浏览器<span>Mozilla Firefox</span></a></li>
            <li><a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie"><img src="<%=path%>/resources/images/ie.jpg" alt="IE浏览器"> IE浏览器<span>Internet Explorer</span></a></li>

        </ul>
        <div class="clean"></div>
        <div class="line"></div>
        <h2>为什么会出现这个页面？</h2>
        <p>如果你不知道升级浏览器是什么意思，请请教一些熟练电脑操作的朋友。如果你使用的不是IE6/7/8，而是360浏览器、QQ浏览器、搜狗浏览器等，出现这个页面是因为你使用的不是该浏览器的最新版本或者这些浏览器使用了“兼容模式”，升级至最新版本或者取消“兼容性视图”即可。</p>
        <div class="line"></div>
        
        <h2>使用IE6、IE7、IE8会造成精神损伤</h2>
        <p>大量的用户经验表明，IE系列浏览器使用过程中随时会无故崩溃，如果您是在进行重要的业务操作，请慎重使用，以免造成财产损失或者精神困扰。</p>

        
        <h2>告别IE6、IE7、IE8</h2>
        <p>为了兼容这个曾经的浏览器霸主，网页设计人员做了大量的代码工作，而且最终效果也始终不能让人满意。对于普通用户而言，低版本IE更是一个岌岌可危的安全隐患，在Windows历史上几次大的木马病毒事件都是利用IE漏洞进行传播。所以，请接受世界所有软件厂商的忠告，告别IE6、IE7、IE8！</p>

    </body></html>
