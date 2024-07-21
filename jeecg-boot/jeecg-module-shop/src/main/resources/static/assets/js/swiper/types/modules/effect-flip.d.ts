import { CSSSelector } from '../shared';

export interface FlipEffectMethods {}

export interface FlipEffectEvents {}

export interface FlipEffectOptions {
  /**
   * Enables slides shadows
   *
   * @default true
   */
  slideShadows?: boolean;
  /**
   * Limit edge slides rotation
   *
   * @default true
   */
  limitRotation?: boolean;
  /**
   * CSS selector of the element inside of the slide to transform instead of the slide itself. Useful to use with cssMode
   *
   * @default null
   */
  transformEl?: CSSSelector;
}
