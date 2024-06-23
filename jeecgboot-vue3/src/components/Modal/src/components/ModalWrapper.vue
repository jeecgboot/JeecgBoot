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

      let realHeight = 0;

      let stopElResizeFn: Fn = () => {};

      useWindowSizeFn(setModalHeight.bind(null, false));

      // update-begin--author:liaozhiyang---date:2024-04-18---for：【QQYUN-9035】basicModal不设置maxHeight或height会一直执行setModalHeight，需即使销毁MutationObserver
      // update-begin--author:liaozhiyang---date:2024-05-30---for：【TV360X-145】将弹窗还原全屏后，关闭再打开窗口变小了
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
      // update-end--author:liaozhiyang---date:2024-05-30---for：【TV360X-145】将弹窗还原全屏后，关闭再打开窗口变小了
      // update-end--author:liaozhiyang---date:2024-04-18---for：【QQYUN-9035】basicModal不设置maxHeight或height会一直执行setModalHeight，需即使销毁MutationObserver

      createModalContext({
        redoModalHeight: setModalHeight,
      });

      const spinStyle = computed((): CSSProperties => {
        // update-begin--author:liaozhiyang---date:20231205---for：【QQYUN-7147】Model的高度设置不生效
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
            return {
              minHeight: `${props.minHeight === null ? defaultMiniHeight : props.minHeight}px`,
              // update-begin--author:liaozhiyang---date:20231219---for：【QQYUN-7641】basicModal组件添加MaxHeight属性
              maxHeight: `${props.maxHeight ? props.maxHeight : unref(realHeightRef)}px`,
              // update-end--author:liaozhiyang---date:20231219---for：【QQYUN-7641】basicModal组件添加MaxHeight属性
            };
          }
        }
        // update-end--author:liaozhiyang---date:20231205---for：【QQYUN-7147】Model的高度设置不生效
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
        console.log("---------性能监控--------setModalHeight----------")
        const options = option || {};
        const source = options.source;
        const callBack = options.callBack;
        // 解决在弹窗关闭的时候监听还存在,导致再次打开弹窗没有高度
        // 加上这个,就必须在使用的时候传递父级的visible
        if (!props.visible) return;
        const wrapperRefDom = unref(wrapperRef);
        if (!wrapperRefDom) return;
        // update-begin--author:liaozhiyang---date:20240320---for：【QQYUN-8573】BasicModal组件在非全屏的情况下最大高度获取异常，不论内容高度是否超出屏幕高度，都等于内容高度
        const bodyDom = wrapperRefDom.$el.parentElement?.parentElement?.parentElement;
        // update-end--author:liaozhiyang---date:20240320---for：BasicModal组件在非全屏的情况下最大高度获取异常，不论内容高度是否超出屏幕高度，都等于内容高度
        if (!bodyDom) return;
        // bodyDom.style.padding = '0';
        await nextTick();

        try {
          const modalDom = bodyDom.parentElement && bodyDom.parentElement.parentElement;
          if (!modalDom) return;

          const modalRect = getComputedStyle(modalDom as Element).top;
          const modalTop = Number.parseInt(modalRect);
          let maxHeight = window.innerHeight - modalTop * 2 + (props.footerOffset! || 0) - props.modalFooterHeight - props.modalHeaderHeight;

          // 距离顶部过进会出现滚动条
          if (modalTop < 40) {
            maxHeight -= 26;
          }
          await nextTick();
          const spinEl = unref(spinRef);

          if (!spinEl) return;
          await nextTick();
          // if (!realHeight) {
          realHeight = spinEl.scrollHeight;
          // }

          if (props.fullScreen) {
            realHeightRef.value = window.innerHeight - props.modalFooterHeight - props.modalHeaderHeight - 28;
          } else {
            realHeightRef.value = props.height ? props.height : realHeight > maxHeight ? maxHeight : realHeight;
          }
          // update-begin--author:liaozhiyang---date:2024-04-18---for：【QQYUN-9035】basicModal不设置maxHeight或height会一直执行setModalHeight，需即使销毁MutationObserver
          if (source == 'muob') {
            callBack(realHeightRef.value);
          }
          // update-end--author:liaozhiyang---date:2024-04-18---for：【QQYUN-9035】basicModal不设置maxHeight或height会一直执行setModalHeight，需即使销毁MutationObserver
          
          emit('height-change', unref(realHeightRef));
        } catch (error) {
          console.log(error);
        }
      }

      return { wrapperRef, spinRef, spinStyle, scrollTop, setModalHeight };
    },
  });
</script>
