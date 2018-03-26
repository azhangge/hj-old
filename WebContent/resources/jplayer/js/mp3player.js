
var xx = "<div id=\"jquery_jplayer\" class=\"jp-jplayer\"></div>";
document.write(xx);

var player = {
    startPlayer: function(url) {
        $("#jquery_jplayer").jPlayer("destroy");
        $("#jquery_jplayer").jPlayer({
            ready: function() {
                //alert(url);
                $(this).jPlayer("setMedia", {
                    m4v: url
                }).jPlayer("play");
            },
			solution: 'html, flash',
 supplied: 'm4v',
			swfPath: "./",
			preload: 'metadata',
 volume: 0.8,
 muted: false,
 backgroundColor: '#000000',
        });
    }
};


function disablePP(id) {
    $("input[id^=\"" + id + "\"]").attr({"disabled": "disabled"});
}