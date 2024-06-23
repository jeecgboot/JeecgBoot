import { computed, reactive, h } from 'vue';
import { JVxeTableMethods, JVxeTableProps } from '/@/components/jeecg/JVxeTable/src/types';
import { isEmpty } from '/@/utils/is';
import { Pagination } from 'ant-design-vue';

export function usePagination(props: JVxeTableProps, methods: JVxeTableMethods) {
  const innerPagination = reactive({
    current: 1,
    pageSize: 10,
    pageSizeOptions: ['10', '20', '30'],
    showTotal: (total, range) => {
      return range[0] + '-' + range[1] + ' 共 ' + total + ' 条';
    },
    showQuickJumper: true,
    showSizeChanger: true,
    total: 100,
  });

  const bindProps = computed(() => {
    return {
      ...innerPagination,
      ...props.pagination,
      size: props.size === 'tiny' ? 'small' : '',
    };
  });

  const boxClass = computed(() => {
    return {
      'j-vxe-pagination': true,
      'show-quick-jumper': !!bindProps.value.showQuickJumper,
    };
  });

  function handleChange(current, pageSize) {
    innerPagination.current = current;
    methods.trigger('pageChange', { current, pageSize });
  }

  function handleShowSizeChange(current, pageSize) {
    innerPagination.pageSize = pageSize;
    methods.trigger('pageChange', { current, pageSize });
  }

  /** 渲染分页器 */
  function renderPagination() {
    if (props.pagination && !isEmpty(props.pagination)) {
      return h(
        'div',
        {
          class: boxClass.value,
        },
        [
          h(Pagination, {
            ...bindProps.value,
            disabled: props.disabled,
            onChange: handleChange,
            onShowSizeChange: handleShowSizeChange,
          }),
        ]
      );
    }
    return null;
  }

  return { renderPagination };
}
