(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/core'), require('@angular/common'), require('swiper'), require('rxjs')) :
    typeof define === 'function' && define.amd ? define('swiper_angular', ['exports', '@angular/core', '@angular/common', 'swiper', 'rxjs'], factory) :
    (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.swiper_angular = {}, global.ng.core, global.ng.common, global.Swiper, global.rxjs));
}(this, (function (exports, i0, i1, Swiper, rxjs) { 'use strict';

    function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

    function _interopNamespace(e) {
        if (e && e.__esModule) return e;
        var n = Object.create(null);
        if (e) {
            Object.keys(e).forEach(function (k) {
                if (k !== 'default') {
                    var d = Object.getOwnPropertyDescriptor(e, k);
                    Object.defineProperty(n, k, d.get ? d : {
                        enumerable: true,
                        get: function () {
                            return e[k];
                        }
                    });
                }
            });
        }
        n['default'] = e;
        return Object.freeze(n);
    }

    var i0__namespace = /*#__PURE__*/_interopNamespace(i0);
    var i1__namespace = /*#__PURE__*/_interopNamespace(i1);
    var Swiper__default = /*#__PURE__*/_interopDefaultLegacy(Swiper);

    /*! *****************************************************************************
    Copyright (c) Microsoft Corporation.

    Permission to use, copy, modify, and/or distribute this software for any
    purpose with or without fee is hereby granted.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
    REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
    AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
    INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
    LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
    OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
    PERFORMANCE OF THIS SOFTWARE.
    ***************************************************************************** */
    /* global Reflect, Promise */
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b)
                if (Object.prototype.hasOwnProperty.call(b, p))
                    d[p] = b[p]; };
        return extendStatics(d, b);
    };
    function __extends(d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    }
    var __assign = function () {
        __assign = Object.assign || function __assign(t) {
            for (var s, i = 1, n = arguments.length; i < n; i++) {
                s = arguments[i];
                for (var p in s)
                    if (Object.prototype.hasOwnProperty.call(s, p))
                        t[p] = s[p];
            }
            return t;
        };
        return __assign.apply(this, arguments);
    };
    function __rest(s, e) {
        var t = {};
        for (var p in s)
            if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
                t[p] = s[p];
        if (s != null && typeof Object.getOwnPropertySymbols === "function")
            for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
                if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                    t[p[i]] = s[p[i]];
            }
        return t;
    }
    function __decorate(decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function")
            r = Reflect.decorate(decorators, target, key, desc);
        else
            for (var i = decorators.length - 1; i >= 0; i--)
                if (d = decorators[i])
                    r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    }
    function __param(paramIndex, decorator) {
        return function (target, key) { decorator(target, key, paramIndex); };
    }
    function __metadata(metadataKey, metadataValue) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function")
            return Reflect.metadata(metadataKey, metadataValue);
    }
    function __awaiter(thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try {
                step(generator.next(value));
            }
            catch (e) {
                reject(e);
            } }
            function rejected(value) { try {
                step(generator["throw"](value));
            }
            catch (e) {
                reject(e);
            } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    }
    function __generator(thisArg, body) {
        var _ = { label: 0, sent: function () { if (t[0] & 1)
                throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
        return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function () { return this; }), g;
        function verb(n) { return function (v) { return step([n, v]); }; }
        function step(op) {
            if (f)
                throw new TypeError("Generator is already executing.");
            while (_)
                try {
                    if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done)
                        return t;
                    if (y = 0, t)
                        op = [op[0] & 2, t.value];
                    switch (op[0]) {
                        case 0:
                        case 1:
                            t = op;
                            break;
                        case 4:
                            _.label++;
                            return { value: op[1], done: false };
                        case 5:
                            _.label++;
                            y = op[1];
                            op = [0];
                            continue;
                        case 7:
                            op = _.ops.pop();
                            _.trys.pop();
                            continue;
                        default:
                            if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) {
                                _ = 0;
                                continue;
                            }
                            if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) {
                                _.label = op[1];
                                break;
                            }
                            if (op[0] === 6 && _.label < t[1]) {
                                _.label = t[1];
                                t = op;
                                break;
                            }
                            if (t && _.label < t[2]) {
                                _.label = t[2];
                                _.ops.push(op);
                                break;
                            }
                            if (t[2])
                                _.ops.pop();
                            _.trys.pop();
                            continue;
                    }
                    op = body.call(thisArg, _);
                }
                catch (e) {
                    op = [6, e];
                    y = 0;
                }
                finally {
                    f = t = 0;
                }
            if (op[0] & 5)
                throw op[1];
            return { value: op[0] ? op[1] : void 0, done: true };
        }
    }
    var __createBinding = Object.create ? (function (o, m, k, k2) {
        if (k2 === undefined)
            k2 = k;
        Object.defineProperty(o, k2, { enumerable: true, get: function () { return m[k]; } });
    }) : (function (o, m, k, k2) {
        if (k2 === undefined)
            k2 = k;
        o[k2] = m[k];
    });
    function __exportStar(m, o) {
        for (var p in m)
            if (p !== "default" && !Object.prototype.hasOwnProperty.call(o, p))
                __createBinding(o, m, p);
    }
    function __values(o) {
        var s = typeof Symbol === "function" && Symbol.iterator, m = s && o[s], i = 0;
        if (m)
            return m.call(o);
        if (o && typeof o.length === "number")
            return {
                next: function () {
                    if (o && i >= o.length)
                        o = void 0;
                    return { value: o && o[i++], done: !o };
                }
            };
        throw new TypeError(s ? "Object is not iterable." : "Symbol.iterator is not defined.");
    }
    function __read(o, n) {
        var m = typeof Symbol === "function" && o[Symbol.iterator];
        if (!m)
            return o;
        var i = m.call(o), r, ar = [], e;
        try {
            while ((n === void 0 || n-- > 0) && !(r = i.next()).done)
                ar.push(r.value);
        }
        catch (error) {
            e = { error: error };
        }
        finally {
            try {
                if (r && !r.done && (m = i["return"]))
                    m.call(i);
            }
            finally {
                if (e)
                    throw e.error;
            }
        }
        return ar;
    }
    /** @deprecated */
    function __spread() {
        for (var ar = [], i = 0; i < arguments.length; i++)
            ar = ar.concat(__read(arguments[i]));
        return ar;
    }
    /** @deprecated */
    function __spreadArrays() {
        for (var s = 0, i = 0, il = arguments.length; i < il; i++)
            s += arguments[i].length;
        for (var r = Array(s), k = 0, i = 0; i < il; i++)
            for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
                r[k] = a[j];
        return r;
    }
    function __spreadArray(to, from, pack) {
        if (pack || arguments.length === 2)
            for (var i = 0, l = from.length, ar; i < l; i++) {
                if (ar || !(i in from)) {
                    if (!ar)
                        ar = Array.prototype.slice.call(from, 0, i);
                    ar[i] = from[i];
                }
            }
        return to.concat(ar || Array.prototype.slice.call(from));
    }
    function __await(v) {
        return this instanceof __await ? (this.v = v, this) : new __await(v);
    }
    function __asyncGenerator(thisArg, _arguments, generator) {
        if (!Symbol.asyncIterator)
            throw new TypeError("Symbol.asyncIterator is not defined.");
        var g = generator.apply(thisArg, _arguments || []), i, q = [];
        return i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function () { return this; }, i;
        function verb(n) { if (g[n])
            i[n] = function (v) { return new Promise(function (a, b) { q.push([n, v, a, b]) > 1 || resume(n, v); }); }; }
        function resume(n, v) { try {
            step(g[n](v));
        }
        catch (e) {
            settle(q[0][3], e);
        } }
        function step(r) { r.value instanceof __await ? Promise.resolve(r.value.v).then(fulfill, reject) : settle(q[0][2], r); }
        function fulfill(value) { resume("next", value); }
        function reject(value) { resume("throw", value); }
        function settle(f, v) { if (f(v), q.shift(), q.length)
            resume(q[0][0], q[0][1]); }
    }
    function __asyncDelegator(o) {
        var i, p;
        return i = {}, verb("next"), verb("throw", function (e) { throw e; }), verb("return"), i[Symbol.iterator] = function () { return this; }, i;
        function verb(n, f) { i[n] = o[n] ? function (v) { return (p = !p) ? { value: __await(o[n](v)), done: n === "return" } : f ? f(v) : v; } : f; }
    }
    function __asyncValues(o) {
        if (!Symbol.asyncIterator)
            throw new TypeError("Symbol.asyncIterator is not defined.");
        var m = o[Symbol.asyncIterator], i;
        return m ? m.call(o) : (o = typeof __values === "function" ? __values(o) : o[Symbol.iterator](), i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function () { return this; }, i);
        function verb(n) { i[n] = o[n] && function (v) { return new Promise(function (resolve, reject) { v = o[n](v), settle(resolve, reject, v.done, v.value); }); }; }
        function settle(resolve, reject, d, v) { Promise.resolve(v).then(function (v) { resolve({ value: v, done: d }); }, reject); }
    }
    function __makeTemplateObject(cooked, raw) {
        if (Object.defineProperty) {
            Object.defineProperty(cooked, "raw", { value: raw });
        }
        else {
            cooked.raw = raw;
        }
        return cooked;
    }
    ;
    var __setModuleDefault = Object.create ? (function (o, v) {
        Object.defineProperty(o, "default", { enumerable: true, value: v });
    }) : function (o, v) {
        o["default"] = v;
    };
    function __importStar(mod) {
        if (mod && mod.__esModule)
            return mod;
        var result = {};
        if (mod != null)
            for (var k in mod)
                if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k))
                    __createBinding(result, mod, k);
        __setModuleDefault(result, mod);
        return result;
    }
    function __importDefault(mod) {
        return (mod && mod.__esModule) ? mod : { default: mod };
    }
    function __classPrivateFieldGet(receiver, state, kind, f) {
        if (kind === "a" && !f)
            throw new TypeError("Private accessor was defined without a getter");
        if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver))
            throw new TypeError("Cannot read private member from an object whose class did not declare it");
        return kind === "m" ? f : kind === "a" ? f.call(receiver) : f ? f.value : state.get(receiver);
    }
    function __classPrivateFieldSet(receiver, state, value, kind, f) {
        if (kind === "m")
            throw new TypeError("Private method is not writable");
        if (kind === "a" && !f)
            throw new TypeError("Private accessor was defined without a setter");
        if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver))
            throw new TypeError("Cannot write private member to an object whose class did not declare it");
        return (kind === "a" ? f.call(receiver, value) : f ? f.value = value : state.set(receiver, value)), value;
    }

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
        var noExtend = ['__proto__', 'constructor', 'prototype'];
        Object.keys(src)
            .filter(function (key) { return noExtend.indexOf(key) < 0; })
            .forEach(function (key) {
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
        return value != null && "" + value !== 'false';
    }
    var ignoreNgOnChanges = ['pagination', 'navigation', 'scrollbar', 'virtual'];
    function setProperty(val, obj) {
        if (obj === void 0) { obj = {}; }
        if (isObject(val)) {
            return val;
        }
        if (coerceBooleanProperty(val) === true) {
            return obj;
        }
        return false;
    }

    /* underscore in name -> watch for changes */
    var paramsList = [
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
    var allowedParams = paramsList.map(function (key) { return key.replace(/_/, ''); });
    function getParams(obj) {
        if (obj === void 0) { obj = {}; }
        var params = {
            on: {},
        };
        var passedParams = {};
        extend(params, Swiper__default['default'].defaults);
        extend(params, Swiper__default['default'].extendedDefaults);
        params._emitClasses = true;
        var rest = {};
        Object.keys(obj).forEach(function (key) {
            var _key = key.replace(/^_/, '');
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
        return { params: params, passedParams: passedParams, rest: rest };
    }

    var SwiperSlideDirective = /** @class */ (function () {
        function SwiperSlideDirective(template) {
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
        Object.defineProperty(SwiperSlideDirective.prototype, "zoom", {
            get: function () {
                return this._zoom;
            },
            set: function (val) {
                this._zoom = coerceBooleanProperty(val);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperSlideDirective.prototype, "classNames", {
            get: function () {
                return this._classNames;
            },
            set: function (val) {
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
            },
            enumerable: false,
            configurable: true
        });
        SwiperSlideDirective.prototype._hasClass = function (classNames) {
            var _this = this;
            return classNames.some(function (className) { return _this._classNames.indexOf(className) >= 0; });
        };
        return SwiperSlideDirective;
    }());
    SwiperSlideDirective.ɵfac = i0__namespace.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperSlideDirective, deps: [{ token: i0__namespace.TemplateRef }], target: i0__namespace.ɵɵFactoryTarget.Directive });
    SwiperSlideDirective.ɵdir = i0__namespace.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "12.2.2", type: SwiperSlideDirective, selector: "ng-template[swiperSlide]", inputs: { virtualIndex: "virtualIndex", class: "class", autoplayDelay: ["data-swiper-autoplay", "autoplayDelay"], zoom: "zoom" }, ngImport: i0__namespace });
    i0__namespace.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperSlideDirective, decorators: [{
                type: i0.Directive,
                args: [{
                        selector: 'ng-template[swiperSlide]',
                    }]
            }], ctorParameters: function () { return [{ type: i0__namespace.TemplateRef }]; }, propDecorators: { virtualIndex: [{
                    type: i0.Input
                }], class: [{
                    type: i0.Input
                }], autoplayDelay: [{
                    type: i0.Input,
                    args: ['data-swiper-autoplay']
                }], zoom: [{
                    type: i0.Input
                }] } });

    var SwiperComponent = /** @class */ (function () {
        function SwiperComponent(_ngZone, elementRef, _changeDetectorRef, _platformId) {
            var _this = this;
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
            this.s__beforeBreakpoint = new i0.EventEmitter();
            // prettier-ignore
            this.s__containerClasses = new i0.EventEmitter();
            // prettier-ignore
            this.s__slideClass = new i0.EventEmitter();
            // prettier-ignore
            this.s__swiper = new i0.EventEmitter();
            // prettier-ignore
            this.s_activeIndexChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_afterInit = new i0.EventEmitter();
            // prettier-ignore
            this.s_autoplay = new i0.EventEmitter();
            // prettier-ignore
            this.s_autoplayStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_autoplayStop = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeDestroy = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeInit = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeLoopFix = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeResize = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeSlideChangeStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_beforeTransitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_breakpoint = new i0.EventEmitter();
            // prettier-ignore
            this.s_changeDirection = new i0.EventEmitter();
            // prettier-ignore
            this.s_click = new i0.EventEmitter();
            // prettier-ignore
            this.s_doubleTap = new i0.EventEmitter();
            // prettier-ignore
            this.s_doubleClick = new i0.EventEmitter();
            // prettier-ignore
            this.s_destroy = new i0.EventEmitter();
            // prettier-ignore
            this.s_fromEdge = new i0.EventEmitter();
            // prettier-ignore
            this.s_hashChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_hashSet = new i0.EventEmitter();
            // prettier-ignore
            this.s_imagesReady = new i0.EventEmitter();
            // prettier-ignore
            this.s_init = new i0.EventEmitter();
            // prettier-ignore
            this.s_keyPress = new i0.EventEmitter();
            // prettier-ignore
            this.s_lazyImageLoad = new i0.EventEmitter();
            // prettier-ignore
            this.s_lazyImageReady = new i0.EventEmitter();
            // prettier-ignore
            this.s_loopFix = new i0.EventEmitter();
            // prettier-ignore
            this.s_momentumBounce = new i0.EventEmitter();
            // prettier-ignore
            this.s_navigationHide = new i0.EventEmitter();
            // prettier-ignore
            this.s_navigationShow = new i0.EventEmitter();
            // prettier-ignore
            this.s_observerUpdate = new i0.EventEmitter();
            // prettier-ignore
            this.s_orientationchange = new i0.EventEmitter();
            // prettier-ignore
            this.s_paginationHide = new i0.EventEmitter();
            // prettier-ignore
            this.s_paginationRender = new i0.EventEmitter();
            // prettier-ignore
            this.s_paginationShow = new i0.EventEmitter();
            // prettier-ignore
            this.s_paginationUpdate = new i0.EventEmitter();
            // prettier-ignore
            this.s_progress = new i0.EventEmitter();
            // prettier-ignore
            this.s_reachBeginning = new i0.EventEmitter();
            // prettier-ignore
            this.s_reachEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_realIndexChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_resize = new i0.EventEmitter();
            // prettier-ignore
            this.s_scroll = new i0.EventEmitter();
            // prettier-ignore
            this.s_scrollbarDragEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_scrollbarDragMove = new i0.EventEmitter();
            // prettier-ignore
            this.s_scrollbarDragStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_setTransition = new i0.EventEmitter();
            // prettier-ignore
            this.s_setTranslate = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideChangeTransitionEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideChangeTransitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideNextTransitionEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideNextTransitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_slidePrevTransitionEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_slidePrevTransitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideResetTransitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_slideResetTransitionEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_sliderMove = new i0.EventEmitter();
            // prettier-ignore
            this.s_sliderFirstMove = new i0.EventEmitter();
            // prettier-ignore
            this.s_slidesLengthChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_slidesGridLengthChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_snapGridLengthChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_snapIndexChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_tap = new i0.EventEmitter();
            // prettier-ignore
            this.s_toEdge = new i0.EventEmitter();
            // prettier-ignore
            this.s_touchEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_touchMove = new i0.EventEmitter();
            // prettier-ignore
            this.s_touchMoveOpposite = new i0.EventEmitter();
            // prettier-ignore
            this.s_touchStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_transitionEnd = new i0.EventEmitter();
            // prettier-ignore
            this.s_transitionStart = new i0.EventEmitter();
            // prettier-ignore
            this.s_update = new i0.EventEmitter();
            // prettier-ignore
            this.s_zoomChange = new i0.EventEmitter();
            // prettier-ignore
            this.s_swiper = new i0.EventEmitter();
            this.indexChange = new i0.EventEmitter();
            this._activeSlides = new rxjs.Subject();
            this.containerClasses = 'swiper';
            this.slidesChanges = function (val) {
                _this.slides = val.map(function (slide, index) {
                    slide.slideIndex = index;
                    slide.classNames = _this.slideClass || '';
                    return slide;
                });
                if (_this.loop && !_this.loopedSlides) {
                    _this.calcLoopedSlides();
                }
                if (!_this.virtual) {
                    if (_this.loopedSlides) {
                        _this.prependSlides = rxjs.of(_this.slides.slice(_this.slides.length - _this.loopedSlides));
                        _this.appendSlides = rxjs.of(_this.slides.slice(0, _this.loopedSlides));
                    }
                }
                else if (_this.swiperRef && _this.swiperRef.virtual) {
                    _this._ngZone.runOutsideAngular(function () {
                        _this.swiperRef.virtual.slides = _this.slides;
                        _this.swiperRef.virtual.update(true);
                    });
                }
                _this._changeDetectorRef.detectChanges();
            };
            this.style = null;
            this.updateVirtualSlides = function (virtualData) {
                var _d;
                // TODO: type virtualData
                if (!_this.swiperRef ||
                    (_this.currentVirtualData &&
                        _this.currentVirtualData.from === virtualData.from &&
                        _this.currentVirtualData.to === virtualData.to &&
                        _this.currentVirtualData.offset === virtualData.offset)) {
                    return;
                }
                _this.style = _this.swiperRef.isHorizontal()
                    ? (_d = {},
                        _d[_this.swiperRef.rtlTranslate ? 'right' : 'left'] = virtualData.offset + "px",
                        _d) : {
                    top: virtualData.offset + "px",
                };
                _this.currentVirtualData = virtualData;
                _this._activeSlides.next(virtualData.slides);
                _this._ngZone.run(function () {
                    _this._changeDetectorRef.detectChanges();
                });
                _this._ngZone.runOutsideAngular(function () {
                    _this.swiperRef.updateSlides();
                    _this.swiperRef.updateProgress();
                    _this.swiperRef.updateSlidesClasses();
                    if (_this.swiperRef.lazy && _this.swiperRef.params.lazy['enabled']) {
                        _this.swiperRef.lazy.load();
                    }
                    _this.swiperRef.virtual.update(true);
                });
                return;
            };
        }
        Object.defineProperty(SwiperComponent.prototype, "navigation", {
            get: function () {
                return this._navigation;
            },
            set: function (val) {
                var _a, _b, _c;
                var currentNext = typeof this._navigation !== 'boolean' && this._navigation !== ''
                    ? (_a = this._navigation) === null || _a === void 0 ? void 0 : _a.nextEl
                    : null;
                var currentPrev = typeof this._navigation !== 'boolean' && this._navigation !== ''
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
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "pagination", {
            get: function () {
                return this._pagination;
            },
            set: function (val) {
                var _a;
                var current = typeof this._pagination !== 'boolean' && this._pagination !== ''
                    ? (_a = this._pagination) === null || _a === void 0 ? void 0 : _a.el
                    : null;
                this._pagination = setProperty(val, {
                    el: current || null,
                });
                this.showPagination = isShowEl(val, this._pagination, this._paginationElRef);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "scrollbar", {
            get: function () {
                return this._scrollbar;
            },
            set: function (val) {
                var _a;
                var current = typeof this._scrollbar !== 'boolean' && this._scrollbar !== '' ? (_a = this._scrollbar) === null || _a === void 0 ? void 0 : _a.el : null;
                this._scrollbar = setProperty(val, {
                    el: current || null,
                });
                this.showScrollbar = isShowEl(val, this._scrollbar, this._scrollbarElRef);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "virtual", {
            get: function () {
                return this._virtual;
            },
            set: function (val) {
                this._virtual = setProperty(val);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "index", {
            set: function (index) {
                console.warn('`[(index)]` prop is deprecated and will be removed in upcoming versions');
                this.setIndex(index);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "config", {
            set: function (val) {
                this.updateSwiper(val);
                var params = getParams(val).params;
                Object.assign(this, params);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "prevElRef", {
            set: function (el) {
                this._prevElRef = el;
                this._setElement(el, this.navigation, 'navigation', 'prevEl');
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "nextElRef", {
            set: function (el) {
                this._nextElRef = el;
                this._setElement(el, this.navigation, 'navigation', 'nextEl');
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "scrollbarElRef", {
            set: function (el) {
                this._scrollbarElRef = el;
                this._setElement(el, this.scrollbar, 'scrollbar');
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "paginationElRef", {
            set: function (el) {
                this._paginationElRef = el;
                this._setElement(el, this.pagination, 'pagination');
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "activeSlides", {
            get: function () {
                if (this.virtual) {
                    return this._activeSlides;
                }
                return rxjs.of(this.slides);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SwiperComponent.prototype, "zoomContainerClass", {
            get: function () {
                return this.zoom && typeof this.zoom !== 'boolean'
                    ? this.zoom.containerClass
                    : 'swiper-zoom-container';
            },
            enumerable: false,
            configurable: true
        });
        SwiperComponent.prototype._setElement = function (el, ref, update, key) {
            if (key === void 0) { key = 'el'; }
            if (!el || !ref) {
                return;
            }
            if (ref && el.nativeElement) {
                if (ref[key] === el.nativeElement) {
                    return;
                }
                ref[key] = el.nativeElement;
            }
            var updateObj = {};
            updateObj[update] = true;
            this.updateInitSwiper(updateObj);
        };
        SwiperComponent.prototype.ngOnInit = function () {
            var params = getParams(this).params;
            Object.assign(this, params);
        };
        SwiperComponent.prototype.ngAfterViewInit = function () {
            var _this = this;
            this.childrenSlidesInit();
            this.initSwiper();
            this._changeDetectorRef.detectChanges();
            setTimeout(function () {
                _this.s_swiper.emit(_this.swiperRef);
            });
        };
        SwiperComponent.prototype.childrenSlidesInit = function () {
            this.slidesChanges(this.slidesEl);
            this.slidesEl.changes.subscribe(this.slidesChanges);
        };
        Object.defineProperty(SwiperComponent.prototype, "isSwiperActive", {
            get: function () {
                return this.swiperRef && !this.swiperRef.destroyed;
            },
            enumerable: false,
            configurable: true
        });
        SwiperComponent.prototype.initSwiper = function () {
            var _this = this;
            var _d = getParams(this), swiperParams = _d.params, passedParams = _d.passedParams;
            Object.assign(this, swiperParams);
            this._ngZone.runOutsideAngular(function () {
                swiperParams.init = false;
                if (!swiperParams.virtual) {
                    swiperParams.observer = true;
                }
                swiperParams.onAny = function (eventName) {
                    var args = [];
                    for (var _i = 1; _i < arguments.length; _i++) {
                        args[_i - 1] = arguments[_i];
                    }
                    var emitter = _this[('s_' + eventName)];
                    if (emitter) {
                        emitter.emit.apply(emitter, __spreadArray([], __read(args)));
                    }
                };
                var _slideClasses = function (_, updated) {
                    updated.forEach(function (_d, index) {
                        var slideEl = _d.slideEl, classNames = _d.classNames;
                        var dataIndex = slideEl.getAttribute('data-swiper-slide-index');
                        var slideIndex = dataIndex ? parseInt(dataIndex) : index;
                        if (_this.virtual) {
                            var virtualSlide = _this.slides.find(function (item) {
                                return item.virtualIndex && item.virtualIndex === slideIndex;
                            });
                            if (virtualSlide) {
                                virtualSlide.classNames = classNames;
                                return;
                            }
                        }
                        if (_this.slides[slideIndex]) {
                            _this.slides[slideIndex].classNames = classNames;
                        }
                    });
                    _this._changeDetectorRef.detectChanges();
                };
                var _containerClasses = function (_, classes) {
                    setTimeout(function () {
                        _this.containerClasses = classes;
                    });
                };
                Object.assign(swiperParams.on, {
                    _containerClasses: _containerClasses,
                    _slideClasses: _slideClasses,
                });
                var swiperRef = new Swiper__default['default'](swiperParams);
                swiperRef.loopCreate = function () { };
                swiperRef.loopDestroy = function () { };
                if (swiperParams.loop) {
                    swiperRef.loopedSlides = _this.loopedSlides;
                }
                var isVirtualEnabled = typeof swiperRef.params.virtual !== 'undefined' &&
                    typeof swiperRef.params.virtual !== 'boolean' &&
                    swiperRef.params.virtual.enabled;
                if (swiperRef.virtual && isVirtualEnabled) {
                    swiperRef.virtual.slides = _this.slides;
                    var extendWith = {
                        cache: false,
                        slides: _this.slides,
                        renderExternal: _this.updateVirtualSlides,
                        renderExternalUpdate: false,
                    };
                    extend(swiperRef.params.virtual, extendWith);
                    extend(swiperRef.originalParams.virtual, extendWith);
                }
                if (i1.isPlatformBrowser(_this._platformId)) {
                    _this.swiperRef = swiperRef.init(_this.elementRef.nativeElement);
                    var isEnabled = typeof _this.swiperRef.params.virtual !== 'undefined' &&
                        typeof _this.swiperRef.params.virtual !== 'boolean' &&
                        _this.swiperRef.params.virtual.enabled;
                    if (_this.swiperRef.virtual && isEnabled) {
                        _this.swiperRef.virtual.update(true);
                    }
                    _this._changeDetectorRef.detectChanges();
                    swiperRef.on('slideChange', function () {
                        _this.indexChange.emit(_this.swiperRef.realIndex);
                    });
                }
            });
        };
        SwiperComponent.prototype.ngOnChanges = function (changedParams) {
            this.updateSwiper(changedParams);
            this._changeDetectorRef.detectChanges();
        };
        SwiperComponent.prototype.updateInitSwiper = function (changedParams) {
            var _this = this;
            if (!(changedParams && this.swiperRef && !this.swiperRef.destroyed)) {
                return;
            }
            this._ngZone.runOutsideAngular(function () {
                var _d = _this.swiperRef, currentParams = _d.params, pagination = _d.pagination, navigation = _d.navigation, scrollbar = _d.scrollbar, virtual = _d.virtual, thumbs = _d.thumbs;
                if (changedParams.pagination) {
                    if (_this.pagination &&
                        typeof _this.pagination !== 'boolean' &&
                        _this.pagination.el &&
                        pagination &&
                        !pagination.el) {
                        _this.updateParameter('pagination', _this.pagination);
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
                    if (_this.scrollbar &&
                        typeof _this.scrollbar !== 'boolean' &&
                        _this.scrollbar.el &&
                        scrollbar &&
                        !scrollbar.el) {
                        _this.updateParameter('scrollbar', _this.scrollbar);
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
                    if (_this.navigation &&
                        typeof _this.navigation !== 'boolean' &&
                        _this.navigation.prevEl &&
                        _this.navigation.nextEl &&
                        navigation &&
                        !navigation.prevEl &&
                        !navigation.nextEl) {
                        _this.updateParameter('navigation', _this.navigation);
                        navigation.init();
                        navigation.update();
                    }
                    else if (navigation.prevEl && navigation.nextEl) {
                        navigation.destroy();
                        navigation.nextEl = null;
                        navigation.prevEl = null;
                    }
                }
                if (changedParams.thumbs && _this.thumbs && _this.thumbs.swiper) {
                    _this.updateParameter('thumbs', _this.thumbs);
                    var initialized = thumbs.init();
                    if (initialized)
                        thumbs.update(true);
                }
                if (changedParams.controller && _this.controller && _this.controller.control) {
                    _this.swiperRef.controller.control = _this.controller.control;
                }
                _this.swiperRef.update();
            });
        };
        SwiperComponent.prototype.updateSwiper = function (changedParams) {
            var _this = this;
            this._ngZone.runOutsideAngular(function () {
                var _a, _b;
                if (changedParams.config) {
                    return;
                }
                if (!(changedParams && _this.swiperRef && !_this.swiperRef.destroyed)) {
                    return;
                }
                for (var key in changedParams) {
                    if (ignoreNgOnChanges.indexOf(key) >= 0) {
                        continue;
                    }
                    var newValue = (_b = (_a = changedParams[key]) === null || _a === void 0 ? void 0 : _a.currentValue) !== null && _b !== void 0 ? _b : changedParams[key];
                    _this.updateParameter(key, newValue);
                }
                if (changedParams.allowSlideNext) {
                    _this.swiperRef.allowSlideNext = _this.allowSlideNext;
                }
                if (changedParams.allowSlidePrev) {
                    _this.swiperRef.allowSlidePrev = _this.allowSlidePrev;
                }
                if (changedParams.direction) {
                    _this.swiperRef.changeDirection(_this.direction, false);
                }
                if (changedParams.breakpoints) {
                    if (_this.loop && !_this.loopedSlides) {
                        _this.calcLoopedSlides();
                    }
                    _this.swiperRef.currentBreakpoint = null;
                    _this.swiperRef.setBreakpoint();
                }
                if (changedParams.thumbs || changedParams.controller) {
                    _this.updateInitSwiper(changedParams);
                }
                _this.swiperRef.update();
            });
        };
        SwiperComponent.prototype.calcLoopedSlides = function () {
            if (!this.loop) {
                return;
            }
            var slidesPerViewParams = this.slidesPerView;
            if (this.breakpoints) {
                var breakpoint = Swiper__default['default'].prototype.getBreakpoint(this.breakpoints);
                var breakpointOnlyParams = breakpoint in this.breakpoints ? this.breakpoints[breakpoint] : undefined;
                if (breakpointOnlyParams && breakpointOnlyParams.slidesPerView) {
                    slidesPerViewParams = breakpointOnlyParams.slidesPerView;
                }
            }
            if (slidesPerViewParams === 'auto') {
                this.loopedSlides = this.slides.length;
                return this.slides.length;
            }
            var loopedSlides = this.loopedSlides || slidesPerViewParams;
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
        };
        SwiperComponent.prototype.updateParameter = function (key, value) {
            if (!(this.swiperRef && !this.swiperRef.destroyed)) {
                return;
            }
            var _key = key.replace(/^_/, '');
            var isCurrentParamObj = isObject(this.swiperRef.params[_key]);
            if (Object.keys(this.swiperRef.modules).indexOf(_key) >= 0) {
                var defaultParams = this.swiperRef.modules[_key].params[_key];
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
        };
        /**
         * @deprecated will be removed in upcoming versions
         */
        SwiperComponent.prototype.setIndex = function (index, speed, silent) {
            var _this = this;
            if (!this.isSwiperActive) {
                this.initialSlide = index;
                return;
            }
            if (index === this.swiperRef.activeIndex) {
                return;
            }
            this._ngZone.runOutsideAngular(function () {
                if (_this.loop) {
                    _this.swiperRef.slideToLoop(index, speed, !silent);
                }
                else {
                    _this.swiperRef.slideTo(index, speed, !silent);
                }
            });
        };
        SwiperComponent.prototype.ngOnDestroy = function () {
            var _this = this;
            this._ngZone.runOutsideAngular(function () {
                var _a;
                (_a = _this.swiperRef) === null || _a === void 0 ? void 0 : _a.destroy(true, false);
            });
        };
        return SwiperComponent;
    }());
    SwiperComponent.ɵfac = i0__namespace.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperComponent, deps: [{ token: i0__namespace.NgZone }, { token: i0__namespace.ElementRef }, { token: i0__namespace.ChangeDetectorRef }, { token: i0.PLATFORM_ID }], target: i0__namespace.ɵɵFactoryTarget.Component });
    SwiperComponent.ɵcmp = i0__namespace.ɵɵngDeclareComponent({ minVersion: "12.0.0", version: "12.2.2", type: SwiperComponent, selector: "swiper, [swiper]", inputs: { enabled: "enabled", direction: "direction", touchEventsTarget: "touchEventsTarget", initialSlide: "initialSlide", speed: "speed", cssMode: "cssMode", updateOnWindowResize: "updateOnWindowResize", resizeObserver: "resizeObserver", nested: "nested", focusableElements: "focusableElements", width: "width", height: "height", preventInteractionOnTransition: "preventInteractionOnTransition", userAgent: "userAgent", url: "url", edgeSwipeDetection: "edgeSwipeDetection", edgeSwipeThreshold: "edgeSwipeThreshold", freeMode: "freeMode", autoHeight: "autoHeight", setWrapperSize: "setWrapperSize", virtualTranslate: "virtualTranslate", effect: "effect", breakpoints: "breakpoints", spaceBetween: "spaceBetween", slidesPerView: "slidesPerView", grid: "grid", slidesPerGroup: "slidesPerGroup", slidesPerGroupSkip: "slidesPerGroupSkip", centeredSlides: "centeredSlides", centeredSlidesBounds: "centeredSlidesBounds", slidesOffsetBefore: "slidesOffsetBefore", slidesOffsetAfter: "slidesOffsetAfter", normalizeSlideIndex: "normalizeSlideIndex", centerInsufficientSlides: "centerInsufficientSlides", watchOverflow: "watchOverflow", roundLengths: "roundLengths", touchRatio: "touchRatio", touchAngle: "touchAngle", simulateTouch: "simulateTouch", shortSwipes: "shortSwipes", longSwipes: "longSwipes", longSwipesRatio: "longSwipesRatio", longSwipesMs: "longSwipesMs", followFinger: "followFinger", allowTouchMove: "allowTouchMove", threshold: "threshold", touchMoveStopPropagation: "touchMoveStopPropagation", touchStartPreventDefault: "touchStartPreventDefault", touchStartForcePreventDefault: "touchStartForcePreventDefault", touchReleaseOnEdges: "touchReleaseOnEdges", uniqueNavElements: "uniqueNavElements", resistance: "resistance", resistanceRatio: "resistanceRatio", watchSlidesProgress: "watchSlidesProgress", grabCursor: "grabCursor", preventClicks: "preventClicks", preventClicksPropagation: "preventClicksPropagation", slideToClickedSlide: "slideToClickedSlide", preloadImages: "preloadImages", updateOnImagesReady: "updateOnImagesReady", loop: "loop", loopAdditionalSlides: "loopAdditionalSlides", loopedSlides: "loopedSlides", loopFillGroupWithBlank: "loopFillGroupWithBlank", loopPreventsSlide: "loopPreventsSlide", allowSlidePrev: "allowSlidePrev", allowSlideNext: "allowSlideNext", swipeHandler: "swipeHandler", noSwiping: "noSwiping", noSwipingClass: "noSwipingClass", noSwipingSelector: "noSwipingSelector", passiveListeners: "passiveListeners", containerModifierClass: "containerModifierClass", slideClass: "slideClass", slideBlankClass: "slideBlankClass", slideActiveClass: "slideActiveClass", slideDuplicateActiveClass: "slideDuplicateActiveClass", slideVisibleClass: "slideVisibleClass", slideDuplicateClass: "slideDuplicateClass", slideNextClass: "slideNextClass", slideDuplicateNextClass: "slideDuplicateNextClass", slidePrevClass: "slidePrevClass", slideDuplicatePrevClass: "slideDuplicatePrevClass", wrapperClass: "wrapperClass", runCallbacksOnInit: "runCallbacksOnInit", observeParents: "observeParents", observeSlideChildren: "observeSlideChildren", a11y: "a11y", autoplay: "autoplay", controller: "controller", coverflowEffect: "coverflowEffect", cubeEffect: "cubeEffect", fadeEffect: "fadeEffect", flipEffect: "flipEffect", creativeEffect: "creativeEffect", cardsEffect: "cardsEffect", hashNavigation: "hashNavigation", history: "history", keyboard: "keyboard", lazy: "lazy", mousewheel: "mousewheel", parallax: "parallax", thumbs: "thumbs", zoom: "zoom", class: "class", id: "id", navigation: "navigation", pagination: "pagination", scrollbar: "scrollbar", virtual: "virtual", index: "index", config: "config" }, outputs: { s__beforeBreakpoint: "_beforeBreakpoint", s__containerClasses: "_containerClasses", s__slideClass: "_slideClass", s__swiper: "_swiper", s_activeIndexChange: "activeIndexChange", s_afterInit: "afterInit", s_autoplay: "autoplay", s_autoplayStart: "autoplayStart", s_autoplayStop: "autoplayStop", s_beforeDestroy: "beforeDestroy", s_beforeInit: "beforeInit", s_beforeLoopFix: "beforeLoopFix", s_beforeResize: "beforeResize", s_beforeSlideChangeStart: "beforeSlideChangeStart", s_beforeTransitionStart: "beforeTransitionStart", s_breakpoint: "breakpoint", s_changeDirection: "changeDirection", s_click: "click", s_doubleTap: "doubleTap", s_doubleClick: "doubleClick", s_destroy: "destroy", s_fromEdge: "fromEdge", s_hashChange: "hashChange", s_hashSet: "hashSet", s_imagesReady: "imagesReady", s_init: "init", s_keyPress: "keyPress", s_lazyImageLoad: "lazyImageLoad", s_lazyImageReady: "lazyImageReady", s_loopFix: "loopFix", s_momentumBounce: "momentumBounce", s_navigationHide: "navigationHide", s_navigationShow: "navigationShow", s_observerUpdate: "observerUpdate", s_orientationchange: "orientationchange", s_paginationHide: "paginationHide", s_paginationRender: "paginationRender", s_paginationShow: "paginationShow", s_paginationUpdate: "paginationUpdate", s_progress: "progress", s_reachBeginning: "reachBeginning", s_reachEnd: "reachEnd", s_realIndexChange: "realIndexChange", s_resize: "resize", s_scroll: "scroll", s_scrollbarDragEnd: "scrollbarDragEnd", s_scrollbarDragMove: "scrollbarDragMove", s_scrollbarDragStart: "scrollbarDragStart", s_setTransition: "setTransition", s_setTranslate: "setTranslate", s_slideChange: "slideChange", s_slideChangeTransitionEnd: "slideChangeTransitionEnd", s_slideChangeTransitionStart: "slideChangeTransitionStart", s_slideNextTransitionEnd: "slideNextTransitionEnd", s_slideNextTransitionStart: "slideNextTransitionStart", s_slidePrevTransitionEnd: "slidePrevTransitionEnd", s_slidePrevTransitionStart: "slidePrevTransitionStart", s_slideResetTransitionStart: "slideResetTransitionStart", s_slideResetTransitionEnd: "slideResetTransitionEnd", s_sliderMove: "sliderMove", s_sliderFirstMove: "sliderFirstMove", s_slidesLengthChange: "slidesLengthChange", s_slidesGridLengthChange: "slidesGridLengthChange", s_snapGridLengthChange: "snapGridLengthChange", s_snapIndexChange: "snapIndexChange", s_tap: "tap", s_toEdge: "toEdge", s_touchEnd: "touchEnd", s_touchMove: "touchMove", s_touchMoveOpposite: "touchMoveOpposite", s_touchStart: "touchStart", s_transitionEnd: "transitionEnd", s_transitionStart: "transitionStart", s_update: "update", s_zoomChange: "zoomChange", s_swiper: "swiper", indexChange: "indexChange" }, host: { properties: { "class": "this.containerClasses" } }, queries: [{ propertyName: "slidesEl", predicate: SwiperSlideDirective }], viewQueries: [{ propertyName: "prevElRef", first: true, predicate: ["prevElRef"], descendants: true }, { propertyName: "nextElRef", first: true, predicate: ["nextElRef"], descendants: true }, { propertyName: "scrollbarElRef", first: true, predicate: ["scrollbarElRef"], descendants: true }, { propertyName: "paginationElRef", first: true, predicate: ["paginationElRef"], descendants: true }], usesOnChanges: true, ngImport: i0__namespace, template: "<ng-content select=\"[slot=container-start]\"></ng-content>\n<ng-container *ngIf=\"navigation && showNavigation\">\n  <div class=\"swiper-button-prev\" #prevElRef></div>\n  <div class=\"swiper-button-next\" #nextElRef></div>\n</ng-container>\n<div *ngIf=\"scrollbar && showScrollbar\" class=\"swiper-scrollbar\" #scrollbarElRef></div>\n<div *ngIf=\"pagination && showPagination\" class=\"swiper-pagination\" #paginationElRef></div>\n<div [ngClass]=\"wrapperClass\" [attr.id]=\"id\">\n  <ng-content select=\"[slot=wrapper-start]\"></ng-content>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: prependSlides,\n        key: 'prepend'\n      }\n    \"\n  ></ng-template>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: activeSlides,\n        key: ''\n      }\n    \"\n  ></ng-template>\n  <ng-template\n    *ngTemplateOutlet=\"\n      slidesTemplate;\n      context: {\n        loopSlides: appendSlides,\n        key: 'append'\n      }\n    \"\n  ></ng-template>\n  <ng-content select=\"[slot=wrapper-end]\"></ng-content>\n</div>\n<ng-content select=\"[slot=container-end]\"></ng-content>\n\n<ng-template #slidesTemplate let-loopSlides=\"loopSlides\" let-slideKey=\"key\">\n  <div\n    *ngFor=\"let slide of loopSlides | async\"\n    [ngClass]=\"\n      (slide.class ? slide.class + ' ' : '') +\n      slideClass +\n      (slideKey !== '' ? ' ' + slideDuplicateClass : '')\n    \"\n    [attr.data-swiper-slide-index]=\"slide.virtualIndex ? slide.virtualIndex : slide.slideIndex\"\n    [attr.data-swiper-autoplay]=\"slide.autoplayDelay\"\n    [style]=\"style\"\n    [ngSwitch]=\"slide.zoom\"\n  >\n    <div *ngSwitchCase=\"true\" [ngClass]=\"zoomContainerClass\">\n      <ng-template\n        [ngTemplateOutlet]=\"slide.template\"\n        [ngTemplateOutletContext]=\"{\n          $implicit: slide.slideData\n        }\"\n      ></ng-template>\n    </div>\n    <ng-container *ngSwitchDefault>\n      <ng-template\n        [ngTemplateOutlet]=\"slide.template\"\n        [ngTemplateOutletContext]=\"{\n          $implicit: slide.slideData\n        }\"\n      ></ng-template>\n    </ng-container>\n  </div>\n</ng-template>\n", styles: ["\n      swiper {\n        display: block;\n      }\n    "], directives: [{ type: i1__namespace.NgIf, selector: "[ngIf]", inputs: ["ngIf", "ngIfThen", "ngIfElse"] }, { type: i1__namespace.NgClass, selector: "[ngClass]", inputs: ["class", "ngClass"] }, { type: i1__namespace.NgTemplateOutlet, selector: "[ngTemplateOutlet]", inputs: ["ngTemplateOutletContext", "ngTemplateOutlet"] }, { type: i1__namespace.NgForOf, selector: "[ngFor][ngForOf]", inputs: ["ngForOf", "ngForTrackBy", "ngForTemplate"] }, { type: i1__namespace.NgSwitch, selector: "[ngSwitch]", inputs: ["ngSwitch"] }, { type: i1__namespace.NgSwitchCase, selector: "[ngSwitchCase]", inputs: ["ngSwitchCase"] }, { type: i1__namespace.NgSwitchDefault, selector: "[ngSwitchDefault]" }], pipes: { "async": i1__namespace.AsyncPipe }, changeDetection: i0__namespace.ChangeDetectionStrategy.OnPush, encapsulation: i0__namespace.ViewEncapsulation.None });
    i0__namespace.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperComponent, decorators: [{
                type: i0.Component,
                args: [{
                        selector: 'swiper, [swiper]',
                        templateUrl: './swiper.component.html',
                        changeDetection: i0.ChangeDetectionStrategy.OnPush,
                        encapsulation: i0.ViewEncapsulation.None,
                        styles: [
                            "\n      swiper {\n        display: block;\n      }\n    ",
                        ],
                    }]
            }], ctorParameters: function () {
            return [{ type: i0__namespace.NgZone }, { type: i0__namespace.ElementRef }, { type: i0__namespace.ChangeDetectorRef }, { type: Object, decorators: [{
                            type: i0.Inject,
                            args: [i0.PLATFORM_ID]
                        }] }];
        }, propDecorators: { enabled: [{
                    type: i0.Input
                }], direction: [{
                    type: i0.Input
                }], touchEventsTarget: [{
                    type: i0.Input
                }], initialSlide: [{
                    type: i0.Input
                }], speed: [{
                    type: i0.Input
                }], cssMode: [{
                    type: i0.Input
                }], updateOnWindowResize: [{
                    type: i0.Input
                }], resizeObserver: [{
                    type: i0.Input
                }], nested: [{
                    type: i0.Input
                }], focusableElements: [{
                    type: i0.Input
                }], width: [{
                    type: i0.Input
                }], height: [{
                    type: i0.Input
                }], preventInteractionOnTransition: [{
                    type: i0.Input
                }], userAgent: [{
                    type: i0.Input
                }], url: [{
                    type: i0.Input
                }], edgeSwipeDetection: [{
                    type: i0.Input
                }], edgeSwipeThreshold: [{
                    type: i0.Input
                }], freeMode: [{
                    type: i0.Input
                }], autoHeight: [{
                    type: i0.Input
                }], setWrapperSize: [{
                    type: i0.Input
                }], virtualTranslate: [{
                    type: i0.Input
                }], effect: [{
                    type: i0.Input
                }], breakpoints: [{
                    type: i0.Input
                }], spaceBetween: [{
                    type: i0.Input
                }], slidesPerView: [{
                    type: i0.Input
                }], grid: [{
                    type: i0.Input
                }], slidesPerGroup: [{
                    type: i0.Input
                }], slidesPerGroupSkip: [{
                    type: i0.Input
                }], centeredSlides: [{
                    type: i0.Input
                }], centeredSlidesBounds: [{
                    type: i0.Input
                }], slidesOffsetBefore: [{
                    type: i0.Input
                }], slidesOffsetAfter: [{
                    type: i0.Input
                }], normalizeSlideIndex: [{
                    type: i0.Input
                }], centerInsufficientSlides: [{
                    type: i0.Input
                }], watchOverflow: [{
                    type: i0.Input
                }], roundLengths: [{
                    type: i0.Input
                }], touchRatio: [{
                    type: i0.Input
                }], touchAngle: [{
                    type: i0.Input
                }], simulateTouch: [{
                    type: i0.Input
                }], shortSwipes: [{
                    type: i0.Input
                }], longSwipes: [{
                    type: i0.Input
                }], longSwipesRatio: [{
                    type: i0.Input
                }], longSwipesMs: [{
                    type: i0.Input
                }], followFinger: [{
                    type: i0.Input
                }], allowTouchMove: [{
                    type: i0.Input
                }], threshold: [{
                    type: i0.Input
                }], touchMoveStopPropagation: [{
                    type: i0.Input
                }], touchStartPreventDefault: [{
                    type: i0.Input
                }], touchStartForcePreventDefault: [{
                    type: i0.Input
                }], touchReleaseOnEdges: [{
                    type: i0.Input
                }], uniqueNavElements: [{
                    type: i0.Input
                }], resistance: [{
                    type: i0.Input
                }], resistanceRatio: [{
                    type: i0.Input
                }], watchSlidesProgress: [{
                    type: i0.Input
                }], grabCursor: [{
                    type: i0.Input
                }], preventClicks: [{
                    type: i0.Input
                }], preventClicksPropagation: [{
                    type: i0.Input
                }], slideToClickedSlide: [{
                    type: i0.Input
                }], preloadImages: [{
                    type: i0.Input
                }], updateOnImagesReady: [{
                    type: i0.Input
                }], loop: [{
                    type: i0.Input
                }], loopAdditionalSlides: [{
                    type: i0.Input
                }], loopedSlides: [{
                    type: i0.Input
                }], loopFillGroupWithBlank: [{
                    type: i0.Input
                }], loopPreventsSlide: [{
                    type: i0.Input
                }], allowSlidePrev: [{
                    type: i0.Input
                }], allowSlideNext: [{
                    type: i0.Input
                }], swipeHandler: [{
                    type: i0.Input
                }], noSwiping: [{
                    type: i0.Input
                }], noSwipingClass: [{
                    type: i0.Input
                }], noSwipingSelector: [{
                    type: i0.Input
                }], passiveListeners: [{
                    type: i0.Input
                }], containerModifierClass: [{
                    type: i0.Input
                }], slideClass: [{
                    type: i0.Input
                }], slideBlankClass: [{
                    type: i0.Input
                }], slideActiveClass: [{
                    type: i0.Input
                }], slideDuplicateActiveClass: [{
                    type: i0.Input
                }], slideVisibleClass: [{
                    type: i0.Input
                }], slideDuplicateClass: [{
                    type: i0.Input
                }], slideNextClass: [{
                    type: i0.Input
                }], slideDuplicateNextClass: [{
                    type: i0.Input
                }], slidePrevClass: [{
                    type: i0.Input
                }], slideDuplicatePrevClass: [{
                    type: i0.Input
                }], wrapperClass: [{
                    type: i0.Input
                }], runCallbacksOnInit: [{
                    type: i0.Input
                }], observeParents: [{
                    type: i0.Input
                }], observeSlideChildren: [{
                    type: i0.Input
                }], a11y: [{
                    type: i0.Input
                }], autoplay: [{
                    type: i0.Input
                }], controller: [{
                    type: i0.Input
                }], coverflowEffect: [{
                    type: i0.Input
                }], cubeEffect: [{
                    type: i0.Input
                }], fadeEffect: [{
                    type: i0.Input
                }], flipEffect: [{
                    type: i0.Input
                }], creativeEffect: [{
                    type: i0.Input
                }], cardsEffect: [{
                    type: i0.Input
                }], hashNavigation: [{
                    type: i0.Input
                }], history: [{
                    type: i0.Input
                }], keyboard: [{
                    type: i0.Input
                }], lazy: [{
                    type: i0.Input
                }], mousewheel: [{
                    type: i0.Input
                }], parallax: [{
                    type: i0.Input
                }], thumbs: [{
                    type: i0.Input
                }], zoom: [{
                    type: i0.Input
                }], class: [{
                    type: i0.Input
                }], id: [{
                    type: i0.Input
                }], navigation: [{
                    type: i0.Input
                }], pagination: [{
                    type: i0.Input
                }], scrollbar: [{
                    type: i0.Input
                }], virtual: [{
                    type: i0.Input
                }], index: [{
                    type: i0.Input
                }], config: [{
                    type: i0.Input
                }], s__beforeBreakpoint: [{
                    type: i0.Output,
                    args: ['_beforeBreakpoint']
                }], s__containerClasses: [{
                    type: i0.Output,
                    args: ['_containerClasses']
                }], s__slideClass: [{
                    type: i0.Output,
                    args: ['_slideClass']
                }], s__swiper: [{
                    type: i0.Output,
                    args: ['_swiper']
                }], s_activeIndexChange: [{
                    type: i0.Output,
                    args: ['activeIndexChange']
                }], s_afterInit: [{
                    type: i0.Output,
                    args: ['afterInit']
                }], s_autoplay: [{
                    type: i0.Output,
                    args: ['autoplay']
                }], s_autoplayStart: [{
                    type: i0.Output,
                    args: ['autoplayStart']
                }], s_autoplayStop: [{
                    type: i0.Output,
                    args: ['autoplayStop']
                }], s_beforeDestroy: [{
                    type: i0.Output,
                    args: ['beforeDestroy']
                }], s_beforeInit: [{
                    type: i0.Output,
                    args: ['beforeInit']
                }], s_beforeLoopFix: [{
                    type: i0.Output,
                    args: ['beforeLoopFix']
                }], s_beforeResize: [{
                    type: i0.Output,
                    args: ['beforeResize']
                }], s_beforeSlideChangeStart: [{
                    type: i0.Output,
                    args: ['beforeSlideChangeStart']
                }], s_beforeTransitionStart: [{
                    type: i0.Output,
                    args: ['beforeTransitionStart']
                }], s_breakpoint: [{
                    type: i0.Output,
                    args: ['breakpoint']
                }], s_changeDirection: [{
                    type: i0.Output,
                    args: ['changeDirection']
                }], s_click: [{
                    type: i0.Output,
                    args: ['click']
                }], s_doubleTap: [{
                    type: i0.Output,
                    args: ['doubleTap']
                }], s_doubleClick: [{
                    type: i0.Output,
                    args: ['doubleClick']
                }], s_destroy: [{
                    type: i0.Output,
                    args: ['destroy']
                }], s_fromEdge: [{
                    type: i0.Output,
                    args: ['fromEdge']
                }], s_hashChange: [{
                    type: i0.Output,
                    args: ['hashChange']
                }], s_hashSet: [{
                    type: i0.Output,
                    args: ['hashSet']
                }], s_imagesReady: [{
                    type: i0.Output,
                    args: ['imagesReady']
                }], s_init: [{
                    type: i0.Output,
                    args: ['init']
                }], s_keyPress: [{
                    type: i0.Output,
                    args: ['keyPress']
                }], s_lazyImageLoad: [{
                    type: i0.Output,
                    args: ['lazyImageLoad']
                }], s_lazyImageReady: [{
                    type: i0.Output,
                    args: ['lazyImageReady']
                }], s_loopFix: [{
                    type: i0.Output,
                    args: ['loopFix']
                }], s_momentumBounce: [{
                    type: i0.Output,
                    args: ['momentumBounce']
                }], s_navigationHide: [{
                    type: i0.Output,
                    args: ['navigationHide']
                }], s_navigationShow: [{
                    type: i0.Output,
                    args: ['navigationShow']
                }], s_observerUpdate: [{
                    type: i0.Output,
                    args: ['observerUpdate']
                }], s_orientationchange: [{
                    type: i0.Output,
                    args: ['orientationchange']
                }], s_paginationHide: [{
                    type: i0.Output,
                    args: ['paginationHide']
                }], s_paginationRender: [{
                    type: i0.Output,
                    args: ['paginationRender']
                }], s_paginationShow: [{
                    type: i0.Output,
                    args: ['paginationShow']
                }], s_paginationUpdate: [{
                    type: i0.Output,
                    args: ['paginationUpdate']
                }], s_progress: [{
                    type: i0.Output,
                    args: ['progress']
                }], s_reachBeginning: [{
                    type: i0.Output,
                    args: ['reachBeginning']
                }], s_reachEnd: [{
                    type: i0.Output,
                    args: ['reachEnd']
                }], s_realIndexChange: [{
                    type: i0.Output,
                    args: ['realIndexChange']
                }], s_resize: [{
                    type: i0.Output,
                    args: ['resize']
                }], s_scroll: [{
                    type: i0.Output,
                    args: ['scroll']
                }], s_scrollbarDragEnd: [{
                    type: i0.Output,
                    args: ['scrollbarDragEnd']
                }], s_scrollbarDragMove: [{
                    type: i0.Output,
                    args: ['scrollbarDragMove']
                }], s_scrollbarDragStart: [{
                    type: i0.Output,
                    args: ['scrollbarDragStart']
                }], s_setTransition: [{
                    type: i0.Output,
                    args: ['setTransition']
                }], s_setTranslate: [{
                    type: i0.Output,
                    args: ['setTranslate']
                }], s_slideChange: [{
                    type: i0.Output,
                    args: ['slideChange']
                }], s_slideChangeTransitionEnd: [{
                    type: i0.Output,
                    args: ['slideChangeTransitionEnd']
                }], s_slideChangeTransitionStart: [{
                    type: i0.Output,
                    args: ['slideChangeTransitionStart']
                }], s_slideNextTransitionEnd: [{
                    type: i0.Output,
                    args: ['slideNextTransitionEnd']
                }], s_slideNextTransitionStart: [{
                    type: i0.Output,
                    args: ['slideNextTransitionStart']
                }], s_slidePrevTransitionEnd: [{
                    type: i0.Output,
                    args: ['slidePrevTransitionEnd']
                }], s_slidePrevTransitionStart: [{
                    type: i0.Output,
                    args: ['slidePrevTransitionStart']
                }], s_slideResetTransitionStart: [{
                    type: i0.Output,
                    args: ['slideResetTransitionStart']
                }], s_slideResetTransitionEnd: [{
                    type: i0.Output,
                    args: ['slideResetTransitionEnd']
                }], s_sliderMove: [{
                    type: i0.Output,
                    args: ['sliderMove']
                }], s_sliderFirstMove: [{
                    type: i0.Output,
                    args: ['sliderFirstMove']
                }], s_slidesLengthChange: [{
                    type: i0.Output,
                    args: ['slidesLengthChange']
                }], s_slidesGridLengthChange: [{
                    type: i0.Output,
                    args: ['slidesGridLengthChange']
                }], s_snapGridLengthChange: [{
                    type: i0.Output,
                    args: ['snapGridLengthChange']
                }], s_snapIndexChange: [{
                    type: i0.Output,
                    args: ['snapIndexChange']
                }], s_tap: [{
                    type: i0.Output,
                    args: ['tap']
                }], s_toEdge: [{
                    type: i0.Output,
                    args: ['toEdge']
                }], s_touchEnd: [{
                    type: i0.Output,
                    args: ['touchEnd']
                }], s_touchMove: [{
                    type: i0.Output,
                    args: ['touchMove']
                }], s_touchMoveOpposite: [{
                    type: i0.Output,
                    args: ['touchMoveOpposite']
                }], s_touchStart: [{
                    type: i0.Output,
                    args: ['touchStart']
                }], s_transitionEnd: [{
                    type: i0.Output,
                    args: ['transitionEnd']
                }], s_transitionStart: [{
                    type: i0.Output,
                    args: ['transitionStart']
                }], s_update: [{
                    type: i0.Output,
                    args: ['update']
                }], s_zoomChange: [{
                    type: i0.Output,
                    args: ['zoomChange']
                }], s_swiper: [{
                    type: i0.Output,
                    args: ['swiper']
                }], indexChange: [{
                    type: i0.Output
                }], prevElRef: [{
                    type: i0.ViewChild,
                    args: ['prevElRef', { static: false }]
                }], nextElRef: [{
                    type: i0.ViewChild,
                    args: ['nextElRef', { static: false }]
                }], scrollbarElRef: [{
                    type: i0.ViewChild,
                    args: ['scrollbarElRef', { static: false }]
                }], paginationElRef: [{
                    type: i0.ViewChild,
                    args: ['paginationElRef', { static: false }]
                }], slidesEl: [{
                    type: i0.ContentChildren,
                    args: [SwiperSlideDirective, { descendants: false, emitDistinctChangesOnly: true }]
                }], containerClasses: [{
                    type: i0.HostBinding,
                    args: ['class']
                }] } });

    var SwiperModule = /** @class */ (function () {
        function SwiperModule() {
        }
        return SwiperModule;
    }());
    SwiperModule.ɵfac = i0__namespace.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperModule, deps: [], target: i0__namespace.ɵɵFactoryTarget.NgModule });
    SwiperModule.ɵmod = i0__namespace.ɵɵngDeclareNgModule({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperModule, declarations: [SwiperComponent, SwiperSlideDirective], imports: [i1.CommonModule], exports: [SwiperComponent, SwiperSlideDirective] });
    SwiperModule.ɵinj = i0__namespace.ɵɵngDeclareInjector({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperModule, imports: [[i1.CommonModule]] });
    i0__namespace.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0__namespace, type: SwiperModule, decorators: [{
                type: i0.NgModule,
                args: [{
                        declarations: [SwiperComponent, SwiperSlideDirective],
                        exports: [SwiperComponent, SwiperSlideDirective],
                        imports: [i1.CommonModule],
                    }]
            }] });

    /*
     * Public API Surface of angular
     */

    /**
     * Generated bundle index. Do not edit.
     */

    exports.SwiperComponent = SwiperComponent;
    exports.SwiperModule = SwiperModule;
    exports.SwiperSlideDirective = SwiperSlideDirective;

    Object.defineProperty(exports, '__esModule', { value: true });

})));
//# sourceMappingURL=swiper_angular.umd.js.map
