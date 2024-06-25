import { h } from 'vue';
import JVxeToolbar from '../components/JVxeToolbar.vue';
import { JVxeDataProps, JVxeTableMethods, JVxeTableProps } from '../types';

export function useToolbar(props: JVxeTableProps, data: JVxeDataProps, methods: JVxeTableMethods, $slots) {
  /** 渲染工具栏 */
  function renderToolbar() {
    if (props.toolbar) {
      return h(
        JVxeToolbar,
        {
          size: props.size,
          disabled: props.disabled,
          toolbarConfig: props.toolbarConfig,
          disabledRows: props.disabledRows,
          hasBtnAuth: methods.hasBtnAuth,
          selectedRowIds: data.selectedRowIds.value,
          custom: props.custom,
          // 新增事件
          onAdd: () => {
            // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-212】online新增字段就出校验提示
            setTimeout(() => {
              methods.addRows();
            }, 0);
            // update-end--author:liaozhiyang---date:20240521---for：【TV360X-212】online新增字段就出校验提示
          },
          // 保存事件
          onSave: () => methods.trigger('save'),
          onRemove() {
            const $table = methods.getXTable();
            // update-begin--author:liaozhiyang---date:20231018---for：【QQYUN-6805】修复asyncRemove字段不生效
            // 触发删除事件
            if (data.selectedRows.value.length > 0) {
              const deleteOldRows = methods.filterNewRows(data.selectedRows.value);
              const removeEvent: any = { deleteRows: data.selectedRows.value, $table };
              const insertRecords = $table.getInsertRecords();
              if (props.asyncRemove && deleteOldRows.length) {
                data.selectedRows.value.forEach((item) => {
                  // 删除新添加的数据id
                  if (insertRecords.includes(item)) {
                    delete item.id;
                  }
                });
                // 确认删除，只有调用这个方法才会真删除
                removeEvent.confirmRemove = () => methods.removeSelection();
              } else {
                if (props.asyncRemove) {
                  // asyncRemove删除的只有新增的数据时，防止调用confirmRemove报错
                  removeEvent.confirmRemove = () => {};
                }
                methods.removeSelection();
              }
              methods.trigger('removed', removeEvent);
            } else {
              methods.removeSelection();
            }
            // update-end--author:liaozhiyang---date:20231018---for：【QQYUN-6805】修复asyncRemove字段不生效
          },
          // 清除选择事件
          onClearSelection: () => methods.clearSelection(),
          onRegister: ({ xToolbarRef }) => methods.getXTable().connect(xToolbarRef.value),
        },
        {
          toolbarPrefix: $slots.toolbarPrefix,
          toolbarSuffix: $slots.toolbarSuffix,
        }
      );
    }
    return null;
  }

  return { renderToolbar };
}
