<template>
  <BasicModal v-bind="$attrs" @register="registerModal" width="500px"  :title="title" @ok="handleSubmit" destroyOnClose>
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { formSchema } from '../UserSetting.data';
import { ref, unref, defineEmits } from 'vue';
import { userEdit } from "../UserSetting.api";
import { useUserStore } from "/@/store/modules/user";
import { useMessage } from "/@/hooks/web/useMessage";

const userStore = useUserStore();
const { createMessage } = useMessage();
const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
  schemas: formSchema,
  showActionButtonGroup: false,
});
const userDetail = ref<any>({});
const isUpdate = ref<boolean>(false);
const title = ref<string>('');
const emit = defineEmits(['register','success']);
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  await resetFields();
  setModalProps({ confirmLoading: false });
  title.value = '编辑个人资料';
  if(data.record.post){
    data.record.post = data.record.post.split(",")
  }
  if(data.record.relTenantIds){
    data.record.relTenantIds = data.record.relTenantIds.split(",")
  }
  userDetail.value = data.record;
  //update-begin---author:wangshuai ---date:20230130  for：[QQYUN-3976]个人信息 编辑时，刚注册没有设置生日 生日显示有问题------------
  if(data.record.birthday === '未填写'){
    data.record.birthday = undefined;
  }
  //update-end---author:wangshuai ---date:20230130  for：[QQYUN-3976]个人信息 编辑时，刚注册没有设置生日 生日显示有问题------------
  await setFieldsValue({ ...data.record });
});

/**
 * 数据修改和新增
 */
async function handleSubmit() {
  try {
    let values = await validate();
    setModalProps({ confirmLoading: true });
    //提交表单
    await userEdit(values).then((res) =>{
      if(res.success){
        createMessage.success(res.message)
      }else{
        createMessage.warn(res.message)
      }
    });
    //更新缓存
    Object.assign(userDetail.value,values)
    userStore.setUserInfo(unref(userDetail));
    emit('success')
    //关闭弹窗
    closeModal();
  } finally {
    setModalProps({ confirmLoading: false });
  }
}
</script>
