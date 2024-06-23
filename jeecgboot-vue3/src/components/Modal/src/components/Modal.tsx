import { Modal } from 'ant-design-vue';
import { defineComponent, toRefs, unref } from 'vue';
import { basicProps } from '../props';
import { useModalDragMove } from '../hooks/useModalDrag';
import { useAttrs } from '/@/hooks/core/useAttrs';
import { extendSlots } from '/@/utils/helper/tsxHelper';
import { omit } from 'lodash-es';

export default defineComponent({
  name: 'Modal',
  inheritAttrs: false,
  props: omit(basicProps, ['visible']),
  emits: ['cancel'],
  setup(props, { slots, emit }) {
    const { open, draggable, destroyOnClose } = toRefs(props);
    const attrs = useAttrs();
    useModalDragMove({
      visible: open,
      destroyOnClose,
      draggable,
    });
    const onCancel = (e: Event) => {
      emit('cancel', e);
    };

    return () => {
      const propsData = { ...unref(attrs), ...props, onCancel } as Recordable;
      return <Modal {...propsData}>{extendSlots(slots)}</Modal>;
    };
  },
});
