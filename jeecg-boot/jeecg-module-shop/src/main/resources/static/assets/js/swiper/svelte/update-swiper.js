import { isObject, extend } from './utils.js';

function updateSwiper({
  swiper,
  passedParams,
  changedParams,
  nextEl,
  prevEl,
  scrollbarEl,
  paginationEl
}) {
  const updateParams = changedParams.filter(key => key !== 'children' && key !== 'direction');
  const {
    params: currentParams,
    pagination,
    navigation,
    scrollbar,
    thumbs
  } = swiper;
  let needThumbsInit;
  let needControllerInit;
  let needPaginationInit;
  let needScrollbarInit;
  let needNavigationInit;

  if (changedParams.includes('thumbs') && passedParams.thumbs && passedParams.thumbs.swiper && currentParams.thumbs && !currentParams.thumbs.swiper) {
    needThumbsInit = true;
  }

  if (changedParams.includes('controller') && passedParams.controller && passedParams.controller.control && currentParams.controller && !currentParams.controller.control) {
    needControllerInit = true;
  }

  if (changedParams.includes('pagination') && passedParams.pagination && (passedParams.pagination.el || paginationEl) && (currentParams.pagination || currentParams.pagination === false) && pagination && !pagination.el) {
    needPaginationInit = true;
  }

  if (changedParams.includes('scrollbar') && passedParams.scrollbar && (passedParams.scrollbar.el || scrollbarEl) && (currentParams.scrollbar || currentParams.scrollbar === false) && scrollbar && !scrollbar.el) {
    needScrollbarInit = true;
  }

  if (changedParams.includes('navigation') && passedParams.navigation && (passedParams.navigation.prevEl || prevEl) && (passedParams.navigation.nextEl || nextEl) && (currentParams.navigation || currentParams.navigation === false) && navigation && !navigation.prevEl && !navigation.nextEl) {
    needNavigationInit = true;
  }

  if (changedParams.includes('virtual')) {
    if (passedParams.virtual && passedParams.virtual.slides && swiper.virtual) {
      swiper.virtual.slides = passedParams.virtual.slides;
      swiper.virtual.update();
    }
  }

  const destroyModule = mod => {
    if (!swiper[mod]) return;
    swiper[mod].destroy();

    if (mod === 'navigation') {
      currentParams[mod].prevEl = undefined;
      currentParams[mod].nextEl = undefined;
      swiper[mod].prevEl = undefined;
      swiper[mod].nextEl = undefined;
    } else {
      currentParams[mod].el = undefined;
      swiper[mod].el = undefined;
    }
  };

  updateParams.forEach(key => {
    if (isObject(currentParams[key]) && isObject(passedParams[key])) {
      extend(currentParams[key], passedParams[key]);
    } else {
      const newValue = passedParams[key];

      if ((newValue === true || newValue === false) && (key === 'navigation' || key === 'pagination' || key === 'scrollbar')) {
        if (newValue === false) {
          destroyModule(key);
        }
      } else {
        currentParams[key] = passedParams[key];
      }
    }
  });

  if (needThumbsInit) {
    const initialized = thumbs.init();

    if (initialized) {
      thumbs.update(true);
    }
  }

  if (needControllerInit) {
    swiper.controller.control = currentParams.controller.control;
  }

  if (needPaginationInit) {
    if (paginationEl) currentParams.pagination.el = paginationEl;
    pagination.init();
    pagination.render();
    pagination.update();
  }

  if (needScrollbarInit) {
    if (scrollbarEl) currentParams.scrollbar.el = scrollbarEl;
    scrollbar.init();
    scrollbar.updateSize();
    scrollbar.setTranslate();
  }

  if (needNavigationInit) {
    if (nextEl) currentParams.navigation.nextEl = nextEl;
    if (prevEl) currentParams.navigation.prevEl = prevEl;
    navigation.init();
    navigation.update();
  }

  if (changedParams.includes('allowSlideNext')) {
    swiper.allowSlideNext = passedParams.allowSlideNext;
  }

  if (changedParams.includes('allowSlidePrev')) {
    swiper.allowSlidePrev = passedParams.allowSlidePrev;
  }

  if (changedParams.includes('direction')) {
    swiper.changeDirection(passedParams.direction, false);
  }

  swiper.update();
}

export { updateSwiper };