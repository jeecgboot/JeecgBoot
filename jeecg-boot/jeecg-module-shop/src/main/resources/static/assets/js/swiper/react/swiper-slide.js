function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

import React, { useRef, useState, forwardRef } from 'react';
import { uniqueClasses } from './utils.js';
import { useIsomorphicLayoutEffect } from './use-isomorphic-layout-effect.js';
const SwiperSlide = /*#__PURE__*/forwardRef(({
  tag: Tag = 'div',
  children,
  className = '',
  swiper,
  zoom,
  virtualIndex,
  ...rest
} = {}, externalRef) => {
  const slideElRef = useRef(null);
  const [slideClasses, setSlideClasses] = useState('swiper-slide');

  function updateClasses(_s, el, classNames) {
    if (el === slideElRef.current) {
      setSlideClasses(classNames);
    }
  }

  useIsomorphicLayoutEffect(() => {
    if (externalRef) {
      externalRef.current = slideElRef.current;
    }

    if (!slideElRef.current || !swiper) {
      return;
    }

    if (swiper.destroyed) {
      if (slideClasses !== 'swiper-slide') {
        setSlideClasses('swiper-slide');
      }

      return;
    }

    swiper.on('_slideClass', updateClasses); // eslint-disable-next-line

    return () => {
      if (!swiper) return;
      swiper.off('_slideClass', updateClasses);
    };
  });
  useIsomorphicLayoutEffect(() => {
    if (swiper && slideElRef.current) {
      setSlideClasses(swiper.getSlideClasses(slideElRef.current));
    }
  }, [swiper]);
  let slideData;

  if (typeof children === 'function') {
    slideData = {
      isActive: slideClasses.indexOf('swiper-slide-active') >= 0 || slideClasses.indexOf('swiper-slide-duplicate-active') >= 0,
      isVisible: slideClasses.indexOf('swiper-slide-visible') >= 0,
      isDuplicate: slideClasses.indexOf('swiper-slide-duplicate') >= 0,
      isPrev: slideClasses.indexOf('swiper-slide-prev') >= 0 || slideClasses.indexOf('swiper-slide-duplicate-prev') >= 0,
      isNext: slideClasses.indexOf('swiper-slide-next') >= 0 || slideClasses.indexOf('swiper-slide-duplicate-next') >= 0
    };
  }

  const renderChildren = () => {
    return typeof children === 'function' ? children(slideData) : children;
  };

  return /*#__PURE__*/React.createElement(Tag, _extends({
    ref: slideElRef,
    className: uniqueClasses(`${slideClasses}${className ? ` ${className}` : ''}`),
    "data-swiper-slide-index": virtualIndex
  }, rest), zoom ? /*#__PURE__*/React.createElement("div", {
    className: "swiper-zoom-container",
    "data-swiper-zoom": typeof zoom === 'number' ? zoom : undefined
  }, renderChildren()) : renderChildren());
});
SwiperSlide.displayName = 'SwiperSlide';
export { SwiperSlide };