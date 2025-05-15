<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
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
  import { formSchema } from '../AiKnowledgeBase.data';
  import { saveKnowledge, editKnowledge, queryById } from '../AiKnowledgeBase.api';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default {
    name: 'KnowledgeBaseModal',
    components: {
      BasicForm,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('创建知识库');

      //保存或修改
      const isUpdate = ref<boolean>(false);

      //表单配置
      const [registerForm, { resetFields, setFieldsValue, validate, clearValidate, updateSchema }] = useForm({
        schemas: formSchema,
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
        title.value = isUpdate.value ? '编辑知识库' : '创建知识库';
        if (unref(isUpdate)) {
          let values = await queryById({ id: data.id });
          //表单赋值
          await setFieldsValue({
            ...values.result,
          });
        }
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px' } });
      });

      /**
       * 保存
       */
      async function handleOk() {
        try {
          setModalProps({ confirmLoading: true });
          let values = await validate();
          if (!unref(isUpdate)) {
            await saveKnowledge(values);
          } else {
            await editKnowledge(values);
          }
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
