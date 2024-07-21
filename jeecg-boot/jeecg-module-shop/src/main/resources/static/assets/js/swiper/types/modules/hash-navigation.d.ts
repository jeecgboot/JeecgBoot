import Swiper from '../swiper-class';

export interface HashNavigationMethods {}

export interface HashNavigationEvents {
  /**
   * Event will be fired on window hash change
   */
  hashChange: (swiper: Swiper) => void;
  /**
   * Event will be fired when swiper updates the hash
   */
  hashSet: (swiper: Swiper) => void;
}

export interface HashNavigationOptions {
  /**
   * Set to `true` to enable also navigation through slides (when hashnav
   * is enabled) by browser history or by setting directly hash on document location
   *
   * @default false
   */
  watchState?: boolean;

  /**
   * Works in addition to hashnav to replace current url state with the
   * new one instead of adding it to history
   *
   * @default     false
   */
  replaceState?: boolean;
}
