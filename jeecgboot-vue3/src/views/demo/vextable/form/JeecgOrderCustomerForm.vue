<template>
  <BasicForm @register="registerForm"></BasicForm>
</template>

<script>
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { computed, defineComponent, toRaw } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { getOrderCustomerFormSchema } from '../data';

  export default defineComponent({
    name: 'JeecgOrderCustomerForm',
    components: {
      BasicForm,
    },
    props: {
      formData: propTypes.object.def({}),
    },
    setup(props) {
      const [registerForm, { setFieldsValue, setProps, getFieldsValue, updateSchema }] = useForm({
        labelWidth: 150,
        schemas: getOrderCustomerFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: { span: 8 },
      });

      const formDisabled = computed(() => {
        if (props.formData.disabled === false) {
          return false;
        }
        return true;
      });

      let orderCustomerFormData = {};
      const queryByIdUrl = '/test/jeecgOrderMain/queryOrderCustomerListByMainId';
      async function initFormData(mainId) {
        let params = { id: mainId };
        const data = await defHttp.get({ url: queryByIdUrl, params });
        console.log('data', data);
        if (data && data.length > 0) {
          let temp = data[0];
          orderCustomerFormData = { ...temp };
          //设置表单的值
          await setFieldsValue(orderCustomerFormData);
          await setProps({ disabled: formDisabled.value });
        }
      }
      async function getFormData() {
        let subFormData = { ...orderCustomerFormData };
        if (Object.keys(subFormData).length > 0) {
          return subFormData;
        }
        return false;
      }

      return {
        registerForm,
        formDisabled,
        initFormData,
        getFormData,
      };
    },
  });
</script>

<style scoped></style>
