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
        var _3 = $("<span class=\"spinner\">" + "<span class=\"spinner-arrow\">" + "<span class=\"spinner-arrow-up\"></span>" + "<span class=\"spinner-arrow-down\"></span>" + "</span>" + "</span>").insertAfter(_2);
        $(_2).addClass("spinner-text spinner-f").prependTo(_3);
        return _3;
    };
    function _4(_5, _6) {
        var _7 = $.data(_5, "spinner").options;
        var _8 = $.data(_5, "spinner").spinner;
        if (_6) {
            _7.width = _6;
        }
        var _9 = $("<div style=\"display:none\"></div>").insertBefore(_8);
        _8.appendTo("body");
        if (isNaN(_7.width)) {
            _7.width = $(_5).outerWidth();
        }
        var _a = _8.find(".spinner-arrow");
        _8._outerWidth(_7.width)._outerHeight(_7.height);
        $(_5)._outerWidth(_8.width() - _a.outerWidth());
        $(_5).css({ height: _8.height() + "px", lineHeight: _8.height() + "px" });
        _a._outerHeight(_8.height());
        _a.find("span")._outerHeight(_a.height() / 2);
        _8.insertAfter(_9);
        _9.remove();
    };
    function _b(_c) {
        var _d = $.data(_c, "spinner").options;
        var _e = $.data(_c, "spinner").spinner;
        $(_c).unbind(".spinner");
        _e.find(".spinner-arrow-up,.spinner-arrow-down").unbind(".spinner");
        if (!_d.disabled && !_d.readonly) {
            _e.find(".spinner-arrow-up").bind("mouseenter.spinner", function () {
                $(this).addClass("spinner-arrow-hover");
            }).bind("mouseleave.spinner", function () {
                $(this).removeClass("spinner-arrow-hover");
            }).bind("click.spinner", function () {
                _d.spin.call(_c, false);
                _d.onSpinUp.call(_c);
                $(_c).validatebox("validate");
            });
            _e.find(".spinner-arrow-down").bind("mouseenter.spinner", function () {
                $(this).addClass("spinner-arrow-hover");
            }).bind("mouseleave.spinner", function () {
                $(this).removeClass("spinner-arrow-hover");
            }).bind("click.spinner", function () {
                _d.spin.call(_c, true);
                _d.onSpinDown.call(_c);
                $(_c).validatebox("validate");
            });
            $(_c).bind("change.spinner", function () {
                $(this).spinner("setValue", $(this).val());
            });
        }
    };
    function _f(_10, _11) {
        var _12 = $.data(_10, "spinner").options;
        if (_11) {
            _12.disabled = true;
            $(_10).attr("disabled", true);
        } else {
            _12.disabled = false;
            $(_10).removeAttr("disabled");
        }
    };
    function _13(_14, _15) {
        var _16 = $.data(_14, "spinner");
        var _17 = _16.options;
        _17.readonly = _15 == undefined ? true : _15;
        var _18 = _17.readonly ? true : (!_17.editable);
        $(_14).attr("readonly", _18).css("cursor", _18 ? "pointer" : "");
    };
    $.fn.spinner = function (_19, _1a) {
        if (typeof _19 == "string") {
            var _1b = $.fn.spinner.methods[_19];
            if (_1b) {
                return _1b(this, _1a);
            } else {
                return this.validatebox(_19, _1a);
            }
        }
        _19 = _19 || {};
        return this.each(function () {
            var _1c = $.data(this, "spinner");
            if (_1c) {
                $.extend(_1c.options, _19);
            } else {
                _1c = $.data(this, "spinner", { options: $.extend({}, $.fn.spinner.defaults, $.fn.spinner.parseOptions(this), _19), spinner: _1(this) });
                $(this).removeAttr("disabled");
            }
            _1c.options.originalValue = _1c.options.value;
            $(this).val(_1c.options.value);
            _f(this, _1c.options.disabled);
            _13(this, _1c.options.readonly);
            _4(this);
            $(this).validatebox(_1c.options);
            _b(this);
        });
    };
    $.fn.spinner.methods = {
        options: function (jq) {
            var _1d = $.data(jq[0], "spinner").options;
            return $.extend(_1d, { value: jq.val() });
        }, destroy: function (jq) {
            return jq.each(function () {
                var _1e = $.data(this, "spinner").spinner;
                $(this).validatebox("destroy");
                _1e.remove();
            });
        }, resize: function (jq, _1f) {
            return jq.each(function () {
                _4(this, _1f);
            });
        }, enable: function (jq) {
            return jq.each(function () {
                _f(this, false);
                _b(this);
            });
        }, disable: function (jq) {
            return jq.each(function () {
                _f(this, true);
                _b(this);
            });
        }, readonly: function (jq, _20) {
            return jq.each(function () {
                _13(this, _20);
                _b(this);
            });
        }, getValue: function (jq) {
            return jq.val();
        }, setValue: function (jq, _21) {
            return jq.each(function () {
                var _22 = $.data(this, "spinner").options;
                var _23 = _22.value;
                _22.value = _21;
                $(this).val(_21);
                if (_23 != _21) {
                    _22.onChange.call(this, _21, _23);
                }
            });
        }, clear: function (jq) {
            return jq.each(function () {
                var _24 = $.data(this, "spinner").options;
                _24.value = "";
                $(this).val("");
            });
        }, reset: function (jq) {
            return jq.each(function () {
                var _25 = $(this).spinner("options");
                $(this).spinner("setValue", _25.originalValue);
            });
        }
    };
    $.fn.spinner.parseOptions = function (_26) {
        var t = $(_26);
        return $.extend({}, $.fn.validatebox.parseOptions(_26), $.parser.parseOptions(_26, ["width", "height", "min", "max", { increment: "number", editable: "boolean" }]), { value: (t.val() || undefined), disabled: (t.attr("disabled") ? true : undefined), readonly: (t.attr("readonly") ? true : undefined) });
    };
    $.fn.spinner.defaults = $.extend({}, $.fn.validatebox.defaults, {
        width: "auto", height: 22, deltaX: 19, value: "", min: null, max: null, increment: 1, editable: true, disabled: false, readonly: false, spin: function (_27) {
        }, onSpinUp: function () {
        }, onSpinDown: function () {
        }, onChange: function (_28, _29) {
        }
    });
})(jQuery);

