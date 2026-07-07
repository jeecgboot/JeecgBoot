/*!
* Admin Util for XXL-BOOT
* ================
*
* 1、openTab：    打开新页面，兼容iframe和window.open
*
* @author       xuxueli
* @repository   https://github.com/xuxueli/xxl-boot
*/
$(function(){


    // ---------------------- openTab ----------------------

    /**
     * 打开新页面，兼容iframe和window.open
     *
     * @param tabSrc            tab访问地址
     * @param tabName           tab展示名称
     * @param isCloseCurrent    是否关闭当前页
     */
    window.openTab = function (tabSrc, tabName, isCloseCurrent) {
        // open tab
        if (window.parent.$.adminTab) {
            window.parent.$.adminTab.openTab({
                tabSrc: tabSrc,
                tabName: tabName
            }, isCloseCurrent)
        } else {
            if (isCloseCurrent) {
                window.open(tabSrc, '_self');
            } else {
                window.open(tabSrc, '_blank');
            }
        }
    }

    // ---------------------- isOpenWithTab ----------------------

    /**
     * 是否在Tab中打开
     */
    window.isOpenWithTab = function () {
        return !!window.parent.$.adminTab;
    }

});
