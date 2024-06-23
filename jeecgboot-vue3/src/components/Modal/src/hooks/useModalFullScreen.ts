import { computed, Ref, ref, unref } from 'vue';

export interface UseFullScreenContext {
  wrapClassName: Ref<string | undefined>;
  modalWrapperRef: Ref<ComponentRef>;
  extHeightRef: Ref<number>;
}

export function useFullScreen(context: UseFullScreenContext) {
  // const formerHeightRef = ref(0);
  const fullScreenRef = ref(false);

  const getWrapClassName = computed(() => {
    const clsName = unref(context.wrapClassName) || '';
    return unref(fullScreenRef) ? `fullscreen-modal ${clsName} ` : unref(clsName);
  });

  function handleFullScreen(e: Event) {
    e && e.stopPropagation();
    fullScreenRef.value = !unref(fullScreenRef);

    // const modalWrapper = unref(context.modalWrapperRef);

    // if (!modalWrapper) return;

    // const wrapperEl = modalWrapper.$el as HTMLElement;
    // if (!wrapperEl) return;
    // const modalWrapSpinEl = wrapperEl.querySelector('.ant-spin-nested-loading') as HTMLElement;

    // if (!modalWrapSpinEl) return;

    // if (!unref(formerHeightRef) && unref(fullScreenRef)) {
    //   formerHeightRef.value = modalWrapSpinEl.offsetHeight;
    // }

    // if (unref(fullScreenRef)) {
    //   modalWrapSpinEl.style.height = `${window.innerHeight - unref(context.extHeightRef)}px`;
    // } else {
    //   modalWrapSpinEl.style.height = `${unref(formerHeightRef)}px`;
    // }
  }
  return { getWrapClassName, handleFullScreen, fullScreenRef };
}
