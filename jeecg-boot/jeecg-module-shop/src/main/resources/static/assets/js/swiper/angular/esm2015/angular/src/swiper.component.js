import { ChangeDetectionStrategy, Component, ContentChildren, EventEmitter, HostBinding, Inject, Input, Output, PLATFORM_ID, ViewChild, ViewEncapsulation, } from '@angular/core';
// @ts-ignore
import Swiper from 'swiper';
import { of, Subject } from 'rxjs';
import { getParams } from './utils/get-params';
import { SwiperSlideDirective } from './swiper-slide.directive';
import { extend, isObject, setProperty, ignoreNgOnChanges, coerceBooleanProperty, isShowEl, } from './utils/utils';
import { isPlatformBrowser } from '@angular/common';
import * as i0 from "@angular/core";
import * as i1 from "@angular/common";
export class SwiperComponent {
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic3dpcGVyLmNvbXBvbmVudC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL3NyYy9hbmd1bGFyL3NyYy9zd2lwZXIuY29tcG9uZW50LnRzIiwiLi4vLi4vLi4vLi4vLi4vc3JjL2FuZ3VsYXIvc3JjL3N3aXBlci5jb21wb25lbnQuaHRtbCJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQ0wsdUJBQXVCLEVBRXZCLFNBQVMsRUFDVCxlQUFlLEVBRWYsWUFBWSxFQUNaLFdBQVcsRUFDWCxNQUFNLEVBQ04sS0FBSyxFQUdMLE1BQU0sRUFDTixXQUFXLEVBR1gsU0FBUyxFQUNULGlCQUFpQixHQUNsQixNQUFNLGVBQWUsQ0FBQztBQUN2QixhQUFhO0FBQ2IsT0FBTyxNQUFNLE1BQU0sUUFBUSxDQUFDO0FBQzVCLE9BQU8sRUFBYyxFQUFFLEVBQUUsT0FBTyxFQUFFLE1BQU0sTUFBTSxDQUFDO0FBQy9DLE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxvQkFBb0IsQ0FBQztBQUMvQyxPQUFPLEVBQUUsb0JBQW9CLEVBQUUsTUFBTSwwQkFBMEIsQ0FBQztBQUNoRSxPQUFPLEVBQ0wsTUFBTSxFQUNOLFFBQVEsRUFDUixXQUFXLEVBQ1gsaUJBQWlCLEVBQ2pCLHFCQUFxQixFQUNyQixRQUFRLEdBQ1QsTUFBTSxlQUFlLENBQUM7QUFTdkIsT0FBTyxFQUFFLGlCQUFpQixFQUFFLE1BQU0saUJBQWlCLENBQUM7OztBQWNwRCxNQUFNLE9BQU8sZUFBZTtJQTBZMUIsWUFDVSxPQUFlLEVBQ2YsVUFBc0IsRUFDdEIsa0JBQXFDLEVBQ2hCLFdBQW1CO1FBSHhDLFlBQU8sR0FBUCxPQUFPLENBQVE7UUFDZixlQUFVLEdBQVYsVUFBVSxDQUFZO1FBQ3RCLHVCQUFrQixHQUFsQixrQkFBa0IsQ0FBbUI7UUFDaEIsZ0JBQVcsR0FBWCxXQUFXLENBQVE7UUFwVXpDLGVBQVUsR0FBZ0MsY0FBYyxDQUFDO1FBVXpELGlCQUFZLEdBQWtDLGdCQUFnQixDQUFDO1FBcUR4RSxtQkFBYyxHQUFZLElBQUksQ0FBQztRQWlCL0IsbUJBQWMsR0FBWSxJQUFJLENBQUM7UUFlL0Isa0JBQWEsR0FBWSxJQUFJLENBQUM7UUFzQjlCLGtCQUFrQjtRQUNXLHdCQUFtQixHQUFvRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzVILGtCQUFrQjtRQUNXLHdCQUFtQixHQUFvRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzVILGtCQUFrQjtRQUNLLGtCQUFhLEdBQThDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDMUcsa0JBQWtCO1FBQ0MsY0FBUyxHQUEwQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzlGLGtCQUFrQjtRQUNXLHdCQUFtQixHQUFvRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzVILGtCQUFrQjtRQUNHLGdCQUFXLEdBQTRDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDcEcsa0JBQWtCO1FBQ0UsZUFBVSxHQUEyQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ2pHLGtCQUFrQjtRQUNPLG9CQUFlLEdBQWdELElBQUksWUFBWSxFQUFPLENBQUM7UUFDaEgsa0JBQWtCO1FBQ00sbUJBQWMsR0FBK0MsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUM3RyxrQkFBa0I7UUFDTyxvQkFBZSxHQUFnRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ2hILGtCQUFrQjtRQUNJLGlCQUFZLEdBQTZDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDdkcsa0JBQWtCO1FBQ08sb0JBQWUsR0FBZ0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNoSCxrQkFBa0I7UUFDTSxtQkFBYyxHQUErQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzdHLGtCQUFrQjtRQUNnQiw2QkFBd0IsR0FBeUQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUMzSSxrQkFBa0I7UUFDZSw0QkFBdUIsR0FBd0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUN4SSxrQkFBa0I7UUFDSSxpQkFBWSxHQUE2QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3ZHLGtCQUFrQjtRQUNTLHNCQUFpQixHQUFrRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3RILGtCQUFrQjtRQUNELFlBQU8sR0FBd0MsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUN4RixrQkFBa0I7UUFDRyxnQkFBVyxHQUE0QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3BHLGtCQUFrQjtRQUNLLGtCQUFhLEdBQThDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDMUcsa0JBQWtCO1FBQ0MsY0FBUyxHQUEwQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzlGLGtCQUFrQjtRQUNFLGVBQVUsR0FBMkMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNqRyxrQkFBa0I7UUFDSSxpQkFBWSxHQUE2QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3ZHLGtCQUFrQjtRQUNDLGNBQVMsR0FBMEMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUM5RixrQkFBa0I7UUFDSyxrQkFBYSxHQUE4QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzFHLGtCQUFrQjtRQUNGLFdBQU0sR0FBdUMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNyRixrQkFBa0I7UUFDRSxlQUFVLEdBQTJDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDakcsa0JBQWtCO1FBQ08sb0JBQWUsR0FBZ0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNoSCxrQkFBa0I7UUFDUSxxQkFBZ0IsR0FBaUQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNuSCxrQkFBa0I7UUFDQyxjQUFTLEdBQTBDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDOUYsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1csd0JBQW1CLEdBQW9ELElBQUksWUFBWSxFQUFPLENBQUM7UUFDNUgsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1UsdUJBQWtCLEdBQW1ELElBQUksWUFBWSxFQUFPLENBQUM7UUFDekgsa0JBQWtCO1FBQ1EscUJBQWdCLEdBQWlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDbkgsa0JBQWtCO1FBQ1UsdUJBQWtCLEdBQW1ELElBQUksWUFBWSxFQUFPLENBQUM7UUFDekgsa0JBQWtCO1FBQ0UsZUFBVSxHQUEyQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ2pHLGtCQUFrQjtRQUNRLHFCQUFnQixHQUFpRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ25ILGtCQUFrQjtRQUNFLGVBQVUsR0FBMkMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNqRyxrQkFBa0I7UUFDUyxzQkFBaUIsR0FBa0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUN0SCxrQkFBa0I7UUFDQSxhQUFRLEdBQXlDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDM0Ysa0JBQWtCO1FBQ0EsYUFBUSxHQUF5QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzNGLGtCQUFrQjtRQUNVLHVCQUFrQixHQUFtRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3pILGtCQUFrQjtRQUNXLHdCQUFtQixHQUFvRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzVILGtCQUFrQjtRQUNZLHlCQUFvQixHQUFxRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQy9ILGtCQUFrQjtRQUNPLG9CQUFlLEdBQWdELElBQUksWUFBWSxFQUFPLENBQUM7UUFDaEgsa0JBQWtCO1FBQ00sbUJBQWMsR0FBK0MsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUM3RyxrQkFBa0I7UUFDSyxrQkFBYSxHQUE4QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzFHLGtCQUFrQjtRQUNrQiwrQkFBMEIsR0FBMkQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNqSixrQkFBa0I7UUFDb0IsaUNBQTRCLEdBQTZELElBQUksWUFBWSxFQUFPLENBQUM7UUFDdkosa0JBQWtCO1FBQ2dCLDZCQUF3QixHQUF5RCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzNJLGtCQUFrQjtRQUNrQiwrQkFBMEIsR0FBMkQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNqSixrQkFBa0I7UUFDZ0IsNkJBQXdCLEdBQXlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDM0ksa0JBQWtCO1FBQ2tCLCtCQUEwQixHQUEyRCxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ2pKLGtCQUFrQjtRQUNtQixnQ0FBMkIsR0FBNEQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNwSixrQkFBa0I7UUFDaUIsOEJBQXlCLEdBQTBELElBQUksWUFBWSxFQUFPLENBQUM7UUFDOUksa0JBQWtCO1FBQ0ksaUJBQVksR0FBNkMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUN2RyxrQkFBa0I7UUFDUyxzQkFBaUIsR0FBa0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUN0SCxrQkFBa0I7UUFDWSx5QkFBb0IsR0FBcUQsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUMvSCxrQkFBa0I7UUFDZ0IsNkJBQXdCLEdBQXlELElBQUksWUFBWSxFQUFPLENBQUM7UUFDM0ksa0JBQWtCO1FBQ2MsMkJBQXNCLEdBQXVELElBQUksWUFBWSxFQUFPLENBQUM7UUFDckksa0JBQWtCO1FBQ1Msc0JBQWlCLEdBQWtELElBQUksWUFBWSxFQUFPLENBQUM7UUFDdEgsa0JBQWtCO1FBQ0gsVUFBSyxHQUFzQyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ2xGLGtCQUFrQjtRQUNBLGFBQVEsR0FBeUMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUMzRixrQkFBa0I7UUFDRSxlQUFVLEdBQTJDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDakcsa0JBQWtCO1FBQ0csZ0JBQVcsR0FBNEMsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUNwRyxrQkFBa0I7UUFDVyx3QkFBbUIsR0FBb0QsSUFBSSxZQUFZLEVBQU8sQ0FBQztRQUM1SCxrQkFBa0I7UUFDSSxpQkFBWSxHQUE2QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQ3ZHLGtCQUFrQjtRQUNPLG9CQUFlLEdBQWdELElBQUksWUFBWSxFQUFPLENBQUM7UUFDaEgsa0JBQWtCO1FBQ1Msc0JBQWlCLEdBQWtELElBQUksWUFBWSxFQUFPLENBQUM7UUFDdEgsa0JBQWtCO1FBQ0EsYUFBUSxHQUF5QyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBQzNGLGtCQUFrQjtRQUNJLGlCQUFZLEdBQTZDLElBQUksWUFBWSxFQUFPLENBQUM7UUFDdkcsa0JBQWtCO1FBQ0EsYUFBUSxHQUFzQixJQUFJLFlBQVksRUFBTyxDQUFDO1FBRTlELGdCQUFXLEdBQUcsSUFBSSxZQUFZLEVBQVUsQ0FBQztRQWtDMUMsa0JBQWEsR0FBRyxJQUFJLE9BQU8sRUFBMEIsQ0FBQztRQWV6QyxxQkFBZ0IsR0FBVyxRQUFRLENBQUM7UUF3Q2xELGtCQUFhLEdBQUcsQ0FBQyxHQUFvQyxFQUFFLEVBQUU7WUFDL0QsSUFBSSxDQUFDLE1BQU0sR0FBRyxHQUFHLENBQUMsR0FBRyxDQUFDLENBQUMsS0FBMkIsRUFBRSxLQUFhLEVBQUUsRUFBRTtnQkFDbkUsS0FBSyxDQUFDLFVBQVUsR0FBRyxLQUFLLENBQUM7Z0JBQ3pCLEtBQUssQ0FBQyxVQUFVLEdBQUcsSUFBSSxDQUFDLFVBQVUsSUFBSSxFQUFFLENBQUM7Z0JBQ3pDLE9BQU8sS0FBSyxDQUFDO1lBQ2YsQ0FBQyxDQUFDLENBQUM7WUFDSCxJQUFJLElBQUksQ0FBQyxJQUFJLElBQUksQ0FBQyxJQUFJLENBQUMsWUFBWSxFQUFFO2dCQUNuQyxJQUFJLENBQUMsZ0JBQWdCLEVBQUUsQ0FBQzthQUN6QjtZQUNELElBQUksQ0FBQyxJQUFJLENBQUMsT0FBTyxFQUFFO2dCQUNqQixJQUFJLElBQUksQ0FBQyxZQUFZLEVBQUU7b0JBQ3JCLElBQUksQ0FBQyxhQUFhLEdBQUcsRUFBRSxDQUFDLElBQUksQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLElBQUksQ0FBQyxNQUFNLENBQUMsTUFBTSxHQUFHLElBQUksQ0FBQyxZQUFZLENBQUMsQ0FBQyxDQUFDO29CQUNuRixJQUFJLENBQUMsWUFBWSxHQUFHLEVBQUUsQ0FBQyxJQUFJLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxDQUFDLEVBQUUsSUFBSSxDQUFDLFlBQVksQ0FBQyxDQUFDLENBQUM7aUJBQ2pFO2FBQ0Y7aUJBQU0sSUFBSSxJQUFJLENBQUMsU0FBUyxJQUFJLElBQUksQ0FBQyxTQUFTLENBQUMsT0FBTyxFQUFFO2dCQUNuRCxJQUFJLENBQUMsT0FBTyxDQUFDLGlCQUFpQixDQUFDLEdBQUcsRUFBRTtvQkFDbEMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxPQUFPLENBQUMsTUFBTSxHQUFHLElBQUksQ0FBQyxNQUFNLENBQUM7b0JBQzVDLElBQUksQ0FBQyxTQUFTLENBQUMsT0FBTyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsQ0FBQztnQkFDdEMsQ0FBQyxDQUFDLENBQUM7YUFDSjtZQUNELElBQUksQ0FBQyxrQkFBa0IsQ0FBQyxhQUFhLEVBQUUsQ0FBQztRQUMxQyxDQUFDLENBQUM7UUF5RkYsVUFBSyxHQUFRLElBQUksQ0FBQztRQUVWLHdCQUFtQixHQUFHLENBQUMsV0FBZ0IsRUFBRSxFQUFFO1lBQ2pELHlCQUF5QjtZQUN6QixJQUNFLENBQUMsSUFBSSxDQUFDLFNBQVM7Z0JBQ2YsQ0FBQyxJQUFJLENBQUMsa0JBQWtCO29CQUN0QixJQUFJLENBQUMsa0JBQWtCLENBQUMsSUFBSSxLQUFLLFdBQVcsQ0FBQyxJQUFJO29CQUNqRCxJQUFJLENBQUMsa0JBQWtCLENBQUMsRUFBRSxLQUFLLFdBQVcsQ0FBQyxFQUFFO29CQUM3QyxJQUFJLENBQUMsa0JBQWtCLENBQUMsTUFBTSxLQUFLLFdBQVcsQ0FBQyxNQUFNLENBQUMsRUFDeEQ7Z0JBQ0EsT0FBTzthQUNSO1lBQ0QsSUFBSSxDQUFDLEtBQUssR0FBRyxJQUFJLENBQUMsU0FBUyxDQUFDLFlBQVksRUFBRTtnQkFDeEMsQ0FBQyxDQUFDO29CQUNFLENBQUMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxZQUFZLENBQUMsQ0FBQyxDQUFDLE9BQU8sQ0FBQyxDQUFDLENBQUMsTUFBTSxDQUFDLEVBQUUsR0FBRyxXQUFXLENBQUMsTUFBTSxJQUFJO2lCQUM1RTtnQkFDSCxDQUFDLENBQUM7b0JBQ0UsR0FBRyxFQUFFLEdBQUcsV0FBVyxDQUFDLE1BQU0sSUFBSTtpQkFDL0IsQ0FBQztZQUNOLElBQUksQ0FBQyxrQkFBa0IsR0FBRyxXQUFXLENBQUM7WUFDdEMsSUFBSSxDQUFDLGFBQWEsQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLE1BQU0sQ0FBQyxDQUFDO1lBQzVDLElBQUksQ0FBQyxPQUFPLENBQUMsR0FBRyxDQUFDLEdBQUcsRUFBRTtnQkFDcEIsSUFBSSxDQUFDLGtCQUFrQixDQUFDLGFBQWEsRUFBRSxDQUFDO1lBQzFDLENBQUMsQ0FBQyxDQUFDO1lBQ0gsSUFBSSxDQUFDLE9BQU8sQ0FBQyxpQkFBaUIsQ0FBQyxHQUFHLEVBQUU7Z0JBQ2xDLElBQUksQ0FBQyxTQUFTLENBQUMsWUFBWSxFQUFFLENBQUM7Z0JBQzlCLElBQUksQ0FBQyxTQUFTLENBQUMsY0FBYyxFQUFFLENBQUM7Z0JBQ2hDLElBQUksQ0FBQyxTQUFTLENBQUMsbUJBQW1CLEVBQUUsQ0FBQztnQkFDckMsSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLElBQUksSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDLEVBQUU7b0JBQ2hFLElBQUksQ0FBQyxTQUFTLENBQUMsSUFBSSxDQUFDLElBQUksRUFBRSxDQUFDO2lCQUM1QjtnQkFDRCxJQUFJLENBQUMsU0FBUyxDQUFDLE9BQU8sQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLENBQUM7WUFDdEMsQ0FBQyxDQUFDLENBQUM7WUFDSCxPQUFPO1FBQ1QsQ0FBQyxDQUFDO0lBbkxDLENBQUM7SUFwU0osSUFDSSxVQUFVLENBQUMsR0FBRzs7UUFDaEIsTUFBTSxXQUFXLEdBQ2YsT0FBTyxJQUFJLENBQUMsV0FBVyxLQUFLLFNBQVMsSUFBSSxJQUFJLENBQUMsV0FBVyxLQUFLLEVBQUU7WUFDOUQsQ0FBQyxDQUFDLE1BQUEsSUFBSSxDQUFDLFdBQVcsMENBQUUsTUFBTTtZQUMxQixDQUFDLENBQUMsSUFBSSxDQUFDO1FBQ1gsTUFBTSxXQUFXLEdBQ2YsT0FBTyxJQUFJLENBQUMsV0FBVyxLQUFLLFNBQVMsSUFBSSxJQUFJLENBQUMsV0FBVyxLQUFLLEVBQUU7WUFDOUQsQ0FBQyxDQUFDLE1BQUEsSUFBSSxDQUFDLFdBQVcsMENBQUUsTUFBTTtZQUMxQixDQUFDLENBQUMsSUFBSSxDQUFDO1FBQ1gsSUFBSSxDQUFDLFdBQVcsR0FBRyxXQUFXLENBQUMsR0FBRyxFQUFFO1lBQ2xDLE1BQU0sRUFBRSxXQUFXLElBQUksSUFBSTtZQUMzQixNQUFNLEVBQUUsV0FBVyxJQUFJLElBQUk7U0FDNUIsQ0FBQyxDQUFDO1FBQ0gsSUFBSSxDQUFDLGNBQWMsR0FBRyxDQUFDLENBQ3JCLHFCQUFxQixDQUFDLEdBQUcsQ0FBQyxLQUFLLElBQUk7WUFDbkMsQ0FBQyxJQUFJLENBQUMsV0FBVztnQkFDZixPQUFPLElBQUksQ0FBQyxXQUFXLEtBQUssU0FBUztnQkFDckMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxNQUFNLE1BQUssTUFBQSxJQUFJLENBQUMsVUFBVSwwQ0FBRSxhQUFhLENBQUE7Z0JBQzFELENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxNQUFNLEtBQUssSUFBSSxJQUFJLElBQUksQ0FBQyxXQUFXLENBQUMsTUFBTSxLQUFLLElBQUksQ0FBQztnQkFDdEUsQ0FBQyxPQUFPLElBQUksQ0FBQyxXQUFXLENBQUMsTUFBTSxLQUFLLFFBQVE7b0JBQzFDLE9BQU8sSUFBSSxDQUFDLFdBQVcsQ0FBQyxNQUFNLEtBQUssUUFBUTtvQkFDM0MsT0FBTyxJQUFJLENBQUMsV0FBVyxDQUFDLE1BQU0sS0FBSyxRQUFRO29CQUMzQyxPQUFPLElBQUksQ0FBQyxXQUFXLENBQUMsTUFBTSxLQUFLLFFBQVEsQ0FBQyxDQUFDLENBQ2xELENBQUM7SUFDSixDQUFDO0lBQ0QsSUFBSSxVQUFVO1FBQ1osT0FBTyxJQUFJLENBQUMsV0FBVyxDQUFDO0lBQzFCLENBQUM7SUFJRCxJQUNJLFVBQVUsQ0FBQyxHQUFHOztRQUNoQixNQUFNLE9BQU8sR0FDWCxPQUFPLElBQUksQ0FBQyxXQUFXLEtBQUssU0FBUyxJQUFJLElBQUksQ0FBQyxXQUFXLEtBQUssRUFBRTtZQUM5RCxDQUFDLENBQUMsTUFBQSxJQUFJLENBQUMsV0FBVywwQ0FBRSxFQUFFO1lBQ3RCLENBQUMsQ0FBQyxJQUFJLENBQUM7UUFDWCxJQUFJLENBQUMsV0FBVyxHQUFHLFdBQVcsQ0FBQyxHQUFHLEVBQUU7WUFDbEMsRUFBRSxFQUFFLE9BQU8sSUFBSSxJQUFJO1NBQ3BCLENBQUMsQ0FBQztRQUNILElBQUksQ0FBQyxjQUFjLEdBQUcsUUFBUSxDQUFDLEdBQUcsRUFBRSxJQUFJLENBQUMsV0FBVyxFQUFFLElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxDQUFDO0lBQy9FLENBQUM7SUFDRCxJQUFJLFVBQVU7UUFDWixPQUFPLElBQUksQ0FBQyxXQUFXLENBQUM7SUFDMUIsQ0FBQztJQUlELElBQ0ksU0FBUyxDQUFDLEdBQUc7O1FBQ2YsTUFBTSxPQUFPLEdBQ1gsT0FBTyxJQUFJLENBQUMsVUFBVSxLQUFLLFNBQVMsSUFBSSxJQUFJLENBQUMsVUFBVSxLQUFLLEVBQUUsQ0FBQyxDQUFDLENBQUMsTUFBQSxJQUFJLENBQUMsVUFBVSwwQ0FBRSxFQUFFLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQztRQUM5RixJQUFJLENBQUMsVUFBVSxHQUFHLFdBQVcsQ0FBQyxHQUFHLEVBQUU7WUFDakMsRUFBRSxFQUFFLE9BQU8sSUFBSSxJQUFJO1NBQ3BCLENBQUMsQ0FBQztRQUNILElBQUksQ0FBQyxhQUFhLEdBQUcsUUFBUSxDQUFDLEdBQUcsRUFBRSxJQUFJLENBQUMsVUFBVSxFQUFFLElBQUksQ0FBQyxlQUFlLENBQUMsQ0FBQztJQUM1RSxDQUFDO0lBQ0QsSUFBSSxTQUFTO1FBQ1gsT0FBTyxJQUFJLENBQUMsVUFBVSxDQUFDO0lBQ3pCLENBQUM7SUFJRCxJQUNJLE9BQU8sQ0FBQyxHQUFHO1FBQ2IsSUFBSSxDQUFDLFFBQVEsR0FBRyxXQUFXLENBQUMsR0FBRyxDQUFDLENBQUM7SUFDbkMsQ0FBQztJQUNELElBQUksT0FBTztRQUNULE9BQU8sSUFBSSxDQUFDLFFBQVEsQ0FBQztJQUN2QixDQUFDO0lBR0QsSUFDSSxLQUFLLENBQUMsS0FBYTtRQUNyQixPQUFPLENBQUMsSUFBSSxDQUFDLHlFQUF5RSxDQUFDLENBQUM7UUFDeEYsSUFBSSxDQUFDLFFBQVEsQ0FBQyxLQUFLLENBQUMsQ0FBQztJQUN2QixDQUFDO0lBQ0QsSUFDSSxNQUFNLENBQUMsR0FBa0I7UUFDM0IsSUFBSSxDQUFDLFlBQVksQ0FBQyxHQUFHLENBQUMsQ0FBQztRQUN2QixNQUFNLEVBQUUsTUFBTSxFQUFFLEdBQUcsU0FBUyxDQUFDLEdBQUcsQ0FBQyxDQUFDO1FBQ2xDLE1BQU0sQ0FBQyxNQUFNLENBQUMsSUFBSSxFQUFFLE1BQU0sQ0FBQyxDQUFDO0lBQzlCLENBQUM7SUE0SkQsSUFDSSxTQUFTLENBQUMsRUFBYztRQUMxQixJQUFJLENBQUMsVUFBVSxHQUFHLEVBQUUsQ0FBQztRQUNyQixJQUFJLENBQUMsV0FBVyxDQUFDLEVBQUUsRUFBRSxJQUFJLENBQUMsVUFBVSxFQUFFLFlBQVksRUFBRSxRQUFRLENBQUMsQ0FBQztJQUNoRSxDQUFDO0lBRUQsSUFDSSxTQUFTLENBQUMsRUFBYztRQUMxQixJQUFJLENBQUMsVUFBVSxHQUFHLEVBQUUsQ0FBQztRQUNyQixJQUFJLENBQUMsV0FBVyxDQUFDLEVBQUUsRUFBRSxJQUFJLENBQUMsVUFBVSxFQUFFLFlBQVksRUFBRSxRQUFRLENBQUMsQ0FBQztJQUNoRSxDQUFDO0lBRUQsSUFDSSxjQUFjLENBQUMsRUFBYztRQUMvQixJQUFJLENBQUMsZUFBZSxHQUFHLEVBQUUsQ0FBQztRQUMxQixJQUFJLENBQUMsV0FBVyxDQUFDLEVBQUUsRUFBRSxJQUFJLENBQUMsU0FBUyxFQUFFLFdBQVcsQ0FBQyxDQUFDO0lBQ3BELENBQUM7SUFFRCxJQUNJLGVBQWUsQ0FBQyxFQUFjO1FBQ2hDLElBQUksQ0FBQyxnQkFBZ0IsR0FBRyxFQUFFLENBQUM7UUFDM0IsSUFBSSxDQUFDLFdBQVcsQ0FBQyxFQUFFLEVBQUUsSUFBSSxDQUFDLFVBQVUsRUFBRSxZQUFZLENBQUMsQ0FBQztJQUN0RCxDQUFDO0lBWUQsSUFBSSxZQUFZO1FBQ2QsSUFBSSxJQUFJLENBQUMsT0FBTyxFQUFFO1lBQ2hCLE9BQU8sSUFBSSxDQUFDLGFBQWEsQ0FBQztTQUMzQjtRQUNELE9BQU8sRUFBRSxDQUFDLElBQUksQ0FBQyxNQUFNLENBQUMsQ0FBQztJQUN6QixDQUFDO0lBRUQsSUFBSSxrQkFBa0I7UUFDcEIsT0FBTyxJQUFJLENBQUMsSUFBSSxJQUFJLE9BQU8sSUFBSSxDQUFDLElBQUksS0FBSyxTQUFTO1lBQ2hELENBQUMsQ0FBQyxJQUFJLENBQUMsSUFBSSxDQUFDLGNBQWM7WUFDMUIsQ0FBQyxDQUFDLHVCQUF1QixDQUFDO0lBQzlCLENBQUM7SUFVTyxXQUFXLENBQUMsRUFBYyxFQUFFLEdBQVEsRUFBRSxNQUFjLEVBQUUsR0FBRyxHQUFHLElBQUk7UUFDdEUsSUFBSSxDQUFDLEVBQUUsSUFBSSxDQUFDLEdBQUcsRUFBRTtZQUNmLE9BQU87U0FDUjtRQUNELElBQUksR0FBRyxJQUFJLEVBQUUsQ0FBQyxhQUFhLEVBQUU7WUFDM0IsSUFBSSxHQUFHLENBQUMsR0FBRyxDQUFDLEtBQUssRUFBRSxDQUFDLGFBQWEsRUFBRTtnQkFDakMsT0FBTzthQUNSO1lBQ0QsR0FBRyxDQUFDLEdBQUcsQ0FBQyxHQUFHLEVBQUUsQ0FBQyxhQUFhLENBQUM7U0FDN0I7UUFDRCxNQUFNLFNBQVMsR0FBK0IsRUFBRSxDQUFDO1FBQ2pELFNBQVMsQ0FBQyxNQUFNLENBQUMsR0FBRyxJQUFJLENBQUM7UUFDekIsSUFBSSxDQUFDLGdCQUFnQixDQUFDLFNBQVMsQ0FBQyxDQUFDO0lBQ25DLENBQUM7SUFDRCxRQUFRO1FBQ04sTUFBTSxFQUFFLE1BQU0sRUFBRSxHQUFHLFNBQVMsQ0FBQyxJQUFJLENBQUMsQ0FBQztRQUNuQyxNQUFNLENBQUMsTUFBTSxDQUFDLElBQUksRUFBRSxNQUFNLENBQUMsQ0FBQztJQUM5QixDQUFDO0lBQ0QsZUFBZTtRQUNiLElBQUksQ0FBQyxrQkFBa0IsRUFBRSxDQUFDO1FBQzFCLElBQUksQ0FBQyxVQUFVLEVBQUUsQ0FBQztRQUNsQixJQUFJLENBQUMsa0JBQWtCLENBQUMsYUFBYSxFQUFFLENBQUM7UUFDeEMsVUFBVSxDQUFDLEdBQUcsRUFBRTtZQUNkLElBQUksQ0FBQyxRQUFRLENBQUMsSUFBSSxDQUFDLElBQUksQ0FBQyxTQUFTLENBQUMsQ0FBQztRQUNyQyxDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7SUFFTyxrQkFBa0I7UUFDeEIsSUFBSSxDQUFDLGFBQWEsQ0FBQyxJQUFJLENBQUMsUUFBUSxDQUFDLENBQUM7UUFDbEMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxPQUFPLENBQUMsU0FBUyxDQUFDLElBQUksQ0FBQyxhQUFhLENBQUMsQ0FBQztJQUN0RCxDQUFDO0lBeUJELElBQUksY0FBYztRQUNoQixPQUFPLElBQUksQ0FBQyxTQUFTLElBQUksQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDLFNBQVMsQ0FBQztJQUNyRCxDQUFDO0lBRUQsVUFBVTtRQUNSLE1BQU0sRUFBRSxNQUFNLEVBQUUsWUFBWSxFQUFFLFlBQVksRUFBRSxHQUFHLFNBQVMsQ0FBQyxJQUFJLENBQUMsQ0FBQztRQUMvRCxNQUFNLENBQUMsTUFBTSxDQUFDLElBQUksRUFBRSxZQUFZLENBQUMsQ0FBQztRQUNsQyxJQUFJLENBQUMsT0FBTyxDQUFDLGlCQUFpQixDQUFDLEdBQUcsRUFBRTtZQUNsQyxZQUFZLENBQUMsSUFBSSxHQUFHLEtBQUssQ0FBQztZQUMxQixJQUFJLENBQUMsWUFBWSxDQUFDLE9BQU8sRUFBRTtnQkFDekIsWUFBWSxDQUFDLFFBQVEsR0FBRyxJQUFJLENBQUM7YUFDOUI7WUFFRCxZQUFZLENBQUMsS0FBSyxHQUFHLENBQUMsU0FBZ0MsRUFBRSxHQUFHLElBQVcsRUFBRSxFQUFFO2dCQUN4RSxNQUFNLE9BQU8sR0FBRyxJQUFJLENBQUMsQ0FBQyxJQUFJLEdBQUcsU0FBUyxDQUEwQixDQUFzQixDQUFDO2dCQUN2RixJQUFJLE9BQU8sRUFBRTtvQkFDWCxPQUFPLENBQUMsSUFBSSxDQUFDLEdBQUcsSUFBSSxDQUFDLENBQUM7aUJBQ3ZCO1lBQ0gsQ0FBQyxDQUFDO1lBQ0YsTUFBTSxhQUFhLEdBQWtDLENBQUMsQ0FBQyxFQUFFLE9BQU8sRUFBRSxFQUFFO2dCQUNsRSxPQUFPLENBQUMsT0FBTyxDQUFDLENBQUMsRUFBRSxPQUFPLEVBQUUsVUFBVSxFQUFFLEVBQUUsS0FBSyxFQUFFLEVBQUU7b0JBQ2pELE1BQU0sU0FBUyxHQUFHLE9BQU8sQ0FBQyxZQUFZLENBQUMseUJBQXlCLENBQUMsQ0FBQztvQkFDbEUsTUFBTSxVQUFVLEdBQUcsU0FBUyxDQUFDLENBQUMsQ0FBQyxRQUFRLENBQUMsU0FBUyxDQUFDLENBQUMsQ0FBQyxDQUFDLEtBQUssQ0FBQztvQkFDM0QsSUFBSSxJQUFJLENBQUMsT0FBTyxFQUFFO3dCQUNoQixNQUFNLFlBQVksR0FBRyxJQUFJLENBQUMsTUFBTSxDQUFDLElBQUksQ0FBQyxDQUFDLElBQUksRUFBRSxFQUFFOzRCQUM3QyxPQUFPLElBQUksQ0FBQyxZQUFZLElBQUksSUFBSSxDQUFDLFlBQVksS0FBSyxVQUFVLENBQUM7d0JBQy9ELENBQUMsQ0FBQyxDQUFDO3dCQUNILElBQUksWUFBWSxFQUFFOzRCQUNoQixZQUFZLENBQUMsVUFBVSxHQUFHLFVBQVUsQ0FBQzs0QkFDckMsT0FBTzt5QkFDUjtxQkFDRjtvQkFFRCxJQUFJLElBQUksQ0FBQyxNQUFNLENBQUMsVUFBVSxDQUFDLEVBQUU7d0JBQzNCLElBQUksQ0FBQyxNQUFNLENBQUMsVUFBVSxDQUFDLENBQUMsVUFBVSxHQUFHLFVBQVUsQ0FBQztxQkFDakQ7Z0JBQ0gsQ0FBQyxDQUFDLENBQUM7Z0JBQ0gsSUFBSSxDQUFDLGtCQUFrQixDQUFDLGFBQWEsRUFBRSxDQUFDO1lBQzFDLENBQUMsQ0FBQztZQUNGLE1BQU0saUJBQWlCLEdBQXNDLENBQUMsQ0FBQyxFQUFFLE9BQU8sRUFBRSxFQUFFO2dCQUMxRSxVQUFVLENBQUMsR0FBRyxFQUFFO29CQUNkLElBQUksQ0FBQyxnQkFBZ0IsR0FBRyxPQUFPLENBQUM7Z0JBQ2xDLENBQUMsQ0FBQyxDQUFDO1lBQ0wsQ0FBQyxDQUFDO1lBQ0YsTUFBTSxDQUFDLE1BQU0sQ0FBQyxZQUFZLENBQUMsRUFBRSxFQUFFO2dCQUM3QixpQkFBaUI7Z0JBQ2pCLGFBQWE7YUFDZCxDQUFDLENBQUM7WUFDSCxNQUFNLFNBQVMsR0FBRyxJQUFJLE1BQU0sQ0FBQyxZQUFZLENBQUMsQ0FBQztZQUMzQyxTQUFTLENBQUMsVUFBVSxHQUFHLEdBQUcsRUFBRSxHQUFFLENBQUMsQ0FBQztZQUNoQyxTQUFTLENBQUMsV0FBVyxHQUFHLEdBQUcsRUFBRSxHQUFFLENBQUMsQ0FBQztZQUNqQyxJQUFJLFlBQVksQ0FBQyxJQUFJLEVBQUU7Z0JBQ3JCLFNBQVMsQ0FBQyxZQUFZLEdBQUcsSUFBSSxDQUFDLFlBQVksQ0FBQzthQUM1QztZQUNELE1BQU0sZ0JBQWdCLEdBQ3BCLE9BQU8sU0FBUyxDQUFDLE1BQU0sQ0FBQyxPQUFPLEtBQUssV0FBVztnQkFDL0MsT0FBTyxTQUFTLENBQUMsTUFBTSxDQUFDLE9BQU8sS0FBSyxTQUFTO2dCQUM3QyxTQUFTLENBQUMsTUFBTSxDQUFDLE9BQU8sQ0FBQyxPQUFPLENBQUM7WUFDbkMsSUFBSSxTQUFTLENBQUMsT0FBTyxJQUFJLGdCQUFnQixFQUFFO2dCQUN6QyxTQUFTLENBQUMsT0FBTyxDQUFDLE1BQU0sR0FBRyxJQUFJLENBQUMsTUFBTSxDQUFDO2dCQUN2QyxNQUFNLFVBQVUsR0FBRztvQkFDakIsS0FBSyxFQUFFLEtBQUs7b0JBQ1osTUFBTSxFQUFFLElBQUksQ0FBQyxNQUFNO29CQUNuQixjQUFjLEVBQUUsSUFBSSxDQUFDLG1CQUFtQjtvQkFDeEMsb0JBQW9CLEVBQUUsS0FBSztpQkFDNUIsQ0FBQztnQkFDRixNQUFNLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxPQUFPLEVBQUUsVUFBVSxDQUFDLENBQUM7Z0JBQzdDLE1BQU0sQ0FBQyxTQUFTLENBQUMsY0FBYyxDQUFDLE9BQU8sRUFBRSxVQUFVLENBQUMsQ0FBQzthQUN0RDtZQUVELElBQUksaUJBQWlCLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxFQUFFO2dCQUN2QyxJQUFJLENBQUMsU0FBUyxHQUFHLFNBQVMsQ0FBQyxJQUFJLENBQUMsSUFBSSxDQUFDLFVBQVUsQ0FBQyxhQUFhLENBQUMsQ0FBQztnQkFDL0QsTUFBTSxTQUFTLEdBQ2IsT0FBTyxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxPQUFPLEtBQUssV0FBVztvQkFDcEQsT0FBTyxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxPQUFPLEtBQUssU0FBUztvQkFDbEQsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsT0FBTyxDQUFDLE9BQU8sQ0FBQztnQkFDeEMsSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLE9BQU8sSUFBSSxTQUFTLEVBQUU7b0JBQ3ZDLElBQUksQ0FBQyxTQUFTLENBQUMsT0FBTyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsQ0FBQztpQkFDckM7Z0JBQ0QsSUFBSSxDQUFDLGtCQUFrQixDQUFDLGFBQWEsRUFBRSxDQUFDO2dCQUN4QyxTQUFTLENBQUMsRUFBRSxDQUFDLGFBQWEsRUFBRSxHQUFHLEVBQUU7b0JBQy9CLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSxDQUFDLElBQUksQ0FBQyxTQUFTLENBQUMsU0FBUyxDQUFDLENBQUM7Z0JBQ2xELENBQUMsQ0FBQyxDQUFDO2FBQ0o7UUFDSCxDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7SUF1Q0QsV0FBVyxDQUFDLGFBQTRCO1FBQ3RDLElBQUksQ0FBQyxZQUFZLENBQUMsYUFBYSxDQUFDLENBQUM7UUFDakMsSUFBSSxDQUFDLGtCQUFrQixDQUFDLGFBQWEsRUFBRSxDQUFDO0lBQzFDLENBQUM7SUFFRCxnQkFBZ0IsQ0FBQyxhQUFrQjtRQUNqQyxJQUFJLENBQUMsQ0FBQyxhQUFhLElBQUksSUFBSSxDQUFDLFNBQVMsSUFBSSxDQUFDLElBQUksQ0FBQyxTQUFTLENBQUMsU0FBUyxDQUFDLEVBQUU7WUFDbkUsT0FBTztTQUNSO1FBRUQsSUFBSSxDQUFDLE9BQU8sQ0FBQyxpQkFBaUIsQ0FBQyxHQUFHLEVBQUU7WUFDbEMsTUFBTSxFQUNKLE1BQU0sRUFBRSxhQUFhLEVBQ3JCLFVBQVUsRUFDVixVQUFVLEVBQ1YsU0FBUyxFQUNULE9BQU8sRUFDUCxNQUFNLEdBQ1AsR0FBRyxJQUFJLENBQUMsU0FBUyxDQUFDO1lBRW5CLElBQUksYUFBYSxDQUFDLFVBQVUsRUFBRTtnQkFDNUIsSUFDRSxJQUFJLENBQUMsVUFBVTtvQkFDZixPQUFPLElBQUksQ0FBQyxVQUFVLEtBQUssU0FBUztvQkFDcEMsSUFBSSxDQUFDLFVBQVUsQ0FBQyxFQUFFO29CQUNsQixVQUFVO29CQUNWLENBQUMsVUFBVSxDQUFDLEVBQUUsRUFDZDtvQkFDQSxJQUFJLENBQUMsZUFBZSxDQUFDLFlBQVksRUFBRSxJQUFJLENBQUMsVUFBVSxDQUFDLENBQUM7b0JBQ3BELFVBQVUsQ0FBQyxJQUFJLEVBQUUsQ0FBQztvQkFDbEIsVUFBVSxDQUFDLE1BQU0sRUFBRSxDQUFDO29CQUNwQixVQUFVLENBQUMsTUFBTSxFQUFFLENBQUM7aUJBQ3JCO3FCQUFNO29CQUNMLFVBQVUsQ0FBQyxPQUFPLEVBQUUsQ0FBQztvQkFDckIsVUFBVSxDQUFDLEVBQUUsR0FBRyxJQUFJLENBQUM7aUJBQ3RCO2FBQ0Y7WUFFRCxJQUFJLGFBQWEsQ0FBQyxTQUFTLEVBQUU7Z0JBQzNCLElBQ0UsSUFBSSxDQUFDLFNBQVM7b0JBQ2QsT0FBTyxJQUFJLENBQUMsU0FBUyxLQUFLLFNBQVM7b0JBQ25DLElBQUksQ0FBQyxTQUFTLENBQUMsRUFBRTtvQkFDakIsU0FBUztvQkFDVCxDQUFDLFNBQVMsQ0FBQyxFQUFFLEVBQ2I7b0JBQ0EsSUFBSSxDQUFDLGVBQWUsQ0FBQyxXQUFXLEVBQUUsSUFBSSxDQUFDLFNBQVMsQ0FBQyxDQUFDO29CQUNsRCxTQUFTLENBQUMsSUFBSSxFQUFFLENBQUM7b0JBQ2pCLFNBQVMsQ0FBQyxVQUFVLEVBQUUsQ0FBQztvQkFDdkIsU0FBUyxDQUFDLFlBQVksRUFBRSxDQUFDO2lCQUMxQjtxQkFBTTtvQkFDTCxTQUFTLENBQUMsT0FBTyxFQUFFLENBQUM7b0JBQ3BCLFNBQVMsQ0FBQyxFQUFFLEdBQUcsSUFBSSxDQUFDO2lCQUNyQjthQUNGO1lBRUQsSUFBSSxhQUFhLENBQUMsVUFBVSxFQUFFO2dCQUM1QixJQUNFLElBQUksQ0FBQyxVQUFVO29CQUNmLE9BQU8sSUFBSSxDQUFDLFVBQVUsS0FBSyxTQUFTO29CQUNwQyxJQUFJLENBQUMsVUFBVSxDQUFDLE1BQU07b0JBQ3RCLElBQUksQ0FBQyxVQUFVLENBQUMsTUFBTTtvQkFDdEIsVUFBVTtvQkFDVixDQUFDLFVBQVUsQ0FBQyxNQUFNO29CQUNsQixDQUFDLFVBQVUsQ0FBQyxNQUFNLEVBQ2xCO29CQUNBLElBQUksQ0FBQyxlQUFlLENBQUMsWUFBWSxFQUFFLElBQUksQ0FBQyxVQUFVLENBQUMsQ0FBQztvQkFDcEQsVUFBVSxDQUFDLElBQUksRUFBRSxDQUFDO29CQUNsQixVQUFVLENBQUMsTUFBTSxFQUFFLENBQUM7aUJBQ3JCO3FCQUFNLElBQUksVUFBVSxDQUFDLE1BQU0sSUFBSSxVQUFVLENBQUMsTUFBTSxFQUFFO29CQUNqRCxVQUFVLENBQUMsT0FBTyxFQUFFLENBQUM7b0JBQ3JCLFVBQVUsQ0FBQyxNQUFNLEdBQUcsSUFBSSxDQUFDO29CQUN6QixVQUFVLENBQUMsTUFBTSxHQUFHLElBQUksQ0FBQztpQkFDMUI7YUFDRjtZQUNELElBQUksYUFBYSxDQUFDLE1BQU0sSUFBSSxJQUFJLENBQUMsTUFBTSxJQUFJLElBQUksQ0FBQyxNQUFNLENBQUMsTUFBTSxFQUFFO2dCQUM3RCxJQUFJLENBQUMsZUFBZSxDQUFDLFFBQVEsRUFBRSxJQUFJLENBQUMsTUFBTSxDQUFDLENBQUM7Z0JBQzVDLE1BQU0sV0FBVyxHQUFHLE1BQU0sQ0FBQyxJQUFJLEVBQUUsQ0FBQztnQkFDbEMsSUFBSSxXQUFXO29CQUFFLE1BQU0sQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLENBQUM7YUFDdEM7WUFFRCxJQUFJLGFBQWEsQ0FBQyxVQUFVLElBQUksSUFBSSxDQUFDLFVBQVUsSUFBSSxJQUFJLENBQUMsVUFBVSxDQUFDLE9BQU8sRUFBRTtnQkFDMUUsSUFBSSxDQUFDLFNBQVMsQ0FBQyxVQUFVLENBQUMsT0FBTyxHQUFHLElBQUksQ0FBQyxVQUFVLENBQUMsT0FBTyxDQUFDO2FBQzdEO1lBRUQsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLEVBQUUsQ0FBQztRQUMxQixDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7SUFFRCxZQUFZLENBQUMsYUFBa0M7UUFDN0MsSUFBSSxDQUFDLE9BQU8sQ0FBQyxpQkFBaUIsQ0FBQyxHQUFHLEVBQUU7O1lBQ2xDLElBQUksYUFBYSxDQUFDLE1BQU0sRUFBRTtnQkFDeEIsT0FBTzthQUNSO1lBQ0QsSUFBSSxDQUFDLENBQUMsYUFBYSxJQUFJLElBQUksQ0FBQyxTQUFTLElBQUksQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDLFNBQVMsQ0FBQyxFQUFFO2dCQUNuRSxPQUFPO2FBQ1I7WUFDRCxLQUFLLE1BQU0sR0FBRyxJQUFJLGFBQWEsRUFBRTtnQkFDL0IsSUFBSSxpQkFBaUIsQ0FBQyxPQUFPLENBQUMsR0FBRyxDQUFDLElBQUksQ0FBQyxFQUFFO29CQUN2QyxTQUFTO2lCQUNWO2dCQUNELE1BQU0sUUFBUSxHQUFHLE1BQUEsTUFBQSxhQUFhLENBQUMsR0FBRyxDQUFDLDBDQUFFLFlBQVksbUNBQUksYUFBYSxDQUFDLEdBQUcsQ0FBQyxDQUFDO2dCQUN4RSxJQUFJLENBQUMsZUFBZSxDQUFDLEdBQUcsRUFBRSxRQUFRLENBQUMsQ0FBQzthQUNyQztZQUVELElBQUksYUFBYSxDQUFDLGNBQWMsRUFBRTtnQkFDaEMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxjQUFjLEdBQUcsSUFBSSxDQUFDLGNBQWMsQ0FBQzthQUNyRDtZQUNELElBQUksYUFBYSxDQUFDLGNBQWMsRUFBRTtnQkFDaEMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxjQUFjLEdBQUcsSUFBSSxDQUFDLGNBQWMsQ0FBQzthQUNyRDtZQUNELElBQUksYUFBYSxDQUFDLFNBQVMsRUFBRTtnQkFDM0IsSUFBSSxDQUFDLFNBQVMsQ0FBQyxlQUFlLENBQUMsSUFBSSxDQUFDLFNBQVMsRUFBRSxLQUFLLENBQUMsQ0FBQzthQUN2RDtZQUNELElBQUksYUFBYSxDQUFDLFdBQVcsRUFBRTtnQkFDN0IsSUFBSSxJQUFJLENBQUMsSUFBSSxJQUFJLENBQUMsSUFBSSxDQUFDLFlBQVksRUFBRTtvQkFDbkMsSUFBSSxDQUFDLGdCQUFnQixFQUFFLENBQUM7aUJBQ3pCO2dCQUNELElBQUksQ0FBQyxTQUFTLENBQUMsaUJBQWlCLEdBQUcsSUFBSSxDQUFDO2dCQUN4QyxJQUFJLENBQUMsU0FBUyxDQUFDLGFBQWEsRUFBRSxDQUFDO2FBQ2hDO1lBRUQsSUFBSSxhQUFhLENBQUMsTUFBTSxJQUFJLGFBQWEsQ0FBQyxVQUFVLEVBQUU7Z0JBQ3BELElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxhQUFhLENBQUMsQ0FBQzthQUN0QztZQUNELElBQUksQ0FBQyxTQUFTLENBQUMsTUFBTSxFQUFFLENBQUM7UUFDMUIsQ0FBQyxDQUFDLENBQUM7SUFDTCxDQUFDO0lBRUQsZ0JBQWdCO1FBQ2QsSUFBSSxDQUFDLElBQUksQ0FBQyxJQUFJLEVBQUU7WUFDZCxPQUFPO1NBQ1I7UUFDRCxJQUFJLG1CQUFtQixHQUFHLElBQUksQ0FBQyxhQUFhLENBQUM7UUFDN0MsSUFBSSxJQUFJLENBQUMsV0FBVyxFQUFFO1lBQ3BCLE1BQU0sVUFBVSxHQUFHLE1BQU0sQ0FBQyxTQUFTLENBQUMsYUFBYSxDQUFDLElBQUksQ0FBQyxXQUFXLENBQUMsQ0FBQztZQUNwRSxNQUFNLG9CQUFvQixHQUN4QixVQUFVLElBQUksSUFBSSxDQUFDLFdBQVcsQ0FBQyxDQUFDLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxVQUFVLENBQUMsQ0FBQyxDQUFDLENBQUMsU0FBUyxDQUFDO1lBQzVFLElBQUksb0JBQW9CLElBQUksb0JBQW9CLENBQUMsYUFBYSxFQUFFO2dCQUM5RCxtQkFBbUIsR0FBRyxvQkFBb0IsQ0FBQyxhQUFhLENBQUM7YUFDMUQ7U0FDRjtRQUNELElBQUksbUJBQW1CLEtBQUssTUFBTSxFQUFFO1lBQ2xDLElBQUksQ0FBQyxZQUFZLEdBQUcsSUFBSSxDQUFDLE1BQU0sQ0FBQyxNQUFNLENBQUM7WUFDdkMsT0FBTyxJQUFJLENBQUMsTUFBTSxDQUFDLE1BQU0sQ0FBQztTQUMzQjtRQUNELElBQUksWUFBWSxHQUFHLElBQUksQ0FBQyxZQUFZLElBQUksbUJBQW1CLENBQUM7UUFDNUQsSUFBSSxDQUFDLFlBQVksRUFBRTtZQUNqQixJQUFJO1lBQ0osT0FBTztTQUNSO1FBRUQsSUFBSSxJQUFJLENBQUMsb0JBQW9CLEVBQUU7WUFDN0IsWUFBWSxJQUFJLElBQUksQ0FBQyxvQkFBb0IsQ0FBQztTQUMzQztRQUNELElBQUksWUFBWSxHQUFHLElBQUksQ0FBQyxNQUFNLENBQUMsTUFBTSxFQUFFO1lBQ3JDLFlBQVksR0FBRyxJQUFJLENBQUMsTUFBTSxDQUFDLE1BQU0sQ0FBQztTQUNuQztRQUNELElBQUksQ0FBQyxZQUFZLEdBQUcsWUFBWSxDQUFDO1FBQ2pDLE9BQU8sWUFBWSxDQUFDO0lBQ3RCLENBQUM7SUFFRCxlQUFlLENBQUMsR0FBVyxFQUFFLEtBQVU7UUFDckMsSUFBSSxDQUFDLENBQUMsSUFBSSxDQUFDLFNBQVMsSUFBSSxDQUFDLElBQUksQ0FBQyxTQUFTLENBQUMsU0FBUyxDQUFDLEVBQUU7WUFDbEQsT0FBTztTQUNSO1FBQ0QsTUFBTSxJQUFJLEdBQUcsR0FBRyxDQUFDLE9BQU8sQ0FBQyxJQUFJLEVBQUUsRUFBRSxDQUFDLENBQUM7UUFDbkMsTUFBTSxpQkFBaUIsR0FBRyxRQUFRLENBQUMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQztRQUVoRSxJQUFJLE1BQU0sQ0FBQyxJQUFJLENBQUMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxPQUFPLENBQUMsQ0FBQyxPQUFPLENBQUMsSUFBSSxDQUFDLElBQUksQ0FBQyxFQUFFO1lBQzFELE1BQU0sYUFBYSxHQUFHLElBQUksQ0FBQyxTQUFTLENBQUMsT0FBTyxDQUFDLElBQUksQ0FBQyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsQ0FBQztZQUNoRSxJQUFJLGlCQUFpQixFQUFFO2dCQUNyQixNQUFNLENBQUMsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLEVBQUUsYUFBYSxDQUFDLENBQUM7YUFDcEQ7aUJBQU07Z0JBQ0wsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLEdBQUcsYUFBYSxDQUFDO2FBQzdDO1NBQ0Y7UUFDRCxJQUFJLElBQUksS0FBSyxTQUFTLEVBQUU7WUFDdEIsSUFBSSxLQUFLLEtBQUssSUFBSSxFQUFFO2dCQUNsQixJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sRUFBRSxDQUFDO2FBQ3pCO2lCQUFNLElBQUksS0FBSyxLQUFLLEtBQUssRUFBRTtnQkFDMUIsSUFBSSxDQUFDLFNBQVMsQ0FBQyxPQUFPLEVBQUUsQ0FBQzthQUMxQjtZQUNELE9BQU87U0FDUjtRQUNELElBQUksaUJBQWlCLElBQUksUUFBUSxDQUFDLEtBQUssQ0FBQyxFQUFFO1lBQ3hDLE1BQU0sQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsRUFBRSxLQUFLLENBQUMsQ0FBQztTQUM1QzthQUFNO1lBQ0wsSUFBSSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLEdBQUcsS0FBSyxDQUFDO1NBQ3JDO0lBQ0gsQ0FBQztJQUNEOztPQUVHO0lBQ0gsUUFBUSxDQUFDLEtBQWEsRUFBRSxLQUFjLEVBQUUsTUFBZ0I7UUFDdEQsSUFBSSxDQUFDLElBQUksQ0FBQyxjQUFjLEVBQUU7WUFDeEIsSUFBSSxDQUFDLFlBQVksR0FBRyxLQUFLLENBQUM7WUFDMUIsT0FBTztTQUNSO1FBQ0QsSUFBSSxLQUFLLEtBQUssSUFBSSxDQUFDLFNBQVMsQ0FBQyxXQUFXLEVBQUU7WUFDeEMsT0FBTztTQUNSO1FBQ0QsSUFBSSxDQUFDLE9BQU8sQ0FBQyxpQkFBaUIsQ0FBQyxHQUFHLEVBQUU7WUFDbEMsSUFBSSxJQUFJLENBQUMsSUFBSSxFQUFFO2dCQUNiLElBQUksQ0FBQyxTQUFTLENBQUMsV0FBVyxDQUFDLEtBQUssRUFBRSxLQUFLLEVBQUUsQ0FBQyxNQUFNLENBQUMsQ0FBQzthQUNuRDtpQkFBTTtnQkFDTCxJQUFJLENBQUMsU0FBUyxDQUFDLE9BQU8sQ0FBQyxLQUFLLEVBQUUsS0FBSyxFQUFFLENBQUMsTUFBTSxDQUFDLENBQUM7YUFDL0M7UUFDSCxDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7SUFFRCxXQUFXO1FBQ1QsSUFBSSxDQUFDLE9BQU8sQ0FBQyxpQkFBaUIsQ0FBQyxHQUFHLEVBQUU7O1lBQ2xDLE1BQUEsSUFBSSxDQUFDLFNBQVMsMENBQUUsT0FBTyxDQUFDLElBQUksRUFBRSxLQUFLLENBQUMsQ0FBQztRQUN2QyxDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7OzRHQTN4QlUsZUFBZSxtR0E4WWhCLFdBQVc7Z0dBOVlWLGVBQWUsa3pNQWtYVCxvQkFBb0IsMmJDeGF2QyxzckVBdUVBOzJGRGpCYSxlQUFlO2tCQWIzQixTQUFTO21CQUFDO29CQUNULFFBQVEsRUFBRSxrQkFBa0I7b0JBQzVCLFdBQVcsRUFBRSx5QkFBeUI7b0JBQ3RDLGVBQWUsRUFBRSx1QkFBdUIsQ0FBQyxNQUFNO29CQUMvQyxhQUFhLEVBQUUsaUJBQWlCLENBQUMsSUFBSTtvQkFDckMsTUFBTSxFQUFFO3dCQUNOOzs7O0tBSUM7cUJBQ0Y7aUJBQ0Y7d0lBK1k2QyxNQUFNOzBCQUEvQyxNQUFNOzJCQUFDLFdBQVc7NENBN1laLE9BQU87c0JBQWYsS0FBSztnQkFDRyxTQUFTO3NCQUFqQixLQUFLO2dCQUNHLGlCQUFpQjtzQkFBekIsS0FBSztnQkFDRyxZQUFZO3NCQUFwQixLQUFLO2dCQUNHLEtBQUs7c0JBQWIsS0FBSztnQkFDRyxPQUFPO3NCQUFmLEtBQUs7Z0JBQ0csb0JBQW9CO3NCQUE1QixLQUFLO2dCQUNHLGNBQWM7c0JBQXRCLEtBQUs7Z0JBQ0csTUFBTTtzQkFBZCxLQUFLO2dCQUNHLGlCQUFpQjtzQkFBekIsS0FBSztnQkFDRyxLQUFLO3NCQUFiLEtBQUs7Z0JBQ0csTUFBTTtzQkFBZCxLQUFLO2dCQUNHLDhCQUE4QjtzQkFBdEMsS0FBSztnQkFDRyxTQUFTO3NCQUFqQixLQUFLO2dCQUNHLEdBQUc7c0JBQVgsS0FBSztnQkFDRyxrQkFBa0I7c0JBQTFCLEtBQUs7Z0JBQ0csa0JBQWtCO3NCQUExQixLQUFLO2dCQUNHLFFBQVE7c0JBQWhCLEtBQUs7Z0JBQ0csVUFBVTtzQkFBbEIsS0FBSztnQkFDRyxjQUFjO3NCQUF0QixLQUFLO2dCQUNHLGdCQUFnQjtzQkFBeEIsS0FBSztnQkFDRyxNQUFNO3NCQUFkLEtBQUs7Z0JBQ0csV0FBVztzQkFBbkIsS0FBSztnQkFDRyxZQUFZO3NCQUFwQixLQUFLO2dCQUNHLGFBQWE7c0JBQXJCLEtBQUs7Z0JBQ0csSUFBSTtzQkFBWixLQUFLO2dCQUNHLGNBQWM7c0JBQXRCLEtBQUs7Z0JBQ0csa0JBQWtCO3NCQUExQixLQUFLO2dCQUNHLGNBQWM7c0JBQXRCLEtBQUs7Z0JBQ0csb0JBQW9CO3NCQUE1QixLQUFLO2dCQUNHLGtCQUFrQjtzQkFBMUIsS0FBSztnQkFDRyxpQkFBaUI7c0JBQXpCLEtBQUs7Z0JBQ0csbUJBQW1CO3NCQUEzQixLQUFLO2dCQUNHLHdCQUF3QjtzQkFBaEMsS0FBSztnQkFDRyxhQUFhO3NCQUFyQixLQUFLO2dCQUNHLFlBQVk7c0JBQXBCLEtBQUs7Z0JBQ0csVUFBVTtzQkFBbEIsS0FBSztnQkFDRyxVQUFVO3NCQUFsQixLQUFLO2dCQUNHLGFBQWE7c0JBQXJCLEtBQUs7Z0JBQ0csV0FBVztzQkFBbkIsS0FBSztnQkFDRyxVQUFVO3NCQUFsQixLQUFLO2dCQUNHLGVBQWU7c0JBQXZCLEtBQUs7Z0JBQ0csWUFBWTtzQkFBcEIsS0FBSztnQkFDRyxZQUFZO3NCQUFwQixLQUFLO2dCQUNHLGNBQWM7c0JBQXRCLEtBQUs7Z0JBQ0csU0FBUztzQkFBakIsS0FBSztnQkFDRyx3QkFBd0I7c0JBQWhDLEtBQUs7Z0JBQ0csd0JBQXdCO3NCQUFoQyxLQUFLO2dCQUNHLDZCQUE2QjtzQkFBckMsS0FBSztnQkFDRyxtQkFBbUI7c0JBQTNCLEtBQUs7Z0JBQ0csaUJBQWlCO3NCQUF6QixLQUFLO2dCQUNHLFVBQVU7c0JBQWxCLEtBQUs7Z0JBQ0csZUFBZTtzQkFBdkIsS0FBSztnQkFDRyxtQkFBbUI7c0JBQTNCLEtBQUs7Z0JBQ0csVUFBVTtzQkFBbEIsS0FBSztnQkFDRyxhQUFhO3NCQUFyQixLQUFLO2dCQUNHLHdCQUF3QjtzQkFBaEMsS0FBSztnQkFDRyxtQkFBbUI7c0JBQTNCLEtBQUs7Z0JBQ0csYUFBYTtzQkFBckIsS0FBSztnQkFDRyxtQkFBbUI7c0JBQTNCLEtBQUs7Z0JBQ0csSUFBSTtzQkFBWixLQUFLO2dCQUNHLG9CQUFvQjtzQkFBNUIsS0FBSztnQkFDRyxZQUFZO3NCQUFwQixLQUFLO2dCQUNHLHNCQUFzQjtzQkFBOUIsS0FBSztnQkFDRyxpQkFBaUI7c0JBQXpCLEtBQUs7Z0JBQ0csY0FBYztzQkFBdEIsS0FBSztnQkFDRyxjQUFjO3NCQUF0QixLQUFLO2dCQUNHLFlBQVk7c0JBQXBCLEtBQUs7Z0JBQ0csU0FBUztzQkFBakIsS0FBSztnQkFDRyxjQUFjO3NCQUF0QixLQUFLO2dCQUNHLGlCQUFpQjtzQkFBekIsS0FBSztnQkFDRyxnQkFBZ0I7c0JBQXhCLEtBQUs7Z0JBQ0csc0JBQXNCO3NCQUE5QixLQUFLO2dCQUNHLFVBQVU7c0JBQWxCLEtBQUs7Z0JBQ0csZUFBZTtzQkFBdkIsS0FBSztnQkFDRyxnQkFBZ0I7c0JBQXhCLEtBQUs7Z0JBQ0cseUJBQXlCO3NCQUFqQyxLQUFLO2dCQUNHLGlCQUFpQjtzQkFBekIsS0FBSztnQkFDRyxtQkFBbUI7c0JBQTNCLEtBQUs7Z0JBQ0csY0FBYztzQkFBdEIsS0FBSztnQkFDRyx1QkFBdUI7c0JBQS9CLEtBQUs7Z0JBQ0csY0FBYztzQkFBdEIsS0FBSztnQkFDRyx1QkFBdUI7c0JBQS9CLEtBQUs7Z0JBQ0csWUFBWTtzQkFBcEIsS0FBSztnQkFDRyxrQkFBa0I7c0JBQTFCLEtBQUs7Z0JBQ0csY0FBYztzQkFBdEIsS0FBSztnQkFDRyxvQkFBb0I7c0JBQTVCLEtBQUs7Z0JBQ0csSUFBSTtzQkFBWixLQUFLO2dCQUNHLFFBQVE7c0JBQWhCLEtBQUs7Z0JBQ0csVUFBVTtzQkFBbEIsS0FBSztnQkFDRyxlQUFlO3NCQUF2QixLQUFLO2dCQUNHLFVBQVU7c0JBQWxCLEtBQUs7Z0JBQ0csVUFBVTtzQkFBbEIsS0FBSztnQkFDRyxVQUFVO3NCQUFsQixLQUFLO2dCQUNHLGNBQWM7c0JBQXRCLEtBQUs7Z0JBQ0csV0FBVztzQkFBbkIsS0FBSztnQkFDRyxjQUFjO3NCQUF0QixLQUFLO2dCQUNHLE9BQU87c0JBQWYsS0FBSztnQkFDRyxRQUFRO3NCQUFoQixLQUFLO2dCQUNHLElBQUk7c0JBQVosS0FBSztnQkFDRyxVQUFVO3NCQUFsQixLQUFLO2dCQUNHLFFBQVE7c0JBQWhCLEtBQUs7Z0JBQ0csTUFBTTtzQkFBZCxLQUFLO2dCQUNHLElBQUk7c0JBQVosS0FBSztnQkFDRyxLQUFLO3NCQUFiLEtBQUs7Z0JBQ0csRUFBRTtzQkFBVixLQUFLO2dCQUVGLFVBQVU7c0JBRGIsS0FBSztnQkFpQ0YsVUFBVTtzQkFEYixLQUFLO2dCQWtCRixTQUFTO3NCQURaLEtBQUs7Z0JBZ0JGLE9BQU87c0JBRFYsS0FBSztnQkFVRixLQUFLO3NCQURSLEtBQUs7Z0JBTUYsTUFBTTtzQkFEVCxLQUFLO2dCQU91QixtQkFBbUI7c0JBQS9DLE1BQU07dUJBQUMsbUJBQW1CO2dCQUVFLG1CQUFtQjtzQkFBL0MsTUFBTTt1QkFBQyxtQkFBbUI7Z0JBRUosYUFBYTtzQkFBbkMsTUFBTTt1QkFBQyxhQUFhO2dCQUVGLFNBQVM7c0JBQTNCLE1BQU07dUJBQUMsU0FBUztnQkFFWSxtQkFBbUI7c0JBQS9DLE1BQU07dUJBQUMsbUJBQW1CO2dCQUVOLFdBQVc7c0JBQS9CLE1BQU07dUJBQUMsV0FBVztnQkFFQyxVQUFVO3NCQUE3QixNQUFNO3VCQUFDLFVBQVU7Z0JBRU8sZUFBZTtzQkFBdkMsTUFBTTt1QkFBQyxlQUFlO2dCQUVDLGNBQWM7c0JBQXJDLE1BQU07dUJBQUMsY0FBYztnQkFFRyxlQUFlO3NCQUF2QyxNQUFNO3VCQUFDLGVBQWU7Z0JBRUQsWUFBWTtzQkFBakMsTUFBTTt1QkFBQyxZQUFZO2dCQUVLLGVBQWU7c0JBQXZDLE1BQU07dUJBQUMsZUFBZTtnQkFFQyxjQUFjO3NCQUFyQyxNQUFNO3VCQUFDLGNBQWM7Z0JBRVksd0JBQXdCO3NCQUF6RCxNQUFNO3VCQUFDLHdCQUF3QjtnQkFFQyx1QkFBdUI7c0JBQXZELE1BQU07dUJBQUMsdUJBQXVCO2dCQUVULFlBQVk7c0JBQWpDLE1BQU07dUJBQUMsWUFBWTtnQkFFTyxpQkFBaUI7c0JBQTNDLE1BQU07dUJBQUMsaUJBQWlCO2dCQUVSLE9BQU87c0JBQXZCLE1BQU07dUJBQUMsT0FBTztnQkFFTSxXQUFXO3NCQUEvQixNQUFNO3VCQUFDLFdBQVc7Z0JBRUksYUFBYTtzQkFBbkMsTUFBTTt1QkFBQyxhQUFhO2dCQUVGLFNBQVM7c0JBQTNCLE1BQU07dUJBQUMsU0FBUztnQkFFRyxVQUFVO3NCQUE3QixNQUFNO3VCQUFDLFVBQVU7Z0JBRUksWUFBWTtzQkFBakMsTUFBTTt1QkFBQyxZQUFZO2dCQUVELFNBQVM7c0JBQTNCLE1BQU07dUJBQUMsU0FBUztnQkFFTSxhQUFhO3NCQUFuQyxNQUFNO3VCQUFDLGFBQWE7Z0JBRUwsTUFBTTtzQkFBckIsTUFBTTt1QkFBQyxNQUFNO2dCQUVNLFVBQVU7c0JBQTdCLE1BQU07dUJBQUMsVUFBVTtnQkFFTyxlQUFlO3NCQUF2QyxNQUFNO3VCQUFDLGVBQWU7Z0JBRUcsZ0JBQWdCO3NCQUF6QyxNQUFNO3VCQUFDLGdCQUFnQjtnQkFFTCxTQUFTO3NCQUEzQixNQUFNO3VCQUFDLFNBQVM7Z0JBRVMsZ0JBQWdCO3NCQUF6QyxNQUFNO3VCQUFDLGdCQUFnQjtnQkFFRSxnQkFBZ0I7c0JBQXpDLE1BQU07dUJBQUMsZ0JBQWdCO2dCQUVFLGdCQUFnQjtzQkFBekMsTUFBTTt1QkFBQyxnQkFBZ0I7Z0JBRUUsZ0JBQWdCO3NCQUF6QyxNQUFNO3VCQUFDLGdCQUFnQjtnQkFFSyxtQkFBbUI7c0JBQS9DLE1BQU07dUJBQUMsbUJBQW1CO2dCQUVELGdCQUFnQjtzQkFBekMsTUFBTTt1QkFBQyxnQkFBZ0I7Z0JBRUksa0JBQWtCO3NCQUE3QyxNQUFNO3VCQUFDLGtCQUFrQjtnQkFFQSxnQkFBZ0I7c0JBQXpDLE1BQU07dUJBQUMsZ0JBQWdCO2dCQUVJLGtCQUFrQjtzQkFBN0MsTUFBTTt1QkFBQyxrQkFBa0I7Z0JBRU4sVUFBVTtzQkFBN0IsTUFBTTt1QkFBQyxVQUFVO2dCQUVRLGdCQUFnQjtzQkFBekMsTUFBTTt1QkFBQyxnQkFBZ0I7Z0JBRUosVUFBVTtzQkFBN0IsTUFBTTt1QkFBQyxVQUFVO2dCQUVTLGlCQUFpQjtzQkFBM0MsTUFBTTt1QkFBQyxpQkFBaUI7Z0JBRVAsUUFBUTtzQkFBekIsTUFBTTt1QkFBQyxRQUFRO2dCQUVFLFFBQVE7c0JBQXpCLE1BQU07dUJBQUMsUUFBUTtnQkFFWSxrQkFBa0I7c0JBQTdDLE1BQU07dUJBQUMsa0JBQWtCO2dCQUVHLG1CQUFtQjtzQkFBL0MsTUFBTTt1QkFBQyxtQkFBbUI7Z0JBRUcsb0JBQW9CO3NCQUFqRCxNQUFNO3VCQUFDLG9CQUFvQjtnQkFFSCxlQUFlO3NCQUF2QyxNQUFNO3VCQUFDLGVBQWU7Z0JBRUMsY0FBYztzQkFBckMsTUFBTTt1QkFBQyxjQUFjO2dCQUVDLGFBQWE7c0JBQW5DLE1BQU07dUJBQUMsYUFBYTtnQkFFZSwwQkFBMEI7c0JBQTdELE1BQU07dUJBQUMsMEJBQTBCO2dCQUVJLDRCQUE0QjtzQkFBakUsTUFBTTt1QkFBQyw0QkFBNEI7Z0JBRUYsd0JBQXdCO3NCQUF6RCxNQUFNO3VCQUFDLHdCQUF3QjtnQkFFSSwwQkFBMEI7c0JBQTdELE1BQU07dUJBQUMsMEJBQTBCO2dCQUVBLHdCQUF3QjtzQkFBekQsTUFBTTt1QkFBQyx3QkFBd0I7Z0JBRUksMEJBQTBCO3NCQUE3RCxNQUFNO3VCQUFDLDBCQUEwQjtnQkFFRywyQkFBMkI7c0JBQS9ELE1BQU07dUJBQUMsMkJBQTJCO2dCQUVBLHlCQUF5QjtzQkFBM0QsTUFBTTt1QkFBQyx5QkFBeUI7Z0JBRVgsWUFBWTtzQkFBakMsTUFBTTt1QkFBQyxZQUFZO2dCQUVPLGlCQUFpQjtzQkFBM0MsTUFBTTt1QkFBQyxpQkFBaUI7Z0JBRUssb0JBQW9CO3NCQUFqRCxNQUFNO3VCQUFDLG9CQUFvQjtnQkFFTSx3QkFBd0I7c0JBQXpELE1BQU07dUJBQUMsd0JBQXdCO2dCQUVBLHNCQUFzQjtzQkFBckQsTUFBTTt1QkFBQyxzQkFBc0I7Z0JBRUgsaUJBQWlCO3NCQUEzQyxNQUFNO3VCQUFDLGlCQUFpQjtnQkFFVixLQUFLO3NCQUFuQixNQUFNO3VCQUFDLEtBQUs7Z0JBRUssUUFBUTtzQkFBekIsTUFBTTt1QkFBQyxRQUFRO2dCQUVJLFVBQVU7c0JBQTdCLE1BQU07dUJBQUMsVUFBVTtnQkFFRyxXQUFXO3NCQUEvQixNQUFNO3VCQUFDLFdBQVc7Z0JBRVUsbUJBQW1CO3NCQUEvQyxNQUFNO3VCQUFDLG1CQUFtQjtnQkFFTCxZQUFZO3NCQUFqQyxNQUFNO3VCQUFDLFlBQVk7Z0JBRUssZUFBZTtzQkFBdkMsTUFBTTt1QkFBQyxlQUFlO2dCQUVJLGlCQUFpQjtzQkFBM0MsTUFBTTt1QkFBQyxpQkFBaUI7Z0JBRVAsUUFBUTtzQkFBekIsTUFBTTt1QkFBQyxRQUFRO2dCQUVNLFlBQVk7c0JBQWpDLE1BQU07dUJBQUMsWUFBWTtnQkFFRixRQUFRO3NCQUF6QixNQUFNO3VCQUFDLFFBQVE7Z0JBRU4sV0FBVztzQkFBcEIsTUFBTTtnQkFHSCxTQUFTO3NCQURaLFNBQVM7dUJBQUMsV0FBVyxFQUFFLEVBQUUsTUFBTSxFQUFFLEtBQUssRUFBRTtnQkFPckMsU0FBUztzQkFEWixTQUFTO3VCQUFDLFdBQVcsRUFBRSxFQUFFLE1BQU0sRUFBRSxLQUFLLEVBQUU7Z0JBT3JDLGNBQWM7c0JBRGpCLFNBQVM7dUJBQUMsZ0JBQWdCLEVBQUUsRUFBRSxNQUFNLEVBQUUsS0FBSyxFQUFFO2dCQU8xQyxlQUFlO3NCQURsQixTQUFTO3VCQUFDLGlCQUFpQixFQUFFLEVBQUUsTUFBTSxFQUFFLEtBQUssRUFBRTtnQkFPL0MsUUFBUTtzQkFEUCxlQUFlO3VCQUFDLG9CQUFvQixFQUFFLEVBQUUsV0FBVyxFQUFFLEtBQUssRUFBRSx1QkFBdUIsRUFBRSxJQUFJLEVBQUU7Z0JBdUJ0RSxnQkFBZ0I7c0JBQXJDLFdBQVc7dUJBQUMsT0FBTyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7XG4gIENoYW5nZURldGVjdGlvblN0cmF0ZWd5LFxuICBDaGFuZ2VEZXRlY3RvclJlZixcbiAgQ29tcG9uZW50LFxuICBDb250ZW50Q2hpbGRyZW4sXG4gIEVsZW1lbnRSZWYsXG4gIEV2ZW50RW1pdHRlcixcbiAgSG9zdEJpbmRpbmcsXG4gIEluamVjdCxcbiAgSW5wdXQsXG4gIE5nWm9uZSxcbiAgT25Jbml0LFxuICBPdXRwdXQsXG4gIFBMQVRGT1JNX0lELFxuICBRdWVyeUxpc3QsXG4gIFNpbXBsZUNoYW5nZXMsXG4gIFZpZXdDaGlsZCxcbiAgVmlld0VuY2Fwc3VsYXRpb24sXG59IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuLy8gQHRzLWlnbm9yZVxuaW1wb3J0IFN3aXBlciBmcm9tICdzd2lwZXInO1xuaW1wb3J0IHsgT2JzZXJ2YWJsZSwgb2YsIFN1YmplY3QgfSBmcm9tICdyeGpzJztcbmltcG9ydCB7IGdldFBhcmFtcyB9IGZyb20gJy4vdXRpbHMvZ2V0LXBhcmFtcyc7XG5pbXBvcnQgeyBTd2lwZXJTbGlkZURpcmVjdGl2ZSB9IGZyb20gJy4vc3dpcGVyLXNsaWRlLmRpcmVjdGl2ZSc7XG5pbXBvcnQge1xuICBleHRlbmQsXG4gIGlzT2JqZWN0LFxuICBzZXRQcm9wZXJ0eSxcbiAgaWdub3JlTmdPbkNoYW5nZXMsXG4gIGNvZXJjZUJvb2xlYW5Qcm9wZXJ0eSxcbiAgaXNTaG93RWwsXG59IGZyb20gJy4vdXRpbHMvdXRpbHMnO1xuaW1wb3J0IHtcbiAgU3dpcGVyT3B0aW9ucyxcbiAgU3dpcGVyRXZlbnRzLFxuICBOYXZpZ2F0aW9uT3B0aW9ucyxcbiAgUGFnaW5hdGlvbk9wdGlvbnMsXG4gIFNjcm9sbGJhck9wdGlvbnMsXG4gIFZpcnR1YWxPcHRpb25zLFxufSBmcm9tICdzd2lwZXIvdHlwZXMnO1xuaW1wb3J0IHsgaXNQbGF0Zm9ybUJyb3dzZXIgfSBmcm9tICdAYW5ndWxhci9jb21tb24nO1xuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnc3dpcGVyLCBbc3dpcGVyXScsXG4gIHRlbXBsYXRlVXJsOiAnLi9zd2lwZXIuY29tcG9uZW50Lmh0bWwnLFxuICBjaGFuZ2VEZXRlY3Rpb246IENoYW5nZURldGVjdGlvblN0cmF0ZWd5Lk9uUHVzaCxcbiAgZW5jYXBzdWxhdGlvbjogVmlld0VuY2Fwc3VsYXRpb24uTm9uZSxcbiAgc3R5bGVzOiBbXG4gICAgYFxuICAgICAgc3dpcGVyIHtcbiAgICAgICAgZGlzcGxheTogYmxvY2s7XG4gICAgICB9XG4gICAgYCxcbiAgXSxcbn0pXG5leHBvcnQgY2xhc3MgU3dpcGVyQ29tcG9uZW50IGltcGxlbWVudHMgT25Jbml0IHtcbiAgQElucHV0KCkgZW5hYmxlZDogU3dpcGVyT3B0aW9uc1snZW5hYmxlZCddO1xuICBASW5wdXQoKSBkaXJlY3Rpb246IFN3aXBlck9wdGlvbnNbJ2RpcmVjdGlvbiddO1xuICBASW5wdXQoKSB0b3VjaEV2ZW50c1RhcmdldDogU3dpcGVyT3B0aW9uc1sndG91Y2hFdmVudHNUYXJnZXQnXTtcbiAgQElucHV0KCkgaW5pdGlhbFNsaWRlOiBTd2lwZXJPcHRpb25zWydpbml0aWFsU2xpZGUnXTtcbiAgQElucHV0KCkgc3BlZWQ6IFN3aXBlck9wdGlvbnNbJ3NwZWVkJ107XG4gIEBJbnB1dCgpIGNzc01vZGU6IFN3aXBlck9wdGlvbnNbJ2Nzc01vZGUnXTtcbiAgQElucHV0KCkgdXBkYXRlT25XaW5kb3dSZXNpemU6IFN3aXBlck9wdGlvbnNbJ3VwZGF0ZU9uV2luZG93UmVzaXplJ107XG4gIEBJbnB1dCgpIHJlc2l6ZU9ic2VydmVyOiBTd2lwZXJPcHRpb25zWydyZXNpemVPYnNlcnZlciddO1xuICBASW5wdXQoKSBuZXN0ZWQ6IFN3aXBlck9wdGlvbnNbJ25lc3RlZCddO1xuICBASW5wdXQoKSBmb2N1c2FibGVFbGVtZW50czogU3dpcGVyT3B0aW9uc1snZm9jdXNhYmxlRWxlbWVudHMnXTtcbiAgQElucHV0KCkgd2lkdGg6IFN3aXBlck9wdGlvbnNbJ3dpZHRoJ107XG4gIEBJbnB1dCgpIGhlaWdodDogU3dpcGVyT3B0aW9uc1snaGVpZ2h0J107XG4gIEBJbnB1dCgpIHByZXZlbnRJbnRlcmFjdGlvbk9uVHJhbnNpdGlvbjogU3dpcGVyT3B0aW9uc1sncHJldmVudEludGVyYWN0aW9uT25UcmFuc2l0aW9uJ107XG4gIEBJbnB1dCgpIHVzZXJBZ2VudDogU3dpcGVyT3B0aW9uc1sndXNlckFnZW50J107XG4gIEBJbnB1dCgpIHVybDogU3dpcGVyT3B0aW9uc1sndXJsJ107XG4gIEBJbnB1dCgpIGVkZ2VTd2lwZURldGVjdGlvbjogYm9vbGVhbiB8IHN0cmluZztcbiAgQElucHV0KCkgZWRnZVN3aXBlVGhyZXNob2xkOiBudW1iZXI7XG4gIEBJbnB1dCgpIGZyZWVNb2RlOiBTd2lwZXJPcHRpb25zWydmcmVlTW9kZSddO1xuICBASW5wdXQoKSBhdXRvSGVpZ2h0OiBTd2lwZXJPcHRpb25zWydhdXRvSGVpZ2h0J107XG4gIEBJbnB1dCgpIHNldFdyYXBwZXJTaXplOiBTd2lwZXJPcHRpb25zWydzZXRXcmFwcGVyU2l6ZSddO1xuICBASW5wdXQoKSB2aXJ0dWFsVHJhbnNsYXRlOiBTd2lwZXJPcHRpb25zWyd2aXJ0dWFsVHJhbnNsYXRlJ107XG4gIEBJbnB1dCgpIGVmZmVjdDogU3dpcGVyT3B0aW9uc1snZWZmZWN0J107XG4gIEBJbnB1dCgpIGJyZWFrcG9pbnRzOiBTd2lwZXJPcHRpb25zWydicmVha3BvaW50cyddO1xuICBASW5wdXQoKSBzcGFjZUJldHdlZW46IFN3aXBlck9wdGlvbnNbJ3NwYWNlQmV0d2VlbiddO1xuICBASW5wdXQoKSBzbGlkZXNQZXJWaWV3OiBTd2lwZXJPcHRpb25zWydzbGlkZXNQZXJWaWV3J107XG4gIEBJbnB1dCgpIGdyaWQ6IFN3aXBlck9wdGlvbnNbJ2dyaWQnXTtcbiAgQElucHV0KCkgc2xpZGVzUGVyR3JvdXA6IFN3aXBlck9wdGlvbnNbJ3NsaWRlc1Blckdyb3VwJ107XG4gIEBJbnB1dCgpIHNsaWRlc1Blckdyb3VwU2tpcDogU3dpcGVyT3B0aW9uc1snc2xpZGVzUGVyR3JvdXBTa2lwJ107XG4gIEBJbnB1dCgpIGNlbnRlcmVkU2xpZGVzOiBTd2lwZXJPcHRpb25zWydjZW50ZXJlZFNsaWRlcyddO1xuICBASW5wdXQoKSBjZW50ZXJlZFNsaWRlc0JvdW5kczogU3dpcGVyT3B0aW9uc1snY2VudGVyZWRTbGlkZXNCb3VuZHMnXTtcbiAgQElucHV0KCkgc2xpZGVzT2Zmc2V0QmVmb3JlOiBTd2lwZXJPcHRpb25zWydzbGlkZXNPZmZzZXRCZWZvcmUnXTtcbiAgQElucHV0KCkgc2xpZGVzT2Zmc2V0QWZ0ZXI6IFN3aXBlck9wdGlvbnNbJ3NsaWRlc09mZnNldEFmdGVyJ107XG4gIEBJbnB1dCgpIG5vcm1hbGl6ZVNsaWRlSW5kZXg6IFN3aXBlck9wdGlvbnNbJ25vcm1hbGl6ZVNsaWRlSW5kZXgnXTtcbiAgQElucHV0KCkgY2VudGVySW5zdWZmaWNpZW50U2xpZGVzOiBTd2lwZXJPcHRpb25zWydjZW50ZXJJbnN1ZmZpY2llbnRTbGlkZXMnXTtcbiAgQElucHV0KCkgd2F0Y2hPdmVyZmxvdzogU3dpcGVyT3B0aW9uc1snd2F0Y2hPdmVyZmxvdyddO1xuICBASW5wdXQoKSByb3VuZExlbmd0aHM6IFN3aXBlck9wdGlvbnNbJ3JvdW5kTGVuZ3RocyddO1xuICBASW5wdXQoKSB0b3VjaFJhdGlvOiBTd2lwZXJPcHRpb25zWyd0b3VjaFJhdGlvJ107XG4gIEBJbnB1dCgpIHRvdWNoQW5nbGU6IFN3aXBlck9wdGlvbnNbJ3RvdWNoQW5nbGUnXTtcbiAgQElucHV0KCkgc2ltdWxhdGVUb3VjaDogU3dpcGVyT3B0aW9uc1snc2ltdWxhdGVUb3VjaCddO1xuICBASW5wdXQoKSBzaG9ydFN3aXBlczogU3dpcGVyT3B0aW9uc1snc2hvcnRTd2lwZXMnXTtcbiAgQElucHV0KCkgbG9uZ1N3aXBlczogU3dpcGVyT3B0aW9uc1snbG9uZ1N3aXBlcyddO1xuICBASW5wdXQoKSBsb25nU3dpcGVzUmF0aW86IFN3aXBlck9wdGlvbnNbJ2xvbmdTd2lwZXNSYXRpbyddO1xuICBASW5wdXQoKSBsb25nU3dpcGVzTXM6IFN3aXBlck9wdGlvbnNbJ2xvbmdTd2lwZXNNcyddO1xuICBASW5wdXQoKSBmb2xsb3dGaW5nZXI6IFN3aXBlck9wdGlvbnNbJ2ZvbGxvd0ZpbmdlciddO1xuICBASW5wdXQoKSBhbGxvd1RvdWNoTW92ZTogU3dpcGVyT3B0aW9uc1snYWxsb3dUb3VjaE1vdmUnXTtcbiAgQElucHV0KCkgdGhyZXNob2xkOiBTd2lwZXJPcHRpb25zWyd0aHJlc2hvbGQnXTtcbiAgQElucHV0KCkgdG91Y2hNb3ZlU3RvcFByb3BhZ2F0aW9uOiBTd2lwZXJPcHRpb25zWyd0b3VjaE1vdmVTdG9wUHJvcGFnYXRpb24nXTtcbiAgQElucHV0KCkgdG91Y2hTdGFydFByZXZlbnREZWZhdWx0OiBTd2lwZXJPcHRpb25zWyd0b3VjaFN0YXJ0UHJldmVudERlZmF1bHQnXTtcbiAgQElucHV0KCkgdG91Y2hTdGFydEZvcmNlUHJldmVudERlZmF1bHQ6IFN3aXBlck9wdGlvbnNbJ3RvdWNoU3RhcnRGb3JjZVByZXZlbnREZWZhdWx0J107XG4gIEBJbnB1dCgpIHRvdWNoUmVsZWFzZU9uRWRnZXM6IFN3aXBlck9wdGlvbnNbJ3RvdWNoUmVsZWFzZU9uRWRnZXMnXTtcbiAgQElucHV0KCkgdW5pcXVlTmF2RWxlbWVudHM6IFN3aXBlck9wdGlvbnNbJ3VuaXF1ZU5hdkVsZW1lbnRzJ107XG4gIEBJbnB1dCgpIHJlc2lzdGFuY2U6IFN3aXBlck9wdGlvbnNbJ3Jlc2lzdGFuY2UnXTtcbiAgQElucHV0KCkgcmVzaXN0YW5jZVJhdGlvOiBTd2lwZXJPcHRpb25zWydyZXNpc3RhbmNlUmF0aW8nXTtcbiAgQElucHV0KCkgd2F0Y2hTbGlkZXNQcm9ncmVzczogU3dpcGVyT3B0aW9uc1snd2F0Y2hTbGlkZXNQcm9ncmVzcyddO1xuICBASW5wdXQoKSBncmFiQ3Vyc29yOiBTd2lwZXJPcHRpb25zWydncmFiQ3Vyc29yJ107XG4gIEBJbnB1dCgpIHByZXZlbnRDbGlja3M6IFN3aXBlck9wdGlvbnNbJ3ByZXZlbnRDbGlja3MnXTtcbiAgQElucHV0KCkgcHJldmVudENsaWNrc1Byb3BhZ2F0aW9uOiBTd2lwZXJPcHRpb25zWydwcmV2ZW50Q2xpY2tzUHJvcGFnYXRpb24nXTtcbiAgQElucHV0KCkgc2xpZGVUb0NsaWNrZWRTbGlkZTogU3dpcGVyT3B0aW9uc1snc2xpZGVUb0NsaWNrZWRTbGlkZSddO1xuICBASW5wdXQoKSBwcmVsb2FkSW1hZ2VzOiBTd2lwZXJPcHRpb25zWydwcmVsb2FkSW1hZ2VzJ107XG4gIEBJbnB1dCgpIHVwZGF0ZU9uSW1hZ2VzUmVhZHk6IFN3aXBlck9wdGlvbnNbJ3VwZGF0ZU9uSW1hZ2VzUmVhZHknXTtcbiAgQElucHV0KCkgbG9vcDogU3dpcGVyT3B0aW9uc1snbG9vcCddO1xuICBASW5wdXQoKSBsb29wQWRkaXRpb25hbFNsaWRlczogU3dpcGVyT3B0aW9uc1snbG9vcEFkZGl0aW9uYWxTbGlkZXMnXTtcbiAgQElucHV0KCkgbG9vcGVkU2xpZGVzOiBTd2lwZXJPcHRpb25zWydsb29wZWRTbGlkZXMnXTtcbiAgQElucHV0KCkgbG9vcEZpbGxHcm91cFdpdGhCbGFuazogU3dpcGVyT3B0aW9uc1snbG9vcEZpbGxHcm91cFdpdGhCbGFuayddO1xuICBASW5wdXQoKSBsb29wUHJldmVudHNTbGlkZTogU3dpcGVyT3B0aW9uc1snbG9vcFByZXZlbnRzU2xpZGUnXTtcbiAgQElucHV0KCkgYWxsb3dTbGlkZVByZXY6IFN3aXBlck9wdGlvbnNbJ2FsbG93U2xpZGVQcmV2J107XG4gIEBJbnB1dCgpIGFsbG93U2xpZGVOZXh0OiBTd2lwZXJPcHRpb25zWydhbGxvd1NsaWRlTmV4dCddO1xuICBASW5wdXQoKSBzd2lwZUhhbmRsZXI6IFN3aXBlck9wdGlvbnNbJ3N3aXBlSGFuZGxlciddO1xuICBASW5wdXQoKSBub1N3aXBpbmc6IFN3aXBlck9wdGlvbnNbJ25vU3dpcGluZyddO1xuICBASW5wdXQoKSBub1N3aXBpbmdDbGFzczogU3dpcGVyT3B0aW9uc1snbm9Td2lwaW5nQ2xhc3MnXTtcbiAgQElucHV0KCkgbm9Td2lwaW5nU2VsZWN0b3I6IFN3aXBlck9wdGlvbnNbJ25vU3dpcGluZ1NlbGVjdG9yJ107XG4gIEBJbnB1dCgpIHBhc3NpdmVMaXN0ZW5lcnM6IFN3aXBlck9wdGlvbnNbJ3Bhc3NpdmVMaXN0ZW5lcnMnXTtcbiAgQElucHV0KCkgY29udGFpbmVyTW9kaWZpZXJDbGFzczogU3dpcGVyT3B0aW9uc1snY29udGFpbmVyTW9kaWZpZXJDbGFzcyddO1xuICBASW5wdXQoKSBzbGlkZUNsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZUNsYXNzJ10gPSAnc3dpcGVyLXNsaWRlJztcbiAgQElucHV0KCkgc2xpZGVCbGFua0NsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZUJsYW5rQ2xhc3MnXTtcbiAgQElucHV0KCkgc2xpZGVBY3RpdmVDbGFzczogU3dpcGVyT3B0aW9uc1snc2xpZGVBY3RpdmVDbGFzcyddO1xuICBASW5wdXQoKSBzbGlkZUR1cGxpY2F0ZUFjdGl2ZUNsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZUR1cGxpY2F0ZUFjdGl2ZUNsYXNzJ107XG4gIEBJbnB1dCgpIHNsaWRlVmlzaWJsZUNsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZVZpc2libGVDbGFzcyddO1xuICBASW5wdXQoKSBzbGlkZUR1cGxpY2F0ZUNsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZUR1cGxpY2F0ZUNsYXNzJ107XG4gIEBJbnB1dCgpIHNsaWRlTmV4dENsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZU5leHRDbGFzcyddO1xuICBASW5wdXQoKSBzbGlkZUR1cGxpY2F0ZU5leHRDbGFzczogU3dpcGVyT3B0aW9uc1snc2xpZGVEdXBsaWNhdGVOZXh0Q2xhc3MnXTtcbiAgQElucHV0KCkgc2xpZGVQcmV2Q2xhc3M6IFN3aXBlck9wdGlvbnNbJ3NsaWRlUHJldkNsYXNzJ107XG4gIEBJbnB1dCgpIHNsaWRlRHVwbGljYXRlUHJldkNsYXNzOiBTd2lwZXJPcHRpb25zWydzbGlkZUR1cGxpY2F0ZVByZXZDbGFzcyddO1xuICBASW5wdXQoKSB3cmFwcGVyQ2xhc3M6IFN3aXBlck9wdGlvbnNbJ3dyYXBwZXJDbGFzcyddID0gJ3N3aXBlci13cmFwcGVyJztcbiAgQElucHV0KCkgcnVuQ2FsbGJhY2tzT25Jbml0OiBTd2lwZXJPcHRpb25zWydydW5DYWxsYmFja3NPbkluaXQnXTtcbiAgQElucHV0KCkgb2JzZXJ2ZVBhcmVudHM6IFN3aXBlck9wdGlvbnNbJ29ic2VydmVQYXJlbnRzJ107XG4gIEBJbnB1dCgpIG9ic2VydmVTbGlkZUNoaWxkcmVuOiBTd2lwZXJPcHRpb25zWydvYnNlcnZlU2xpZGVDaGlsZHJlbiddO1xuICBASW5wdXQoKSBhMTF5OiBTd2lwZXJPcHRpb25zWydhMTF5J107XG4gIEBJbnB1dCgpIGF1dG9wbGF5OiBTd2lwZXJPcHRpb25zWydhdXRvcGxheSddO1xuICBASW5wdXQoKSBjb250cm9sbGVyOiBTd2lwZXJPcHRpb25zWydjb250cm9sbGVyJ107XG4gIEBJbnB1dCgpIGNvdmVyZmxvd0VmZmVjdDogU3dpcGVyT3B0aW9uc1snY292ZXJmbG93RWZmZWN0J107XG4gIEBJbnB1dCgpIGN1YmVFZmZlY3Q6IFN3aXBlck9wdGlvbnNbJ2N1YmVFZmZlY3QnXTtcbiAgQElucHV0KCkgZmFkZUVmZmVjdDogU3dpcGVyT3B0aW9uc1snZmFkZUVmZmVjdCddO1xuICBASW5wdXQoKSBmbGlwRWZmZWN0OiBTd2lwZXJPcHRpb25zWydmbGlwRWZmZWN0J107XG4gIEBJbnB1dCgpIGNyZWF0aXZlRWZmZWN0OiBTd2lwZXJPcHRpb25zWydjcmVhdGl2ZUVmZmVjdCddO1xuICBASW5wdXQoKSBjYXJkc0VmZmVjdDogU3dpcGVyT3B0aW9uc1snY2FyZHNFZmZlY3QnXTtcbiAgQElucHV0KCkgaGFzaE5hdmlnYXRpb246IFN3aXBlck9wdGlvbnNbJ2hhc2hOYXZpZ2F0aW9uJ107XG4gIEBJbnB1dCgpIGhpc3Rvcnk6IFN3aXBlck9wdGlvbnNbJ2hpc3RvcnknXTtcbiAgQElucHV0KCkga2V5Ym9hcmQ6IFN3aXBlck9wdGlvbnNbJ2tleWJvYXJkJ107XG4gIEBJbnB1dCgpIGxhenk6IFN3aXBlck9wdGlvbnNbJ2xhenknXTtcbiAgQElucHV0KCkgbW91c2V3aGVlbDogU3dpcGVyT3B0aW9uc1snbW91c2V3aGVlbCddO1xuICBASW5wdXQoKSBwYXJhbGxheDogU3dpcGVyT3B0aW9uc1sncGFyYWxsYXgnXTtcbiAgQElucHV0KCkgdGh1bWJzOiBTd2lwZXJPcHRpb25zWyd0aHVtYnMnXTtcbiAgQElucHV0KCkgem9vbTogU3dpcGVyT3B0aW9uc1snem9vbSddO1xuICBASW5wdXQoKSBjbGFzczogc3RyaW5nO1xuICBASW5wdXQoKSBpZDogc3RyaW5nO1xuICBASW5wdXQoKVxuICBzZXQgbmF2aWdhdGlvbih2YWwpIHtcbiAgICBjb25zdCBjdXJyZW50TmV4dCA9XG4gICAgICB0eXBlb2YgdGhpcy5fbmF2aWdhdGlvbiAhPT0gJ2Jvb2xlYW4nICYmIHRoaXMuX25hdmlnYXRpb24gIT09ICcnXG4gICAgICAgID8gdGhpcy5fbmF2aWdhdGlvbj8ubmV4dEVsXG4gICAgICAgIDogbnVsbDtcbiAgICBjb25zdCBjdXJyZW50UHJldiA9XG4gICAgICB0eXBlb2YgdGhpcy5fbmF2aWdhdGlvbiAhPT0gJ2Jvb2xlYW4nICYmIHRoaXMuX25hdmlnYXRpb24gIT09ICcnXG4gICAgICAgID8gdGhpcy5fbmF2aWdhdGlvbj8ucHJldkVsXG4gICAgICAgIDogbnVsbDtcbiAgICB0aGlzLl9uYXZpZ2F0aW9uID0gc2V0UHJvcGVydHkodmFsLCB7XG4gICAgICBuZXh0RWw6IGN1cnJlbnROZXh0IHx8IG51bGwsXG4gICAgICBwcmV2RWw6IGN1cnJlbnRQcmV2IHx8IG51bGwsXG4gICAgfSk7XG4gICAgdGhpcy5zaG93TmF2aWdhdGlvbiA9ICEoXG4gICAgICBjb2VyY2VCb29sZWFuUHJvcGVydHkodmFsKSAhPT0gdHJ1ZSB8fFxuICAgICAgKHRoaXMuX25hdmlnYXRpb24gJiZcbiAgICAgICAgdHlwZW9mIHRoaXMuX25hdmlnYXRpb24gIT09ICdib29sZWFuJyAmJlxuICAgICAgICB0aGlzLl9uYXZpZ2F0aW9uLnByZXZFbCAhPT0gdGhpcy5fcHJldkVsUmVmPy5uYXRpdmVFbGVtZW50ICYmXG4gICAgICAgICh0aGlzLl9uYXZpZ2F0aW9uLnByZXZFbCAhPT0gbnVsbCB8fCB0aGlzLl9uYXZpZ2F0aW9uLm5leHRFbCAhPT0gbnVsbCkgJiZcbiAgICAgICAgKHR5cGVvZiB0aGlzLl9uYXZpZ2F0aW9uLm5leHRFbCA9PT0gJ3N0cmluZycgfHxcbiAgICAgICAgICB0eXBlb2YgdGhpcy5fbmF2aWdhdGlvbi5wcmV2RWwgPT09ICdzdHJpbmcnIHx8XG4gICAgICAgICAgdHlwZW9mIHRoaXMuX25hdmlnYXRpb24ubmV4dEVsID09PSAnb2JqZWN0JyB8fFxuICAgICAgICAgIHR5cGVvZiB0aGlzLl9uYXZpZ2F0aW9uLnByZXZFbCA9PT0gJ29iamVjdCcpKVxuICAgICk7XG4gIH1cbiAgZ2V0IG5hdmlnYXRpb24oKSB7XG4gICAgcmV0dXJuIHRoaXMuX25hdmlnYXRpb247XG4gIH1cbiAgcHJpdmF0ZSBfbmF2aWdhdGlvbjogTmF2aWdhdGlvbk9wdGlvbnMgfCBib29sZWFuIHwgJyc7XG4gIHNob3dOYXZpZ2F0aW9uOiBib29sZWFuID0gdHJ1ZTtcblxuICBASW5wdXQoKVxuICBzZXQgcGFnaW5hdGlvbih2YWwpIHtcbiAgICBjb25zdCBjdXJyZW50ID1cbiAgICAgIHR5cGVvZiB0aGlzLl9wYWdpbmF0aW9uICE9PSAnYm9vbGVhbicgJiYgdGhpcy5fcGFnaW5hdGlvbiAhPT0gJydcbiAgICAgICAgPyB0aGlzLl9wYWdpbmF0aW9uPy5lbFxuICAgICAgICA6IG51bGw7XG4gICAgdGhpcy5fcGFnaW5hdGlvbiA9IHNldFByb3BlcnR5KHZhbCwge1xuICAgICAgZWw6IGN1cnJlbnQgfHwgbnVsbCxcbiAgICB9KTtcbiAgICB0aGlzLnNob3dQYWdpbmF0aW9uID0gaXNTaG93RWwodmFsLCB0aGlzLl9wYWdpbmF0aW9uLCB0aGlzLl9wYWdpbmF0aW9uRWxSZWYpO1xuICB9XG4gIGdldCBwYWdpbmF0aW9uKCkge1xuICAgIHJldHVybiB0aGlzLl9wYWdpbmF0aW9uO1xuICB9XG4gIHByaXZhdGUgX3BhZ2luYXRpb246IFBhZ2luYXRpb25PcHRpb25zIHwgYm9vbGVhbiB8ICcnO1xuICBzaG93UGFnaW5hdGlvbjogYm9vbGVhbiA9IHRydWU7XG5cbiAgQElucHV0KClcbiAgc2V0IHNjcm9sbGJhcih2YWwpIHtcbiAgICBjb25zdCBjdXJyZW50ID1cbiAgICAgIHR5cGVvZiB0aGlzLl9zY3JvbGxiYXIgIT09ICdib29sZWFuJyAmJiB0aGlzLl9zY3JvbGxiYXIgIT09ICcnID8gdGhpcy5fc2Nyb2xsYmFyPy5lbCA6IG51bGw7XG4gICAgdGhpcy5fc2Nyb2xsYmFyID0gc2V0UHJvcGVydHkodmFsLCB7XG4gICAgICBlbDogY3VycmVudCB8fCBudWxsLFxuICAgIH0pO1xuICAgIHRoaXMuc2hvd1Njcm9sbGJhciA9IGlzU2hvd0VsKHZhbCwgdGhpcy5fc2Nyb2xsYmFyLCB0aGlzLl9zY3JvbGxiYXJFbFJlZik7XG4gIH1cbiAgZ2V0IHNjcm9sbGJhcigpIHtcbiAgICByZXR1cm4gdGhpcy5fc2Nyb2xsYmFyO1xuICB9XG4gIHByaXZhdGUgX3Njcm9sbGJhcjogU2Nyb2xsYmFyT3B0aW9ucyB8IGJvb2xlYW4gfCAnJztcbiAgc2hvd1Njcm9sbGJhcjogYm9vbGVhbiA9IHRydWU7XG5cbiAgQElucHV0KClcbiAgc2V0IHZpcnR1YWwodmFsKSB7XG4gICAgdGhpcy5fdmlydHVhbCA9IHNldFByb3BlcnR5KHZhbCk7XG4gIH1cbiAgZ2V0IHZpcnR1YWwoKSB7XG4gICAgcmV0dXJuIHRoaXMuX3ZpcnR1YWw7XG4gIH1cbiAgcHJpdmF0ZSBfdmlydHVhbDogVmlydHVhbE9wdGlvbnMgfCBib29sZWFuIHwgJyc7XG5cbiAgQElucHV0KClcbiAgc2V0IGluZGV4KGluZGV4OiBudW1iZXIpIHtcbiAgICBjb25zb2xlLndhcm4oJ2BbKGluZGV4KV1gIHByb3AgaXMgZGVwcmVjYXRlZCBhbmQgd2lsbCBiZSByZW1vdmVkIGluIHVwY29taW5nIHZlcnNpb25zJyk7XG4gICAgdGhpcy5zZXRJbmRleChpbmRleCk7XG4gIH1cbiAgQElucHV0KClcbiAgc2V0IGNvbmZpZyh2YWw6IFN3aXBlck9wdGlvbnMpIHtcbiAgICB0aGlzLnVwZGF0ZVN3aXBlcih2YWwpO1xuICAgIGNvbnN0IHsgcGFyYW1zIH0gPSBnZXRQYXJhbXModmFsKTtcbiAgICBPYmplY3QuYXNzaWduKHRoaXMsIHBhcmFtcyk7XG4gIH1cbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ19iZWZvcmVCcmVha3BvaW50Jykgc19fYmVmb3JlQnJlYWtwb2ludDogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snX2JlZm9yZUJyZWFrcG9pbnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ19jb250YWluZXJDbGFzc2VzJykgc19fY29udGFpbmVyQ2xhc3NlczogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snX2NvbnRhaW5lckNsYXNzZXMnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ19zbGlkZUNsYXNzJykgc19fc2xpZGVDbGFzczogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snX3NsaWRlQ2xhc3MnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ19zd2lwZXInKSBzX19zd2lwZXI6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ19zd2lwZXInXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2FjdGl2ZUluZGV4Q2hhbmdlJykgc19hY3RpdmVJbmRleENoYW5nZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snYWN0aXZlSW5kZXhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2FmdGVySW5pdCcpIHNfYWZ0ZXJJbml0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydhZnRlckluaXQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2F1dG9wbGF5Jykgc19hdXRvcGxheTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snYXV0b3BsYXknXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2F1dG9wbGF5U3RhcnQnKSBzX2F1dG9wbGF5U3RhcnQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2F1dG9wbGF5U3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2F1dG9wbGF5U3RvcCcpIHNfYXV0b3BsYXlTdG9wOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydhdXRvcGxheVN0b3AnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZURlc3Ryb3knKSBzX2JlZm9yZURlc3Ryb3k6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2JlZm9yZURlc3Ryb3knXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZUluaXQnKSBzX2JlZm9yZUluaXQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2JlZm9yZUluaXQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZUxvb3BGaXgnKSBzX2JlZm9yZUxvb3BGaXg6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2JlZm9yZUxvb3BGaXgnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZVJlc2l6ZScpIHNfYmVmb3JlUmVzaXplOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydiZWZvcmVSZXNpemUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZVNsaWRlQ2hhbmdlU3RhcnQnKSBzX2JlZm9yZVNsaWRlQ2hhbmdlU3RhcnQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2JlZm9yZVNsaWRlQ2hhbmdlU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JlZm9yZVRyYW5zaXRpb25TdGFydCcpIHNfYmVmb3JlVHJhbnNpdGlvblN0YXJ0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydiZWZvcmVUcmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2JyZWFrcG9pbnQnKSBzX2JyZWFrcG9pbnQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2JyZWFrcG9pbnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2NoYW5nZURpcmVjdGlvbicpIHNfY2hhbmdlRGlyZWN0aW9uOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydjaGFuZ2VEaXJlY3Rpb24nXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2NsaWNrJykgc19jbGljazogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snY2xpY2snXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2RvdWJsZVRhcCcpIHNfZG91YmxlVGFwOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydkb3VibGVUYXAnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2RvdWJsZUNsaWNrJykgc19kb3VibGVDbGljazogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snZG91YmxlQ2xpY2snXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2Rlc3Ryb3knKSBzX2Rlc3Ryb3k6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2Rlc3Ryb3knXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2Zyb21FZGdlJykgc19mcm9tRWRnZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snZnJvbUVkZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2hhc2hDaGFuZ2UnKSBzX2hhc2hDaGFuZ2U6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2hhc2hDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2hhc2hTZXQnKSBzX2hhc2hTZXQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2hhc2hTZXQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2ltYWdlc1JlYWR5Jykgc19pbWFnZXNSZWFkeTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snaW1hZ2VzUmVhZHknXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2luaXQnKSBzX2luaXQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2luaXQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2tleVByZXNzJykgc19rZXlQcmVzczogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sna2V5UHJlc3MnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2xhenlJbWFnZUxvYWQnKSBzX2xhenlJbWFnZUxvYWQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2xhenlJbWFnZUxvYWQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2xhenlJbWFnZVJlYWR5Jykgc19sYXp5SW1hZ2VSZWFkeTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snbGF6eUltYWdlUmVhZHknXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ2xvb3BGaXgnKSBzX2xvb3BGaXg6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ2xvb3BGaXgnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ21vbWVudHVtQm91bmNlJykgc19tb21lbnR1bUJvdW5jZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snbW9tZW50dW1Cb3VuY2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ25hdmlnYXRpb25IaWRlJykgc19uYXZpZ2F0aW9uSGlkZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snbmF2aWdhdGlvbkhpZGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ25hdmlnYXRpb25TaG93Jykgc19uYXZpZ2F0aW9uU2hvdzogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snbmF2aWdhdGlvblNob3cnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ29ic2VydmVyVXBkYXRlJykgc19vYnNlcnZlclVwZGF0ZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snb2JzZXJ2ZXJVcGRhdGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ29yaWVudGF0aW9uY2hhbmdlJykgc19vcmllbnRhdGlvbmNoYW5nZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snb3JpZW50YXRpb25jaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3BhZ2luYXRpb25IaWRlJykgc19wYWdpbmF0aW9uSGlkZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sncGFnaW5hdGlvbkhpZGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3BhZ2luYXRpb25SZW5kZXInKSBzX3BhZ2luYXRpb25SZW5kZXI6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3BhZ2luYXRpb25SZW5kZXInXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3BhZ2luYXRpb25TaG93Jykgc19wYWdpbmF0aW9uU2hvdzogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sncGFnaW5hdGlvblNob3cnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3BhZ2luYXRpb25VcGRhdGUnKSBzX3BhZ2luYXRpb25VcGRhdGU6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3BhZ2luYXRpb25VcGRhdGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Byb2dyZXNzJykgc19wcm9ncmVzczogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sncHJvZ3Jlc3MnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3JlYWNoQmVnaW5uaW5nJykgc19yZWFjaEJlZ2lubmluZzogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sncmVhY2hCZWdpbm5pbmcnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3JlYWNoRW5kJykgc19yZWFjaEVuZDogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sncmVhY2hFbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3JlYWxJbmRleENoYW5nZScpIHNfcmVhbEluZGV4Q2hhbmdlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydyZWFsSW5kZXhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Jlc2l6ZScpIHNfcmVzaXplOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydyZXNpemUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Njcm9sbCcpIHNfc2Nyb2xsOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzY3JvbGwnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Njcm9sbGJhckRyYWdFbmQnKSBzX3Njcm9sbGJhckRyYWdFbmQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3Njcm9sbGJhckRyYWdFbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Njcm9sbGJhckRyYWdNb3ZlJykgc19zY3JvbGxiYXJEcmFnTW92ZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snc2Nyb2xsYmFyRHJhZ01vdmUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3Njcm9sbGJhckRyYWdTdGFydCcpIHNfc2Nyb2xsYmFyRHJhZ1N0YXJ0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzY3JvbGxiYXJEcmFnU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NldFRyYW5zaXRpb24nKSBzX3NldFRyYW5zaXRpb246IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NldFRyYW5zaXRpb24nXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NldFRyYW5zbGF0ZScpIHNfc2V0VHJhbnNsYXRlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzZXRUcmFuc2xhdGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlQ2hhbmdlJykgc19zbGlkZUNoYW5nZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snc2xpZGVDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlQ2hhbmdlVHJhbnNpdGlvbkVuZCcpIHNfc2xpZGVDaGFuZ2VUcmFuc2l0aW9uRW5kOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbGlkZUNoYW5nZVRyYW5zaXRpb25FbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlQ2hhbmdlVHJhbnNpdGlvblN0YXJ0Jykgc19zbGlkZUNoYW5nZVRyYW5zaXRpb25TdGFydDogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snc2xpZGVDaGFuZ2VUcmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlTmV4dFRyYW5zaXRpb25FbmQnKSBzX3NsaWRlTmV4dFRyYW5zaXRpb25FbmQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NsaWRlTmV4dFRyYW5zaXRpb25FbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlTmV4dFRyYW5zaXRpb25TdGFydCcpIHNfc2xpZGVOZXh0VHJhbnNpdGlvblN0YXJ0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbGlkZU5leHRUcmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlUHJldlRyYW5zaXRpb25FbmQnKSBzX3NsaWRlUHJldlRyYW5zaXRpb25FbmQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NsaWRlUHJldlRyYW5zaXRpb25FbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlUHJldlRyYW5zaXRpb25TdGFydCcpIHNfc2xpZGVQcmV2VHJhbnNpdGlvblN0YXJ0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbGlkZVByZXZUcmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlUmVzZXRUcmFuc2l0aW9uU3RhcnQnKSBzX3NsaWRlUmVzZXRUcmFuc2l0aW9uU3RhcnQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NsaWRlUmVzZXRUcmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlUmVzZXRUcmFuc2l0aW9uRW5kJykgc19zbGlkZVJlc2V0VHJhbnNpdGlvbkVuZDogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snc2xpZGVSZXNldFRyYW5zaXRpb25FbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlck1vdmUnKSBzX3NsaWRlck1vdmU6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NsaWRlck1vdmUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlckZpcnN0TW92ZScpIHNfc2xpZGVyRmlyc3RNb3ZlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbGlkZXJGaXJzdE1vdmUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlc0xlbmd0aENoYW5nZScpIHNfc2xpZGVzTGVuZ3RoQ2hhbmdlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbGlkZXNMZW5ndGhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NsaWRlc0dyaWRMZW5ndGhDaGFuZ2UnKSBzX3NsaWRlc0dyaWRMZW5ndGhDaGFuZ2U6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3NsaWRlc0dyaWRMZW5ndGhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NuYXBHcmlkTGVuZ3RoQ2hhbmdlJykgc19zbmFwR3JpZExlbmd0aENoYW5nZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1snc25hcEdyaWRMZW5ndGhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3NuYXBJbmRleENoYW5nZScpIHNfc25hcEluZGV4Q2hhbmdlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWydzbmFwSW5kZXhDaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RhcCcpIHNfdGFwOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWyd0YXAnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RvRWRnZScpIHNfdG9FZGdlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWyd0b0VkZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RvdWNoRW5kJykgc190b3VjaEVuZDogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sndG91Y2hFbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RvdWNoTW92ZScpIHNfdG91Y2hNb3ZlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWyd0b3VjaE1vdmUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RvdWNoTW92ZU9wcG9zaXRlJykgc190b3VjaE1vdmVPcHBvc2l0ZTogRXZlbnRFbWl0dGVyPFN3aXBlckV2ZW50c1sndG91Y2hNb3ZlT3Bwb3NpdGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RvdWNoU3RhcnQnKSBzX3RvdWNoU3RhcnQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3RvdWNoU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RyYW5zaXRpb25FbmQnKSBzX3RyYW5zaXRpb25FbmQ6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3RyYW5zaXRpb25FbmQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3RyYW5zaXRpb25TdGFydCcpIHNfdHJhbnNpdGlvblN0YXJ0OiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWyd0cmFuc2l0aW9uU3RhcnQnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3VwZGF0ZScpIHNfdXBkYXRlOiBFdmVudEVtaXR0ZXI8U3dpcGVyRXZlbnRzWyd1cGRhdGUnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3pvb21DaGFuZ2UnKSBzX3pvb21DaGFuZ2U6IEV2ZW50RW1pdHRlcjxTd2lwZXJFdmVudHNbJ3pvb21DaGFuZ2UnXT4gPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgLy8gcHJldHRpZXItaWdub3JlXG4gIEBPdXRwdXQoJ3N3aXBlcicpIHNfc3dpcGVyOiBFdmVudEVtaXR0ZXI8YW55PiA9IG5ldyBFdmVudEVtaXR0ZXI8YW55PigpO1xuXG4gIEBPdXRwdXQoKSBpbmRleENoYW5nZSA9IG5ldyBFdmVudEVtaXR0ZXI8bnVtYmVyPigpO1xuXG4gIEBWaWV3Q2hpbGQoJ3ByZXZFbFJlZicsIHsgc3RhdGljOiBmYWxzZSB9KVxuICBzZXQgcHJldkVsUmVmKGVsOiBFbGVtZW50UmVmKSB7XG4gICAgdGhpcy5fcHJldkVsUmVmID0gZWw7XG4gICAgdGhpcy5fc2V0RWxlbWVudChlbCwgdGhpcy5uYXZpZ2F0aW9uLCAnbmF2aWdhdGlvbicsICdwcmV2RWwnKTtcbiAgfVxuICBfcHJldkVsUmVmOiBFbGVtZW50UmVmO1xuICBAVmlld0NoaWxkKCduZXh0RWxSZWYnLCB7IHN0YXRpYzogZmFsc2UgfSlcbiAgc2V0IG5leHRFbFJlZihlbDogRWxlbWVudFJlZikge1xuICAgIHRoaXMuX25leHRFbFJlZiA9IGVsO1xuICAgIHRoaXMuX3NldEVsZW1lbnQoZWwsIHRoaXMubmF2aWdhdGlvbiwgJ25hdmlnYXRpb24nLCAnbmV4dEVsJyk7XG4gIH1cbiAgX25leHRFbFJlZjogRWxlbWVudFJlZjtcbiAgQFZpZXdDaGlsZCgnc2Nyb2xsYmFyRWxSZWYnLCB7IHN0YXRpYzogZmFsc2UgfSlcbiAgc2V0IHNjcm9sbGJhckVsUmVmKGVsOiBFbGVtZW50UmVmKSB7XG4gICAgdGhpcy5fc2Nyb2xsYmFyRWxSZWYgPSBlbDtcbiAgICB0aGlzLl9zZXRFbGVtZW50KGVsLCB0aGlzLnNjcm9sbGJhciwgJ3Njcm9sbGJhcicpO1xuICB9XG4gIF9zY3JvbGxiYXJFbFJlZjogRWxlbWVudFJlZjtcbiAgQFZpZXdDaGlsZCgncGFnaW5hdGlvbkVsUmVmJywgeyBzdGF0aWM6IGZhbHNlIH0pXG4gIHNldCBwYWdpbmF0aW9uRWxSZWYoZWw6IEVsZW1lbnRSZWYpIHtcbiAgICB0aGlzLl9wYWdpbmF0aW9uRWxSZWYgPSBlbDtcbiAgICB0aGlzLl9zZXRFbGVtZW50KGVsLCB0aGlzLnBhZ2luYXRpb24sICdwYWdpbmF0aW9uJyk7XG4gIH1cbiAgX3BhZ2luYXRpb25FbFJlZjogRWxlbWVudFJlZjtcbiAgQENvbnRlbnRDaGlsZHJlbihTd2lwZXJTbGlkZURpcmVjdGl2ZSwgeyBkZXNjZW5kYW50czogZmFsc2UsIGVtaXREaXN0aW5jdENoYW5nZXNPbmx5OiB0cnVlIH0pXG4gIHNsaWRlc0VsOiBRdWVyeUxpc3Q8U3dpcGVyU2xpZGVEaXJlY3RpdmU+O1xuICBwcml2YXRlIHNsaWRlczogU3dpcGVyU2xpZGVEaXJlY3RpdmVbXTtcblxuICBwcmVwZW5kU2xpZGVzOiBPYnNlcnZhYmxlPFN3aXBlclNsaWRlRGlyZWN0aXZlW10+O1xuICBhcHBlbmRTbGlkZXM6IE9ic2VydmFibGU8U3dpcGVyU2xpZGVEaXJlY3RpdmVbXT47XG5cbiAgc3dpcGVyUmVmOiBTd2lwZXI7XG4gIHJlYWRvbmx5IF9hY3RpdmVTbGlkZXMgPSBuZXcgU3ViamVjdDxTd2lwZXJTbGlkZURpcmVjdGl2ZVtdPigpO1xuXG4gIGdldCBhY3RpdmVTbGlkZXMoKSB7XG4gICAgaWYgKHRoaXMudmlydHVhbCkge1xuICAgICAgcmV0dXJuIHRoaXMuX2FjdGl2ZVNsaWRlcztcbiAgICB9XG4gICAgcmV0dXJuIG9mKHRoaXMuc2xpZGVzKTtcbiAgfVxuXG4gIGdldCB6b29tQ29udGFpbmVyQ2xhc3MoKSB7XG4gICAgcmV0dXJuIHRoaXMuem9vbSAmJiB0eXBlb2YgdGhpcy56b29tICE9PSAnYm9vbGVhbidcbiAgICAgID8gdGhpcy56b29tLmNvbnRhaW5lckNsYXNzXG4gICAgICA6ICdzd2lwZXItem9vbS1jb250YWluZXInO1xuICB9XG5cbiAgQEhvc3RCaW5kaW5nKCdjbGFzcycpIGNvbnRhaW5lckNsYXNzZXM6IHN0cmluZyA9ICdzd2lwZXInO1xuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIF9uZ1pvbmU6IE5nWm9uZSxcbiAgICBwcml2YXRlIGVsZW1lbnRSZWY6IEVsZW1lbnRSZWYsXG4gICAgcHJpdmF0ZSBfY2hhbmdlRGV0ZWN0b3JSZWY6IENoYW5nZURldGVjdG9yUmVmLFxuICAgIEBJbmplY3QoUExBVEZPUk1fSUQpIHByaXZhdGUgX3BsYXRmb3JtSWQ6IE9iamVjdCxcbiAgKSB7fVxuXG4gIHByaXZhdGUgX3NldEVsZW1lbnQoZWw6IEVsZW1lbnRSZWYsIHJlZjogYW55LCB1cGRhdGU6IHN0cmluZywga2V5ID0gJ2VsJykge1xuICAgIGlmICghZWwgfHwgIXJlZikge1xuICAgICAgcmV0dXJuO1xuICAgIH1cbiAgICBpZiAocmVmICYmIGVsLm5hdGl2ZUVsZW1lbnQpIHtcbiAgICAgIGlmIChyZWZba2V5XSA9PT0gZWwubmF0aXZlRWxlbWVudCkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICByZWZba2V5XSA9IGVsLm5hdGl2ZUVsZW1lbnQ7XG4gICAgfVxuICAgIGNvbnN0IHVwZGF0ZU9iajogeyBba2V5OiBzdHJpbmddOiBib29sZWFuIH0gPSB7fTtcbiAgICB1cGRhdGVPYmpbdXBkYXRlXSA9IHRydWU7XG4gICAgdGhpcy51cGRhdGVJbml0U3dpcGVyKHVwZGF0ZU9iaik7XG4gIH1cbiAgbmdPbkluaXQoKTogdm9pZCB7XG4gICAgY29uc3QgeyBwYXJhbXMgfSA9IGdldFBhcmFtcyh0aGlzKTtcbiAgICBPYmplY3QuYXNzaWduKHRoaXMsIHBhcmFtcyk7XG4gIH1cbiAgbmdBZnRlclZpZXdJbml0KCkge1xuICAgIHRoaXMuY2hpbGRyZW5TbGlkZXNJbml0KCk7XG4gICAgdGhpcy5pbml0U3dpcGVyKCk7XG4gICAgdGhpcy5fY2hhbmdlRGV0ZWN0b3JSZWYuZGV0ZWN0Q2hhbmdlcygpO1xuICAgIHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgdGhpcy5zX3N3aXBlci5lbWl0KHRoaXMuc3dpcGVyUmVmKTtcbiAgICB9KTtcbiAgfVxuXG4gIHByaXZhdGUgY2hpbGRyZW5TbGlkZXNJbml0KCkge1xuICAgIHRoaXMuc2xpZGVzQ2hhbmdlcyh0aGlzLnNsaWRlc0VsKTtcbiAgICB0aGlzLnNsaWRlc0VsLmNoYW5nZXMuc3Vic2NyaWJlKHRoaXMuc2xpZGVzQ2hhbmdlcyk7XG4gIH1cblxuICBwcml2YXRlIHNsaWRlc0NoYW5nZXMgPSAodmFsOiBRdWVyeUxpc3Q8U3dpcGVyU2xpZGVEaXJlY3RpdmU+KSA9PiB7XG4gICAgdGhpcy5zbGlkZXMgPSB2YWwubWFwKChzbGlkZTogU3dpcGVyU2xpZGVEaXJlY3RpdmUsIGluZGV4OiBudW1iZXIpID0+IHtcbiAgICAgIHNsaWRlLnNsaWRlSW5kZXggPSBpbmRleDtcbiAgICAgIHNsaWRlLmNsYXNzTmFtZXMgPSB0aGlzLnNsaWRlQ2xhc3MgfHwgJyc7XG4gICAgICByZXR1cm4gc2xpZGU7XG4gICAgfSk7XG4gICAgaWYgKHRoaXMubG9vcCAmJiAhdGhpcy5sb29wZWRTbGlkZXMpIHtcbiAgICAgIHRoaXMuY2FsY0xvb3BlZFNsaWRlcygpO1xuICAgIH1cbiAgICBpZiAoIXRoaXMudmlydHVhbCkge1xuICAgICAgaWYgKHRoaXMubG9vcGVkU2xpZGVzKSB7XG4gICAgICAgIHRoaXMucHJlcGVuZFNsaWRlcyA9IG9mKHRoaXMuc2xpZGVzLnNsaWNlKHRoaXMuc2xpZGVzLmxlbmd0aCAtIHRoaXMubG9vcGVkU2xpZGVzKSk7XG4gICAgICAgIHRoaXMuYXBwZW5kU2xpZGVzID0gb2YodGhpcy5zbGlkZXMuc2xpY2UoMCwgdGhpcy5sb29wZWRTbGlkZXMpKTtcbiAgICAgIH1cbiAgICB9IGVsc2UgaWYgKHRoaXMuc3dpcGVyUmVmICYmIHRoaXMuc3dpcGVyUmVmLnZpcnR1YWwpIHtcbiAgICAgIHRoaXMuX25nWm9uZS5ydW5PdXRzaWRlQW5ndWxhcigoKSA9PiB7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLnZpcnR1YWwuc2xpZGVzID0gdGhpcy5zbGlkZXM7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLnZpcnR1YWwudXBkYXRlKHRydWUpO1xuICAgICAgfSk7XG4gICAgfVxuICAgIHRoaXMuX2NoYW5nZURldGVjdG9yUmVmLmRldGVjdENoYW5nZXMoKTtcbiAgfTtcblxuICBnZXQgaXNTd2lwZXJBY3RpdmUoKSB7XG4gICAgcmV0dXJuIHRoaXMuc3dpcGVyUmVmICYmICF0aGlzLnN3aXBlclJlZi5kZXN0cm95ZWQ7XG4gIH1cblxuICBpbml0U3dpcGVyKCkge1xuICAgIGNvbnN0IHsgcGFyYW1zOiBzd2lwZXJQYXJhbXMsIHBhc3NlZFBhcmFtcyB9ID0gZ2V0UGFyYW1zKHRoaXMpO1xuICAgIE9iamVjdC5hc3NpZ24odGhpcywgc3dpcGVyUGFyYW1zKTtcbiAgICB0aGlzLl9uZ1pvbmUucnVuT3V0c2lkZUFuZ3VsYXIoKCkgPT4ge1xuICAgICAgc3dpcGVyUGFyYW1zLmluaXQgPSBmYWxzZTtcbiAgICAgIGlmICghc3dpcGVyUGFyYW1zLnZpcnR1YWwpIHtcbiAgICAgICAgc3dpcGVyUGFyYW1zLm9ic2VydmVyID0gdHJ1ZTtcbiAgICAgIH1cblxuICAgICAgc3dpcGVyUGFyYW1zLm9uQW55ID0gKGV2ZW50TmFtZToga2V5b2YgU3dpcGVyQ29tcG9uZW50LCAuLi5hcmdzOiBhbnlbXSkgPT4ge1xuICAgICAgICBjb25zdCBlbWl0dGVyID0gdGhpc1soJ3NfJyArIGV2ZW50TmFtZSkgYXMga2V5b2YgU3dpcGVyQ29tcG9uZW50XSBhcyBFdmVudEVtaXR0ZXI8YW55PjtcbiAgICAgICAgaWYgKGVtaXR0ZXIpIHtcbiAgICAgICAgICBlbWl0dGVyLmVtaXQoLi4uYXJncyk7XG4gICAgICAgIH1cbiAgICAgIH07XG4gICAgICBjb25zdCBfc2xpZGVDbGFzc2VzOiBTd2lwZXJFdmVudHNbJ19zbGlkZUNsYXNzZXMnXSA9IChfLCB1cGRhdGVkKSA9PiB7XG4gICAgICAgIHVwZGF0ZWQuZm9yRWFjaCgoeyBzbGlkZUVsLCBjbGFzc05hbWVzIH0sIGluZGV4KSA9PiB7XG4gICAgICAgICAgY29uc3QgZGF0YUluZGV4ID0gc2xpZGVFbC5nZXRBdHRyaWJ1dGUoJ2RhdGEtc3dpcGVyLXNsaWRlLWluZGV4Jyk7XG4gICAgICAgICAgY29uc3Qgc2xpZGVJbmRleCA9IGRhdGFJbmRleCA/IHBhcnNlSW50KGRhdGFJbmRleCkgOiBpbmRleDtcbiAgICAgICAgICBpZiAodGhpcy52aXJ0dWFsKSB7XG4gICAgICAgICAgICBjb25zdCB2aXJ0dWFsU2xpZGUgPSB0aGlzLnNsaWRlcy5maW5kKChpdGVtKSA9PiB7XG4gICAgICAgICAgICAgIHJldHVybiBpdGVtLnZpcnR1YWxJbmRleCAmJiBpdGVtLnZpcnR1YWxJbmRleCA9PT0gc2xpZGVJbmRleDtcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgaWYgKHZpcnR1YWxTbGlkZSkge1xuICAgICAgICAgICAgICB2aXJ0dWFsU2xpZGUuY2xhc3NOYW1lcyA9IGNsYXNzTmFtZXM7XG4gICAgICAgICAgICAgIHJldHVybjtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9XG5cbiAgICAgICAgICBpZiAodGhpcy5zbGlkZXNbc2xpZGVJbmRleF0pIHtcbiAgICAgICAgICAgIHRoaXMuc2xpZGVzW3NsaWRlSW5kZXhdLmNsYXNzTmFtZXMgPSBjbGFzc05hbWVzO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICAgIHRoaXMuX2NoYW5nZURldGVjdG9yUmVmLmRldGVjdENoYW5nZXMoKTtcbiAgICAgIH07XG4gICAgICBjb25zdCBfY29udGFpbmVyQ2xhc3NlczogU3dpcGVyRXZlbnRzWydfY29udGFpbmVyQ2xhc3NlcyddID0gKF8sIGNsYXNzZXMpID0+IHtcbiAgICAgICAgc2V0VGltZW91dCgoKSA9PiB7XG4gICAgICAgICAgdGhpcy5jb250YWluZXJDbGFzc2VzID0gY2xhc3NlcztcbiAgICAgICAgfSk7XG4gICAgICB9O1xuICAgICAgT2JqZWN0LmFzc2lnbihzd2lwZXJQYXJhbXMub24sIHtcbiAgICAgICAgX2NvbnRhaW5lckNsYXNzZXMsXG4gICAgICAgIF9zbGlkZUNsYXNzZXMsXG4gICAgICB9KTtcbiAgICAgIGNvbnN0IHN3aXBlclJlZiA9IG5ldyBTd2lwZXIoc3dpcGVyUGFyYW1zKTtcbiAgICAgIHN3aXBlclJlZi5sb29wQ3JlYXRlID0gKCkgPT4ge307XG4gICAgICBzd2lwZXJSZWYubG9vcERlc3Ryb3kgPSAoKSA9PiB7fTtcbiAgICAgIGlmIChzd2lwZXJQYXJhbXMubG9vcCkge1xuICAgICAgICBzd2lwZXJSZWYubG9vcGVkU2xpZGVzID0gdGhpcy5sb29wZWRTbGlkZXM7XG4gICAgICB9XG4gICAgICBjb25zdCBpc1ZpcnR1YWxFbmFibGVkID1cbiAgICAgICAgdHlwZW9mIHN3aXBlclJlZi5wYXJhbXMudmlydHVhbCAhPT0gJ3VuZGVmaW5lZCcgJiZcbiAgICAgICAgdHlwZW9mIHN3aXBlclJlZi5wYXJhbXMudmlydHVhbCAhPT0gJ2Jvb2xlYW4nICYmXG4gICAgICAgIHN3aXBlclJlZi5wYXJhbXMudmlydHVhbC5lbmFibGVkO1xuICAgICAgaWYgKHN3aXBlclJlZi52aXJ0dWFsICYmIGlzVmlydHVhbEVuYWJsZWQpIHtcbiAgICAgICAgc3dpcGVyUmVmLnZpcnR1YWwuc2xpZGVzID0gdGhpcy5zbGlkZXM7XG4gICAgICAgIGNvbnN0IGV4dGVuZFdpdGggPSB7XG4gICAgICAgICAgY2FjaGU6IGZhbHNlLFxuICAgICAgICAgIHNsaWRlczogdGhpcy5zbGlkZXMsXG4gICAgICAgICAgcmVuZGVyRXh0ZXJuYWw6IHRoaXMudXBkYXRlVmlydHVhbFNsaWRlcyxcbiAgICAgICAgICByZW5kZXJFeHRlcm5hbFVwZGF0ZTogZmFsc2UsXG4gICAgICAgIH07XG4gICAgICAgIGV4dGVuZChzd2lwZXJSZWYucGFyYW1zLnZpcnR1YWwsIGV4dGVuZFdpdGgpO1xuICAgICAgICBleHRlbmQoc3dpcGVyUmVmLm9yaWdpbmFsUGFyYW1zLnZpcnR1YWwsIGV4dGVuZFdpdGgpO1xuICAgICAgfVxuXG4gICAgICBpZiAoaXNQbGF0Zm9ybUJyb3dzZXIodGhpcy5fcGxhdGZvcm1JZCkpIHtcbiAgICAgICAgdGhpcy5zd2lwZXJSZWYgPSBzd2lwZXJSZWYuaW5pdCh0aGlzLmVsZW1lbnRSZWYubmF0aXZlRWxlbWVudCk7XG4gICAgICAgIGNvbnN0IGlzRW5hYmxlZCA9XG4gICAgICAgICAgdHlwZW9mIHRoaXMuc3dpcGVyUmVmLnBhcmFtcy52aXJ0dWFsICE9PSAndW5kZWZpbmVkJyAmJlxuICAgICAgICAgIHR5cGVvZiB0aGlzLnN3aXBlclJlZi5wYXJhbXMudmlydHVhbCAhPT0gJ2Jvb2xlYW4nICYmXG4gICAgICAgICAgdGhpcy5zd2lwZXJSZWYucGFyYW1zLnZpcnR1YWwuZW5hYmxlZDtcbiAgICAgICAgaWYgKHRoaXMuc3dpcGVyUmVmLnZpcnR1YWwgJiYgaXNFbmFibGVkKSB7XG4gICAgICAgICAgdGhpcy5zd2lwZXJSZWYudmlydHVhbC51cGRhdGUodHJ1ZSk7XG4gICAgICAgIH1cbiAgICAgICAgdGhpcy5fY2hhbmdlRGV0ZWN0b3JSZWYuZGV0ZWN0Q2hhbmdlcygpO1xuICAgICAgICBzd2lwZXJSZWYub24oJ3NsaWRlQ2hhbmdlJywgKCkgPT4ge1xuICAgICAgICAgIHRoaXMuaW5kZXhDaGFuZ2UuZW1pdCh0aGlzLnN3aXBlclJlZi5yZWFsSW5kZXgpO1xuICAgICAgICB9KTtcbiAgICAgIH1cbiAgICB9KTtcbiAgfVxuXG4gIHN0eWxlOiBhbnkgPSBudWxsO1xuICBjdXJyZW50VmlydHVhbERhdGE6IGFueTsgLy8gVE9ETzogdHlwZSB2aXJ0dWFsRGF0YTtcbiAgcHJpdmF0ZSB1cGRhdGVWaXJ0dWFsU2xpZGVzID0gKHZpcnR1YWxEYXRhOiBhbnkpID0+IHtcbiAgICAvLyBUT0RPOiB0eXBlIHZpcnR1YWxEYXRhXG4gICAgaWYgKFxuICAgICAgIXRoaXMuc3dpcGVyUmVmIHx8XG4gICAgICAodGhpcy5jdXJyZW50VmlydHVhbERhdGEgJiZcbiAgICAgICAgdGhpcy5jdXJyZW50VmlydHVhbERhdGEuZnJvbSA9PT0gdmlydHVhbERhdGEuZnJvbSAmJlxuICAgICAgICB0aGlzLmN1cnJlbnRWaXJ0dWFsRGF0YS50byA9PT0gdmlydHVhbERhdGEudG8gJiZcbiAgICAgICAgdGhpcy5jdXJyZW50VmlydHVhbERhdGEub2Zmc2V0ID09PSB2aXJ0dWFsRGF0YS5vZmZzZXQpXG4gICAgKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIHRoaXMuc3R5bGUgPSB0aGlzLnN3aXBlclJlZi5pc0hvcml6b250YWwoKVxuICAgICAgPyB7XG4gICAgICAgICAgW3RoaXMuc3dpcGVyUmVmLnJ0bFRyYW5zbGF0ZSA/ICdyaWdodCcgOiAnbGVmdCddOiBgJHt2aXJ0dWFsRGF0YS5vZmZzZXR9cHhgLFxuICAgICAgICB9XG4gICAgICA6IHtcbiAgICAgICAgICB0b3A6IGAke3ZpcnR1YWxEYXRhLm9mZnNldH1weGAsXG4gICAgICAgIH07XG4gICAgdGhpcy5jdXJyZW50VmlydHVhbERhdGEgPSB2aXJ0dWFsRGF0YTtcbiAgICB0aGlzLl9hY3RpdmVTbGlkZXMubmV4dCh2aXJ0dWFsRGF0YS5zbGlkZXMpO1xuICAgIHRoaXMuX25nWm9uZS5ydW4oKCkgPT4ge1xuICAgICAgdGhpcy5fY2hhbmdlRGV0ZWN0b3JSZWYuZGV0ZWN0Q2hhbmdlcygpO1xuICAgIH0pO1xuICAgIHRoaXMuX25nWm9uZS5ydW5PdXRzaWRlQW5ndWxhcigoKSA9PiB7XG4gICAgICB0aGlzLnN3aXBlclJlZi51cGRhdGVTbGlkZXMoKTtcbiAgICAgIHRoaXMuc3dpcGVyUmVmLnVwZGF0ZVByb2dyZXNzKCk7XG4gICAgICB0aGlzLnN3aXBlclJlZi51cGRhdGVTbGlkZXNDbGFzc2VzKCk7XG4gICAgICBpZiAodGhpcy5zd2lwZXJSZWYubGF6eSAmJiB0aGlzLnN3aXBlclJlZi5wYXJhbXMubGF6eVsnZW5hYmxlZCddKSB7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLmxhenkubG9hZCgpO1xuICAgICAgfVxuICAgICAgdGhpcy5zd2lwZXJSZWYudmlydHVhbC51cGRhdGUodHJ1ZSk7XG4gICAgfSk7XG4gICAgcmV0dXJuO1xuICB9O1xuXG4gIG5nT25DaGFuZ2VzKGNoYW5nZWRQYXJhbXM6IFNpbXBsZUNoYW5nZXMpIHtcbiAgICB0aGlzLnVwZGF0ZVN3aXBlcihjaGFuZ2VkUGFyYW1zKTtcbiAgICB0aGlzLl9jaGFuZ2VEZXRlY3RvclJlZi5kZXRlY3RDaGFuZ2VzKCk7XG4gIH1cblxuICB1cGRhdGVJbml0U3dpcGVyKGNoYW5nZWRQYXJhbXM6IGFueSkge1xuICAgIGlmICghKGNoYW5nZWRQYXJhbXMgJiYgdGhpcy5zd2lwZXJSZWYgJiYgIXRoaXMuc3dpcGVyUmVmLmRlc3Ryb3llZCkpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICB0aGlzLl9uZ1pvbmUucnVuT3V0c2lkZUFuZ3VsYXIoKCkgPT4ge1xuICAgICAgY29uc3Qge1xuICAgICAgICBwYXJhbXM6IGN1cnJlbnRQYXJhbXMsXG4gICAgICAgIHBhZ2luYXRpb24sXG4gICAgICAgIG5hdmlnYXRpb24sXG4gICAgICAgIHNjcm9sbGJhcixcbiAgICAgICAgdmlydHVhbCxcbiAgICAgICAgdGh1bWJzLFxuICAgICAgfSA9IHRoaXMuc3dpcGVyUmVmO1xuXG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy5wYWdpbmF0aW9uKSB7XG4gICAgICAgIGlmIChcbiAgICAgICAgICB0aGlzLnBhZ2luYXRpb24gJiZcbiAgICAgICAgICB0eXBlb2YgdGhpcy5wYWdpbmF0aW9uICE9PSAnYm9vbGVhbicgJiZcbiAgICAgICAgICB0aGlzLnBhZ2luYXRpb24uZWwgJiZcbiAgICAgICAgICBwYWdpbmF0aW9uICYmXG4gICAgICAgICAgIXBhZ2luYXRpb24uZWxcbiAgICAgICAgKSB7XG4gICAgICAgICAgdGhpcy51cGRhdGVQYXJhbWV0ZXIoJ3BhZ2luYXRpb24nLCB0aGlzLnBhZ2luYXRpb24pO1xuICAgICAgICAgIHBhZ2luYXRpb24uaW5pdCgpO1xuICAgICAgICAgIHBhZ2luYXRpb24ucmVuZGVyKCk7XG4gICAgICAgICAgcGFnaW5hdGlvbi51cGRhdGUoKTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBwYWdpbmF0aW9uLmRlc3Ryb3koKTtcbiAgICAgICAgICBwYWdpbmF0aW9uLmVsID0gbnVsbDtcbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy5zY3JvbGxiYXIpIHtcbiAgICAgICAgaWYgKFxuICAgICAgICAgIHRoaXMuc2Nyb2xsYmFyICYmXG4gICAgICAgICAgdHlwZW9mIHRoaXMuc2Nyb2xsYmFyICE9PSAnYm9vbGVhbicgJiZcbiAgICAgICAgICB0aGlzLnNjcm9sbGJhci5lbCAmJlxuICAgICAgICAgIHNjcm9sbGJhciAmJlxuICAgICAgICAgICFzY3JvbGxiYXIuZWxcbiAgICAgICAgKSB7XG4gICAgICAgICAgdGhpcy51cGRhdGVQYXJhbWV0ZXIoJ3Njcm9sbGJhcicsIHRoaXMuc2Nyb2xsYmFyKTtcbiAgICAgICAgICBzY3JvbGxiYXIuaW5pdCgpO1xuICAgICAgICAgIHNjcm9sbGJhci51cGRhdGVTaXplKCk7XG4gICAgICAgICAgc2Nyb2xsYmFyLnNldFRyYW5zbGF0ZSgpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHNjcm9sbGJhci5kZXN0cm95KCk7XG4gICAgICAgICAgc2Nyb2xsYmFyLmVsID0gbnVsbDtcbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy5uYXZpZ2F0aW9uKSB7XG4gICAgICAgIGlmIChcbiAgICAgICAgICB0aGlzLm5hdmlnYXRpb24gJiZcbiAgICAgICAgICB0eXBlb2YgdGhpcy5uYXZpZ2F0aW9uICE9PSAnYm9vbGVhbicgJiZcbiAgICAgICAgICB0aGlzLm5hdmlnYXRpb24ucHJldkVsICYmXG4gICAgICAgICAgdGhpcy5uYXZpZ2F0aW9uLm5leHRFbCAmJlxuICAgICAgICAgIG5hdmlnYXRpb24gJiZcbiAgICAgICAgICAhbmF2aWdhdGlvbi5wcmV2RWwgJiZcbiAgICAgICAgICAhbmF2aWdhdGlvbi5uZXh0RWxcbiAgICAgICAgKSB7XG4gICAgICAgICAgdGhpcy51cGRhdGVQYXJhbWV0ZXIoJ25hdmlnYXRpb24nLCB0aGlzLm5hdmlnYXRpb24pO1xuICAgICAgICAgIG5hdmlnYXRpb24uaW5pdCgpO1xuICAgICAgICAgIG5hdmlnYXRpb24udXBkYXRlKCk7XG4gICAgICAgIH0gZWxzZSBpZiAobmF2aWdhdGlvbi5wcmV2RWwgJiYgbmF2aWdhdGlvbi5uZXh0RWwpIHtcbiAgICAgICAgICBuYXZpZ2F0aW9uLmRlc3Ryb3koKTtcbiAgICAgICAgICBuYXZpZ2F0aW9uLm5leHRFbCA9IG51bGw7XG4gICAgICAgICAgbmF2aWdhdGlvbi5wcmV2RWwgPSBudWxsO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy50aHVtYnMgJiYgdGhpcy50aHVtYnMgJiYgdGhpcy50aHVtYnMuc3dpcGVyKSB7XG4gICAgICAgIHRoaXMudXBkYXRlUGFyYW1ldGVyKCd0aHVtYnMnLCB0aGlzLnRodW1icyk7XG4gICAgICAgIGNvbnN0IGluaXRpYWxpemVkID0gdGh1bWJzLmluaXQoKTtcbiAgICAgICAgaWYgKGluaXRpYWxpemVkKSB0aHVtYnMudXBkYXRlKHRydWUpO1xuICAgICAgfVxuXG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy5jb250cm9sbGVyICYmIHRoaXMuY29udHJvbGxlciAmJiB0aGlzLmNvbnRyb2xsZXIuY29udHJvbCkge1xuICAgICAgICB0aGlzLnN3aXBlclJlZi5jb250cm9sbGVyLmNvbnRyb2wgPSB0aGlzLmNvbnRyb2xsZXIuY29udHJvbDtcbiAgICAgIH1cblxuICAgICAgdGhpcy5zd2lwZXJSZWYudXBkYXRlKCk7XG4gICAgfSk7XG4gIH1cblxuICB1cGRhdGVTd2lwZXIoY2hhbmdlZFBhcmFtczogU2ltcGxlQ2hhbmdlcyB8IGFueSkge1xuICAgIHRoaXMuX25nWm9uZS5ydW5PdXRzaWRlQW5ndWxhcigoKSA9PiB7XG4gICAgICBpZiAoY2hhbmdlZFBhcmFtcy5jb25maWcpIHtcbiAgICAgICAgcmV0dXJuO1xuICAgICAgfVxuICAgICAgaWYgKCEoY2hhbmdlZFBhcmFtcyAmJiB0aGlzLnN3aXBlclJlZiAmJiAhdGhpcy5zd2lwZXJSZWYuZGVzdHJveWVkKSkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBmb3IgKGNvbnN0IGtleSBpbiBjaGFuZ2VkUGFyYW1zKSB7XG4gICAgICAgIGlmIChpZ25vcmVOZ09uQ2hhbmdlcy5pbmRleE9mKGtleSkgPj0gMCkge1xuICAgICAgICAgIGNvbnRpbnVlO1xuICAgICAgICB9XG4gICAgICAgIGNvbnN0IG5ld1ZhbHVlID0gY2hhbmdlZFBhcmFtc1trZXldPy5jdXJyZW50VmFsdWUgPz8gY2hhbmdlZFBhcmFtc1trZXldO1xuICAgICAgICB0aGlzLnVwZGF0ZVBhcmFtZXRlcihrZXksIG5ld1ZhbHVlKTtcbiAgICAgIH1cblxuICAgICAgaWYgKGNoYW5nZWRQYXJhbXMuYWxsb3dTbGlkZU5leHQpIHtcbiAgICAgICAgdGhpcy5zd2lwZXJSZWYuYWxsb3dTbGlkZU5leHQgPSB0aGlzLmFsbG93U2xpZGVOZXh0O1xuICAgICAgfVxuICAgICAgaWYgKGNoYW5nZWRQYXJhbXMuYWxsb3dTbGlkZVByZXYpIHtcbiAgICAgICAgdGhpcy5zd2lwZXJSZWYuYWxsb3dTbGlkZVByZXYgPSB0aGlzLmFsbG93U2xpZGVQcmV2O1xuICAgICAgfVxuICAgICAgaWYgKGNoYW5nZWRQYXJhbXMuZGlyZWN0aW9uKSB7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLmNoYW5nZURpcmVjdGlvbih0aGlzLmRpcmVjdGlvbiwgZmFsc2UpO1xuICAgICAgfVxuICAgICAgaWYgKGNoYW5nZWRQYXJhbXMuYnJlYWtwb2ludHMpIHtcbiAgICAgICAgaWYgKHRoaXMubG9vcCAmJiAhdGhpcy5sb29wZWRTbGlkZXMpIHtcbiAgICAgICAgICB0aGlzLmNhbGNMb29wZWRTbGlkZXMoKTtcbiAgICAgICAgfVxuICAgICAgICB0aGlzLnN3aXBlclJlZi5jdXJyZW50QnJlYWtwb2ludCA9IG51bGw7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLnNldEJyZWFrcG9pbnQoKTtcbiAgICAgIH1cblxuICAgICAgaWYgKGNoYW5nZWRQYXJhbXMudGh1bWJzIHx8IGNoYW5nZWRQYXJhbXMuY29udHJvbGxlcikge1xuICAgICAgICB0aGlzLnVwZGF0ZUluaXRTd2lwZXIoY2hhbmdlZFBhcmFtcyk7XG4gICAgICB9XG4gICAgICB0aGlzLnN3aXBlclJlZi51cGRhdGUoKTtcbiAgICB9KTtcbiAgfVxuXG4gIGNhbGNMb29wZWRTbGlkZXMoKSB7XG4gICAgaWYgKCF0aGlzLmxvb3ApIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgbGV0IHNsaWRlc1BlclZpZXdQYXJhbXMgPSB0aGlzLnNsaWRlc1BlclZpZXc7XG4gICAgaWYgKHRoaXMuYnJlYWtwb2ludHMpIHtcbiAgICAgIGNvbnN0IGJyZWFrcG9pbnQgPSBTd2lwZXIucHJvdG90eXBlLmdldEJyZWFrcG9pbnQodGhpcy5icmVha3BvaW50cyk7XG4gICAgICBjb25zdCBicmVha3BvaW50T25seVBhcmFtcyA9XG4gICAgICAgIGJyZWFrcG9pbnQgaW4gdGhpcy5icmVha3BvaW50cyA/IHRoaXMuYnJlYWtwb2ludHNbYnJlYWtwb2ludF0gOiB1bmRlZmluZWQ7XG4gICAgICBpZiAoYnJlYWtwb2ludE9ubHlQYXJhbXMgJiYgYnJlYWtwb2ludE9ubHlQYXJhbXMuc2xpZGVzUGVyVmlldykge1xuICAgICAgICBzbGlkZXNQZXJWaWV3UGFyYW1zID0gYnJlYWtwb2ludE9ubHlQYXJhbXMuc2xpZGVzUGVyVmlldztcbiAgICAgIH1cbiAgICB9XG4gICAgaWYgKHNsaWRlc1BlclZpZXdQYXJhbXMgPT09ICdhdXRvJykge1xuICAgICAgdGhpcy5sb29wZWRTbGlkZXMgPSB0aGlzLnNsaWRlcy5sZW5ndGg7XG4gICAgICByZXR1cm4gdGhpcy5zbGlkZXMubGVuZ3RoO1xuICAgIH1cbiAgICBsZXQgbG9vcGVkU2xpZGVzID0gdGhpcy5sb29wZWRTbGlkZXMgfHwgc2xpZGVzUGVyVmlld1BhcmFtcztcbiAgICBpZiAoIWxvb3BlZFNsaWRlcykge1xuICAgICAgLy8gP1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGlmICh0aGlzLmxvb3BBZGRpdGlvbmFsU2xpZGVzKSB7XG4gICAgICBsb29wZWRTbGlkZXMgKz0gdGhpcy5sb29wQWRkaXRpb25hbFNsaWRlcztcbiAgICB9XG4gICAgaWYgKGxvb3BlZFNsaWRlcyA+IHRoaXMuc2xpZGVzLmxlbmd0aCkge1xuICAgICAgbG9vcGVkU2xpZGVzID0gdGhpcy5zbGlkZXMubGVuZ3RoO1xuICAgIH1cbiAgICB0aGlzLmxvb3BlZFNsaWRlcyA9IGxvb3BlZFNsaWRlcztcbiAgICByZXR1cm4gbG9vcGVkU2xpZGVzO1xuICB9XG5cbiAgdXBkYXRlUGFyYW1ldGVyKGtleTogc3RyaW5nLCB2YWx1ZTogYW55KSB7XG4gICAgaWYgKCEodGhpcy5zd2lwZXJSZWYgJiYgIXRoaXMuc3dpcGVyUmVmLmRlc3Ryb3llZCkpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgY29uc3QgX2tleSA9IGtleS5yZXBsYWNlKC9eXy8sICcnKTtcbiAgICBjb25zdCBpc0N1cnJlbnRQYXJhbU9iaiA9IGlzT2JqZWN0KHRoaXMuc3dpcGVyUmVmLnBhcmFtc1tfa2V5XSk7XG5cbiAgICBpZiAoT2JqZWN0LmtleXModGhpcy5zd2lwZXJSZWYubW9kdWxlcykuaW5kZXhPZihfa2V5KSA+PSAwKSB7XG4gICAgICBjb25zdCBkZWZhdWx0UGFyYW1zID0gdGhpcy5zd2lwZXJSZWYubW9kdWxlc1tfa2V5XS5wYXJhbXNbX2tleV07XG4gICAgICBpZiAoaXNDdXJyZW50UGFyYW1PYmopIHtcbiAgICAgICAgZXh0ZW5kKHRoaXMuc3dpcGVyUmVmLnBhcmFtc1tfa2V5XSwgZGVmYXVsdFBhcmFtcyk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICB0aGlzLnN3aXBlclJlZi5wYXJhbXNbX2tleV0gPSBkZWZhdWx0UGFyYW1zO1xuICAgICAgfVxuICAgIH1cbiAgICBpZiAoX2tleSA9PT0gJ2VuYWJsZWQnKSB7XG4gICAgICBpZiAodmFsdWUgPT09IHRydWUpIHtcbiAgICAgICAgdGhpcy5zd2lwZXJSZWYuZW5hYmxlKCk7XG4gICAgICB9IGVsc2UgaWYgKHZhbHVlID09PSBmYWxzZSkge1xuICAgICAgICB0aGlzLnN3aXBlclJlZi5kaXNhYmxlKCk7XG4gICAgICB9XG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIGlmIChpc0N1cnJlbnRQYXJhbU9iaiAmJiBpc09iamVjdCh2YWx1ZSkpIHtcbiAgICAgIGV4dGVuZCh0aGlzLnN3aXBlclJlZi5wYXJhbXNbX2tleV0sIHZhbHVlKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdGhpcy5zd2lwZXJSZWYucGFyYW1zW19rZXldID0gdmFsdWU7XG4gICAgfVxuICB9XG4gIC8qKlxuICAgKiBAZGVwcmVjYXRlZCB3aWxsIGJlIHJlbW92ZWQgaW4gdXBjb21pbmcgdmVyc2lvbnNcbiAgICovXG4gIHNldEluZGV4KGluZGV4OiBudW1iZXIsIHNwZWVkPzogbnVtYmVyLCBzaWxlbnQ/OiBib29sZWFuKTogdm9pZCB7XG4gICAgaWYgKCF0aGlzLmlzU3dpcGVyQWN0aXZlKSB7XG4gICAgICB0aGlzLmluaXRpYWxTbGlkZSA9IGluZGV4O1xuICAgICAgcmV0dXJuO1xuICAgIH1cbiAgICBpZiAoaW5kZXggPT09IHRoaXMuc3dpcGVyUmVmLmFjdGl2ZUluZGV4KSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIHRoaXMuX25nWm9uZS5ydW5PdXRzaWRlQW5ndWxhcigoKSA9PiB7XG4gICAgICBpZiAodGhpcy5sb29wKSB7XG4gICAgICAgIHRoaXMuc3dpcGVyUmVmLnNsaWRlVG9Mb29wKGluZGV4LCBzcGVlZCwgIXNpbGVudCk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICB0aGlzLnN3aXBlclJlZi5zbGlkZVRvKGluZGV4LCBzcGVlZCwgIXNpbGVudCk7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICBuZ09uRGVzdHJveSgpIHtcbiAgICB0aGlzLl9uZ1pvbmUucnVuT3V0c2lkZUFuZ3VsYXIoKCkgPT4ge1xuICAgICAgdGhpcy5zd2lwZXJSZWY/LmRlc3Ryb3kodHJ1ZSwgZmFsc2UpO1xuICAgIH0pO1xuICB9XG59XG4iLCI8bmctY29udGVudCBzZWxlY3Q9XCJbc2xvdD1jb250YWluZXItc3RhcnRdXCI+PC9uZy1jb250ZW50PlxuPG5nLWNvbnRhaW5lciAqbmdJZj1cIm5hdmlnYXRpb24gJiYgc2hvd05hdmlnYXRpb25cIj5cbiAgPGRpdiBjbGFzcz1cInN3aXBlci1idXR0b24tcHJldlwiICNwcmV2RWxSZWY+PC9kaXY+XG4gIDxkaXYgY2xhc3M9XCJzd2lwZXItYnV0dG9uLW5leHRcIiAjbmV4dEVsUmVmPjwvZGl2PlxuPC9uZy1jb250YWluZXI+XG48ZGl2ICpuZ0lmPVwic2Nyb2xsYmFyICYmIHNob3dTY3JvbGxiYXJcIiBjbGFzcz1cInN3aXBlci1zY3JvbGxiYXJcIiAjc2Nyb2xsYmFyRWxSZWY+PC9kaXY+XG48ZGl2ICpuZ0lmPVwicGFnaW5hdGlvbiAmJiBzaG93UGFnaW5hdGlvblwiIGNsYXNzPVwic3dpcGVyLXBhZ2luYXRpb25cIiAjcGFnaW5hdGlvbkVsUmVmPjwvZGl2PlxuPGRpdiBbbmdDbGFzc109XCJ3cmFwcGVyQ2xhc3NcIiBbYXR0ci5pZF09XCJpZFwiPlxuICA8bmctY29udGVudCBzZWxlY3Q9XCJbc2xvdD13cmFwcGVyLXN0YXJ0XVwiPjwvbmctY29udGVudD5cbiAgPG5nLXRlbXBsYXRlXG4gICAgKm5nVGVtcGxhdGVPdXRsZXQ9XCJcbiAgICAgIHNsaWRlc1RlbXBsYXRlO1xuICAgICAgY29udGV4dDoge1xuICAgICAgICBsb29wU2xpZGVzOiBwcmVwZW5kU2xpZGVzLFxuICAgICAgICBrZXk6ICdwcmVwZW5kJ1xuICAgICAgfVxuICAgIFwiXG4gID48L25nLXRlbXBsYXRlPlxuICA8bmctdGVtcGxhdGVcbiAgICAqbmdUZW1wbGF0ZU91dGxldD1cIlxuICAgICAgc2xpZGVzVGVtcGxhdGU7XG4gICAgICBjb250ZXh0OiB7XG4gICAgICAgIGxvb3BTbGlkZXM6IGFjdGl2ZVNsaWRlcyxcbiAgICAgICAga2V5OiAnJ1xuICAgICAgfVxuICAgIFwiXG4gID48L25nLXRlbXBsYXRlPlxuICA8bmctdGVtcGxhdGVcbiAgICAqbmdUZW1wbGF0ZU91dGxldD1cIlxuICAgICAgc2xpZGVzVGVtcGxhdGU7XG4gICAgICBjb250ZXh0OiB7XG4gICAgICAgIGxvb3BTbGlkZXM6IGFwcGVuZFNsaWRlcyxcbiAgICAgICAga2V5OiAnYXBwZW5kJ1xuICAgICAgfVxuICAgIFwiXG4gID48L25nLXRlbXBsYXRlPlxuICA8bmctY29udGVudCBzZWxlY3Q9XCJbc2xvdD13cmFwcGVyLWVuZF1cIj48L25nLWNvbnRlbnQ+XG48L2Rpdj5cbjxuZy1jb250ZW50IHNlbGVjdD1cIltzbG90PWNvbnRhaW5lci1lbmRdXCI+PC9uZy1jb250ZW50PlxuXG48bmctdGVtcGxhdGUgI3NsaWRlc1RlbXBsYXRlIGxldC1sb29wU2xpZGVzPVwibG9vcFNsaWRlc1wiIGxldC1zbGlkZUtleT1cImtleVwiPlxuICA8ZGl2XG4gICAgKm5nRm9yPVwibGV0IHNsaWRlIG9mIGxvb3BTbGlkZXMgfCBhc3luY1wiXG4gICAgW25nQ2xhc3NdPVwiXG4gICAgICAoc2xpZGUuY2xhc3MgPyBzbGlkZS5jbGFzcyArICcgJyA6ICcnKSArXG4gICAgICBzbGlkZUNsYXNzICtcbiAgICAgIChzbGlkZUtleSAhPT0gJycgPyAnICcgKyBzbGlkZUR1cGxpY2F0ZUNsYXNzIDogJycpXG4gICAgXCJcbiAgICBbYXR0ci5kYXRhLXN3aXBlci1zbGlkZS1pbmRleF09XCJzbGlkZS52aXJ0dWFsSW5kZXggPyBzbGlkZS52aXJ0dWFsSW5kZXggOiBzbGlkZS5zbGlkZUluZGV4XCJcbiAgICBbYXR0ci5kYXRhLXN3aXBlci1hdXRvcGxheV09XCJzbGlkZS5hdXRvcGxheURlbGF5XCJcbiAgICBbc3R5bGVdPVwic3R5bGVcIlxuICAgIFtuZ1N3aXRjaF09XCJzbGlkZS56b29tXCJcbiAgPlxuICAgIDxkaXYgKm5nU3dpdGNoQ2FzZT1cInRydWVcIiBbbmdDbGFzc109XCJ6b29tQ29udGFpbmVyQ2xhc3NcIj5cbiAgICAgIDxuZy10ZW1wbGF0ZVxuICAgICAgICBbbmdUZW1wbGF0ZU91dGxldF09XCJzbGlkZS50ZW1wbGF0ZVwiXG4gICAgICAgIFtuZ1RlbXBsYXRlT3V0bGV0Q29udGV4dF09XCJ7XG4gICAgICAgICAgJGltcGxpY2l0OiBzbGlkZS5zbGlkZURhdGFcbiAgICAgICAgfVwiXG4gICAgICA+PC9uZy10ZW1wbGF0ZT5cbiAgICA8L2Rpdj5cbiAgICA8bmctY29udGFpbmVyICpuZ1N3aXRjaERlZmF1bHQ+XG4gICAgICA8bmctdGVtcGxhdGVcbiAgICAgICAgW25nVGVtcGxhdGVPdXRsZXRdPVwic2xpZGUudGVtcGxhdGVcIlxuICAgICAgICBbbmdUZW1wbGF0ZU91dGxldENvbnRleHRdPVwie1xuICAgICAgICAgICRpbXBsaWNpdDogc2xpZGUuc2xpZGVEYXRhXG4gICAgICAgIH1cIlxuICAgICAgPjwvbmctdGVtcGxhdGU+XG4gICAgPC9uZy1jb250YWluZXI+XG4gIDwvZGl2PlxuPC9uZy10ZW1wbGF0ZT5cbiJdfQ==