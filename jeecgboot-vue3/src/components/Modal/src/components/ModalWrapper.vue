<template>
  <ScrollContainer ref="wrapperRef">
    <div ref="spinRef" :style="spinStyle" v-loading="loading" :loading-tip="loadingTip">
      <slot></slot>
    </div>
  </ScrollContainer>
</template>
<script lang="ts">
  import type { CSSProperties } from 'vue';
  import { defineComponent, computed, ref, watchEffect, unref, watch, onMounted, nextTick, onUnmounted } from 'vue';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import { ScrollContainer } from '/@/components/Container';
  import { createModalContext } from '../hooks/useModalContext';
  import { useMutationObserver } from '@vueuse/core';
  import { computeModalBodyMaxHeight, resolveModalTop } from '../utils/modalHeight';

  const props = {
    loading: { type: Boolean },
    useWrapper: { type: Boolean, default: true },
    modalHeaderHeight: { type: Number, default: 57 },
    modalFooterHeight: { type: Number, default: 74 },
    minHeight: { type: Number, default: null },
    maxHeight: { type: Number, default: null },
    height: { type: Number },
    footerOffset: { type: Number, default: 0 },
    visible: { type: Boolean },
    fullScreen: { type: Boolean },
    loadingTip: { type: String },
  };

  export default defineComponent({
    name: 'ModalWrapper',
    components: { ScrollContainer },
    inheritAttrs: false,
    props,
    emits: ['height-change', 'ext-height'],
    setup(props, { emit }) {
      const wrapperRef = ref<ComponentRef>(null);
      const spinRef = ref<ElRef>(null);
      const realHeightRef = ref(0);
      const minRealHeightRef = ref(0);
      const availableHeightRef = ref(0);

      let realHeight = 0;

      let stopElResizeFn: Fn = () => {};

      useWindowSizeFn(setModalHeight.bind(null, false));

      // 代码逻辑说明: 【TV360X-145】将弹窗还原全屏后，关闭再打开窗口变小了
      let observer,
        recordCount: any = {};
      watch(
        () => props.visible,
        () => {
          if (props.visible && !observer && !(props.maxHeight || props.height)) {
            recordCount = {};
            observer = useMutationObserver(
              spinRef,
              () => {
                setModalHeight({
                  source: 'muob',
                  callBack: (height) => {
                    const count = recordCount[height];
                    if (count) {
                      recordCount[height] = ++recordCount[height];
                      if (count > 10) {
                        observer.stop();
                        recordCount = {};
                        observer = null;
                      }
                    } else {
                      recordCount = {};
                      recordCount[height] = 1;
                    }
                  },
                });
              },
              {
                attributes: true,
                childList: true,
                subtree: true,
              }
            );
          } else {
            if (observer) {
              observer.stop();
              observer = null;
            }
          }
        },
        { immediate: true }
      );

      createModalContext({
        redoModalHeight: setModalHeight,
      });

      const spinStyle = computed((): CSSProperties => {
        // 代码逻辑说明: 【QQYUN-7147】Model的高度设置不生效
        if (props.fullScreen) {
          return {
            height: `${unref(realHeightRef)}px`,
          };
        } else {
          const defaultMiniHeight = 200;
          if (props.height != undefined) {
            let height: number = props.height;
            if (props.minHeight === null) {
              return {
                height: `${height}px`,
              };
            } else {
              return {
                height: `${props.minHeight > height ? props.minHeight : height}px`,
              };
            }
          } else {
            const minH = props.minHeight === null ? defaultMiniHeight : props.minHeight;
            const available = unref(availableHeightRef) || unref(realHeightRef) || minH;
            const cap = props.maxHeight
              ? Math.min(props.maxHeight, available || props.maxHeight)
              : (unref(realHeightRef) || available);
            return {
              minHeight: `${minH}px`,
              // 代码逻辑说明: 【QQYUN-7641】basicModal组件添加MaxHeight属性 / 【issues/9850】未设置 height 时用视口可用高度封顶
              maxHeight: `${Math.max(cap, minH)}px`,
            };
          }
        }
      });

      watchEffect(() => {
        props.useWrapper && setModalHeight();
      });

      watch(
        () => props.fullScreen,
        (v) => {
          setModalHeight();
          if (!v) {
            realHeightRef.value = minRealHeightRef.value;
          } else {
            minRealHeightRef.value = realHeightRef.value;
          }
        }
      );

      onMounted(() => {
        const { modalHeaderHeight, modalFooterHeight } = props;
        emit('ext-height', modalHeaderHeight + modalFooterHeight);
      });

      onUnmounted(() => {
        stopElResizeFn && stopElResizeFn();
      });

      async function scrollTop() {
        nextTick(() => {
          const wrapperRefDom = unref(wrapperRef);
          if (!wrapperRefDom) return;
          (wrapperRefDom as any)?.scrollTo?.(0);
        });
      }

      async function setModalHeight(option?) {
        const options = option || {};
        const source = options.source;
        const callBack = options.callBack;
        // 解决在弹窗关闭的时候监听还存在,导致再次打开弹窗没有高度
        // 加上这个,就必须在使用的时候传递父级的visible
        if (!props.visible) return;
        const wrapperRefDom = unref(wrapperRef);
        if (!wrapperRefDom) return;
        const spinEl = unref(spinRef);
        if (!spinEl) return;
        await nextTick();

        try {
          const modalDom = (spinEl as HTMLElement).closest('.ant-modal') as HTMLElement | null;
          if (!modalDom) return;

          const headerEl = modalDom.querySelector('.ant-modal-header') as HTMLElement | null;
          const footerEl = modalDom.querySelector('.ant-modal-footer') as HTMLElement | null;
          const headerHeight = headerEl?.offsetHeight || (props.modalHeaderHeight ?? 0);
          const footerHeight = footerEl?.offsetHeight || (props.modalFooterHeight ?? 0);

          // CSS top in px is stable during enter animation; painted rect is the fallback (antd 4 inner wrap / centered)
          const modalTop = resolveModalTop(getComputedStyle(modalDom).top, modalDom.getBoundingClientRect().top);
          let maxHeight = computeModalBodyMaxHeight({
            viewportHeight: window.innerHeight,
            modalTop,
            headerHeight,
            footerHeight,
            footerOffset: props.footerOffset || 0,
          });

          availableHeightRef.value = maxHeight;
          await nextTick();
          realHeight = spinEl.scrollHeight;

          if (props.fullScreen) {
            realHeightRef.value = window.innerHeight - footerHeight - headerHeight - 28;
          } else {
            realHeightRef.value = props.height ? props.height : realHeight > maxHeight ? maxHeight : realHeight;
          }
          // 代码逻辑说明: 【QQYUN-9035】basicModal不设置maxHeight或height会一直执行setModalHeight，需即使销毁MutationObserver
          if (source == 'muob') {
            callBack(realHeightRef.value);
          }

          emit('height-change', unref(realHeightRef));
        } catch (error) {
          console.log(error);
        }
      }

      return { wrapperRef, spinRef, spinStyle, scrollTop, setModalHeight };
    },
  });
</script>
