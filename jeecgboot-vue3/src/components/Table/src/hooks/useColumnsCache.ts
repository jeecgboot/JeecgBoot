import { computed, nextTick, unref, watchEffect } from 'vue';
import { router } from '/@/router';
import { useRoute } from 'vue-router';
import { createLocalStorage } from '/@/utils/cache';
import { useTableContext } from './useTableContext';
import { useMessage } from '/@/hooks/web/useMessage';

/**
 * 列表配置缓存
 */
export function useColumnsCache(opt, setColumns, handleColumnFixed) {
  let isInit = false;
  const table = useTableContext();
  const $ls = createLocalStorage();
  const { createMessage: $message } = useMessage();
  const route = useRoute();
  // 列表配置缓存key
  const cacheKey = computed(() => {
    // update-begin--author:liaozhiyang---date:20240226---for：【QQYUN-8367】online报表配置列展示保存，影响到其他页面的table字段的显示隐藏（开发环境热更新会有此问题，生产环境无问题）
    const path = route.path;
    let key = path.replace(/[\/\\]/g, '_');
    // update-end--author:liaozhiyang---date:20240226---for：【QQYUN-8367】online报表配置列展示保存，影响到其他页面的table字段的显示隐藏（开发环境热更新会有此问题，生产环境无问题）
    let cacheKey = table.getBindValues.value.tableSetting?.cacheKey;
    if (cacheKey) {
      key += ':' + cacheKey;
    }
    return 'columnCache:' + key;
  });

  watchEffect(() => {
    const columns = table.getColumns();
    if (columns.length) {
      init();
    }
  });

  async function init() {
    if (isInit) {
      return;
    }
    isInit = true;
    let columnCache = $ls.get(cacheKey.value);
    if (columnCache && columnCache.checkedList) {
      const { checkedList, sortedList, sortableOrder, checkIndex } = columnCache;
      await nextTick();
      // checkbox的排序缓存
      opt.sortableOrder.value = sortableOrder;
      // checkbox的选中缓存
      opt.state.checkedList = checkedList;
      // tableColumn的排序缓存
      opt.plainSortOptions.value.sort((prev, next) => {
        return sortedList.indexOf(prev.value) - sortedList.indexOf(next.value);
      });
      // 重新排序tableColumn
      checkedList.sort((prev, next) => sortedList.indexOf(prev) - sortedList.indexOf(next));
      // 是否显示行号列
      if (checkIndex) {
        table.setProps({ showIndexColumn: true });
      }
      setColumns(checkedList);
      // 设置固定列
      setColumnFixed(columnCache);
    }
  }

  /** 设置被固定的列 */
  async function setColumnFixed(columnCache) {
    const { fixedColumns } = columnCache;
    const columns = opt.plainOptions.value;
    for (const column of columns) {
      let fixedCol = fixedColumns.find((fc) => fc.key === (column.key || column.dataIndex));
      if (fixedCol) {
        await nextTick();
        handleColumnFixed(column, fixedCol.fixed);
      }
    }
  }

  // 判断列固定状态
  const fixedReg = /^(true|left|right)$/;

  /** 获取被固定的列 */
  function getFixedColumns() {
    let fixedColumns: any[] = [];
    const columns = opt.plainOptions.value;
    for (const column of columns) {
      if (fixedReg.test((column.fixed ?? '').toString())) {
        fixedColumns.push({
          key: column.key || column.dataIndex,
          fixed: column.fixed === true ? 'left' : column.fixed,
        });
      }
    }
    return fixedColumns;
  }

  /** 保存列配置 */
  function saveSetting() {
    const { checkedList } = opt.state;
    const sortedList = unref(opt.plainSortOptions).map((item) => item.value);
    $ls.set(cacheKey.value, {
      // 保存的列
      checkedList,
      // 排序后的列
      sortedList,
      // 是否显示行号列
      checkIndex: unref(opt.checkIndex),
      // checkbox原始排序
      sortableOrder: unref(opt.sortableOrder),
      // 固定列
      fixedColumns: getFixedColumns(),
    });
    $message.success('保存成功');
    // 保存之后直接关闭
    opt.popoverVisible.value = false;
  }

  /** 重置（删除）列配置 */
  async function resetSetting() {
    // 重置固定列
    await resetFixedColumn();
    $ls.remove(cacheKey.value);
    $message.success('重置成功');
  }

  async function resetFixedColumn() {
    const columns = opt.plainOptions.value;
    for (const column of columns) {
      column.fixed;
      if (fixedReg.test((column.fixed ?? '').toString())) {
        await nextTick();
        handleColumnFixed(column, null);
      }
    }
  }

  return {
    saveSetting,
    resetSetting,
  };
}
