import Swiper from '../swiper-class';

export interface ControllerMethods {
  /**
   * Pass here another Swiper instance or array with Swiper instances that should be controlled
   * by this Swiper
   */
  control?: Swiper | Swiper[];
}

export interface ControllerEvents {}

export interface ControllerOptions {
  /**
   * Pass here another Swiper instance or array with Swiper instances that should be controlled
   * by this Swiper
   */
  control?: Swiper | Swiper[];

  /**
   * Set to `true` and controlling will be in inverse direction
   *
   * @default false
   */
  inverse?: boolean;

  /**
   * Defines a way how to control another slider: slide by slide
   * (with respect to other slider's grid) or depending on all slides/container
   * (depending on total slider percentage).
   *
   * @default 'slide'
   */
  by?: 'slide' | 'container';
}
