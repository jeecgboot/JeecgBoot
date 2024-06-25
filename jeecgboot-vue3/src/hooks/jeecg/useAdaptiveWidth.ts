/**
 * 自适应宽度构造器
 *
 * @time 2022-4-8
 * @author sunjianlei
 */
import { ref } from 'vue';
import { useDebounceFn, tryOnUnmounted } from '@vueuse/core';
import { useEventListener } from '/@/hooks/event/useEventListener';

// key = js运算符+数字
const defWidthConfig: configType = {
  '<=565': '100%',
  '<=1366': '800px',
  '<=1600': '600px',
  '<=1920': '600px',
  '>1920': '500px',
};

type configType = Record<string, string | number>;

/**
 * 自适应宽度
 *
 * @param widthConfig 宽度配置，可参考 defWidthConfig 配置
 * @param assign 是否合并默认配置
 * @param debounce 去抖毫秒数
 */
export function useAdaptiveWidth(widthConfig = defWidthConfig, assign = true, debounce = 50) {
  const widthConfigAssign = assign ? Object.assign({}, defWidthConfig, widthConfig) : widthConfig;
  const configKeys = Object.keys(widthConfigAssign);

  const adaptiveWidth = ref<string | number>();

  /**
   * 进行计算宽度
   * @param innerWidth
   */
  function calcWidth(innerWidth) {
    let width;
    for (const key of configKeys) {
      try {
        // 通过js运算
        let flag = new Function(`return ${innerWidth} ${key}`)();
        if (flag) {
          width = widthConfigAssign[key];
          break;
        }
      } catch (e) {
        console.error(e);
      }
    }
    if (width) {
      adaptiveWidth.value = width;
    } else {
      console.warn('没有找到匹配的自适应宽度');
    }
  }

  // 初始计算
  calcWidth(window.innerWidth);

  // 监听 resize 事件
  const { removeEvent } = useEventListener({
    el: window,
    name: 'resize',
    listener: useDebounceFn(() => calcWidth(window.innerWidth), debounce),
  });
  // 卸载组件时取消监听事件
  tryOnUnmounted(() => removeEvent());

  return { adaptiveWidth };
}

/**
 * 抽屉自适应宽度
 */
export function useDrawerAdaptiveWidth() {
  return useAdaptiveWidth(
    {
      '<=620': '100%',
      '<=1600': 600,
      '<=1920': 650,
      '>1920': 700,
    },
    false
  );
}
