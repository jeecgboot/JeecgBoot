
(function ($) {

    var hash = window.location.hash, start = new Date();

    $(function () {
       // window.onbeforeunload = function () { return "您确定要退出本程序？"; };

        //window.mainpage.instMainMenus();
        //window.mainpage.instFavoMenus();
        //window.mainpage.instTimerSpan();
        window.mainpage.bindNavTabsButtonEvent();
        window.mainpage.bindToolbarButtonEvent();
        window.mainpage.bindMainTabsButtonEvent();

 
        var portal = $("#portal"), layout = $("#mainLayout"), navTabs = $("#navTabs"), themeCombo = $("#themeSelector"),codeThemeCombo = $("#codeThemeSelector");
 
        $.util.exec(function () {
            var theme = $.easyui.theme(), themeName = $.cookie("themeName");
           // alert(themeName );
            
            if (themeName && themeName != theme) { 
            	window.mainpage.setTheme(themeName, false); 
            }

            layout.removeClass("hidden").layout("resize");

            $("#maskContainer").remove();

            var size = $.util.windowSize();
            //  判断当浏览器窗口宽度小于像素 1280 时，右侧 region-panel 自动收缩
            if (size.width < 1280) { layout.layout("collapse", "east"); }

            var stop = new Date();
            $.easyui.messager.show({ msg: "当前页面加载耗时(毫秒)：" + $.date.diff(start, "ms", stop), position: "bottomRight" });
        });

    });
    

})(jQuery);