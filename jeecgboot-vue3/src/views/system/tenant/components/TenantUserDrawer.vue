<template>
  <BasicDrawer @register="registerDrawer" :title="title" :width="580" destroyOnClose @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>

<script lang="ts">
  import { defineComponent, ref, unref, computed } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicForm, useForm } from '/@/components/Form';
  import { getUserDepartList } from '../../user/user.api';
  import { tenantUserSchema } from '../tenant.data';
  import { saveOrUpdateTenantUser } from '../tenant.api';

  export default defineComponent({
    name: 'TenantUserDrawer',
    components: {
      BasicDrawer,
      BasicForm,
    },
    emits: ['success', 'register'],
    setup(_p, { emit }) {
      const status = ref<string>('');
      const isUpdate = ref(false);
      const title = computed(() => {
        return isUpdate.value ? '编辑人员' : '添加人员';
      });

      //表单
      const [registerForm, { setFieldsValue, resetFields, validate, setProps, clearValidate }] = useForm({
        schemas: tenantUserSchema,
        showActionButtonGroup: false,
        labelCol: { span: 24 },
        wrapperCol: { span: 24 },
      });

      const showFooter = ref<boolean>(true);
      // modal
      const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(async (data) => {
        isUpdate.value = data.isUpdate;
        await resetFields();
        showFooter.value = data?.showFooter ?? true;
        setDrawerProps({ showFooter: showFooter.value });
        if (unref(isUpdate)) {
          const userDepart = await getUserDepartList({ userId: data.record.id });
          let departData: any = '';
          if (userDepart && userDepart.length > 0) {
            departData = userDepart.map((item) => item.value);
          }
          let formData = {
            ...data.record,
            selecteddeparts: departData,
            selectedroles: data.record.selectedroles,
          };
          status.value = data.status;
          await setFieldsValue(formData);
        }
        // 隐藏底部时禁用整个表单
        setProps({ disabled: !data?.showFooter });
        if(!data?.showFooter){
          await clearValidate();
        }
      });

      const confirmLoading = ref<boolean>(false);

      //提交事件
      async function handleSubmit() {
        const data: any = await validate();
        if (!data.username) {
          data.username = data.phone;
        }
        data.password = '123456';
        confirmLoading.value = true;
        await saveOrUpdateTenantUser(data, isUpdate.value);
        confirmLoading.value = false;
        emit('success');
        handleClose();
      }

      /**
       * 取消
       */
      function handleClose() {
        closeDrawer();
      }

      return {
        isUpdate,
        title,
        registerForm,
        registerDrawer,
        handleSubmit,
        handleClose,
        status,
        confirmLoading,
      };
    },
  });
</script>
