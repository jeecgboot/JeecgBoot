import { defineComponent, h, ref, watch } from 'vue';
import { randomString } from '/@/utils/common/compUtils';
import '../style/reload-effect.less';

// 修改数据特效
export default defineComponent({
  props: {
    vNode: null,
    // 是否启用特效
    effect: Boolean,
  },
  emits: ['effectBegin', 'effectEnd'],
  setup(props, { emit }) {
    // vNode: null,
    const innerEffect = ref(props.effect);
    // 应付同时多个特效
    const effectIdx = ref(0);
    const effectList = ref<any[]>([]);

    watch(
      () => props.effect,
      () => (innerEffect.value = props.effect)
    );
    watch(
      () => props.vNode,
      (_vNode, old) => {
        if (props.effect && old != null) {
          let topLayer = renderSpan(old, 'top');
          effectList.value.push(topLayer);
        }
      },
      { deep: true, immediate: true }
    );

    // 条件渲染内容 span
    function renderVNode() {
      if (props.vNode == null) {
        return null;
      }
      let bottom = renderSpan(props.vNode, 'bottom');
      // 启用了特效，并且有旧数据，就渲染特效顶层
      if (innerEffect.value && effectList.value.length > 0) {
        emit('effectBegin');
        // 1.4s 以后关闭特效
        window.setTimeout(() => {
          let item = effectList.value[effectIdx.value];
          if (item && item.elm) {
            // 特效结束后，展示先把 display 设为 none，而不是直接删掉该元素，
            // 目的是为了防止页面重新渲染，导致动画重置
            item.elm.style.display = 'none';
          }
          // 当所有的层级动画都结束时，再删掉所有元素
          if (++effectIdx.value === effectList.value.length) {
            innerEffect.value = false;
            effectIdx.value = 0;
            effectList.value = [];
            emit('effectEnd');
          }
        }, 1400);
        return [effectList.value, bottom];
      } else {
        return bottom;
      }
    }

    // 渲染内容 span
    function renderSpan(vNode, layer) {
      let options = {
        key: layer + effectIdx.value + randomString(6),
        class: ['j-vxe-reload-effect-span', `layer-${layer}`],
        style: {},
        // update-begin--author:liaozhiyang---date:20240424---for：【issues/1175】解决vxetable鼠标hover之后title显示不对的问题
        title: vNode,
        // update-end--author:liaozhiyang---date:20240424---for：【issues/1175】解决vxetable鼠标hover之后title显示不对的问题

      };
      if (layer === 'top') {
        // 最新渲染的在下面
        options.style['z-index'] = 9999 - effectIdx.value;
      }
      return h('span', options, [vNode]);
    }

    return () =>
      h(
        'div',
        {
          class: ['j-vxe-reload-effect-box'],
        },
        [renderVNode()]
      );
  },
});
