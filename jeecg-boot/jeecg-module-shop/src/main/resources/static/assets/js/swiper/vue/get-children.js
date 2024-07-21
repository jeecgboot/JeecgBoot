function getChildren(originalSlots = {}, slidesRef, oldSlidesRef) {
  const slides = [];
  const slots = {
    'container-start': [],
    'container-end': [],
    'wrapper-start': [],
    'wrapper-end': []
  };

  const getSlidesFromElements = (els, slotName) => {
    if (!Array.isArray(els)) {
      return;
    }

    els.forEach(vnode => {
      const isFragment = typeof vnode.type === 'symbol';
      if (slotName === 'default') slotName = 'container-end';

      if (isFragment && vnode.children) {
        getSlidesFromElements(vnode.children, 'default');
      } else if (vnode.type && (vnode.type.name === 'SwiperSlide' || vnode.type.name === 'AsyncComponentWrapper')) {
        slides.push(vnode);
      } else if (slots[slotName]) {
        slots[slotName].push(vnode);
      }
    });
  };

  Object.keys(originalSlots).forEach(slotName => {
    const els = originalSlots[slotName]();
    getSlidesFromElements(els, slotName);
  });
  oldSlidesRef.value = slidesRef.value;
  slidesRef.value = slides;
  return {
    slides,
    slots
  };
}

export { getChildren };