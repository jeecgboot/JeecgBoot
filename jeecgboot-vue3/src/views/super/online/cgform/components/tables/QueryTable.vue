<template>
  <JVxeTable
    ref="tableRef"
    rowNumber
    keyboardEdit
    :maxHeight="tableHeight.noToolbar"
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :disabledRows="{ dbFieldName: ['id', 'has_child'] }"
    @valueChange="handleChange"
    v-bind="tableProps"
  />
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { JVxeColumn, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useTableSync } from '../../hooks/useTableSync';

  export default defineComponent({
    name: 'QueryTable',
    emits: ['query'],
    setup(_, { emit }) {
      // 定义列信息
      const columns = ref<JVxeColumn[]>([
        { title: '字段名称', key: 'dbFieldName', fixed: 'left', width: 130 },
        { title: '字段备注', key: 'dbFieldTxt', fixed: 'left', width: 130 },
        {
          title: '控件类型',
          key: 'queryShowType',
          width: 170,
          type: JVxeTypes.select,
          options: [
            { title: '文本框', value: 'text' },
            { title: '日期(yyyy-MM-dd)', value: 'date' },
            { title: '日期（yyyy-MM-dd HH:mm:ss）', value: 'datetime' },
            { title: '时间（HH:mm:ss）', value: 'time' },
            // update-begin--author:liaozhiyang---date:20240531---for：【TV360X-415】个性化查询支持年、月、周、季度
            // 虚拟的date_year、date_month、date_week、date_quarter其实走的还是date组件，为了查询
            { title: '日期-年', value: 'date_year' },
            { title: '日期-月', value: 'date_month' },
            { title: '日期-周', value: 'date_week' },
            { title: '日期-季度', value: 'date_quarter' },
            // update-end--author:liaozhiyang---date:20240531---for：【TV360X-415】个性化查询支持年、月、周、季度
            { title: '下拉框', value: 'list' },
            { title: '下拉多选框', value: 'list_multi' },
            { title: '下拉搜索框', value: 'sel_search' },
            { title: '分类字典树', value: 'cat_tree' },
            { title: 'Popup弹框', value: 'popup' },
            { title: '部门选择', value: 'sel_depart' },
            { title: '用户选择', value: 'sel_user' },
            { title: '省市区组件', value: 'pca' },
            { title: '自定义树控件', value: 'sel_tree' },
            // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-8】个性化配置控件类型增加开关组件
            { title: '开关', value: 'switch' },
            // update-end--author:liaozhiyang---date:20240521---for：【TV360X-8】个性化配置控件类型增加开关组件
            // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-415】个性化配置控件类型增加popup字典组件
            { title: 'Popup字典', value: 'popup_dict' },
            // update-end--author:liaozhiyang---date:20240529---for：【TV360X-415】个性化配置控件类型增加popup字典组件
            // update-begin--author:liaozhiyang---date:20260810---for：【单表@自定义查询】生成数据时正确展示关联记录和他表字段控件类型
            { title: '关联记录', value: 'link_table' },
            { title: '他表字段', value: 'link_table_field' },
            // update-end--author:liaozhiyang---date:20260810---for：【单表@自定义查询】生成数据时正确展示关联记录和他表字段控件类型
          ],
          defaultValue: 'text',
          placeholder: '请选择${title}',
          validateRules: [{ handler: validateQueryShowType }]
        },
        {
          title: '字典Table',
          key: 'queryDictTable',
          width: 130,
          type: JVxeTypes.textarea,
          defaultValue: '',
        },
        {
          title: '字典Code',
          key: 'queryDictField',
          width: 130,
          type: JVxeTypes.input,
          defaultValue: '',
        },
        {
          title: '字典Text',
          key: 'queryDictText',
          width: 130,
          type: JVxeTypes.input,
          defaultValue: '',
        },
        {
          title: '默认值',
          key: 'queryDefVal',
          width: 130,
          type: JVxeTypes.input,
          props: {
            allowClear: true,
          },
          defaultValue: '',
        },
        {
          title: '是否启用',
          key: 'queryConfigFlag',
          minWidth: 80,
          type: JVxeTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-816】如果是关联记录或者没勾选数据库字段个性化查询checkbox禁用
          props: {
            isDisabledCell({ row, column }) {
              let { pageTable, dbTable, fkTable } = tables;
              // 获取到 pageTable（页面属性） 中的数据
              const pageRowData = pageTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              if (['link_table'].includes(pageRowData?.fieldShowType)) {
                row.queryConfigFlag = '0';
                return true;
              }
              // 获取到 dbTable（数据库属性） 中的数据
              const dbRowData = dbTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              if (dbRowData?.dbIsPersist == '0') {
                row.queryConfigFlag = '0';
                return true;
              }
              // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-199】自定义查询禁用外键
              // 获取到 ForeignKeyTable（外键） 中的数据
              const fkRowData = fkTable.value!.tableRef!.getTableData({ rowIds: [row.id] })[0];
              if (fkRowData?.mainTable && fkRowData?.mainField) {
                return true;
              }
              // update-end--author:liaozhiyang---date:20240717---for：【TV360X-199】自定义查询禁用外键
              return false;
            },
          },
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-816】如果是关联记录或者没勾选数据库字段个性化查询checkbox禁用
        },
      ]);
      const setup = useTableSync(columns);
      const { tables } = setup;

      function handleChange({ row, column, value }) {
        if (column.key === 'queryConfigFlag') {
          //update-begin-author:taoyan date:2022-5-23 for: VUEN-1084 【vue3】online表单测试发现的新问题 3、高级查询，列表查询字段有问题，未按照查询配置展示
          if (value === '1') {
            emit('query', row.id);
          }
          //update-end-author:taoyan date:2022-5-23 for: VUEN-1084 【vue3】online表单测试发现的新问题 3、高级查询，列表查询字段有问题，未按照查询配置展示
        }
      }

      /**
       *  2024-05-27
       *  liaozhiyang
       *  【TV360X-307】查询启用状态下，控件类型必选~
       */
      function validateQueryShowType({ cellValue, row }, callback) {
        // 启用开始状态下，控件类型必填
        if (cellValue == null && row.queryConfigFlag == '1') {
          callback(false, '查询启用状态下，控件类型必选~');
        }
        callback(true);
      }
      return { ...setup, columns, handleChange };
    },
  });
</script>
