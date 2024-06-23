<template>
  <div class="step1">
    <div class="step1-form">
      <BasicForm @register="register">
        <template #fac="{ model, field }">
          <a-input-group compact>
            <a-select v-model:value="model['pay']" class="pay-select">
              <a-select-option value="zfb"> 支付宝 </a-select-option>
              <a-select-option value="yl"> 银联 </a-select-option>
            </a-select>
            <a-input class="pay-input" v-model:value="model[field]" />
          </a-input-group>
        </template>
      </BasicForm>
    </div>
    <a-divider />
    <h3>说明</h3>
    <h4>转账到支付宝账户</h4>
    <p>
      如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。
    </p>

    <h4>转账到银行卡</h4>
    <p>
      如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。
    </p>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form';
  import { step1Schemas } from './data';

  import { Select, Input, Divider } from 'ant-design-vue';
  export default defineComponent({
    components: {
      BasicForm,
      [Select.name]: Select,
      ASelectOption: Select.Option,
      [Input.name]: Input,
      [Input.Group.name]: Input.Group,
      [Divider.name]: Divider,
    },
    emits: ['next'],
    setup(_, { emit }) {
      const [register, { validate }] = useForm({
        labelWidth: 200,
        schemas: step1Schemas,
        actionColOptions: {
          span: 14,
        },
        showResetButton: false,
        submitButtonOptions: {
          text: '下一步',
        },
        submitFunc: customSubmitFunc,
      });

      async function customSubmitFunc() {
        try {
          const values = await validate();
          emit('next', values);
        } catch (error) {}
      }

      return { register };
    },
  });
</script>
<style lang="less" scoped>
  .step1 {
    &-form {
      width: 550px;
      margin: 0 auto;
    }

    h3 {
      margin: 0 0 12px;
      font-size: 16px;
      line-height: 32px;
      color: @text-color;
    }

    h4 {
      margin: 0 0 4px;
      font-size: 14px;
      line-height: 22px;
      color: @text-color;
    }

    p {
      color: @text-color;
    }
  }

  .pay-select {
    width: 20%;
  }

  .pay-input {
    width: 70%;
  }
</style>
