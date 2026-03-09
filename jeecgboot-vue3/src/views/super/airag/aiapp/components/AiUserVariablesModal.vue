<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="用户变量" :width="1000" @ok="handleSubmit" destroyOnClose>
    <div class="p-4">
      <div class="mb-4 text-gray-500"> 用于存储每个用户使用项目过程中，需要持久化存储和读取的数据，如用户的语言偏好、个性化设置等。 </div>

      <JVxeTable
        ref="tableRef"
        toolbar
        dragSort
        :maxHeight="500"
        :columns="columns"
        :dataSource="dataSource"
        :toolbarConfig="{ btns: ['remove', 'clearSelection'] }"
      >
        <template #toolbarSuffix>
          <a-button type="primary" @click="handleAdd">
            <Icon icon="ant-design:plus-outlined" />
            新增
          </a-button>
        </template>

        <template #action="props">
          <div class="action-group">
            <a-switch v-model:checked="props.row.enable" checked-children="开" un-checked-children="关" size="small" class="ml-2" />

            <a-popconfirm title="确定删除吗？" @confirm="handleDelete(props)">
              <Icon icon="ant-design:delete-outlined" class="cursor-pointer hover:text-red-500 ml-2" size="18" color="red" />
            </a-popconfirm>
          </div>
        </template>
      </JVxeTable>
    </div>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/src/components/Modal';
  import { Icon } from '/src/components/Icon';
  import { Button, Checkbox, Switch, Popconfirm } from 'ant-design-vue';
  import { JVxeTypes, JVxeColumn, JVxeTableInstance } from '/src/components/jeecg/JVxeTable/types';
  import { JVxeTable } from '/src/components/jeecg/JVxeTable';
  import { useMessage } from '/src/hooks/web/useMessage';

  export default defineComponent({
    name: 'AiUserVariablesModal',
    components: {
      BasicModal,
      Icon,
      JVxeTable,
      AButton: Button,
      ACheckbox: Checkbox,
      ASwitch: Switch,
      APopconfirm: Popconfirm,
    },
    emits: ['register', 'ok'],
    setup(props, { emit }) {
      const { createMessage } = useMessage();
      const tableRef = ref<JVxeTableInstance>();

      // 定义列配置
      const columns = ref<JVxeColumn[]>([
        {
          title: '名称',
          key: 'name',
          type: JVxeTypes.input,
          width: 200,
          placeholder: '请输入变量名',
          validateRules: [
            { required: true, message: '名称不能为空' },
            { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '仅支持字母、数字和下划线，且以字母开头' },
          ],
        },
        {
          title: '描述',
          key: 'description',
          type: JVxeTypes.input,
          width: 300,
          placeholder: '字段描述',
        },
        {
          title: '默认值',
          key: 'defaultValue',
          type: JVxeTypes.input,
          width: 200,
          placeholder: '默认值',
        },
        {
          title: '操作',
          key: 'action',
          type: JVxeTypes.slot,
          slotName: 'action',
          width: 220,
          align: 'center',
        },
      ]);

      const dataSource = ref<any[]>([]);

      const [registerModal, { setModalProps, closeModal }] = useModalInner((data) => {
        // 如果传入了已有数据，进行初始化
        if (data && data.variables) {
          dataSource.value = JSON.parse(data.variables);
        } else {
          dataSource.value = [];
        }
      });

      // 新增行
      const handleAdd = () => {
        if (dataSource.value.length > 9) {
          createMessage.warn('最多支持10个变量！');
          return;
        }
        tableRef.value?.addRows({
          name: '',
          description: '',
          defaultValue: '',
          enable: true,
        });
      };

      // 删除行
      const handleDelete = (props) => {
        tableRef.value?.removeRows(props.row);
      };

      const handleSubmit = async () => {
        // 校验表格
        const errMap = await tableRef.value?.validateTable();
        if (errMap) {
          createMessage.error('请检查表单填写是否正确');
          return;
        }

        // 获取全量数据
        const tableData = tableRef.value?.getTableData();
        console.log('保存用户变量:', tableData);

        closeModal();
        emit('ok', tableData);
      };

      return {
        registerModal,
        tableRef,
        columns,
        dataSource,
        handleAdd,
        handleDelete,
        handleSubmit,
      };
    },
  });
</script>

<style scoped>
  .p-4 {
    padding: 1rem;
  }

  .mb-4 {
    margin-bottom: 1rem;
  }

  .text-gray-500 {
    color: #6b7280;
  }

  .ml-2 {
    margin-left: 0.5rem;
  }

  .cursor-pointer {
    cursor: pointer;
  }

  .hover\:text-red-500:hover {
    color: #ef4444;
  }

  .action-group {
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
