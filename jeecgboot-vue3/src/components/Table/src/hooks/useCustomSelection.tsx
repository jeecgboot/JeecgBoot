import type { BasicColumn } from '/@/components/Table';
import type { Ref, ComputedRef } from 'vue';
import type { BasicTableProps, PaginationProps, TableRowSelection } from '/@/components/Table';
import { computed, nextTick, onUnmounted, ref, toRaw, unref, watch, watchEffect } from 'vue';
import { omit, isEqual } from 'lodash-es';
import { throttle } from 'lodash-es';
import { Checkbox, Radio } from 'ant-design-vue';
import { isFunction } from '/@/utils/is';
import { findNodeAll } from '/@/utils/helper/treeHelper';
import { ROW_KEY } from '/@/components/Table/src/const';
import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
import { useMessage } from '/@/hooks/web/useMessage';
import { ModalFunc } from 'ant-design-vue/lib/modal/Modal';

// 自定义选择列的key
export const CUS_SEL_COLUMN_KEY = 'j-custom-selected-column';

/**
 * 自定义选择列
 */
export function useCustomSelection(
  propsRef: ComputedRef<BasicTableProps>,
  emit: EmitType,
  wrapRef: Ref<null | HTMLDivElement>,
  getPaginationRef: ComputedRef<boolean | PaginationProps>,
  tableData: Ref<Recordable[]>,
  childrenColumnName: ComputedRef<string>
) {
  const { createConfirm } = useMessage();
  // 表格body元素
  const bodyEl = ref<HTMLDivElement>();
  // body元素高度
  const bodyHeight = ref<number>(0);
  // 表格tr高度
  const rowHeight = ref<number>(0);
  // body 滚动高度
  const scrollTop = ref(0);
  // 选择的key
  const selectedKeys = ref<string[]>([]);
  // 选择的行
  const selectedRows = ref<Recordable[]>([]);
  // 变更的行
  let changeRows: Recordable[] = [];
  let allSelected: boolean = false;

  // 扁平化数据，children数据也会放到一起
  const flattedData = computed(() => {
    // update-begin--author:liaozhiyang---date:20231016---for：【QQYUN-6774】解决checkbox禁用后全选仍能勾选问题
    const data = flattenData(tableData.value, childrenColumnName.value);
    const rowSelection = propsRef.value.rowSelection;
    if (rowSelection?.type === 'checkbox' && rowSelection.getCheckboxProps) {
      for (let i = 0, len = data.length; i < len; i++) {
        const record = data[i];
        const result = rowSelection.getCheckboxProps(record);
        if (result.disabled) {
          data.splice(i, 1);
          i--;
          len--;
        }
      }
    }
    return data;
    // update-end--author:liaozhiyang---date:20231016---for：【QQYUN-6774】解决checkbox禁用后全选仍能勾选问题
  });

  const getRowSelectionRef = computed((): TableRowSelection | null => {
    const { rowSelection } = unref(propsRef);
    if (!rowSelection) {
      return null;
    }

    return {
      preserveSelectedRowKeys: true,
      // selectedRowKeys: unref(selectedKeys),
      // onChange: (selectedRowKeys: string[]) => {
      //   setSelectedRowKeys(selectedRowKeys);
      // },
      ...omit(rowSelection, ['onChange', 'selectedRowKeys']),
    };
  });

  // 是否是单选
  const isRadio = computed(() => {
    return getRowSelectionRef.value?.type === 'radio';
  });

  const getAutoCreateKey = computed(() => {
    return unref(propsRef).autoCreateKey && !unref(propsRef).rowKey;
  });

  // 列key字段
  const getRowKey = computed(() => {
    const { rowKey } = unref(propsRef);
    return unref(getAutoCreateKey) ? ROW_KEY : rowKey;
  });
  // 获取行的key字段数据
  const getRecordKey = (record) => {
    if (!getRowKey.value) {
      return record[ROW_KEY];
    } else if (isFunction(getRowKey.value)) {
      return getRowKey.value(record);
    } else {
      return record[getRowKey.value];
    }
  };

  // 分页配置
  const getPagination = computed<PaginationProps>(() => {
    return typeof getPaginationRef.value === 'boolean' ? {} : getPaginationRef.value;
  });
  // 当前页条目数量
  const currentPageSize = computed(() => {
    const { pageSize = 10, total = flattedData.value.length } = getPagination.value;
    return pageSize > total ? total : pageSize;
  });

  // 选择列表头props
  const selectHeaderProps = computed(() => {
    return {
      onSelectAll,
      isRadio: isRadio.value,
      selectedLength: flattedData.value.filter((data) => selectedKeys.value.includes(getRecordKey(data))).length,
      // update-begin--author:liaozhiyang---date:20240511---for：【QQYUN-9289】解决表格条数不足pageSize数量时行数全部勾选但是全选框不勾选
      // 【TV360X-53】为空时会报错，加强判断
      pageSize: tableData.value?.length ?? 0,
      // update-end--author:liaozhiyang---date:20240511---for：【QQYUN-9289】解决表格条数不足pageSize数量时行数全部勾选但是全选框不勾选
      // 【QQYUN-6774】解决checkbox禁用后全选仍能勾选问题
      disabled: flattedData.value.length == 0,
      hideSelectAll: unref(propsRef)?.rowSelection?.hideSelectAll,
    };
  });

  // 监听传入的selectedRowKeys
  // update-begin--author:liaozhiyang---date:20240306---for：【QQYUN-8390】部门人员组件点击重置未清空（selectedRowKeys.value=[]，watch没监听到加deep）
  watch(
    () => unref(propsRef)?.rowSelection?.selectedRowKeys,
    (val: string[]) => {
      // 解决selectedRowKeys在页面调用处使用ref失效
      const value = unref(val);
      if (Array.isArray(value) && !sameArray(value, selectedKeys.value)) {
        setSelectedRowKeys(value);
      }
    },
    {
      immediate: true,
      deep: true
    }
  );
  // update-end--author:liaozhiyang---date:20240306---for：【QQYUN-8390】部门人员组件点击重置未清空（selectedRowKeys.value=[]，watch没监听到加deep）

  /**
  * 2024-03-06
  * liaozhiyang
  * 判断是否同一个数组 (引用地址，长度，元素位置信息相同才是同一个数组。数组元素只有字符串)
  */
  function sameArray(a, b) {
    if (a === b) {
      if (a.length === b.length) {
        return a.toString() === b.toString();
      } else {
        return false;
      }
    } else {
      // update-begin--author:liaozhiyang---date:20240425---for：【QQYUN-9123】popupdict打开弹窗打开程序运行
      if (isEqual(a, b)) {
        return true;
      }
      // update-end--author:liaozhiyang---date:20240425---for：【QQYUN-9123】popupdict打开弹窗打开程序运行
      return false;
    }
  }

  // 当任意一个变化时，触发同步检测
  watch([selectedKeys, selectedRows], () => {
    nextTick(() => {
      syncSelectedRows();
    });
  });

  // 监听滚动条事件
  const onScrollTopChange = throttle((e) => (scrollTop.value = e?.target?.scrollTop), 150);

  let bodyResizeObserver: Nullable<ResizeObserver> = null;
  // 获取首行行高
  watchEffect(() => {
    if (bodyEl.value) {
      // 监听div高度变化
      bodyResizeObserver = new ResizeObserver((entries) => {
        for (let entry of entries) {
          if (entry.target === bodyEl.value && entry.contentRect) {
            const { height } = entry.contentRect;
            bodyHeight.value = Math.ceil(height);
          }
        }
      });
      bodyResizeObserver.observe(bodyEl.value);
      const el = bodyEl.value?.querySelector('tbody.ant-table-tbody tr.ant-table-row') as HTMLDivElement;
      if (el) {
        rowHeight.value = el.offsetHeight;
        return;
      }
    }
    rowHeight.value = 50;
    // 这种写法是为了监听到 size 的变化
    propsRef.value.size && void 0;
  });

  onMountedOrActivated(async () => {
    bodyEl.value = await getTableBody(wrapRef.value!);
    bodyEl.value.addEventListener('scroll', onScrollTopChange);
  });
  onUnmounted(() => {
    if (bodyEl.value) {
      bodyEl.value?.removeEventListener('scroll', onScrollTopChange);
    }
    if (bodyResizeObserver != null) {
      bodyResizeObserver.disconnect();
    }
  });

  // 选择全部
  function onSelectAll(checked: boolean) {
    // update-begin--author:liaozhiyang---date:20231122---for：【issues/5577】BasicTable组件全选和取消全选时不触发onSelectAll事件
    if (unref(propsRef)?.rowSelection?.onSelectAll) {
      allSelected = checked;
      changeRows = getInvertRows(selectedRows.value);
    }
    // update-end--author:liaozhiyang---date:20231122---for：【issues/5577】BasicTable组件全选和取消全选时不触发onSelectAll事件
    // 取消全选
    if (!checked) {
      // update-begin--author:liaozhiyang---date:20240510---for：【issues/1173】取消全选只是当前页面取消
      // selectedKeys.value = [];
      // selectedRows.value = [];
      // emitChange('all');
      flattedData.value.forEach((item) => {
        updateSelected(item, false);
      });
      // update-end--author:liaozhiyang---date:20240510---for：【issues/1173】取消全选只是当前页面取消
      return;
    }
    let modal: Nullable<ReturnType<ModalFunc>> = null;
    // 全选
    const checkAll = () => {
      if (modal != null) {
        modal.update({
          content: '正在分批全选，请稍后……',
          cancelButtonProps: { disabled: true },
        });
      }
      let showCount = 0;
      // 最小选中数量
      let minSelect = 100;
      const hidden: Recordable[] = [];
      flattedData.value.forEach((item, index, array) => {
        if (array.length > 120) {
          if (showCount <= minSelect && recordIsShow(index, Math.max((minSelect - 10) / 2, 3))) {
            showCount++;
            updateSelected(item, checked);
          } else {
            hidden.push(item);
          }
        } else {
          updateSelected(item, checked);
        }
      });
      if (hidden.length > 0) {
        return batchesSelectAll(hidden, checked, minSelect);
      } else {
        emitChange('all');
      }
    };

    // 当数据量大于120条时，全选会导致页面卡顿，需进行慢速全选
    if (flattedData.value.length > 120) {
      modal = createConfirm({
        title: '全选',
        content: '当前数据量较大，全选可能会导致页面卡顿，确定要执行此操作吗？',
        iconType: 'warning',
        onOk: () => checkAll(),
      });
    } else {
      checkAll();
    }
  }

  // 分批全选
  function batchesSelectAll(hidden: Recordable[], checked: boolean, minSelect: number) {
    return new Promise<void>((resolve) => {
      (function call() {
        // 每隔半秒钟，选择100条数据
        setTimeout(() => {
          const list = hidden.splice(0, minSelect);
          if (list.length > 0) {
            list.forEach((item) => {
              updateSelected(item, checked);
            });
            call();
          } else {
            setTimeout(() => {
              emitChange('all');
              // update-begin--author:liaozhiyang---date:20230811---for：【QQYUN-5687】批量选择，提示成功后，又来一个提示
              setTimeout(() =>resolve(), 0);
              // update-end--author:liaozhiyang---date:20230811---for：【QQYUN-5687】批量选择，提示成功后，又来一个提示
            }, 500);
          }
        }, 300);
      })();
    });
  }

  // 选中单个
  function onSelect(record, checked) {
    updateSelected(record, checked);
    emitChange();
  }

  function updateSelected(record, checked) {
    const recordKey = getRecordKey(record);
    if (isRadio.value) {
      selectedKeys.value = [recordKey];
      selectedRows.value = [record];
      return;
    }
    const index = selectedKeys.value.findIndex((key) => key === recordKey);
    if (checked) {
      if (index === -1) {
        selectedKeys.value.push(recordKey);
        selectedRows.value.push(record);
      }
    } else {
      if (index !== -1) {
        selectedKeys.value.splice(index, 1);
        selectedRows.value.splice(index, 1);
      }
    }
  }

  // 调用用户自定义的onChange事件
  function emitChange(mode = 'single') {
    const { rowSelection } = unref(propsRef);
    if (rowSelection) {
      const { onChange } = rowSelection;
      if (onChange && isFunction(onChange)) {
        setTimeout(() => {
          onChange(selectedKeys.value, selectedRows.value);
        }, 0);
      }
    }
    emit('selection-change', {
      keys: getSelectRowKeys(),
      rows: getSelectRows(),
    });
    // update-begin--author:liaozhiyang---date:20231122---for：【issues/5577】BasicTable组件全选和取消全选时不触发onSelectAll事件
    if (mode == 'all') {
      const rowSelection = unref(propsRef)?.rowSelection;
      if (rowSelection?.onSelectAll) {
        rowSelection.onSelectAll(allSelected, toRaw(getSelectRows()), toRaw(changeRows));
      }
    }
    // update-end--author:liaozhiyang---date:20231122---for：【issues/5577】BasicTable组件全选和取消全选时不触发
  }

  // 用于判断是否是自定义选择列
  function isCustomSelection(column: BasicColumn) {
    return column.key === CUS_SEL_COLUMN_KEY;
  }

  /**
   * 判断当前行是否可视，虚拟滚动用
   * @param index 行下标
   * @param threshold 前后阈值，默认可视区域前后显示3条
   */
  function recordIsShow(index: number, threshold = 3) {
    // 只有数据量大于50条时，才会进行虚拟滚动
    const isVirtual = flattedData.value.length > 50;
    if (isVirtual) {
      // 根据 scrollTop、bodyHeight、rowHeight 计算出当前行是否可视（阈值前后3条）
      // flag1 = 判断当前行是否在可视区域上方3条
      const flag1 = scrollTop.value - rowHeight.value * threshold < index * rowHeight.value;
      // flag2 = 判断当前行是否在可视区域下方3条
      const flag2 = index * rowHeight.value < scrollTop.value + bodyHeight.value + rowHeight.value * threshold;
      // 全部条件满足时，才显示当前行
      return flag1 && flag2;
    }
    return true;
  }

  // 自定义渲染Body
  function bodyCustomRender(params) {
    const { index } = params;
    // update-begin--author:liaozhiyang---date:20231009--for：【issues/776】显示100条/页，复选框只能显示3个的问题
    if (propsRef.value.canResize && !recordIsShow(index)) {
      return '';
    }
    if (isRadio.value) {
      return renderRadioComponent(params);
    } else {
      return renderCheckboxComponent(params);
    }
    // update-end--author:liaozhiyang---date:20231009---for：【issues/776】显示100条/页，复选框只能显示3个的问题
  }

  /**
   * 渲染checkbox组件
   */
  function renderCheckboxComponent({ record }) {
    const recordKey = getRecordKey(record);
    // 获取用户自定义checkboxProps
    const checkboxProps = ((getCheckboxProps) => {
      if (typeof getCheckboxProps === 'function') {
        try {
          return getCheckboxProps(record) ?? {};
        } catch (error) {
          console.error(error);
        }
      }
      return {};
    })(propsRef.value.rowSelection?.getCheckboxProps);
    return (
      <Checkbox
        {...checkboxProps}
        key={'j-select__' + recordKey}
        checked={selectedKeys.value.includes(recordKey)}
        onUpdate:checked={(checked) => onSelect(record, checked)}
        // update-begin--author:liaozhiyang---date:20230326---for：【QQYUN-8694】BasicTable在使用clickToRowSelect=true下，selection-change 事件在触发多次
        onClick={(e) => e.stopPropagation()}
        // update-end--author:liaozhiyang---date:20230326---for：【QQYUN-8694】BasicTable在使用clickToRowSelect=true下，selection-change 事件在触发多次
      />
    );
  }

  /**
   * 渲染radio组件
   */
  function renderRadioComponent({ record }) {
    const recordKey = getRecordKey(record);
    // update-begin--author:liaozhiyang---date:20231016---for：【QQYUN-6794】table列表增加radio禁用功能
    // 获取用户自定义radioProps
    const checkboxProps = (() => {
      const rowSelection = propsRef.value.rowSelection;
      if (rowSelection?.getCheckboxProps) {
        return rowSelection.getCheckboxProps(record);
      }
      return {};
    })();
    // update-end--author:liaozhiyang---date:20231016---for：【QQYUN-6794】table列表增加radio禁用功能
    return (
      <Radio
        {...checkboxProps}
        key={'j-select__' + recordKey}
        checked={selectedKeys.value.includes(recordKey)}
        onUpdate:checked={(checked) => onSelect(record, checked)}
        // update-begin--author:liaozhiyang---date:20230326---for：【QQYUN-8694】BasicTable在使用clickToRowSelect=true下，selection-change 事件在触发多次
        onClick={(e) => e.stopPropagation()}
        // update-end--author:liaozhiyang---date:20230326---for：【QQYUN-8694】BasicTable在使用clickToRowSelect=true下，selection-change 事件在触发多次
      />
    );
  }

  // 创建选择列
  function handleCustomSelectColumn(columns: BasicColumn[]) {
    // update-begin--author:liaozhiyang---date:20230919---for：【issues/757】JPopup表格的选择列固定配置不生效
    const rowSelection = propsRef.value.rowSelection;
    if (!rowSelection) {
      return;
    }
    const isFixedLeft = rowSelection.fixed || columns.some((item) => item.fixed === 'left');
    // update-begin--author:liaozhiyang---date:20230919---for：【issues/757】JPopup表格的选择列固定配置不生效
    columns.unshift({
      title: '选择列',
      flag: 'CHECKBOX',
      key: CUS_SEL_COLUMN_KEY,
      width: 50,
      minWidth: 50,
      maxWidth: 50,
      align: 'center',
      ...(isFixedLeft ? { fixed: 'left' } : {}),
      customRender: bodyCustomRender,
    });
  }

  // 清空所有选择
  function clearSelectedRowKeys() {
    onSelectAll(false);
  }

  // 通过 selectedKeys 同步 selectedRows
  function syncSelectedRows() {
    if (selectedKeys.value.length !== selectedRows.value.length) {
      setSelectedRowKeys(selectedKeys.value);
    }
  }

  // 设置选择的key
  function setSelectedRowKeys(rowKeys: string[]) {
    const isSomeRowKeys = selectedKeys.value === rowKeys;
    selectedKeys.value = rowKeys;
    const allSelectedRows = findNodeAll(
      toRaw(unref(flattedData)).concat(toRaw(unref(selectedRows))),
      (item) => rowKeys.includes(getRecordKey(item)),
      {
        children: propsRef.value.childrenColumnName ?? 'children',
      }
    );
    const trueSelectedRows: any[] = [];
    rowKeys.forEach((key: string) => {
      const found = allSelectedRows.find((item) => getRecordKey(item) === key);
      found && trueSelectedRows.push(found);
    });
    // update-begin--author:liaozhiyang---date:20231103---for：【issues/828】解决卡死问题
    if (!(isSomeRowKeys && equal(selectedRows.value, trueSelectedRows))) {
      selectedRows.value = trueSelectedRows;
      emitChange();
    }
    // update-end--author:liaozhiyang---date:20231103---for：【issues/828】解决卡死问题
  }
  /**
   *2023-11-03
   *廖志阳
   *检测selectedRows.value和trueSelectedRows是否相等，防止死循环
   */
  function equal(oldVal, newVal) {
    let oldKeys = [],
      newKeys = [];
    if (oldVal.length === newVal.length) {
      oldKeys = oldVal.map((item) => getRecordKey(item));
      newKeys = newVal.map((item) => getRecordKey(item));
      for (let i = 0, len = oldKeys.length; i < len; i++) {
        const findItem = newKeys.find((item) => item === oldKeys[i]);
        if (!findItem) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  /**
   *2023-11-22
   *廖志阳
   *根据全选或者反选返回源数据中这次需要变更的数据
   */
  function getInvertRows(rows: any): any {
    const allRows = findNodeAll(toRaw(unref(flattedData)), () => true, {
      children: propsRef.value.childrenColumnName ?? 'children',
    });
    if (rows.length === 0 || rows.length === allRows.length) {
      return allRows;
    } else {
      const allRowsKey = allRows.map((item) => getRecordKey(item));
      rows.forEach((rItem) => {
        const rItemKey = getRecordKey(rItem);
        const findIndex = allRowsKey.findIndex((item) => rItemKey === item);
        if (findIndex != -1) {
          allRowsKey.splice(findIndex, 1);
          allRows.splice(findIndex, 1);
        }
      });
    }
    return allRows;
  }
  function getSelectRows<T = Recordable>() {
    return unref(selectedRows) as T[];
  }

  function getSelectRowKeys() {
    return unref(selectedKeys);
  }

  function getRowSelection() {
    return unref(getRowSelectionRef)!;
  }

  function deleteSelectRowByKey(key: string) {
    const index = selectedKeys.value.findIndex((item) => item === key);
    if (index !== -1) {
      selectedKeys.value.splice(index, 1);
      selectedRows.value.splice(index, 1);
    }
  }

  // 【QQYUN-5837】动态计算 expandIconColumnIndex
  const getExpandIconColumnIndex = computed(() => {
    const { expandIconColumnIndex } = unref(propsRef);
    // 未设置选择列，则保持不变
    if (getRowSelectionRef.value == null) {
      return expandIconColumnIndex;
    }
    // 设置了选择列，并且未传入 index 参数，则返回 1
    if (expandIconColumnIndex == null) {
      return 1;
    }
    return expandIconColumnIndex;
  });

  return {
    getRowSelection,
    getRowSelectionRef,
    getSelectRows,
    getSelectRowKeys,
    setSelectedRowKeys,
    deleteSelectRowByKey,
    selectHeaderProps,
    isCustomSelection,
    handleCustomSelectColumn,
    clearSelectedRowKeys,
    getExpandIconColumnIndex,
  };
}

function getTableBody(wrap: HTMLDivElement) {
  return new Promise<HTMLDivElement>((resolve) => {
    (function fn() {
      const bodyEl = wrap.querySelector('.ant-table-wrapper .ant-table-body') as HTMLDivElement;
      if (bodyEl) {
        resolve(bodyEl);
      } else {
        setTimeout(fn, 100);
      }
    })();
  });
}

function flattenData<RecordType>(data: RecordType[] | undefined, childrenColumnName: string): RecordType[] {
  let list: RecordType[] = [];
  (data || []).forEach((record) => {
    list.push(record);

    if (record && typeof record === 'object' && childrenColumnName in record) {
      list = [...list, ...flattenData<RecordType>((record as any)[childrenColumnName], childrenColumnName)];
    }
  });

  return list;
}

