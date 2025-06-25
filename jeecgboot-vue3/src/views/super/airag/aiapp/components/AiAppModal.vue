<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="800px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <template #title>
         <span style="display: flex">
          {{title}}
          <a-tooltip title="AI应用文档">
            <a style="color: unset" href="https://help.jeecg.com/aigc/guide/app" target="_blank">
              <Icon style="position:relative;left:2px;top:1px" icon="ant-design:question-circle-outlined"></Icon>
            </a>
          </a-tooltip>
        </span>
      </template>
      <BasicForm @register="registerForm">
        <template #typeSlot="{ model, field }">
          <a-radio-group v-model:value="model[field]" style="display: flex">
            <a-card
              v-for="item in appTypeOption"
              style="margin-right: 10px; cursor: pointer; width: 100%"
              @click="model[field] = item.value"
              :style="model[field] === item.value ? { borderColor: '#3370ff' } : {}"
            >
              <a-radio :value="item.value">
                <div class="type-title">{{ item.title }}</div>
                <div class="type-desc">{{ item.desc }}</div>
              </a-radio>
            </a-card>
          </a-radio-group>
        </template>
      </BasicForm>
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref, computed } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';

  import BasicForm from '@/components/Form/src/BasicForm.vue';
  import { useForm } from '@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { formSchema } from '../AiApp.data';
  import { initDictOptions } from '@/utils/dict';
  import { saveApp } from '@/views/super/airag/aiapp/AiApp.api';

  export default {
    name: 'AiAppModal',
    components: {
      BasicForm,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      //保存或修改
      const isUpdate = ref<boolean>(false);

      const title = computed<string>(() => isUpdate.value ? '修改应用' : '创建应用');

      //app类型
      const appTypeOption = ref<any>([]);

      //表单配置
      const [registerForm, { validate, resetFields, setFieldsValue }] = useForm({
        schemas: formSchema,
        showActionButtonGroup: false,
        layout: 'vertical',
        wrapperCol: { span: 24 },
      });

      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        await resetFields();
        //update-begin---author:wangshuai---date:2025-03-11---for: 【QQYUN-11324】8.修改弹窗head---
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          //表单赋值
          await setFieldsValue({
            ...data.record,
          });
        } else {
          await setFieldsValue({
            type: 'chatSimple',
          })
        }
        //update-end---author:wangshuai---date:2025-03-11---for:【QQYUN-11324】8.修改弹窗head---
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px' } });
      });

      /**
       * 保存
       */
      async function handleOk() {
        try {
          let values = await validate();
          setModalProps({ confirmLoading: true });
          let result = await saveApp(values);
          if (result) {
            //关闭弹窗
            closeModal();
            //update-begin---author:wangshuai---date:2025-03-11---for: 【QQYUN-11324】8.修改弹窗head---
            if(isUpdate.value){
              //刷新列表
              emit('success', values);
            }else{
              //刷新列表
              emit('success', result);
            }
            //update-end---author:wangshuai---date:2025-03-11---for: 【QQYUN-11324】8.修改弹窗head---
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      //初始化AI应用类型
      initAppTypeOption();

      function initAppTypeOption() {
        initDictOptions('ai_app_type').then((data) => {
          if (data && data.length > 0) {
            for (const datum of data) {
              if (datum.value === 'chatSimple') {
                datum['desc'] = '适合新手创建小助手';
              } else if (datum.value === 'chatFLow') {
                datum['desc'] = '适合高级用户自定义小助手的工作流';
              }
            }
          }
          appTypeOption.value = data;
        });
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
        appTypeOption,
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }
  .type-title {
    color: #1d2025;
    margin-bottom: 4px;
  }
  .type-desc {
    color: #8f959e;
    font-weight: 400;
  }
</style>
