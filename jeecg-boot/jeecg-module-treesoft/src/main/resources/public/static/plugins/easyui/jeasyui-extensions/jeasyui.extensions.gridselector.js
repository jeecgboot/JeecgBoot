/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI gridselector Extensions 1.0 beta
* jQuery EasyUI gridselector 组件扩展
* jeasyui.extensions.gridselector.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late
*   5、jeasyui.extensions.window.js v1.0 beta late
*   6、jeasyui.extensions.dialog.js v1.0 beta late
*   7、jeasyui.extensions.datagrid.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {


    $.util.namespace("$.easyui");


    //  增加自定义扩展方法 $.easyui.showGridSelector；该方法弹出一个 easyui-datagrid 选择框窗口；该方法定义如下参数：
    //      options: 这是一个 JSON-Object 对象；
    //              具体格式参考 $.easyui.showDialog 方法的参数 options 的格式 和 easyui-datagrid 的初始化参数 options 的格式；
    //              该参数格式在 $.easyui.showDialog 和 easyui-datagrid 参数 options 格式基础上扩展了如下属性：
    //          extToolbar:
    //          selected:
    //          onEnter :
    //  返回值：返回弹出窗口的 easyui-dialog 控件对象(jQuery-DOM 格式)。
    $.easyui.showGridSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.showGridSelector.apply(this, arguments); }
        var opts = $.extend({
            width: 580, height: 360, minWidth: 580, minHeight: 360,
            title: "选择数据，" + (options.singleSelect ? "单选" : "多选"),
            iconCls: "icon-hamburg-zoom",
            maximizable: true,
            collapsible: true,
            selected: null,
            extToolbar: false,
            onEnter: function (val) { }
        }, options);
        var value = opts.singleSelect ? opts.selected : ($.util.likeArrayNotString(opts.selected) ? opts.selected : (opts.selected ? [opts.selected] : [])),
            tempData = value ? ($.util.likeArrayNotString(value) ? $.array.clone(value) : value) : null,
            dg = null,
            dia = $.easyui.showDialog($.extend({}, opts, {
                content: "<div class=\"grid-selector-container\"></div>",
                saveButtonText: "确定",
                saveButtonIconCls: "icon-ok",
                enableApplyButton: false,
                toolbar: "",
                onSave: function () {
                    if ($.isFunction(opts.onEnter)) { return opts.onEnter.call(dg[0], value); }
                }
            }));
        $.util.exec(function () {
            var container = dia.find(".grid-selector-container"),
                dgOpts = $.util.excludeProperties(opts, ["id", "width", "height", "left", "top", "cls", "bodyCls", "style", "content", "href"]),
                selectRow = function (row) {
                    var tOpts = dg.datagrid("options"), idField = tOpts.idField;
                    dg.datagrid("selectRecord", idField && row && row[idField] ? row[idField] : row);
                },
                refreshValue = function () {
                    var tOpts = dg.datagrid("options");
                    if (opts.singleSelect) {
                        var row = dg.datagrid("getSelected");
                        value = row ? row : null;
                    } else {
                        value = dg.datagrid("getSelections");
                    }
                };
            dgOpts = $.extend({ striped: true, checkOnSelect: true, selectOnCheck: true, rownumbers: true }, dgOpts, {
                noheader: true, fit: true, border: false, doSize: true,
                onSelect: function (index, row) { refreshValue(); },
                onUnselect: function (index, row) { refreshValue(); },
                onSelectAll: function (rows) { refreshValue(); },
                onUnselectAll: function (rows) { refreshValue(); },
                onLoadSuccess: function (data) {
                    $.fn.datagrid.defaults.onLoadSuccess.apply(this, arguments);
                    if ($.isFunction(opts.onLoadSuccess) && opts.onLoadSuccess != $.fn.datagrid.defaults.onLoadSuccess) {
                        opts.onLoadSuccess.apply(this, arguments);
                    }
                    if (!tempData) { return; }
                    if ($.util.likeArrayNotString(tempData)) {
                        $.each(tempData, function (i, val) {
                            selectRow(val);
                        });
                    } else {
                        selectRow(tempData);
                    }
                    refreshValue();
                }
            });
            if ($.util.likeArrayNotString(opts.toolbar) && opts.extToolbar) {
                dgOpts.toolbar = null;
                container.append("<div data-options=\"region: 'north', split: false, border: false\"><div class=\"grid-selector-toolbar\"></div></div>" +
                    "<div data-options=\"region: 'center', border: false\"><div class=\"grid-selector\"></div></div>");
                container.find("div.grid-selector-toolbar").toolbar({ data: opts.toolbar });
                dg = container.find("div.grid-selector");
                container.layout({ fit: true });
            } else {
                dg = container.addClass("grid-selector");
            }
            dia.grid = dg.datagrid(dgOpts);
        });
        return dia;
    };

    //  增加自定义扩展方法 $.easyui.showDblGridSelector；该方法弹出一个包含两个 easyui-datagrid 控件的选择框窗口；该方法定义如下参数：
    //      options: 这是一个 JSON-Object 对象；
    //              具体格式参考 $.easyui.showDialog 方法的参数 options 的格式 和 easyui-datagrid 的初始化参数 options 的格式；
    //              该参数格式在 $.easyui.showDialog 和 easyui-datagrid 参数 options 格式基础上扩展了如下属性：
    //          extToolbar:
    //          selected:
    //          onEnter :
    $.easyui.showDblGridSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.showDblGridSelector.apply(this, arguments); }
        var opts = $.extend({
            width: 900, height: 580, minWidth: 600, minHeight: 480,
            title: "选择数据，多选",
            iconCls: "icon-hamburg-zoom",
            maximizable: true,
            collapsible: true,
            selected: null,
            centerWidth: 55,
            extToolbar: false,
            onEnter: function (value) { }
        }, options);
        var value = $.util.likeArrayNotString(opts.selected) ? opts.selected : (opts.selected ? [opts.selected] : []),
            tempData = $.array.clone(value), dg = null,
            dia = $.easyui.showDialog($.extend({}, opts, {
                content: "<div class=\"grid-selector-container\"></div>",
                saveButtonText: "确定",
                saveButtonIconCls: "icon-ok",
                enableApplyButton: false,
                toolbar: opts.extToolbar ? "" : opts.toolbar,
                onSave: function () {
                    if ($.isFunction(opts.onEnter)) { return opts.onEnter.call(dg[0], value); }
                }
            }));
        $.util.exec(function () {
            var diaOpts = dia.dialog("options"), onResize = diaOpts.onResize, init = false,
                container = dia.find(".grid-selector-container"), northPanel = null,
                width = (($.isNumeric(opts.width) ? opts.width : dia.outerWidth()) - opts.centerWidth) / 2,
                leftPanel = $("<div data-options=\"region: 'west', split: false, border: false\"></div>").width(width).appendTo(container),
                centerPanel = $("<div data-options=\"region: 'center', border: true, bodyCls: 'grid-selector-buttons'\"></div>").appendTo(container),
                rightPanel = $("<div data-options=\"region: 'east', split: false, border: false\"></div>").width(width).appendTo(container),
                dgOpts = $.util.excludeProperties(opts, ["id", "width", "height", "left", "top", "cls", "bodyCls", "style", "content", "href"]),
                dgOpts1 = $.extend({ striped: true, checkOnSelect: true, selectOnCheck: true, rownumbers: true }, dgOpts, {
                    singleSelect: false, title: "待选择项", toolbar: "", fit: true, border: false, doSize: true,
                    noheader: false, iconCls: null, collapsible: false, minimizable: false, maximizable: false, closable: false,
                    rowContextMenu: [
                        { text: "选择该行", iconCls: null, handler: function (e, index, row) { selectRow(row); refreshValue(); } }, "-",
                        { text: "选择全部", iconCls: null, handler: function (e, index, row) { btn1.trigger("click"); } },
                        { text: "选择勾选部分", iconCls: null, handler: function (e, index, row) { btn2.trigger("click"); } }
                    ],
                    onLoadSuccess: function () {
                        $.fn.datagrid.defaults.onLoadSuccess.apply(this, arguments);
                        if ($.isFunction(opts.onLoadSuccess) && opts.onLoadSuccess != $.fn.datagrid.defaults.onLoadSuccess) {
                            opts.onLoadSuccess.apply(this, arguments);
                        }
                        if (!init) {
                            $.each(tempData, function (i, val) { selectRow(val); });
                            refreshValue();
                            init = true;
                        }
                    }
                }),
                dgOpts2 = $.extend({}, dgOpts1, {
                    url: null, queryParams: {}, remoteSort: false, pagination: false, title: "已选择项", iconCls: null, pagingMenu: false, refreshMenu: false,
                    rowContextMenu: [
                        { text: "取消该行", iconCls: null, handler: function (e, index, row) { unselectRow(row); refreshValue(); } }, "-",
                        { text: "取消全部", iconCls: null, handler: function (e, index, row) { btn4.trigger("click"); } },
                        { text: "取消勾选部分", iconCls: null, handler: function (e, index, row) { btn3.trigger("click"); } }
                    ],
                    onLoadSuccess: $.fn.datagrid.defaults.onLoadSuccess
                }),
                dg1 = $("<div></div>").appendTo(leftPanel),
                dg2 = dg = $("<div class=\"grid-selector\"></div>").appendTo(rightPanel),
                btn1 = $("<a></a>").linkbutton({ plain: true, iconCls: "pagination-last" }).tooltip({ content: "选择全部" }).appendTo(centerPanel).click(function () {
                    var rows = dg1.datagrid("getRows"), data = $.array.clone(rows);
                    $.each(data, function (i, val) { selectRow(val); });
                    dg1.datagrid("unselectAll");
                    refreshValue();
                }),
                btn2 = $("<a></a>").linkbutton({ plain: true, iconCls: "pagination-next" }).tooltip({ content: "选择勾选部分" }).appendTo(centerPanel).click(function () {
                    var rows = dg1.datagrid("getSelections"), data = $.array.clone(rows);
                    if (!data.length) { return $.easyui.messager.show("您未选择任何数据。"); }
                    $.each(data, function (i, val) { selectRow(val); });
                    dg1.datagrid("unselectAll");
                    refreshValue();
                }),
                btn3 = $("<a></a>").linkbutton({ plain: true, iconCls: "pagination-prev" }).tooltip({ content: "取消勾选部分" }).appendTo(centerPanel).click(function () {
                    var rows = dg2.datagrid("getSelections"), data = $.array.clone(rows);
                    if (!data.length) { return $.easyui.messager.show("您未选择任何数据。"); }
                    $.each(data, function (i, val) { unselectRow(val); });
                    dg2.datagrid("unselectAll");
                    refreshValue();
                }),
                btn4 = $("<a></a>").linkbutton({ plain: true, iconCls: "pagination-first" }).tooltip({ content: "取消全部" }).appendTo(centerPanel).click(function () {
                    var rows = dg2.datagrid("getRows"), data = $.array.clone(rows);
                    $.each(data, function (i, val) { unselectRow(val); });
                    dg2.datagrid("unselectAll");
                    refreshValue();
                }),
                selectRow = function (row) {
                    if (!row) { return; }
                    var tOpts = dg2.datagrid("options"), idField = tOpts.idField;
                    if (!row[idField]) {
                        var rows = dg1.datagrid("getRows"), index = dg1.datagrid("getRowIndex", row);
                        if (index > -1) { row = rows[index]; }
                    }
                    var isExists = dg2.datagrid("getRowIndex", row[idField] ? row[idField] : row) > -1;
                    if (!isExists) { dg2.datagrid("appendRow", row); }
                },
                unselectRow = function (row) {
                    if (!row) { return; }
                    var index = dg2.datagrid("getRowIndex", row);
                    if (index > -1) { dg2.datagrid("deleteRow", index); }
                },
                refreshValue = function () {
                    var rows = dg2.datagrid("getRows");
                    value = $.array.clone(rows);
                };
            diaOpts.onResize = function (w, h) {
                if ($.isFunction(onResize)) { onResize.apply(this, arguments); }
                $.util.exec(function () {
                    var ww = (dia.panel("options").width - diaOpts.centerWidth) / 2;
                    leftPanel.panel("resize", { width: ww });
                    rightPanel.panel("resize", { width: ww });
                    container.layout("resize");
                });
            };
            if (opts.extToolbar && opts.toolbar) {
                northPanel = $("<div data-options=\"region: 'north', split: false, border: false\"></div>").appendTo(container);
                $("<div class=\"grid-selector-toolbar\"></div>").appendTo(northPanel).toolbar({ data: opts.toolbar });;
            }
            dia.grid = dg1.datagrid(dgOpts1);
            dia.valueGrid = dg2.datagrid(dgOpts2);

            container.layout({ fit: true });
        });
        return dia;
    };


    $.easyui.showTreeSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.showTreeSelector.apply(this, arguments); }
    };

    $.easyui.showTreeGridSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.showTreeGridSelector.apply(this, arguments); }
    };

    $.easyui.showTreeWithGridSelector = function (options) {
        if (options && options.topMost && $ != $.util.$) { return $.util.$.easyui.showDblTreeGridSelector.apply(this, arguments); }
    };

})(jQuery);