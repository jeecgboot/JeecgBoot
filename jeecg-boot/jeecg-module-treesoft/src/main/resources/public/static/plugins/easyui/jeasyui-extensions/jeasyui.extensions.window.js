/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window Extensions 1.0 beta
* jQuery EasyUI window 组件扩展
* jeasyui.extensions.window.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {


    $.fn.window.extensions = {};


    var initialize = function (target) {
        var t = $(target), state = $.data(target, "window"), opts = t.window("options"), win = t.window("window"), body = t.window("body");
        if (!opts._initialized) {
            t.window("header").on({
                dblclick: function () {
                    var opts = t.window("options");
                    if (opts.autoRestore) { if (opts.maximized) { t.window("restore"); } else if (opts.maximizable) { t.window("maximize"); } }
                },
                contextmenu: function (e) {
                    var opts = t.window("options");
                    if (opts.enableHeaderContextMenu) {
                        e.preventDefault();
                        var items = [
                            { text: "最大化", iconCls: "panel-tool-max", disabled: !opts.maximized && opts.maximizable ? false : true, onclick: function () { t.window("maximize"); } },
                            { text: "恢复", iconCls: "panel-tool-restore", disabled: opts.maximized ? false : true, onclick: function () { t.window("restore"); } },
                            "-",
                            { text: "关闭", iconCls: "panel-tool-close", disabled: !opts.closable, onclick: function () { t.window("close"); } }
                        ];
                        var headerContextMenu = $.array.likeArray(opts.headerContextMenu) ? opts.headerContextMenu : [];
                        if (headerContextMenu.length) { $.array.insertRange(items, 0, $.util.merge([], headerContextMenu, "-")); }
                        items = parseContextMenuMap(e, items, t);
                        $.easyui.showMenu({ items: items, left: e.pageX, top: e.pageY });
                    }
                }
            });
            if (opts.bodyCls) { body.addClass(opts.bodyCls); }

            if (opts.draggable) {
                var dragOpts = state.window.draggable("options"), cursor = dragOpts.cursor,
                    onBeforeDrag = dragOpts.onBeforeDrag, onStartDrag = dragOpts.onStartDrag, onStopDrag = dragOpts.onStopDrag, onDrag = dragOpts.onDrag;
                dragOpts.cursor = "default";
                dragOpts.onBeforeDrag = function (e) {
                    var ret = onBeforeDrag.apply(this, arguments);
                    if (ret == false || e.which != 1 || t.window("options").maximized) { return false; }
                    dragOpts.cursor = cursor;
                };
                dragOpts.onStartDrag = function () {
                    onStartDrag.apply(this, arguments);
                    t.window("body").addClass("window-body-hidden").children().addClass("window-body-hidden-proxy");
                };
                dragOpts.onStopDrag = function () {
                    onStopDrag.apply(this, arguments);
                    t.window("body").removeClass("window-body-hidden").children().removeClass("window-body-hidden-proxy");
                    dragOpts.cursor = "default";
                };
                dragOpts.onDrag = function (e) {
                    if (!opts.inContainer) { return onDrag.apply(this, arguments); }
                    var left = e.data.left, top = e.data.top,
                        p = win.parent(), root = p.is("body"),
                        scope = $.extend({}, root ? $.util.windowSize() : { width: p.innerWidth(), height: p.innerHeight() }),
                        width = $.isNumeric(opts.width) ? opts.width : win.outerWidth(),
                        height = $.isNumeric(opts.height) ? opts.height : win.outerHeight();
                    if (left < 0) { left = 0; }
                    if (top < 0) { top = 0; }
                    if (left + width > scope.width && left > 0) { left = scope.width - width; b = true; }
                    if (top + height > scope.height && top > 0) { top = scope.height - height; b = true; }
                    state.proxy.css({
                        display: 'block',
                        left: left,
                        top: top
                    });
                    return false;
                };
            }
            if (opts.resizable) {
                var resizableOpts = state.window.resizable("options"),
                    _onResize = resizableOpts.onResize, _onStopResize = resizableOpts.onStopResize;
                resizableOpts.onResize = function (e) {
                    if (!opts.minWidth && !opts.maxWidth && !opts.minHeight && !opts.maxHeight) {
                        return _onResize.apply(this, arguments);
                    }
                    state.proxy.css({ left: e.data.left, top: e.data.top });
                    var width = e.data.width, height = e.data.height,
                        minWidth = $.isNumeric(opts.minWidth) ? opts.minWidth : defaults.minHeight,
                        maxWidth = $.isNumeric(opts.maxWidth) ? opts.maxWidth : defaults.maxWidth,
                        minHeight = $.isNumeric(opts.minHeight) ? opts.minHeight : defaults.minHeight,
                        maxHeight = $.isNumeric(opts.maxHeight) ? opts.maxHeight : defaults.maxHeight;
                    if (width > opts.maxWidth) { width = maxWidth; resizable = true; }
                    if (width < opts.minWidth) { width = minWidth; resizable = true; }
                    if (height > opts.maxHeight) { height = maxHeight; resizable = true; }
                    if (height < opts.minHeight) { height = minHeight; resizable = true; }
                    state.proxy._outerWidth(width);
                    state.proxy._outerHeight(height);
                    return false;
                };
                resizableOpts.onStopResize = function (e) {
                    var ret = _onStopResize.apply(this, arguments);
                    if (t.window("options").maximized) {
                        t.window("restore").window("maximize");
                    }
                    return ret;
                };
            }

            opts._initialized = true;
        }
    };

    function parseContextMenuMap(e, menus, win) {
        return $.array.map(menus, function (value, index) {
            if (!value || $.util.isString(value)) { return value; }
            var ret = $.extend({}, value);
            ret.id = $.isFunction(value.id) ? value.id.call(ret, e, win) : value.id;
            ret.text = $.isFunction(value.text) ? value.text.call(ret, e, win) : value.text;
            ret.iconCls = $.isFunction(value.iconCls) ? value.iconCls.call(ret, e, win) : value.iconCls;
            ret.disabled = $.isFunction(value.disabled) ? value.disabled.call(ret, e, win) : value.disabled;
            ret.hideOnClick = $.isFunction(value.hideOnClick) ? value.hideOnClick.call(ret, e, win) : value.hideOnClick;
            ret.onclick = $.isFunction(value.onclick) ? function (e, item, menu) { value.onclick.call(this, e, win, item, menu); } : value.onclick;
            ret.handler = $.isFunction(value.handler) ? function (e, item, menu) { value.handler.call(this, e, win, item, menu); } : value.handler;
            if (ret.children && ret.children.length) { ret.children = parseContextMenuMap(e, ret.children, win); }
            return ret;
        });
    };


    var _window = $.fn.window;
    $.fn.window = function (options, param) {
        if (typeof options == "string") {
            return _window.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "window") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.window.parseOptions(this), $.parser.parseOptions(this, [{
                    autoHCenter: "boolean", autoVCenter: "boolean", autoCloseOnEsc: "boolean",
                    autoRestore: "boolean", enableHeaderContextMenu: "boolean"
                }]), options);
            _window.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.window, _window);



    var methods = $.fn.window.extensions.methods = {};
    var defaults = $.fn.window.extensions.defaults = $.extend({}, $.fn.panel.extensions.defaults, {

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口对象是否在屏幕大小调整的情况下自动进行左右居中，默认为 true。
        autoHCenter: false,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口对象是否在屏幕大小调整的情况下自动进行上下居中，默认为 true。
        autoVCenter: false,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口对象是否在按下 ESC，默认为 true。
        autoCloseOnEsc: true,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口是否在双击头部时自动最大化。
        autoRestore: true,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示是否启用该窗口的右键菜单。
        enableHeaderContextMenu: true,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口的右键菜单；
        //  这是一个数组格式对象，数组中的每一项都是一个 menu-item 元素；该 menu-item 元素格式定义如下：
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, win, item, menu)，其中 this 指向菜单项本身
        headerContextMenu: null
    });

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.window.methods, methods);

    $(function () {
        //  设置当屏幕大小调整时，所有 easyui-window 或 easyui-dialog 窗口在属性 autoHCenter: true 或 autoVCenter: true 的情况下自动居中。
        $(window).resize(function () {
            $(".panel-body.window-body").each(function () {
                var win = $(this), opts = win.window("options");
                if (opts && opts.draggable) {
                    if (opts.autoHCenter || opts.autoVCenter) {
                        var method = opts.autoHCenter && opts.autoVCenter ? "center" : (opts.autoHCenter ? "hcenter" : "vcenter");
                        win.window(method);
                    } else if (opts.inContainer) { win.window("move"); }
                }
            });
        });

        //  在当前打开 modal:true 的 easyui-window 或者 easyui-dialog 时，按 ESC 键关闭顶层的 easyui-window 或者 easyui-dialog 对象。
        $(document).keydown(function (e) {
            if (e.which == 27) {
                $("div.window-mask:last").prevAll("div.panel.window:first").children(".panel-body.window-body").each(function () {
                    var win = $(this), opts = win.window("options");
                    if (opts && opts.closable && opts.autoCloseOnEsc && !win.window("header").find(".panel-tool a").attr("disabled")) {
                        $.util.exec(function () { win.window("close"); });
                    }
                });
            }
        });

        //  点击模式对话框（例如 easyui-messager、easyui-window、easyui-dialog）的背景遮蔽层使窗口闪动
        $("body").on("click", "div.window-mask:last", function (e) {
            $(this).prevAll("div.panel.window:first").shine();
        });
    });

})(jQuery);