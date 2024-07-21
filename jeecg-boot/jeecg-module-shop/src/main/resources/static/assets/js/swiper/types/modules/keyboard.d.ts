import Swiper from '../swiper-class';

export interface KeyboardMethods {
  /**
   * Whether the keyboard control is enabled
   */
  enabled: boolean;

  /**
   * Enable keyboard control
   */
  enable(): void;

  /**
   * Disable keyboard control
   */
  disable(): void;
}

export interface KeyboardEvents {
  /**
   * Event will be fired on key press
   */
  keyPress: (swiper: Swiper, keyCode: string) => void;
}

export interface KeyboardOptions {
  /**
   * Set to `true` to enable keyboard control
   *
   * @default false
   */
  enabled?: boolean;
  /**
   * When enabled it will control sliders that are currently in viewport
   *
   * @default true
   */
  onlyInViewport?: boolean;
  /**
   * When enabled it will enable keyboard navigation by Page Up and Page Down keys
   *
   * @default true
   */
  pageUpDown?: boolean;
}
