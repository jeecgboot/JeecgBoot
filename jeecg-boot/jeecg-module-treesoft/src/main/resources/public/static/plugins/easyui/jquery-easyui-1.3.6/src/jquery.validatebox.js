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
        $(_2).addClass("validatebox-text");
    };
    function _3(_4) {
        var _5 = $.data(_4, "validatebox");
        _5.validating = false;
        if (_5.timer) {
            clearTimeout(_5.timer);
        }
        $(_4).tooltip("destroy");
        $(_4).unbind();
        $(_4).remove();
    };
    function _6(_7) {
        var _8 = $(_7);
        var _9 = $.data(_7, "validatebox");
        _8.unbind(".validatebox");
        if (_9.options.novalidate) {
            return;
        }
        _8.bind("focus.validatebox", function () {
            _9.validating = true;
            _9.value = undefined;
            (function () {
                if (_9.validating) {
                    if (_9.value != _8.val()) {
                        _9.value = _8.val();
                        if (_9.timer) {
                            clearTimeout(_9.timer);
                        }
                        _9.timer = setTimeout(function () {
                            $(_7).validatebox("validate");
                        }, _9.options.delay);
                    } else {
                        _f(_7);
                    }
                    setTimeout(arguments.callee, 200);
                }
            })();
        }).bind("blur.validatebox", function () {
            if (_9.timer) {
                clearTimeout(_9.timer);
                _9.timer = undefined;
            }
            _9.validating = false;
            _a(_7);
        }).bind("mouseenter.validatebox", function () {
            if (_8.hasClass("validatebox-invalid")) {
                _b(_7);
            }
        }).bind("mouseleave.validatebox", function () {
            if (!_9.validating) {
                _a(_7);
            }
        });
    };
    function _b(_c) {
        var _d = $.data(_c, "validatebox");
        var _e = _d.options;
        $(_c).tooltip($.extend({}, _e.tipOptions, { content: _d.message, position: _e.tipPosition, deltaX: _e.deltaX })).tooltip("show");
        _d.tip = true;
    };
    function _f(_10) {
        var _11 = $.data(_10, "validatebox");
        if (_11 && _11.tip) {
            $(_10).tooltip("reposition");
        }
    };
    function _a(_12) {
        var _13 = $.data(_12, "validatebox");
        _13.tip = false;
        $(_12).tooltip("hide");
    };
    function _14(_15) {
        var _16 = $.data(_15, "validatebox");
        var _17 = _16.options;
        var box = $(_15);
        var _18 = box.val();
        function _19(msg) {
            _16.message = msg;
        };
        function _1a(_1b, _1c) {
            var _1d = /([a-zA-Z_]+)(.*)/.exec(_1b);
            var _1e = _17.rules[_1d[1]];
            if (_1e && _18) {
                var _1f = _1c || _17.validParams || eval(_1d[2]);
                if (!_1e["validator"].call(_15, _18, _1f)) {
                    box.addClass("validatebox-invalid");
                    var _20 = _1e["message"];
                    if (_1f) {
                        for (var i = 0; i < _1f.length; i++) {
                            _20 = _20.replace(new RegExp("\\{" + i + "\\}", "g"), _1f[i]);
                        }
                    }
                    _19(_17.invalidMessage || _20);
                    if (_16.validating) {
                        _b(_15);
                    }
                    return false;
                }
            }
            return true;
        };
        box.removeClass("validatebox-invalid");
        _a(_15);
        if (_17.novalidate || box.is(":disabled")) {
            return true;
        }
        if (_17.required) {
            if (_18 == "") {
                box.addClass("validatebox-invalid");
                _19(_17.missingMessage);
                if (_16.validating) {
                    _b(_15);
                }
                return false;
            }
        }
        if (_17.validType) {
            if ($.isArray(_17.validType)) {
                for (var i = 0; i < _17.validType.length; i++) {
                    if (!_1a(_17.validType[i])) {
                        return false;
                    }
                }
            } else {
                if (typeof _17.validType == "string") {
                    if (!_1a(_17.validType)) {
                        return false;
                    }
                } else {
                    for (var _21 in _17.validType) {
                        var _22 = _17.validType[_21];
                        if (!_1a(_21, _22)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    };
    function _23(_24, _25) {
        var _26 = $.data(_24, "validatebox").options;
        if (_25 != undefined) {
            _26.novalidate = _25;
        }
        if (_26.novalidate) {
            $(_24).removeClass("validatebox-invalid");
            _a(_24);
        }
        _6(_24);
    };
    $.fn.validatebox = function (_27, _28) {
        if (typeof _27 == "string") {
            return $.fn.validatebox.methods[_27](this, _28);
        }
        _27 = _27 || {};
        return this.each(function () {
            var _29 = $.data(this, "validatebox");
            if (_29) {
                $.extend(_29.options, _27);
            } else {
                _1(this);
                $.data(this, "validatebox", { options: $.extend({}, $.fn.validatebox.defaults, $.fn.validatebox.parseOptions(this), _27) });
            }
            _23(this);
            _14(this);
        });
    };
    $.fn.validatebox.methods = {
        options: function (jq) {
            return $.data(jq[0], "validatebox").options;
        }, destroy: function (jq) {
            return jq.each(function () {
                _3(this);
            });
        }, validate: function (jq) {
            return jq.each(function () {
                _14(this);
            });
        }, isValid: function (jq) {
            return _14(jq[0]);
        }, enableValidation: function (jq) {
            return jq.each(function () {
                _23(this, false);
            });
        }, disableValidation: function (jq) {
            return jq.each(function () {
                _23(this, true);
            });
        }
    };
    $.fn.validatebox.parseOptions = function (_2a) {
        var t = $(_2a);
        return $.extend({}, $.parser.parseOptions(_2a, [
            "validType", "missingMessage", "invalidMessage", "tipPosition",
            { delay: "number", deltaX: "number" }
        ]),
        {
            required: (t.attr("required") ? true : undefined),
            novalidate: (t.attr("novalidate") != undefined ? true : undefined)
        });
    };
    $.fn.validatebox.defaults = {
        required: false,
        validType: null,
        validParams: null,
        delay: 200,
        missingMessage: "This field is required.",
        invalidMessage: null,
        tipPosition: "right",
        deltaX: 0,
        novalidate: false,
        tipOptions: {
            showEvent: "none",
            hideEvent: "none",
            showDelay: 0,
            hideDelay: 0,
            zIndex: "",
            onShow: function () {
                $(this).tooltip("tip").css({
                    color: "#000", borderColor: "#CC9933", backgroundColor: "#FFFFCC"
                });
            }, onHide: function () {
                $(this).tooltip("destroy");
            }
        }, rules: {
            email: {
                validator: function (_2b) {
                    return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(_2b);
                }, message: "Please enter a valid email address."
            }, url: {
                validator: function (_2c) {
                    return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(_2c);
                }, message: "Please enter a valid URL."
            }, length: {
                validator: function (_2d, _2e) {
                    var len = $.trim(_2d).length;
                    return len >= _2e[0] && len <= _2e[1];
                }, message: "Please enter a value between {0} and {1}."
            }, remote: {
                validator: function (_2f, _30) {
                    var _31 = {};
                    _31[_30[1]] = _2f;
                    var _32 = $.ajax({ url: _30[0], dataType: "json", data: _31, async: false, cache: false, type: "post" }).responseText;
                    return _32 == "true";
                }, message: "Please fix this field."
            }
        }
    };
})(jQuery);

