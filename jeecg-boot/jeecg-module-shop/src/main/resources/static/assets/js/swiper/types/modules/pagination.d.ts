import { Dom7Array } from 'dom7';
import { CSSSelector } from '../shared';
import Swiper from '../swiper-class';

export interface PaginationMethods {
  /**
   * HTMLElement of pagination container element
   */
  el: HTMLElement;

  /**
   * Dom7 array-like collection of pagination bullets
   * HTML elements. To get specific slide HTMLElement
   * use `swiper.pagination.bullets[1]`.
   */
  bullets: Dom7Array[];

  /**
   * Render pagination layout
   */
  render(): void;

  /**
   * Update pagination state (enabled/disabled/active)
   */
  update(): void;

  /**
   * Initialize pagination
   */
  init(): void;

  /**
   * Destroy pagination
   */
  destroy(): void;
}

export interface PaginationEvents {
  /**
   * Event will be fired after pagination rendered
   */
  paginationRender: (swiper: Swiper, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired when pagination updated
   */
  paginationUpdate: (swiper: Swiper, paginationEl: HTMLElement) => void;

  /**
   * Event will be fired on pagination hide
   */
  paginationHide: (swiper: Swiper) => void;

  /**
   * Event will be fired on pagination show
   */
  paginationShow: (swiper: Swiper) => void;
}

export interface PaginationOptions {
  /**
   * String with CSS selector or HTML element of the container with pagination
   *
   * @default null
   */
  el?: CSSSelector | HTMLElement | null;

  /**
   * String with type of pagination. Can be `'bullets'`, `'fraction'`, `'progressbar'` or `'custom'`
   *
   * @default 'bullets'
   */
  type?: 'bullets' | 'fraction' | 'progressbar' | 'custom';

  /**
   * Defines which HTML tag will be used to represent single pagination bullet. Only for `'bullets'` pagination type.
   *
   * @default 'span'
   */
  bulletElement?: string;

  /**
   * Good to enable if you use bullets pagination with a lot of slides. So it will keep only few bullets visible at the same time.
   *
   * @default false
   */
  dynamicBullets?: boolean;

  /**
   * The number of main bullets visible when `dynamicBullets` enabled.
   *
   * @default 1
   */
  dynamicMainBullets?: number;

  /**
   * Toggle (hide/show) pagination container visibility after click on Slider's container
   *
   * @default true
   */
  hideOnClick?: boolean;

  /**
   * If `true` then clicking on pagination button will cause transition to appropriate slide. Only for bullets pagination type
   *
   * @default false
   */
  clickable?: boolean;

  /**
   * Makes pagination progressbar opposite to Swiper's `direction` parameter, means vertical progressbar for horizontal swiper
   * direction and horizontal progressbar for vertical swiper direction
   *
   * @default false
   */
  progressbarOpposite?: boolean;

  /**
   * format fraction pagination current number. Function receives current number,
   * and you need to return formatted value
   */
  formatFractionCurrent?: (number: number) => number;

  /**
   * format fraction pagination total number. Function receives total number, and you
   * need to return formatted value
   */
  formatFractionTotal?: (number: number) => number;

  /**
   * This parameter allows totally customize pagination bullets, you need to pass here a function that accepts `index` number of
   * pagination bullet and required element class name (`className`). Only for `'bullets'` pagination type
   *
   * @default null
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   //...
   *   renderBullet: function (index, className) {
   *     return '<span class="' + className + '">' + (index + 1) + '</span>';
   *   }
   * });
   * ```
   */
  renderBullet?: (index: number, className: string) => void;

  /**
   * This parameter allows to customize "fraction" pagination html. Only for `'fraction'` pagination type
   *
   * @default null
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   //...
   *   renderFraction: function (currentClass, totalClass) {
   *       return '<span class="' + currentClass + '"></span>' +
   *               ' of ' +
   *               '<span class="' + totalClass + '"></span>';
   *   }
   * });
   * ```
   */
  renderFraction?: (currentClass: string, totalClass: string) => void;

  /**
   * This parameter allows to customize "progress" pagination. Only for `'progress'` pagination type
   *
   * @default null
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   //...
   *   renderProgressbar: function (progressbarFillClass) {
   *       return '<span class="' + progressbarFillClass + '"></span>';
   *   }
   * });
   * ```
   */
  renderProgressbar?: (progressbarFillClass: string) => void;

  /**
   * This parameter is required for `'custom'` pagination type where you have to specify
   * how it should be rendered.
   *
   * @default null
   *
   * @example
   * ```js
   * const swiper = new Swiper('.swiper', {
   *   //...
   *   renderCustom: function (swiper, current, total) {
   *     return current + ' of ' + total;
   *   }
   * });
   * ```
   */
  renderCustom?: (swiper: Swiper, current: number, total: number) => void;

  /**
   * CSS class name of single pagination bullet
   *
   * @default 'swiper-pagination-bullet'
   */
  bulletClass?: string;

  /**
   * CSS class name of currently active pagination bullet
   *
   * @default 'swiper-pagination-bullet-active'
   */
  bulletActiveClass?: string;

  /**
   * The beginning of the modifier CSS class name that will be added to pagination depending on parameters
   *
   * @default 'swiper-pagination-'
   */
  modifierClass?: string;

  /**
   * CSS class name of the element with currently active index in "fraction" pagination
   *
   * @default 'swiper-pagination-current'
   */
  currentClass?: string;

  /**
   * CSS class name of the element with total number of "snaps" in "fraction" pagination
   *
   * @default 'swiper-pagination-total'
   */
  totalClass?: string;

  /**
   * CSS class name of pagination when it becomes inactive
   *
   * @default 'swiper-pagination-hidden'
   */
  hiddenClass?: string;

  /**
   * CSS class name of pagination progressbar fill element
   *
   * @default 'swiper-pagination-progressbar-fill'
   */
  progressbarFillClass?: string;

  /**
   * CSS class name of pagination progressbar opposite
   *
   * @default 'swiper-pagination-progressbar-opposite'
   */
  progressbarOppositeClass?: string;
  /**
   * CSS class name set to pagination when it is clickable
   *
   * @default 'swiper-pagination-clickable'
   */
  clickableClass?: string;

  /**
   * CSS class name set to pagination when it is disabled
   *
   * @default 'swiper-pagination-lock'
   */
  lockClass?: string;

  /**
   * CSS class name set to pagination in horizontal Swiper
   *
   * @default 'swiper-pagination-horizontal'
   */
  horizontalClass?: string;

  /**
   * CSS class name set to pagination in vertical Swiper
   *
   * @default 'swiper-pagination-vertical'
   */
  verticalClass?: string;
}
