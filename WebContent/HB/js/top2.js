$(window).scroll(function(event){  
    var wScrollY = window.scrollY; // 当前滚动条位置    
    var wInnerH = window.innerHeight; // 设备窗口的高度（不会变）    
    var bScrollH = document.body.scrollHeight; // 滚动条总高度        
    if (wScrollY >= 555) {            
        $("#top2_div").show();
    }else{
    	$("#top2_div").hide();
    }
});