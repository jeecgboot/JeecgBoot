import {
  A11yOptions,
  AutoplayOptions,
  ControllerOptions,
  CoverflowEffectOptions,
  CubeEffectOptions,
  FadeEffectOptions,
  FlipEffectOptions,
  CreativeEffectOptions,
  CardsEffectOptions,
  HashNavigationOptions,
  HistoryOptions,
  KeyboardOptions,
  LazyOptions,
  MousewheelOptions,
  NavigationOptions,
  PaginationOptions,
  ParallaxOptions,
  ScrollbarOptions,
  ThumbsOptions,
  VirtualOptions,
  ZoomOptions,
  FreeModeOptions,
  GridOptions,
} from '../types';
import { ComponentOptionsMixin, DefineComponent, PropType } from 'vue';
import { SwiperOptions, Swiper as SwiperClass } from '../types';

declare const Swiper: DefineComponent<
  {
    tag: {
      type: StringConstructor;
      default: string;
    };
    wrapperTag: {
      type: StringConstructor;
      default: string;
    };
    modules: {
      type: ArrayConstructor;
      default: undefined;
    };
    init: {
      type: BooleanConstructor;
      default: undefined;
    };
    direction: {
      type: PropType<SwiperOptions['direction']>;
      default: SwiperOptions['direction'];
    };
    touchEventsTarget: {
      type: PropType<SwiperOptions['touchEventsTarget']>;
      default: undefined;
    };
    initialSlide: {
      type: NumberConstructor;
      default: undefined;
    };
    speed: { type: NumberConstructor; default: undefined };
    cssMode: { type: BooleanConstructor; default: undefined };
    updateOnWindowResize: {
      type: BooleanConstructor;
      default: undefined;
    };
    resizeObserver: {
      type: BooleanConstructor;
      default: undefined;
    };
    nested: { type: BooleanConstructor; default: undefined };
    focusableElements: {
      type: StringConstructor;
      default: undefined;
    };
    width: { type: NumberConstructor; default: undefined };
    height: { type: NumberConstructor; default: undefined };
    preventInteractionOnTransition: {
      type: BooleanConstructor;
      default: undefined;
    };
    userAgent: { type: StringConstructor; default: undefined };
    url: { type: StringConstructor; default: undefined };
    edgeSwipeDetection: {
      type: BooleanConstructor | StringConstructor;
      default: undefined;
    };
    edgeSwipeThreshold: {
      type: NumberConstructor;
      default: undefined;
    };
    autoHeight: {
      type: BooleanConstructor;
      default: undefined;
    };
    setWrapperSize: {
      type: BooleanConstructor;
      default: undefined;
    };
    virtualTranslate: {
      type: BooleanConstructor;
      default: undefined;
    };
    effect: {
      type: PropType<SwiperOptions['effect']>;
      default: undefined;
    };
    breakpoints: {
      type: PropType<SwiperOptions['breakpoints']>;
      default: undefined;
    };
    spaceBetween: {
      type: NumberConstructor;
      default: undefined;
    };
    slidesPerView: {
      type: PropType<SwiperOptions['slidesPerView']>;
      default: undefined;
    };
    slidesPerGroup: {
      type: NumberConstructor;
      default: undefined;
    };
    slidesPerGroupSkip: {
      type: NumberConstructor;
      default: undefined;
    };
    slidesPerGroupAuto: {
      type: BooleanConstructor;
      default: undefined;
    };
    centeredSlides: {
      type: BooleanConstructor;
      default: undefined;
    };
    centeredSlidesBounds: {
      type: BooleanConstructor;
      default: undefined;
    };
    slidesOffsetBefore: {
      type: NumberConstructor;
      default: undefined;
    };
    slidesOffsetAfter: {
      type: NumberConstructor;
      default: undefined;
    };
    normalizeSlideIndex: {
      type: BooleanConstructor;
      default: undefined;
    };
    centerInsufficientSlides: {
      type: BooleanConstructor;
      default: undefined;
    };
    watchOverflow: {
      type: BooleanConstructor;
      default: undefined;
    };
    roundLengths: {
      type: BooleanConstructor;
      default: undefined;
    };
    touchRatio: {
      type: NumberConstructor;
      default: undefined;
    };
    touchAngle: {
      type: NumberConstructor;
      default: undefined;
    };
    simulateTouch: {
      type: BooleanConstructor;
      default: undefined;
    };
    shortSwipes: {
      type: BooleanConstructor;
      default: undefined;
    };
    longSwipes: {
      type: BooleanConstructor;
      default: undefined;
    };
    longSwipesRatio: {
      type: NumberConstructor;
      default: undefined;
    };
    longSwipesMs: {
      type: NumberConstructor;
      default: undefined;
    };
    followFinger: {
      type: BooleanConstructor;
      default: undefined;
    };
    allowTouchMove: {
      type: BooleanConstructor;
      default: undefined;
    };
    threshold: { type: NumberConstructor; default: undefined };
    touchMoveStopPropagation: {
      type: BooleanConstructor;
      default: undefined;
    };
    touchStartPreventDefault: {
      type: BooleanConstructor;
      default: undefined;
    };
    touchStartForcePreventDefault: {
      type: BooleanConstructor;
      default: undefined;
    };
    touchReleaseOnEdges: {
      type: BooleanConstructor;
      default: undefined;
    };
    uniqueNavElements: {
      type: BooleanConstructor;
      default: undefined;
    };
    resistance: {
      type: BooleanConstructor;
      default: undefined;
    };
    resistanceRatio: {
      type: NumberConstructor;
      default: undefined;
    };
    watchSlidesProgress: {
      type: BooleanConstructor;
      default: undefined;
    };
    grabCursor: {
      type: BooleanConstructor;
      default: undefined;
    };
    preventClicks: {
      type: BooleanConstructor;
      default: undefined;
    };
    preventClicksPropagation: {
      type: BooleanConstructor;
      default: undefined;
    };
    slideToClickedSlide: {
      type: BooleanConstructor;
      default: undefined;
    };
    preloadImages: {
      type: BooleanConstructor;
      default: undefined;
    };
    updateOnImagesReady: {
      type: BooleanConstructor;
      default: undefined;
    };
    loop: { type: BooleanConstructor; default: undefined };
    loopAdditionalSlides: {
      type: NumberConstructor;
      default: undefined;
    };
    loopedSlides: {
      type: NumberConstructor;
      default: undefined;
    };
    loopFillGroupWithBlank: {
      type: BooleanConstructor;
      default: undefined;
    };
    loopPreventsSlide: {
      type: BooleanConstructor;
      default: undefined;
    };
    allowSlidePrev: {
      type: BooleanConstructor;
      default: undefined;
    };
    allowSlideNext: {
      type: BooleanConstructor;
      default: undefined;
    };
    swipeHandler: {
      type: BooleanConstructor;
      default: undefined;
    };
    noSwiping: {
      type: BooleanConstructor;
      default: undefined;
    };
    noSwipingClass: {
      type: StringConstructor;
      default: undefined;
    };
    noSwipingSelector: {
      type: StringConstructor;
      default: undefined;
    };
    passiveListeners: {
      type: BooleanConstructor;
      default: undefined;
    };
    containerModifierClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideBlankClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideActiveClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideDuplicateActiveClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideVisibleClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideDuplicateClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideNextClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideDuplicateNextClass: {
      type: StringConstructor;
      default: undefined;
    };
    slidePrevClass: {
      type: StringConstructor;
      default: undefined;
    };
    slideDuplicatePrevClass: {
      type: StringConstructor;
      default: undefined;
    };
    wrapperClass: {
      type: StringConstructor;
      default: undefined;
    };
    runCallbacksOnInit: {
      type: BooleanConstructor;
      default: undefined;
    };
    observer: { type: BooleanConstructor; default: undefined };
    observeParents: {
      type: BooleanConstructor;
      default: undefined;
    };
    observeSlideChildren: {
      type: BooleanConstructor;
      default: undefined;
    };
    a11y: {
      type: PropType<A11yOptions | boolean>;
      default: undefined;
    };
    autoplay: {
      type: PropType<AutoplayOptions | boolean>;
      default: undefined;
    };
    controller: {
      type: PropType<ControllerOptions>;
      default: undefined;
    };
    coverflowEffect: {
      type: PropType<CoverflowEffectOptions>;
      default: undefined;
    };
    cubeEffect: {
      type: PropType<CubeEffectOptions>;
      default: undefined;
    };
    fadeEffect: {
      type: PropType<FadeEffectOptions>;
      default: undefined;
    };
    flipEffect: {
      type: PropType<FlipEffectOptions>;
      default: undefined;
    };
    creativeEffect: {
      type: PropType<CreativeEffectOptions>;
      default: undefined;
    };
    cardsEffect: {
      type: PropType<CardsEffectOptions>;
      default: undefined;
    };
    hashNavigation: {
      type: PropType<HashNavigationOptions | boolean>;
      default: undefined;
    };
    history: {
      type: PropType<HistoryOptions | boolean>;
      default: undefined;
    };
    keyboard: {
      type: PropType<KeyboardOptions | boolean>;
      default: undefined;
    };
    lazy: {
      type: PropType<LazyOptions | boolean>;
      default: undefined;
    };
    mousewheel: {
      type: PropType<MousewheelOptions | boolean>;
      default: undefined;
    };
    navigation: {
      type: PropType<NavigationOptions | boolean>;
      default: undefined;
    };
    pagination: {
      type: PropType<PaginationOptions | boolean>;
      default: undefined;
    };
    parallax: {
      type: PropType<ParallaxOptions | boolean>;
      default: undefined;
    };
    scrollbar: {
      type: PropType<ScrollbarOptions | boolean>;
      default: undefined;
    };
    thumbs: { type: PropType<ThumbsOptions>; default: undefined };
    virtual: {
      type: PropType<VirtualOptions | boolean>;
      default: undefined;
    };
    zoom: {
      type: PropType<ZoomOptions | boolean>;
      default: undefined;
    };
    freeMode: {
      type: PropType<FreeModeOptions | boolean>;
      default: undefined;
    };
    grid: {
      type: PropType<GridOptions>;
      default: undefined;
    };
  },
  () => JSX.Element,
  unknown,
  {},
  {},
  ComponentOptionsMixin,
  ComponentOptionsMixin,
  {
    swiper: (swiper: SwiperClass) => void;
    /**
   * Event will be fired in when autoplay started
   */
  autoplayStart: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when autoplay stopped
   */
  autoplayStop: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when slide changed with autoplay
   */
  autoplay: (swiper: SwiperClass) => void;/**
   * Event will be fired on window hash change
   */
  hashChange: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper updates the hash
   */
  hashSet: (swiper: SwiperClass) => void;/**
   * Event will be fired on mousewheel scroll
   */
  scroll: (swiper: SwiperClass, event: WheelEvent) => void;/**
   * Event will be fired in the beginning of lazy loading of image
   */
  lazyImageLoad: (swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement) => void;
  /**
   * Event will be fired when lazy loading image will be loaded
   */
  lazyImageReady: (swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement) => void;/**
   * Event will be fired on key press
   */
  keyPress: (swiper: SwiperClass, keyCode: string) => void;/**
   * Event will be fired on navigation hide
   */
  navigationHide: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on navigation show
   */
  navigationShow: (swiper: SwiperClass) => void;/**
   * Event will be fired on draggable scrollbar drag start
   */
  scrollbarDragStart: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired on draggable scrollbar drag move
   */
  scrollbarDragMove: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired on draggable scrollbar drag end
   */
  scrollbarDragEnd: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;/**
   * Event will be fired after pagination rendered
   */
  paginationRender: (swiper: SwiperClass, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired when pagination updated
   */
  paginationUpdate: (swiper: SwiperClass, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired on pagination hide
   */
  paginationHide: (swiper: SwiperClass) => void;

  /**
   * Event will be fired on pagination show
   */
  paginationShow: (swiper: SwiperClass) => void;/**
   * Event will be fired on zoom change
   */
  zoomChange: (swiper: SwiperClass, scale: number, imageEl: HTMLElement, slideEl: HTMLElement) => void;
    
  /**
   * Fired right after Swiper initialization.
   * @note Note that with `swiper.on('init')` syntax it will
   * work only in case you set `init: false` parameter.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   init: false,
   *   // other parameters
   * });
   * swiper.on('init', function() {
   *  // do something
   * });
   * // init Swiper
   * swiper.init();
   * ```
   *
   * @example
   * ```js
   * // Otherwise use it as the parameter:
   * const swiper = new Swiper('.swiper', {
   *   // other parameters
   *   on: {
   *     init: function () {
   *       // do something
   *     },
   *   }
   * });
   * ```
   */
  init: (swiper: SwiperClass) => any;

  /**
   * Event will be fired right before Swiper destroyed
   */
  beforeDestroy: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when currently active slide is changed
   */
  slideChange: (swiper: SwiperClass) => void;

  /**
   * Event will be fired in the beginning of animation to other slide (next or previous).
   */
  slideChangeTransitionStart: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after animation to other slide (next or previous).
   */
  slideChangeTransitionEnd: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionStart" but for "forward" direction only
   */
  slideNextTransitionStart: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionEnd" but for "forward" direction only
   */
  slideNextTransitionEnd: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionStart" but for "backward" direction only
   */
  slidePrevTransitionStart: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionEnd" but for "backward" direction only
   */
  slidePrevTransitionEnd: (swiper: SwiperClass) => void;

  /**
   * Event will be fired in the beginning of transition.
   */
  transitionStart: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after transition.
   */
  transitionEnd: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when user touch Swiper. Receives `touchstart` event as an arguments.
   */
  touchStart: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper. Receives `touchmove` event as an arguments.
   */
  touchMove: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper in direction opposite to direction parameter. Receives `touchmove` event as an arguments.
   */
  touchMoveOpposite: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper and move it. Receives `touchmove` event as an arguments.
   */
  sliderMove: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user release Swiper. Receives `touchend` event as an arguments.
   */
  touchEnd: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  click: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  tap: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user double tap on Swiper's container. Receives `touchend` event as an arguments
   */
  doubleTap: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired right after all inner images are loaded. updateOnImagesReady should be also enabled
   */
  imagesReady: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper progress is changed, as an arguments it receives progress that is always from 0 to 1
   */
  progress: (swiper: SwiperClass, progress: number) => void;

  /**
   * Event will be fired when Swiper reach its beginning (initial position)
   */
  reachBeginning: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper reach last slide
   */
  reachEnd: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper goes to beginning or end position
   */
  toEdge: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper goes from beginning or end position
   */
  fromEdge: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when swiper's wrapper change its position. Receives current translate value as an arguments
   */
  setTranslate: (swiper: SwiperClass, translate: number) => void;

  /**
   * Event will be fired everytime when swiper starts animation. Receives current transition duration (in ms) as an arguments
   */
  setTransition: (swiper: SwiperClass, transition: number) => void;

  /**
   * Event will be fired on window resize right before swiper's onresize manipulation
   */
  resize: (swiper: SwiperClass) => void;

  /**
   * Event will be fired if observer is enabled and it detects DOM mutations
   */
  observerUpdate: (swiper: SwiperClass) => void;

  /**
   * Event will be fired right before "loop fix"
   */
  beforeLoopFix: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after "loop fix"
   */
  loopFix: (swiper: SwiperClass) => void;

  /**
   * Event will be fired on breakpoint change
   */
  breakpoint: (swiper: SwiperClass, breakpointParams: SwiperOptions) => void;

  /**
   * !INTERNAL: Event will fired right before breakpoint change
   */
  _beforeBreakpoint: (swiper: SwiperClass, breakpointParams: SwiperOptions) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper container element
   */
  _containerClasses: (swiper: SwiperClass, classNames: string) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper slide element
   */
  _slideClass: (swiper: SwiperClass, slideEl: HTMLElement, classNames: string) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on all swiper slides
   */
  _slideClasses: (
    swiper: SwiperClass,
    slides: { slideEl: HTMLElement; classNames: string; index: number }[],
  ) => void;

  /**
   * !INTERNAL: Event will fired as soon as swiper instance available (before init)
   */
  _swiper: (swiper: SwiperClass) => void;

  /**
   * !INTERNAL: Event will be fired on free mode touch end (release) and there will no be momentum
   */
  _freeModeNoMomentumRelease: (swiper: SwiperClass) => void;

  /**
   * Event will fired on active index change
   */
  activeIndexChange: (swiper: SwiperClass) => void;
  /**
   * Event will fired on snap index change
   */
  snapIndexChange: (swiper: SwiperClass) => void;
  /**
   * Event will fired on real index change
   */
  realIndexChange: (swiper: SwiperClass) => void;
  /**
   * Event will fired right after initialization
   */
  afterInit: (swiper: SwiperClass) => void;
  /**
   * Event will fired right before initialization
   */
  beforeInit: (swiper: SwiperClass) => void;
  /**
   * Event will fired before resize handler
   */
  beforeResize: (swiper: SwiperClass) => void;
  /**
   * Event will fired before slide change transition start
   */
  beforeSlideChangeStart: (swiper: SwiperClass) => void;
  /**
   * Event will fired before transition start
   */
  beforeTransitionStart: (swiper: SwiperClass, speed: number, internal: any) => void; // what is internal?
  /**
   * Event will fired on direction change
   */
  changeDirection: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when user double click/tap on Swiper
   */
  doubleClick: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;
  /**
   * Event will be fired on swiper destroy
   */
  destroy: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on momentum bounce
   */
  momentumBounce: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on orientation change (e.g. landscape -> portrait)
   */
  orientationchange: (swiper: SwiperClass) => void;
  /**
   * Event will be fired in the beginning of animation of resetting slide to current one
   */
  slideResetTransitionStart: (swiper: SwiperClass) => void;
  /**
   * Event will be fired in the end of animation of resetting slide to current one
   */
  slideResetTransitionEnd: (swiper: SwiperClass) => void;
  /**
   * Event will be fired with first touch/drag move
   */
  sliderFirstMove: (swiper: SwiperClass, event: TouchEvent) => void;
  /**
   * Event will be fired when number of slides has changed
   */
  slidesLengthChange: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when slides grid has changed
   */
  slidesGridLengthChange: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when snap grid has changed
   */
  snapGridLengthChange: (swiper: SwiperClass) => void;
  /**
   * Event will be fired after swiper.update() call
   */
  update: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper is locked (when `watchOverflow` enabled)
   */
  lock: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper is unlocked (when `watchOverflow` enabled)
   */
  unlock: (swiper: SwiperClass) => void;
  
  }
>;

declare const SwiperSlide: DefineComponent<{
  tag: {
    type: StringConstructor;
    default: string;
  };
  swiperRef: { type: PropType<SwiperClass>; required: false };
  zoom: { type: BooleanConstructor; default: undefined };
  virtualIndex: {
    type: StringConstructor | NumberConstructor;
    default: undefined;
  };
}>;

export { Swiper, SwiperSlide };
