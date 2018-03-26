function aabbcc(url) {
	var scrWidth = screen.availWidth;
	var scrHeight = screen.availHeight;
	var self = window
			.open(
					url,
					'',
					"fullscreen=3,resizable=false,toolbar=no,menubar=no,scrollbars=yes,location=no,top=0,left=0,width="
							+ scrWidth + ",height=" + scrHeight);
	// self.resizeTo(scrWidth,scrHeight);
	self.moveTo(0, 0);
}

/*
 * function collect(){ if(#{empty clientSession.usr}){ PF('login1').show();
 * return; } var add = 0; if($('a[name="collect"]')[0].title=="�����ղ�"){
 * $('a[name="collect"]') .css("background-image",
 * "url(../HJ/image/after-collection.jpg)");
 * $('a[name="collect"]').attr("title","ȡ���ղ�"); add = 1; }else{
 * $('a[name="collect"]').css("background-image",
 * "url(../HJ/image/before-collection.jpg)");
 * $('a[name="collect"]').attr("title","�����ղ�"); } // $('a[name="collect"]')
 * .css("background-color", "lightgreen"); $('a[name="collect"]')
 * .css("background-position","center"); $('a[name="collect"]')
 * .css("background-size","80% 100%"); $('a[name="collect"]')
 * .css("background-color","#f6f6f6"); $('a[name="collect"]')
 * .css("background-repeat","no-repeat"); change(add); }
 */
function change(add){
	var t = {  
			"userId":"#{clientSession.usr.id}",  
			"courseIds":"#{lessonList.lessonType.id}",
			"addOrRemove":add
		 } 
	$.ajax({
		type: "post",
		url: "/servlet/app/changeUserCollectionCourses",
		data: JSON.stringify(t),
		cache: false,
		async : false,
		dataType: "json",
		success: function (data ,textStatus, jqXHR)
		{
			$('#test').empty();
			$('#test').html(data);
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {      
		}
	 });
}

$(function(){
	function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  // 匹配目标参数
        if (r != null) return unescape(r[2]); return null; // 返回参数值
    }
	var index = getUrlParam('index')
	$('ul.nav.nav-tabs>li').removeClass('active');
	$('ul.nav.nav-tabs>li').eq(index).addClass('active');
	$('#myTabContent>div').removeClass('in active');
	$('#myTabContent>div').eq(index-1).addClass('in active');
})

function cancleCollect(){
	$('#myTabContent input:checkbox:checked').each(function(){
// $(this).parent().siblings('div.course').remove();
		$(this).parent().parent().remove();
	})
}
function allselect(){
	var id='';
	$('#myTabContent div.tab-pane').each(function(){
		if($(this).attr('class').indexOf('active')!=-1){
			id = $(this)[0].id;
			return false;
		}
	})
	$('#'+id+' input:checkbox').each(function(){
		var vv = $(this).is(':checked');
		$(this).prop( 'checked', !vv);
	})
	
// $.ajax({
// type: "post",
// url: "/servlet/app/changeUserCollectionCourses",
// // data: JSON.stringify(t),
// cache: false,
// async : false,
// dataType: "json",
// success: function (data ,textStatus, jqXHR)
// {
// var data = $('#collect_course').prop("outerHTML");
// $('#collect_course').empty();
// $('#collect_course').html(data);
// },
// error:function (XMLHttpRequest, textStatus, errorThrown) {
// }
// });
}