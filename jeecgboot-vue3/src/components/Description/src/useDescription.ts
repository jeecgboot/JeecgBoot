import type { DescriptionProps, DescInstance, UseDescReturnType } from './typing';
import { ref, getCurrentInstance, unref, onUnmounted } from 'vue';
import { isProdMode } from '/@/utils/env';

export function useDescription(props?: Partial<DescriptionProps>): UseDescReturnType {
  if (!getCurrentInstance()) {
    throw new Error('useDescription() can only be used inside setup() or functional components!');
  }
  const desc = ref<Nullable<DescInstance>>(null);
  const loaded = ref(false);

  function register(instance: DescInstance) {
    // update-begin--author:liaozhiyang---date:20251223---for:【pull/9125】在抽屉中配置destroy-on-close，再次打开未正确渲染
    isProdMode() &&
      onUnmounted(() => {
        desc.value = null;
        loaded.value = false;
      });
    if (unref(loaded) && isProdMode() && instance === unref(desc)) return;
    // update-end--author:liaozhiyang---date:20251223---for:【pull/9125】在抽屉中配置destroy-on-close，再次打开未正确渲染
    desc.value = instance;
    props && instance.setDescProps(props);
    loaded.value = true;
  }

  const methods: DescInstance = {
    setDescProps: (descProps: Partial<DescriptionProps>): void => {
      unref(desc)?.setDescProps(descProps);
    },
  };

  return [register, methods];
}
