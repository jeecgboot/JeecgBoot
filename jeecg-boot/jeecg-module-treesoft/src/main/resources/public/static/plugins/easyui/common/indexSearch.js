

(function ($) {
    
    $.util.namespace("mainpage.searchTabs");
    $.util.namespace("mainpage.searchTabs");    

    var homePageTitle = "主页", homePageHref = null, 
         searchTabs = "#searchTabs",  searchTabs = "#searchTabs", searcHomePanel = "#searcHomePanel"  ;


    //  按照指定的根节点菜单 id，加载其相应的子菜单树面板数据；该方法定义如下参数：
    //      id: 表示根节点菜单的 id；

    //  将指定的根节点数据集合数据加载至左侧面板中“导航菜单”的 ul 控件中；该方法定义如下参数：
    //      callback:  为一个 Function 对象；表示家在完成菜单数据后调用的回调函数

    //  初始化 westSouthPanel 位置的“导航菜单”区域子菜单 ul 控件(仅初始化 easyui-tree 对象，不加载数据)。

    //  初始化应用程序主界面左侧面板中“导航菜单”的数据，并加载特定的子菜单树数据。

 

    //  判断指定的选项卡是否存在于当前主页面的选项卡组中；
    //  返回值：返回的值可能是以下几种：
    //      0:  表示不存在于当前选项卡组中；
    //      1:  表示仅 title 值存在于当前选项卡组中；
    //      2:  表示 title 和 href 都存在于当前选项卡组中；
    window.mainpage.searchTabs.isExists = function (title, href) {
        var t = $(searchTabs), tabs = t.tabs("tabs"), array = $.array.map(tabs, function (val) { return val.panel("options"); }),
            list = $.array.filter(array, function (val) { return val.title == title; }), ret = list.length ? 1 : 0;
        if (ret && $.array.some(list, function (val) { return val.href == href; })) { ret = 2; }
        return ret;
    };

    window.mainpage.searchTabs.tabDefaultOption = {
        title: "新建选项卡", href: "", iniframe: true, closable: true, refreshable: true, iconCls: "icon-standard-application-form", repeatable: true, selected: true
    };
    window.mainpage.searchTabs.parseCreateTabArgs = function (args) {
        var ret = {};
        if (!args || !args.length) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption);
        } else if (args.length == 1) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, typeof args[0] == "object" ? args[0] : { href: args[0] });
        } else if (args.length == 2) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1] });
        } else if (args.length == 3) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2] });
        } else if (args.length == 4) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3] });
        } else if (args.length == 5) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4] });
        } else if (args.length == 6) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4], refreshable: args[5] });
        } else if (args.length >= 7) {
            $.extend(ret, window.mainpage.searchTabs.tabDefaultOption, { title: args[0], href: args[1], iconCls: args[2], iniframe: args[3], closable: args[4], refreshable: args[5], selected: args[6] });
        }
        return ret;
    };

    window.mainpage.searchTabs.createTab = function (title, href, iconCls, iniframe, closable, refreshable, selected) {
        var t = $(searchTabs), i = 0, opts = window.mainpage.searchTabs.parseCreateTabArgs(arguments);
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
    window.mainpage.searchTabs.addModule = function (title, href, iconCls, iniframe, closable, refreshable, selected) {
        var opts = window.mainpage.searchTabs.parseCreateTabArgs(arguments), isExists = window.mainpage.searchTabs.isExists(opts.title, opts.href);
        switch (isExists) {
            case 0: window.mainpage.searchTabs.createTab(opts); break;
            case 1: window.mainpage.searchTabs.createTab(opts); break;
            case 2: window.mainpage.searchTabs.jumeTab(opts.title); break;
            default: break;
        }
    };

    window.mainpage.searchTabs.jumeTab = function (which) { $(searchTabs).tabs("select", which); };

    window.mainpage.searchTabs.jumpHome = function () {
        var t = $(searchTabs), tabs = t.tabs("tabs"), panel = $.array.first(tabs, function (val) {
            var opts = val.panel("options");
            return opts.title == homePageTitle && opts.href == homePageHref;
        });
        if (panel && panel.length) {
            var index = t.tabs("getTabIndex", panel);
            t.tabs("select", index);
        }
    }

    window.mainpage.searchTabs.jumpTab = function (which) { $(searchTabs).tabs("jumpTab", which); };

    window.mainpage.searchTabs.closeTab = function (which) { $(searchTabs).tabs("close", which); };
    
    window.mainpage.searchTabs.refCurrentTab = function () {
        var t = $(searchTabs), index = t.tabs("getSelectedIndex");
        return t.tabs("refresh", index);
    };

    window.mainpage.searchTabs.closeCurrentTab = function () {
        var t = $(searchTabs), index = t.tabs("getSelectedIndex");
        return t.tabs("closeClosable", index);
    };
    

    window.mainpage.searchTabs.closeAllTabs = function () {
        return $(searchTabs).tabs("closeAllClosable");
    };

   

})(jQuery);