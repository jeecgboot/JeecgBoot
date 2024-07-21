import Swiper from '../swiper-class';

export interface ThumbsMethods {
  /**
   * Swiper instance of thumbs swiper
   */
  swiper: Swiper;

  /**
   * Update thumbs
   */
  update(initial: boolean): void;

  /**
   * Initialize thumbs
   */
  init(): boolean;
}

export interface ThumbsEvents {}

export interface ThumbsOptions {
  /**
   * Swiper instance of swiper used as thumbs or object with Swiper parameters to initialize thumbs swiper
   *
   * @default null
   */
  swiper?: Swiper | null;
  /**
   * Additional class that will be added to activated thumbs swiper slide
   *
   * @default 'swiper-slide-thumb-active'
   */
  slideThumbActiveClass?: string;
  /**
   * Additional class that will be added to thumbs swiper
   *
   * @default 'swiper-thumbs'
   */
  thumbsContainerClass?: string;
  /**
   * When enabled multiple thumbnail slides may get activated
   *
   * @default true
   */
  multipleActiveThumbs?: boolean;
  /**
   * Allows to set on which thumbs active slide from edge it should automatically move scroll thumbs. For example, if set to 1 and last visible thumb will be activated (1 from edge) it will auto scroll thumbs

   *
   * @default 0
   */
  autoScrollOffset?: number;
}
