export interface CubeEffectMethods {}

export interface CubeEffectEvents {}

export interface CubeEffectOptions {
  /**
   * Enables slides shadows
   *
   * @default true
   */
  slideShadows?: boolean;
  /**
   * Enables main slider shadow
   *
   * @default true
   */
  shadow?: boolean;
  /**
   * Main shadow offset in px
   *
   * @default 20
   */
  shadowOffset?: number;
  /**
   * Main shadow scale ratio
   *
   * @default 0.94
   */
  shadowScale?: number;
}
