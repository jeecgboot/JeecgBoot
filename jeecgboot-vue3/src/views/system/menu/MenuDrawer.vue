<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" showFooter :width="adaptiveWidth" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" class="menuForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, useAttrs } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema, ComponentTypes } from './menu.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { list, saveOrUpdateMenu } from './menu.api';
  import { useDrawerAdaptiveWidth } from '/@/hooks/jeecg/useAdaptiveWidth';
  import { useI18n } from "/@/hooks/web/useI18n";
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const { adaptiveWidth } = useDrawerAdaptiveWidth();
  const attrs = useAttrs();
  const isUpdate = ref(true);
  const menuType = ref(0);
  const isButton = (type) => type === 2;
  const [registerForm, { setProps, resetFields, setFieldsValue, updateSchema, validate, clearValidate }] = useForm({
    labelCol: {
      md: { span: 4 },
      sm: { span: 6 },
    },
    wrapperCol: {
      md: { span: 20 },
      sm: { span: 18 },
    },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await resetFields();
    setDrawerProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    menuType.value = data?.record?.menuType;

    //获取下拉树信息
    const treeData = await list();
    updateSchema([
      {
        field: 'parentId',
        // update-begin--author:liaozhiyang---date:20240306---for：【QQYUN-8379】菜单管理页菜单国际化
        componentProps: { treeData: translateMenu(treeData, 'name') },
        // update-end--author:liaozhiyang---date:20240306---for：【QQYUN-8379】菜单管理页菜单国际化
      },
      {
        field: 'name',
        label: isButton(unref(menuType)) ? '按钮/权限' : '菜单名称',
      },
      {
        field: 'url',
        required: !isButton(unref(menuType)),
        componentProps: {
          onChange: (e) => onUrlChange(e.target.value),
        },
      },
    ]);

    // 无论新增还是编辑，都可以设置表单值
    if (typeof data.record === 'object') {
      let values = { ...data.record };
      setFieldsValue(values);
      onUrlChange(values.url);
    }
    //按钮类型情况下，编辑时候清除一下地址的校验
    if (menuType.value == 2) {
      clearValidate();
    }
    //禁用表单
    setProps({ disabled: !attrs.showFooter });
  });
  //获取弹窗标题
  const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));
  //提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      // iframe兼容
      if (ComponentTypes.IFrame === values.component) {
        values.component = values.frameSrc;
      }
      setDrawerProps({ confirmLoading: true });
      //提交表单
      await saveOrUpdateMenu(values, unref(isUpdate));
      closeDrawer();
      emit('success');
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }

  /** url 变化时，动态设置组件名称placeholder */
  function onUrlChange(url) {
    let placeholder = '';
    let httpUrl = url;
    if (url != null && url != '') {
      if (url.startsWith('/')) {
        url = url.substring(1);
      }
      url = url.replaceAll('/', '-');
      // 特殊标记
      url = url.replaceAll(':', '@');
      placeholder = `${url}`;
    } else {
      placeholder = '请输入组件名称';
    }
    updateSchema([{ field: 'componentName', componentProps: { placeholder } }]);
    //update-begin---author:wangshuai ---date:20230204  for：[QQYUN-4058]菜单添加智能化处理------------
    if (httpUrl != null && httpUrl != '') {
      if (httpUrl.startsWith('http://') || httpUrl.startsWith('https://')) {
        setFieldsValue({ component: httpUrl });
      }
    }
    //update-end---author:wangshuai ---date:20230204  for：[QQYUN-4058]菜单添加智能化处理------------
  }

  /**
  * 2024-03-06
  * liaozhiyang
  * 翻译菜单名称
  */
  function translateMenu(data, key) {
    if (data?.length) {
      const { t } = useI18n();
      data.forEach((item) => {
        if (item[key]) {
          if (item[key].includes("t('") && t) {
            item[key] = new Function('t', `return ${item[key]}`)(t);
          }
        }
        if (item.children?.length) {
          translateMenu(item.children, key);
        }
      });
    }
    return data;
  }
</script>
