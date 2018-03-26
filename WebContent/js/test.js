/* 全局变量 */
var userCount;// 符合查找条件的用户总页数，分页参考
var pageIndex = 0;// 当前页，默认为0
var pageSize = 8;// 每页显示个数为8

// 按条件查找用户
function searchUser(index, size) {
	var findTerm = $("#serchTerm").val();
	var provinceId = $('#province').val();
	var cityId = $('#city').val();
	$.ajax({
		type : "POST",
		url : "user/findContactsAjax",
		cache : false,
		data : {
			provinceId : provinceId,
			cityId : cityId,
			pageIndex : index,
			pageSize : size
		},
		async : true,
		error : function() {
			alert("网络异常！");
		},
		success : function(data) {
			alter(data);
		}
	});
}
// 首页
function GoToFirstPage() {
	pageIndex = 0;
	searchUser(pageIndex, pageSize);
}
// 前一页
function GoToPrePage() {
	pageIndex -= 1;
	pageIndex = pageIndex >= 0 ? pageIndex : 0;
	searchUser(pageIndex, pageSize);
}
// 后一页
function GoToNextPage() {
	if (pageIndex + 1 < userCount) {
		pageIndex += 1;
	}
	searchUser(pageIndex, pageSize);
}
// 尾页
function GoToEndPage() {
	pageIndex = userCount - 1;
	searchUser(pageIndex, pageSize);
}

function test() {
	var num = $('.active').text();
	num++;
	$('.active').removeClass('active');
	$('.pagination span').each(function(){
		if($(this).text() == num){
			$(this).addClass('active');
			return false;
		}
	})
	$.ajax({
		type : "POST",
		url : "/th/findContactsAjax.do",
		cache : false,
		data : {
			pageIndex : num,
			pageSize : 10
		},
		async : true,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
		success : function(data) {
			$('#testTable tr:not(:first)').empty();
			var list = data.list;
			for(var i=0;i<list.length;i++){
				var a = list[i].num;
				var b = list[i].name;
				var c = list[i].price;
				var d = list[i].time;
				appendData(a,b,c,d,'testTable')
			}
		}
	});
}

function test2() {
	var num = $('.active').text();
	num--;
	$('.active').removeClass('active');
	$('.pagination span').each(function(){
		if($(this).text() == num){
			$(this).addClass('active');
			return false;
		}
	})
	$.ajax({
		type : "POST",
		url : "/th/findContactsAjax.do",
		cache : false,
		data : {
			pageIndex : num,
			pageSize : 10
		},
		async : true,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
		success : function(data) {
			$('#testTable tr:not(:first)').empty();
			var list = data.list;
			for(var i=0;i<list.length;i++){
				var a = list[i].num;
				var b = list[i].name;
				var c = list[i].price;
				var d = list[i].time;
				appendData(a,b,c,d,'testTable')
			}
		}
	});
}

function test3(){
	var num = $('.input').val();
	$('.active').removeClass('active');
	$('.pagination span').each(function(){
		if($(this).text() == num){
			$(this).addClass('active');
			return false;
		}
	})
	$.ajax({
		type : "POST",
		url : "/th/findContactsAjax.do",
		cache : false,
		data : {
			pageIndex : num,
			pageSize : 10
		},
		async : true,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
		success : function(data) {
			$('#testTable tr:not(:first)').empty();
			var list = data.list;
			for(var i=0;i<list.length;i++){
				var a = list[i].num;
				var b = list[i].name;
				var c = list[i].price;
				var d = list[i].time;
				appendData(a,b,c,d,'testTable')
			}
		}
	});
}

function appendData(a,b,c,d,tab){
	var text = '<tr><td>'+a+'</td><td>'+b+'</td><td>'+c+'</td><td>'+d+'</td></tr>';
	$('#'+tab+' thead').append(text);
}