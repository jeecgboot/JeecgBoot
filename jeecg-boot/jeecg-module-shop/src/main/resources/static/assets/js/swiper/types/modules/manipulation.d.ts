export interface ManipulationMethods {
  /**
   * Add new slides to the end. slides could be
   * HTMLElement or HTML string with new slide or
   * array with such slides, for example:
   *
   * @example
   * ```js
   * appendSlide('<div class="swiper-slide">Slide 10"</div>')
   *
   * appendSlide([
   *  '<div class="swiper-slide">Slide 10"</div>',
   *  '<div class="swiper-slide">Slide 11"</div>'
   * ]);
   * ```
   */
  appendSlide(slides: HTMLElement | string | string[] | HTMLElement[]): void;

  /**
   * Add new slides to the beginning. slides could be
   * HTMLElement or HTML string with new slide or array with such slides, for example:
   *
   * @example
   * ```js
   * prependSlide('<div class="swiper-slide">Slide 0"</div>')
   *
   * prependSlide([
   *  '<div class="swiper-slide">Slide 1"</div>',
   *  '<div class="swiper-slide">Slide 2"</div>'
   * ]);
   * ```
   */
  prependSlide(slides: HTMLElement | string | string[] | HTMLElement[]): void;

  /**
   * Add new slides to the required index. slides could be HTMLElement or HTML string with new slide or array with such slides, for example:
   *
   * @example
   * ```js
   * addSlide(1, '<div class="swiper-slide">Slide 10"</div>')
   *
   * addSlide(1, [
   *  '<div class="swiper-slide">Slide 10"</div>',
   *  '<div class="swiper-slide">Slide 11"</div>'
   * ]);
   * ```
   */
  addSlide(index: number, slides: HTMLElement | string | string[] | HTMLElement[]): void;

  /**
   * Remove selected slides. slideIndex could be a number with slide index to remove or array with indexes.
   *
   * @example
   * ```js
   * removeSlide(0); // remove first slide
   * removeSlide([0, 1]); // remove first and second slides
   * removeAllSlides();    // Remove all slides
   * ```
   */
  removeSlide(slideIndex: number | number[]): void;

  /**
   * Remove all slides
   */
  removeAllSlides(): void;
}

export interface ManipulationEvents {}

export interface ManipulationOptions {}
