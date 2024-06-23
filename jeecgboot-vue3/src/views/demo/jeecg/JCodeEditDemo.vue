<template>
  <BasicForm
    :labelWidth="200"
    :schemas="schemas"
    :showResetButton="false"
    :showSubmitButton="false"
    :actionColOptions="{ span: 24 }"
    @submit="handleSubmit"
    @reset="handleReset"
    style="height: 800px"
  >
    <template #jCodeEdit="{ model, field }">
      <JCodeEditor v-model:value="model[field]" mode="js" height="300px" :fullScreen="true" />
    </template>
  </BasicForm>
</template>
<script lang="ts">
  import { computed, defineComponent, unref, ref } from 'vue';
  import { BasicForm, FormSchema, JCodeEditor } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { optionsListApi } from '/@/api/demo/select';
  import { useDebounceFn } from '@vueuse/core';

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'JCodeEditor',
      label: '代码编辑器',
      required: true,
      slot: 'jCodeEdit',
      colProps: {
        span: 15,
      },
      defaultValue: 'Hello JeecgBoot',
    },
  ];

  export default defineComponent({
    components: { BasicForm, JCodeEditor },
    setup() {
      const check = ref(null);
      const { createMessage } = useMessage();
      const keyword = ref<string>('');
      const searchParams = computed<Recordable>(() => {
        return { keyword: unref(keyword) };
      });

      function onSearch(value: string) {
        keyword.value = value;
      }

      return {
        schemas,
        optionsListApi,
        onSearch: useDebounceFn(onSearch, 300),
        searchParams,
        handleReset: () => {
          keyword.value = '';
        },
        handleSubmit: (values: any) => {
          createMessage.success('click search,values:' + JSON.stringify(values));
        },
        check,
      };
    },
  });
</script>
