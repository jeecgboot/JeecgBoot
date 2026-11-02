<template>
  <JVxeTable
    ref="tableRef"
    toolbar
    rowNumber
    rowSelection
    dragSort
    sortKey="orderNum"
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :height="398"
  >
    <template #toolbarSuffix>
      <a-dropdown>
        <template #overlay>
          <a-menu>
            <a-menu-item key="1" @click="fieldNameConvertCase('Upper', '大写')">
              <a-icon type="sort-descending" />
              <span>转换为大写</span>
            </a-menu-item>
            <a-menu-item key="2" @click="fieldNameConvertCase('Lower', '小写')">
              <a-icon type="sort-ascending" />
              <span>转换为小写</span>
            </a-menu-item>
          </a-menu>
        </template>
        <a-button preIcon="ant-design:down">转换字段名</a-button>
      </a-dropdown>
    </template>
  </JVxeTable>
</template>

<script lang="ts">
  import { ref, defineComponent, nextTick, watch } from 'vue';
  import { JVxeTypes, JVxeColumn, JVxeTableInstance } from '/@/components/jeecg/JVxeTable/types';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'ParamsTable',
    props: {
      loading: Boolean,
      dataSource: Array,
      disableSearch: Boolean,
    },
    setup(props) {
      const tableRef = ref<JVxeTableInstance>();
      const columns = ref<JVxeColumn[]>([
        {
          title: '字段名',
          key: 'fieldName',
          width: 180,
          type: JVxeTypes.input,
          defaultValue: '',
          validateRules: [{ required: true, message: '${title}不能为空' }],
          placeholder: '请输入${title}',
        },
        {
          title: '字段文本',
          key: 'fieldTxt',
          width: 180,
          type: JVxeTypes.input,
          defaultValue: '',
          placeholder: '请输入${title}',
        },
        {
          title: '字段类型',
          key: 'fieldType',
          width: 200,
          type: JVxeTypes.select,
          options: [
            { title: '数值类型', value: 'Integer' },
            { title: '日期类型', value: 'Date' },
            { title: '字符类型', value: 'String' },
            { title: '长整型', value: 'Long' },
          ],
          defaultValue: '',
          placeholder: '请选择${title}',
        },
        {
          title: '计算总计',
          key: 'isTotal',
          width: 80,
          type: JVxeTypes.checkbox,
          defaultChecked: false,
          customValue: ['Y', 'N'],
        },
        {
          title: '是否查询',
          key: 'searchFlag',
          width: 80,
          type: JVxeTypes.checkbox,
          disabled: props.disableSearch,
          defaultChecked: false,
          customValue: ['Y', 'N'],
        },
        {
          title: '查询模式',
          key: 'searchMode',
          width: 180,
          type: JVxeTypes.select,
          options: [
            { title: '单条件查询', value: 'single' },
            { title: '范围查询', value: 'group' },
          ],
          defaultValue: '',
          placeholder: '请选择${title}',
        },
        {
          title: '字典Code',
          key: 'dictCode',
          width: 200,
          type: JVxeTypes.input,
          defaultValue: '',
          placeholder: '请选择${title}',
        },
        {
          title: '是否显示',
          key: 'isShow',
          type: JVxeTypes.checkbox,
          defaultChecked: true,
          customValue: ['Y', 'N'],
        },
      ]);
      const { createConfirm: $confirm } = useMessage();

      // update-begin--author:liaozhiyang---date:20260824---for：【LHZP-1055】online图表JSON方式禁用查询
      /** JSON 数据不支持查询条件，取消所有是否查询选项 */
      async function clearSearchFlags() {
        await nextTick();
        const values = tableRef.value?.getTableData() || [];
        tableRef.value?.setValues(
          values.map((item) => ({
            rowKey: item.id,
            values: { searchFlag: 'N' },
          }))
        );
      }

      watch(
        () => props.disableSearch,
        (disabled) => {
          const searchColumn = columns.value.find((column) => column.key === 'searchFlag');
          if (searchColumn) {
            searchColumn.disabled = disabled;
          }
          if (disabled) {
            clearSearchFlags();
          }
        },
        { immediate: true }
      );

      watch(
        () => props.dataSource,
        () => {
          if (props.disableSearch) {
            clearSearchFlags();
          }
        }
      );
      // update-end--author:liaozhiyang---date:20260824---for：【LHZP-1055】online图表JSON方式禁用查询

      /** 字段名转换大小写 */
      function fieldNameConvertCase(convertCase, name) {
        $confirm({
          iconType: 'info',
          title: '转换字段名',
          content: `确定要将所有的字段名都转换为${name}吗？`,
          onOk() {
            let fn = `to${convertCase}Case`;
            let values = tableRef.value!.getTableData();
            let newValues = values.map((item) => {
              return {
                rowKey: item.id,
                values: {
                  fieldName: item['fieldName'][fn](),
                },
              };
            });
            tableRef.value!.setValues(newValues);
          },
        });
      }

      return { tableRef, columns, fieldNameConvertCase };
    },
  });
</script>

<style scoped></style>
