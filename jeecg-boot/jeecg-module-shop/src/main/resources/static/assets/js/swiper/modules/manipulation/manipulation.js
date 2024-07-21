import appendSlide from './methods/appendSlide.js';
import prependSlide from './methods/prependSlide.js';
import addSlide from './methods/addSlide.js';
import removeSlide from './methods/removeSlide.js';
import removeAllSlides from './methods/removeAllSlides.js';
export default function Manipulation({
  swiper
}) {
  Object.assign(swiper, {
    appendSlide: appendSlide.bind(swiper),
    prependSlide: prependSlide.bind(swiper),
    addSlide: addSlide.bind(swiper),
    removeSlide: removeSlide.bind(swiper),
    removeAllSlides: removeAllSlides.bind(swiper)
  });
}