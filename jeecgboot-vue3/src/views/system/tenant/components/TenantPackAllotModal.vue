<!--  用户套餐分配页面 -->
<template>
  <BasicModal @register="registerModal" :width="500" title="分配套餐" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #packId="{ model, field }">
        <a-checkbox-group v-model:value="model[field]" :options="packOption"></a-checkbox-group>
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { packUserAllotSchemas } from '@/views/system/tenant/tenant.data';
  import { listPackByTenantUserId } from '@/views/system/tenant/tenant.api';
  import { getTenantId } from '@/utils/auth';
  import { ref, defineComponent } from 'vue';

  export default defineComponent({
    name: 'TenantCurrentPackList',
    components: {
      BasicModal,
      BasicForm,
    },
    setup() {
      //产品包的option
      const packOption = ref<any>([]);
      //表单配置
      const [registerForm, { resetFields, setFieldsValue, setProps }] = useForm({
        schemas: packUserAllotSchemas,
        showActionButtonGroup: false,
      });
      //表单赋值
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false, showCancelBtn: false, showOkBtn: false });
        //重置表单
        await resetFields();
        let tenantId = getTenantId();
        let result = await listPackByTenantUserId({ tenantId: tenantId, userId: data.record.id });
        if (result) {
          if (result.packList) {
            for (const item of result.packList) {
              if (item.packCode) {
                item.label = item.packName + "(默认产品包)";
              } else {
                item.label = item.packName;
              }
              item.value = item.id;
              item.key = item.id;
            }
            packOption.value = result.packList;
          } else {
            packOption.value = [];
          }
          if(result.userPackIdList){
            data.record.packId = result.userPackIdList;
          }
        }
        setModalProps({ confirmLoading: false });
        await setFieldsValue({
          ...data.record,
          userId: data.record.id,
        });
        setProps({ disabled: true });
      })

      return {
        registerForm,
        registerModal,
        packOption,
      };
    },
  });
</script>

<style scoped lang="less"></style>
