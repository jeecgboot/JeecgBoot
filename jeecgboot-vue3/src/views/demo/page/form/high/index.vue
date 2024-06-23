<template>
  <PageWrapper class="high-form" title="高级表单" content=" 高级表单常见于一次性输入和提交大批量数据的场景。">
    <a-card title="仓库管理" :bordered="false">
      <BasicForm @register="register" />
    </a-card>
    <a-card title="任务管理" :bordered="false" class="!mt-5">
      <BasicForm @register="registerTask" />
    </a-card>
    <a-card title="成员管理" :bordered="false">
      <PersonTable ref="tableRef" />
    </a-card>

    <template #rightFooter>
      <a-button type="primary" @click="submitAll"> 提交 </a-button>
    </template>
  </PageWrapper>
</template>
<script lang="ts">
  import { BasicForm, useForm } from '/@/components/Form';
  import { defineComponent, ref } from 'vue';
  import PersonTable from './PersonTable.vue';
  import { PageWrapper } from '/@/components/Page';
  import { schemas, taskSchemas } from './data';
  import { Card } from 'ant-design-vue';

  export default defineComponent({
    name: 'FormHightPage',
    components: { BasicForm, PersonTable, PageWrapper, [Card.name]: Card },
    setup() {
      const tableRef = ref<{ getDataSource: () => any } | null>(null);

      const [register, { validate }] = useForm({
        baseColProps: {
          span: 6,
        },
        labelWidth: 200,
        layout: 'vertical',
        schemas: schemas,
        showActionButtonGroup: false,
      });

      const [registerTask, { validate: validateTaskForm }] = useForm({
        baseColProps: {
          span: 6,
        },
        labelWidth: 200,
        layout: 'vertical',
        schemas: taskSchemas,
        showActionButtonGroup: false,
      });

      async function submitAll() {
        try {
          if (tableRef.value) {
            console.log('table data:', tableRef.value.getDataSource());
          }

          const [values, taskValues] = await Promise.all([validate(), validateTaskForm()]);
          console.log('form data:', values, taskValues);
        } catch (error) {}
      }

      return { register, registerTask, submitAll, tableRef };
    },
  });
</script>
<style lang="less" scoped>
  .high-form {
    padding-bottom: 48px;
  }

  /** update-begin-author:taoyan date:2022-5-16 for:/issues/63 下拉框z-index问题 */
  :deep(.ant-select-dropdown) {
    z-index: 98 !important;
  }
  /** update-end-author:taoyan date:2022-5-16 for:/issues/63 下拉框z-index问题 */
</style>
