import { CSSSelector } from '../shared';

export interface CardsEffectMethods {}

export interface CardsEffectEvents {}

export interface CardsEffectOptions {
  /**
   * Enables slides shadows
   *
   * @default true
   */
  slideShadows?: boolean;
  /**
   * CSS selector of the element inside of the slide to transform instead of the slide itself. Useful to use with cssMode
   *
   * @default null
   */
  transformEl?: CSSSelector;
}
