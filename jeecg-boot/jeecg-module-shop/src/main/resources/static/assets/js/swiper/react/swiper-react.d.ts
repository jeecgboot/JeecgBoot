import * as React from 'react';

import { SwiperOptions, Swiper as SwiperClass } from '../types/';

interface Swiper extends SwiperOptions {
  /**
   * Swiper container tag
   *
   * @default 'div'
   */
  tag?: string;

  /**
   * Swiper wrapper tag
   *
   * @default 'div'
   */
  wrapperTag?: string;

  /**
   * Get Swiper instance
   */
  onSwiper?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired in when autoplay started
   */
  onAutoplayStart?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when autoplay stopped
   */
  onAutoplayStop?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when slide changed with autoplay
   */
  onAutoplay?: (swiper: SwiperClass) => void;/**
   * Event will be fired on window hash change
   */
  onHashChange?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper updates the hash
   */
  onHashSet?: (swiper: SwiperClass) => void;/**
   * Event will be fired on mousewheel scroll
   */
  onScroll?: (swiper: SwiperClass, event: WheelEvent) => void;/**
   * Event will be fired in the beginning of lazy loading of image
   */
  onLazyImageLoad?: (swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement) => void;
  /**
   * Event will be fired when lazy loading image will be loaded
   */
  onLazyImageReady?: (swiper: SwiperClass, slideEl: HTMLElement, imageEl: HTMLElement) => void;/**
   * Event will be fired on key press
   */
  onKeyPress?: (swiper: SwiperClass, keyCode: string) => void;/**
   * Event will be fired on navigation hide
   */
  onNavigationHide?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on navigation show
   */
  onNavigationShow?: (swiper: SwiperClass) => void;/**
   * Event will be fired on draggable scrollbar drag start
   */
  onScrollbarDragStart?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired on draggable scrollbar drag move
   */
  onScrollbarDragMove?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired on draggable scrollbar drag end
   */
  onScrollbarDragEnd?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;/**
   * Event will be fired after pagination rendered
   */
  onPaginationRender?: (swiper: SwiperClass, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired when pagination updated
   */
  onPaginationUpdate?: (swiper: SwiperClass, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired on pagination hide
   */
  onPaginationHide?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired on pagination show
   */
  onPaginationShow?: (swiper: SwiperClass) => void;/**
   * Event will be fired on zoom change
   */
  onZoomChange?: (swiper: SwiperClass, scale: number, imageEl: HTMLElement, slideEl: HTMLElement) => void;
  
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
  onInit?: (swiper: SwiperClass) => any;

  /**
   * Event will be fired right before Swiper destroyed
   */
  onBeforeDestroy?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when currently active slide is changed
   */
  onSlideChange?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired in the beginning of animation to other slide (next or previous).
   */
  onSlideChangeTransitionStart?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after animation to other slide (next or previous).
   */
  onSlideChangeTransitionEnd?: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionStart" but for "forward" direction only
   */
  onSlideNextTransitionStart?: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionEnd" but for "forward" direction only
   */
  onSlideNextTransitionEnd?: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionStart" but for "backward" direction only
   */
  onSlidePrevTransitionStart?: (swiper: SwiperClass) => void;

  /**
   * Same as "slideChangeTransitionEnd" but for "backward" direction only
   */
  onSlidePrevTransitionEnd?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired in the beginning of transition.
   */
  onTransitionStart?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after transition.
   */
  onTransitionEnd?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when user touch Swiper. Receives `touchstart` event as an arguments.
   */
  onTouchStart?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper. Receives `touchmove` event as an arguments.
   */
  onTouchMove?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper in direction opposite to direction parameter. Receives `touchmove` event as an arguments.
   */
  onTouchMoveOpposite?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user touch and move finger over Swiper and move it. Receives `touchmove` event as an arguments.
   */
  onSliderMove?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user release Swiper. Receives `touchend` event as an arguments.
   */
  onTouchEnd?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  onClick?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user click/tap on Swiper. Receives `touchend` event as an arguments.
   */
  onTap?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired when user double tap on Swiper's container. Receives `touchend` event as an arguments
   */
  onDoubleTap?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;

  /**
   * Event will be fired right after all inner images are loaded. updateOnImagesReady should be also enabled
   */
  onImagesReady?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper progress is changed, as an arguments it receives progress that is always from 0 to 1
   */
  onProgress?: (swiper: SwiperClass, progress: number) => void;

  /**
   * Event will be fired when Swiper reach its beginning (initial position)
   */
  onReachBeginning?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper reach last slide
   */
  onReachEnd?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper goes to beginning or end position
   */
  onToEdge?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when Swiper goes from beginning or end position
   */
  onFromEdge?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired when swiper's wrapper change its position. Receives current translate value as an arguments
   */
  onSetTranslate?: (swiper: SwiperClass, translate: number) => void;

  /**
   * Event will be fired everytime when swiper starts animation. Receives current transition duration (in ms) as an arguments
   */
  onSetTransition?: (swiper: SwiperClass, transition: number) => void;

  /**
   * Event will be fired on window resize right before swiper's onresize manipulation
   */
  onResize?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired if observer is enabled and it detects DOM mutations
   */
  onObserverUpdate?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired right before "loop fix"
   */
  onBeforeLoopFix?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired after "loop fix"
   */
  onLoopFix?: (swiper: SwiperClass) => void;

  /**
   * Event will be fired on breakpoint change
   */
  onBreakpoint?: (swiper: SwiperClass, breakpointParams: SwiperOptions) => void;

  /**
   * !INTERNAL: Event will fired right before breakpoint change
   */
  _beforeBreakpoint?: (swiper: SwiperClass, breakpointParams: SwiperOptions) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper container element
   */
  _containerClasses?: (swiper: SwiperClass, classNames: string) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on swiper slide element
   */
  _slideClass?: (swiper: SwiperClass, slideEl: HTMLElement, classNames: string) => void;

  /**
   * !INTERNAL: Event will fired after setting CSS classes on all swiper slides
   */
  _slideClasses?: (
    swiper: SwiperClass,
    slides: { slideEl: HTMLElement; classNames: string; index: number }[],
  ) => void;

  /**
   * !INTERNAL: Event will fired as soon as swiper instance available (before init)
   */
  _swiper?: (swiper: SwiperClass) => void;

  /**
   * !INTERNAL: Event will be fired on free mode touch end (release) and there will no be momentum
   */
  _freeModeNoMomentumRelease?: (swiper: SwiperClass) => void;

  /**
   * Event will fired on active index change
   */
  onActiveIndexChange?: (swiper: SwiperClass) => void;
  /**
   * Event will fired on snap index change
   */
  onSnapIndexChange?: (swiper: SwiperClass) => void;
  /**
   * Event will fired on real index change
   */
  onRealIndexChange?: (swiper: SwiperClass) => void;
  /**
   * Event will fired right after initialization
   */
  onAfterInit?: (swiper: SwiperClass) => void;
  /**
   * Event will fired right before initialization
   */
  onBeforeInit?: (swiper: SwiperClass) => void;
  /**
   * Event will fired before resize handler
   */
  onBeforeResize?: (swiper: SwiperClass) => void;
  /**
   * Event will fired before slide change transition start
   */
  onBeforeSlideChangeStart?: (swiper: SwiperClass) => void;
  /**
   * Event will fired before transition start
   */
  onBeforeTransitionStart?: (swiper: SwiperClass, speed: number, internal: any) => void; // what is internal?
  /**
   * Event will fired on direction change
   */
  onChangeDirection?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when user double click/tap on Swiper
   */
  onDoubleClick?: (swiper: SwiperClass, event: MouseEvent | TouchEvent | PointerEvent) => void;
  /**
   * Event will be fired on swiper destroy
   */
  onDestroy?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on momentum bounce
   */
  onMomentumBounce?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired on orientation change (e.g. landscape -> portrait)
   */
  onOrientationchange?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired in the beginning of animation of resetting slide to current one
   */
  onSlideResetTransitionStart?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired in the end of animation of resetting slide to current one
   */
  onSlideResetTransitionEnd?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired with first touch/drag move
   */
  onSliderFirstMove?: (swiper: SwiperClass, event: TouchEvent) => void;
  /**
   * Event will be fired when number of slides has changed
   */
  onSlidesLengthChange?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when slides grid has changed
   */
  onSlidesGridLengthChange?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when snap grid has changed
   */
  onSnapGridLengthChange?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired after swiper.update() call
   */
  onUpdate?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper is locked (when `watchOverflow` enabled)
   */
  onLock?: (swiper: SwiperClass) => void;
  /**
   * Event will be fired when swiper is unlocked (when `watchOverflow` enabled)
   */
  onUnlock?: (swiper: SwiperClass) => void;
  
}

interface SlideData {
  isActive: boolean;
  isVisible: boolean;
  isDuplicate: boolean;
  isPrev: boolean;
  isNext: boolean;
}

interface SwiperSlide {
  /**
   * Slide tag
   *
   * @default 'div'
   */
  tag?: string;

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

  /**
   * Slide's child element or render function
   *
   * @default undefined
   */
  children?: React.ReactNode | ((slideData: SlideData) => React.ReactNode);
}

interface Swiper
  extends Omit<
    React.HTMLAttributes<HTMLElement>,
    | 'onProgress'
    | 'onClick'
    | 'onTouchEnd'
    | 'onTouchMove'
    | 'onTouchStart'
    | 'onTransitionEnd'
    | 'onKeyPress'
    | 'onDoubleClick'
    | 'onScroll'
  > {}
interface SwiperSlide extends React.HTMLAttributes<HTMLElement> {}

declare const Swiper: React.FunctionComponent<Swiper>;
declare const SwiperSlide: React.VoidFunctionComponent<SwiperSlide>;

export { Swiper, SwiperSlide };
