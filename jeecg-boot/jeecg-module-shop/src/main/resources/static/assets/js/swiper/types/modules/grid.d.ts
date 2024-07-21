export interface GridMethods {}

export interface GridEvents {}

export interface GridOptions {
  /**
   * Number of slides rows, for multirow layout
   *
   * @note `rows` > 1 is currently not compatible with loop mode (`loop: true`)
   *
   * @default 1
   */
  rows?: number;

  /**
   * Can be `'column'` or `'row'`. Defines how slides should fill rows, by column or by row
   *
   * @default 'column'
   */
  fill?: 'row' | 'column';
}
