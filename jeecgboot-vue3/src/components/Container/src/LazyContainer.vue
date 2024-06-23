<template>
  <transition-group class="h-full w-full" v-bind="$attrs" ref="elRef" :name="transitionName" :tag="tag" mode="out-in">
    <div key="component" v-if="isInit">
      <slot :loading="loading"></slot>
    </div>
    <div key="skeleton" v-else>
      <slot name="skeleton" v-if="$slots.skeleton"></slot>
      <Skeleton v-else />
    </div>
  </transition-group>
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import { defineComponent, reactive, onMounted, ref, toRef, toRefs } from 'vue';
  import { Skeleton } from 'ant-design-vue';
  import { useTimeoutFn } from '/@/hooks/core/useTimeout';
  import { useIntersectionObserver } from '/@/hooks/event/useIntersectionObserver';

  interface State {
    isInit: boolean;
    loading: boolean;
    intersectionObserverInstance: IntersectionObserver | null;
  }

  const props = {
    /**
     * Waiting time, if the time is specified, whether visible or not, it will be automatically loaded after the specified time
     */
    timeout: { type: Number },
    /**
     * The viewport where the component is located.
     * If the component is scrolling in the page container, the viewport is the container
     */
    viewport: {
      type: (typeof window !== 'undefined' ? window.HTMLElement : Object) as PropType<HTMLElement>,
      default: () => null,
    },
    /**
     * Preload threshold, css unit
     */
    threshold: { type: String, default: '0px' },
    /**
     * The scroll direction of the viewport, vertical represents the vertical direction, horizontal represents the horizontal direction
     */
    direction: {
      type: String,
      default: 'vertical',
      validator: (v) => ['vertical', 'horizontal'].includes(v),
    },
    /**
     * The label name of the outer container that wraps the component
     */
    tag: { type: String, default: 'div' },
    maxWaitingTime: { type: Number, default: 80 },
    /**
     * transition name
     */
    transitionName: { type: String, default: 'lazy-container' },
  };

  export default defineComponent({
    name: 'LazyContainer',
    components: { Skeleton },
    inheritAttrs: false,
    props,
    emits: ['init'],
    setup(props, { emit }) {
      const elRef = ref();
      const state = reactive<State>({
        isInit: false,
        loading: false,
        intersectionObserverInstance: null,
      });

      onMounted(() => {
        immediateInit();
        initIntersectionObserver();
      });

      // If there is a set delay time, it will be executed immediately
      function immediateInit() {
        const { timeout } = props;
        timeout &&
          useTimeoutFn(() => {
            init();
          }, timeout);
      }

      function init() {
        state.loading = true;

        useTimeoutFn(() => {
          if (state.isInit) return;
          state.isInit = true;
          emit('init');
        }, props.maxWaitingTime || 80);
      }

      function initIntersectionObserver() {
        const { timeout, direction, threshold } = props;
        if (timeout) return;
        // According to the scrolling direction to construct the viewport margin, used to load in advance
        let rootMargin = '0px';
        switch (direction) {
          case 'vertical':
            rootMargin = `${threshold} 0px`;
            break;
          case 'horizontal':
            rootMargin = `0px ${threshold}`;
            break;
        }

        try {
          const { stop, observer } = useIntersectionObserver({
            rootMargin,
            target: toRef(elRef.value, '$el'),
            onIntersect: (entries: any[]) => {
              const isIntersecting = entries[0].isIntersecting || entries[0].intersectionRatio;
              if (isIntersecting) {
                init();
                if (observer) {
                  stop();
                }
              }
            },
            root: toRef(props, 'viewport'),
          });
        } catch (e) {
          init();
        }
      }
      return {
        elRef,
        ...toRefs(state),
      };
    },
  });
</script>
