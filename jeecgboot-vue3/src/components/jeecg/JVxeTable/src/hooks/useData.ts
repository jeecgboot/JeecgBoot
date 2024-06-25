import { ref, reactive, provide, resolveComponent } from 'vue';
import { useDesign } from '/@/hooks/web/useDesign';
import { JVxeDataProps, JVxeRefs, JVxeTableProps } from '../types';
import { VxeGridInstance } from 'vxe-table';
import { randomString } from '/@/utils/common/compUtils';

export function useData(props: JVxeTableProps): JVxeDataProps {
  const { prefixCls } = useDesign('j-vxe-table');
  provide('prefixCls', prefixCls);
  return {
    prefixCls: prefixCls,
    caseId: `j-vxe-${randomString(8)}`,
    vxeDataSource: ref([]),
    scroll: reactive({ top: 0, left: 0 }),
    scrolling: ref(false),
    defaultVxeProps: reactive({
      // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-327】vxetable警告
      // rowId: props.rowKey,
      rowConfig: {
        keyField: props.rowKey,
      },
      // update-end--author:liaozhiyang---date:20240607---for：【TV360X-327】vxetable警告
      // 高亮hover的行
      highlightHoverRow: true,

      // --- 【issues/209】自带的tooltip会错位，所以替换成原生的title ---
      // 溢出隐藏并显示tooltip
      showOverflow: "title",
      // 表头溢出隐藏并显示tooltip
      showHeaderOverflow: "title",
      // --- 【issues/209】自带的tooltip会错位，所以替换成原生的title ---

      showFooterOverflow: true,
      // 可编辑配置
      editConfig: {
        trigger: 'click',
        mode: 'cell',
        // update-begin--author:liaozhiyang---date:20231013---for：【QQYUN-5133】JVxeTable 行编辑升级
        //activeMethod: () => !props.disabled,
        beforeEditMethod: () => !props.disabled,
        // update-end--author:liaozhiyang---date:20231013---for：【QQYUN-5133】JVxeTable 行编辑升级
      },
      expandConfig: {
        iconClose: 'ant-table-row-expand-icon ant-table-row-expand-icon-collapsed',
        iconOpen: 'ant-table-row-expand-icon ant-table-row-expand-icon-expanded',
      },
      // 虚拟滚动配置，y轴大于xx条数据时启用虚拟滚动
      scrollY: {
        gt: 30,
      },
      scrollX: {
        gt: 20,
        // 暂时关闭左右虚拟滚动
        enabled: false,
      },
      radioConfig: { highlight: true },
      checkboxConfig: { highlight: true },
      mouseConfig: { selected: false },
      keyboardConfig: {
        // 删除键功能
        isDel: false,
        // Esc键关闭编辑功能
        isEsc: true,
        // Tab 键功能
        isTab: true,
        // 任意键进入编辑（功能键除外）
        isEdit: true,
        // 方向键功能
        isArrow: true,
        // 回车键功能
        isEnter: true,
        // 如果功能被支持，用于 column.type=checkbox|radio，开启空格键切换复选框或单选框状态功能
        isChecked: true,
      },
    }),
    selectedRows: ref<any[]>([]),
    selectedRowIds: ref<string[]>([]),
    disabledRowIds: [],
    statistics: reactive({
      has: false,
      sum: [],
      average: [],
    }),
    authsMap: ref(null),
    innerEditRules: {},
    innerLinkageConfig: new Map<string, any>(),
    reloadEffectRowKeysMap: reactive({}),
  };
}

export function useRefs(): JVxeRefs {
  return {
    gridRef: ref<VxeGridInstance>(),
    subPopoverRef: ref<any>(),
    detailsModalRef: ref<any>(),
  };
}

export function useResolveComponent(...t: any[]): any {
  // @ts-ignore
  return resolveComponent(...t);
}
