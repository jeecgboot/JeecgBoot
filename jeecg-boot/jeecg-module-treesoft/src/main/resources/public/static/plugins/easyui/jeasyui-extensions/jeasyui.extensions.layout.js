/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI layout Extensions 1.0 beta
* jQuery EasyUI layout 组件扩展
* jeasyui.extensions.layout.js
* 二次开发 流云
* 最近更新：2014-06-17
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.layout.extensions = { resizeDelay: 500 };



    function getPanels(target, withCenter) {
        var l = $(target),
            flag = (withCenter == null || withCenter == undefined) ? true : withCenter,
            regions = flag ? ["north", "west", "east", "center", "south"] : ["north", "west", "east", "south"];
        return $.array.reduce(regions, function (prev, val, index) {
            var p = l.layout("panel", val);
            if (p && p.length) {
                prev.push({ region: val, panel: p });
                prev[val] = p;
            }
            return prev;
        }, []);
    };

    function collapseRegion(l, region) {
        var p = l.layout("panel", region);
        if (p && p.length) {
            var opts = p.panel("options");
            if (!opts.collapsed) { l.layout("collapse", region); }
        }
    };

    function collapseAll(target) {
        var l = $(target), panels = l.layout("panels", false);
        $.each(panels, function (index, item) {
            var opts = item.panel.panel("options");
            if (!opts.collapsed) { l.layout("collapse", item.region); }
        });
        $.util.exec(function () { l.layout("resize"); }, $.fn.layout.extensions.resizeDelay);
    };

    function expandRegion(l, region) {
        var p = l.layout("panel", region);
        if (p && p.length) {
            var opts = p.panel("options");
            if (opts.collapsed) { l.layout("expand", region); }
        }
    };

    function expandAll(target) {
        var l = $(target), panels = l.layout("panels", false);
        $.each(panels, function (index, item) {
            var opts = item.panel.panel("options");
            if (opts.collapsed) { l.layout("expand", item.region); }
        });
        $.util.exec(function () { l.layout("resize"); }, $.fn.layout.extensions.resizeDelay);
    };


    function toggle(target, region) {
        if (!$.array.contains(["north", "west", "east", "center", "south"], region)) { return; }
        var l = $(target), p = l.layout("panel", region);
        if (p && p.length) {
            var opts = p.panel("options");
            if (opts.collapsed) { l.layout("expand", region); } else { l.layout("collapse", region); }
        }
    };

    function toggleAll(target, type) {
        if (!$.array.contains(["collapse", "expand", "toggle"], type)) { type = "toggle"; }
        var l = $(target), regions = ["north", "west", "east", "south"],
            hasCollapsed = $.array.some(regions, function (region) {
                var p = l.layout("panel", region);
                if (p && p.length) { var opts = p.panel("options"); return opts.collapsed ? true : false; } else { return false; }
            }),
            hasExpanded = $.array.some(regions, function (region) {
                var p = l.layout("panel", region);
                if (p && p.length) { var opts = p.panel("options"); return !opts.collapsed ? true : false; } else { return false; }
            });
        switch (type) {
            case "collapse":
                l.layout(hasExpanded ? "collapseAll" : "expandAll");
                break;
            case "expand":
                l.layout(hasCollapsed ? "expandAll" : "collapseAll");
                break;
            case "toggle":
                toggleRegions();
                break;
            default:
                toggleRegions();
                break;
        }
        function toggleRegions() {
            $.each(regions, function (i, region) {
                l.layout("toggle", region);
            });
            $.util.exec(function () { l.layout("resize"); }, $.fn.layout.extensions.resizeDelay);
        };
    };




    var defaults = $.fn.layout.extensions.defaults = {};

    var methods = $.fn.layout.extensions.methods = {

        // 扩展 easyui-layout 组件的自定义方法；获取 easyui-layout 组件的所有 panel 面板；
        // 该方法的参数 withCenter 是一个 boolean 类型值，默认为 true；表示返回的数组中是否包含 center panel。
        // 返回值：该方法返回一个 Array 数组对象；数组中的每个元素都是一个包含如下属性定义的 JSON-Object：
        //      region  : String 类型值，表示该面板所在的位置，可能的值为 "north"、"west"、"east"、"center"、"south"；
        //      panel   : jQuery 对象，表示 easyui-panel 面板对象；
        panels: function (jq, withCenter) { return getPanels(jq[0], withCenter); },

        //  扩展 easyui-layout 组件的自定义方法；用于折叠 easyui-layout 组件除 center 位置外的所有 panel 面板；
        //  返回值：返回表示当前 easyui-combo layout jQuery 链式对象。
        collapseAll: function (jq) { return jq.each(function () { collapseAll(this); }); },

        //  扩展 easyui-layout 组件的自定义方法；用于展开 easyui-layout 组件除 center 位置外的所有 panel 面板；
        //  返回值：返回表示当前 easyui-combo layout jQuery 链式对象。
        expandAll: function (jq) { return jq.each(function () { expandAll(this); }); },

        //  扩展 easyui-layout 组件的自定义方法；用于切换 panel 面板的 折叠/展开 状态；该方法定义如下参数：
        //      region: String 类型值，表示要切换 折叠/展开 状态的面板的位置；
        //  返回值：返回表示当前 easyui-combo layout jQuery 链式对象。
        toggle: function (jq, region) { return jq.each(function () { toggle(this, region); }); },

        //  扩展 easyui-layout 组件的自定义方法；用于切换所有 panel 面板的 折叠/展开 状态；该方法定义如下参数：
        //      type:   String 类型值，表示在进行 折叠/展开 操作时的操作方式；该参数传入的值限定在以下范围内：
        //          "collapse": 当既有展开的面板也有折叠的面板时，对所有面板执行折叠操作；
        //          "expand"  : 当既有展开的面板也有折叠的面板时，对所有面板执行展开操作；
        //          "toggle"  : 当既有展开的面板也有折叠的面板时，对所有面板执行切换 折叠/展开 状态操作；默认值。
        //  返回值：返回表示当前 easyui-combo layout jQuery 链式对象。
        toggleAll: function (jq, type) { return jq.each(function () { toggleAll(this, type); }); }
    };


    $.extend($.fn.layout.defaults, defaults);
    $.extend($.fn.layout.methods, methods);

})(jQuery);