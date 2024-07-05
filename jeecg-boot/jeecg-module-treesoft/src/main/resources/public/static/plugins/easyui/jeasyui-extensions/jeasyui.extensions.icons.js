/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI icons Extensions 1.0 beta
* jQuery EasyUI icons 组件扩展
* jeasyui.extensions.icons.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late
*   5、jeasyui.extensions.window.js v1.0 beta late
*   6、jeasyui.extensions.dialog.js v1.0 beta late
*   7、icons/jeasyui.icons.all.js 和 icons/icon-all.css v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.util.namespace("$.easyui.icons");

    //  增加自定义扩展方法 $.easyui.icons.showSelector；该方法弹出一个图标选择框窗口；该方法定义如下参数：
    //      options: 这是一个 JSON-Object 对象；具体格式参考 $.easyui.showDialog 方法的参数 options 的格式；
    //               该参数格式在 $.easyui.showDialog 参数 options 格式基础上扩展了如下属性：
    //          selected:
    //          multiple:
    //          size:
    //          onEnter :
    //  返回值：返回弹出窗口的 easyui-dialog 控件对象(jQuery-DOM 格式)。
    $.easyui.icons.showSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.icons.showSelector.apply(this, arguments); }
        var opts = $.extend({
            width: 580, minWidth: 520, height: 480, minHeight: 360,
            title: "选择图标",
            iconCls: "icon-hamburg-zoom",
            maximizable: true,
            collapsible: true,
            selected: null,
            multiple: false,
            size: null,
            onEnter: function (value) { }
        }, options);
        opts.size = opts.size || "16";
        opts.title = opts.title + ", " + (opts.multiple ? "多选" : "单选") + ", 尺寸:" + opts.size;
        var value = opts.selected,
            dia = $.easyui.showDialog($.extend({}, opts, {
                content: "<div class=\"icons-layout\"><div data-options=\"region: 'north', split: false, border: false\" style=\"height: 31px; overflow: hidden;\"><div class=\"icons-toolbar\"></div></div><div data-options=\"region: 'center', border: false\"><div class=\"icons-tabs\"></div></div></div>",
                saveButtonText: "确定",
                saveButtonIconCls: "icon-ok",
                enableApplyButton: false,
                //topMost: true,
                onSave: function () { if ($.isFunction(opts.onEnter)) { return opts.onEnter.call(this, value); } }
            }));
        $.util.exec(function () {
            var content = dia.find("div.icons-layout").layout({ fit: true }),
                toolbar = content.find("div.icons-toolbar").toolbar(),
                tabs = content.find("div.icons-tabs").tabs({ fit: true, border: false }),
                tabsOpts = tabs.tabs("options"),
                onSelect = tabsOpts.onSelect,
                iconStyles = $.array.filter($.easyui.iconStyles, function (item) {
                    return item.size.indexOf(opts.size) > -1;
                }).sort(function (a, b) { return a.sort - b.sort; }),
                last,
                singleSelect = function (e) {
                    if (last) { last.removeClass("selected"); }
                    last = $(this);
                    last.addClass("selected");
                    value = last.attr("title");
                    refreshToolbar();
                },
                multipleSelect = function (e) {
                    var li = $(this), iconCls = li.attr("title");
                    if (!value) { value = []; }
                    if (!$.isArray(value)) { value = [value]; }
                    $.array[li.hasClass("selected") ? "remove" : "attach"](value, iconCls);
                    li.toggleClass("selected");
                    refreshToolbar();
                };
            dia.setValue = function (val) {
                value = val;
                refreshToolbar();
                refreshCheckedStatus();
            };
            dia.getValue = function () {
                return value;
            };
            tabsOpts.onSelect = function (title, index) {
                if ($.isFunction(onSelect)) { onSelect.apply(this, arguments); }
                var tab = tabs.tabs("getTab", index), tabOpts = tab.panel("options"), style = tabOpts.id;
                if (tab.find("ul.icon-selector-ul").length) { return; }
                var icons = $.array.filter($.easyui.icons[style], opts.size == "32" ?
                        function (icon) { return icon.iconCls.indexOf("32") > -1; } :
                        function (icon) { return icon.iconCls.indexOf("32") == -1; }
                    );
                var ul = $("<ul class='icon-selector-ul'></ul>").appendTo(tab);
                $.each(icons, function () {
                    var li = $("<li></li>").attr("title", this.iconCls).appendTo(ul).click(opts.multiple ? multipleSelect : singleSelect);
                    $("<span>&nbsp;</span>").addClass(this.iconCls).appendTo(li);
                });
                if (opts.size == "32") { ul.addClass("icon-selector-ul-32"); }
            };
            $.util.exec(function () { dia.setValue(value); });
            $.each(iconStyles, function (i, item) {
                tabs.tabs("add", {
                    id: item.style, title: item.name, iconCls: "", closable: false, selected: false, refreshable: false
                });
            });
            if ($.easyui.iconStyles.length) { tabs.tabs("select", 0); }
            function refreshToolbar() {
                toolbar.toolbar("clear");
                if (value) {
                    if ($.isArray(value)) {
                        var title = value.join("\n"),
                            tip = $("<span>，详情</span>").css({
                                color: "Blue"
                            }).attr("title", title),
                            clear = $("<div>清除选择</div>").css({
                                color: "Blue", cursor: "pointer",
                                width: 100,
                                textAlign: "right"
                            }).attr("title", "清除所有选择的项").click(function () {
                                tabs.find("ul>li.selected").removeClass("selected");
                                dia.setValue(null);
                            });
                        toolbar.toolbar("appendItem", [
                            "当前共选中的图标数量为：", value.length, tip, clear
                        ]);
                    } else {
                        toolbar.toolbar("appendItem", [
                            "当前选中的图标值为：", { type: "button", options: { iconCls: value, plain: true } }, value
                        ]);
                    }
                } else {
                    toolbar.toolbar("appendItem", "当前没有选中图标");
                }
            };
            function refreshCheckedStatus() {
                var li = dia.dialog("body").find("ul.icon-selector-ul li").removeClass("selected");
                if ($.isArray(value)) {
                    $.each(value, function (i, n) {
                        if (n) { li.find("span." + n).parent().addClass("selected"); }
                    });
                } else {
                    if (value) { last = li.find("span." + value).parent().addClass("selected"); }
                }
            };
        });
        return dia;
    };

})(jQuery);