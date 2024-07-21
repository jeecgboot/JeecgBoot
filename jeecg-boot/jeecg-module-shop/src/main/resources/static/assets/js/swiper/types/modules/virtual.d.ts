export interface VirtualMethods {
  /**
   * Object with cached slides HTML elements
   */
  cache: object;

  /**
   * Index of first rendered slide
   */
  from: number;

  /**
   * Index of last rendered slide
   */
  to: number;

  /**
   * Array with slide items passed by `virtual.slides` parameter
   */
  slides: any[];

  /*
   * Methods
   */

  /**
   * Append slide. `slides` can be a single slide item or array with such slides.
   */
  appendSlide(slide: HTMLElement | string | HTMLElement[] | string[]): void;

  /**
   * Prepend slide. `slides` can be a single slide item or array with such slides.
   */
  prependSlide(slide: HTMLElement | string | HTMLElement[] | string[]): void;

  /**
   * Remove specific slide or slides. `slideIndexes` can be a number with slide index to remove or array with indexes.
   */
  removeSlide(slideIndexes: number[]): void;

  /**
   * Remove all slides
   */
  removeAllSlides(): void;

  /**
   * Update virtual slides state
   */
  update(force: boolean): void;
}

export interface VirtualEvents {}

export interface VirtualData {
  /**
   * slides left/top offset in px
   */
  offset: number;
  /**
   * index of first slide required to be rendered
   */
  from: number;
  /**
   * index of last slide required to be rendered
   */
  to: number;
  /**
   * array with slide items to be rendered
   */
  slides: any[];
}

export interface VirtualOptions {
  enabled?: boolean;
  /**
   * Array with slides
   *
   * @default []
   */
  slides?: any[];
  /**
   * Enables DOM cache of rendering slides html elements. Once they are rendered they will be saved to cache and reused from it.
   *
   * @default true
   */
  cache?: boolean;
  /**
   * Increases amount of pre-rendered slides before active slide
   *
   * @default 0
   */
  addSlidesBefore?: number;
  /**
   * Increases amount of pre-rendered slides after active slide
   *
   * @default 0
   */
  addSlidesAfter?: number;
  /**
   * Function to render slide. As an argument it accepts current slide item for `slides` array and index number of the current slide. Function must return an outter HTML of the swiper slide.
   *
   * @default null
   */
  renderSlide?: (slide: any, index: any) => any | null;
  /**
   * Function for external rendering (e.g. using some other library to handle DOM manipulations and state like React.js or Vue.js). As an argument it accepts `data` object with the following properties:
   *
   * - `offset` - slides left/top offset in px
   * - `from` - index of first slide required to be rendered
   * - `to` - index of last slide required to be rendered
   * - `slides` - array with slide items to be rendered
   *
   * @default null
   */
  renderExternal?: (data: VirtualData) => any | null;
  /**
   * When enabled (by default) it will update Swiper layout right after renderExternal called. Useful to disable and update swiper manually when used with render libraries that renders asynchronously
   *
   * @default true
   */
  renderExternalUpdate?: boolean;
}
