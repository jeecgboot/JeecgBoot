<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="800px">
    <!-- update-begin---author:wangshuai---date:2023-10-23---for:【QQYUN-6804】后台模式字典没有颜色配置---  -->
    <BasicForm @register="registerForm" >
      <template #itemColor="{ model, field }">
        <div class="item-tool">
          <div
              v-for="(item,index) in Colors"
              :style="{ color: item[0] }"
              :class="model.itemColor===item[0]?'item-active':''"
              class="item-color"
              @click="itemColorClick(item)">
            <div class="item-color-border"></div>
            <div class="item-back" :style="{ background: item[0] }"></div>
          </div>
        </div>
      </template>
    </BasicForm>
    <!-- update-end---author:wangshuai---date:2023-10-23---for:【QQYUN-6804】后台模式字典没有颜色配置---  -->
  </BasicModal>
</template>
<script lang="ts" setup>
  import { defineProps, ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/src/components/Modal';
  import { BasicForm, useForm } from '/src/components/Form';
  import { itemFormSchema } from '../dict.data';
  import { saveOrUpdateDictItem } from '../dict.api';
  import { Colors } from '/@/utils/dict/DictColors.js'
  
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({ dictId: String });
  const isUpdate = ref(true);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: itemFormSchema,
    showActionButtonGroup: false,
    mergeDynamicData: props,
    labelCol: {
      xs: { span: 24 },
      sm: { span: 4 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 18 },
    },
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    }
  });

  //设置标题
  const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      values.dictId = props.dictId;
      setModalProps({ confirmLoading: true });
      //提交表单
      await saveOrUpdateDictItem(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  
  /**
   * 字典颜色点击事件
   * 
   * @param index
   * @param item
   * @param model
   */
  function itemColorClick(item) {
    console.log(item)
    setFieldsValue({ itemColor: item[0] })
  }
  
</script>
<style lang="less" scoped>
   /*begin 字典颜色配置样式*/
  .item-tool{
    display: flex;
    flex-wrap: wrap;
    .item-color{
      width: 18px;
      display: flex;
      justify-content: center;
      cursor: pointer;
      align-items: center;
      margin-right: 10px;
    }
    .item-back{
      width: 18px;
      height: 18px;
      border-radius: 50%;
    }
  }
  .item-color-border{
    visibility: hidden;
  }
  .item-active .item-color-border{
    visibility: visible;
    position: absolute;
    border: 1px solid;
    width: 24px;
    height: 24px;
    border-radius: 50%;
  }
   /*end 字典颜色配置样式*/
</style>
