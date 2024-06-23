import type { ComputedRef, Ref, Slots } from 'vue';
import type { BasicTableProps } from '../types/table';
import { unref, computed, h, nextTick, watchEffect } from 'vue';
import TableFooter from '../components/TableFooter.vue';
import { useEventListener } from '/@/hooks/event/useEventListener';

export function useTableFooter(
  propsRef: ComputedRef<BasicTableProps>,
  slots: Slots,
  scrollRef: ComputedRef<{
    x: string | number | true;
    y: Nullable<number>;
    scrollToFirstRowOnChange: boolean;
  }>,
  tableElRef: Ref<ComponentRef>,
  getDataSourceRef: ComputedRef<Recordable>
) {
  const getIsEmptyData = computed(() => {
    return (unref(getDataSourceRef) || []).length === 0;
  });

  // 是否有展开行
  const hasExpandedRow = computed(() => Object.keys(slots).includes('expandedRowRender'))

  const getFooterProps = computed((): Recordable | undefined => {
    const { summaryFunc, showSummary, summaryData, bordered } = unref(propsRef);
    return showSummary && !unref(getIsEmptyData) ? () => h(TableFooter, {
      bordered,
      summaryFunc,
      summaryData,
      scroll: unref(scrollRef),
      hasExpandedRow: hasExpandedRow.value
    }) : undefined;
  });

  watchEffect(() => {
    handleSummary();
  });

  function handleSummary() {
    const { showSummary } = unref(propsRef);
    if (!showSummary || unref(getIsEmptyData)) return;

    nextTick(() => {
      const tableEl = unref(tableElRef);
      if (!tableEl) return;
      const bodyDom = tableEl.$el.querySelector('.ant-table-content');
      useEventListener({
        el: bodyDom,
        name: 'scroll',
        listener: () => {
          const footerBodyDom = tableEl.$el.querySelector('.ant-table-footer .ant-table-content') as HTMLDivElement;
          if (!footerBodyDom || !bodyDom) return;
          footerBodyDom.scrollLeft = bodyDom.scrollLeft;
        },
        wait: 0,
        options: true,
      });
    });
  }
  return { getFooterProps };
}
