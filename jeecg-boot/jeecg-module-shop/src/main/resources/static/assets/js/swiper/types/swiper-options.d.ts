import { A11yOptions } from './modules/a11y';
import { AutoplayOptions } from './modules/autoplay';
import { ControllerOptions } from './modules/controller';
import { CoverflowEffectOptions } from './modules/effect-coverflow';
import { CubeEffectOptions } from './modules/effect-cube';
import { FadeEffectOptions } from './modules/effect-fade';
import { FlipEffectOptions } from './modules/effect-flip';
import { CreativeEffectOptions } from './modules/effect-creative';
import { CardsEffectOptions } from './modules/effect-cards';
import { HashNavigationOptions } from './modules/hash-navigation';
import { HistoryOptions } from './modules/history';
import { KeyboardOptions } from './modules/keyboard';
import { LazyOptions } from './modules/lazy';
import { MousewheelOptions } from './modules/mousewheel';
import { NavigationOptions } from './modules/navigation';
import { PaginationOptions } from './modules/pagination';
import { ParallaxOptions } from './modules/parallax';
import { ScrollbarOptions } from './modules/scrollbar';
import { ThumbsOptions } from './modules/thumbs';
import { VirtualOptions } from './modules/virtual';
import { ZoomOptions } from './modules/zoom';
import { FreeModeOptions } from './modules/free-mode';
import { GridOptions } from './modules/grid';

import { CSSSelector, SwiperModule } from './shared';
import { SwiperEvents } from './swiper-events';

export interface SwiperOptions {
  /**
   * Array with Swiper modules
   *
   * @example
   * ```js
   * import Swiper, { Navigation, Pagination } from 'swiper';
   *
   * const swiper = new Swiper('.swiper', {
   *    modules: [ Navigation, Pagination ],
   *  });
   * ```
   */
  modules?: SwiperModule[];
  /**
   * Whether Swiper should be initialised automatically when you create an instance.
   * If disabled, then you need to init it manually by calling `swiper.init()`
   *
   * @default true
   */
  init?: boolean;

  /**
   * Whether Swiper initially enabled. When Swiper is disabled, it will hide all navigation elements and won't respond to any events and interactions
   *
   * @default true
   */
  enabled?: boolean;

  /**
   * Swiper will recalculate slides position on window resize (orientationchange)
   *
   * @default true
   */
  updateOnWindowResize?: boolean;

  /**
   * When enabled it will use ResizeObserver (if supported by browser) on swiper container to detect container resize (instead of watching for window resize)
   *
   * @default true
   */
  resizeObserver?: boolean;

  /**
   * Index number of initial slide.
   *
   * @default 0
   */
  initialSlide?: number;

  /**
   * Can be `'horizontal'` or `'vertical'` (for vertical slider).
   *
   * @default 'horizontal'
   */
  direction?: 'horizontal' | 'vertical';

  /**
   * Duration of transition between slides (in ms)
   *
   * @default 300
   */
  speed?: number;

  /**
   * Enabled this option and plugin will set width/height on swiper wrapper equal to total size of all slides.
   * Mostly should be used as compatibility fallback option for browser that don't support flexbox layout well
   *
   * @default false
   */
  setWrapperSize?: boolean;

  /**
   * Enabled this option and swiper will be operated as usual except it will not move, real translate values on wrapper will not be set.
   * Useful when you may need to create custom slide transition
   *
   * @default false
   */
  virtualTranslate?: boolean;

  /**
   * Swiper width (in px). Parameter allows to force Swiper width.
   * Useful only if you initialize Swiper when it is hidden and in SSR and Test environments for correct Swiper initialization
   *
   * @default null
   *
   * @note Setting this parameter will make Swiper not responsive
   */
  width?: number | null;

  /**
   * Swiper height (in px). Parameter allows to force Swiper height.
   * Useful only if you initialize Swiper when it is hidden and in SSR and Test environments for correct Swiper initialization
   *
   * @default null
   *
   * @note Setting this parameter will make Swiper not responsive
   */
  height?: number | null;

  /**
   * Set to `true` and slider wrapper will adapt its height to the height of the currently active slide
   *
   * @default false
   */
  autoHeight?: boolean;

  /**
   * Set to `true` to round values of slides width and height to prevent blurry texts on usual
   * resolution screens (if you have such)
   *
   * @default false
   */
  roundLengths?: boolean;

  /**
   * Set to `true` on  Swiper for correct touch events interception. Use only on
   * swipers that use same direction as the parent one
   *
   * @default false
   */
  nested?: boolean;

  /**
   * When enabled Swiper will automatically wrap slides with swiper-wrapper element,
   * and will create required elements for navigation, pagination and scrollbar
   * they are enabled (with their respective params object or with boolean `true`))
   *
   * @default false
   */
  createElements?: boolean;

  /**
   * CSS selector for focusable elements. Swiping will be disabled on such elements if they are "focused"
   *
   * @default 'input, select, option, textarea, button, video, label'
   */
  focusableElements?: string;

  /**
   * If enabled (by default) and navigation elements' parameters passed as a string (like `".pagination"`)
   * then Swiper will look for such elements through child elements first.
   * Applies for pagination, prev/next buttons and scrollbar elements
   *
   * @default true
   */
  uniqueNavElements?: boolean;

  /**
   * Transition effect. Can be `'slide'`, `'fade'`, `'cube'`, `'coverflow'`, `'flip'` or `'creative'`
   *
   * @default 'slide'
   */
  effect?: 'slide' | 'fade' | 'cube' | 'coverflow' | 'flip' | 'creative' | 'cards';

  /**
   * Fire Transition/SlideChange/Start/End events on swiper initialization.
   * Such events will be fired on initialization in case of your initialSlide is not 0, or you use loop mode
   *
   * @default true
   */
  runCallbacksOnInit?: boolean;

  /**
   * When enabled Swiper will be disabled and hide navigation buttons on
   * case there are not enough slides for sliding.
   *
   * @default true
   */
  watchOverflow?: boolean;

  /**
   * userAgent string. Required for browser/device detection when rendered on server-side
   *
   * @default null
   */
  userAgent?: string | null;

  /**
   * Required for active slide detection when rendered on server-side and enabled history
   *
   * @default null
   */
  url?: string | null;

  /**
   * Register event handlers
   */
  on?: {
    [event in keyof SwiperEvents]?: SwiperEvents[event];
  };

  /**
   * Add event listener that will be fired on all events
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *    onAny(eventName, ...args) {
   *      console.log('Event: ', eventName);
   *      console.log('Event data: ', args);
   *    }
   *  });
   * ```
   */
  onAny?(handler: (eventName: string, ...args: any[]) => void): void;

  /**
   * When enabled it will use modern CSS Scroll Snap API.
   * It doesn't support all of Swiper's features, but potentially should bring a much better performance in simple configurations.
   *
   * This is what is not supported when it is enabled:
   *
   * - Cube and Cards effects
   * - `speed` parameter may not have no effect
   * - All transition start/end related events (use `slideChange` instead)
   * - `slidesPerGroup` has limited support
   * - `simulateTouch` doesn't have effect and "dragging" with mouse doesn't work
   * - `resistance` doesn't have any effect
   * - `allowSlidePrev/Next`
   * - `swipeHandler`
   * - `freeMode` and all relevant features
   *
   * @default false
   */
  cssMode?: boolean;

  // Slides grid

  /**
   * Distance between slides in px.
   *
   * @default 0
   *
   * @note If you use "margin" css property to the elements which go into Swiper in which you pass "spaceBetween" into, navigation might not work property.
   */
  spaceBetween?: number;

  /**
   * Number of slides per view (slides visible at the same time on slider's container).
   * @note If you use it with "auto" value and along with `loop: true` then you need to specify `loopedSlides` parameter with amount of slides to loop (duplicate)
   * @note `slidesPerView: 'auto'` is currently not compatible with multirow mode, when `grid.rows` > 1
   *
   * @default 1
   */
  slidesPerView?: number | 'auto';

  /**
   * Set numbers of slides to define and enable group sliding. Useful to use with slidesPerView > 1
   *
   * @default 1
   */
  slidesPerGroup?: number;

  /**
   * The parameter works in the following way: If `slidesPerGroupSkip` equals `0` (default), no slides are excluded from grouping, and the resulting behaviour is the same as without this change.
   *
   * If `slidesPerGroupSkip` is equal or greater than `1` the first X slides are treated as single groups, whereas all following slides are grouped by the `slidesPerGroup` value.
   *
   * @default 0
   */
  slidesPerGroupSkip?: number;

  /**
   * This param intended to be used only with `slidesPerView: 'auto'` and `slidesPerGroup: 1`. When enabled, it will skip all slides in view on `.slideNext()` & `.slidePrev()` methods calls, on Navigation "buttons" clicks and in autoplay.
   *
   * @default false
   */
  slidesPerGroupAuto?: boolean;

  /**
   * If `true`, then active slide will be centered, not always on the left side.
   *
   * @default false
   */
  centeredSlides?: boolean;

  /**
   * If `true`, then active slide will be centered without adding gaps at the beginning and end of slider.
   * Required `centeredSlides: true`. Not intended to be used with `loop` or `pagination`
   *
   * @default false
   */
  centeredSlidesBounds?: boolean;

  /**
   * Add (in px) additional slide offset in the beginning of the container (before all slides)
   *
   * @default 0
   */
  slidesOffsetBefore?: number;

  /**
   * Add (in px) additional slide offset in the end of the container (after all slides)
   *
   * @default 0
   */
  slidesOffsetAfter?: number;

  /**
   * Normalize slide index.
   *
   * @default true
   */
  normalizeSlideIndex?: boolean;

  /**
   * When enabled it center slides if the amount of slides less than `slidesPerView`. Not intended to be used `loop` mode and `grid.rows`
   *
   * @default false
   */
  centerInsufficientSlides?: boolean;

  /**
   * This option may a little improve desktop usability. If `true`, user will see the "grab" cursor when hover on Swiper
   *
   * @default false
   */
  grabCursor?: boolean;

  /**
   * Target element to listen touch events on. Can be `'container'` (to listen for touch events on swiper) or `'wrapper'`
   * (to listen for touch events on swiper-wrapper)
   *
   * @default 'wrapper'
   */
  touchEventsTarget?: 'container' | 'wrapper';

  /**
   * Touch ratio
   *
   * @default 1
   */
  touchRatio?: number;

  /**
   * Allowable angle (in degrees) to trigger touch move
   *
   * @default 45
   */
  touchAngle?: number;

  /**
   * If `true`, Swiper will accept mouse events like touch events (click and drag to change slides)
   *
   * @default true
   */
  simulateTouch?: boolean;

  /**
   * Set to `false` if you want to disable short swipes
   *
   * @default true
   */
  shortSwipes?: boolean;

  /**
   * Set to `false` if you want to disable long swipes
   *
   * @default true
   */
  longSwipes?: boolean;

  /**
   * Ratio to trigger swipe to next/previous slide during long swipes
   *
   * @default 0.5
   */
  longSwipesRatio?: number;

  /**
   * Minimal duration (in ms) to trigger swipe to next/previous slide during long swipes
   *
   * @default 300
   */
  longSwipesMs?: number;

  /**
   * If disabled, then slider will be animated only when you release it, it will not move while you hold your finger on it
   *
   * @default true
   */
  followFinger?: boolean;

  /**
   * If `false`, then the only way to switch the slide is use of external API functions like slidePrev or slideNext
   *
   * @default true
   */
  allowTouchMove?: boolean;

  /**
   * Threshold value in px. If "touch distance" will be lower than this value then swiper will not move
   *
   * @default 0
   */
  threshold?: number;

  /**
   * If disabled, `touchstart` (`pointerdown`) event won't be prevented
   *
   * @default true
   */
  touchStartPreventDefault?: boolean;

  /**
   * Force to always prevent default for `touchstart` (`pointerdown`) event
   *
   * @default false
   */
  touchStartForcePreventDefault?: boolean;

  /**
   * If enabled, then propagation of "touchmove" will be stopped
   *
   * @default false
   */
  touchMoveStopPropagation?: boolean;

  /**
   * Enable to release Swiper events for swipe-back work in app. If set to `'prevent'` then it will prevent system swipe-back navigation instead
   *
   * @default false
   */
  edgeSwipeDetection?: boolean | string;

  /**
   * Area (in px) from left edge of the screen to release touch events for swipe-back in app
   *
   * @default 20
   */
  edgeSwipeThreshold?: number;

  /**
   * Enable to release touch events on slider edge position (beginning, end) to allow for further page scrolling
   *
   * @default false
   */
  touchReleaseOnEdges?: boolean;

  /**
   * Passive event listeners will be used by default where possible to improve scrolling performance on mobile devices.
   * But if you need to use `e.preventDefault` and you have conflict with it, then you should disable this parameter
   *
   * @default true
   */
  passiveListeners?: boolean;

  // Touch Resistance

  /**
   * Set to `false` if you want to disable resistant bounds
   *
   * @default true
   */
  resistance?: boolean;

  /**
   * This option allows you to control resistance ratio
   *
   * @default 0.85
   */
  resistanceRatio?: number;

  // Swiping / No swiping
  /**
   * When enabled it won't allow to change slides by swiping or navigation/pagination buttons during transition
   *
   * @default false
   */
  preventInteractionOnTransition?: boolean;

  /**
   * Set to `false` to disable swiping to previous slide direction (to left or top)
   *
   * @default true
   */
  allowSlidePrev?: boolean;

  /**
   * Set to `false` to disable swiping to next slide direction (to right or bottom)
   *
   * @default true
   */
  allowSlideNext?: boolean;

  /**
   * Enable/disable swiping on elements matched to class specified in `noSwipingClass`
   *
   * @default true
   */
  noSwiping?: boolean;

  /**
   * Specify `noSwiping`'s element css class
   *
   * @default 'swiper-no-swiping'
   */
  noSwipingClass?: string;

  /**
   * Can be used instead of `noSwipingClass` to specify elements to disable swiping on.
   * For example `'input'` will disable swiping on all inputs
   *
   * @default
   */
  noSwipingSelector?: string;

  /**
   * String with CSS selector or HTML element of the container with pagination that will work as only available handler for swiping
   *
   * @default null
   */
  swipeHandler?: CSSSelector | HTMLElement | null;

  // Clicks
  /**
   * Set to `true` to prevent accidental unwanted clicks on links during swiping
   *
   * @default true
   */
  preventClicks?: boolean;

  /**
   * Set to `true` to stop clicks event propagation on links during swiping
   *
   * @default true
   */
  preventClicksPropagation?: boolean;

  /**
   * Set to `true` and click on any slide will produce transition to this slide
   *
   * @default false
   */
  slideToClickedSlide?: boolean;

  // Progress
  /**
   * Enable this feature to calculate each slides progress and visibility (slides in viewport will have additional visible class)
   *
   * @default false
   */
  watchSlidesProgress?: boolean;

  // Images
  /**
   * When enabled Swiper will force to load all images
   *
   * @default true
   */
  preloadImages?: boolean;

  /**
   * When enabled Swiper will be reinitialized after all inner images (<img> tags) are loaded. Required `preloadImages: true`
   *
   * @default true
   */
  updateOnImagesReady?: boolean;

  /**
   * Set to `true` to enable continuous loop mode
   *
   * Because of nature of how the loop mode works, it will add duplicated slides. Such duplicated slides will have additional classes:
   * - `swiper-slide-duplicate` - represents duplicated slide
   * - `swiper-slide-duplicate-active` - represents slide duplicated to the currently active slide
   * - `swiper-slide-duplicate-next` - represents slide duplicated to the slide next to active
   * - `swiper-slide-duplicate-prev` - represents slide duplicated to the slide previous to active
   *
   * @default false
   *
   * @note If you use it along with `slidesPerView: 'auto'` then you need to specify `loopedSlides` parameter with amount of slides to loop (duplicate)
   */
  loop?: boolean;

  /**
   * Addition number of slides that will be cloned after creating of loop
   *
   * @default 0
   */
  loopAdditionalSlides?: number;

  /**
   * If you use `slidesPerView:'auto'` with loop mode you should tell to Swiper how many slides it should loop (duplicate) using this parameter
   *
   * @default null
   */
  loopedSlides?: number | null;

  /**
   * Enable and loop mode will fill groups with insufficient number of slides with blank slides. Good to be used with `slidesPerGroup` parameter
   *
   * @default false
   */
  loopFillGroupWithBlank?: boolean;
  /**
   * When enabled it prevents Swiper slide prev/next transitions when transitions is already in progress (has effect when `loop` enabled)
   *
   * @default true
   */
  loopPreventsSlide?: boolean;

  /**
   * Allows to set different parameter for different responsive breakpoints (screen sizes). Not all parameters can be changed in breakpoints, only those which are not required different layout and logic, like `slidesPerView`, `slidesPerGroup`, `spaceBetween`, `grid.rows`. Such parameters like `loop` and `effect` won't work
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   // Default parameters
   *   slidesPerView: 1,
   *   spaceBetween: 10,
   *   // Responsive breakpoints
   *   breakpoints: {
   *     // when window width is >= 320px
   *     320: {
   *       slidesPerView: 2,
   *       spaceBetween: 20
   *     },
   *     // when window width is >= 480px
   *     480: {
   *       slidesPerView: 3,
   *       spaceBetween: 30
   *     },
   *     // when window width is >= 640px
   *     640: {
   *       slidesPerView: 4,
   *       spaceBetween: 40
   *     }
   *   }
   * })
   * ```
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   slidesPerView: 1,
   *   spaceBetween: 10,
   *   // using "ratio" endpoints
   *   breakpoints: {
   *     '@0.75': {
   *       slidesPerView: 2,
   *       spaceBetween: 20,
   *     },
   *     '@1.00': {
   *       slidesPerView: 3,
   *       spaceBetween: 40,
   *     },
   *     '@1.50': {
   *       slidesPerView: 4,
   *       spaceBetween: 50,
   *     },
   *   }
   * });
   * ```
   */
  breakpoints?: {
    [width: number]: SwiperOptions;
    [ratio: string]: SwiperOptions;
  };

  /**
   * Base for breakpoints (beta). Can be `window` or `container`. If set to `window` (by default) then breakpoint keys mean window width. If set to `container` then breakpoint keys treated as swiper container width
   *
   * @default 'window'
   *
   * @note Currently in beta and not supported by Swiper Angular, React, Svelte and Vue components
   */
  breakpointsBase?: string;

  // Observer
  /**
   * Set to `true` to enable Mutation Observer on Swiper and its elements. In this case Swiper will be updated (reinitialized) each time if you change its style (like hide/show) or modify its child elements (like adding/removing slides)
   *
   * @default false
   */
  observer?: boolean;
  /**
   * Set to `true` if you also need to watch Mutations for Swiper slide children elements
   *
   * @default false
   */
  observeSlideChildren?: boolean;
  /**
   * Set to `true` if you also need to watch Mutations for Swiper parent elements
   *
   * @default false
   */
  observeParents?: boolean;

  // Namespace
  /**
   * The beginning of the modifier CSS class that can be added to swiper container depending on different parameters
   *
   * @default 'swiper-'
   */
  containerModifierClass?: string;

  /**
   * CSS class name of slide
   *
   * @default 'swiper-slide'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue components
   */
  slideClass?: string;

  /**
   * CSS class name of currently active slide
   *
   * @default 'swiper-slide-active'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue components
   */
  slideActiveClass?: string;

  /**
   * CSS class name of duplicated slide which represents the currently active slide
   *
   * @default 'swiper-slide-duplicate-active'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue components
   */
  slideDuplicateActiveClass?: string;

  /**
   * CSS class name of currently visible slide
   *
   * @default 'swiper-slide-visible'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideVisibleClass?: string;

  /**
   * CSS class name of slide duplicated by loop mode
   *
   * @default 'swiper-slide-duplicate'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideDuplicateClass?: string;

  /**
   * CSS class name of slide which is right after currently active slide
   *
   * @default 'swiper-slide-next'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideNextClass?: string;

  /**
   * CSS class name of duplicated slide which represents the slide next to active slide
   *
   * @default 'swiper-slide-duplicate-next'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideDuplicateNextClass?: string;

  /**
   * CSS class name of slide which is right before currently active slide
   *
   * @default 'swiper-slide-prev'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slidePrevClass?: string;

  /**
   * CSS class name of duplicated slide which represents the slide previous to active slide
   *
   * @default 'swiper-slide-duplicate-prev'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideDuplicatePrevClass?: string;

  /**
   * CSS class name of blank slide append to fill groups in loop mode when `loopFillGroupWithBlank` is also enabled
   *
   * @default 'swiper-slide-invisible-blank'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   */
  slideBlankClass?: string;

  /**
   * CSS class name of slides' wrapper
   *
   * @default 'swiper-wrapper'
   *
   * @note By changing classes you will also need to change Swiper's CSS to reflect changed classes
   *
   * @note Not supported in Swiper Angular/React/Svelte/Vue
   *
   */
  wrapperClass?: string;

  /**
   * Object with a11y parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   a11y: {
   *     prevSlideMessage: 'Previous slide',
   *     nextSlideMessage: 'Next slide',
   *   },
   * });
   * ```
   */
  a11y?: A11yOptions;

  /**
   * Object with autoplay parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *  autoplay: {
   *    delay: 5000,
   *  },
   *});
   * ```
   */
  autoplay?: AutoplayOptions | boolean;

  /**
   * Object with controller parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   controller: {
   *     inverse: true,
   *   },
   * });
   * ```
   */
  controller?: ControllerOptions;

  /**
   * Object with Coverflow-effect parameters.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'coverflow',
   *   coverflowEffect: {
   *     rotate: 30,
   *     slideShadows: false,
   *   },
   * });
   * ```
   */
  coverflowEffect?: CoverflowEffectOptions;

  /**
   * Object with Cube-effect parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'cube',
   *   cubeEffect: {
   *     slideShadows: false,
   *   },
   * });
   * ```
   */
  cubeEffect?: CubeEffectOptions;

  /**
   * Object with Fade-effect parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'fade',
   *   fadeEffect: {
   *     crossFade: true
   *   },
   * });
   * ```
   */
  fadeEffect?: FadeEffectOptions;

  /**
   * Object with Flip-effect parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'flip',
   *   flipEffect: {
   *     slideShadows: false,
   *   },
   * });
   * ```
   */
  flipEffect?: FlipEffectOptions;

  /**
   * Object with Creative-effect parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'creative',
   *   creativeEffect: {
   *     prev: {
   *       // will set `translateZ(-400px)` on previous slides
   *       translate: [0, 0, -400],
   *     },
   *     next: {
   *       // will set `translateX(100%)` on next slides
   *       translate: ['100%', 0, 0],
   *     },
   *   },
   * });
   * ```
   */
  creativeEffect?: CreativeEffectOptions;

  /**
   * Object with Cards-effect parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   effect: 'cards',
   *   cardsEffect: {
   *     // ...
   *   },
   * });
   * ```
   */
  cardsEffect?: CardsEffectOptions;

  /**
   * Enables hash url navigation to for slides.
   * Object with hash navigation parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   hashNavigation: {
   *     replaceState: true,
   *   },
   * });
   * ```
   */
  hashNavigation?: HashNavigationOptions | boolean;

  /**
   * Enables history push state where every slide will have its own url. In this parameter you have to specify main slides url like `"slides"` and specify every slide url using `data-history` attribute.
   *
   * Object with history navigation parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   history: {
   *     replaceState: true,
   *   },
   * });
   * ```
   *
   * @example
   * ```html
   * <!-- will produce "slides/slide1" url in browser history -->
   * <div class="swiper-slide" data-history="slide1"></div>
   * ```
   */
  history?: HistoryOptions | boolean;

  /**
   * Enables navigation through slides using keyboard. Object with keyboard parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   keyboard: {
   *     enabled: true,
   *     onlyInViewport: false,
   *   },
   * });
   * ```
   */
  keyboard?: KeyboardOptions | boolean;

  /**
   * Enables images lazy loading. Object with lazy loading parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   lazy: {
   *     loadPrevNext: true,
   *   },
   * });
   * ```
   */
  lazy?: LazyOptions | boolean;

  /**
   * Enables navigation through slides using mouse wheel. Object with mousewheel parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   mousewheel: {
   *     invert: true,
   *   },
   * });
   * ```
   */
  mousewheel?: MousewheelOptions | boolean;

  /**
   * Object with navigation parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   navigation: {
   *     nextEl: '.swiper-button-next',
   *     prevEl: '.swiper-button-prev',
   *   },
   * });
   * ```
   */
  navigation?: NavigationOptions | boolean;

  /**
   * Object with pagination parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   pagination: {
   *     el: '.swiper-pagination',
   *     type: 'bullets',
   *   },
   * });
   * ```
   */
  pagination?: PaginationOptions | boolean;

  /**
   * Object with parallax parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   parallax: true,
   * });
   * ```
   */
  parallax?: ParallaxOptions | boolean;

  /**
   * Object with scrollbar parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   scrollbar: {
   *     el: '.swiper-scrollbar',
   *     draggable: true,
   *   },
   * });
   * ```
   */
  scrollbar?: ScrollbarOptions | boolean;

  /**
   * Object with thumbs component parameters
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   ...
   *   thumbs: {
   *     swiper: thumbsSwiper
   *   }
   * });
   * ```
   */
  thumbs?: ThumbsOptions;

  /**
   * Enables virtual slides functionality. Object with virtual slides parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   virtual: {
   *     slides: ['Slide 1', 'Slide 2', 'Slide 3', 'Slide 4', 'Slide 5'],
   *   },
   * });
   * ```
   */
  virtual?: VirtualOptions | boolean;

  /**
   * Enables zooming functionality. Object with zoom parameters or boolean `true` to enable with default settings
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   zoom: {
   *     maxRatio: 5,
   *   },
   * });
   * ```
   */
  zoom?: ZoomOptions | boolean;

  /**
   * Enables free mode functionality. Object with free mode parameters or boolean `true` to enable with default settings.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   freeMode: true,
   * });
   *
   * const swiper = new Swiper('.swiper', {
   *   freeMode: {
   *     enabled: true,
   *     sticky: true,
   *   },
   * });
   * ```
   */
  freeMode?: FreeModeOptions | boolean;

  /**
   * Object with grid parameters to enable "multirow" slider.
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   grid: {
   *     rows: 2,
   *   },
   * });
   * ```
   */
  grid?: GridOptions;

  /**
   * !INTERNAL When enabled will emit "_containerClasses" and "_slideClass" events
   */
  _emitClasses?: boolean;
}
