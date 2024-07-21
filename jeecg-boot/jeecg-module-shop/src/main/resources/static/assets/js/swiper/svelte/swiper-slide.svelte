<script>
  import { onMount, onDestroy, beforeUpdate, afterUpdate } from 'svelte';
  import { uniqueClasses } from './utils.js';

  export let zoom = undefined;
  export let virtualIndex = undefined;

  let className = undefined;
  export { className as class };

  let slideEl = null;
  let slideClasses = 'swiper-slide';

  let swiper = null;
  let eventAttached = false;

  const updateClasses = (_, el, classNames) => {
    if (el === slideEl) {
      slideClasses = classNames;
    }
  };

  const attachEvent = () => {
    if (!swiper || eventAttached) return;
    swiper.on('_slideClass', updateClasses);
    eventAttached = true;
  };

  const detachEvent = () => {
    if (!swiper) return;
    swiper.off('_slideClass', updateClasses);
    eventAttached = false;
  };

  $: slideData = {
    isActive:
      slideClasses.indexOf('swiper-slide-active') >= 0 ||
      slideClasses.indexOf('swiper-slide-duplicate-active') >= 0,
    isVisible: slideClasses.indexOf('swiper-slide-visible') >= 0,
    isDuplicate: slideClasses.indexOf('swiper-slide-duplicate') >= 0,
    isPrev:
      slideClasses.indexOf('swiper-slide-prev') >= 0 ||
      slideClasses.indexOf('swiper-slide-duplicate-prev') >= 0,
    isNext:
      slideClasses.indexOf('swiper-slide-next') >= 0 ||
      slideClasses.indexOf('swiper-slide-duplicate-next') >= 0,
  };

  onMount(() => {
    if (typeof virtualIndex === 'undefined') return;
    slideEl.onSwiper = (_swiper) => {
      swiper = _swiper;
      attachEvent();
    };
    attachEvent();
  });

  afterUpdate(() => {
    if (!slideEl || !swiper) return;
    if (swiper.destroyed) {
      if (slideClasses !== 'swiper-slide') {
        slideClasses = 'swiper-slide';
      }
      return;
    }
    attachEvent();
  });

  beforeUpdate(() => {
    attachEvent();
  });

  onDestroy(() => {
    if (!swiper) return;
    detachEvent();
  });
</script>

<div
  bind:this={slideEl}
  class={uniqueClasses(`${slideClasses}${className ? ` ${className}` : ''}`)}
  data-swiper-slide-index={virtualIndex}
  {...$$restProps}
>
  {#if zoom}
    <div
      class="swiper-zoom-container"
      data-swiper-zoom={typeof zoom === 'number' ? zoom : undefined}
    >
      <slot data={slideData} />
    </div>
  {:else}
    <slot data={slideData} />
  {/if}
</div>
