<!--手动录入text-->
<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <BasicForm @register="registerForm"></BasicForm>
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';

  import BasicForm from '@/components/Form/src/BasicForm.vue';
  import { useForm } from '@/components/Form';
  import { docTextSchema } from '../AiKnowledgeBase.data';
  import { knowledgeSaveDoc, queryById } from '../AiKnowledgeBase.api';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default {
    name: 'AiragKnowledgeDocModal',
    components: {
      BasicForm,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('创建知识库');

      //保存或修改
      const isUpdate = ref<boolean>(false);
      //知识库id
      const knowledgeId = ref<string>();
      //表单配置
      const [registerForm, { resetFields, setFieldsValue, validate, clearValidate, updateSchema }] = useForm({
        schemas: docTextSchema,
        showActionButtonGroup: false,
        layout: 'vertical',
        wrapperCol: { span: 24 },
      });

      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        //重置表单
        await resetFields();
        setModalProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        title.value = isUpdate.value ? '编辑文档' : '创建文档';
        if (unref(isUpdate)) {
          if(data.record.type === 'file' && data.record.metadata){
            data.record.filePath = JSON.parse(data.record.metadata).filePath;
          }
          //表单赋值
          await setFieldsValue({
            ...data.record,
          });
        } else {
          knowledgeId.value = data.knowledgeId;
          await setFieldsValue({ type: data.type })
        }
        setModalProps({ bodyStyle: { padding: '10px' } });
      });

      /**
       * 保存
       */
      async function handleOk() {
        try {
          setModalProps({ confirmLoading: true });
          let values = await validate();
          if (!unref(isUpdate)) {
            values.knowledgeId = knowledgeId.value;
          }
          if(values.filePath){
            values.metadata = JSON.stringify({ filePath: values.filePath });
            delete values.filePath;
          }
          await knowledgeSaveDoc(values);
          //关闭弹窗
          closeModal();
          //刷新列表
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      /**
       * 取消
       */
      function handleCancel() {
        closeModal();
      }

      return {
        registerModal,
        registerForm,
        title,
        handleOk,
        handleCancel,
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }
</style>
