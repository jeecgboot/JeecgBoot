/**
 * jQuery EasyUI 1.3.6
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
/**
 * linkbutton - jQuery EasyUI
 * 
 */
(function ($) {

    function createButton(target) {
        var opts = $.data(target, 'linkbutton').options;
        var t = $(target).empty();

        t.addClass('l-btn').removeClass('l-btn-plain l-btn-selected l-btn-plain-selected');
        t.removeClass('l-btn-small l-btn-medium l-btn-large').addClass('l-btn-' + opts.size);
        if (opts.plain) { t.addClass('l-btn-plain') }
        if (opts.selected) {
            t.addClass(opts.plain ? 'l-btn-selected l-btn-plain-selected' : 'l-btn-selected');
        }
        t.attr('group', opts.group || '');
        t.attr('id', opts.id || '');

        var inner = $('<span class="l-btn-left"></span>').appendTo(t);
        if (opts.text) {
            $('<span class="l-btn-text"></span>').html(opts.text).appendTo(inner);
        } else {
            $('<span class="l-btn-text l-btn-empty">&nbsp;</span>').appendTo(inner);
        }
        if (opts.iconCls) {
            $('<span class="l-btn-icon">&nbsp;</span>').addClass(opts.iconCls).appendTo(inner);
            inner.addClass('l-btn-icon-' + opts.iconAlign);
        }

        t.unbind('.linkbutton').bind('focus.linkbutton', function () {
            if (!opts.disabled) {
                $(this).addClass('l-btn-focus');
            }
        }).bind('blur.linkbutton', function () {
            $(this).removeClass('l-btn-focus');
        }).bind('click.linkbutton', function () {
            if (!opts.disabled) {
                if (opts.toggle) {
                    if (opts.selected) {
                        $(this).linkbutton('unselect');
                    } else {
                        $(this).linkbutton('select');
                    }
                }
                opts.onClick.call(this);
            }
            return false;
        });
        //		if (opts.toggle && !opts.disabled){
        //			t.bind('click.linkbutton', function(){
        //				if (opts.selected){
        //					$(this).linkbutton('unselect');
        //				} else {
        //					$(this).linkbutton('select');
        //				}
        //			});
        //		}

        setSelected(target, opts.selected)
        setDisabled(target, opts.disabled);
    }

    function setSelected(target, selected) {
        var opts = $.data(target, 'linkbutton').options;
        if (selected) {
            if (opts.group) {
                $('a.l-btn[group="' + opts.group + '"]').each(function () {
                    var o = $(this).linkbutton('options');
                    if (o.toggle) {
                        $(this).removeClass('l-btn-selected l-btn-plain-selected');
                        o.selected = false;
                    }
                });
            }
            $(target).addClass(opts.plain ? 'l-btn-selected l-btn-plain-selected' : 'l-btn-selected');
            opts.selected = true;
        } else {
            if (!opts.group) {
                $(target).removeClass('l-btn-selected l-btn-plain-selected');
                opts.selected = false;
            }
        }
    }

    function setDisabled(target, disabled) {
        var state = $.data(target, 'linkbutton');
        var opts = state.options;
        $(target).removeClass('l-btn-disabled l-btn-plain-disabled');
        if (disabled) {
            opts.disabled = true;
            var href = $(target).attr('href');
            if (href) {
                state.href = href;
                $(target).attr('href', 'javascript:void(0)');
            }
            if (target.onclick) {
                state.onclick = target.onclick;
                target.onclick = null;
            }
            opts.plain ? $(target).addClass('l-btn-disabled l-btn-plain-disabled') : $(target).addClass('l-btn-disabled');
        } else {
            opts.disabled = false;
            if (state.href) {
                $(target).attr('href', state.href);
            }
            if (state.onclick) {
                target.onclick = state.onclick;
            }
        }
    }

    $.fn.linkbutton = function (options, param) {
        if (typeof options == 'string') {
            return $.fn.linkbutton.methods[options](this, param);
        }

        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'linkbutton');
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, 'linkbutton', {
                    options: $.extend({}, $.fn.linkbutton.defaults, $.fn.linkbutton.parseOptions(this), options)
                });
                $(this).removeAttr('disabled');
            }

            createButton(this);
        });
    };

    $.fn.linkbutton.methods = {
        options: function (jq) {
            return $.data(jq[0], 'linkbutton').options;
        },
        enable: function (jq) {
            return jq.each(function () {
                setDisabled(this, false);
            });
        },
        disable: function (jq) {
            return jq.each(function () {
                setDisabled(this, true);
            });
        },
        select: function (jq) {
            return jq.each(function () {
                setSelected(this, true);
            });
        },
        unselect: function (jq) {
            return jq.each(function () {
                setSelected(this, false);
            });
        }
    };

    $.fn.linkbutton.parseOptions = function (target) {
        var t = $(target);
        return $.extend({}, $.parser.parseOptions(target,
			['id', 'iconCls', 'iconAlign', 'group', 'size', { plain: 'boolean', toggle: 'boolean', selected: 'boolean' }]
		), {
		    disabled: (t.attr('disabled') ? true : undefined),
		    text: $.trim(t.html()),
		    iconCls: (t.attr('icon') || t.attr('iconCls'))
		});
    };

    $.fn.linkbutton.defaults = {
        id: null,
        disabled: false,
        toggle: false,
        selected: false,
        group: null,
        plain: false,
        text: '',
        iconCls: null,
        iconAlign: 'left',
        size: 'small',	// small,large
        onClick: function () { }
    };

})(jQuery);
