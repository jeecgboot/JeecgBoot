/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combogrid Extensions 1.0 beta
* jQuery EasyUI combogrid 组件扩展
* jeasyui.extensions.combogrid.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.combo.js v1.0 beta late
*   4、jeasyui.extensions.menu.js v1.0 beta late
*   5、jeasyui.extensions.datagrid.js v1.0 beta late
*   6、jeasyui.extensions.panel.js v1.0 beta late 和 jeasyui.extensions.window.js v1.0 beta late(可选)
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.combogrid.extensions = {};


    var methods = $.fn.combogrid.extensions.methods = {};
    var defaults = $.fn.combogrid.extensions.defaults = $.extend({}, $.fn.datagrid.extensions.defaults, {

        //  覆盖 easyui-combogrid 的事件 onLoadSuccess 以支持 easyui-datagrid 的自定义扩展功能；
        onLoadSuccess: function () {
            $.fn.datagrid.extensions.defaults.onLoadSuccess.apply($(this).combogrid("grid")[0], arguments);
        },

        //  覆盖 easyui-combogrid 的事件 onResizeColumn 以支持 easyui-datagrid 的自定义扩展功能；
        onResizeColumn: function () {
            var t = $(this), grid = t.combogrid("grid");
            $.fn.datagrid.extensions.defaults.onResizeColumn.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeEdit: function () {
            $.fn.datagrid.extensions.defaults.onBeforeEdit.apply($(this).combogrid("grid")[0], arguments);
        },

        onAfterEdit: function () {
            $.fn.datagrid.extensions.defaults.onAfterEdit.apply($(this).combogrid("grid")[0], arguments);
        },

        onCancelEdit: function () {
            $.fn.datagrid.extensions.defaults.onCancelEdit.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeDeleteColumn: function () {
            $.fn.datagrid.extensions.defaults.onBeforeDeleteColumn.apply($(this).combogrid("grid")[0], arguments);
        },

        onDeleteColumn: function () {
            $.fn.datagrid.extensions.defaults.onDeleteColumn.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeMoveColumn: function () {
            $.fn.datagrid.extensions.defaults.onBeforeMoveColumn.apply($(this).combogrid("grid")[0], arguments);
        },

        onMoveColumn: function () {
            $.fn.datagrid.extensions.defaults.onMoveColumn.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeDrop: function () {
            $.fn.datagrid.extensions.defaults.onBeforeDrop.apply($(this).combogrid("grid")[0], arguments);
        },

        onDrop: function () {
            $.fn.datagrid.extensions.defaults.onDrop.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeDrag: function () {
            $.fn.datagrid.extensions.defaults.onBeforeDrag.apply($(this).combogrid("grid")[0], arguments);
        },

        onStartDrag: function () {
            $.fn.datagrid.extensions.defaults.onStartDrag.apply($(this).combogrid("grid")[0], arguments);
        },

        onStopDrag: function () {
            $.fn.datagrid.extensions.defaults.onStopDrag.apply($(this).combogrid("grid")[0], arguments);
        },

        onDragEnter: function () {
            $.fn.datagrid.extensions.defaults.onDragEnter.apply($(this).combogrid("grid")[0], arguments);
        },

        onDragOver: function () {
            $.fn.datagrid.extensions.defaults.onDragOver.apply($(this).combogrid("grid")[0], arguments);
        },

        onDragLeave: function () {
            $.fn.datagrid.extensions.defaults.onDragLeave.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeUpdateRow: function () {
            $.fn.datagrid.extensions.defaults.onBeforeUpdateRow.apply($(this).combogrid("grid")[0], arguments);
        },

        onUpdateRow: function () {
            $.fn.datagrid.extensions.defaults.onUpdateRow.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeAppendRow: function () {
            $.fn.datagrid.extensions.defaults.onBeforeAppendRow.apply($(this).combogrid("grid")[0], arguments);
        },

        onAppendRow: function () {
            $.fn.datagrid.extensions.defaults.onAppendRow.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeInsertRow: function () {
            $.fn.datagrid.extensions.defaults.onBeforeInsertRow.apply($(this).combogrid("grid")[0], arguments);
        },

        onBeforeRow: function () {
            $.fn.datagrid.extensions.defaults.onBeforeRow.apply($(this).combogrid("grid")[0], arguments);
        }
    });

    $.extend($.fn.combogrid.defaults, defaults);
    $.extend($.fn.combogrid.methods, methods);
})(jQuery);