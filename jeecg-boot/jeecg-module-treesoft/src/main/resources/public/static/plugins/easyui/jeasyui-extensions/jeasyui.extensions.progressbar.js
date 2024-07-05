/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI progressbar Extensions 1.0 beta
* jQuery EasyUI progressbar 组件扩展
* jeasyui.extensions.progressbar.js

*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.progressbar.extensions = {};



    function setText(target, text) {
        var t = $(target), opts = t.progressbar("options");
        t.find(".progressbar-text").text(opts.text = text);
    };



    var defaults = $.fn.progressbar.extensions.defaults = {};

    var methods = $.fn.progressbar.extensions.methods = {

        //  扩展 easyui-progressbar 的自定义扩展方法；设置当前 easyui-progressbar 控件的 text 值；该方法的参数 text 表示将被设置的 text 值；
        //  返回值：返回表示当前 easyui-progressbar 的 jQuery 链式对象。
        setText: function (jq, text) { return jq.each(function () { setText(this, text); }); }
    };


    $.extend($.fn.progressbar.defaults, defaults);
    $.extend($.fn.progressbar.methods, methods);

})(jQuery);