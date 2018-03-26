$(function() {
	$(".fullSlide").mouseover(function() {
		$(".prev").show();
		$(".next").show();
	});
	$(".fullSlide").mouseout(function() {
		$(".prev").hide();
		$(".next").hide();
	});
})

function changbgcolor(bgcolor) {
	$(".banner").animate({'background-color':bgcolor},500);
// 	$(".banner").css('background-color', bgcolor);
}