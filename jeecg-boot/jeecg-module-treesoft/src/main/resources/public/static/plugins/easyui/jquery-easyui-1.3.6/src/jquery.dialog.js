/**
 * jQuery EasyUI 1.3.6
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
(function ($) {
    function _1(_2) {
        var cp = document.createElement("div");
        while (_2.firstChild) {
            cp.appendChild(_2.firstChild);
        }
        _2.appendChild(cp);
        var _3 = $(cp);
        _3.attr("style", $(_2).attr("style"));
        $(_2).removeAttr("style").css("overflow", "hidden");
        _3.panel({ border: false, doSize: false, bodyCls: "dialog-content" });
        return _3;
    };
    function _4(_5) {
        var _6 = $.data(_5, "dialog").options;
        var _7 = $.data(_5, "dialog").contentPanel;
        if (_6.toolbar) {
            if ($.isArray(_6.toolbar)) {
                $(_5).find("div.dialog-toolbar").remove();
                var _8 = $("<div class=\"dialog-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>").prependTo(_5);
                var tr = _8.find("tr");
                for (var i = 0; i < _6.toolbar.length; i++) {
                    var _9 = _6.toolbar[i];
                    if (_9 == "-") {
                        $("<td><div class=\"dialog-tool-separator\"></div></td>").appendTo(tr);
                    } else {
                        var td = $("<td></td>").appendTo(tr);
                        var _a = $("<a href=\"javascript:void(0)\"></a>").appendTo(td);
                        _a[0].onclick = eval(_9.handler || function () {
                        });
                        _a.linkbutton($.extend({}, _9, { plain: true }));
                    }
                }
            } else {
                $(_6.toolbar).addClass("dialog-toolbar").prependTo(_5);
                $(_6.toolbar).show();
            }
        } else {
            $(_5).find("div.dialog-toolbar").remove();
        }
        if (_6.buttons) {
            if ($.isArray(_6.buttons)) {
                $(_5).find("div.dialog-button").remove();
                var _b = $("<div class=\"dialog-button\"></div>").appendTo(_5);
                for (var i = 0; i < _6.buttons.length; i++) {
                    var p = _6.buttons[i];
                    var _c = $("<a href=\"javascript:void(0)\"></a>").appendTo(_b);
                    if (p.handler) {
                        _c[0].onclick = p.handler;
                    }
                    _c.linkbutton(p);
                }
            } else {
                $(_6.buttons).addClass("dialog-button").appendTo(_5);
                $(_6.buttons).show();
            }
        } else {
            $(_5).find("div.dialog-button").remove();
        }
        var _d = _6.href;
        var _e = _6.content;
        _6.href = null;
        _6.content = null;
        _7.panel({
            closed: _6.closed, cache: _6.cache, href: _d, content: _e, onLoad: function () {
                if (_6.height == "auto") {
                    $(_5).window("resize");
                }
                _6.onLoad.apply(_5, arguments);
            }
        });
        $(_5).window($.extend({}, _6, {
            onOpen: function () {
                if (_7.panel("options").closed) {
                    _7.panel("open");
                }
                if (_6.onOpen) {
                    _6.onOpen.call(_5);
                }
            }, onResize: function (_f, _10) {
                var _11 = $(_5);
                _7.panel("panel").show();
                _7.panel("resize", { width: _11.width(), height: (_10 == "auto") ? "auto" : _11.height() - _11.children("div.dialog-toolbar")._outerHeight() - _11.children("div.dialog-button")._outerHeight() });
                if (_6.onResize) {
                    _6.onResize.call(_5, _f, _10);
                }
            }
        }));
        _6.href = _d;
        _6.content = _e;
    };
    function _12(_13, _14) {
        var _15 = $.data(_13, "dialog").contentPanel;
        _15.panel("refresh", _14);
    };
    $.fn.dialog = function (_16, _17) {
        if (typeof _16 == "string") {
            var _18 = $.fn.dialog.methods[_16];
            if (_18) {
                return _18(this, _17);
            } else {
                return this.window(_16, _17);
            }
        }
        _16 = _16 || {};
        return this.each(function () {
            var _19 = $.data(this, "dialog");
            if (_19) {
                $.extend(_19.options, _16);
            } else {
                $.data(this, "dialog", { options: $.extend({}, $.fn.dialog.defaults, $.fn.dialog.parseOptions(this), _16), contentPanel: _1(this) });
            }
            _4(this);
        });
    };
    $.fn.dialog.methods = {
        options: function (jq) {
            var _1a = $.data(jq[0], "dialog").options;
            var _1b = jq.panel("options");
            $.extend(_1a, { closed: _1b.closed, collapsed: _1b.collapsed, minimized: _1b.minimized, maximized: _1b.maximized });
            var _1c = $.data(jq[0], "dialog").contentPanel;
            return _1a;
        }, dialog: function (jq) {
            return jq.window("window");
        }, refresh: function (jq, _1d) {
            return jq.each(function () {
                _12(this, _1d);
            });
        }
    };
    $.fn.dialog.parseOptions = function (_1e) {
        return $.extend({}, $.fn.window.parseOptions(_1e), $.parser.parseOptions(_1e, ["toolbar", "buttons"]));
    };
    $.fn.dialog.defaults = $.extend({}, $.fn.window.defaults, { title: "New Dialog", collapsible: false, minimizable: false, maximizable: false, resizable: false, toolbar: null, buttons: null });
})(jQuery);

