

(function ($) {

    $.util.namespace("mainpage.nav");
    $.util.namespace("mainpage.favo");
    $.util.namespace("mainpage.mainTabs");
     
    var homePageTitle = "主页", homePageHref = null, navMenuList = "#navMenu_list",
        navMenuTree = "#navMenu_Tree", mainTabs = "#mainTabs", navTabs = "#navTabs", favoMenuTree = "#favoMenu_Tree",
        mainLayout = "#mainLayout", northPanel = "#northPanel", themeSelector = "#themeSelector",
        westLayout = "#westLayout", westCenterLayout = "#westCenterLayout", westFavoLayout = "#westFavoLayout",
        westSouthPanel = "#westSouthPanel", homePanel = "#homePanel",
        btnContact = "#btnContact", btnFullScreen = "#btnFullScreen", btnExit = "#btnExit" ;


    //  按照指定的根节点菜单 id，加载其相应的子菜单树面板数据；该方法定义如下参数：
    //      id: 表示根节点菜单的 id；

    //  将指定的根节点数据集合数据加载至左侧面板中“导航菜单”的 ul 控件中；该方法定义如下参数：
    //      callback:  为一个 Function 对象；表示家在完成菜单数据后调用的回调函数

    //  初始化 westSouthPanel 位置的“导航菜单”区域子菜单 ul 控件(仅初始化 easyui-tree 对象，不加载数据)。

    //  初始化应用程序主界面左侧面板中“导航菜单”的数据，并加载特定的子菜单树数据。

    //时间
    window.mainpage.instTimerSpan = function () {
        var timerSpan = $("#timerSpan"), interval = function () { timerSpan.text($.date.toLongDateTimeString(new Date())); };
        interval();
        window.setInterval(interval, 1000);
    };

    window.mainpage.bindToolbarButtonEvent = function () {
        
        $("#btnHideNorth").click(function () { window.mainpage.hideNorth(); });
        var btnShow = $("#btnShowNorth").click(function () { window.mainpage.showNorth(); });
        var l = $(mainLayout), north = l.layout("panel", "north"), panel = north.panel("panel"),
            toolbar = $("#toolbar"), topbar = $("#topbar");
        opts = north.panel("options"), onCollapse = opts.onCollapse, onExpand = opts.onExpand;
        opts.onCollapse = function () {
            if ($.isFunction(onCollapse)) { onCollapse.apply(this, arguments); }
            btnShow.show();
            toolbar.insertBefore(panel).addClass("top-toolbar-topmost");
        };
        opts.onExpand = function () {
            if ($.isFunction(onExpand)) { onExpand.apply(this, arguments); }
            btnShow.hide();
            toolbar.insertAfter(topbar).removeClass("top-toolbar-topmost");
        };

        var themeName = $.cookie("themeName"),
            themeCombo = $(themeSelector).combobox({
                width: 100, editable: false,
                data: window.mainpage.themeData,
                valueField: "path", textField: "name",
                value: themeName || window.mainpage.themeData[0].path,
                onSelect: function (record) {
                    var opts = themeCombo.combobox("options");
                    window.mainpage.setTheme(record[opts.valueField], true);
                }
            });

        $(btnFullScreen).click(function () {
            if ($.util.supportsFullScreen) {
                if ($.util.isFullScreen()) {
                    $.util.cancelFullScreen();
                } else {
                    $.util.requestFullScreen();
                }
            } else {
                $.easyui.messager.show("当前浏览器不支持全屏 API，请更换至最新的 Chrome/Firefox/Safari 浏览器或通过 F11 快捷键进行操作。");
            }
        });

        $(btnExit).click(function () {
            $.easyui.messager.confirm("操作提醒", "您确定要退出当前程序并关闭该网页？", function (c) {
                if (c) {
                     window.onbeforeunload = null;
                    // location.href='logout';
                     location.href='../treesoft/logout';
                  //  location.href='/treesoft/logout';
                  //  location.href='./treesoft/logout';
                }
            });
        });
    };

    window.mainpage.search = function (value, name) { $.easyui.messager.show($.string.format("您设置的主题为：value: {0}, name: {1}", value, name)); };


    window.mainpage.themeData = $.array.filter($.easyui.theme.dataSource, function (val) {
        return val.disabled ? false : true;
    });

    //  设置当前系统主界面的界面皮肤风格；该方法提供如下参数：
    //      theme:      界面皮肤风格名称
    //      setCookie:  可选参数，boolean 类型，表示是否将 theme 保存至 cookie 名为 themeName
    window.mainpage.setTheme = function (theme, setCookie) {
        if (setCookie == null || setCookie == undefined) { setCookie = true; }
        $.easyui.theme(true, theme, function (item) {
            if (setCookie) {
                $.cookie("themeName", theme, { expires: 30,path:"/"});
                var msg = $.string.format("您设置了新的系统主题皮肤为：{0}，{1}。", item.name, item.path);
                $.easyui.messager.show(msg);
            }
        });
    };

    window.mainpage.bindMainTabsButtonEvent = function () {
        $("#mainTabs_jumpHome").click(function () { window.mainpage.mainTabs.jumpHome();    });
        $("#mainTabs_toggleAll").click(function () { window.mainpage.togglePanels(); });
        $("#mainTabs_jumpTab").click(function () { window.mainpage.mainTabs.jumpTab(); });
        $("#mainTabs_closeTab").click(function () { window.mainpage.mainTabs.closeCurrentTab(); });
        $("#mainTabs_refTab").click(function () { window.mainpage.mainTabs.refCurrentTab(); });
        $("#mainTabs_closeOther").click(function () { window.mainpage.mainTabs.closeOtherTabs(); });
        $("#mainTabs_closeLeft").click(function () { window.mainpage.mainTabs.closeLeftTabs(); });
        $("#mainTabs_closeRight").click(function () { window.mainpage.mainTabs.closeRightTabs(); });
        $("#mainTabs_closeAll").click(function () { window.mainpage.mainTabs.closeAllTabs(); });
    };

    window.mainpage.bindNavTabsButtonEvent = function () {
        $("#navMenu_refresh").click(function () { window.mainpage.refreshNavTab(); });

        $("#navMenu_Favo").click(function () { window.mainpage.nav.addFavo(); });
        $("#navMenu_Rename").click(function () { window.mainpage.nav.rename(); });
        $("#navMenu_expand").click(function () { window.mainpage.nav.expand(); });
        $("#navMenu_collapse").click(function () { window.mainpage.nav.collapse(); });
        $("#navMenu_collapseCurrentAll").click(function () { window.mainpage.nav.expandCurrentAll(); });
        $("#navMenu_expandCurrentAll").click(function () { window.mainpage.nav.collapseCurrentAll(); });
        $("#navMenu_collapseAll").click(function () { window.mainpage.nav.expandAll(); });
        $("#navMenu_expandAll").click(function () { window.mainpage.nav.collapseAll(); });

        $("#favoMenu_Favo").click(function () { window.mainpage.favo.removeFavo(); });
        $("#favoMenu_Rename").click(function () { window.mainpage.favo.rename(); });
        $("#favoMenu_expand").click(function () { window.mainpage.favo.expand(); });
        $("#favoMenu_collapse").click(function () { window.mainpage.favo.collapse(); });
        $("#favoMenu_collapseCurrentAll").click(function () { window.mainpage.favo.expandCurrentAll(); });
        $("#favoMenu_expandCurrentAll").click(function () { window.mainpage.favo.collapseCurrentAll(); });
        $("#favoMenu_collapseAll").click(function () { window.mainpage.favo.expandAll(); });
        $("#favoMenu_expandAll").click(function () { window.mainpage.favo.collapseAll(); });
    };

    window.mainpage.hideNorth = function () { $(mainLayout).layout("collapse", "north"); };

    window.mainpage.showNorth = function () { $(mainLayout).layout("expand", "north"); };

    window.mainpage.togglePanels = function () { $(mainLayout).layout("toggleAll", "collapse"); };

    window.mainpage.addModuleTab = function (node) {
        var n = node || {}, attrs = node.attributes || {}, opts = $.extend({}, n, { title: n.text }, attrs);
        if (!opts.href) { return; }
        window.mainpage.mainTabs.addModule(opts);
    };

    //  判断指定的选项卡是否存在于当前主页面的选项卡组中；
    //  返回值：返回的值可能是以下几种：
    //      0:  表示不存在于当前选项卡组中；
    //      1:  表示仅 title 值存在于当前选项卡组中；
    //      2:  表示 title 和 href 都存在于当前选项卡组中；
    window.mainpage.mainTabs.isExists = function (title, href) {
        var t = $(mainTabs), tabs = t.tabs("tabs"), array = $.array.map(tabs, function (val) { return val.panel("options"); }),
            list = $.array.filter(array, function (val) { return val.title == title; }), ret = list.length ? 1 : 0;
        if (ret && $.array.some(list, function (val) { return val.href == href; })) { ret = 2; }
        return ret;
    };

    window.mainpage.mainTabs.tabDefaultOption = {
        title: "新建选项卡", href: "", iniframe: true, closable: true, refreshable: true, iconCls: "icon-standard-application-form", repeatable: true, selected: true
    };
    window.mainpage.mainTabs.parseCreateTabArgs = function (args) {
        var ret = {};
        if (!args || !args.length) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption);
        } else if (args.length == 1) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, typeof args[0] == "object" ? args[0] : { href: args[0] });
        } else if (args.length == 2) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1] });
        } else if (args.length == 3) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2] });
        } else if (args.length == 4) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3] });
        } else if (args.length == 5) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4] });
        } else if (args.length == 6) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4], refreshable: args[5] });
        } else if (args.length >= 7) {
            $.extend(ret, window.mainpage.mainTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4], refreshable: args[5], selected: args[6] });
        }
        return ret;
    };

    window.mainpage.mainTabs.createTab = function (title, href, iconCls, iniframe, closable, refreshable, selected) {
        var t = $(mainTabs), i = 0, opts = window.mainpage.mainTabs.parseCreateTabArgs(arguments);
        while (t.tabs("getTab", opts.title + (i ? String(i) : ""))) { i++; }
        t.tabs("add", opts);
    };

    //  添加一个新的模块选项卡；该方法重载方式如下：
    //      function (tabOption)
    //      function (href)
    //      function (title, href)
    //      function (title, href, iconCls)
    //      function (title, href, iconCls, iniframe)
    //      function (title, href, iconCls, iniframe, closable)
    //      function (title, href, iconCls, iniframe, closable, refreshable)
    //      function (title, href, iconCls, iniframe, closable, refreshable, selected)
    window.mainpage.mainTabs.addModule = function (title, href, iconCls, iniframe, closable, refreshable, selected) {
        var opts = window.mainpage.mainTabs.parseCreateTabArgs(arguments), isExists = window.mainpage.mainTabs.isExists(opts.title, opts.href);
        switch (isExists) {
            case 0: window.mainpage.mainTabs.createTab(opts); break;
            case 1: window.mainpage.mainTabs.createTab(opts); break;
            case 2: window.mainpage.mainTabs.jumeTab(opts.title); break;
            default: break;
        }
    };

    window.mainpage.mainTabs.jumeTab = function (which) { $(mainTabs).tabs("select", which); };

    window.mainpage.mainTabs.jumpHome = function () {
    	$(mainTabs).tabs("select", 0); //20160429 yyj
    }

    window.mainpage.mainTabs.jumpTab = function (which) { $(mainTabs).tabs("jumpTab", which); };

    window.mainpage.mainTabs.closeTab = function (which) { $(mainTabs).tabs("close", which); };
    
    window.mainpage.mainTabs.refCurrentTab = function () {
        var t = $(mainTabs), index = t.tabs("getSelectedIndex");
        return t.tabs("refresh", index);
    };

    window.mainpage.mainTabs.closeCurrentTab = function () {
        var t = $(mainTabs), index = t.tabs("getSelectedIndex");
        return t.tabs("closeClosable", index);
    };

    window.mainpage.mainTabs.closeOtherTabs = function (index) {
        var t = $(mainTabs);
        if (index == null || index == undefined) { index = t.tabs("getSelectedIndex"); }
        return t.tabs("closeOtherClosable", index);
    };

    window.mainpage.mainTabs.closeLeftTabs = function (index) {
        var t = $(mainTabs);
        if (index == null || index == undefined) { index = t.tabs("getSelectedIndex"); }
        return t.tabs("closeLeftClosable", index);
    };

    window.mainpage.mainTabs.closeRightTabs = function (index) {
        var t = $(mainTabs);
        if (index == null || index == undefined) { index = t.tabs("getSelectedIndex"); }
        return t.tabs("closeRightClosable", index);
    };

    window.mainpage.mainTabs.closeAllTabs = function () {
        return $(mainTabs).tabs("closeAllClosable");
    };

    window.mainpage.mainTabs.updateHash = function (index) {
        var opts = $(mainTabs).tabs("getTab", index).panel("options");
        //window.location.hash = opts.href ? opts.href : "";
    };

    window.mainpage.mainTabs.loadHash = function (hash) {
       
    };


    window.mainpage.refreshNavTab = function (index) {
        var t = $(navTabs);
        if (index == null || index == undefined) { index = t.tabs("getSelectedIndex"); }
        if (index == 0) {
            window.mainpage.nav.refreshNav();
        } else {
            window.mainpage.favo.refreshTree();
        }
    };

    window.mainpage.nav.refreshNav = function () { window.mainpage.instMainMenus(); };

})(jQuery);