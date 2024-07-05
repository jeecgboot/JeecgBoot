/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI theme Extensions 1.0 beta
* jQuery EasyUI theme 组件扩展
* jeasyui.extensions.theme.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($) {

    $.util.namespace("$.easyui");

    //  获取或设置当前页面的 jQuery EasyUI 主题皮肤包；该函数定义如下参数：
    //      isTop: 可选参数；表示是否从当前窗口的顶级页面逐级向下依次设置所有页面包括iframe在内的皮肤包名称；
    //      themeName: 可选参数；表示被设置的皮肤包名称；
    //      callback: 可选参数；表示在设置完皮肤包名称后要执行的回调函数；该回调函数执行时依次定义如下参数：
    //          newTheme: 表示更改后的主题皮肤；
    //          oldTheme: 表示更改前的主题皮肤；
    //          上面两个参数 oldTheme、newTheme 都是一个格式如 { id, name, path } 的 JSON-Object，其可能的值参考 $.easyui.theme.dataSource 。
    //      thisArg: 可选参数；表示 callback 回调函数执行中的 this 引用；
    //  备注：如果该方法未传入任何参数；则获取当前页面的 jQuery EasyUI 主题皮肤名称并返回；
    $.easyui.theme = function (isTop, themeName, callback, thisArg) {
        if (arguments.length == 0) { return getTheme($); }
        if (arguments.length == 1 && typeof isTop == "boolean") {
            return isTop ? getTheme($.util.$) : getTheme($);
        } else {
            if (isTop === true) {
                return setTopTheme($.util.$, themeName, callback, thisArg);
            } else {
                return isTop === false ? setTheme($, themeName, callback, thisArg) : setTheme($, isTop, themeName, callback);
            }
        }
    };

    function getTheme(jq) {
        jq = jq || $;
        var link = jq("link[href$='easyui.css'],link[href*='easyui.css']"), href = link.attr("href"), array = href ? href.split("/") : [];
        return array.length > 1 ? array[array.length - 2] : array[array.length - 1];
    };

    function setTheme(jq, theme, callback, thisArg) {
        var oldTheme = getTheme(jq);
        if (oldTheme == theme) {
            return;
        }
        var link = jq("link[href$='easyui.css'],link[href*='easyui.css']"),
            href = link.attr("href"), array = href ? href.split("/") : [];
        if (arguments.length > 1) {
            array[array.length - 2] = theme;
        } else {
            jq.array.insert(array, 0, theme);
        }
        link.attr("href", array.join("/"));
        callbackFunc(callback, oldTheme, theme, thisArg);
    };

    function setTopTheme(jq, theme, callback, thisArg) {
        var oldTheme = getTheme(jq);
        setTheme(jq, theme);
        jq("iframe,iframe").each(function () {
            try {
                if (jq.util && jq.util.isWindow(this.contentWindow) && jq.util.isObject(this.contentWindow.document)
                    && jq.isFunction(this.contentWindow.$) && this.contentWindow.$.easyui && this.contentWindow.$.easyui.theme) {
                    setTopTheme(this.contentWindow.$, theme);
                }
            } catch (ex) { };
        });
        callbackFunc(callback, oldTheme, theme, thisArg);
    };

    function callbackFunc(callback, oldTheme, theme, thisArg) {
        if (!$.isFunction(callback)) { return; }
        var item1 = $.array.first($.easyui.theme.dataSource, function (val) { return val.path == oldTheme; }),
            item2 = $.array.first($.easyui.theme.dataSource, function (val) { return val.path == theme; });
        if (item1) { oldTheme = item1; }
        if (item2) { theme = item2; }
        $.util.exec(function () {
            callback.call(thisArg, theme, oldTheme);
        });
    };

    $.easyui.theme.dataSource = [
    	{ id: 1, name: "金属黑", path: "default" },
        { id: 2, name: "天空蓝", path: "blue" },
        { id: 3, name: "神秘银", path: "bootstrap" },
        { id: 4, name: "宝石蓝", path: "gray" },
        { id: 5, name: "梵星蓝", path: "ui-cupertino", disabled: false },
        { id: 6, name: "钛金黑", path: "ui-dark-hive", disabled: false },
        { id: 7, name: "星河银", path: "ui-pepper-grinder", disabled: false },
        { id: 8, name: "耀沙金", path: "ui-sunny", disabled: false },

        { id: 9, name: "清新蓝", path: "metro-standard" },
        { id: 10, name:"科技蓝", path: "metro-blue" },
        { id: 11, name:"铁灰色", path: "metro-gray" },
        { id: 12, name:"魔法绿", path: "metro-green" },
        { id: 13, name:"魅惑橙", path: "metro-orange" },
        { id: 14, name:"温莎红", path: "metro-red" }
    ];




})(jQuery);





