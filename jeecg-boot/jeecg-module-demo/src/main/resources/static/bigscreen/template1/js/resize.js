
window.onresize = function() {
    setAppScale();
};
 
function setAppScale() {
	 var ratioY = $(window).height()/1536;
     var ratioX = $(window).width()/4352;
     var screenWidth = window.screen.width; 
     var screenHeigth = window.screen.height; 
     if (screenWidth >= 960) {
            ratioX = 0.62
        }
    if(screenHeigth <= 1080){
            ratioY = 0.62
    } 
    $("body").css({
        transform: "scale("+ ratioX+","+ ratioY+")",
        transformOrigin: "left top",
        overflow:"visible"
    }); 	 
 }
 

$().ready(function(){
        //初始化时调整大小
        setAppScale();    
});
