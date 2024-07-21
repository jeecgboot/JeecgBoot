layui.define([], function (exports) {
    var PLUGIN_NAME = 'iziToast';  // 样式类名
    var BODY = document.querySelector('body');
    var ISMOBILE = (/Mobi/.test(navigator.userAgent)) ? true : false;
    var MOBILEWIDTH = 568;
    var ISCHROME = /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor);
    var ISFIREFOX = typeof InstallTrigger !== 'undefined';
    var ACCEPTSTOUCH = 'ontouchstart' in document.documentElement;
    // 显示区域
    var POSITIONS = ['bottomRight', 'bottomLeft', 'bottomCenter', 'topRight', 'topLeft', 'topCenter', 'center'];
    // 默认主题
    var THEMES = {
        info: {
            color: 'blue',
            icon: 'ico-info'
        },
        success: {
            color: 'green',
            icon: 'ico-success'
        },
        warning: {
            color: 'orange',
            icon: 'ico-warning'
        },
        error: {
            color: 'red',
            icon: 'ico-error'
        },
        question: {
            color: 'yellow',
            icon: 'ico-question'
        }
    };
    var CONFIG = {};  // 全局配置
    // 默认配置
    var defaults = {
        id: null,
        className: '',  // 自定义class，用空格分割
        title: '',  // 标题
        titleColor: '',  // 标题文字颜色
        titleSize: '',  // 标题文字大小
        titleLineHeight: '',  // 标题高度
        message: '',  // 内容
        messageColor: '',  // 内容文字颜色
        messageSize: '',  // 内容文字大小
        messageLineHeight: '',  // 内容高度
        backgroundColor: '',  // 背景颜色
        theme: 'light', // dark
        color: '', // 背景颜色
        icon: '',  // 图标
        iconText: '',  // 图标文字
        iconColor: '',  // 图标颜色
        iconUrl: null,  // 图标地址
        image: '',  // 是否显示图片
        imageWidth: 60,  // 图片宽度
        maxWidth: null,  // 最大宽度
        zindex: null,  //
        layout: 2,  // 布局类型
        balloon: false,  // 气泡
        close: true,  // 是否显示关闭按钮
        closeOnEscape: false,
        closeOnClick: false,  // 点击关闭
        displayMode: 0,  // 0无限制,1存在就不发出,2销毁之前
        position: 'topRight', // bottomRight, bottomLeft, topRight, topLeft, topCenter, bottomCenter, center
        target: '',  // 显示位置
        targetFirst: null,  // 插入顺序
        timeout: 5000,  // 关闭时间，false不自动关闭
        rtl: false,  // 内容居右
        animateInside: false,  // 进入动画效果
        drag: true,  // 是否可滑动移除
        pauseOnHover: true,  // 鼠标移入暂停进度条时间
        resetOnHover: false,  // 鼠标移入重置进度条时间
        progressBar: true,  // 是否显示进度条
        progressBarColor: '',  // 进度条颜色
        progressBarEasing: 'linear',  // 进度条动画效果
        overlay: false,  // 是否显示遮罩层
        overlayClose: false,  // 点击遮罩层是否关闭
        overlayColor: 'rgba(0, 0, 0, 0.1)',  // 遮罩层颜色
        transitionIn: 'fadeInLeft', // bounceInLeft, bounceInRight, bounceInUp, bounceInDown, fadeIn, fadeInDown, fadeInUp, fadeInLeft, fadeInRight, flipInX
        transitionOut: 'fadeOutRight', // fadeOut, fadeOutUp, fadeOutDown, fadeOutLeft, fadeOutRight, flipOutX
        transitionInMobile: 'bounceInDown',  // 移动端进入动画
        transitionOutMobile: 'fadeOutUp',  // 移动端退出动画
        buttons: {},  // 操作按钮
        inputs: {},  // 输入框
        audio: '',  // 音效
        onOpening: function () {
        },
        onOpened: function () {
        },
        onClosing: function () {
        },
        onClosed: function () {
        }
    };

    var $iziToast = {
        children: {},
        setSetting: function (ref, option, value) {
            $iziToast.children[ref][option] = value;
        },
        getSetting: function (ref, option) {
            return $iziToast.children[ref][option];
        },
        // 全局设置
        settings: function (options) {
            $iziToast.destroy();  // 全部销毁之前的通知
            CONFIG = options;
            defaults = extend(defaults, options || {});
        },
        // 关闭所有通知
        destroy: function () {
            forEach(document.querySelectorAll('.' + PLUGIN_NAME + '-overlay'), function (element, index) {
                element.remove();
            });
            forEach(document.querySelectorAll('.' + PLUGIN_NAME + '-wrapper'), function (element, index) {
                element.remove();
            });
            forEach(document.querySelectorAll('.' + PLUGIN_NAME), function (element, index) {
                element.remove();
            });
            this.children = {};
            // 移除事件监听
            document.removeEventListener(PLUGIN_NAME + '-opened', {}, false);
            document.removeEventListener(PLUGIN_NAME + '-opening', {}, false);
            document.removeEventListener(PLUGIN_NAME + '-closing', {}, false);
            document.removeEventListener(PLUGIN_NAME + '-closed', {}, false);
            document.removeEventListener('keyup', {}, false);
            CONFIG = {};  // 移除全局配置
        },
        // msg类型
        msg: function (msg, options) {
            if (options.icon == 4) {
                options.overlay = true;
                options.timeout = false;
                options.drag = false;
                options.displayMode = 0;
            }
            var icons = ['ico-success', 'ico-error', 'ico-warning', 'ico-load', 'ico-info'];
            options.icon = icons[options.icon - 1];
            var theme = {
                message: msg,
                position: 'topCenter',
                transitionIn: 'bounceInDown',
                transitionOut: 'fadeOut',
                transitionOutMobile: 'fadeOut',
                progressBar: false,
                close: false,
                layout: 1,
                audio: ''
            };
            var settings = extend(CONFIG, options || {});
            settings = extend(theme, settings || {});
            this.show(settings);
        }
    };

    // 关闭指定的通知
    $iziToast.hide = function (options, $toast, closedBy) {
        if (typeof $toast != 'object') {
            $toast = document.querySelector($toast);
        }
        var that = this;
        var settings = extend(this.children[$toast.getAttribute('data-iziToast-ref')], options || {});
        settings.closedBy = closedBy || null;
        delete settings.time.REMAINING;
        $toast.classList.add(PLUGIN_NAME + '-closing');
        // 移除遮罩层
        (function () {
            var $overlay = document.querySelector('.' + PLUGIN_NAME + '-overlay');
            if ($overlay !== null) {
                var refs = $overlay.getAttribute('data-iziToast-ref');
                refs = refs.split(',');
                var index = refs.indexOf(String(settings.ref));
                if (index !== -1) {
                    refs.splice(index, 1);
                }
                $overlay.setAttribute('data-iziToast-ref', refs.join());
                if (refs.length === 0) {
                    $overlay.classList.remove('fadeIn');
                    $overlay.classList.add('fadeOut');
                    setTimeout(function () {
                        $overlay.remove();
                    }, 700);
                }
            }
        })();
        // 移除动画
        if (settings.transitionIn) {
            $toast.classList.remove(settings.transitionIn);
        }
        if (settings.transitionInMobile) {
            $toast.classList.remove(settings.transitionInMobile);
        }
        if (ISMOBILE || window.innerWidth <= MOBILEWIDTH) {
            if (settings.transitionOutMobile)
                $toast.classList.add(settings.transitionOutMobile);
        } else {
            if (settings.transitionOut)
                $toast.classList.add(settings.transitionOut);
        }
        var H = $toast.parentNode.offsetHeight;
        $toast.parentNode.style.height = H + 'px';
        $toast.style.pointerEvents = 'none';
        if (!ISMOBILE || window.innerWidth > MOBILEWIDTH) {
            $toast.parentNode.style.transitionDelay = '0.2s';
        }
        try {
            var event = new CustomEvent(PLUGIN_NAME + '-closing', {detail: settings, bubbles: true, cancelable: true});
            document.dispatchEvent(event);
        } catch (ex) {
            console.warn(ex);
        }
        setTimeout(function () {
            $toast.parentNode.style.height = '0px';
            $toast.parentNode.style.overflow = '';
            setTimeout(function () {
                delete that.children[settings.ref];
                $toast.parentNode.remove();
                try {
                    var event = new CustomEvent(PLUGIN_NAME + '-closed', {
                        detail: settings,
                        bubbles: true,
                        cancelable: true
                    });
                    document.dispatchEvent(event);
                } catch (ex) {
                    console.warn(ex);
                }
                if (typeof settings.onClosed !== 'undefined') {
                    settings.onClosed.apply(null, [settings, $toast, closedBy]);
                }
            }, 1000);
        }, 200);
        // 回调关闭事件
        if (typeof settings.onClosing !== 'undefined') {
            settings.onClosing.apply(null, [settings, $toast, closedBy]);
        }
    };

    // 显示通知
    $iziToast.show = function (options) {
        var that = this;
        // Merge user options with defaults
        var settings = extend(CONFIG, options || {});
        settings = extend(defaults, settings);
        settings.time = {};
        if (settings.id === null) {
            settings.id = generateId(settings.title + settings.message + settings.color);
        }
        if (settings.displayMode == 1 || settings.displayMode == 'once') {
            try {
                if (document.querySelectorAll('.' + PLUGIN_NAME + '#' + settings.id).length > 0) {
                    return false;
                }
            } catch (exc) {
                console.warn('[' + PLUGIN_NAME + '] Could not find an element with this selector: ' + '#' + settings.id + '. Try to set an valid id.');
            }
        }
        if (settings.displayMode == 2 || settings.displayMode == 'replace') {
            try {
                forEach(document.querySelectorAll('.' + PLUGIN_NAME + '#' + settings.id), function (element, index) {
                    that.hide(settings, element, 'replaced');
                });
            } catch (exc) {
                console.warn('[' + PLUGIN_NAME + '] Could not find an element with this selector: ' + '#' + settings.id + '. Try to set an valid id.');
            }
        }
        settings.ref = new Date().getTime() + Math.floor((Math.random() * 10000000) + 1);
        $iziToast.children[settings.ref] = settings;
        var $DOM = {
            body: document.querySelector('body'),
            overlay: document.createElement('div'),
            toast: document.createElement('div'),
            toastBody: document.createElement('div'),
            toastTexts: document.createElement('div'),
            toastCapsule: document.createElement('div'),
            cover: document.createElement('div'),
            buttons: document.createElement('div'),
            inputs: document.createElement('div'),
            icon: !settings.iconUrl ? document.createElement('i') : document.createElement('img'),
            wrapper: null
        };
        $DOM.toast.setAttribute('data-iziToast-ref', settings.ref);
        $DOM.toast.appendChild($DOM.toastBody);
        $DOM.toastCapsule.appendChild($DOM.toast);
        // CSS Settings
        (function () {
            $DOM.toast.classList.add(PLUGIN_NAME);
            $DOM.toast.classList.add(PLUGIN_NAME + '-opening');
            $DOM.toastCapsule.classList.add(PLUGIN_NAME + '-capsule');
            $DOM.toastBody.classList.add(PLUGIN_NAME + '-body');
            $DOM.toastTexts.classList.add(PLUGIN_NAME + '-texts');
            if (ISMOBILE || window.innerWidth <= MOBILEWIDTH) {
                if (settings.transitionInMobile)
                    $DOM.toast.classList.add(settings.transitionInMobile);
            } else {
                if (settings.transitionIn)
                    $DOM.toast.classList.add(settings.transitionIn);
            }
            if (settings.className) {
                var classes = settings.className.split(' ');
                forEach(classes, function (value, index) {
                    $DOM.toast.classList.add(value);
                });
            }
            if (settings.id) {
                $DOM.toast.id = settings.id;
            }
            if (settings.rtl) {
                $DOM.toast.classList.add(PLUGIN_NAME + '-rtl');
                $DOM.toast.setAttribute('dir', 'rtl');
            }
            if (settings.layout > 1) {
                $DOM.toast.classList.add(PLUGIN_NAME + '-layout' + settings.layout);
            }
            if (settings.balloon) {
                $DOM.toast.classList.add(PLUGIN_NAME + '-balloon');
            }
            if (settings.maxWidth) {
                if (!isNaN(settings.maxWidth)) {
                    $DOM.toast.style.maxWidth = settings.maxWidth + 'px';
                } else {
                    $DOM.toast.style.maxWidth = settings.maxWidth;
                }
            }
            if (settings.theme !== '' || settings.theme !== 'light') {
                $DOM.toast.classList.add(PLUGIN_NAME + '-theme-' + settings.theme);
            }
            if (settings.color) { //#, rgb, rgba, hsl
                if (isColor(settings.color)) {
                    $DOM.toast.style.background = settings.color;
                } else {
                    $DOM.toast.classList.add(PLUGIN_NAME + '-color-' + settings.color);
                }
            }
            if (settings.backgroundColor) {
                $DOM.toast.style.background = settings.backgroundColor;
                if (settings.balloon) {
                    $DOM.toast.style.borderColor = settings.backgroundColor;
                }
            }
        })();
        // Cover image
        (function () {
            if (settings.image) {
                $DOM.cover.classList.add(PLUGIN_NAME + '-cover');
                $DOM.cover.style.width = settings.imageWidth + 'px';
                if (isBase64(settings.image.replace(/ /g, ''))) {
                    $DOM.cover.style.backgroundImage = 'url(data:image/png;base64,' + settings.image.replace(/ /g, '') + ')';
                } else {
                    $DOM.cover.style.backgroundImage = 'url(' + settings.image + ')';
                }
                if (settings.rtl) {
                    $DOM.toastBody.style.marginRight = (settings.imageWidth) + 'px';
                } else {
                    $DOM.toastBody.style.marginLeft = (settings.imageWidth) + 'px';
                }
                $DOM.toast.appendChild($DOM.cover);
            }
        })();
        // Button close
        (function () {
            if (settings.close) {
                $DOM.buttonClose = document.createElement('button');
                // $DOM.buttonClose.type = 'button';
                $DOM.buttonClose.setAttribute('type', 'button');
                $DOM.buttonClose.classList.add(PLUGIN_NAME + '-close');
                $DOM.buttonClose.addEventListener('click', function (e) {
                    var button = e.target;
                    that.hide(settings, $DOM.toast, 'button');
                });
                $DOM.toast.appendChild($DOM.buttonClose);
            } else {
                if (settings.rtl) {
                    $DOM.toast.style.paddingLeft = '18px';
                } else {
                    $DOM.toast.style.paddingRight = '18px';
                }
            }
        })();
        // Progress Bar & Timeout
        (function () {
            if (settings.progressBar) {
                $DOM.progressBar = document.createElement('div');
                $DOM.progressBarDiv = document.createElement('div');
                $DOM.progressBar.classList.add(PLUGIN_NAME + '-progressbar');
                $DOM.progressBarDiv.style.background = settings.progressBarColor;
                $DOM.progressBar.appendChild($DOM.progressBarDiv);
                $DOM.toast.appendChild($DOM.progressBar);
            }
            if (settings.timeout) {
                if (settings.pauseOnHover && !settings.resetOnHover) {
                    $DOM.toast.addEventListener('mouseenter', function (e) {
                        that.progress(settings, $DOM.toast).pause();
                    });
                    $DOM.toast.addEventListener('mouseleave', function (e) {
                        that.progress(settings, $DOM.toast).resume();
                    });
                }
                if (settings.resetOnHover) {
                    $DOM.toast.addEventListener('mouseenter', function (e) {
                        that.progress(settings, $DOM.toast).reset();
                    });
                    $DOM.toast.addEventListener('mouseleave', function (e) {
                        that.progress(settings, $DOM.toast).start();
                    });
                }
            }
        })();
        // Icon
        (function () {
            if (settings.iconUrl) {
                $DOM.icon.setAttribute('class', PLUGIN_NAME + '-icon');
                $DOM.icon.setAttribute('src', settings.iconUrl);
            } else if (settings.icon) {
                $DOM.icon.setAttribute('class', PLUGIN_NAME + '-icon ' + settings.icon);
                if (settings.iconText) {
                    $DOM.icon.appendChild(document.createTextNode(settings.iconText));
                }
                if (settings.iconColor) {
                    $DOM.icon.style.color = settings.iconColor;
                }
            }
            if (settings.icon || settings.iconUrl) {
                if (settings.rtl) {
                    $DOM.toastBody.style.paddingRight = '33px';
                } else {
                    $DOM.toastBody.style.paddingLeft = '33px';
                }
                $DOM.toastBody.appendChild($DOM.icon);
            }

        })();
        // Title & Message
        (function () {
            if (settings.title.length > 0) {
                $DOM.strong = document.createElement('strong');
                $DOM.strong.classList.add(PLUGIN_NAME + '-title');
                $DOM.strong.appendChild(createFragElem(settings.title));
                $DOM.toastTexts.appendChild($DOM.strong);
                if (settings.titleColor) {
                    $DOM.strong.style.color = settings.titleColor;
                }
                if (settings.titleSize) {
                    if (!isNaN(settings.titleSize)) {
                        $DOM.strong.style.fontSize = settings.titleSize + 'px';
                    } else {
                        $DOM.strong.style.fontSize = settings.titleSize;
                    }
                }
                if (settings.titleLineHeight) {
                    if (!isNaN(settings.titleSize)) {
                        $DOM.strong.style.lineHeight = settings.titleLineHeight + 'px';
                    } else {
                        $DOM.strong.style.lineHeight = settings.titleLineHeight;
                    }
                }
            }
            if (settings.message.length > 0) {
                $DOM.p = document.createElement('p');
                $DOM.p.classList.add(PLUGIN_NAME + '-message');
                $DOM.p.appendChild(createFragElem(settings.message));
                $DOM.toastTexts.appendChild($DOM.p);
                if (settings.messageColor) {
                    $DOM.p.style.color = settings.messageColor;
                }
                if (settings.messageSize) {
                    if (!isNaN(settings.titleSize)) {
                        $DOM.p.style.fontSize = settings.messageSize + 'px';
                    } else {
                        $DOM.p.style.fontSize = settings.messageSize;
                    }
                }
                if (settings.messageLineHeight) {
                    if (!isNaN(settings.titleSize)) {
                        $DOM.p.style.lineHeight = settings.messageLineHeight + 'px';
                    } else {
                        $DOM.p.style.lineHeight = settings.messageLineHeight;
                    }
                }
            }
            if (settings.title.length > 0 && settings.message.length > 0) {
                if (settings.rtl) {
                    $DOM.strong.style.marginLeft = '10px';
                } else if (settings.layout != 2 && !settings.rtl) {
                    $DOM.strong.style.marginRight = '10px';
                    $DOM.strong.style.marginBottom = '0px';
                }
            }
        })();
        $DOM.toastBody.appendChild($DOM.toastTexts);
        // Inputs
        var $inputs;
        (function () {
            if (settings.inputs.length > 0) {
                $DOM.inputs.classList.add(PLUGIN_NAME + '-inputs');
                forEach(settings.inputs, function (value, index) {
                    $DOM.inputs.appendChild(createFragElem(value[0]));
                    $inputs = $DOM.inputs.childNodes;
                    $inputs[index].classList.add(PLUGIN_NAME + '-inputs-child');
                    if (value[3]) {
                        setTimeout(function () {
                            $inputs[index].focus();
                        }, 300);
                    }
                    $inputs[index].addEventListener(value[1], function (e) {
                        var ts = value[2];
                        return ts(that, $DOM.toast, this, e);
                    });
                });
                $DOM.toastBody.appendChild($DOM.inputs);
            }
        })();
        // Buttons
        (function () {
            if (settings.buttons.length > 0) {
                $DOM.buttons.classList.add(PLUGIN_NAME + '-buttons');
                forEach(settings.buttons, function (value, index) {
                    $DOM.buttons.appendChild(createFragElem(value[0]));
                    var $btns = $DOM.buttons.childNodes;
                    $btns[index].classList.add(PLUGIN_NAME + '-buttons-child');
                    if (value[2]) {
                        setTimeout(function () {
                            $btns[index].focus();
                        }, 300);
                    }
                    $btns[index].addEventListener('click', function (e) {
                        e.preventDefault();
                        var ts = value[1];
                        return ts(that, $DOM.toast, this, e, $inputs);
                    });
                });
            }
            $DOM.toastTexts.appendChild($DOM.buttons);
        })();
        if (settings.message.length > 0 && (settings.inputs.length > 0 || settings.buttons.length > 0)) {
            $DOM.p.style.marginBottom = '0';
        }
        if (settings.inputs.length > 0 || settings.buttons.length > 0) {
            if (settings.rtl) {
                $DOM.toastTexts.style.marginLeft = '10px';
            } else {
                $DOM.toastTexts.style.marginRight = '10px';
            }
            if (settings.inputs.length > 0 && settings.buttons.length > 0) {
                if (settings.rtl) {
                    $DOM.inputs.style.marginLeft = '8px';
                } else {
                    $DOM.inputs.style.marginRight = '8px';
                }
            }
        }
        // Wrap
        (function () {
            $DOM.toastCapsule.style.visibility = 'hidden';
            setTimeout(function () {
                var H = $DOM.toast.offsetHeight;
                var style = $DOM.toast.currentStyle || window.getComputedStyle($DOM.toast);
                var marginTop = style.marginTop;
                marginTop = marginTop.split('px');
                marginTop = parseInt(marginTop[0]);
                var marginBottom = style.marginBottom;
                marginBottom = marginBottom.split('px');
                marginBottom = parseInt(marginBottom[0]);

                $DOM.toastCapsule.style.visibility = '';
                $DOM.toastCapsule.style.height = (H + marginBottom + marginTop) + 'px';

                setTimeout(function () {
                    $DOM.toastCapsule.style.height = 'auto';
                    if (settings.target) {
                        $DOM.toastCapsule.style.overflow = 'visible';
                    }
                }, 500);

                if (settings.timeout) {
                    that.progress(settings, $DOM.toast).start();
                }
            }, 100);
        })();
        // Target
        (function () {
            var position = settings.position;
            if (settings.target) {
                $DOM.wrapper = document.querySelector(settings.target);
                $DOM.wrapper.classList.add(PLUGIN_NAME + '-target');
                if (settings.targetFirst) {
                    $DOM.wrapper.insertBefore($DOM.toastCapsule, $DOM.wrapper.firstChild);
                } else {
                    $DOM.wrapper.appendChild($DOM.toastCapsule);
                }
            } else {
                if (POSITIONS.indexOf(settings.position) == -1) {
                    console.warn('[' + PLUGIN_NAME + '] Incorrect position.\nIt can be › ' + POSITIONS);
                    return;
                }
                if (ISMOBILE || window.innerWidth <= MOBILEWIDTH) {
                    if (settings.position == 'bottomLeft' || settings.position == 'bottomRight' || settings.position == 'bottomCenter') {
                        position = PLUGIN_NAME + '-wrapper-bottomCenter';
                    } else if (settings.position == 'topLeft' || settings.position == 'topRight' || settings.position == 'topCenter') {
                        position = PLUGIN_NAME + '-wrapper-topCenter';
                    } else {
                        position = PLUGIN_NAME + '-wrapper-center';
                    }
                } else {
                    position = PLUGIN_NAME + '-wrapper-' + position;
                }
                $DOM.wrapper = document.querySelector('.' + PLUGIN_NAME + '-wrapper.' + position);
                if (!$DOM.wrapper) {
                    $DOM.wrapper = document.createElement('div');
                    $DOM.wrapper.classList.add(PLUGIN_NAME + '-wrapper');
                    $DOM.wrapper.classList.add(position);
                    document.body.appendChild($DOM.wrapper);
                }
                var targetFirst = settings.targetFirst;
                if ((targetFirst == undefined || targetFirst == null) && (settings.position == 'topLeft' || settings.position == 'topCenter' || settings.position == 'topRight')) {
                    targetFirst = true;
                }
                if (targetFirst) {
                    $DOM.wrapper.insertBefore($DOM.toastCapsule, $DOM.wrapper.firstChild);
                } else {
                    $DOM.wrapper.appendChild($DOM.toastCapsule);
                }
            }
            if (!isNaN(settings.zindex)) {
                $DOM.wrapper.style.zIndex = settings.zindex;
            } else {
                console.warn('[' + PLUGIN_NAME + '] Invalid zIndex.');
            }
        })();
        // Overlay
        (function () {
            if (settings.overlay) {
                if (document.querySelector('.' + PLUGIN_NAME + '-overlay.fadeIn') !== null) {
                    $DOM.overlay = document.querySelector('.' + PLUGIN_NAME + '-overlay');
                    $DOM.overlay.setAttribute('data-iziToast-ref', $DOM.overlay.getAttribute('data-iziToast-ref') + ',' + settings.ref);
                    if (!isNaN(settings.zindex) && settings.zindex !== null) {
                        $DOM.overlay.style.zIndex = settings.zindex - 1;
                    }
                } else {
                    $DOM.overlay.classList.add(PLUGIN_NAME + '-overlay');
                    $DOM.overlay.classList.add('fadeIn');
                    $DOM.overlay.style.background = settings.overlayColor;
                    $DOM.overlay.setAttribute('data-iziToast-ref', settings.ref);
                    if (!isNaN(settings.zindex) && settings.zindex !== null) {
                        $DOM.overlay.style.zIndex = settings.zindex - 1;
                    }
                    document.querySelector('body').appendChild($DOM.overlay);
                }
                if (settings.overlayClose) {
                    $DOM.overlay.removeEventListener('click', {});
                    $DOM.overlay.addEventListener('click', function (e) {
                        that.hide(settings, $DOM.toast, 'overlay');
                    });
                } else {
                    $DOM.overlay.removeEventListener('click', {});
                }
            }
        })();
        // Inside animations
        (function () {
            if (settings.animateInside) {
                $DOM.toast.classList.add(PLUGIN_NAME + '-animateInside');
                var animationTimes = [200, 100, 300];
                if (settings.transitionIn == 'bounceInLeft' || settings.transitionIn == 'bounceInRight') {
                    animationTimes = [400, 200, 400];
                }
                if (settings.title.length > 0) {
                    setTimeout(function () {
                        $DOM.strong.classList.add('slideIn');
                    }, animationTimes[0]);
                }
                if (settings.message.length > 0) {
                    setTimeout(function () {
                        $DOM.p.classList.add('slideIn');
                    }, animationTimes[1]);
                }
                if (settings.icon || settings.iconUrl) {
                    setTimeout(function () {
                        $DOM.icon.classList.add('revealIn');
                    }, animationTimes[2]);
                }
                var counter = 150;
                if (settings.buttons.length > 0 && $DOM.buttons) {
                    setTimeout(function () {
                        forEach($DOM.buttons.childNodes, function (element, index) {
                            setTimeout(function () {
                                element.classList.add('revealIn');
                            }, counter);
                            counter = counter + 150;
                        });
                    }, settings.inputs.length > 0 ? 150 : 0);
                }
                if (settings.inputs.length > 0 && $DOM.inputs) {
                    counter = 150;
                    forEach($DOM.inputs.childNodes, function (element, index) {
                        setTimeout(function () {
                            element.classList.add('revealIn');
                        }, counter);
                        counter = counter + 150;
                    });
                }
            }
        })();
        settings.onOpening.apply(null, [settings, $DOM.toast]);
        try {
            var event = new CustomEvent(PLUGIN_NAME + '-opening', {detail: settings, bubbles: true, cancelable: true});
            document.dispatchEvent(event);
        } catch (ex) {
            console.warn(ex);
        }
        setTimeout(function () {
            $DOM.toast.classList.remove(PLUGIN_NAME + '-opening');
            $DOM.toast.classList.add(PLUGIN_NAME + '-opened');
            try {
                var event = new CustomEvent(PLUGIN_NAME + '-opened', {
                    detail: settings,
                    bubbles: true,
                    cancelable: true
                });
                document.dispatchEvent(event);
            } catch (ex) {
                console.warn(ex);
            }
            settings.onOpened.apply(null, [settings, $DOM.toast]);
        }, 1000);
        if (settings.drag) {
            if (ACCEPTSTOUCH) {
                $DOM.toast.addEventListener('touchstart', function (e) {
                    drag.startMoving(this, that, settings, e);
                }, false);
                $DOM.toast.addEventListener('touchend', function (e) {
                    drag.stopMoving(this, e);
                }, false);
            } else {
                $DOM.toast.addEventListener('mousedown', function (e) {
                    e.preventDefault();
                    drag.startMoving(this, that, settings, e);
                }, false);
                $DOM.toast.addEventListener('mouseup', function (e) {
                    e.preventDefault();
                    drag.stopMoving(this, e);
                }, false);
            }
        }
        if (settings.closeOnEscape) {
            document.addEventListener('keyup', function (evt) {
                evt = evt || window.event;
                if (evt.keyCode == 27) {
                    that.hide(settings, $DOM.toast, 'esc');
                }
            });
        }
        if (settings.closeOnClick) {
            $DOM.toast.addEventListener('click', function (evt) {
                that.hide(settings, $DOM.toast, 'toast');
            });
        }
        // 播放声音
        if (settings.audio) {
            that.playSound(settings.audio);
        }
        that.toast = $DOM.toast;
    };

    // 控制进度条
    $iziToast.progress = function (options, $toast, callback) {
        var that = this,
            ref = $toast.getAttribute('data-iziToast-ref'),
            settings = extend(this.children[ref], options || {}),
            $elem = $toast.querySelector('.' + PLUGIN_NAME + '-progressbar div');
        return {
            start: function () {
                if (typeof settings.time.REMAINING == 'undefined') {
                    $toast.classList.remove(PLUGIN_NAME + '-reseted');
                    if ($elem !== null) {
                        $elem.style.transition = 'width ' + settings.timeout + 'ms ' + settings.progressBarEasing;
                        $elem.style.width = '0%';
                    }
                    settings.time.START = new Date().getTime();
                    settings.time.END = settings.time.START + settings.timeout;
                    settings.time.TIMER = setTimeout(function () {
                        clearTimeout(settings.time.TIMER);
                        if (!$toast.classList.contains(PLUGIN_NAME + '-closing')) {
                            that.hide(settings, $toast, 'timeout');
                            if (typeof callback === 'function') {
                                callback.apply(that);
                            }
                        }
                    }, settings.timeout);
                    that.setSetting(ref, 'time', settings.time);
                }
            },
            pause: function () {
                if (typeof settings.time.START !== 'undefined' && !$toast.classList.contains(PLUGIN_NAME + '-paused') && !$toast.classList.contains(PLUGIN_NAME + '-reseted')) {
                    $toast.classList.add(PLUGIN_NAME + '-paused');
                    settings.time.REMAINING = settings.time.END - new Date().getTime();
                    clearTimeout(settings.time.TIMER);
                    that.setSetting(ref, 'time', settings.time);
                    if ($elem !== null) {
                        var computedStyle = window.getComputedStyle($elem),
                            propertyWidth = computedStyle.getPropertyValue('width');
                        $elem.style.transition = 'none';
                        $elem.style.width = propertyWidth;
                    }
                    if (typeof callback === 'function') {
                        setTimeout(function () {
                            callback.apply(that);
                        }, 10);
                    }
                }
            },
            resume: function () {
                if (typeof settings.time.REMAINING !== 'undefined') {
                    $toast.classList.remove(PLUGIN_NAME + '-paused');
                    if ($elem !== null) {
                        $elem.style.transition = 'width ' + settings.time.REMAINING + 'ms ' + settings.progressBarEasing;
                        $elem.style.width = '0%';
                    }
                    settings.time.END = new Date().getTime() + settings.time.REMAINING;
                    settings.time.TIMER = setTimeout(function () {
                        clearTimeout(settings.time.TIMER);
                        if (!$toast.classList.contains(PLUGIN_NAME + '-closing')) {
                            that.hide(settings, $toast, 'timeout');
                            if (typeof callback === 'function') {
                                callback.apply(that);
                            }
                        }
                    }, settings.time.REMAINING);
                    that.setSetting(ref, 'time', settings.time);
                } else {
                    this.start();
                }
            },
            reset: function () {
                clearTimeout(settings.time.TIMER);
                delete settings.time.REMAINING;
                that.setSetting(ref, 'time', settings.time);
                $toast.classList.add(PLUGIN_NAME + '-reseted');
                $toast.classList.remove(PLUGIN_NAME + '-paused');
                if ($elem !== null) {
                    $elem.style.transition = 'none';
                    $elem.style.width = '100%';
                }
                if (typeof callback === 'function') {
                    setTimeout(function () {
                        callback.apply(that);
                    }, 10);
                }
            }
        };
    };

    // 判断是否是ie9以下版本
    var isIE9_ = function () {
        var userAgent = navigator.userAgent;
        if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if (fIEVersion != 10) {
                return true;
            }
        }
        return false;
    };

    // 给Element添加remove方法
    if (!('remove' in Element.prototype)) {
        Element.prototype.remove = function () {
            if (this.parentNode) {
                this.parentNode.removeChild(this);
            }
        };
    }

    // 自定义事件
    if (typeof window.CustomEvent !== 'function') {
        var CustomEventPolyfill = function (event, params) {
            params = params || {bubbles: false, cancelable: false, detail: undefined};
            var evt = document.createEvent('CustomEvent');
            evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
            return evt;
        };
        CustomEventPolyfill.prototype = window.Event.prototype;
        window.CustomEvent = CustomEventPolyfill;
    }

    // 遍历数据
    var forEach = function (collection, callback, scope) {
        if (Object.prototype.toString.call(collection) === '[object Object]') {
            for (var prop in collection) {
                if (Object.prototype.hasOwnProperty.call(collection, prop)) {
                    callback.call(scope, collection[prop], prop, collection);
                }
            }
        } else {
            if (collection) {
                for (var i = 0, len = collection.length; i < len; i++) {
                    callback.call(scope, collection[i], i, collection);
                }
            }
        }
    };

    // 合并自定义参数和默认参数
    var extend = function (defaults, options) {
        var extended = {};
        forEach(defaults, function (value, prop) {
            extended[prop] = defaults[prop];
        });
        forEach(options, function (value, prop) {
            extended[prop] = options[prop];
        });
        return extended;
    };

    // 创建新的文档片段
    var createFragElem = function (htmlStr) {
        var frag = document.createDocumentFragment(),
            temp = document.createElement('div');
        temp.innerHTML = htmlStr;
        while (temp.firstChild) {
            frag.appendChild(temp.firstChild);
        }
        return frag;
    };

    // 生成ID
    var generateId = function (params) {
        var newId = btoa(encodeURIComponent(params));
        return newId.replace(/=/g, "");
    };

    // 判断是否是颜色字符串
    var isColor = function (color) {
        if (color.substring(0, 1) == '#' || color.substring(0, 3) == 'rgb' || color.substring(0, 3) == 'hsl') {
            return true;
        } else {
            return false;
        }
    };

    // 判断是否是base64字符串
    var isBase64 = function (str) {
        try {
            return btoa(atob(str)) == str;
        } catch (err) {
            return false;
        }
    };

    // 拖拽方法
    var drag = function () {
        return {
            move: function (toast, instance, settings, xpos) {
                var opacity,
                    opacityRange = 0.3,
                    distance = 180;
                if (xpos !== 0) {
                    toast.classList.add(PLUGIN_NAME + '-dragged');
                    toast.style.transform = 'translateX(' + xpos + 'px)';
                    if (xpos > 0) {
                        opacity = (distance - xpos) / distance;
                        if (opacity < opacityRange) {
                            instance.hide(extend(settings, {
                                transitionOut: 'fadeOutRight',
                                transitionOutMobile: 'fadeOutRight'
                            }), toast, 'drag');
                        }
                    } else {
                        opacity = (distance + xpos) / distance;
                        if (opacity < opacityRange) {
                            instance.hide(extend(settings, {
                                transitionOut: 'fadeOutLeft',
                                transitionOutMobile: 'fadeOutLeft'
                            }), toast, 'drag');
                        }
                    }
                    toast.style.opacity = opacity;
                    if (opacity < opacityRange) {
                        if (ISCHROME || ISFIREFOX)
                            toast.style.left = xpos + 'px';
                        toast.parentNode.style.opacity = opacityRange;
                        this.stopMoving(toast, null);
                    }
                }
            },
            startMoving: function (toast, instance, settings, e) {
                e = e || window.event;
                var posX = ((ACCEPTSTOUCH) ? e.touches[0].clientX : e.clientX),
                    toastLeft = toast.style.transform.replace('px)', '');
                toastLeft = toastLeft.replace('translateX(', '');
                var offsetX = posX - toastLeft;
                if (settings.transitionIn) {
                    toast.classList.remove(settings.transitionIn);
                }
                if (settings.transitionInMobile) {
                    toast.classList.remove(settings.transitionInMobile);
                }
                toast.style.transition = '';
                if (ACCEPTSTOUCH) {
                    document.ontouchmove = function (e) {
                        e.preventDefault();
                        e = e || window.event;
                        var posX = e.touches[0].clientX,
                            finalX = posX - offsetX;
                        drag.move(toast, instance, settings, finalX);
                    };
                } else {
                    document.onmousemove = function (e) {
                        e.preventDefault();
                        e = e || window.event;
                        var posX = e.clientX,
                            finalX = posX - offsetX;
                        drag.move(toast, instance, settings, finalX);
                    };
                }
            },
            stopMoving: function (toast, e) {
                if (ACCEPTSTOUCH) {
                    document.ontouchmove = function () {
                    };
                } else {
                    document.onmousemove = function () {
                    };
                }
                toast.style.opacity = '';
                toast.style.transform = '';
                if (toast.classList.contains(PLUGIN_NAME + '-dragged')) {
                    toast.classList.remove(PLUGIN_NAME + '-dragged');
                    toast.style.transition = 'transform 0.4s ease, opacity 0.4s ease';
                    setTimeout(function () {
                        toast.style.transition = '';
                    }, 400);
                }
            }
        };
    }();

    // 兼容IE
    var Base64 = {
        _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", encode: function (e) {
            var t = "";
            var n, r, i, s, o, u, a;
            var f = 0;
            e = Base64._utf8_encode(e);
            while (f < e.length) {
                n = e.charCodeAt(f++);
                r = e.charCodeAt(f++);
                i = e.charCodeAt(f++);
                s = n >> 2;
                o = (n & 3) << 4 | r >> 4;
                u = (r & 15) << 2 | i >> 6;
                a = i & 63;
                if (isNaN(r)) {
                    u = a = 64
                } else if (isNaN(i)) {
                    a = 64
                }
                t = t + this._keyStr.charAt(s) + this._keyStr.charAt(o) + this._keyStr.charAt(u) + this._keyStr.charAt(a)
            }
            return t
        }, decode: function (e) {
            var t = "";
            var n, r, i;
            var s, o, u, a;
            var f = 0;
            e = e.replace(/[^A-Za-z0-9+/=]/g, "");
            while (f < e.length) {
                s = this._keyStr.indexOf(e.charAt(f++));
                o = this._keyStr.indexOf(e.charAt(f++));
                u = this._keyStr.indexOf(e.charAt(f++));
                a = this._keyStr.indexOf(e.charAt(f++));
                n = s << 2 | o >> 4;
                r = (o & 15) << 4 | u >> 2;
                i = (u & 3) << 6 | a;
                t = t + String.fromCharCode(n);
                if (u != 64) {
                    t = t + String.fromCharCode(r)
                }
                if (a != 64) {
                    t = t + String.fromCharCode(i)
                }
            }
            t = Base64._utf8_decode(t);
            return t
        }, _utf8_encode: function (e) {
            e = e.replace(/rn/g, "n");
            var t = "";
            for (var n = 0; n < e.length; n++) {
                var r = e.charCodeAt(n);
                if (r < 128) {
                    t += String.fromCharCode(r)
                } else if (r > 127 && r < 2048) {
                    t += String.fromCharCode(r >> 6 | 192);
                    t += String.fromCharCode(r & 63 | 128)
                } else {
                    t += String.fromCharCode(r >> 12 | 224);
                    t += String.fromCharCode(r >> 6 & 63 | 128);
                    t += String.fromCharCode(r & 63 | 128)
                }
            }
            return t
        }, _utf8_decode: function (e) {
            var t = "";
            var n = 0;
            var r = c1 = c2 = 0;
            while (n < e.length) {
                r = e.charCodeAt(n);
                if (r < 128) {
                    t += String.fromCharCode(r);
                    n++
                } else if (r > 191 && r < 224) {
                    c2 = e.charCodeAt(n + 1);
                    t += String.fromCharCode((r & 31) << 6 | c2 & 63);
                    n += 2
                } else {
                    c2 = e.charCodeAt(n + 1);
                    c3 = e.charCodeAt(n + 2);
                    t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
                    n += 3
                }
            }
            return t
        }
    };
    if (isIE9_()) {
        // 兼容btoa和atob方法
        window.btoa = function (str) {
            return Base64.encode(str);
        };
        window.atob = function (str) {
            return Base64.decode(str);
        };
        // 兼容classList属性
        if (!("classList" in document.documentElement)) {
            Object.defineProperty(window.Element.prototype, 'classList', {
                get: function () {
                    var self = this;

                    function update(fn) {
                        return function () {
                            var className = self.className.replace(/^\s+|\s+$/g, ''),
                                valArr = arguments;
                            return fn(className, valArr)
                        }
                    }

                    function add_rmv(className, valArr, tag) {
                        for (var i in valArr) {
                            if (typeof valArr[i] !== 'string' || !!~valArr[i].search(/\s+/g)) throw TypeError('the type of value is error')
                            var temp = valArr[i]
                            var flag = !!~className.search(new RegExp('(\\s+)?' + temp + '(\\s+)?'))
                            if (tag === 1) {
                                !flag ? className += ' ' + temp : ''
                            } else if (tag === 2) {
                                flag ? className = className.replace(new RegExp('(\\s+)?' + temp), '') : ''
                            }
                        }
                        self.className = className;
                        return tag;
                    }

                    return {
                        add: update(function (className, valArr) {
                            add_rmv(className, valArr, 1)
                        }),
                        remove: update(function (className, valArr) {
                            add_rmv(className, valArr, 2)
                        }),
                        toggle: function (value) {
                            if (typeof value !== 'string' || arguments.length === 0) throw TypeError("Failed to execute 'toggle' on 'DOMTokenList': 1 argument(string) required, but only 0 present.")
                            if (arguments.length === 1) {
                                this.contains(value) ? this.remove(value) : this.add(value)
                                return
                            }
                            !arguments[1] ? this.remove(value) : this.add(value)
                        },
                        contains: update(function (className, valArr) {
                            if (valArr.length === 0) throw TypeError("Failed to execute 'contains' on 'DOMTokenList': 1 argument required, but only 0 present.")
                            if (typeof valArr[0] !== 'string' || !!~valArr[0].search(/\s+/g)) return false
                            return !!~className.search(new RegExp(valArr[0]))
                        }),
                        item: function (index) {
                            typeof index === 'string' ? index = parseInt(index) : ''
                            if (arguments.length === 0 || typeof index !== 'number') throw TypeError("Failed to execute 'toggle' on 'DOMTokenList': 1 argument required, but only 0 present.")
                            var claArr = self.className.replace(/^\s+|\s+$/, '').split(/\s+/)
                            var len = claArr.length
                            if (index < 0 || index >= len) return null
                            return claArr[index]
                        }
                    }
                }
            });
        }
    }

    // 播放声音
    $iziToast.playSound = function (src) {
        if (!(src.indexOf('http') == 0)) {
            src = layui.cache.base + 'notice/' + src + '.wav';
        }
        if (!!window.ActiveXObject || "ActiveXObject" in window) {  // IE
            var embed = document.noticePlay;
            if (embed) {
                embed.remove();
            }
            embed = document.createElement('embed');
            embed.setAttribute('name', 'noticePlay');
            embed.setAttribute('src', src);
            embed.setAttribute('autostart', true);
            embed.setAttribute('loop', false);
            embed.setAttribute('hidden', true);
            document.body.appendChild(embed);
            embed = document.noticePlay;
            embed.volume = 100;
        } else {   // 非IE
            var audio = document.createElement('audio');
            audio.setAttribute('hidden', true);
            audio.setAttribute('src', src);
            document.body.appendChild(audio);
            audio.addEventListener('ended', function () {
                audio.parentNode.removeChild(audio);
            }, false);
            audio.play();
        }
    };

    // 不同主题的通知
    forEach(THEMES, function (theme, name) {
        $iziToast[name] = function (options) {
            var settings = extend(CONFIG, options || {});
            settings = extend(theme, settings || {});
            this.show(settings);
        };
    });

    layui.link(layui.cache.base + 'notice/notice.css');  // 加载css
    exports('notice', $iziToast);
});
