<template>
  <BasicModal :title="title" :width="800" v-bind="$attrs" @ok="handleOk" @register="registerModal">
    <BasicForm @register="registerForm" >
      <template #depPostParentId="{ model, field }">
        <a-tree-select v-model:value="depPostValue" :treeData="treeData" allowClear treeCheckable @select="treeSelect">
          <template #title="{ orgCategory, title }">
            <TreeIcon :orgCategory="orgCategory" :title="title"></TreeIcon>
          </template>
          <template #tagRender="{option}">
            <span style="margin-left: 10px" v-if="orgNameMap[option.id]">{{orgNameMap[option.id]}}</span>
          </template>
        </a-tree-select>
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { watch, computed, inject, ref, unref, onMounted } from 'vue';

  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicModal, useModalInner } from '/@/components/Modal';

  import { saveOrUpdateDepart } from '../depart.api';
  import { useBasicFormSchema, orgCategoryOptions } from '../depart.data';
  import TreeIcon from "@/components/Form/src/jeecg/components/TreeIcon/TreeIcon.vue";
  import { getDepartPathNameByOrgCode } from "@/utils/common/compUtils";

  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    rootTreeData: { type: Array, default: () => [] },
  });
  const prefixCls = inject('prefixCls');
  // 当前是否是更新模式
  const isUpdate = ref<boolean>(false);
  // 当前的弹窗数据
  const model = ref<object>({});
  const title = computed(() => (isUpdate.value ? '编辑' : '新增'));
  const treeData = ref<any>([]);
  //上级岗位
  const depPostValue = ref<any>([]);
  //上级岗位名称映射
  const orgNameMap = ref<Record<string, string>>({});

  //注册表单
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: useBasicFormSchema(treeData).basicFormSchema,
    showActionButtonGroup: false,
  });

  // 注册弹窗
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    isUpdate.value = unref(data?.isUpdate);
    // 当前是否为添加子级
    let isChild = unref(data?.isChild);
    let categoryOptions = isChild ? orgCategoryOptions.child : orgCategoryOptions.root;
    
    if(data.record?.orgCategory && data.record?.orgCategory === '2'){
      categoryOptions = orgCategoryOptions.childDepartPost; 
    }
    if(data.record?.orgCategory && data.record?.orgCategory === '3'){
      categoryOptions = orgCategoryOptions.childPost; 
    }
    if(data.record?.depPostParentId){
      orgNameMap.value[data.record.depPostParentId] = await getDepartPathNameByOrgCode('', '', data.record.depPostParentId);
      depPostValue.value = [data.record.depPostParentId];
    }
    // 隐藏不需要展示的字段
    updateSchema([
      {
        field: 'parentId',
        show: isChild,
        componentProps: {
          // 如果是添加子部门，就禁用该字段
          disabled: isChild,
          treeData: props.rootTreeData,
        },
      },
      {
        field: 'orgCode',
        show: false,
      },
      {
        field: 'orgCategory',
        componentProps: { options: categoryOptions },
      },
    ]);

    let record = unref(data?.record);
    if (typeof record !== 'object') {
      record = {};
    }
    let orgCategory = data.record?.orgCategory;
    let company = orgCategory === '1' || orgCategory === '4';
    delete data.record?.orgCategory;
    // 赋默认值
    record = Object.assign(
      {
        departOrder: 0,
        orgCategory: company?categoryOptions[1].value:categoryOptions[0].value,
      },
      record
    );
    model.value = record;
    await setFieldsValue({ ...record });
  });

  // 提交事件
  async function handleOk() {
    try {
      setModalProps({ confirmLoading: true });
      let values = await validate();
      if(depPostValue.value && depPostValue.value.length > 0){
        values.depPostParentId = depPostValue.value[0];
      }else{
        values.depPostParentId = "";
      }
      //提交表单
      await saveOrUpdateDepart(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 树选中事件
   *
   * @param info
   * @param keys
   */
  async function treeSelect(keys,info) {
    if (info.checkable) {
      //解决闪动问题
      orgNameMap.value[info.id] = "";
      depPostValue.value = [info.value];
      orgNameMap.value[info.id] = await getDepartPathNameByOrgCode(info.orgCode,info.label,info.id);
    } else {
      depPostValue.value = [];
    }
  }
</script>

<style lang="less" scoped>
  :deep(.ant-select-selector .ant-select-selection-item){
    svg {
      display: none !important;
    }
  }
</style>