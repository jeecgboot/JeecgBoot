import type { ComputedRef } from 'vue';
import type { BasicTableProps, TableCustomRecord } from '../types/table';
import { unref } from 'vue';
import { isFunction } from '/@/utils/is';
import { ROW_KEY } from '/@/components/Table/src/const';

export function useTableStyle(propsRef: ComputedRef<BasicTableProps>, prefixCls: string) {
  /**
   * 2024-09-19
   * liaozhiyang
   * 【issues/7200】basicTable选中后没有选中样式
   * */
  const isChecked = (propsRef, record) => {
    const getAutoCreateKey = () => {
      return unref(propsRef).autoCreateKey && !unref(propsRef).rowKey;
    };
    const getRowKey = () => {
      const { rowKey } = unref(propsRef);
      return getAutoCreateKey() ? ROW_KEY : rowKey;
    };
    // 获取行的key字段数据
    const getRecordKey = (record) => {
      const key = getRowKey();
      if (!key) {
        return record[ROW_KEY];
      } else if (isFunction(key)) {
        return key(record);
      } else {
        return record[key];
      }
    };
    const { rowSelection } = unref(propsRef);
    if (rowSelection?.selectedRowKeys?.length) {
      return rowSelection.selectedRowKeys.includes(getRecordKey(record));
    }
    return false;
  };

  function getRowClassName(record: TableCustomRecord, index: number) {
    const { striped, rowClassName } = unref(propsRef);
    const classNames: string[] = [];
    if (striped) {
      classNames.push((index || 0) % 2 === 1 ? `${prefixCls}-row__striped` : '');
    }
    if (rowClassName && isFunction(rowClassName)) {
      classNames.push(rowClassName(record, index));
    }
    // update-begin--author:liaozhiyang---date:20240919---for：【issues/7200】basicTable选中后没有选中样式
    if (isChecked(propsRef, record)) {
      classNames.push('ant-table-row-selected');
    }
    // update-end--author:liaozhiyang---date:20240919---for：【issues/7200】basicTable选中后没有选中样式
    return classNames.filter((cls) => !!cls).join(' ');
  }

  return { getRowClassName };
}
