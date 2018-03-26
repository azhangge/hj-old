// 防止第一个tab布局异常
$('.ui-datatable-tablewrapper').eq(0).css('overflow', 'visible');
// 标签切换
$(function(){
	$("#tab_switch li").click(function() {
		var offset = $("#tabs").offset();
		var tabsheight = parseInt(offset.top) - 5;
		if ($(window).scrollTop() > tabsheight) {
			$(window).scrollTop(tabsheight);
		}
		$("#tab_switch li a").removeClass('curr');
		$(this).children('a').addClass('curr');
		$(".m-content .m-block").hide();
		$(".m-content .m-block").eq($(this).index()).show();
	});
})

/*$('a[name="collect"]') .click(function(){
	var ifLogin = document.getElementById("myForm:ifLogin").value;
	if(ifLogin){
		PF('login1').show();
		return false;
		return;
	}
	if($('a[name="collect"]')[0].title=="加入收藏"){
		$('a[name="collect"]') .css("background-image", "url(../HJ/image/after-collection.jpg)");
		$('a[name="collect"]').attr("title","取消收藏");
	}else{
		$('a[name="collect"]').css("background-image", "url(../HJ/image/before-collection.jpg)");
		$('a[name="collect"]').attr("title","加入收藏");
	}
//	$('a[name="collect"]') .css("background-color", "lightgreen");
	$('a[name="collect"]') .css("background-position","center");
	$('a[name="collect"]') .css("background-size","80% 100%");
	$('a[name="collect"]') .css("background-color","#f6f6f6");
	$('a[name="collect"]') .css("background-repeat","no-repeat");
})*/

