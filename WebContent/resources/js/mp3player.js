
var xx = "<div id=\"jquery_jplayer\" class=\"jp-jplayer\"></div>";
document.write(xx);

var player = {
    startPlayer: function(url) {
        $("#jquery_jplayer").jPlayer("destroy");
        $("#jquery_jplayer").jPlayer({
            ready: function() {
                //alert(url);
                $(this).jPlayer("setMedia", {
                    mp3: url
                }).jPlayer("play");
            },
            supplied: "mp3"
        });
    }
};


function disablePP(id) {
    $("input[id^=\"" + id + "\"]").attr({"disabled": "disabled"});
}