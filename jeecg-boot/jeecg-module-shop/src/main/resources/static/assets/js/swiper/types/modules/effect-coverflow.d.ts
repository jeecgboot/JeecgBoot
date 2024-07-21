import { CSSSelector } from '../shared';

export interface CoverflowEffectMethods {}

export interface CoverflowEffectEvents {}

export interface CoverflowEffectOptions {
  /**
   * Enables slides shadows
   *
   * @default true
   */
  slideShadows?: boolean;
  /**
   * Slide rotate in degrees
   *
   * @default 50
   */
  rotate?: number;
  /**
   * Stretch space between slides (in px)
   *
   * @default 0
   */
  stretch?: number;
  /**
   * Depth offset in px (slides translate in Z axis)
   *
   * @default 100
   */
  depth?: number;
  /**
   * Slide scale effect
   *
   * @default 1
   */
  scale?: number;
  /**
   * Effect multiplier
   *
   * @default 1
   */
  modifier?: number;
  /**
   * CSS selector of the element inside of the slide to transform instead of the slide itself. Useful to use with cssMode
   *
   * @default null
   */
  transformEl?: CSSSelector;
}
