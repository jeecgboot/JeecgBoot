<template>
  <BasicForm
    ref="formElRef"
    :class="'jee-select-demo-form'"
    :labelCol="{ span: 6 }"
    :wrapperCol="{ span: 14 }"
    :showResetButton="false"
    :showSubmitButton="false"
    :schemas="schemas"
    :actionColOptions="{ span: 24 }"
    @submit="handleSubmit"
    @reset="handleReset"
    style="height: 100%"
  >
    <template #jAreaLinkage="{ model, field }">
      <JAreaLinkage v-model:value="model[field]" :showArea="true" :showAll="false" />
    </template>
    <template #jAreaLinkage1="{ model, field }">
      <JAreaLinkage :disabled="isDisabledAuth(['demo.dbarray'])" v-model:value="model[field]" :showArea="true" :showAll="false" />
    </template>
    <template #JPopup="{ model, field }">
      <JPopup v-model:value="model[field]" :formElRef="formElRef" code="report_user" :fieldConfig="[{ source: 'username', target: 'pop1' }]" />
    </template>
    <template #JAreaSelect="{ model, field }">
      <JAreaSelect v-model:value="model[field]" />
    </template>
    <template #JCheckbox="{ model, field }">
      <JCheckbox v-model:value="model[field]" dictCode="remindMode" />
    </template>
    <template #JInput="{ model, field }">
      <JInput v-model:value="model[field]" :type="model['jinputtype']" />
    </template>
    <template #dargVerify="{ model, field }">
      <BasicDragVerify v-model:value="model[field]" />
    </template>
    <template #superQuery="{ model, field }">
      <super-query :config="superQueryConfig" @search="(value)=>handleSuperQuery(value, model, field)"/>
    </template>
    <template #superQuery1="{ model, field }">
      <super-query :config="superQueryConfig" @search="(value)=>handleSuperQuery(value, model, field)" :isCustomSave="true" :saveSearchData="saveSearchData" :save="handleSuperQuerySave"/>
    </template>
  </BasicForm>
</template>
<script lang="ts">
  import { computed, defineComponent, unref, ref } from 'vue';
  import { BasicForm, ApiSelect, JAreaLinkage, JPopup, JAreaSelect, FormActionType, JCheckbox, JInput, JEllipsis } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { optionsListApi } from '/@/api/demo/select';
  import { useDebounceFn } from '@vueuse/core';
  import { schemas } from './jeecgComponents.data';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { BasicDragVerify } from '/@/components/Verify';

  export default defineComponent({
    components: {
      BasicForm,
      ApiSelect,
      JAreaLinkage,
      JPopup,
      JAreaSelect,
      JCheckbox,
      JInput,
      JEllipsis,
      BasicDragVerify,
    },
    name: 'JeecgComponents',
    setup() {
      const { isDisabledAuth } = usePermission();
      const check = ref(null);
      const formElRef = ref<Nullable<FormActionType>>(null);
      const { createMessage } = useMessage();
      const keyword = ref<string>('');
      const submitButtonOptions = ref({
        text: '确定',
      });
      const searchParams = computed<Recordable>(() => {
        return { keyword: unref(keyword) };
      });

      function onSearch(value: string) {
        keyword.value = value;
      }
      
      const superQueryConfig = {
        name:{ title: "名称", view: "text", type: "string", order: 1 },
        birthday:{ title: "生日", view: "date", type: "string", order: 2 },
        age:{ title: "年龄", view: "number", type: "number", order: 4 },
        sex:{ title: "性别", view: "list", type: "string", dictCode: "sex", order: 5 },
        bpmStatus:{ title: "流程状态", view: "list_multi", type: "string",  dictCode: "bpm_status", order: 6 },
      }
      function handleSuperQuery(value, model, field){
        if(value){
          let str = decodeURI(value.superQueryParams)
          console.log(str)
          model[field] = str
        }
      }
      const saveSearchData = ref([
        {
          content: '[{"field":"age","rule":"eq","val":14}]',
          title: '豆蔻年华',
          type: 'and',
        },
        {
          content: '[{"field":"name","rule":"eq","val":"张三"}]',
          title: '项目经理',
          type: 'and',
        },
      ]);
      const handleSuperQuerySave = (data) => {
        // 高级查询保存后的信息
        return new Promise<void>((resolve, reject) => {
          // 模拟接口
          setTimeout(() => {
            if (Math.random() > 0.5) {
              console.log('接口成功~');
              saveSearchData.value = data;
              resolve();
            } else {
              console.log('接口失败~');
              reject();
            }
          }, 1e3);
        });
      }
      return {
        schemas,
        formElRef,
        isDisabledAuth,
        optionsListApi,
        submitButtonOptions,
        onSearch: useDebounceFn(onSearch, 300),
        searchParams,
        superQueryConfig,
        handleSuperQuery,
        handleReset: () => {
          keyword.value = '';
        },
        handleSubmit: (values: any) => {
          console.log('values:', values);
          createMessage.success('click search,values:' + JSON.stringify(values));
        },
        check,
        handleSuperQuerySave,
        saveSearchData,
      };
    },
  });
</script>
<style lang="less" scoped>
  /**update-begin-author:taoyan date:20220324 for: VUEN-351【vue3】展示不全*/
  .jee-select-demo-form .ant-col-5 {
    flex: 0 0 159px;
    max-width: 159px;
  }
  /**update-end-author:taoyan date:20220324 for: VUEN-351【vue3】展示不全*/
</style>
