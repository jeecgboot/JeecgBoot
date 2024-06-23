<template>
  <div>
    <b>键盘操作快捷键：</b>
    <div style="border: 1px solid #cccccc; padding: 8px; width: 740px">
      <pre>
             F2 | 如果存在，激活单元格为编辑状态
            Esc | 如果存在，取消单元格编辑状态
              ↑ | 如果存在，则移动到上面的单元格
              ↓ | 如果存在，则移动到下面的单元格
              ← | 如果存在，则移动到左边的单元格
              → | 如果存在，则移动到右边的单元格
            Tab | 如果存在，则移动到右边单元格；如果移动到最后一列，则从下一行开始移到，以此循环
    Shift + Tab | 如果存在，则移动到左边单元格，如果移动到第一列，则从上一行开始移到，以此循环
          Enter | 如果存在，取消单元格编辑并移动到下面的单元格
  Shift + Enter | 如果存在，取消单元格编辑并移动到上面的单元格</pre
      >
    </div>

    <JVxeTable ref="tableRef" stripe toolbar rowNumber rowSelection keyboardEdit :columns="columns" :dataSource="dataSource"> </JVxeTable>
  </div>
</template>
<script lang="ts">
  import { ref, onMounted, nextTick, defineComponent } from 'vue';
  import { Popconfirm } from 'ant-design-vue';
  import { JVxeTypes, JVxeColumn, JVxeTableInstance } from '/@/components/jeecg/JVxeTable/types';

  export default defineComponent({
    name: 'JVxeDemo5',
    components: { [Popconfirm.name]: Popconfirm },
    setup() {
      const tableRef = ref<JVxeTableInstance>();
      const columns = ref<JVxeColumn[]>([
        {
          title: '单行文本',
          key: 'input',
          type: JVxeTypes.input,
          width: 220,
          defaultValue: '',
          placeholder: '请输入${title}',
        },
        {
          title: '多行文本',
          key: 'textarea',
          type: JVxeTypes.textarea,
          width: 240,
        },
        {
          title: '数字',
          key: 'number',
          type: JVxeTypes.inputNumber,
          width: 120,
          defaultValue: 32,
        },
        {
          title: '日期时间',
          key: 'datetime',
          type: JVxeTypes.datetime,
          width: 240,
          defaultValue: '2019-04-30 14:51:22',
          placeholder: '请选择',
        },
        {
          title: '时间',
          key: 'time',
          type: JVxeTypes.time,
          width: 220,
          defaultValue: '14:52:22',
          placeholder: '请选择',
        },
        {
          title: '下拉框',
          key: 'select',
          type: JVxeTypes.select,
          width: 220,
          // 下拉选项
          options: [
            { title: 'String', value: 'string' },
            { title: 'Integer', value: 'int' },
            { title: 'Double', value: 'double' },
            { title: 'Boolean', value: 'boolean' },
          ],
          // allowInput: true,
          allowSearch: true,
          placeholder: '请选择',
        },
        {
          title: '复选框',
          key: 'checkbox',
          type: JVxeTypes.checkbox,
          // width: 100,
          customValue: ['Y', 'N'], // true ,false
          defaultChecked: false,
        },
      ]);
      const dataSource = ref([]);

      function handleView(props) {
        // 参数介绍：
        // props.value          当前单元格的值
        // props.row            当前行的数据
        // props.rowId          当前行ID
        // props.rowIndex       当前行下标
        // props.column         当前列的配置
        // props.columnIndex    当前列下标
        // props.$table         vxe-table实例，可以调用vxe-table内置方法
        // props.scrolling      是否正在滚动
        // props.reloadEffect   是否开启了数据刷新特效
        // props.triggerChange  触发change事件，用于更改slot的值
        console.log('props: ', props);
      }

      function handleDelete({ row }) {
        // 使用实例：删除当前操作的行
        tableRef.value?.removeRows(row);
      }

      onMounted(async () => {
        console.log(tableRef.value);
        await nextTick();
        // 默认添加五行数据
        tableRef.value!.addRows([{ input: 'input_1' }, { input: 'input_2' }, { input: 'input_3' }, { input: 'input_4' }, { input: 'input_5' }], {
          setActive: false,
        });
      });

      return { tableRef, columns, dataSource, handleView, handleDelete };
    },
  });
</script>
