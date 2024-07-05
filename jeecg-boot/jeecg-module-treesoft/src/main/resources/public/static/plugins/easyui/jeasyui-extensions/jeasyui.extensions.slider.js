/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI slider Extensions 1.0 beta
* jQuery EasyUI slider 组件扩展
* jeasyui.extensions.slider.js

*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.slider.extensions = {};


    function initialize(target) {
        var t = $(target), state = $.data(target, "slider"), opts = state.options;
        if (!opts._initialized) {
            buildSliderRepair(target);
            opts._initialized = true;
        }
    };

    function buildSliderRepair(target) {
        var t = $(target), state = $.data(target, "slider"), opts = state.options, slider = state.slider;

        slider.find('div.slider-inner').unbind('.slider').bind('mousedown.slider', function (e) {
            if (state.isDragging || opts.disabled) { return; }
            var pos = $(this).offset();
            var value = pos2value(target, (opts.mode == 'h' ? (e.pageX - pos.left) : (e.pageY - pos.top)));
            adjustValue(value);
            opts.onComplete.call(target, opts.value);
        });

        function adjustValue(value) {
            var s = Math.abs(value % opts.step);
            if (s < opts.step / 2) {
                value -= s;
            } else {
                value = value - s + opts.step;
            }
            t.slider("setValue", value);
            //setValue(target, value);
        }

        function pos2value(target, pos) {
            var size = opts.mode == 'h' ? slider.width() : slider.height(),
                value = opts.converter.toValue.call(target, opts.mode == 'h' ? (opts.reversed ? (size - pos) : pos) : (size - pos), size);
            return value.toFixed(0);
        }
    };


    var _enable = $.fn.slider.methods.enable;
    function enable(target) {
        var t = $(target);
        _enable.call(t, t);
        buildSliderRepair(target);
    };

    var _disable = $.fn.slider.methods.disable;
    function disable(target) {
        var t = $(target);
        _disable.call(t, t);
        buildSliderRepair(target);
    };



    var _slider = $.fn.slider;
    $.fn.slider = function (options, param) {
        if (typeof options == "string") {
            return _slider.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "slider") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.slider.parseOptions(this), options);
            _slider.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.slider, _slider);


    var methods = $.fn.slider.extensions.methods = {

        enable: function (jq) { return jq.each(function () { enable(this); }); },

        disable: function (jq) { return jq.each(function () { disable(this); }); }
    };
    var defaults = $.fn.slider.extensions.defaults = {
    };

    $.extend($.fn.slider.defaults, defaults);
    $.extend($.fn.slider.methods, methods);

    //if ($.fn.form && $.isArray($.fn.form.otherList)) {
    //    $.fn.form.otherList.push("slider");
    //    //$.array.insert($.fn.form.otherList, 0, "searchbox");
    //}


    $.extend($.fn.datagrid.defaults.editors, {
        slider: {
            init: function (container, options) {
                return $("<div class=\"datagrid-editable-slider\"></div>").appendTo(container).slider(options);;
            },
            destroy: function (target) {
                $(target).slider("destroy");
            },
            getValue: function (target) {
                return $(target).slider("getValue");
            },
            setValue: function (target, value) {
                $(target).slider("setValue", value);
            },
            resize: function (target, width) {
                $(target).slider("resize", { width: width });
            }
        }
    });


})(jQuery);