import { isFunction } from '/@/utils/is';
import type { BasicTableProps, TableRowSelection } from '../types/table';
import { computed, ComputedRef, nextTick, Ref, ref, toRaw, unref, watch } from 'vue';
import { ROW_KEY } from '../const';
import { omit } from 'lodash-es';
import { findNodeAll } from '/@/utils/helper/treeHelper';

export function useRowSelection(propsRef: ComputedRef<BasicTableProps>, tableData: Ref<Recordable[]>, emit: EmitType) {
  const selectedRowKeysRef = ref<string[]>([]);
  const selectedRowRef = ref<Recordable[]>([]);

  const getRowSelectionRef = computed((): TableRowSelection | null => {
    const { rowSelection } = unref(propsRef);
    if (!rowSelection) {
      return null;
    }

    return {
      // AntDV3.0 之后使用远程加载数据进行分页时，
      // 默认会清空上一页选择的行数据（导致无法跨页选择），
      // 将此属性设置为 true 即可解决。
      preserveSelectedRowKeys: true,
      selectedRowKeys: unref(selectedRowKeysRef),
      onChange: (selectedRowKeys: string[]) => {
        setSelectedRowKeys(selectedRowKeys);
      },
      ...omit(rowSelection, ['onChange']),
    };
  });

  watch(
    () => unref(propsRef).rowSelection?.selectedRowKeys,
    (v: string[]) => {
      setSelectedRowKeys(v);
    }
  );

  watch(
    () => unref(selectedRowKeysRef),
    () => {
      nextTick(() => {
        const { rowSelection } = unref(propsRef);
        if (rowSelection) {
          const { onChange } = rowSelection;
          if (onChange && isFunction(onChange)) onChange(getSelectRowKeys(), getSelectRows());
        }
        //update-begin---author:scott ---date:2023-06-19  for：【issues/503】table行选择时卡顿明显 #503---
        //table行选择时卡顿明显 #503
        if (unref(tableData).length > 0) {
          emit('selection-change', {
            keys: getSelectRowKeys(),
            rows: getSelectRows(),
          });
        }
        //update-end---author:scott ---date::2023-06-19  for：【issues/503】table行选择时卡顿明显 #503---
      });
    },
    { deep: true }
  );

  const getAutoCreateKey = computed(() => {
    return unref(propsRef).autoCreateKey && !unref(propsRef).rowKey;
  });

  const getRowKey = computed(() => {
    const { rowKey } = unref(propsRef);
    return unref(getAutoCreateKey) ? ROW_KEY : rowKey;
  });

  function setSelectedRowKeys(rowKeys: string[]) {
    selectedRowKeysRef.value = rowKeys;
    const allSelectedRows = findNodeAll(
      toRaw(unref(tableData)).concat(toRaw(unref(selectedRowRef))),
      (item) => rowKeys.includes(item[unref(getRowKey) as string]),
      {
        children: propsRef.value.childrenColumnName ?? 'children',
      }
    );
    const trueSelectedRows: any[] = [];
    rowKeys.forEach((key: string) => {
      const found = allSelectedRows.find((item) => item[unref(getRowKey) as string] === key);
      found && trueSelectedRows.push(found);
    });
    selectedRowRef.value = trueSelectedRows;
  }

  function setSelectedRows(rows: Recordable[]) {
    selectedRowRef.value = rows;
  }

  function clearSelectedRowKeys() {
    selectedRowRef.value = [];
    selectedRowKeysRef.value = [];
  }

  function deleteSelectRowByKey(key: string) {
    const selectedRowKeys = unref(selectedRowKeysRef);
    const index = selectedRowKeys.findIndex((item) => item === key);
    if (index !== -1) {
      unref(selectedRowKeysRef).splice(index, 1);
    }
  }

  function getSelectRowKeys() {
    return unref(selectedRowKeysRef);
  }

  function getSelectRows<T = Recordable>() {
    // const ret = toRaw(unref(selectedRowRef)).map((item) => toRaw(item));
    return unref(selectedRowRef) as T[];
  }

  function getRowSelection() {
    return unref(getRowSelectionRef)!;
  }

  return {
    getRowSelection,
    getRowSelectionRef,
    getSelectRows,
    getSelectRowKeys,
    setSelectedRowKeys,
    clearSelectedRowKeys,
    deleteSelectRowByKey,
    setSelectedRows,
  };
}
