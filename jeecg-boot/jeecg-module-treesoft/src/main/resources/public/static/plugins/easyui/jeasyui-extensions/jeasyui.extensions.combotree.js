/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combotree Extensions 1.0 beta
* jQuery EasyUI combotree 组件扩展
* jeasyui.extensions.combotree.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   3、jeasyui.extensions.combo.js v1.0 beta late
*   5、jeasyui.extensions.tree.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.combotree.extensions = {};


    var methods = $.fn.combotree.extensions.methods = {};
    var defaults = $.fn.combotree.extensions.defaults = $.extend({}, $.fn.tree.extensions.defaults, {

        //  更改继承于 easyui-tree 的自定义属性 toggleOnClick 的默认值，使得 easyui-combotree 中 tree 组件的页节点在点击后不自动展开/折叠子节点；
        //  Boolean 类型，默认为 false。
        toggleOnClick: false,

        //  更改继承于 easyui-tree 的自定义属性 autoBindDblClick 的默认值，使得 easyui-combotree 中 tree 组件的页节点在双击后不触发第一个右键菜单项的事件；
        //  Boolean 类型，默认为 false。
        autoBindDblClick: false,

        onExpand: function () {
            $.fn.tree.extensions.defaults.onExpand.apply($(this).combotree("tree")[0], arguments);
        }
    });

    $.extend($.fn.combotree.defaults, defaults);
    $.extend($.fn.combotree.methods, methods);

})(jQuery);