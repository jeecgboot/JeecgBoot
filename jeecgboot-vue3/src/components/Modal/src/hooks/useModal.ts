import type { UseModalReturnType, ModalMethods, ModalProps, ReturnMethods, UseModalInnerReturnType } from '../typing';
import { ref, onUnmounted, unref, getCurrentInstance, reactive, watchEffect, nextTick, toRaw } from 'vue';
import { isProdMode } from '/@/utils/env';
import { isFunction } from '/@/utils/is';
import { isEqual } from 'lodash-es';
import { tryOnUnmounted } from '@vueuse/core';
import { error } from '/@/utils/log';
import { computed } from 'vue';

const dataTransfer = reactive<any>({});

const visibleData = reactive<{ [key: number]: boolean }>({});

/**
 * @description: Applicable to independent modal and call outside
 */
export function useModal(): UseModalReturnType {
  const modal = ref<Nullable<ModalMethods>>(null);
  const loaded = ref<Nullable<boolean>>(false);
  const uid = ref<string>('');

  function register(modalMethod: ModalMethods, uuid: string) {
    if (!getCurrentInstance()) {
      throw new Error('useModal() can only be used inside setup() or functional components!');
    }
    uid.value = uuid;
    isProdMode() &&
      onUnmounted(() => {
        modal.value = null;
        loaded.value = false;
        dataTransfer[unref(uid)] = null;
      });
    if (unref(loaded) && isProdMode() && modalMethod === unref(modal)) return;

    modal.value = modalMethod;
    loaded.value = true;
    modalMethod.emitVisible = (visible: boolean, uid: number) => {
      visibleData[uid] = visible;
    };
  }

  const getInstance = () => {
    const instance = unref(modal);
    if (!instance) {
      error('useModal instance is undefined!');
    }
    return instance;
  };

  const methods: ReturnMethods = {
    setModalProps: (props: Partial<ModalProps>): void => {
      getInstance()?.setModalProps(props);
    },

    getVisible: computed((): boolean => {
      return visibleData[~~unref(uid)];
    }),
    getOpen: computed((): boolean => {
      return visibleData[~~unref(uid)];
    }),
    redoModalHeight: () => {
      getInstance()?.redoModalHeight?.();
    },

    openModal: <T = any>(visible = true, data?: T, openOnSet = true): void => {
      // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
      getInstance()?.setModalProps({
        open: visible,
      });
      // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x

      if (!data) return;
      const id = unref(uid);
      if (openOnSet) {
        dataTransfer[id] = null;
        dataTransfer[id] = toRaw(data);
        return;
      }
      const equal = isEqual(toRaw(dataTransfer[id]), toRaw(data));
      if (!equal) {
        dataTransfer[id] = toRaw(data);
      }
    },

    closeModal: () => {
      // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
      getInstance()?.setModalProps({ open: false });
      // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
    },
  };
  return [register, methods];
}

export const useModalInner = (callbackFn?: Fn): UseModalInnerReturnType => {
  const modalInstanceRef = ref<Nullable<ModalMethods>>(null);
  const currentInstance = getCurrentInstance();
  const uidRef = ref<string>('');

  const getInstance = () => {
    const instance = unref(modalInstanceRef);
    if (!instance) {
      error('useModalInner instance is undefined!');
    }
    return instance;
  };

  const register = (modalInstance: ModalMethods, uuid: string) => {
    isProdMode() &&
      tryOnUnmounted(() => {
        modalInstanceRef.value = null;
      });
    uidRef.value = uuid;
    modalInstanceRef.value = modalInstance;
    currentInstance?.emit('register', modalInstance, uuid);
  };

  watchEffect(() => {
    const data = dataTransfer[unref(uidRef)];
    if (!data) return;
    if (!callbackFn || !isFunction(callbackFn)) return;
    nextTick(() => {
      callbackFn(data);
    });
  });

  return [
    register,
    {
      changeLoading: (loading = true) => {
        getInstance()?.setModalProps({ loading });
      },
      getVisible: computed((): boolean => {
        return visibleData[~~unref(uidRef)];
      }),
      getOpen: computed((): boolean => {
        return visibleData[~~unref(uidRef)];
      }),
      changeOkLoading: (loading = true) => {
        getInstance()?.setModalProps({ confirmLoading: loading });
      },

      closeModal: () => {
        getInstance()?.setModalProps({ open: false });
      },

      setModalProps: (props: Partial<ModalProps>) => {
        getInstance()?.setModalProps(props);
      },

      redoModalHeight: () => {
        const callRedo = getInstance()?.redoModalHeight;
        callRedo && callRedo();
      },
    },
  ];
};
