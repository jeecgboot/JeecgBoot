import { computed } from 'vue';
import { router } from '/@/router';
import { createLocalStorage } from '/@/utils/cache';
import { useMessage } from '/@/hooks/web/useMessage';

export function useColumnsCache({ cacheColumnsKey, refs }: any) {
  const $ls = createLocalStorage();
  const { createMessage: $message } = useMessage();
  const cacheKey = computed(() => {
    const path = router.currentRoute.value.fullPath;
    let key = path.replace(/[\/\\]/g, '_');
    if (cacheColumnsKey) {
      key += ':' + cacheColumnsKey;
    }
    return 'vxe-columnCache:' + key;
  });
  const initSetting = (props) => {
    const columnCache = $ls.get(cacheKey.value);
    if (columnCache) {
      columnCache.forEach((key) => {
        const column = props.columns.find((item) => item.key === key);
        if (column) {
          column.visible = false;
        }
      });
    }
  };
  // const initSetting = (refs) => {
  //   let columnCache = $ls.get(cacheKey.value);
  //   if (columnCache) {
  //     const $grid = refs.gridRef.value!.getRefMaps().refTable.value;
  //     console.log('refs.gridRef', $grid);
  //     const { fullColumn } = $grid.getTableColumn();
  //     const hideColumns = getHideColumn(fullColumn, columnCache);
  //     if (hideColumns?.length) {
  //       hideColumns.forEach((column) => {
  //         $grid.hideColumn(column);
  //       });
  //     }
  //   }
  //   console.log(columnCache);
  // };
  function saveSetting($grid: any) {
    console.log($grid);
    const { fullColumn, visibleColumn } = $grid.getTableColumn();
    const hideColumnKey = getHideColumnKey(fullColumn, visibleColumn);
    if (hideColumnKey.length) {
      $ls.set(cacheKey.value, hideColumnKey);
      $message.success('保存成功');
    }
  }
  const resetSetting = ($grid) => {
    const columnCache = $ls.get(cacheKey.value);
    if (columnCache) {
      const { fullColumn } = $grid.getTableColumn();
      const hideColumns = getHideColumn(fullColumn, columnCache);
      if (hideColumns?.length) {
        hideColumns.forEach((column) => {
          if (columnCache.includes(column?.params?.key)) {
            $grid.showColumn(column);
          }
        });
      }
    }
    $ls.remove(cacheKey.value);
    $message.success('重置成功');
  };
  const getHideColumn = (fullColumn, columnCache) => {
    const result: any = [];
    if (columnCache?.length) {
    console.log('--fullColumn:',fullColumn);
      columnCache.forEach((key) => {
        const column = fullColumn.find((item) => item?.params?.key === key);
        if (column) {
          result.push(column);
        }
      });
    }
    return result;
  };
  const getHideColumnKey = (fullColumn, visibleColumn) => {
    const reuslt: any = [];
    if (fullColumn.length === visibleColumn.length) {
      return reuslt;
    } else {
      fullColumn.forEach((item) => {
        const fKey = item?.params?.key;
        if (fKey) {
          const vItem = visibleColumn.find((item) => {
            return item?.params?.key === fKey;
          });
          if (!vItem) {
            reuslt.push(fKey);
          }
        }
      });
      return reuslt;
    }
  };
  return {
    initSetting,
    resetSetting,
    saveSetting,
  };
}
