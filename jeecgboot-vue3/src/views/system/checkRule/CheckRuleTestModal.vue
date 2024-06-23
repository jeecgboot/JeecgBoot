<template>
  <BasicModal v-bind="$attrs" :okButtonProps="okButtonProps" @register="registerModal" destroyOnClose>
    <BasicForm @register="registerForm" />
    <div style="display: flex; flex-flow: row wrap">
      <div style="padding: 0 4px" v-for="(str, index) of realTestValue" :key="index">
        <a-row>
          <a-col style="text-align: center">
            <a-input :value="str" style="text-align: center; width: 40px" />
          </a-col>
          <a-col style="text-align: center">{{ index + 1 }}</a-col>
        </a-row>
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { checkRuleInput } from '/@/views/system/checkRule/check.rule.data';
  import { ref } from 'vue';
  let realTestValue = ref('');
  const okButtonProps = {
    style: { display: 'none' },
  };
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
    schemas: checkRuleInput,
    showActionButtonGroup: false,
    labelCol: {
      span: 24,
    },
    wrapperCol: {
      span: 24,
    },
  });

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    realTestValue.value = '';
    setModalProps({
      confirmLoading: false,
      cancelText: '关闭',
      title: '功能测试',
      width: '1000px',
    });
    await setFieldsValue({
      ruleCode: data.ruleCode,
      testValue: realTestValue,
    });
  });
</script>

<style scoped></style>
