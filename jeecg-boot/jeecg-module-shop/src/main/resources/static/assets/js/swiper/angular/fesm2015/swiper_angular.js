import * as i0 from '@angular/core';
import { Directive, Input, EventEmitter, PLATFORM_ID, Component, ChangeDetectionStrategy, ViewEncapsulation, Inject, Output, ViewChild, ContentChildren, HostBinding, NgModule } from '@angular/core';
import * as i1 from '@angular/common';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import Swiper from 'swiper';
import { Subject, of } from 'rxjs';

function isObject(o) {
    return (typeof o === 'object' &&
        o !== null &&
        o.constructor &&
        Object.prototype.toString.call(o).slice(8, -1) === 'Object');
}
function isShowEl(val, obj, el) {
    return ((coerceBooleanProperty(val) === true && obj && !obj.el) ||
        !(typeof obj !== 'boolean' &&
            obj.el !== (el === null || el === void 0 ? void 0 : el.nativeElement) &&
            (typeof obj.el === 'string' || typeof obj.el === 'object')));
}
function extend(target, src) {
    const noExtend = ['__proto__', 'constructor', 'prototype'];
    Object.keys(src)
        .filter((key) => noExtend.indexOf(key) < 0)
        .forEach((key) => {
        if (typeof target[key] === 'undefined') {
            target[key] = src[key];
            return;
        }
        if (target[key] && !src[key]) {
            return;
        }
        if (isObject(src[key]) && isObject(target[key]) && Object.keys(src[key]).length > 0) {
            if (src[key].__swiper__)
                target[key] = src[key];
            else
                extend(target[key], src[key]);
        }
        else {
            target[key] = src[key];
        }
    });
}
function coerceBooleanProperty(value) {
    return value != null && `${value}` !== 'false';
}
const ignoreNgOnChanges = ['pagination', 'navigation', 'scrollbar', 'virtual'];
function setProperty(val, obj = {}) {
    if (isObject(val)) {
        return val;
    }
    if (coerceBooleanProperty(val) === true) {
        return obj;
    }
    return false;
}

/* underscore in name -> watch for changes */
const paramsList = [
    'init',
    'enabled',
    '_direction',
    'touchEventsTarget',
    'initialSlide',
    '_speed',
    'cssMode',
    'updateOnWindowResize',
    'resizeObserver',
    'nested',
    'focusableElements',
    '_width',
    '_height',
    'preventInteractionOnTransition',
    'userAgent',
    'url',
    '_edgeSwipeDetection',
    '_edgeSwipeThreshold',
    '_freeMode',
    '_autoHeight',
    'setWrapperSize',
    'virtualTranslate',
    '_effect',
    'breakpoints',
    '_spaceBetween',
    '_slidesPerView',
    '_grid',
    '_slidesPerGroup',
    '_slidesPerGroupSkip',
    '_centeredSlides',
    '_centeredSlidesBounds',
    '_slidesOffsetBefore',
    '_slidesOffsetAfter',
    'normalizeSlideIndex',
    '_centerInsufficientSlides',
    '_watchOverflow',
    'roundLengths',
    'touchRatio',
    'touchAngle',
    'simulateTouch',
    '_shortSwipes',
    '_longSwipes',
    'longSwipesRatio',
    'longSwipesMs',
    '_followFinger',
    'allowTouchMove',
    '_threshold',
    'touchMoveStopPropagation',
    'touchStartPreventDefault',
    'touchStartForcePreventDefault',
    'touchReleaseOnEdges',
    'uniqueNavElements',
    '_resistance',
    '_resistanceRatio',
    '_watchSlidesProgress',
    '_grabCursor',
    'preventClicks',
    'preventClicksPropagation',
    '_slideToClickedSlide',
    '_preloadImages',
    'updateOnImagesReady',
    '_loop',
    '_loopAdditionalSlides',
    '_loopedSlides',
    '_loopFillGroupWithBlank',
    'loopPreventsSlide',
    '_allowSlidePrev',
    '_allowSlideNext',
    '_swipeHandler',
    '_noSwiping',
    'noSwipingClass',
    'noSwipingSelector',
    'passiveListeners',
    'containerModifierClass',
    'slideClass',
    'slideBlankClass',
    'slideActiveClass',
    'slideDuplicateActiveClass',
    'slideVisibleClass',
    'slideDuplicateClass',
    'slideNextClass',
    'slideDuplicateNextClass',
    'slidePrevClass',
    'slideDuplicatePrevClass',
    'wrapperClass',
    'runCallbacksOnInit',
    'observer',
    'observeParents',
    'observeSlideChildren',
    // modules
    'a11y',
    'autoplay',
    '_controller',
    'coverflowEffect',
    'cubeEffect',
    'fadeEffect',
    'flipEffect',
    'creativeEffect',
    'cardsEffect',
    'hashNavigation',
    'history',
    'keyboard',
    'lazy',
    'mousewheel',
    '_navigation',
    '_pagination',
    'parallax',
    '_scrollbar',
    '_thumbs',
    'virtual',
    'zoom',
];

// eslint-disable-next-line
const allowedParams = paramsList.map((key) => key.replace(/_/, ''));
function getParams(obj = {}) {
    const params = {
        on: {},
    };
    const passedParams = {};
    extend(params, Swiper.defaults);
    extend(params, Swiper.extendedDefaults);
    params._emitClasses = true;
    const rest = {};
    Object.keys(obj).forEach((key) => {
        const _key = key.replace(/^_/, '');
        if (typeof obj[_key] === 'undefined')
            return;
        if (allowedParams.indexOf(_key) >= 0) {
            if (isObject(obj[_key])) {
                params[_key] = {};
                passedParams[_key] = {};
                extend(params[_key], obj[_key]);
                extend(passedParams[_key], obj[_key]);
            }
            else {
                params[_key] = obj[_key];
                passedParams[_key] = obj[_key];
            }
        }
        else {
            rest[_key] = obj[_key];
        }
    });
    return { params, passedParams, rest };
}

class SwiperSlideDirective {
    constructor(template) {
        this.template = template;
        this.class = '';
        this.autoplayDelay = null;
        this.slideData = {
            isActive: false,
            isPrev: false,
            isNext: false,
            isVisible: false,
            isDuplicate: false,
        };
    }
    set zoom(val) {
        this._zoom = coerceBooleanProperty(val);
    }
    get zoom() {
        return this._zoom;
    }
    get classNames() {
        return this._classNames;
    }
    set classNames(val) {
        if (this._classNames === val) {
            return;
        }
        this._classNames = val;
        this.slideData = {
            isActive: this._hasClass(['swiper-slide-active', 'swiper-slide-duplicate-active']),
            isVisible: this._hasClass(['swiper-slide-visible']),
            isDuplicate: this._hasClass(['swiper-slide-duplicate']),
            isPrev: this._hasClass(['swiper-slide-prev', 'swiper-slide-duplicate-prev']),
            isNext: this._hasClass(['swiper-slide-next', 'swiper-slide-duplicate-next']),
        };
    }
    _hasClass(classNames) {
        return classNames.some((className) => this._classNames.indexOf(className) >= 0);
    }
}
SwiperSlideDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperSlideDirective, deps: [{ token: i0.TemplateRef }], target: i0.ɵɵFactoryTarget.Directive });
SwiperSlideDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "12.2.2", type: SwiperSlideDirective, selector: "ng-template[swiperSlide]", inputs: { virtualIndex: "virtualIndex", class: "class", autoplayDelay: ["data-swiper-autoplay", "autoplayDelay"], zoom: "zoom" }, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperSlideDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'ng-template[swiperSlide]',
                }]
        }], ctorParameters: function () { return [{ type: i0.TemplateRef }]; }, propDecorators: { virtualIndex: [{
                type: Input
            }], class: [{
                type: Input
            }], autoplayDelay: [{
                type: Input,
                args: ['data-swiper-autoplay']
            }], zoom: [{
                type: Input
            }] } });

class SwiperComponent {
    constructor(_ngZone, elementRef, _changeDetectorRef, _platformId) {
        this._ngZone = _ngZone;
        this.elementRef = elementRef;
        this._changeDetectorRef = _changeDetectorRef;
        this._platformId = _platformId;
        this.slideClass = 'swiper-slide';
        this.wrapperClass = 'swiper-wrapper';
        this.showNavigation = true;
        this.showPagination = true;
        this.showScrollbar = true;
        // prettier-ignore
        this.s__beforeBreakpoint = new EventEmitter();
        // prettier-ignore
        this.s__containerClasses = new EventEmitter();
        // prettier-ignore
        this.s__slideClass = new EventEmitter();
        // prettier-ignore
        this.s__swiper = new EventEmitter();
        // prettier-ignore
        this.s_activeIndexChange = new EventEmitter();
        // prettier-ignore
        this.s_afterInit = new EventEmitter();
        // prettier-ignore
        this.s_autoplay = new EventEmitter();
        // prettier-ignore
        this.s_autoplayStart = new EventEmitter();
        // prettier-ignore
        this.s_autoplayStop = new EventEmitter();
        // prettier-ignore
        this.s_beforeDestroy = new EventEmitter();
        // prettier-ignore
        this.s_beforeInit = new EventEmitter();
        // prettier-ignore
        this.s_beforeLoopFix = new EventEmitter();
        // prettier-ignore
        this.s_beforeResize = new EventEmitter();
        // prettier-ignore
        this.s_beforeSlideChangeStart = new EventEmitter();
        // prettier-ignore
        this.s_beforeTransitionStart = new EventEmitter();
        // prettier-ignore
        this.s_breakpoint = new EventEmitter();
        // prettier-ignore
        this.s_changeDirection = new EventEmitter();
        // prettier-ignore
        this.s_click = new EventEmitter();
        // prettier-ignore
        this.s_doubleTap = new EventEmitter();
        // prettier-ignore
        this.s_doubleClick = new EventEmitter();
        // prettier-ignore
        this.s_destroy = new EventEmitter();
        // prettier-ignore
        this.s_fromEdge = new EventEmitter();
        // prettier-ignore
        this.s_hashChange = new EventEmitter();
        // prettier-ignore
        this.s_hashSet = new EventEmitter();
        // prettier-ignore
        this.s_imagesReady = new EventEmitter();
        // prettier-ignore
        this.s_init = new EventEmitter();
        // prettier-ignore
        this.s_keyPress = new EventEmitter();
        // prettier-ignore
        this.s_lazyImageLoad = new EventEmitter();
        // prettier-ignore
        this.s_lazyImageReady = new EventEmitter();
        // prettier-ignore
        this.s_loopFix = new EventEmitter();
        // prettier-ignore
        this.s_momentumBounce = new EventEmitter();
        // prettier-ignore
        this.s_navigationHide = new EventEmitter();
        // prettier-ignore
        this.s_navigationShow = new EventEmitter();
        // prettier-ignore
        this.s_observerUpdate = new EventEmitter();
        // prettier-ignore
        this.s_orientationchange = new EventEmitter();
        // prettier-ignore
        this.s_paginationHide = new EventEmitter();
        // prettier-ignore
        this.s_paginationRender = new EventEmitter();
        // prettier-ignore
        this.s_paginationShow = new EventEmitter();
        // prettier-ignore
        this.s_paginationUpdate = new EventEmitter();
        // prettier-ignore
        this.s_progress = new EventEmitter();
        // prettier-ignore
        this.s_reachBeginning = new EventEmitter();
        // prettier-ignore
        this.s_reachEnd = new EventEmitter();
        // prettier-ignore
        this.s_realIndexChange = new EventEmitter();
        // prettier-ignore
        this.s_resize = new EventEmitter();
        // prettier-ignore
        this.s_scroll = new EventEmitter();
        // prettier-ignore
        this.s_scrollbarDragEnd = new EventEmitter();
        // prettier-ignore
        this.s_scrollbarDragMove = new EventEmitter();
        // prettier-ignore
        this.s_scrollbarDragStart = new EventEmitter();
        // prettier-ignore
        this.s_setTransition = new EventEmitter();
        // prettier-ignore
        this.s_setTranslate = new EventEmitter();
        // prettier-ignore
        this.s_slideChange = new EventEmitter();
        // prettier-ignore
        this.s_slideChangeTransitionEnd = new EventEmitter();
        // prettier-ignore
        this.s_slideChangeTransitionStart = new EventEmitter();
        // prettier-ignore
        this.s_slideNextTransitionEnd = new EventEmitter();
        // prettier-ignore
        this.s_slideNextTransitionStart = new EventEmitter();
        // prettier-ignore
        this.s_slidePrevTransitionEnd = new EventEmitter();
        // prettier-ignore
        this.s_slidePrevTransitionStart = new EventEmitter();
        // prettier-ignore
        this.s_slideResetTransitionStart = new EventEmitter();
        // prettier-ignore
        this.s_slideResetTransitionEnd = new EventEmitter();
        // prettier-ignore
        this.s_sliderMove = new EventEmitter();
        // prettier-ignore
        this.s_sliderFirstMove = new EventEmitter();
        // prettier-ignore
        this.s_slidesLengthChange = new EventEmitter();
        // prettier-ignore
        this.s_slidesGridLengthChange = new EventEmitter();
        // prettier-ignore
        this.s_snapGridLengthChange = new EventEmitter();
        // prettier-ignore
        this.s_snapIndexChange = new EventEmitter();
        // prettier-ignore
        this.s_tap = new EventEmitter();
        // prettier-ignore
        this.s_toEdge = new EventEmitter();
        // prettier-ignore
        this.s_touchEnd = new EventEmitter();
        // prettier-ignore
        this.s_touchMove = new EventEmitter();
        // prettier-ignore
        this.s_touchMoveOpposite = new EventEmitter();
        // prettier-ignore
        this.s_touchStart = new EventEmitter();
        // prettier-ignore
        this.s_transitionEnd = new EventEmitter();
        // prettier-ignore
        this.s_transitionStart = new EventEmitter();
        // prettier-ignore
        this.s_update = new EventEmitter();
        // prettier-ignore
        this.s_zoomChange = new EventEmitter();
        // prettier-ignore
        this.s_swiper = new EventEmitter();
        this.indexChange = new EventEmitter();
        this._activeSlides = new Subject();
        this.containerClasses = 'swiper';
        this.slidesChanges = (val) => {
            this.slides = val.map((slide, index) => {
                slide.slideIndex = index;
                slide.classNames = this.slideClass || '';
                return slide;
            });
            if (this.loop && !this.loopedSlides) {
                this.calcLoopedSlides();
            }
            if (!this.virtual) {
                if (this.loopedSlides) {
                    this.prependSlides = of(this.slides.slice(this.slides.length - this.loopedSlides));
                    this.appendSlides = of(this.slides.slice(0, this.loopedSlides));
                }
            }
            else if (this.swiperRef && this.swiperRef.virtual) {
                this._ngZone.runOutsideAngular(() => {
                    this.swiperRef.virtual.slides = this.slides;
                    this.swiperRef.virtual.update(true);
                });
            }
            this._changeDetectorRef.detectChanges();
        };
        this.style = null;
        this.updateVirtualSlides = (virtualData) => {
            // TODO: type virtualData
            if (!this.swiperRef ||
                (this.currentVirtualData &&
                    this.currentVirtualData.from === virtualData.from &&
                    this.currentVirtualData.to === virtualData.to &&
                    this.currentVirtualData.offset === virtualData.offset)) {
                return;
            }
            this.style = this.swiperRef.isHorizontal()
                ? {
                    [this.swiperRef.rtlTranslate ? 'right' : 'left']: `${virtualData.offset}px`,
                }
                : {
                    top: `${virtualData.offset}px`,
                };
            this.currentVirtualData = virtualData;
            this._activeSlides.next(virtualData.slides);
            this._ngZone.run(() => {
                this._changeDetectorRef.detectChanges();
            });
            this._ngZone.runOutsideAngular(() => {
                this.swiperRef.updateSlides();
                this.swiperRef.updateProgress();
                this.swiperRef.updateSlidesClasses();
                if (this.swiperRef.lazy && this.swiperRef.params.lazy['enabled']) {
                    this.swiperRef.lazy.load();
                }
                this.swiperRef.virtual.update(true);
            });
            return;
        };
    }
    set navigation(val) {
        var _a, _b, _c;
        const currentNext = typeof this._navigation !== 'boolean' && this._navigation !== ''
            ? (_a = this._navigation) === null || _a === void 0 ? void 0 : _a.nextEl
            : null;
        const currentPrev = typeof this._navigation !== 'boolean' && this._navigation !== ''
            ? (_b = this._navigation) === null || _b === void 0 ? void 0 : _b.prevEl
            : null;
        this._navigation = setProperty(val, {
            nextEl: currentNext || null,
            prevEl: currentPrev || null,
        });
        this.showNavigation = !(coerceBooleanProperty(val) !== true ||
            (this._navigation &&
                typeof this._navigation !== 'boolean' &&
                this._navigation.prevEl !== ((_c = this._prevElRef) === null || _c === void 0 ? void 0 : _c.nativeElement) &&
                (this._navigation.prevEl !== null || this._navigation.nextEl !== null) &&
                (typeof this._navigation.nextEl === 'string' ||
                    typeof this._navigation.prevEl === 'string' ||
                    typeof this._navigation.nextEl === 'object' ||
                    typeof this._navigation.prevEl === 'object')));
    }
    get navigation() {
        return this._navigation;
    }
    set pagination(val) {
        var _a;
        const current = typeof this._pagination !== 'boolean' && this._pagination !== ''
            ? (_a = this._pagination) === null || _a === void 0 ? void 0 : _a.el
            : null;
        this._pagination = setProperty(val, {
            el: current || null,
        });
        this.showPagination = isShowEl(val, this._pagination, this._paginationElRef);
    }
    get pagination() {
        return this._pagination;
    }
    set scrollbar(val) {
        var _a;
        const current = typeof this._scrollbar !== 'boolean' && this._scrollbar !== '' ? (_a = this._scrollbar) === null || _a === void 0 ? void 0 : _a.el : null;
        this._scrollbar = setProperty(val, {
            el: current || null,
        });
        this.showScrollbar = isShowEl(val, this._scrollbar, this._scrollbarElRef);
    }
    get scrollbar() {
        return this._scrollbar;
    }
    set virtual(val) {
        this._virtual = setProperty(val);
    }
    get virtual() {
        return this._virtual;
    }
    set index(index) {
        console.warn('`[(index)]` prop is deprecated and will be removed in upcoming versions');
        this.setIndex(index);
    }
    set config(val) {
        this.updateSwiper(val);
        const { params } = getParams(val);
        Object.assign(this, params);
    }
    set prevElRef(el) {
        this._prevElRef = el;
        this._setElement(el, this.navigation, 'navigation', 'prevEl');
    }
    set nextElRef(el) {
        this._nextElRef = el;
        this._setElement(el, this.navigation, 'navigation', 'nextEl');
    }
    set scrollbarElRef(el) {
        this._scrollbarElRef = el;
        this._setElement(el, this.scrollbar, 'scrollbar');
    }
    set paginationElRef(el) {
        this._paginationElRef = el;
        this._setElement(el, this.pagination, 'pagination');
    }
    get activeSlides() {
        if (this.virtual) {
            return this._activeSlides;
        }
        return of(this.slides);
    }
    get zoomContainerClass() {
        return this.zoom && typeof this.zoom !== 'boolean'
            ? this.zoom.containerClass
            : 'swiper-zoom-container';
    }
    _setElement(el, ref, update, key = 'el') {
        if (!el || !ref) {
            return;
        }
        if (ref && el.nativeElement) {
            if (ref[key] === el.nativeElement) {
                return;
            }
            ref[key] = el.nativeElement;
        }
        const updateObj = {};
        updateObj[update] = true;
        this.updateInitSwiper(updateObj);
    }
    ngOnInit() {
        const { params } = getParams(this);
        Object.assign(this, params);
    }
    ngAfterViewInit() {
        this.childrenSlidesInit();
        this.initSwiper();
        this._changeDetectorRef.detectChanges();
        setTimeout(() => {
            this.s_swiper.emit(this.swiperRef);
        });
    }
    childrenSlidesInit() {
        this.slidesChanges(this.slidesEl);
        this.slidesEl.changes.subscribe(this.slidesChanges);
    }
    get isSwiperActive() {
        return this.swiperRef && !this.swiperRef.destroyed;
    }
    initSwiper() {
        const { params: swiperParams, passedParams } = getParams(this);
        Object.assign(this, swiperParams);
        this._ngZone.runOutsideAngular(() => {
            swiperParams.init = false;
            if (!swiperParams.virtual) {
                swiperParams.observer = true;
            }
            swiperParams.onAny = (eventName, ...args) => {
                const emitter = this[('s_' + eventName)];
                if (emitter) {
                    emitter.emit(...args);
                }
            };
            const _slideClasses = (_, updated) => {
                updated.forEach(({ slideEl, classNames }, index) => {
                    const dataIndex = slideEl.getAttribute('data-swiper-slide-index');
                    const slideIndex = dataIndex ? parseInt(dataIndex) : index;
                    if (this.virtual) {
                        const virtualSlide = this.slides.find((item) => {
                            return item.virtualIndex && item.virtualIndex === slideIndex;
                        });
                        if (virtualSlide) {
                            virtualSlide.classNames = classNames;
                            return;
                        }
                    }
                    if (this.slides[slideIndex]) {
                        this.slides[slideIndex].classNames = classNames;
                    }
                });
                this._changeDetectorRef.detectChanges();
            };
            const _containerClasses = (_, classes) => {
                setTimeout(() => {
                    this.containerClasses = classes;
                });
            };
            Object.assign(swiperParams.on, {
                _containerClasses,
                _slideClasses,
            });
            const swiperRef = new Swiper(swiperParams);
            swiperRef.loopCreate = () => { };
            swiperRef.loopDestroy = () => { };
            if (swiperParams.loop) {
                swiperRef.loopedSlides = this.loopedSlides;
            }
            const isVirtualEnabled = typeof swiperRef.params.virtual !== 'undefined' &&
                typeof swiperRef.params.virtual !== 'boolean' &&
                swiperRef.params.virtual.enabled;
            if (swiperRef.virtual && isVirtualEnabled) {
                swiperRef.virtual.slides = this.slides;
                const extendWith = {
                    cache: false,
                    slides: this.slides,
                    renderExternal: this.updateVirtualSlides,
                    renderExternalUpdate: false,
                };
                extend(swiperRef.params.virtual, extendWith);
                extend(swiperRef.originalParams.virtual, extendWith);
            }
            if (isPlatformBrowser(this._platformId)) {
                this.swiperRef = swiperRef.init(this.elementRef.nativeElement);
                const isEnabled = typeof this.swiperRef.params.virtual !== 'undefined' &&
                    typeof this.swiperRef.params.virtual !== 'boolean' &&
                    this.swiperRef.params.virtual.enabled;
                if (this.swiperRef.virtual && isEnabled) {
                    this.swiperRef.virtual.update(true);
                }
                this._changeDetectorRef.detectChanges();
                swiperRef.on('slideChange', () => {
                    this.indexChange.emit(this.swiperRef.realIndex);
                });
            }
        });
    }
    ngOnChanges(changedParams) {
        this.updateSwiper(changedParams);
        this._changeDetectorRef.detectChanges();
    }
    updateInitSwiper(changedParams) {
        if (!(changedParams && this.swiperRef && !this.swiperRef.destroyed)) {
            return;
        }
        this._ngZone.runOutsideAngular(() => {
            const { params: currentParams, pagination, navigation, scrollbar, virtual, thumbs, } = this.swiperRef;
            if (changedParams.pagination) {
                if (this.pagination &&
                    typeof this.pagination !== 'boolean' &&
                    this.pagination.el &&
                    pagination &&
                    !pagination.el) {
                    this.updateParameter('pagination', this.pagination);
                    pagination.init();
                    pagination.render();
                    pagination.update();
                }
                else {
                    pagination.destroy();
                    pagination.el = null;
                }
            }
            if (changedParams.scrollbar) {
                if (this.scrollbar &&
                    typeof this.scrollbar !== 'boolean' &&
                    this.scrollbar.el &&
                    scrollbar &&
                    !scrollbar.el) {
                    this.updateParameter('scrollbar', this.scrollbar);
                    scrollbar.init();
                    scrollbar.updateSize();
                    scrollbar.setTranslate();
                }
                else {
                    scrollbar.destroy();
                    scrollbar.el = null;
                }
            }
            if (changedParams.navigation) {
                if (this.navigation &&
                    typeof this.navigation !== 'boolean' &&
                    this.navigation.prevEl &&
                    this.navigation.nextEl &&
                    navigation &&
                    !navigation.prevEl &&
                    !navigation.nextEl) {
                    this.updateParameter('navigation', this.navigation);
                    navigation.init();
                    navigation.update();
                }
                else if (navigation.prevEl && navigation.nextEl) {
                    navigation.destroy();
                    navigation.nextEl = null;
                    navigation.prevEl = null;
                }
            }
            if (changedParams.thumbs && this.thumbs && this.thumbs.swiper) {
                this.updateParameter('thumbs', this.thumbs);
                const initialized = thumbs.init();
                if (initialized)
                    thumbs.update(true);
            }
            if (changedParams.controller && this.controller && this.controller.control) {
                this.swiperRef.controller.control = this.controller.control;
            }
            this.swiperRef.update();
        });
    }
    updateSwiper(changedParams) {
        this._ngZone.runOutsideAngular(() => {
            var _a, _b;
            if (changedParams.config) {
                return;
            }
            if (!(changedParams && this.swiperRef && !this.swiperRef.destroyed)) {
                return;
            }
            for (const key in changedParams) {
                if (ignoreNgOnChanges.indexOf(key) >= 0) {
                    continue;
                }
                const newValue = (_b = (_a = changedParams[key]) === null || _a === void 0 ? void 0 : _a.currentValue) !== null && _b !== void 0 ? _b : changedParams[key];
                this.updateParameter(key, newValue);
            }
            if (changedParams.allowSlideNext) {
                this.swiperRef.allowSlideNext = this.allowSlideNext;
            }
            if (changedParams.allowSlidePrev) {
                this.swiperRef.allowSlidePrev = this.allowSlidePrev;
            }
            if (changedParams.direction) {
                this.swiperRef.changeDirection(this.direction, false);
            }
            if (changedParams.breakpoints) {
                if (this.loop && !this.loopedSlides) {
                    this.calcLoopedSlides();
                }
                this.swiperRef.currentBreakpoint = null;
                this.swiperRef.setBreakpoint();
            }
            if (changedParams.thumbs || changedParams.controller) {
                this.updateInitSwiper(changedParams);
            }
            this.swiperRef.update();
        });
    }
    calcLoopedSlides() {
        if (!this.loop) {
            return;
        }
        let slidesPerViewParams = this.slidesPerView;
        if (this.breakpoints) {
            const breakpoint = Swiper.prototype.getBreakpoint(this.breakpoints);
            const breakpointOnlyParams = breakpoint in this.breakpoints ? this.breakpoints[breakpoint] : undefined;
            if (breakpointOnlyParams && breakpointOnlyParams.slidesPerView) {
                slidesPerViewParams = breakpointOnlyParams.slidesPerView;
            }
        }
        if (slidesPerViewParams === 'auto') {
            this.loopedSlides = this.slides.length;
            return this.slides.length;
        }
        let loopedSlides = this.loopedSlides || slidesPerViewParams;
        if (!loopedSlides) {
            // ?
            return;
        }
        if (this.loopAdditionalSlides) {
            loopedSlides += this.loopAdditionalSlides;
        }
        if (loopedSlides > this.slides.length) {
            loopedSlides = this.slides.length;
        }
        this.loopedSlides = loopedSlides;
        return loopedSlides;
    }
    updateParameter(key, value) {
        if (!(this.swiperRef && !this.swiperRef.destroyed)) {
            return;
        }
        const _key = key.replace(/^_/, '');
        const isCurrentParamObj = isObject(this.swiperRef.params[_key]);
        if (Object.keys(this.swiperRef.modules).indexOf(_key) >= 0) {
            const defaultParams = this.swiperRef.modules[_key].params[_key];
            if (isCurrentParamObj) {
                extend(this.swiperRef.params[_key], defaultParams);
            }
            else {
                this.swiperRef.params[_key] = defaultParams;
            }
        }
        if (_key === 'enabled') {
            if (value === true) {
                this.swiperRef.enable();
            }
            else if (value === false) {
                this.swiperRef.disable();
            }
            return;
        }
        if (isCurrentParamObj && isObject(value)) {
            extend(this.swiperRef.params[_key], value);
        }
        else {
            this.swiperRef.params[_key] = value;
        }
    }
    /**
     * @deprecated will be removed in upcoming versions
     */
    setIndex(index, speed, silent) {
        if (!this.isSwiperActive) {
            this.initialSlide = index;
            return;
        }
        if (index === this.swiperRef.activeIndex) {
            return;
        }
        this._ngZone.runOutsideAngular(() => {
            if (this.loop) {
                this.swiperRef.slideToLoop(index, speed, !silent);
            }
            else {
                this.swiperRef.slideTo(index, speed, !silent);
            }
        });
    }
    ngOnDestroy() {
        this._ngZone.runOutsideAngular(() => {
            var _a;
            (_a = this.swiperRef) === null || _a === void 0 ? void 0 : _a.destroy(true, false);
        });
    }
}
SwiperComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperComponent, deps: [{ token: i0.NgZone }, { token: i0.ElementRef }, { token: i0.ChangeDetectorRef }, { token: PLATFORM_ID }], target: i0.ɵɵFactoryTarget.Component });
SwiperComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "12.0.0", version: "12.2.2", type: SwiperComponent, selector: "swiper, [swiper]", inputs: { enabled: "enabled", direction: "direction", touchEventsTarget: "touchEventsTarget", initialSlide: "initialSlide", speed: "speed", cssMode: "cssMode", updateOnWindowResize: "updateOnWindowResize", resizeObserver: "resizeObserver", nested: "nested", focusableElements: "focusableElements", width: "width", height: "height", preventInteractionOnTransition: "preventInteractionOnTransition", userAgent: "userAgent", url: "url", edgeSwipeDetection: "edgeSwipeDetection", edgeSwipeThreshold: "edgeSwipeThreshold", freeMode: "freeMode", autoHeight: "autoHeight", setWrapperSize: "setWrapperSize", virtualTranslate: "virtualTranslate", effect: "effect", breakpoints: "breakpoints", spaceBetween: "spaceBetween", slidesPerView: "slidesPerView", grid: "grid", slidesPerGroup: "slidesPerGroup", slidesPerGroupSkip: "slidesPerGroupSkip", centeredSlides: "centeredSlides", centeredSlidesBounds: "centeredSlidesBounds", slidesOffsetBefore: "slidesOffsetBefore", slidesOffsetAfter: "slidesOffsetAfter", normalizeSlideIndex: "normalizeSlideIndex", centerInsufficientSlides: "centerInsufficientSlides", watchOverflow: "watchOverflow", roundLengths: "roundLengths", touchRatio: "touchRatio", touchAngle: "touchAngle", simulateTouch: "simulateTouch", shortSwipes: "shortSwipes", longSwipes: "longSwipes", longSwipesRatio: "longSwipesRatio", longSwipesMs: "longSwipesMs", followFinger: "followFinger", allowTouchMove: "allowTouchMove", threshold: "threshold", touchMoveStopPropagation: "touchMoveStopPropagation", touchStartPreventDefault: "touchStartPreventDefault", touchStartForcePreventDefault: "touchStartForcePreventDefault", touchReleaseOnEdges: "touchReleaseOnEdges", uniqueNavElements: "uniqueNavElements", resistance: "resistance", resistanceRatio: "resistanceRatio", watchSlidesProgress: "watchSlidesProgress", grabCursor: "grabCursor", preventClicks: "preventClicks", preventClicksPropagation: "preventClicksPropagation", slideToClickedSlide: "slideToClickedSlide", preloadImages: "preloadImages", updateOnImagesReady: "updateOnImagesReady", loop: "loop", loopAdditionalSlides: "loopAdditionalSlides", loopedSlides: "loopedSlides", loopFillGroupWithBlank: "loopFillGroupWithBlank", loopPreventsSlide: "loopPreventsSlide", allowSlidePrev: "allowSlidePrev", allowSlideNext: "allowSlideNext", swipeHandler: "swipeHandler", noSwiping: "noSwiping", noSwipingClass: "noSwipingClass", noSwipingSelector: "noSwipingSelector", passiveListeners: "passiveListeners", containerModifierClass: "containerModifierClass", slideClass: "slideClass", slideBlankClass: "slideBlankClass", slideActiveClass: "slideActiveClass", slideDuplicateActiveClass: "slideDuplicateActiveClass", slideVisibleClass: "slideVisibleClass", slideDuplicateClass: "slideDuplicateClass", slideNextClass: "slideNextClass", slideDuplicateNextClass: "slideDuplicateNextClass", slidePrevClass: "slidePrevClass", slideDuplicatePrevClass: "slideDuplicatePrevClass", wrapperClass: "wrapperClass", runCallbacksOnInit: "runCallbacksOnInit", observeParents: "observeParents", observeSlideChildren: "observeSlideChildren", a11y: "a11y", autoplay: "autoplay", controller: "controller", coverflowEffect: "coverflowEffect", cubeEffect: "cubeEffect", fadeEffect: "fadeEffect", flipEffect: "flipEffect", creativeEffect: "creativeEffect", cardsEffect: "cardsEffect", hashNavigation: "hashNavigation", history: "history", keyboard: "keyboard", lazy: "lazy", mousewheel: "mousewheel", parallax: "parallax", thumbs: "thumbs", zoom: "zoom", class: "class", id: "id", navigation: "navigation", pagination: "pagination", scrollbar: "scrollbar", virtual: "virtual", index: "index", config: "config" }, outputs: { s__beforeBreakpoint: "_beforeBreakpoint", s__containerClasses: "_containerClasses", s__slideClass: "_slideClass", s__swiper: "_swiper", s_activeIndexChange: "activeIndexChange", s_afterInit: "afterInit", s_autoplay: "autoplay", s_autoplayStart: "autoplayStart", s_autoplayStop: "autoplayStop", s_beforeDestroy: "beforeDestroy", s_beforeInit: "beforeInit", s_beforeLoopFix: "beforeLoopFix", s_beforeResize: "beforeResize", s_beforeSlideChangeStart: "beforeSlideChangeStart", s_beforeTransitionStart: "beforeTransitionStart", s_breakpoint: "breakpoint", s_changeDirection: "changeDirection", s_click: "click", s_doubleTap: "doubleTap", s_doubleClick: "doubleClick", s_destroy: "destroy", s_fromEdge: "fromEdge", s_hashChange: "hashChange", s_hashSet: "hashSet", s_imagesReady: "imagesReady", s_init: "init", s_keyPress: "keyPress", s_lazyImageLoad: "lazyImageLoad", s_lazyImageReady: "lazyImageReady", s_loopFix: "loopFix", s_momentumBounce: "momentumBounce", s_navigationHide: "navigationHide", s_navigationShow: "navigationShow", s_observerUpdate: "observerUpdate", s_orientationchange: "orientationchange", s_paginationHide: "paginationHide", s_paginationRender: "paginationRender", s_paginationShow: "paginationShow", s_paginationUpdate: "paginationUpdate", s_progress: "progress", s_reachBeginning: "reachBeginning", s_reachEnd: "reachEnd", s_realIndexChange: "realIndexChange", s_resize: "resize", s_scroll: "scroll", s_scrollbarDragEnd: "scrollbarDragEnd", s_scrollbarDragMove: "scrollbarDragMove", s_scrollbarDragStart: "scrollbarDragStart", s_setTransition: "setTransition", s_setTranslate: "setTranslate", s_slideChange: "slideChange", s_slideChangeTransitionEnd: "slideChangeTransitionEnd", s_slideChangeTransitionStart: "slideChangeTransitionStart", s_slideNextTransitionEnd: "slideNextTransitionEnd", s_slideNextTransitionStart: "slideNextTransitionStart", s_slidePrevTransitionEnd: "slidePrevTransitionEnd", s_slidePrevTransitionStart: "slidePrevTransitionStart", s_slideResetTransitionStart: "slideResetTransitionStart", s_slideResetTransitionEnd: "slideResetTransitionEnd", s_sliderMove: "sliderMove", s_sliderFirstMove: "sliderFirstMove", s_slidesLengthChange: "slidesLengthChange", s_slidesGridLengthChange: "slidesGridLengthChange", s_snapGridLengthChange: "snapGridLengthChange", s_snapIndexChange: "snapIndexChange", s_tap: "tap", s_toEdge: "toEdge", s_touchEnd: "touchEnd", s_touchMove: "touchMove", s_touchMoveOpposite: "touchMoveOpposite", s_touchStart: "touchStart", s_transitionEnd: "transitionEnd", s_transitionStart: "transitionStart", s_update: "update", s_zoomChange: "zoomChange", s_swiper: "swiper", indexChange: "indexChange" }, host: { properties: { "class": "this.containerClasses" } }, queries: [{ propertyName: "slidesEl", predicate: SwiperSlideDirective }], viewQueries: [{ propertyName: "prevElRef", first: true, predicate: ["prevElRef"], descendants: true }, { propertyName: "nextElRef", first: true, predicate: ["nextElRef"], descendants: true }, { propertyName: "scrollbarElRef", first: true, predicate: ["scrollbarElRef"], descendants: true }, { propertyName: "paginationElRef", first: true, predicate: ["paginationElRef"], descendants: true }], usesOnChanges: true, ngImport: i0, template: "<ng-content select=\"[slot=container-start]\"></ng-content>\n<ng-container *ngIf=\"navigation && showNavigation\">\n  <div class=\"swiper-button-prev\" #prevElRef></div>\n  <div class=\"swiper-button-next\" #nextElRef></div>\n</ng-container>\n<div *ngIf=\"scrollbar && showScrollbar\" class=\"swiper-scrollbar\" #scrollbarElRef></div>\n<div *ngIf=\"pagination && showPagination\" class=\"swiper-pagination\" #paginationElRef></div>\n<div [ngClass]=\"wrapperClass\" [attr.id]=\"id\">\n  <ng-content select=\"[slot=wrapper-start]\"></ng-content>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: prependSlides,\n        key: 'prepend'\n      }\n    \"\n  ></ng-template>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: activeSlides,\n        key: ''\n      }\n    \"\n  ></ng-template>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: appendSlides,\n        key: 'append'\n      }\n    \"\n  ></ng-template>\n  <ng-content select=\"[slot=wrapper-end]\"></ng-content>\n</div>\n<ng-content select=\"[slot=container-end]\"></ng-content>\n\n<ng-template #slidesTemplate let-loopSlides=\"loopSlides\" let-slideKey=\"key\">\n  <div\n    *ngFor=\"let slide of loopSlides | async\"\n    [ngClass]=\"\n      (slide.class ? slide.class + ' ' : '') +\n      slideClass +\n      (slideKey !== '' ? ' ' + slideDuplicateClass : '')\n    \"\n    [attr.data-swiper-slide-index]=\"slide.virtualIndex ? slide.virtualIndex : slide.slideIndex\"\n    [attr.data-swiper-autoplay]=\"slide.autoplayDelay\"\n    [style]=\"style\"\n    [ngSwitch]=\"slide.zoom\"\n  >\n    <div *ngSwitchCase=\"true\" [ngClass]=\"zoomContainerClass\">\n      <ng-template\n        [ngTemplateOutlet]=\"slide.template\"\n        [ngTemplateOutletContext]=\"{\n          $implicit: slide.slideData\n        }\"\n      ></ng-template>\n    </div>\n    <ng-container *ngSwitchDefault>\n      <ng-template\n        [ngTemplateOutlet]=\"slide.template\"\n        [ngTemplateOutletContext]=\"{\n          $implicit: slide.slideData\n        }\"\n      ></ng-template>\n    </ng-container>\n  </div>\n</ng-template>\n", styles: ["\n      swiper {\n        display: block;\n      }\n    "], directives: [{ type: i1.NgIf, selector: "[ngIf]", inputs: ["ngIf", "ngIfThen", "ngIfElse"] }, { type: i1.NgClass, selector: "[ngClass]", inputs: ["class", "ngClass"] }, { type: i1.NgTemplateOutlet, selector: "[ngTemplateOutlet]", inputs: ["ngTemplateOutletContext", "ngTemplateOutlet"] }, { type: i1.NgForOf, selector: "[ngFor][ngForOf]", inputs: ["ngForOf", "ngForTrackBy", "ngForTemplate"] }, { type: i1.NgSwitch, selector: "[ngSwitch]", inputs: ["ngSwitch"] }, { type: i1.NgSwitchCase, selector: "[ngSwitchCase]", inputs: ["ngSwitchCase"] }, { type: i1.NgSwitchDefault, selector: "[ngSwitchDefault]" }], pipes: { "async": i1.AsyncPipe }, changeDetection: i0.ChangeDetectionStrategy.OnPush, encapsulation: i0.ViewEncapsulation.None });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperComponent, decorators: [{
            type: Component,
            args: [{
                    selector: 'swiper, [swiper]',
                    templateUrl: './swiper.component.html',
                    changeDetection: ChangeDetectionStrategy.OnPush,
                    encapsulation: ViewEncapsulation.None,
                    styles: [
                        `
      swiper {
        display: block;
      }
    `,
                    ],
                }]
        }], ctorParameters: function () { return [{ type: i0.NgZone }, { type: i0.ElementRef }, { type: i0.ChangeDetectorRef }, { type: Object, decorators: [{
                    type: Inject,
                    args: [PLATFORM_ID]
                }] }]; }, propDecorators: { enabled: [{
                type: Input
            }], direction: [{
                type: Input
            }], touchEventsTarget: [{
                type: Input
            }], initialSlide: [{
                type: Input
            }], speed: [{
                type: Input
            }], cssMode: [{
                type: Input
            }], updateOnWindowResize: [{
                type: Input
            }], resizeObserver: [{
                type: Input
            }], nested: [{
                type: Input
            }], focusableElements: [{
                type: Input
            }], width: [{
                type: Input
            }], height: [{
                type: Input
            }], preventInteractionOnTransition: [{
                type: Input
            }], userAgent: [{
                type: Input
            }], url: [{
                type: Input
            }], edgeSwipeDetection: [{
                type: Input
            }], edgeSwipeThreshold: [{
                type: Input
            }], freeMode: [{
                type: Input
            }], autoHeight: [{
                type: Input
            }], setWrapperSize: [{
                type: Input
            }], virtualTranslate: [{
                type: Input
            }], effect: [{
                type: Input
            }], breakpoints: [{
                type: Input
            }], spaceBetween: [{
                type: Input
            }], slidesPerView: [{
                type: Input
            }], grid: [{
                type: Input
            }], slidesPerGroup: [{
                type: Input
            }], slidesPerGroupSkip: [{
                type: Input
            }], centeredSlides: [{
                type: Input
            }], centeredSlidesBounds: [{
                type: Input
            }], slidesOffsetBefore: [{
                type: Input
            }], slidesOffsetAfter: [{
                type: Input
            }], normalizeSlideIndex: [{
                type: Input
            }], centerInsufficientSlides: [{
                type: Input
            }], watchOverflow: [{
                type: Input
            }], roundLengths: [{
                type: Input
            }], touchRatio: [{
                type: Input
            }], touchAngle: [{
                type: Input
            }], simulateTouch: [{
                type: Input
            }], shortSwipes: [{
                type: Input
            }], longSwipes: [{
                type: Input
            }], longSwipesRatio: [{
                type: Input
            }], longSwipesMs: [{
                type: Input
            }], followFinger: [{
                type: Input
            }], allowTouchMove: [{
                type: Input
            }], threshold: [{
                type: Input
            }], touchMoveStopPropagation: [{
                type: Input
            }], touchStartPreventDefault: [{
                type: Input
            }], touchStartForcePreventDefault: [{
                type: Input
            }], touchReleaseOnEdges: [{
                type: Input
            }], uniqueNavElements: [{
                type: Input
            }], resistance: [{
                type: Input
            }], resistanceRatio: [{
                type: Input
            }], watchSlidesProgress: [{
                type: Input
            }], grabCursor: [{
                type: Input
            }], preventClicks: [{
                type: Input
            }], preventClicksPropagation: [{
                type: Input
            }], slideToClickedSlide: [{
                type: Input
            }], preloadImages: [{
                type: Input
            }], updateOnImagesReady: [{
                type: Input
            }], loop: [{
                type: Input
            }], loopAdditionalSlides: [{
                type: Input
            }], loopedSlides: [{
                type: Input
            }], loopFillGroupWithBlank: [{
                type: Input
            }], loopPreventsSlide: [{
                type: Input
            }], allowSlidePrev: [{
                type: Input
            }], allowSlideNext: [{
                type: Input
            }], swipeHandler: [{
                type: Input
            }], noSwiping: [{
                type: Input
            }], noSwipingClass: [{
                type: Input
            }], noSwipingSelector: [{
                type: Input
            }], passiveListeners: [{
                type: Input
            }], containerModifierClass: [{
                type: Input
            }], slideClass: [{
                type: Input
            }], slideBlankClass: [{
                type: Input
            }], slideActiveClass: [{
                type: Input
            }], slideDuplicateActiveClass: [{
                type: Input
            }], slideVisibleClass: [{
                type: Input
            }], slideDuplicateClass: [{
                type: Input
            }], slideNextClass: [{
                type: Input
            }], slideDuplicateNextClass: [{
                type: Input
            }], slidePrevClass: [{
                type: Input
            }], slideDuplicatePrevClass: [{
                type: Input
            }], wrapperClass: [{
                type: Input
            }], runCallbacksOnInit: [{
                type: Input
            }], observeParents: [{
                type: Input
            }], observeSlideChildren: [{
                type: Input
            }], a11y: [{
                type: Input
            }], autoplay: [{
                type: Input
            }], controller: [{
                type: Input
            }], coverflowEffect: [{
                type: Input
            }], cubeEffect: [{
                type: Input
            }], fadeEffect: [{
                type: Input
            }], flipEffect: [{
                type: Input
            }], creativeEffect: [{
                type: Input
            }], cardsEffect: [{
                type: Input
            }], hashNavigation: [{
                type: Input
            }], history: [{
                type: Input
            }], keyboard: [{
                type: Input
            }], lazy: [{
                type: Input
            }], mousewheel: [{
                type: Input
            }], parallax: [{
                type: Input
            }], thumbs: [{
                type: Input
            }], zoom: [{
                type: Input
            }], class: [{
                type: Input
            }], id: [{
                type: Input
            }], navigation: [{
                type: Input
            }], pagination: [{
                type: Input
            }], scrollbar: [{
                type: Input
            }], virtual: [{
                type: Input
            }], index: [{
                type: Input
            }], config: [{
                type: Input
            }], s__beforeBreakpoint: [{
                type: Output,
                args: ['_beforeBreakpoint']
            }], s__containerClasses: [{
                type: Output,
                args: ['_containerClasses']
            }], s__slideClass: [{
                type: Output,
                args: ['_slideClass']
            }], s__swiper: [{
                type: Output,
                args: ['_swiper']
            }], s_activeIndexChange: [{
                type: Output,
                args: ['activeIndexChange']
            }], s_afterInit: [{
                type: Output,
                args: ['afterInit']
            }], s_autoplay: [{
                type: Output,
                args: ['autoplay']
            }], s_autoplayStart: [{
                type: Output,
                args: ['autoplayStart']
            }], s_autoplayStop: [{
                type: Output,
                args: ['autoplayStop']
            }], s_beforeDestroy: [{
                type: Output,
                args: ['beforeDestroy']
            }], s_beforeInit: [{
                type: Output,
                args: ['beforeInit']
            }], s_beforeLoopFix: [{
                type: Output,
                args: ['beforeLoopFix']
            }], s_beforeResize: [{
                type: Output,
                args: ['beforeResize']
            }], s_beforeSlideChangeStart: [{
                type: Output,
                args: ['beforeSlideChangeStart']
            }], s_beforeTransitionStart: [{
                type: Output,
                args: ['beforeTransitionStart']
            }], s_breakpoint: [{
                type: Output,
                args: ['breakpoint']
            }], s_changeDirection: [{
                type: Output,
                args: ['changeDirection']
            }], s_click: [{
                type: Output,
                args: ['click']
            }], s_doubleTap: [{
                type: Output,
                args: ['doubleTap']
            }], s_doubleClick: [{
                type: Output,
                args: ['doubleClick']
            }], s_destroy: [{
                type: Output,
                args: ['destroy']
            }], s_fromEdge: [{
                type: Output,
                args: ['fromEdge']
            }], s_hashChange: [{
                type: Output,
                args: ['hashChange']
            }], s_hashSet: [{
                type: Output,
                args: ['hashSet']
            }], s_imagesReady: [{
                type: Output,
                args: ['imagesReady']
            }], s_init: [{
                type: Output,
                args: ['init']
            }], s_keyPress: [{
                type: Output,
                args: ['keyPress']
            }], s_lazyImageLoad: [{
                type: Output,
                args: ['lazyImageLoad']
            }], s_lazyImageReady: [{
                type: Output,
                args: ['lazyImageReady']
            }], s_loopFix: [{
                type: Output,
                args: ['loopFix']
            }], s_momentumBounce: [{
                type: Output,
                args: ['momentumBounce']
            }], s_navigationHide: [{
                type: Output,
                args: ['navigationHide']
            }], s_navigationShow: [{
                type: Output,
                args: ['navigationShow']
            }], s_observerUpdate: [{
                type: Output,
                args: ['observerUpdate']
            }], s_orientationchange: [{
                type: Output,
                args: ['orientationchange']
            }], s_paginationHide: [{
                type: Output,
                args: ['paginationHide']
            }], s_paginationRender: [{
                type: Output,
                args: ['paginationRender']
            }], s_paginationShow: [{
                type: Output,
                args: ['paginationShow']
            }], s_paginationUpdate: [{
                type: Output,
                args: ['paginationUpdate']
            }], s_progress: [{
                type: Output,
                args: ['progress']
            }], s_reachBeginning: [{
                type: Output,
                args: ['reachBeginning']
            }], s_reachEnd: [{
                type: Output,
                args: ['reachEnd']
            }], s_realIndexChange: [{
                type: Output,
                args: ['realIndexChange']
            }], s_resize: [{
                type: Output,
                args: ['resize']
            }], s_scroll: [{
                type: Output,
                args: ['scroll']
            }], s_scrollbarDragEnd: [{
                type: Output,
                args: ['scrollbarDragEnd']
            }], s_scrollbarDragMove: [{
                type: Output,
                args: ['scrollbarDragMove']
            }], s_scrollbarDragStart: [{
                type: Output,
                args: ['scrollbarDragStart']
            }], s_setTransition: [{
                type: Output,
                args: ['setTransition']
            }], s_setTranslate: [{
                type: Output,
                args: ['setTranslate']
            }], s_slideChange: [{
                type: Output,
                args: ['slideChange']
            }], s_slideChangeTransitionEnd: [{
                type: Output,
                args: ['slideChangeTransitionEnd']
            }], s_slideChangeTransitionStart: [{
                type: Output,
                args: ['slideChangeTransitionStart']
            }], s_slideNextTransitionEnd: [{
                type: Output,
                args: ['slideNextTransitionEnd']
            }], s_slideNextTransitionStart: [{
                type: Output,
                args: ['slideNextTransitionStart']
            }], s_slidePrevTransitionEnd: [{
                type: Output,
                args: ['slidePrevTransitionEnd']
            }], s_slidePrevTransitionStart: [{
                type: Output,
                args: ['slidePrevTransitionStart']
            }], s_slideResetTransitionStart: [{
                type: Output,
                args: ['slideResetTransitionStart']
            }], s_slideResetTransitionEnd: [{
                type: Output,
                args: ['slideResetTransitionEnd']
            }], s_sliderMove: [{
                type: Output,
                args: ['sliderMove']
            }], s_sliderFirstMove: [{
                type: Output,
                args: ['sliderFirstMove']
            }], s_slidesLengthChange: [{
                type: Output,
                args: ['slidesLengthChange']
            }], s_slidesGridLengthChange: [{
                type: Output,
                args: ['slidesGridLengthChange']
            }], s_snapGridLengthChange: [{
                type: Output,
                args: ['snapGridLengthChange']
            }], s_snapIndexChange: [{
                type: Output,
                args: ['snapIndexChange']
            }], s_tap: [{
                type: Output,
                args: ['tap']
            }], s_toEdge: [{
                type: Output,
                args: ['toEdge']
            }], s_touchEnd: [{
                type: Output,
                args: ['touchEnd']
            }], s_touchMove: [{
                type: Output,
                args: ['touchMove']
            }], s_touchMoveOpposite: [{
                type: Output,
                args: ['touchMoveOpposite']
            }], s_touchStart: [{
                type: Output,
                args: ['touchStart']
            }], s_transitionEnd: [{
                type: Output,
                args: ['transitionEnd']
            }], s_transitionStart: [{
                type: Output,
                args: ['transitionStart']
            }], s_update: [{
                type: Output,
                args: ['update']
            }], s_zoomChange: [{
                type: Output,
                args: ['zoomChange']
            }], s_swiper: [{
                type: Output,
                args: ['swiper']
            }], indexChange: [{
                type: Output
            }], prevElRef: [{
                type: ViewChild,
                args: ['prevElRef', { static: false }]
            }], nextElRef: [{
                type: ViewChild,
                args: ['nextElRef', { static: false }]
            }], scrollbarElRef: [{
                type: ViewChild,
                args: ['scrollbarElRef', { static: false }]
            }], paginationElRef: [{
                type: ViewChild,
                args: ['paginationElRef', { static: false }]
            }], slidesEl: [{
                type: ContentChildren,
                args: [SwiperSlideDirective, { descendants: false, emitDistinctChangesOnly: true }]
            }], containerClasses: [{
                type: HostBinding,
                args: ['class']
            }] } });

class SwiperModule {
}
SwiperModule.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperModule, deps: [], target: i0.ɵɵFactoryTarget.NgModule });
SwiperModule.ɵmod = i0.ɵɵngDeclareNgModule({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperModule, declarations: [SwiperComponent, SwiperSlideDirective], imports: [CommonModule], exports: [SwiperComponent, SwiperSlideDirective] });
SwiperModule.ɵinj = i0.ɵɵngDeclareInjector({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperModule, imports: [[CommonModule]] });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperModule, decorators: [{
            type: NgModule,
            args: [{
                    declarations: [SwiperComponent, SwiperSlideDirective],
                    exports: [SwiperComponent, SwiperSlideDirective],
                    imports: [CommonModule],
                }]
        }] });

/*
 * Public API Surface of angular
 */

/**
 * Generated bundle index. Do not edit.
 */

export { SwiperComponent, SwiperModule, SwiperSlideDirective };
//# sourceMappingURL=swiper_angular.js.map
