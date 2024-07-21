import { h } from 'vue';

function updateOnVirtualData(swiper) {
  if (!swiper || swiper.destroyed || !swiper.params.virtual || swiper.params.virtual && !swiper.params.virtual.enabled) return;
  swiper.updateSlides();
  swiper.updateProgress();
  swiper.updateSlidesClasses();

  if (swiper.lazy && swiper.params.lazy.enabled) {
    swiper.lazy.load();
  }

  if (swiper.parallax && swiper.params.parallax && swiper.params.parallax.enabled) {
    swiper.parallax.setTranslate();
  }
}

function renderVirtual(swiperRef, slides, virtualData) {
  if (!virtualData) return null;
  const style = swiperRef.value.isHorizontal() ? {
    [swiperRef.value.rtlTranslate ? 'right' : 'left']: `${virtualData.offset}px`
  } : {
    top: `${virtualData.offset}px`
  };
  return slides.filter((slide, index) => index >= virtualData.from && index <= virtualData.to).map(slide => {
    if (!slide.props) slide.props = {};
    if (!slide.props.style) slide.props.style = {};
    slide.props.swiperRef = swiperRef;
    slide.props.style = style;
    return h(slide.type, { ...slide.props
    }, slide.children);
  });
}

export { renderVirtual, updateOnVirtualData };