/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI panel Extensions 1.0 beta
* jQuery EasyUI panel 组件扩展
* jeasyui.extensions.panel.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {


    $.fn.panel.extensions = {};


    //  easyui-panel、easyui-window、easyui-dialog 卸载时回收内存，主要用于 layout、panel(及其继承组件) 使用 iframe 嵌入网页时的内存泄漏问题
    var onBeforeDestroy = function () {
        $("iframe,frame", this).each(function () {
            try {
                if (this.contentWindow && this.contentWindow.document && this.contentWindow.close) {
                    this.contentWindow.document.write("");
                    this.contentWindow.close();
                }
                if ($.isFunction(window.CollectGarbage)) { window.CollectGarbage(); }
            } catch (ex) { }
        }).remove();
    };
    $.fn.panel.defaults.onBeforeDestroy = onBeforeDestroy;
    $.fn.window.defaults.onBeforeDestroy = onBeforeDestroy;
    $.fn.dialog.defaults.onBeforeDestroy = onBeforeDestroy;
    $.fn.datagrid.defaults.onBeforeDestroy = onBeforeDestroy;
    $.fn.propertygrid.defaults.onBeforeDestroy = onBeforeDestroy;
    $.fn.treegrid.defaults.onBeforeDestroy = onBeforeDestroy;


    var _onResize = {
        panel: $.fn.panel.defaults.onResize,
        window: $.fn.window.defaults.onResize,
        dialog: $.fn.dialog.defaults.onResize
    };
    var onResize = function (width, height) {
        var p = $(this), isWin = p.panel("isWindow"), isDia = p.panel("isDialog"),
            plugin = isDia ? "dialog" : (isWin ? "window" : "panel"),
            _onResizeFn = _onResize[plugin];
        if ($.isFunction(_onResizeFn)) { _onResizeFn.apply(this, arguments); }
        if (!p.panel("inLayout")) {
            var opts = p.panel("options"),
                minWidth = $.isNumeric(opts.minWidth) ? opts.minWidth : defaults.minHeight,
                maxWidth = $.isNumeric(opts.maxWidth) ? opts.maxWidth : defaults.maxWidth,
                minHeight = $.isNumeric(opts.minHeight) ? opts.minHeight : defaults.minHeight,
                maxHeight = $.isNumeric(opts.maxHeight) ? opts.maxHeight : defaults.maxHeight;
            var resizable = false;
            if (width > maxWidth) { width = maxWidth; resizable = true; }
            if (width < minWidth) { width = minWidth; resizable = true; }
            if (height > maxHeight) { height = maxHeight; resizable = true; }
            if (height < minHeight) { height = minHeight; resizable = true; }
            if (resizable && !opts.fit) {
                p[plugin]("resize", { width: width, height: height });
            }
        }
    };

    var _onMove = {
        panel: $.fn.panel.defaults.onMove,
        window: $.fn.window.defaults.onMove,
        dialog: $.fn.dialog.defaults.onMove
    };
    var onMove = function (left, top) {
        var p = $(this), isWin = p.panel("isWindow"), isDia = p.panel("isDialog"),
            plugin = isDia ? "dialog" : (isWin ? "window" : "panel"),
            _onMoveFn = _onMove[plugin], opts = p.panel("options");
        if ($.isFunction(_onMoveFn)) { _onMoveFn.apply(this, arguments); }
        if (opts.maximized) { return p[plugin]("restore"); }
        if (!opts.inContainer) { return; }
        var panel = p.panel("panel"), parent = panel.parent(), isRoot = parent.is("body"),
            scope = $.extend({}, isRoot ? $.util.windowSize() : { width: parent.innerWidth(), height: parent.innerHeight() }),
            width = $.isNumeric(opts.width) ? opts.width : panel.outerWidth(),
            height = $.isNumeric(opts.height) ? opts.height : panel.outerHeight(),
            moveable = false;
        if (left < 0) { left = 0; moveable = true; }
        if (top < 0) { top = 0; moveable = true; }
        if (moveable) { return p[plugin]("move", { left: left, top: top }); }
        if (left + width > scope.width && left > 0) { left = scope.width - width; moveable = true; }
        if (top + height > scope.height && top > 0) { top = scope.height - height; moveable = true; }
        if (moveable) { return p[plugin]("move", { left: left, top: top }); }
    };



    var inLayout = function (target) {
        var t = $(target), body = t.panel("body"), panel = t.panel("panel");
        return body.hasClass("layout-body") && panel.hasClass("layout-panel");
    };

    var inTabs = function (target) {
        var t = $(target), panel = t.panel("panel"), panels = panel.parent(), container = panels.parent();
        return panels.hasClass("tabs-panels") && container.hasClass("tabs-container");
    };

    var inAccordion = function (target) {
        var t = $(target), body = t.panel("body"), panel = t.panel("panel"), container = panel.parent();
        return (body.hasClass("accordion-body") && container.hasClass("accordion") && $.data(container[0], "accordion")) ? true : false;
    };

    var isWindow = function (target) {
        var t = $(target), body = t.panel("body");
        return body.hasClass("window-body") && body.parent().hasClass("window");
    };

    var isDialog = function (target) {
        var t = $(target), body = t.panel("body");
        return isWindow(target) && (body.children("div.panel").children("div.panel-body.dialog-content").length ? true : false);
    };





    function parseExtensionsBegin(opts) {
        opts._extensionsPanel = { href: opts.href, content: opts.content };
        if (opts.iniframe) {
            opts.href = null;
            opts.content = null;
        }


    };
    function parseExtensionsEnd(target) {
        var panel = $(target), opts = panel.panel("options"),
            exts = opts._extensionsPanel ? opts._extensionsPanel : opts._extensionsPanel = { href: opts.href, content: opts.content };
        opts.href = exts.href; opts.content = exts.content;
        if (opts.bodyStyle) {
            panel.panel("body").css(opts.bodyStyle);
        }
        if (opts.iniframe) {
            refresh(target);
        }
    };

    var _panel = $.fn.panel;
    $.fn.panel = function (options, param) {
        if (typeof options == "string") {
            return _panel.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "panel") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.panel.parseOptions(this), $.parser.parseOptions(this, [
                    { minWidth: "number", maxWidth: "number", minHeight: "number", maxHeight: "number" },
                    { iniframe: "boolean", inContainer: "boolean", keepTitle: "boolean" }
                ]), options);
            parseExtensionsBegin(opts);
            _panel.call(jq, opts);
            parseExtensionsEnd(this);
        });
    };
    $.union($.fn.panel, _panel);






    function loadPanel(target) {
        var t = $(target), state = $.data(target, "panel"), opts = state.options;
        if (opts.href) {
            if (!state.isLoaded || !opts.cache) {
                var params = $.extend({}, opts.queryParams);
                if (opts.onBeforeLoad.call(target, params) == false) {
                    return;
                }
                state.isLoaded = false;
                destroyContent(target);
                if (opts.loadingMessage) {
                    t.html($("<div class=\"panel-loading\"></div>").html(opts.loadingMessage));
                }
                opts.loader.call(target, params, function (data) {
                    loadContent(opts.extractor.call(target, data));
                    opts.onLoad.apply(target, arguments);
                    state.isLoaded = true;
                }, function () {
                    opts.onLoadError.apply(target, arguments);
                });
            }
        } else {
            if (opts.content) {
                if (!state.isLoaded) {
                    destroyContent(target);
                    loadContent(opts.content);
                    state.isLoaded = true;
                }
            }
        }
        function loadContent(content) {
            t.html(content);
            $.parser.parse(t);
        };
    };
    function destroyContent(target) {
        var t = $(target);
        t.find(".combo-f").each(function () {
            $(this).combo("destroy");
        });
        t.find(".m-btn").each(function () {
            $(this).menubutton("destroy");
        });
        t.find(".s-btn").each(function () {
            $(this).splitbutton("destroy");
        });
        t.find(".tooltip-f").each(function () {
            $(this).tooltip("destroy");
        });
        t.children("div").each(function () {
            $(this)._fit(false);
        });
    };





    function refresh(target, href) {
        var state = $.data(target, "panel"), opts = state.options;
        state.isLoaded = false;
        if (href) {
            if (typeof href == "string") {
                opts.href = href;
            } else {
                opts.queryParams = href;
            }
        }
        if (opts.iniframe) {
            var exts = opts._extensionsPanel ? opts._extensionsPanel : opts._extensionsPanel = { href: opts.href, content: opts.content };
            exts.href = opts.href; exts.content = opts.content;
            opts.href = null;
            opts.content = "<iframe class=\"panel-iframe\" frameborder=\"0\" width=\"100%\" height=\"100%\" marginwidth=\"0px\" marginheight=\"0px\" scrolling=\"auto\"></iframe>";
            loadPanel(target);
            opts.href = exts.href; opts.content = exts.content;
            getIframe(target).bind({
                load: function () {
                    if ($.isFunction(opts.onLoad)) { opts.onLoad.apply(target, arguments); }
                },
                error: function () {
                    if ($.isFunction(opts.onLoadError)) { opts.onLoadError.apply(target, arguments); }
                }
            }).attr("src", opts.href || "");
        } else {
            loadPanel(target);
        }
    };



    function getIframe(target) {
        var p = $(target), body = p.panel("body");
        return body.children("iframe.panel-iframe");
    };

    var _header = $.fn.panel.methods.header;
    function getHeader(target) {
        var t = $(target);
        if (!inTabs(target)) { return _header.call(t, t); }
        var panel = t.panel("panel"), index = panel.index(), tabs = panel.closest(".tabs-container");
        return tabs.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li").eq(index);
    };

    var _setTitle = $.fn.panel.methods.setTitle;
    function setTitle(target, title) {
        var t = $(target);
        if (!inTabs(target)) { return _setTitle.call(t, t, title); }
        if (!title) { return; }
        var opts = t.panel("options"), header = t.panel("header");
        opts.title = title;
        header.find(">a.tabs-inner>span.tabs-title").text(title);
    };


    var methods = $.fn.panel.extensions.methods = {
        //  判断当前 easyui-panel 是否为 easyui-layout 的 panel 部件；
        //  返回值：如果当前 easyui-panel 是 easyui-layout 的 panel 部件，则返回 true，否则返回 false。
        inLayout: function (jq) { return inLayout(jq[0]); },

        //  判断当前 easyui-panel 是否为 easyui-tabs 的选项卡。
        inTabs: function (jq) { return inTabs(jq[0]); },

        //  判断当前 easyui-panel 是否为 easyui-accordion 中的一个折叠面板。
        inAccordion: function (jq) { return inAccordion(jq[0]); },

        //  判断当前 easyui-panel 是否为 easyui-window 组件；
        isWindow: function (jq) { return isWindow(jq[0]); },

        //  判断当前 easyui-panel 是否为 easyui-dialog 组件；
        isDialog: function (jq) { return isDialog(jq[0]); },

        //  增加 easyui-panel 控件的扩展方法；该方法用于获取当前在 iniframe: true 时当前 panel 控件中的 iframe 容器对象；
        //  备注：如果 inirame: false，则该方法返回一个空的 jQuery 对象。
        iframe: function (jq) { return getIframe(jq[0]); },

        //  重写 easyui-panel 控件的 refresh 方法，用于支持 iniframe 属性。
        refresh: function (jq, href) { return jq.each(function () { refresh(this, href); }); },

        //  重写 easyui-panel 控件的 header 方法，支持位于 easyui-tabs 中的 tab-panel 部件获取 header 对象；
        //  备注：如果该 panel 位于 easyui-tabs 中，则该方法返回 easyui-tabs 的 div.tabs-header div.tabs-wrap ul.tabs 中对应该 tab-panel 的 li 对象。
        header: function (jq) { return getHeader(jq[0]); },

        //  重写 easyui-panel 控件的 setTitle 方法，支持位于 easyui-tabs 中的 tab-panel 部件设置 title 操作；
        //  返回值：返回当前选项卡控件 easyui-panel 的 jQuery 链式对象。
        setTitle: function (jq, title) { return jq.each(function () { setTitle(this, title); }); }
    };
    var defaults = $.fn.panel.extensions.defaults = {

        //  增加 easyui-panel 控件的自定义属性，该属性表示 href 加载的远程页面是否装载在一个 iframe 中。
        iniframe: false,

        //  增加 easyui-panel 控件的自定义属性，表示 easyui-panel 面板的最小宽度。
        minWidth: 10,

        //  增加 easyui-panel 控件的自定义属性，表示 easyui-panel 面板的最大宽度。
        maxWidth: 10000,

        //  增加 easyui-panel 控件的自定义属性，表示 easyui-panel 面板的最小高度。
        minHeight: 10,

        //  增加 easyui-panel 控件的自定义属性，表示 easyui-panel 面板的最大高度。
        maxHeight: 10000,

        //  增加 easyui-panel 控件的自定义属性，重新定义的 onResize 事件。用于扩展四个新增属性 minWidth、maxWidth、minHeight、maxHeight 的功能。
        onResize: onResize,

        //  扩展 easyui-panel、easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口是否无法移除父级对象边界，默认为 true。
        inContainer: true,

        //  扩展 easyui-panel、easyui-window 以及 easyui-dialog 控件的自定义属性，表示该面板 body 对象的自定义 CSS 样式；
        //  该属性作用于 panel-body 对象；格式请参照 style 属性；
        bodyStyle: null,

        //  重写 easyui-panel、easyui-window 以及 easyui-dialog 控件的原生事件 onMove，以支持相应扩展功能。
        onMove: onMove
    };

    $.extend($.fn.panel.defaults, defaults);
    $.extend($.fn.panel.methods, methods);

})(jQuery);