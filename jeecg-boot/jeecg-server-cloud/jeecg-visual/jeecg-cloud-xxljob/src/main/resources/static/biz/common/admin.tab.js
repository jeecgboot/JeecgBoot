/*!
* Admin Tab for XXL-BOOT
* ================
*
* 1、menu：
*      - J_menuItem:        菜单
* 2、tab：
*      - J_tabLeft:         左移按扭
*      - J_menuTabs:
*          - J_menuTab :    菜单Tab
*      - J_tabRight:        右移按扭
*      - J_tabCloseOther:   关闭其他Tab
*      - J_tabCloseAll :    关闭所有Tab
* 3、contont
*      - J_mainContent
*          - J_iframe :     页面iframe
*
* @author       xuxueli
* @repository   https://github.com/xuxueli/xxl-boot
*/
(function($) {

    $.extend({
        adminTab: {
            initTab: function(options) {

                /**
                 * 点击菜单：打开Tab，展示Menu页面
                 */
                $('.J_menuItem').on('click', function (){
                    // 获取Tab属性
                    let tabSrc = $(this).attr('href');
                    let tabName = $.trim($(this).text());

                    // open tab
                    return openTab(tabSrc, tabName);
                });

                /**
                 * Tab关闭（x按钮）：关闭菜单页面
                 */
                $('.J_menuTabs').on('click', '.J_menuTab i', closeTab);

                /**
                 * Tab单击（切换/激活）：展示Tab页面 + 左侧菜单active更新
                 */
                $('.J_menuTabs').on('click', '.J_menuTab', activeTab);

                /**
                 * 选项卡-左移按扭：查看左侧隐藏的选项卡
                 */
                $('.J_tabLeft').on('click', scrollTabLeft);

                /**
                 * 选项卡-右移按扭：查看右侧隐藏的选项卡
                 */
                $('.J_tabRight').on('click', scrollTabRight);

                /**
                 * Tab-刷新按钮：刷新active的 Tab 页面
                 */
                $('.tabReload').on('click', refreshTab);

                /**
                 * 关闭其他选项卡
                 */
                $('.J_tabCloseOther').on('click', closeOtherTabs);

                /**
                 * 关闭当前选项卡
                 */
                $('.tabCloseCurrent').on('click', tabCloseCurrent);

                /**
                 * 关闭全部选项卡
                 */
                $('.J_tabCloseAll').on('click', tabCloseAll);

                /**
                 * 全屏显示
                 */
                $('#fullScreen').on('click', function () {
                    let currentHash = window.location.hash;
                    $(document).toggleFullScreen();

                    // reset
                    if (currentHash) {
                        setTimeout(function (){
                            window.location.hash = currentHash;
                        },50)
                    }
                });

                /**
                 * 默认打开菜单Tab：优先尝试打开url路径TAB，兜底打开首个菜单
                 */
                openDefaultTab();
            },
            openTab: function(options, isCloseCurrent) {
                // 当前页面是否关闭
                if (isCloseCurrent) {
                    tabCloseCurrent();
                }
                // 打开Tab页面
                return openTab(options.tabSrc, options.tabName);
            }
        }
    });

    // -------------------- tab：open default --------------------

    /**
     * 默认打开菜单Tab：初始化首页菜单，然后 尝试打开url路径TAB
     */
    function openDefaultTab() {
        // load url
        let tabSrc = window.location.hash.slice(1);

        // 1、首页菜单：初始化
        let $firstMenuItem = $(".J_menuItem:first");
        if ($firstMenuItem.length > 0) {
            $firstMenuItem.click();
            // 首页菜单特殊逻辑，不允许关闭
            $('.J_menuTab[data-id="' + $firstMenuItem.attr('href') + '"] i').remove();
        }

        // 2、URL匹配到菜单，初始化
        if (tabSrc === '' || tabSrc === undefined || tabSrc === null) {
            // URL菜单路径不存在则pass
            return;
        }
        setTimeout(function (){
            var $menuItem = $('.J_menuItem').filter('a[href$="' + decodeURI(tabSrc) + '"]');
            if ($menuItem.length > 0) {
                // URL匹配到菜单，初始化
                $menuItem.click();
                return;
            } else {
                // 匹配失败，兜底直接打开
                openTab(tabSrc, tabSrc);
            }
        }, 100)

    }

    // -------------------- tab：open、close --------------------

    /**
     * 打开Tab：根据 url + 名称
     *
     * @param tabSrc
     * @param tabName
     * @returns {boolean}
     */
    function openTab(tabSrc, tabName) {
        // 0、valid dateurl
        if (tabSrc === undefined || $.trim(tabSrc).length === 0){
            return false;
        }
        if (tabName === undefined || $.trim(tabName).length === 0){
            tabName = tabSrc;
        }
        let tabNameShow = tabName.length > 15 ? tabName.substring(0, 15) + '...' : tabName;

        // 1、菜单Menu联动 + 页面锚点（hash参数）更新
        activeMenuAndPath(tabSrc);

        // 2、匹配已存在Tab，切换/active展示
        let tabExist = false;
        $('.J_menuTab').each(function () {
            // 匹配成功
            if ($(this).data('id') === tabSrc) {
                // Tab是否展示，若否则切换展示
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                    scrollToTab(this);
                    // 显示Tab对应的内容区
                    $('.J_mainContent .J_iframe').each(function () {
                        if ($(this).data('id') === tabSrc) {
                            $(this).show().siblings('.J_iframe').hide();
                            return false;
                        }
                    });
                }
                tabExist = true;
                return false;
            }
        });
        if (tabExist) {
            return false;
        }

        // 3、Tab不存在，初始化新Tab + IFrame
        // build Tab (other tab no-active)
        $('.J_menuTab').removeClass('active');
        var tabStr = '<a href="javascript:;" class="active J_menuTab" data-id="' + tabSrc + '" title="'+ tabName +'" >' + tabNameShow + ' <i class="fa fa-times-circle"></i></a>';

        // build IFrame (other ifame hide)
        var iframeStr = '<iframe class="J_iframe" width="100%" height="100%" src="' + tabSrc + '" frameborder="0" data-id="' + tabSrc + '" seamless></iframe>';

        // 4、添加Tab + IFrame
        // append iframe
        $('.J_mainContent').find('iframe.J_iframe').hide();
        $('.J_mainContent').append(iframeStr);
        // append tab
        $('.J_menuTabs .page-tabs-content').append(tabStr);

        // 添加遮罩层
        NProgress.inc(0.2);
        NProgress.configure({
            easing: 'ease',         // 动画缓动函数 (默认: 'ease')
            speed: 500,             // 动画速度（毫秒）(默认: 200)
            showSpinner: true      // 是否显示旋转图标 (默认: true)
        });
        NProgress.start();

        // load iframe
        let $iframe = $('.J_mainContent iframe:visible');
        $iframe.on('load', function () {
            NProgress.done();
        }).on('error', function () {
            NProgress.done();
            // 处理加载失败情况，防止跳转
            console.error('iframe load error, src = ' + $(this).attr('src'));
        });

        // 5、滚动到已激活的Tab
        scrollToTab($('.J_menuTab.active'));
        return false;
    }

    /**
     * 关闭Tab：根据点击 Tab元素
     *
     * @returns {boolean}
     */
    function closeTab() {
        // param
        var closeTabId = $(this).parents('.J_menuTab').data('id');
        var currentWidth = $(this).parents('.J_menuTab').width();

        // process
        if ($(this).parents('.J_menuTab').hasClass('active')) {
            // 1、当前元素处于活动状态

            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents('.J_menuTab').next('.J_menuTab').length > 0) {
                // 后一个Tab，激活 Tab
                var activeId = $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').data('id');
                $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').addClass('active');

                // 后一个Tab，激活 iframe
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') === activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    }, "fast");
                }

                //  移除 当前 Tab
                $(this).parents('.J_menuTab').remove();

                // 移除 当前 iframe
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') === closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }

            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(this).parents('.J_menuTab').prev('.J_menuTab').length > 0) {
                // 前一个Tab，激活 Tab
                var activeId = $(this).parents('.J_menuTab').prev('.J_menuTab:last').data('id');
                $(this).parents('.J_menuTab').prev('.J_menuTab:last').addClass('active');

                // 前一个Tab，激活 iframe
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                //  移除 当前 Tab
                $(this).parents('.J_menuTab').remove();

                // 移除 当前 iframe
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') === closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
        } else {
            // 2、当前元素不处于活动状态

            //  移除 当前 Tab
            $(this).parents('.J_menuTab').remove();

            // 移除 当前 iframe
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });

            // 滚动到active状态 Tab
            scrollToTab($('.J_menuTab.active'));
        }

        // 3、菜单Menu联动 + 页面锚点（hash参数）更新
        activeMenuAndPath($('.page-tabs-content').find('.active').attr('data-id'));
        return false;
    }

    /**
     * 菜单Menu联动 + 页面锚点（hash参数）更新
     */
    function activeMenuAndPath(tabSrc) {

        // 页面锚点（hash参数）更新
        window.location.hash = tabSrc;

        // 菜单Menu切换/active
        $(".sidebar-menu ul li, .sidebar-menu li").removeClass("active");
        $('.J_menuItem').each(function () {
            if ($(this).attr('href') === tabSrc) {

                // 菜单Menu切换/active
                $(this).parents("li").addClass("active");


                return true;
            }
        })
        return false;
    }

    /**
     * Tab单击（切换/激活）：展示Tab页面 + 左侧菜单active更新
     */
    function activeTab() {
        // 是否已激活 active，避免重复处理
        if (!$(this).hasClass('active')) {
            // 待激活Tab信息
            let tabSrc = $(this).data('id');

            // Ifame 切换展示
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') === tabSrc) {
                    $(this).show().siblings('.J_iframe').hide();
                    return false;
                }
            });

            // Tab 激活
            $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
            scrollToTab(this);

            // 菜单Menu联动
            activeMenuAndPath(tabSrc);
        }
    }

    // -------------------- tab: scroll --------------------

    /**
     * 滚动到指定选项卡
     */
    function scrollToTab(element) {

        // 当前元素左侧宽度
        var marginLeftVal = calSumWidth($(element).prevAll()), marginRightVal = calSumWidth($(element).nextAll());
        // Tab外部区域宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        // 可视区域Tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        // 实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").outerWidth() < visibleWidth) {
            scrollVal = 0;
        } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
            if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                scrollVal = marginLeftVal;
                var tabElement = element;
                while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                    scrollVal -= $(tabElement).prev().outerWidth();
                    tabElement = $(tabElement).prev();
                }
            }
        } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
            scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
        }

        // 滚动处理
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    /**
     * 计算元素集合的总宽度
     */
    function calSumWidth(elements) {
        var width = 0;
        $(elements).each(function () {
            width += $(this).outerWidth(true);
        });
        return width;
    }

    // -------------------- tab: scroll-left、scroll-right、 --------------------

    /**
     * 选项卡-左移按扭：查看左侧隐藏的选项卡
     */
    function scrollTabLeft() {

        // 当前元素左侧宽度
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // Tab外部区域宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        // 可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        // 实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).prev();
                }
                scrollVal = calSumWidth($(tabElement).prevAll());
            }
        }

        // 滚动处理
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    /**
     * 选项卡-右移按扭：查看右侧隐藏的选项卡
     */
    function scrollTabRight() {

        // 当前元素左侧宽度
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        // 可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        // 实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
            if (scrollVal > 0) {
                $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                }, "fast");
            }
        }
    }

    // -------------------- tab: refresh、close-other、close-current、close-all --------------------

    /**
     * Tab-刷新按钮：刷新active的 Tab 页面
     */
    function refreshTab() {

        // 显示进度条
        NProgress.inc(0.2);
        NProgress.configure({
            easing: 'ease',         // 动画缓动函数 (默认: 'ease')
            speed: 500,             // 动画速度（毫秒）(默认: 200)
            showSpinner: true       // 是否显示旋转图标 (默认: true)
        });
        NProgress.start();

        // 1、获取当前激活的 Tab
        var tabSrc = $('.page-tabs-content').find('.active').attr('data-id');
        var target = $('.J_iframe[data-id="' + tabSrc + '"]');
        var url = target.attr('src');

        // 2、重新加载页面
        // target.attr('src', url).ready();
        target.attr('src', url).on('load', function () {
            NProgress.done();

            // 3、菜单Menu联动 + 页面锚点（hash参数）更新
            activeMenuAndPath($('.page-tabs-content').find('.active').attr('data-id'));

        }).on('error', function () {
            NProgress.done();
            // 处理加载失败情况，防止跳转
            console.error('iframe load error, src = ' + $(this).attr('src'));
        });
    }

    /**
     * 选项卡-Tab双击：刷新菜单页面
     */
    /*$('.J_menuTabs').on('dblclick', '.J_menuTab', refreshTab);*/

    /**
     * 滚动到已激活的选项卡
     */
    /*$('.J_tabShowActive').on('click', showActiveTab);
    function showActiveTab(){
        scrollToTab($('.J_menuTab.active'));
    }*/

    /**
     * 关闭其他选项卡
     */
    function closeOtherTabs(){
        $('.page-tabs-content').children("[data-id]").not(":first").not(".active").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').css("margin-left", "0");
    }

    /**
     * 关闭当前选项卡
     */
    function tabCloseCurrent() {
        $('.page-tabs-content').find('.active i').trigger("click");
    }

    /**
     * 关闭全部选项卡
     */
    function tabCloseAll(){
        // 保留 第一个 Tab，其他删除（Tab+iframe）
        $('.page-tabs-content').children("[data-id]").not(":first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        // 第一个 Tab 激活/展示（Tab+iframe）
        $('.page-tabs-content').children("[data-id]:first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').show();
            $(this).addClass("active");
        });
        $('.page-tabs-content').css("margin-left", "0");

        // 菜单Menu联动 + 页面锚点（hash参数）更新
        activeMenuAndPath($('.page-tabs-content').find('.active').attr('data-id'));
    }


})(jQuery);
