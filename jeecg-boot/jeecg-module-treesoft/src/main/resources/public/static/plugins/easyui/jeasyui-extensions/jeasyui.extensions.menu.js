/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI menu Extensions 1.0 beta
* jQuery EasyUI menu 组件扩展
* jeasyui.extensions.menu.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    /**
    * initialize the target menu, the function can be invoked only once
    */
    function init(target) {
        var t = $(target).appendTo('body').addClass('menu-top');

        //$(document).unbind('.menu').bind('mousedown.menu', function (e) {
        //    //			var allMenu = $('body>div.menu:visible');
        //    //			var m = $(e.target).closest('div.menu', allMenu);
        //    var m = $(e.target).closest('div.menu,div.combo-p');
        //    if (m.length) { return }
        //    $('body>div.menu-top:visible').menu('hide');
        //});

        var menus = splitMenu(t);
        for (var i = 0; i < menus.length; i++) {
            createMenu(menus[i]);
        }

        function splitMenu(menu) {
            var menus = [];
            menu.addClass('menu');
            menus.push(menu);
            if (!menu.hasClass('menu-content')) {
                menu.children('div').each(function () {
                    var submenu = $(this).children('div');
                    if (submenu.length) {
                        submenu.insertAfter(target);
                        this.submenu = submenu; 	// point to the sub menu
                        var mm = splitMenu(submenu);
                        menus = menus.concat(mm);
                    }
                });
            }
            return menus;
        }

        function createMenu(menu) {
            var wh = $.parser.parseOptions(menu[0], ['width', 'height']);
            menu[0].originalHeight = wh.height || 0;
            if (menu.hasClass('menu-content')) {
                menu[0].originalWidth = wh.width || menu._outerWidth();
            } else {
                menu[0].originalWidth = wh.width || 0;
                menu.children('div').each(function () {
                    var item = $(this);
                    //var itemOpts = $.extend({}, $.parser.parseOptions(this, ['name', 'iconCls', 'href', { separator: 'boolean' }]), {
                    //    disabled: (item.attr('disabled') ? true : undefined)
                    //});
                    //  注释掉上三行代码，并添加了下三行代码，以实现获取 menu-item 的属性 hideOnClick，该参数表示是否在点击菜单项后菜单自动隐藏
                    var itemOpts = $.extend({ hideOnClick: true }, $.parser.parseOptions(this, ['name', 'iconCls', 'href', { hideOnClick: 'boolean', separator: 'boolean' }]), {
                        disabled: (item.attr('disabled') ? true : undefined)
                    });
                    if (itemOpts.separator) {
                        item.addClass('menu-sep');
                    }
                    if (!item.hasClass('menu-sep')) {
                        item[0].itemName = itemOpts.name || '';
                        item[0].itemHref = itemOpts.href || '';

                        //  添加了下一行代码，以实现将 menu-item 的 hideOnClick 绑定到菜单项上
                        item[0].hideOnClick = (itemOpts.hideOnClick == undefined || itemOpts.hideOnClick == null ? true : itemOpts.hideOnClick);

                        var text = item.addClass('menu-item').html();
                        item.empty().append($('<div class="menu-text"></div>').html(text));
                        if (itemOpts.iconCls) {
                            $('<div class="menu-icon"></div>').addClass(itemOpts.iconCls).appendTo(item);
                        }
                        if (itemOpts.disabled) {
                            setDisabled(target, item[0], true);
                        }
                        if (item[0].submenu) {
                            $('<div class="menu-rightarrow"></div>').appendTo(item); // has sub menu
                        }

                        bindMenuItemEvent(target, item);
                    }
                });
                $('<div class="menu-line"></div>').prependTo(menu);
            }
            setMenuWidth(target, menu);
            menu.hide();

            bindMenuEvent(target, menu);
        }
    }

    function setMenuWidth(target, menu) {
        var opts = $.data(target, 'menu').options;
        var style = menu.attr('style') || '';
        menu.css({
            display: 'block',
            left: -10000,
            height: 'auto',
            overflow: 'hidden'
        });

        var el = menu[0];
        var width = el.originalWidth || 0;
        if (!width) {
            width = 0;
            menu.find('div.menu-text').each(function () {
                if (width < $(this)._outerWidth()) {
                    width = $(this)._outerWidth();
                }
                $(this).closest('div.menu-item')._outerHeight($(this)._outerHeight() + 2);
            });
            width += 40;
        }

        width = Math.max(width, opts.minWidth);
        var height = el.originalHeight || menu.outerHeight();
        var lineHeight = Math.max(el.originalHeight, menu.outerHeight()) - 2;
        menu._outerWidth(width)._outerHeight(height);
        menu.children('div.menu-line')._outerHeight(lineHeight);

        //		menu._outerWidth(Math.max((menu[0].originalWidth || 0), width, opts.minWidth));
        //		
        //		menu.children('div.menu-line')._outerHeight(menu.outerHeight());

        style += ';width:' + el.style.width + ';height:' + el.style.height;

        menu.attr('style', style);
    }

    /**
    * bind menu event
    */
    function bindMenuEvent(target, menu) {
        var state = $.data(target, 'menu');
        menu.unbind('.menu').bind('mouseenter.menu', function () {
            if (state.timer) {
                clearTimeout(state.timer);
                state.timer = null;
            }
        }).bind('mouseleave.menu', function () {
            if (state.options.hideOnUnhover) {
                state.timer = setTimeout(function () {
                    hideAll(target);
                }, 100);
            }
        });
    }

    /**
    * bind menu item event
    */
    function bindMenuItemEvent(target, item) {
        if (!item.hasClass('menu-item')) { return }
        item.unbind('.menu');
        item.bind('click.menu', function () {
            var t = $(this);
            if (t.hasClass('menu-item-disabled')) {
                return;
            }
            // only the sub menu clicked can hide all menus
            if (!this.submenu) {
                //hideAll(target);
                //  注释掉上面一行代码，并添加下面一行代码，以实现当 menu-item 的属性 hideOnClick 为 false 的情况下，点击菜单项不自动隐藏菜单控件。
                if (this.hideOnClick) { hideAll(target); }

                var href = t.attr('href');
                if (href) {
                    location.href = href;
                }
            }
            var item = $(target).menu('getItem', this);
            $.data(target, 'menu').options.onClick.call(target, item);
        }).bind('mouseenter.menu', function (e) {
            // hide other menu
            item.siblings().each(function () {
                if (this.submenu) {
                    hideMenu(this.submenu);
                }
                $(this).removeClass('menu-active');
            });
            // show this menu
            item.addClass('menu-active');

            if ($(this).hasClass('menu-item-disabled')) {
                item.addClass('menu-active-disabled');
                return;
            }

            var submenu = item[0].submenu;
            if (submenu) {
                $(target).menu('show', {
                    menu: submenu,
                    parent: item
                });
            }
        }).bind('mouseleave.menu', function (e) {
            item.removeClass('menu-active menu-active-disabled');
            var submenu = item[0].submenu;
            if (submenu) {
                if (e.pageX >= parseInt(submenu.css('left'))) {
                    item.addClass('menu-active');
                } else {
                    hideMenu(submenu);
                }

            } else {
                item.removeClass('menu-active');
            }
        });
    }

    /**
    * hide top menu and it's all sub menus
    */
    function hideAll(target) {
        var state = $.data(target, 'menu');
        if (state) {
            if ($(target).is(':visible')) {
                hideMenu($(target));
                state.options.onHide.call(target);
            }
        }
        return false;
    }

    /**
    * show the menu, the 'param' object has one or more properties:
    * left: the left position to display
    * top: the top position to display
    * menu: the menu to display, if not defined, the 'target menu' is used
    * parent: the parent menu item to align to
    * alignTo: the element object to align to
    */
    function showMenu(target, param) {
        var left, top;
        param = param || {};
        var menu = $(param.menu || target);
        if (menu.hasClass('menu-top')) {
            var opts = $.data(target, 'menu').options;
            $.extend(opts, param);
            left = opts.left;
            top = opts.top;
            if (opts.alignTo) {
                var at = $(opts.alignTo);
                left = at.offset().left;
                top = at.offset().top + at._outerHeight();
                if (opts.align == 'right') {
                    left += at.outerWidth() - menu.outerWidth();
                }
            }
            if (left + menu.outerWidth() > $(window)._outerWidth() + $(document)._scrollLeft()) {
                left = $(window)._outerWidth() + $(document).scrollLeft() - menu.outerWidth() - 5;
            }
            if (left < 0) { left = 0; }
            if (top + menu.outerHeight() > $(window)._outerHeight() + $(document).scrollTop()) {
                top = $(window)._outerHeight() + $(document).scrollTop() - menu.outerHeight() - 5;
            }
        } else {
            var parent = param.parent;	// the parent menu item
            left = parent.offset().left + parent.outerWidth() - 2;
            if (left + menu.outerWidth() + 5 > $(window)._outerWidth() + $(document).scrollLeft()) {
                left = parent.offset().left - menu.outerWidth() + 2;
            }
            var top = parent.offset().top - 3;
            if (top + menu.outerHeight() > $(window)._outerHeight() + $(document).scrollTop()) {
                top = $(window)._outerHeight() + $(document).scrollTop() - menu.outerHeight() - 5;
            }
        }
        menu.css({ left: left, top: top });
        menu.show(0, function () {
            if (!menu[0].shadow) {
                menu[0].shadow = $('<div class="menu-shadow"></div>').insertAfter(menu);
            }
            menu[0].shadow.css({
                display: 'block',
                zIndex: $.fn.menu.defaults.zIndex++,
                left: menu.css('left'),
                top: menu.css('top'),
                width: menu.outerWidth(),
                height: menu.outerHeight()
            });
            menu.css('z-index', $.fn.menu.defaults.zIndex++);
            if (menu.hasClass('menu-top')) {
                $.data(menu[0], 'menu').options.onShow.call(menu[0]);
            }
        });
    }

    function hideMenu(menu) {
        if (!menu) return;

        hideit(menu);
        menu.find('div.menu-item').each(function () {
            if (this.submenu) {
                hideMenu(this.submenu);
            }
            $(this).removeClass('menu-active');
        });

        function hideit(m) {
            m.stop(true, true);
            if (m[0].shadow) {
                m[0].shadow.hide();
            }
            m.hide();
        }
    }

    function findItem(target, text) {
        var result = null;
        var tmp = $('<div></div>');
        function find(menu) {
            menu.children('div.menu-item').each(function () {
                var item = $(target).menu('getItem', this);
                var s = tmp.empty().html(item.text).text();
                if (text == $.trim(s)) {
                    result = item;
                } else if (this.submenu && !result) {
                    find(this.submenu);
                }
            });
        }
        find($(target));
        tmp.remove();
        return result;
    }

    function setDisabled(target, itemEl, disabled) {
        var t = $(itemEl);
        if (!t.hasClass('menu-item')) { return }

        if (disabled) {
            t.addClass('menu-item-disabled');
            if (itemEl.onclick) {
                itemEl.onclick1 = itemEl.onclick;
                itemEl.onclick = null;
            }
        } else {
            t.removeClass('menu-item-disabled');
            if (itemEl.onclick1) {
                itemEl.onclick = itemEl.onclick1;
                itemEl.onclick1 = null;
            }
        }
    }

    function appendItem(target, param) {
        var menu = $(target);
        if (param.parent) {
            if (!param.parent.submenu) {
                var submenu = $('<div class="menu"><div class="menu-line"></div></div>').appendTo('body');
                submenu.hide();
                param.parent.submenu = submenu;
                $('<div class="menu-rightarrow"></div>').appendTo(param.parent);
            }
            menu = param.parent.submenu;
        }
        if (param.separator) {
            var item = $('<div class="menu-sep"></div>').appendTo(menu);
        } else {
            var item = $('<div class="menu-item"></div>').appendTo(menu);
            $('<div class="menu-text"></div>').html(param.text).appendTo(item);
        }
        if (param.iconCls) $('<div class="menu-icon"></div>').addClass(param.iconCls).appendTo(item);
        if (param.id) item.attr('id', param.id);
        if (param.name) { item[0].itemName = param.name }
        if (param.href) { item[0].itemHref = param.href }
        if (param.onclick) {
            if (typeof param.onclick == 'string') {
                item.attr('onclick', param.onclick);
            } else {
                item[0].onclick = eval(param.onclick);
            }
        }
        if (param.handler) { item[0].onclick = eval(param.handler) }
        if (param.disabled) { setDisabled(target, item[0], true) }

        bindMenuItemEvent(target, item);
        bindMenuEvent(target, menu);
        setMenuWidth(target, menu);
    }

    function removeItem(target, itemEl) {
        function removeit(el) {
            if (el.submenu) {
                el.submenu.children('div.menu-item').each(function () {
                    removeit(this);
                });
                var shadow = el.submenu[0].shadow;
                if (shadow) shadow.remove();
                el.submenu.remove();
            }
            $(el).remove();
        }
        removeit(itemEl);
    }

    function destroyMenu(target) {
        $(target).children('div.menu-item').each(function () {
            removeItem(target, this);
        });
        if (target.shadow) target.shadow.remove();
        $(target).remove();
    }

    $.fn.menu = function (options, param) {
        if (typeof options == 'string') {
            return $.fn.menu.methods[options](this, param);
        }

        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'menu');
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, 'menu', {
                    options: $.extend({}, $.fn.menu.defaults, $.fn.menu.parseOptions(this), options)
                });
                init(this);
            }
            $(this).css({
                left: state.options.left,
                top: state.options.top
            });
        });
    };

    $.fn.menu.methods = {
        options: function (jq) {
            return $.data(jq[0], 'menu').options;
        },
        show: function (jq, pos) {
            return jq.each(function () {
                showMenu(this, pos);
            });
        },
        hide: function (jq) {
            return jq.each(function () {
                hideAll(this);
            });
        },
        destroy: function (jq) {
            return jq.each(function () {
                destroyMenu(this);
            });
        },
        /**
		 * set the menu item text
		 * param: {
		 * 	target: DOM object, indicate the menu item
		 * 	text: string, the new text
		 * }
		 */
        setText: function (jq, param) {
            return jq.each(function () {
                $(param.target).children('div.menu-text').html(param.text);
            });
        },
        /**
		 * set the menu icon class
		 * param: {
		 * 	target: DOM object, indicate the menu item
		 * 	iconCls: the menu item icon class
		 * }
		 */
        setIcon: function (jq, param) {
            return jq.each(function () {
                $(param.target).children('div.menu-icon').remove();
                if (param.iconCls) {
                    $('<div class="menu-icon"></div>').addClass(param.iconCls).appendTo(param.target);
                }
            });
        },
        /**
		 * get the menu item data that contains the following property:
		 * {
		 * 	target: DOM object, the menu item
		 *  id: the menu id
		 * 	text: the menu item text
		 * 	iconCls: the icon class
		 *  href: a remote address to redirect to
		 *  onclick: a function to be called when the item is clicked
		 * }
		 */
        getItem: function (jq, itemEl) {
            var t = $(itemEl);
            var item = {
                target: itemEl,
                id: t.attr('id'),
                text: $.trim(t.children('div.menu-text').html()),
                disabled: t.hasClass('menu-item-disabled'),
                //				href: t.attr('href'),
                //				name: t.attr('name'),
                name: itemEl.itemName,
                href: itemEl.itemHref,
                //  增加下面一行代码，使得通过 getItem 方法返回的 menu-item 中包含其 hideOnClick 属性
                hideOnClick: itemEl.hideOnClick,
                onclick: itemEl.onclick
            }
            var icon = t.children('div.menu-icon');
            if (icon.length) {
                var cc = [];
                var aa = icon.attr('class').split(' ');
                for (var i = 0; i < aa.length; i++) {
                    if (aa[i] != 'menu-icon') {
                        cc.push(aa[i]);
                    }
                }
                item.iconCls = cc.join(' ');
            }
            return item;
        },
        findItem: function (jq, text) {
            return findItem(jq[0], text);
        },
        /**
		 * append menu item, the param contains following properties:
		 * parent,id,text,iconCls,href,onclick
		 * when parent property is assigned, append menu item to it
		 */
        appendItem: function (jq, param) {
            return jq.each(function () {
                appendItem(this, param);
            });
        },
        removeItem: function (jq, itemEl) {
            return jq.each(function () {
                removeItem(this, itemEl);
            });
        },
        enableItem: function (jq, itemEl) {
            return jq.each(function () {
                setDisabled(this, itemEl, false);
            });
        },
        disableItem: function (jq, itemEl) {
            return jq.each(function () {
                setDisabled(this, itemEl, true);
            });
        }
    };

    $.fn.menu.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, ['left', 'top', { minWidth: 'number', hideOnUnhover: "boolean" }]));
    };

    $.fn.menu.defaults = {
        zIndex: 110000,
        left: 0,
        top: 0,
        alignTo: null,
        align: 'left',
        minWidth: 120,
        hideOnUnhover: true,	// Automatically hides the menu when mouse exits it
        onShow: function () { },
        onHide: function () { },
        onClick: function (item) { }
    };









    //  下面这段代码实现即使在跨 IFRAME 的情况下，一个 WEB-PAGE 中也只能同时显示一个 easyui-menu 控件。
    $.easyui.bindPageNestedFunc("mousedown", "jdirkMenu", "menu", function (win, e) {
        var jq = win.jQuery,
            allMenu = jq("body>div.menu:visible"),
            m = jq(e.target).closest('div.menu', allMenu);
        if (m.length) { return }
        jq('body>div.menu-top:visible').menu('hide');
    });




    var buildMenu = function (options) {
        var menu = $("<div></div>").appendTo("body"),
            opts = $.extend({}, $.fn.menu.defaults, {
                left: window.event ? window.event.clientX : 0,
                top: window.event ? window.event.clientY : 0,
                hideOnUnhover: false,
                slideOut: false, autoDestroy: true,
                items: null, hideDisabledMenu: false, minWidth: 140
            }, options || {});
        opts.items = $.util.likeArrayNotString(opts.items) ? opts.items : [];
        if (opts.id) { menu.attr("id", opts.id); }
        if (opts.name) { menu.attr("name", opts.name); }
        if (!opts.items.length) { opts.items.push({ text: "当前无菜单项", disabled: true }); }
        $.each(opts.items, function (i, item) {
            if (opts.hideDisabledMenu && item.disabled) { return; } appendItemToMenu(menu, item, menu);
        });
        return { menu: menu, options: opts };
    };

    var appendItemToMenu = function (menu, item, menus) {
        if ($.util.isString(item) && $.array.contains(["-", "—", "|"], $.trim(item))) {
            $("<div></div>").addClass("menu-sep").appendTo(menu);
            return;
        }
        item = $.extend({
            id: null, text: null, iconCls: null, href: null, disabled: false,
            onclick: null, handler: null, bold: false, style: null,
            children: null, hideDisabledMenu: false, hideOnClick: true
        }, item || {});
        var onclick = item.onclick, handler = item.handler;
        item.onclick = undefined; item.handler = undefined;
        item = $.util.parseMapFunction(item);
        item.onclick = onclick; item.handler = handler;
        if (item.hideDisabledMenu && item.disabled) { return; }
        var itemEle = $("<div></div>").attr({
            iconCls: item.iconCls, href: item.href, disabled: item.disabled, hideOnClick: item.hideOnClick
        }).appendTo(menu);
        if (item.id) { itemEle.attr("id", item.id); }
        if (item.style) { itemEle.css(item.style); }
        if ($.isFunction(item.handler)) {
            var handler = item.handler;
            item.onclick = function (e, item, menus) { handler.call(this, e, item, menus); };
        }
        if ($.isFunction(item.onclick)) {
            itemEle.click(function (e) {
                if (itemEle.hasClass("menu-item-disabled")) { return; }
                item.onclick.call(this, e, item, menus);
            });
        }
        var hasChild = item.children && item.children.length ? true : false, span = $("<span></span>").appendTo(itemEle);
        if (item.text) { span.text(item.text); }
        if (item.bold) { span.css("font-weight", "bold"); }
        if (hasChild) {
            var itemNode = $("<div></div>").appendTo(itemEle);
            $.each(item.children, function (i, n) {
                var val = $.util.isString(n) && $.array.contains(["-", "—", "|"], $.trim(n)) ? n
                    : $.extend({ hideDisabledMenu: item.hideDisabledMenu }, n);
                appendItemToMenu(itemNode, val, menus);
            });
        }
    };



    $.extend($.easyui, {

        //  根据指定的属性创建 easyui-menu 对象；该方法定义如下参数：
        //      options: JSON 对象类型，参数属性继承 easyui-menu 控件的所有属性和事件（参考官方 API 文档），并在此基础上增加了如下参数：
        //          id: 一个 String 对象，表示创建的菜单对象的 ID 属性。
        //          name: 一个 String 对象，表示创建的菜单对象的 name 属性。
        //          hideOnUnhover: 这是官方 API 中原有属性，此处调整其默认值为 false；
        //          hideDisabledMenu: 一个 Boolean 值，默认为 false；该属性表示当菜单项的 disabled: true，是否自动隐藏该菜单项；
        //          items: 一个 Array 对象，该数组对象中的每一个元素都是一个 JSON 格式对象用于表示一个 menu item （关于 menu item 对象属性，参考官方 API）；
        //                  该数组中每个元素的属性，除 easyui-menu 中 menu item 官方 API 定义的属性外，还增加了如下属性：
        //              hideDisabledMenu: 该属性表示在当前子菜单级别下当菜单项的 disabled: true，是否自动隐藏该菜单项；一个 Boolean 值，取上一级的 hideDisabledMenu 值；
        //              handler: 一个回调函数，表示点击菜单项时触发的事件；
        //                  回调函数 handler 和回调函数 onclick 的签名都为 function(e, item, menu)，其中：
        //                      e:  表示动作事件；
        //                      item:   表示当前点击的菜单项的 options 选项；
        //                      menu:   表示整个菜单控件的 jQuery 对象。
        //                      函数中 this 指向触发事件的对象本身
        //                  另，如果同时定义了 onclick 和 handler，则只处理 handler 而不处理 onclick，所以请不要两个回调函数属性同时使用。
        //              children: 同上一级对象的 items 属性，为一个 Array 对象；
        //          slideOut:   一个 Boolean 类型值，表示菜单是否以滑动方式显示出来；默认为 false；
        //          autoDestroy: Boolean 类型值，表示菜单是否在隐藏时自动销毁，默认为 true；
        //  返回值：返回一个 JSON 格式对象，该返回的对象中具有如下属性：
        //      menu: 依据于传入参数 options 构建出的菜单 DOM 元素对象，这是一个 jQuery 对象，该对象未初始化为 easyui-menu 控件，而只是具有该控件的 DOM 结构；
        //      options: 传入参数 options 解析后的结果，该结果尚未用于但可用于初始化 menu 元素。
        createMenu: buildMenu,

        //  根据指定的属性创建 easyui-menu 对象并立即显示出来；该方法定义的参数和本插件文件中的插件方法 createMenu 相同：
        //  注意：本方法与 createMenu 方法不同之处在于：
        //      createMenu: 仅根据传入的 options 参数创建出符合 easyui-menu DOM 结构要求的 jQuery DOM 对象，但是该对象并未初始化为 easyui-menu 控件；
        //      showMenu: 该方法在 createMenu 方法的基础上，对创建出来的 jQuery DOM 对象立即进行 easyui-menu 结构初始化，并显示出来。
        //  返回值：返回一个 jQuery 对象，该对象表示创建并显示出的 easyui-menu 元素，该返回的元素已经被初始化为 easyui-menu 控件。
        showMenu: function (options) {
            var ret = buildMenu(options), mm = ret.menu, opts = mm.menu(ret.options).menu("options"), onHide = opts.onHide;
            opts.onHide = function () {
                var m = $(this);
                if ($.isFunction(onHide)) { onHide.apply(this, arguments); }
                if (opts.autoDestroy) {
                    $.util.exec(function () { m.menu("destroy"); });
                }
            };
            mm.menu("show", { left: opts.left, top: opts.top });
            if (opts.slideOut) {
                mm.hide().slideDown(200);
                if (mm[0] && mm[0].shadow) { mm[0].shadow.hide().slideDown(200); }
            }
            return mm;
        }
    });

    //  另，增加 easyui-menu 控件中 menu-item 的如下自定义扩展属性:
    //      hideOnClick:    Boolean 类型值，默认为 true；表示点击该菜单项后整个菜单是否会自动隐藏；
    //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
    //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
    //  备注：上述增加的 menu-item 的自定义扩展属性，只有通过 $.easyui.createMenu 或者 $.easyui.showMenu 生成菜单时，才有效。

})(jQuery);