import { SvelteComponentTyped } from 'svelte';
import { SwiperOptions, Swiper as SwiperClass } from '../types/';

// @ts-ignore
interface SwiperProps extends svelte.JSX.HTMLAttributes<HTMLElementTagNameMap['div']> {}
interface SwiperProps extends SwiperOptions {}

// @ts-ignore
interface SwiperSlideProps extends svelte.JSX.HTMLAttributes<HTMLElementTagNameMap['div']> {
  /**
   * Enables additional wrapper required for zoom mode
   *
   * @default false
   */
  zoom?: boolean;

  /**
   * Slide's index in slides array/collection
   *
   * @default false
   */
  virtualIndex?: number;
}

declare class Swiper extends SvelteComponentTyped<
  SwiperProps,
  {
    swiper: CustomEvent<void>;
    /**
   * Event will be fired in when autoplay started
   */
  autoplayStart: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when autoplay stopped
   */
  autoplayStop: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when slide changed with autoplay
   */
  autoplay: CustomEvent<[swiper: SwiperClass]>;/**
   * Event will be fired on window hash change
   */
  hashChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when swiper updates the hash
   */
  hashSet: CustomEvent<[swiper: SwiperClass]>;/**
   * Event will be fired on mousewheel scroll
   */
  scroll: CustomEvent<[swiper: SwiperClass, event: WheelEvent]>;/**
   * Event will be fired in the beginning of lazy loading of image
   */
  lazyImageLoad: CustomEvent<[swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement]>;
  /**
   * Event will be fired when lazy loading image will be loaded
   */
  lazyImageReady: CustomEvent<[swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement]>;/**
   * Event will be fired on key press
   */
  keyPress: CustomEvent<[swiper: SwiperClass, keyCode: string]>;/**
   * Event will be fired on navigation hide
   */
  navigationHide: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired on navigation show
   */
  navigationShow: CustomEvent<[swiper: SwiperClass]>;/**
   * Event will be fired on draggable scrollbar drag start
   */
  scrollbarDragStart: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired on draggable scrollbar drag move
   */
  scrollbarDragMove: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired on draggable scrollbar drag end
   */
  scrollbarDragEnd: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;/**
   * Event will be fired after pagination rendered
   */
  paginationRender: CustomEvent<[swiper: SwiperClass, paginationEl: HTMLElement]>;

  /**
   * Event will be fired when pagination updated
   */
  paginationUpdate: CustomEvent<[swiper: SwiperClass, paginationEl: HTMLElement]>;

  /**
   * Event will be fired on pagination hide
   */
  paginationHide: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired on pagination show
   */
  paginationShow: CustomEvent<[swiper: SwiperClass]>;/**
   * Event will be fired on zoom change
   */
  zoomChange: CustomEvent<[swiper: SwiperClass, scale: number, imageEl: HTMLElement, slideEl: HTMLElement]>;
    
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
  init: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired right before Swiper destroyed
   */
  beforeDestroy: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when currently active slide is changed
   */
  slideChange: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired in the beginning of animation to other slide (next or previous).
   */
  slideChangeTransitionStart: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired after animation to other slide (next or previous).
   */
  slideChangeTransitionEnd: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Same as "slideChangeTransitionStart" but for "forward" direction only
   */
  slideNextTransitionStart: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Same as "slideChangeTransitionEnd" but for "forward" direction only
   */
  slideNextTransitionEnd: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Same as "slideChangeTransitionStart" but for "backward" direction only
   */
  slidePrevTransitionStart: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Same as "slideChangeTransitionEnd" but for "backward" direction only
   */
  slidePrevTransitionEnd: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired in the beginning of transition.
   */
  transitionStart: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired after transition.
   */
  transitionEnd: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when user touch Swiper. Receives `touchstart` event as an arguments.
   */
  touchStart: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user touch and move finger over Swiper. Receives `touchmove` event as an arguments.
   */
  touchMove: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user touch and move finger over Swiper in direction opposite to direction parameter. Receives `touchmove` event as an arguments.
   */
  touchMoveOpposite: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user touch and move finger over Swiper and move it. Receives `touchmove` event as an arguments.
   */
  sliderMove: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user release Swiper. Receives `touchend` event as an arguments.
   */
  touchEnd: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  click: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  tap: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired when user double tap on Swiper's container. Receives `touchend` event as an arguments
   */
  doubleTap: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;

  /**
   * Event will be fired right after all inner images are loaded. updateOnImagesReady should be also enabled
   */
  imagesReady: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when Swiper progress is changed, as an arguments it receives progress that is always from 0 to 1
   */
  progress: CustomEvent<[swiper: SwiperClass, progress: number]>;

  /**
   * Event will be fired when Swiper reach its beginning (initial position)
   */
  reachBeginning: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when Swiper reach last slide
   */
  reachEnd: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when Swiper goes to beginning or end position
   */
  toEdge: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when Swiper goes from beginning or end position
   */
  fromEdge: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired when swiper's wrapper change its position. Receives current translate value as an arguments
   */
  setTranslate: CustomEvent<[swiper: SwiperClass, translate: number]>;

  /**
   * Event will be fired everytime when swiper starts animation. Receives current transition duration (in ms) as an arguments
   */
  setTransition: CustomEvent<[swiper: SwiperClass, transition: number]>;

  /**
   * Event will be fired on window resize right before swiper's onresize manipulation
   */
  resize: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired if observer is enabled and it detects DOM mutations
   */
  observerUpdate: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired right before "loop fix"
   */
  beforeLoopFix: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired after "loop fix"
   */
  loopFix: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will be fired on breakpoint change
   */
  breakpoint: CustomEvent<[swiper: SwiperClass, breakpointParams: SwiperOptions]>;

  /**
   * !INTERNAL: Event will fired right before breakpoint change
   */
  _beforeBreakpoint: CustomEvent<[swiper: SwiperClass, breakpointParams: SwiperOptions]>;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper container element
   */
  _containerClasses: CustomEvent<[swiper: SwiperClass, classNames: string]>;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper slide element
   */
  _slideClass: CustomEvent<[swiper: SwiperClass, slideEl: HTMLElement, classNames: string]>;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on all swiper slides
   */
  _slideClasses: CustomEvent<[
    swiper: SwiperClass,
    slides: { slideEl: HTMLElement; classNames: string; index: number }[],
  ]>;

  /**
   * !INTERNAL: Event will fired as soon as swiper instance available (before init)
   */
  _swiper: CustomEvent<[swiper: SwiperClass]>;

  /**
   * !INTERNAL: Event will be fired on free mode touch end (release) and there will no be momentum
   */
  _freeModeNoMomentumRelease: CustomEvent<[swiper: SwiperClass]>;

  /**
   * Event will fired on active index change
   */
  activeIndexChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired on snap index change
   */
  snapIndexChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired on real index change
   */
  realIndexChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired right after initialization
   */
  afterInit: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired right before initialization
   */
  beforeInit: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired before resize handler
   */
  beforeResize: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired before slide change transition start
   */
  beforeSlideChangeStart: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will fired before transition start
   */
  beforeTransitionStart: CustomEvent<[swiper: SwiperClass, speed: number, internal: any]>; // what is internal?
  /**
   * Event will fired on direction change
   */
  changeDirection: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when user double click/tap on Swiper
   */
  doubleClick: CustomEvent<[swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent]>;
  /**
   * Event will be fired on swiper destroy
   */
  destroy: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired on momentum bounce
   */
  momentumBounce: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired on orientation change (e.g. landscape -> portrait)
   */
  orientationchange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired in the beginning of animation of resetting slide to current one
   */
  slideResetTransitionStart: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired in the end of animation of resetting slide to current one
   */
  slideResetTransitionEnd: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired with first touch/drag move
   */
  sliderFirstMove: CustomEvent<[swiper: SwiperClass, event: TouchEvent]>;
  /**
   * Event will be fired when number of slides has changed
   */
  slidesLengthChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when slides grid has changed
   */
  slidesGridLengthChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when snap grid has changed
   */
  snapGridLengthChange: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired after swiper.update() call
   */
  update: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when swiper is locked (when `watchOverflow` enabled)
   */
  lock: CustomEvent<[swiper: SwiperClass]>;
  /**
   * Event will be fired when swiper is unlocked (when `watchOverflow` enabled)
   */
  unlock: CustomEvent<[swiper: SwiperClass]>;
  
  },
  {
    default: {};
    'container-start': {};
    'wrapper-start': {};
    'wrapper-end': {};
    'container-end': {};
  }
> {}

declare class SwiperSlide extends SvelteComponentTyped<
  SwiperSlideProps,
  {},
  {
    default: {};
  }
> {}

export { Swiper, SwiperSlide };
