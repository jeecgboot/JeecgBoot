<template>
  <PageWrapper title="上传组件示例">
    <a-alert message="基础示例" />
    <BasicUpload :maxSize="20" :maxNumber="10" @change="handleChange" :api="uploadApi" class="my-5" :accept="['image/*']" />

    <a-alert message="嵌入表单,加入表单校验" />

    <BasicForm @register="register" class="my-5" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicUpload } from '/@/components/Upload';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form/index';
  import { PageWrapper } from '/@/components/Page';
  import { Alert } from 'ant-design-vue';
  import { uploadApi } from '/@/api/sys/upload';

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'Upload',
      label: '字段1',
      colProps: {
        span: 8,
      },
      rules: [{ required: true, message: '请选择上传文件' }],
      componentProps: {
        api: uploadApi,
      },
    },
  ];
  export default defineComponent({
    components: { BasicUpload, BasicForm, PageWrapper, [Alert.name]: Alert },
    setup() {
      const { createMessage } = useMessage();
      const [register] = useForm({
        labelWidth: 120,
        schemas,
        actionColOptions: {
          span: 16,
        },
      });
      return {
        handleChange: (list: string[]) => {
          createMessage.info(`已上传文件${JSON.stringify(list)}`);
        },
        uploadApi,
        register,
      };
    },
  });
</script>
