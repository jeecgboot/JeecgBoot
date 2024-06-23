import { h } from 'vue';
import { JVxeDataProps, JVxeTableMethods, JVxeTableProps } from '../types';
import JVxeSubPopover from '../components/JVxeSubPopover.vue';
import JVxeDetailsModal from '../components/JVxeDetailsModal.vue';
import { useToolbar } from '/@/components/jeecg/JVxeTable/src/hooks/useToolbar';
import { usePagination } from '/@/components/jeecg/JVxeTable/src/hooks/usePagination';

export function useRenderComponents(props: JVxeTableProps, data: JVxeDataProps, methods: JVxeTableMethods, slots) {
  // 渲染 toolbar
  const { renderToolbar } = useToolbar(props, data, methods, slots);
  // 渲染分页器
  const { renderPagination } = usePagination(props, methods);

  // 渲染 toolbarAfter 插槽
  function renderToolbarAfterSlot() {
    if (slots['toolbarAfter']) {
      return slots['toolbarAfter']();
    }
    return null;
  }

  // 渲染点击时弹出的子表
  function renderSubPopover() {
    if (props.clickRowShowSubForm && slots.subForm) {
      return h(
        JVxeSubPopover,
        {
          ref: 'subPopoverRef',
        },
        {
          subForm: slots.subForm,
        }
      );
    }
    return null;
  }

  // 渲染点击时弹出的详细信息
  function renderDetailsModal() {
    if (props.clickRowShowMainForm && slots.mainForm) {
      return h(
        JVxeDetailsModal,
        {
          ref: 'detailsModalRef',
          trigger: methods.trigger,
        },
        {
          mainForm: slots.mainForm,
        }
      );
    }
  }

  return {
    renderToolbar,
    renderPagination,
    renderSubPopover,
    renderDetailsModal,
    renderToolbarAfterSlot,
  };
}
