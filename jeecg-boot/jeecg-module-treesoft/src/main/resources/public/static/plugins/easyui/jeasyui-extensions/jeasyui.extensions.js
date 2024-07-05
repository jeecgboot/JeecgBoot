/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI Generic Plugins Basic Library 1.0 beta
* jQuery EasyUI 通用插件基础库
* jeasyui.extensions.js
*
* 依赖项：jquery.jdirk.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.util.namespace("$.easyui");


    $.easyui.getTopEasyuiMessager = function () {
        if ($.util.isUtilTop) { return $.messager; }
        return $.util.$ && $.util.$.messager ? $.util.$.messager : $.messager;
    };
    $.easyui.messager = $.easyui.getTopEasyuiMessager();


    $.easyui.getTopEasyuiTooltip = function () {
        if ($.util.isUtilTop) { return $.fn.tooltip; }
        return $.util.$ && $.util.$.fn && $.util.$.fn.tooltip ? $.util.$.fn.tooltip : $.fn.tooltip;
    };
    $.easyui.tooltip = $.fn.tooltip;

    //  对某个元素设置 easyui-tooltip 属性；该函数定义如下参数：
    //      target:     表示要设置 easyui-tooltip 的元素，可以是一个 jQuery 选择器字符串，也可以是一个 DOM 对象或者 jQuery 对象。
    //      options:    表示初始化 easyui-tooltip 的参数信息，为一个 JSON-Object；
    //  备注：通过该方法设置的 easyui-tooltip 属性，在触发 mouseover 事件时，加载 easyui-tooltip，在 tooltip-tip 隐藏时，easyui-tooltip 自动调用 destroy 销毁；
    $.easyui.tooltip.init = function (target, options) {
        var t = $(target);
        t.mouseover(function () {
            t.tooltip($.extend({ trackMouse: true }, options, {
                onHide: function () {
                    if ($.isFunction(options.onHide)) { options.onHide.apply(this, arguments); }
                    t.tooltip("destroy");
                }
            })).tooltip("show");
        });
    };




    var icons = { "error": "messager-error", "info": "messager-info", "question": "messager-question", "warning": "messager-warning" },
        _show = $.messager.show, _alert = $.messager.alert, _confirm = $.messager.confirm, _prompt = $.messager.prompt,
        defaults = { title: "操作提醒", confirm: "您确认要进行该操作？", prompt: "请输入相应内容：", icon: "info", loading: "正在加载，请稍等..." };

    //  重写 $.messager.show 方法，使其支持图标以及默认的单个字符串参数的重载；该方法定义如下参数：
    //      options:    表示需要弹出消息的内容、图标和方式等信息，该参数类型可以为如下：
    //          JSON Object: 兼容 $.messager.show 官方默认 API 的所有属性，并在此基础上增加如下参数：
    //              icon: 表示弹出消息的图标，为一个 String 类型值，该值可选的内容与 $.messager.alert 方法的第三个参数可选内容相同；
    //                  包括："error", "info", "question", "warning"；
    //                  具体内容参见 $.messager.alert 该方法的官方默认 API 中第三个参数可选内容。
    //              position: 表示弹出消息的位置，为一个 String 类型值，该值可选的内容定义如下：
    //                  topLeft: 屏幕左上角, topCenter: 屏幕上方中间，topRight: 屏幕右上角
    //                  centerLeft: 屏幕左侧中间，center: 屏幕正中间，centerRight: 屏幕右侧中间
    //                  bottomLeft: 屏幕左下角，bottomCenter: 屏幕下方中间，bottomRight: 屏幕右下角
    //          String: 以 icon: "info"、title: "操作提醒"、msg: options 为默认的方式调用上一重载。
    //  返回值：返回弹出的消息框 easyui-window 对象
    $.messager.show = function (options) {
        var isString = $.util.isString(options) || $.util.isBoolean(options) || $.isNumeric(options);
        if (isString) {
            return arguments.length == 1 ? $.messager.show({ msg: String(options) }) : $.messager.show({ title: options, msg: arguments[1], icon: arguments[2], position: arguments[3] });
        }
        var opts = $.extend({}, $.messager.defaults, { title: "操作提醒", timeout: 4000, showType: "slide" }, options),
            position = {
                topLeft: { showType: "show", right: "", left: 0, top: document.body.scrollTop + document.documentElement.scrollTop, bottom: "" },
                topCenter: { showType: "slide", right: "", top: document.body.scrollTop + document.documentElement.scrollTop, bottom: "" },
                topRight: { showType: "show", left: "", right: 0, top: document.body.scrollTop + document.documentElement.scrollTop, bottom: "" },
                centerLeft: { showType: "fade", left: 0, right: "", bottom: "" },
                center: { showType: "fade", right: "", bottom: "" },
                centerRight: { showType: "fade", left: "", right: 0, bottom: "" },
                bottomLeft: { showType: "show", left: 0, right: "", top: "", bottom: -document.body.scrollTop - document.documentElement.scrollTop },
                bottomCenter: { showType: "slide", right: "", top: "", bottom: -document.body.scrollTop - document.documentElement.scrollTop },
                bottomRight: { showType: "show", left: "", right: 0, top: "", bottom: -document.body.scrollTop - document.documentElement.scrollTop }
            };
        opts.style = (opts.position && position[opts.position]) ? position[opts.position] : position.topCenter;
        if (opts.style.showType) {
            opts.showType = opts.style.showType;
        }
        var iconCls = (opts.icon && icons[opts.icon]) ? icons[opts.icon] : icons.info;
        opts.msg = "<div class='messager-icon " + iconCls + "'></div>" + "<div>" + opts.msg + "</div>";
        return _show(opts);
    };
    $.union($.messager.show, _show);

    //  重写 $.messager.alert 方法，使其支持如下的多种重载方式：
    //      function (message)
    //      function (message, callback)
    //      function (title, message, callback)
    //      function (title, message, icon)
    //      function (title, message, icon, callback)
    //  返回值：返回弹出的消息框 easyui-window 对象
    $.messager.alert = function (title, msg, icon, fn) {
        if (arguments.length == 1) { return _alert(defaults.title, arguments[0], defaults.icon); }
        if (arguments.length == 2) {
            if ($.isFunction(arguments[1])) { return _alert(defaults.title, arguments[0], defaults.icon, arguments[1]); }
            if (arguments[1] in icons) { return _alert(defaults.title, arguments[0], arguments[1]); }
            return _alert.apply(this, arguments);
        }
        if (arguments.length == 3) {
            if ($.isFunction(arguments[2])) {
                return (arguments[1] in icons) ? _alert(defaults.title, arguments[0], arguments[1], arguments[2])
                    : _alert(arguments[0], arguments[1], defaults.icon, arguments[2]);
            }
            return _alert.apply(this, arguments);
        }
        return _alert.apply(this, arguments);
    };

    //  重写 $.messager.confirm 方法，使其支持如下的多种重载方式：
    //      function (message)
    //      function (callback)
    //      function (message, callback)
    //      function (title, message)
    //  返回值：返回弹出的消息框 easyui-window 对象
    $.messager.confirm = function (title, msg, fn) {
        if (arguments.length == 1) {
            return $.isFunction(arguments[0]) ? _confirm(defaults.title, defaults.confirm, arguments[0]) : _confirm(defaults.title, arguments[0]);
        }
        if (arguments.length == 2) {
            return $.isFunction(arguments[1]) ? _confirm(defaults.title, arguments[0], arguments[1]) : _confirm(arguments[0], arguments[1]);
        }
        return _confirm.apply(this, arguments);
    };

    //  增加 $.messager.solicit 方法，该方法弹出一个包含三个按钮("是"、"否" 和 "取消")的对话框，点击任意按钮或者关闭对话框时，执行指定的回调函数；
    //      该函数提供如下重载方式：
    //      function (param: object)
    //      function (callback: function)
    //      function (message: string, callback: function)
    //      function (title: string, message: string, callback: function)
    //  返回值：返回弹出的消息框 easyui-window 对象
    $.messager.solicit = function (title, msg, fn) {
        var args = arguments, type = $.type(args[0]),
            opts = $.extend({}, $.messager.solicit.defaults,
                type == "object" ? args[0] : (
                    type == "function" ? { callback: args[0] } : (
                        args.length == 2 ? { message: args[0], callback: args[1] } : { title: args[0], message: args[1], callback: args[2] }
                    )
                )
            ),
            ret = $.messager.confirm(opts.title, opts.message, opts.callback),
            options = ret.window("options"), onClose = options.onClose;
        options.onClose = function () {
            if ($.isFunction(onClose)) { onClose.apply(this, arguments); }
            if ($.isFunction(opts.callback)) { opts.callback.call(this, undefined); }
        };
        var buttons = ret.find(">div.messager-button").empty();
        $("<a class=\"messager-solicit messager-solicit-yes\"></a>").appendTo(buttons).linkbutton({
            text: opts.yesText,
            onClick: function () {
                options.onClose = onClose; ret.window("close");
                if ($.isFunction(opts.callback)) { opts.callback.call(this, true); }
            }
        });
        $("<a class=\"messager-solicit messager-solicit-no\"></a>").appendTo(buttons).linkbutton({
            text: opts.noText,
            onClick: function () {
                options.onClose = onClose; ret.window("close");
                if ($.isFunction(opts.callback)) { opts.callback.call(this, false); }
            }
        });
        $("<a class=\"messager-solicit messager-solicit-cancel\"></a>").appendTo(buttons).linkbutton({
            text: opts.cancelText,
            onClick: function () {
                options.onClose = onClose; ret.window("close");
                if ($.isFunction(opts.callback)) { opts.callback.call(this, undefined); }
            }
        });
        return ret;
    };

    $.messager.solicit.defaults = { title: "操作提醒", message: null, callback: null, yesText: "是", noText: "否", cancelText: "取消" };


    //  重写 $.messager.prompt 方法，使其支持如下的多种重载方式：
    //      function (callback)
    //      function (message, callback)
    //      function (title, message)
    //      function (title, message, callback)
    //  返回值：返回弹出的消息框 easyui-window 对象
    $.messager.prompt = function (title, msg, fn) {
        if (arguments.length == 1) {
            return $.isFunction(arguments[0]) ? _prompt(defaults.title, defaults.prompt, arguments[0]) : _prompt(defaults.title, defaults.prompt);
        }
        if (arguments.length == 2) {
            return $.isFunction(arguments[1]) ? _prompt(defaults.title, arguments[0], arguments[1]) : _prompt(arguments[0], arguments[1]);
        }
        return _prompt.apply(this, arguments);
    };




    //  显示类似于 easyui-datagrid 在加载远程数据时显示的 mask 状态层；该函数定义如下重载方式：
    //      function ()
    //      function (options)，其中 options 为一个格式为 { msg, locale, topMost } 的 JSON-Object；
    //  上述参数中：
    //      msg 表示加载显示的消息文本内容，默认为 "正在加载，请稍等..."；
    //      locale 表示加载的区域，可以是一个 jQuery 对象选择器字符串，也可以是一个 jQuery 对象或者 HTML-DOM 对象；默认为字符串 "body"。
    //      topMost 为一个布尔类型参数，默认为 false，表示是否在顶级页面加载此 mask 状态层。
    //  返回值：返回表示弹出的数据加载框和层的 jQuery 对象。
    $.easyui.loading = function (options) {
        var opts = $.extend({ msg: defaults.loading, locale: "body", topMost: false }, options || {}),
            jq = opts.topMost ? $.util.$ : $,
            locale = jq(opts.locale),
            array = locale.children().map(function () {
                var zindex = $(this).css("z-index");
                return $.isNumeric(zindex) ? parseInt(zindex) : 0;
            }),
            zindex = $.array.max(array.length ? array : [1]);
        if (!locale.is("body")) {
            locale.addClass("mask-container");
        }
        var mask = jq("<div></div>").addClass("datagrid-mask").css({ display: "block", "z-index": zindex += 100 }).appendTo(locale);
        var msg = jq("<div></div>").addClass("datagrid-mask-msg").css({ display: "block", left: "50%", "z-index": ++zindex }).html(opts.msg).appendTo(locale);
        msg.css("marginLeft", -msg.outerWidth() / 2);
        return mask.add(msg);
    };

    //  关闭由 $.easyui.loading 方法显示的 "正在加载..." 状态层；该函数定义如下重载方式：
    //      function ()
    //      function (locale)
    //      function (locale, topMost)
    //      function (topMost, locale)
    //      function (options)，其中 options 为一个格式为 { locale, topMost } 的 JSON-Object
    $.easyui.loaded = function (locale, topMost) {
        var opts = { locale: "body", topMost: false };
        if (arguments.length == 1) {
            if ($.isPlainObject(arguments[0])) {
                $.extend(opts, arguments[0]);
            } else if ($.util.isBoolean(arguments[0])) {
                opts.topMost = arguments[0];
            } else {
                opts.locale = arguments[0];
            }
        }
        if (arguments.length == 2) {
            if ($.util.isBoolean(arguments[0])) {
                $.extend(opts, { locale: arguments[1], topMost: arguments[0] });
            } else {
                $.extend(opts, { locale: arguments[0], topMost: arguments[1] });
            }
        }
        var jq = opts.topMost ? $.util.$ : $, locale = jq(opts.locale);
        locale.removeClass("mask-container");
        locale.children("div.datagrid-mask-msg,div.datagrid-mask").remove();
    };


    //  备注： $.messager 表示当前页面的 easyui-messager 对象；
    //         $.easyui.messager 表示可控顶级页面的 easyui-messager 对象；


    //  更改 jQuery EasyUI 中部分控件的国际化语言显示。
    $.extend($.fn.panel.defaults, { loadingMessage: defaults.loading });
    $.extend($.fn.window.defaults, { loadingMessage: defaults.loading });
    $.extend($.fn.dialog.defaults, { loadingMessage: defaults.loading });

    //  更改 jeasyui-combo 组件的非空验证提醒消息语言。
    $.extend($.fn.combo.defaults, { missingMessage: $.fn.validatebox.defaults.missingMessage });



    //  基于当前页面 document 触发，当前页面嵌套的所有子级和父级页面均执行一个签名为 function (win, e) 事件触发函数；该方法提供如下参数：
    //      eventName:
    //      eventNamespace:
    //      plugin:
    //      callback: 一个签名为 function (win, e) 的函数，其中 win 表示所在 iframe 执行函数传入的 window 对象，e 表示最初触发该循环函数调用的事件对象。
    $.easyui.bindPageNestedFunc = function (eventName, eventNamespace, plugin, callback) {
        if (arguments.length == 3) { callback = plugin; plugin = "jquery"; }
        if (arguments.length == 4 && !plugin) { plugin = "jquery"; }
        $(document).unbind("." + eventNamespace).bind(eventName + "." + eventNamespace, function (e) {
            var doCall = function (win) { callback.call(win, win, e); },
                doCallUp = function (win) {
                    var p = win.parent;
                    try {
                        if (win != p && p.jQuery && p.jQuery.parser && p.jQuery.parser.plugins && p.jQuery.fn && p.jQuery.fn[plugin]) {
                            doCall(p);
                            doCallUp(p);
                        }
                    } catch (ex) { }
                },
                doCallDown = function (win) {
                    var jq = win.jQuery;
                    jq("iframe,iframe").each(function () {
                        try {
                            if (this.contentWindow && jq.util.isObject(this.contentWindow.document) && this.contentWindow.jQuery && this.contentWindow.jQuery.parser && this.contentWindow.jQuery.parser.plugins && this.contentWindow.jQuery.fn && this.contentWindow.jQuery.fn[plugin]) {
                                doCall(this.contentWindow);
                                doCallDown(this.contentWindow);
                            }
                        } catch (ex) { }
                    });
                },
                doCallAll = function (win) {
                    doCall(win);
                    doCallUp(win);
                    doCallDown(win);
                };
            doCallAll(window);
        });
    };



    //  获取或更改 jQuery EasyUI 部分组件的通用错误提示函数；该方法定义如下重载方式：
    //      function():         获取 jQuery EasyUI 部分组件的通用错误提示函数；
    //      function(callback): 更改 jQuery EasyUI 部分组件的通用错误提示函数；
    //  备注：该方法会设置如下组件的 onLoadError 事件；
    //          easyui-form
    //          easyui-combobox
    //          easyui-combotree
    //          easyui-combogrid
    //          easyui-datagrid
    //          easyui-propertygrid
    //          easyui-tree
    //          easyui-treegrid
    //      同时还会设置 jQuery-ajax 的通用错误事件 error。
    $.easyui.ajaxError = function (callback) {
        if (!arguments.length) { return $.fn.form.defaults.onLoadError; }
        $.fn.form.defaults.onLoadError = callback;
        $.fn.panel.defaults.onLoadError = callback;
        $.fn.combobox.defaults.onLoadError = callback;
        $.fn.combotree.defaults.onLoadError = callback;
        $.fn.combogrid.defaults.onLoadError = callback;
        $.fn.datagrid.defaults.onLoadError = callback;
        $.fn.propertygrid.defaults.onLoadError = callback;
        $.fn.tree.defaults.onLoadError = callback;
        $.fn.treegrid.defaults.onLoadError = callback;
        $.ajaxSetup({ error: callback });
    };

    var onLoadError = function (XMLHttpRequest, textStatus, errorThrown) {
        $.messager.progress("close");
        if ($.easyui.messager != $.messager) { $.easyui.messager.progress("close"); }
        var msg = (XMLHttpRequest && !$.string.isNullOrWhiteSpace(XMLHttpRequest.responseText) ?
                "如果该问题重复出现，请联系您的系统管理员并反馈该故障。<br />" +
                "错误号：" + XMLHttpRequest.status + "(" + XMLHttpRequest.statusText + ")；<hr />" + XMLHttpRequest.responseText :
                "数据库连接异常，请检查参数配置信息。");
        //系统出现了一个未指明的错误，如果该问题重复出现，请联系您的系统管理员并反馈该故障。

        var win = $.easyui.messager.alert("错误提醒", msg, "error"),
            opts = win.window("options"), panel = win.window("panel"), width = panel.outerWidth(), height = panel.outerHeight();
        if (width > 800 || height > 800) { win.window("resize", { width: width > 800 ? 800 : width, height: height > 800 ? 800 : height }); }
        win.window("center");
    };



    //  备份 jquery ajax 方法的默认参数。
    $.easyui.ajaxDefaults = $.extend({}, $.ajaxSettings);

    //  更改 jQuery EasyUI 部分组件的通用错误提示。
    $.easyui.ajaxError(onLoadError);

    //  更改 jQuery.ajax 函数的部分默认属性。
    $.ajaxSetup({
        dataFilter: function (data, type) {
            return String(type).toLowerCase(type) == "json" ? $.string.toJSONString(data) : data;
        }
        //,beforeSend: function (XMLHttpRequest) {
        //    $.easyui.loading({ msg: "正在将请求数据发送至服务器..." });
        //}
        //,complete: function (XMLHttpRequest, textStatus) {
        //    $.easyui.loaded();
        //}
    });





    $.extend({

        //  判断当前 jQuery 对象是否是指定名称的已经初始化好的 easyui 插件；该方法定义如下参数：
        //      selector:   jQuery 对象选择器，或者 DOM 对象，或者 jQuery 对象均可；
        //      pluginName：要判断的插件名称，例如 "panel"、"dialog"、"datagrid" 等；
        //  返回值：如果 selector 所表示的 jQuery 对象中的第一个 DOM 元素为 pluginName 参数所示的 easyui 插件且已经被初始化，则返回 true，否则返回 false。
        isEasyUI: function (selector, pluginName) {
            if (!$.array.contains($.parser.plugins, pluginName)) { $.error($.string.format("传入的参数 pluginName: {0} 不是 easyui 插件名。")); }
            var t = $(selector);
            if (!t.length) { return false; }
            var state = $.data(t[0], pluginName);
            return state && state.options ? true : false;
        }
    });


    $.fn.extend({

        //  判断当前 jQuery 对象是否是指定名称的已经初始化好的 easyui 插件；该方法定义如下参数：
        //      pluginName：要判断的插件名称，例如 "panel"、"dialog"、"datagrid" 等；
        //  返回值：如果当前 jQuery 对象中的第一个 DOM 元素为 pluginName 参数所示的 easyui 插件且已经被初始化，则返回 true，否则返回 false。
        isEasyUI: function (pluginName) {
            return $.isEasyUI(this, pluginName);
        },


        currentPagination: function () {
            var p = this.closest(".pagination");
            while (p.length && !$.data(p[0], "pagination")) { p = p.parent().closest(".pagination"); }
            return p;
        },

        currentProgressbar: function () {
            var p = this.closest(".progressbar");
            while (p.length && !$.data(p[0], "progressbar")) { p = p.parent().closest(".progressbar"); }
            return p;
        },

        currentPanel: function () {
            var p = this.closest(".panel-body");
            while (p.length && !$.data(p[0], "panel")) { p = p.parent().closest(".panel-body"); }
            return p;
        },

        currentTabPanel: function () {
            var p = this.closest(".panel-body"), panel = p.parent(), panels = panel.parent(), container = panels.parent();
            while (p.length && !($.data(p[0], "panel") && panel.hasClass("panel") && panels.hasClass("tabs-panels") && container.hasClass("tabs-container"))) {
                p = p.parent().closest(".panel-body");
                panel = p.parent();
                panels = panel.parent();
                container = panels.parent();
            }
            return p;
        },

        currentTabIndex: function () {
            var panel = this.currentTabPanel();
            return panel.length ? panel.panel("panel").index() : -1;
        },

        currentTabs: function () {
            var p = this.closest(".tabs-container");
            while (p.length && !$.data(p[0], "tabs")) { p = p.parent().closest(".tabs-container"); }
            return p;
        },

        currentAccordion: function () {
            var p = this.closest(".accordion");
            while (p.length && !$.data(p[0], "accordion")) { p = p.parent().closest(".accordion"); }
            return p;
        },

        currentAccPanel: function () {
            var p = this.closest(".panel-body"), panel = p.parent(), container = panels.parent();
            while (p.length && !($.data(p[0], "panel") && panel.hasClass("panel") && container.hasClass("accordion") && $.data(container[0], "accordion"))) {
                p = p.parent().closest(".panel-body");
                panel = p.parent();
                container = panels.parent();
            }
            return p;
        },

        currentLayout: function () {
            var layout = this.closest(".layout");
            while (layout.length && !$.data(layout[0], "layout")) { layout = layout.closest(".layout"); }
            return layout;
        },

        currentRegion: function () {
            var p = this.closest(".panel.layout-panel"), layout = p.parent(), body = p.children(".panel-body");
            while (p.length && !(layout.hasClass("layout") && $.data(body[0], "panel"))) {
                p = p.parent().closest(".panel.layout-panel");
                layout = p.parent();
                body = p.children(".panel-body");
            }
            return body;
        },

        currentLinkbutton: function () {
            var btn = this.closest(".l-btn");
            while (btn.length && !$.data(btn[0], "linkbutton")) { btn = btn.parent().closest(".layout"); }
            return btn;
        },

        currentCalendar: function () {
            var c = this.closest(".calendar");
            while (c.length && !$.data(c[0], "calendar")) { c = c.parent().closest(".calendar"); }
            return c;
        },

        currentWindow: function () {
            var p = this.closest(".panel-body.window-body");
            while (p.length && !$.data(p[0], "window")) { p = p.parent().closest(".panel-body.window-body"); }
            return p;
        },

        currentDialog: function () {
            var p = this.closest(".panel-body.window-body");
            while (p.length && !$.data(p[0], "dialog")) { p = p.parent().closest(".panel-body.window-body"); }
            return p;
        },

        currentDatagrid: function () {
            var p = this.closest(".datagrid-wrap.panel-body"), dg = p.find(">.datagrid-view>:eq(2)");
            while (p.length && !$.data(dg[0], "datagrid")) {
                p = p.parent().closest(".datagrid-wrap.panel-body");
                dg = p.find(">.datagrid-view>:eq(2)");
            }
            return dg;
        },

        currentPropertygrid: function () {
            var p = this.closest(".datagrid-wrap.panel-body"), pg = p.find(">.datagrid-view>:eq(2)");
            while (p.length && !$.data(pg[0], "propertygrid")) {
                p = p.parent().closest(".datagrid-wrap.panel-body");
                pg = p.find(">.datagrid-view>:eq(2)");
            }
            return pg;
        },

        currentTree: function () {
            var t = this.closest(".tree");
            while (t.length && !$.data(t[0], "tree")) { t = t.parent().closest(".tree"); }
            return t;
        },

        currentTreegrid: function () {
            var p = this.closest(".datagrid-wrap.panel-body"), tg = p.find(">.datagrid-view>:eq(2)");
            while (p.length && !$.data(tg[0], "treegrid")) {
                p = p.parent().closest(".datagrid-wrap.panel-body");
                tg = p.find(">.datagrid-view>:eq(2)");
            }
            return tg;
        }
    });

})(jQuery);