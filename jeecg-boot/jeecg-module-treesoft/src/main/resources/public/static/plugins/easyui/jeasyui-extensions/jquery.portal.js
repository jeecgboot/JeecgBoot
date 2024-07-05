/**
 * portal - jQuery EasyUI
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2010-2012 stworthy [ stworthy@gmail.com ] 
 * 
 * Dependencies:
 *   draggable
 *   panel
 * 
 * jQuery EasyUI portal Plugin 1.0 beta
 * jQuery EasyUI portal ���
 * jquery.portal.js
 
 *
 * Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
 * http://www.chenjianwei.org
 */
(function ($) {
    /**
    * initialize the portal
    */
    function init(target) {
        $(target).addClass('portal');
        var table = $('<table border="0" cellspacing="0" cellpadding="0"><tr></tr></table>').appendTo(target);
        var tr = table.find('tr');

        var columnWidths = [];
        var totalWidth = 0;
        $(target).children('div:first').addClass('portal-column-left');
        $(target).children('div:last').addClass('portal-column-right');
        $(target).find('>div').each(function () {	// each column panel
            var column = $(this);
            totalWidth += column.outerWidth();
            columnWidths.push(column.outerWidth());

            var td = $('<td class="portal-column-td"></td>').appendTo(tr)
            column.addClass('portal-column').appendTo(td);
            column.find('>div').each(function () {	// each portal panel
                var p = $(this).addClass('portal-p').panel({
                    doSize: false,
                    cls: 'portal-panel'//,
                    //onClose: function () { $(this).panel("destroy"); }
                });
                var opts = p.panel("options"), onClose = opts.onClose;
                opts.onClose = function () {
                    if ($.isFunction(onClose)) { onClose.apply(this, arguments); }
                    $(this).panel("destroy");
                };
                makeDraggable(target, p);
            });
        });
        for (var i = 0; i < columnWidths.length; i++) {
            columnWidths[i] /= totalWidth;
        }

        $(target).bind('_resize', function () {
            var opts = $.data(target, 'portal').options;
            if (opts.fit == true) {
                setSize(target);
            }
            return false;
        });

        return columnWidths;
    }

    function setSize(target) {
        var t = $(target);
        var opts = $.data(target, 'portal').options;
        if (opts.fit) {
            var p = t.parent();
            opts.width = p.width();
            opts.height = p.height();
        }
        if (!isNaN(opts.width)) {
            t._outerWidth(opts.width);
        } else {
            t.width('auto');
        }
        if (!isNaN(opts.height)) {
            t._outerHeight(opts.height);
        } else {
            t.height('auto');
        }

        var hasScroll = t.find('>table').outerHeight() > t.height();
        var width = t.width();
        var columnWidths = $.data(target, 'portal').columnWidths;
        var leftWidth = 0;

        // calculate and set every column size
        for (var i = 0; i < columnWidths.length; i++) {
            var p = t.find('div.portal-column:eq(' + i + ')');
            var w = Math.floor(width * columnWidths[i]);
            if (i == columnWidths.length - 1) {
                //				w = width - leftWidth - (hasScroll == true ? 28 : 10);
                w = width - leftWidth - (hasScroll == true ? 18 : 0);
            }
            p._outerWidth(w);
            leftWidth += p.outerWidth();

            // resize every panel of the column
            p.find('div.portal-p').panel('resize', { width: p.width() });
        }
        opts.onResize.call(target, opts.width, opts.height);
    }

    /**
    * set draggable feature for the specified panel
    */
    function makeDraggable(target, panel) {
        var spacer;
        panel.panel('panel').draggable({
            handle: '>div.panel-header>div.panel-title',
            proxy: function (source) {
                var p = $('<div class="portal-proxy">proxy</div>').insertAfter(source);
                p.width($(source).width());
                p.height($(source).height());
                p.html($(source).html());
                p.find('div.portal-p').removeClass('portal-p').hide();
                return p;
            },
            onBeforeDrag: function (e) {
                e.data.startTop = $(this).position().top + $(target).scrollTop();
            },
            onStartDrag: function (e) {
                $(this).hide();
                spacer = $('<div class="portal-spacer"></div>').insertAfter(this);
                setSpacerSize($(this).outerWidth(), $(this).outerHeight());
            },
            onDrag: function (e) {
                var p = findPanel(e, this);
                if (p) {
                    if (p.pos == 'up') {
                        spacer.insertBefore(p.target);
                    } else {
                        spacer.insertAfter(p.target);
                    }
                    setSpacerSize($(p.target).outerWidth());
                } else {
                    var c = findColumn(e);
                    if (c) {
                        if (c.find('div.portal-spacer').length == 0) {
                            spacer.appendTo(c);
                            setSize(target);
                            setSpacerSize(c.width());
                        }
                    }
                }
            },
            onStopDrag: function (e) {
                $(this).css('position', 'static');
                $(this).show();
                spacer.hide();
                $(this).insertAfter(spacer);
                spacer.remove();
                setSize(target);
                panel.panel('move');

                var opts = $.data(target, 'portal').options;
                opts.onStateChange.call(target);
            }
        });

        /**
        * find which panel the cursor is over
        */
        function findPanel(e, source) {
            var result = null;
            $(target).find('div.portal-p').each(function () {
                var pal = $(this).panel('panel');
                if (pal[0] != source) {
                    var pos = pal.offset();
                    if (e.pageX > pos.left && e.pageX < pos.left + pal.outerWidth()
							&& e.pageY > pos.top && e.pageY < pos.top + pal.outerHeight()) {
                        if (e.pageY > pos.top + pal.outerHeight() / 2) {
                            result = {
                                target: pal,
                                pos: 'down'
                            };
                        } else {
                            result = {
                                target: pal,
                                pos: 'up'
                            }
                        }
                    }
                }
            });
            return result;
        }

        /**
        * find which portal column the cursor is over
        */
        function findColumn(e) {
            var result = null;
            $(target).find('div.portal-column').each(function () {
                var pal = $(this);
                var pos = pal.offset();
                if (e.pageX > pos.left && e.pageX < pos.left + pal.outerWidth()) {
                    result = pal;
                }
            });
            return result;
        }

        /**
        * set the spacer size
        */
        function setSpacerSize(width, height) {
            spacer._outerWidth(width);
            if (height) {
                spacer._outerHeight(height);
            }
        }
    }


    $.fn.portal = function (options, param) {
        if (typeof options == 'string') {
            return $.fn.portal.methods[options](this, param);
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'portal');
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, 'portal', {
                    options: $.extend({}, $.fn.portal.defaults, $.fn.portal.parseOptions(this), options),
                    columnWidths: init(this)
                });
            }
            if (state.options.border) {
                $(this).removeClass('portal-noborder');
            } else {
                $(this).addClass('portal-noborder');
            }
            setSize(this);
        });
    };

    $.fn.portal.methods = {
        options: function (jq) {
            return $.data(jq[0], 'portal').options;
        },
        resize: function (jq, param) {
            return jq.each(function () {
                if (param) {
                    var opts = $.data(this, 'portal').options;
                    if (param.width) opts.width = param.width;
                    if (param.height) opts.height = param.height;
                }
                setSize(this);
            });
        },
        getPanels: function (jq, columnIndex) {
            var c = jq; // the panel container
            if (columnIndex >= 0) {
                c = jq.find('div.portal-column:eq(' + columnIndex + ')');
            }
            var panels = [];
            c.find('div.portal-p').each(function () {
                panels.push($(this));
            });
            return panels;
        },
        add: function (jq, param) {	// param: {panel,columnIndex}
            return jq.each(function () {
                if (!param || !$.isNumeric(param.columnIndex) || !param.panel) { return; }
                var portal = $(this), opts = portal.portal("options");
                if (opts.onBeforeAdd.call(this, param.columnIndex, param.panel) == false) { return; }
                var c = portal.find('div.portal-column:eq(' + param.columnIndex + ')');
                var p = param.panel.addClass('portal-p');
                p.panel('panel').addClass('portal-panel').appendTo(c);
                makeDraggable(this, p);
                p.panel('resize', { width: c.width() });
                var panelOpts = p.panel("options"), onClose = panelOpts.onClose;
                panelOpts.onClose = function () {
                    if ($.isFunction(onClose)) { onClose.apply(this, arguments); }
                    if (!p.length) { return; }
                    var body = $("body");
                    if ($.contains(body[0], p[0])) { p.panel("destroy"); }
                }
                opts.onAdd.call(this, param.columnIndex, param.panel);
            });
        },
        remove: function (jq, panel) {
            return jq.each(function () {
                var p = $(this), opts = p.portal("options");
                if (opts.onBeforeRemove.call(this, panel) == false) { return; }
                var panels = p.portal('getPanels'), panelOpts = panel.panel("options");
                for (var i = 0; i < panels.length; i++) {
                    var p = panels[i];
                    if (p[0] == $(panel)[0]) {
                        p.panel('destroy');
                    }
                }
                opts.onRemove.call(this, panelOpts);
            });
        },
        addColumn: function (jq) {
            return jq.each(function () {
                var state = $.data(this, "portal"), opts = state.options, columnWidths = state.columnWidths,
                    portal = $(this), totalWidth = portal.outerWidth(),
                    tr = portal.find(">table tr"), td = $("<td></td>").addClass("portal-column-td").appendTo(tr),
                    column = $("<div></div>").addClass("portal-column").appendTo(td),
                    width = parseFloat(1) / (columnWidths.length + 1);
                if (opts.onBeforeAddColumn.call(this, columnWidths.length) == false) { return; }
                for (var i = 0; i < columnWidths.length; i++) { columnWidths[i] = width; }
                columnWidths.push(width);
                tr.find(">td>div.portal-column").removeClass("portal-column-left portal-column-right");
                tr.find(">td>div.portal-column:first").addClass("portal-column-left");
                tr.find(">td>div.portal-column:last").addClass("portal-column-right");
                setSize(this);
                opts.onAddColumn.call(this, columnWidths.length - 1);
            });
        },
        removeColumn: function (jq, index) {
            return jq.each(function () {
                var state = $.data(this, "portal"), opts = state.options, columnWidths = state.columnWidths;
                if (!$.isNumeric(index) || !columnWidths || !columnWidths.length || index > columnWidths.length - 1) { return; }
                if (opts.onBeforeRemoveColumn.call(this, index) == false) { return; }
                var tr = $(this).find(">table tr"), width = parseFloat(1) / (columnWidths.length - 1);
                columnWidths.pop();
                for (var i = 0; i < columnWidths.length; i++) { columnWidths[i] = width; }
                tr.find(">td:eq(" + index + ")").remove();
                tr.find(">td>div.portal-column").removeClass("portal-column-left portal-column-right");
                tr.find(">td>div.portal-column:first").addClass("portal-column-left");
                tr.find(">td>div.portal-column:last").addClass("portal-column-right");
                setSize(this);
                opts.onRemoveColumn.call(this, index);
            });
        },
        columns: function (jq) {
            return $.data(jq[0], "portal").columnWidths.length;
        },
        disableDragging: function (jq, panel) {
            panel.panel('panel').draggable('disable');
            return jq;
        },
        enableDragging: function (jq, panel) {
            panel.panel('panel').draggable('enable');
            return jq;
        }
    };

    $.fn.portal.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, ["width", "height", { border: "boolean", fit: "boolean"}]));
    };

    $.fn.portal.defaults = {
        width: 'auto',
        height: 'auto',
        border: true,
        fit: false,
        onResize: function (width, height) { },
        onStateChange: function () { },

        onBeforeAdd: function (columnIndex, panel) { },
        onAdd: function (columnIndex, panel) { },

        onBeforeAddColumn: function (columnIndex) { },
        onAddColumn: function (columnIndex) { },

        onBeforeRemove: function (panel) { },
        onRemove: function (panelOption) { },

        onBeforeRemoveColumn: function (columnIndex) { },
        onRemoveColumn: function (columnIndex) { }
    };

    $.parser.plugins.push("portal");

})(jQuery);