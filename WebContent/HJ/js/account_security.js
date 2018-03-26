// 防止第一个tab布局异常
$('.ui-datatable-tablewrapper').eq(0).css('overflow', 'visible');
// 标签切换
$(function(){
	$("#resetPassword").hide();
	
	//去掉所有的div的标签样式
	$(".as_title a div").removeClass('onclick');
	//点击的div的加上标签样式
	$(".title_3").children('div').addClass('onclick');
	//隐藏所有内容区域
	$(".accountsecurity .as_content").hide();
	//显示点击的标签坐标-1对应的div
	$(".accountsecurity .as_content").eq($(".title_3").index()-1).show();
	
	$(".as_title a").click(function() {
//		var offset = $("#tabs").offset();
//		var tabsheight = parseInt(offset.top) - 5;
//		if ($(window).scrollTop() > tabsheight) {
//			$(window).scrollTop(tabsheight);
//		}
		//去掉所有的div的标签样式
		$(".as_title a div").removeClass('onclick');
		//点击的div的加上标签样式
		$(this).children('div').addClass('onclick');
		//隐藏所有内容区域
		$(".accountsecurity .as_content").hide();
		//显示点击的标签坐标-1对应的div
		$(".accountsecurity .as_content").eq($(this).index()-1).show();
	});
})

function asSubmit(){
	$("#resetPassword").hide();
	$("#resetPassword2").show();
}