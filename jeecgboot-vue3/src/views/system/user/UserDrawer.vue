<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    :title="getTitle"
    :width="adaptiveWidth"
    @ok="handleSubmit"
    :showFooter="showFooter"
    destroyOnClose
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { defineComponent, ref, computed, unref, useAttrs } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './user.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { saveOrUpdateUser, getUserRoles, getUserDepartList, getAllRolesListNoByTenant, getAllRolesList } from './user.api';
  import { useDrawerAdaptiveWidth } from '/@/hooks/jeecg/useAdaptiveWidth';
  import { getTenantId } from "/@/utils/auth";

  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const attrs = useAttrs();
  const isUpdate = ref(true);
  const rowId = ref('');
  const departOptions = ref([]);
  //表单配置
  const [registerForm, { setProps, resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    labelWidth: 90,
    schemas: formSchema,
    showActionButtonGroup: false,
  });
  // TODO [VUEN-527] https://www.teambition.com/task/6239beb894b358003fe93626
  const showFooter = ref(true);
  //表单赋值
  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await resetFields();
    showFooter.value = data?.showFooter ?? true;
    setDrawerProps({ confirmLoading: false, showFooter: showFooter.value });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      rowId.value = data.record.id;
      //租户信息定义成数组
      if (data.record.relTenantIds && !Array.isArray(data.record.relTenantIds)) {
        data.record.relTenantIds = data.record.relTenantIds.split(',');
      } else {
        //【issues/I56C5I】用户管理中连续点两次编辑租户配置就丢失了
        //data.record.relTenantIds = [];
      }

      //查角色/赋值/try catch 处理，不然编辑有问题
      try {
        const userRoles = await getUserRoles({ userid: data.record.id });
        if (userRoles && userRoles.length > 0) {
          data.record.selectedroles = userRoles;
        }
      } catch (error) {}

      //查所属部门/赋值
      const userDepart = await getUserDepartList({ userId: data.record.id });
      if (userDepart && userDepart.length > 0) {
        data.record.selecteddeparts = userDepart;
        let selectDepartKeys = Array.from(userDepart, ({ key }) => key);
        data.record.selecteddeparts = selectDepartKeys.join(',');
        departOptions.value = userDepart.map((item) => {
          return { label: item.title, value: item.key };
        });
      }
      //负责部门/赋值
      data.record.departIds && !Array.isArray(data.record.departIds) && (data.record.departIds = data.record.departIds.split(','));
      //update-begin---author:zyf   Date:20211210  for：避免空值显示异常------------
      //update-begin---author:liusq   Date:20231008  for：[issues/772]避免空值显示异常------------
      data.record.departIds =  (!data.record.departIds || data.record.departIds == '') ? [] : data.record.departIds;
      //update-end-----author:liusq   Date:20231008  for：[issues/772]避免空值显示异常------------
      //update-begin---author:zyf   Date:20211210  for：避免空值显示异常------------
    }
    //处理角色用户列表情况(和角色列表有关系)
    data.selectedroles && (await setFieldsValue({ selectedroles: data.selectedroles }));
    //编辑时隐藏密码/角色列表隐藏角色信息/我的部门时隐藏所属部门
    updateSchema([
      {
        field: 'password',
        // 【QQYUN-8324】
        ifShow: !unref(isUpdate),
      },
      {
        field: 'confirmPassword',
        ifShow: !unref(isUpdate),
      },
      {
        field: 'selectedroles',
        show: !data.isRole,
      },
      {
        field: 'departIds',
        componentProps: { options: departOptions },
      },
      {
        field: 'selecteddeparts',
        show: !data?.departDisabled ?? false,
      },
      {
        field: 'selectedroles',
        show: !data?.departDisabled ?? false,
        //update-begin---author:wangshuai ---date:20230424  for：【issues/4844】多租户模式下，新增或编辑用户，选择角色一栏，角色选项没有做租户隔离------------
        //判断是否为多租户模式
        componentProps:{
          api: data.tenantSaas?getAllRolesList:getAllRolesListNoByTenant
        }
        //update-end---author:wangshuai ---date:20230424  for：【issues/4844】多租户模式下，新增或编辑用户，选择角色一栏，角色选项没有做租户隔离------------
      },
      //update-begin---author:wangshuai ---date:20230522  for：【issues/4935】租户用户编辑界面中租户下拉框未过滤，显示当前系统所有的租户------------
      {
        field: 'relTenantIds',
        componentProps:{
          disabled: !!data.tenantSaas,
        },
      },
      //update-end---author:wangshuai ---date:20230522  for：【issues/4935】租户用户编辑界面中租户下拉框未过滤，显示当前系统所有的租户------------
    ]);
    //update-begin---author:wangshuai ---date:20230522  for：【issues/4935】租户用户编辑界面中租户下拉框未过滤，显示当前系统所有的租户------------
    if(!unref(isUpdate) && data.tenantSaas){
      await setFieldsValue({ relTenantIds: getTenantId().toString() })
    }
    //update-end---author:wangshuai ---date:20230522  for：【issues/4935】租户用户编辑界面中租户下拉框未过滤，显示当前系统所有的租户------------
    // 无论新增还是编辑，都可以设置表单值
    if (typeof data.record === 'object') {
      setFieldsValue({
        ...data.record,
      });
    }
    // 隐藏底部时禁用整个表单
    //update-begin-author:taoyan date:2022-5-24 for: VUEN-1117【issue】0523周开源问题
    setProps({ disabled: !showFooter.value });
    //update-end-author:taoyan date:2022-5-24 for: VUEN-1117【issue】0523周开源问题
  });
  //获取标题
  const getTitle = computed(() => {
    // update-begin--author:liaozhiyang---date:20240306---for：【QQYUN-8389】系统用户详情抽屉title更改
    if (!unref(isUpdate)) {
      return '新增用户';
    } else {
      return unref(showFooter) ? '编辑用户' : '用户详情';
    }
    // update-end--author:liaozhiyang---date:20240306---for：【QQYUN-8389】系统用户详情抽屉title更改
  });
  const { adaptiveWidth } = useDrawerAdaptiveWidth();

  //提交事件
  async function handleSubmit() {
    try {
      let values = await validate();
      setDrawerProps({ confirmLoading: true });
      values.userIdentity === 1 && (values.departIds = '');
      let isUpdateVal = unref(isUpdate);
      //提交表单
      await saveOrUpdateUser(values, isUpdateVal);
      //关闭弹窗
      closeDrawer();
      //刷新列表
      emit('success',{isUpdateVal ,values});
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
