<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol" name="OpenApiAuthForm">
          <a-row>
						<a-col :span="24">
							<a-form-item label="授权名称" v-bind="validateInfos.name" id="OpenApiAuthForm-name" name="name">
								<a-input v-model:value="formData.name" placeholder="请输入授权名称"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="AK" v-bind="validateInfos.ak" id="OpenApiAuthForm-ak" name="ak">
								<a-input v-model:value="formData.ak" placeholder="请输入AK" disabled allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="SK" v-bind="validateInfos.sk" id="OpenApiAuthForm-sk" name="sk">
								<a-input v-model:value="formData.sk" placeholder="请输入SK" disabled allow-clear ></a-input>
							</a-form-item>
						</a-col>
<!--						<a-col :span="24">-->
<!--							<a-form-item label="关联系统用户名" v-bind="validateInfos.systemUserId" id="OpenApiAuthForm-systemUserId" name="systemUserId">-->
<!--								<JSearchSelect dict="sys_user,username,id" v-model:value="formData.systemUserId" placeholder="请输入关联系统用户名"  allow-clear ></JSearchSelect>-->
<!--							</a-form-item>-->
<!--						</a-col>-->
          </a-row>
        </a-form>
      </template>
    </JFormContainer>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, defineProps, computed,  } from 'vue';
  import { USER_INFO_KEY} from '/@/enums/cacheEnum';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getValueType } from '/@/utils';
  import { saveOrUpdate,getGenAKSK } from '../OpenApiAuth.api';
  import { Form } from 'ant-design-vue';
  import JFormContainer from '/@/components/Form/src/container/JFormContainer.vue';
  import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
  import { getAuthCache } from "@/utils/auth";
  const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({})},
    formBpm: { type: Boolean, default: true },
    title: { type: String, default: "" },
  });
  const formRef = ref();
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const formData = reactive<Record<string, any>>({
    id: '',
    name: '',   
    ak: '',   
    sk: '',   
    systemUserId: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = reactive({
    name:[{ required: true, message: '请输入授权名称!'},],
    systemUserId:[{ required: true, message: '请输入关联系统用户名!'},],
  });
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: false });

  // 表单禁用
  const disabled = computed(()=>{
    if(props.formBpm === true){
      if(props.formData.disabled === false){
        return false;
      }else{
        return true;
      }
    }
    return props.formDisabled;
  });

  
  /**
   * 新增
   */
  async function add() {
    edit({});
    const AKSKObj = await getGenAKSK({});
    formData.ak = AKSKObj[0];
    formData.sk = AKSKObj[1];
  }

  /**
   * 编辑
   */
  function edit(record) {
    const userData = getAuthCache(USER_INFO_KEY)
    if(props.title == "新增"){
      record.systemUserId = userData.id
    }
    nextTick(() => {
      resetFields();
      const tmpData = {};
      Object.keys(formData).forEach((key) => {
        if(record.hasOwnProperty(key)){
          tmpData[key] = record[key]
        }
      })
      //赋值
      Object.assign(formData, tmpData);
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    try {
      // 触发表单验证
      await validate();
    } catch ({ errorFields }) {
      if (errorFields) {
        const firstField = errorFields[0];
        if (firstField) {
          formRef.value.scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
        }
      }
      return Promise.reject(errorFields);
    }
    confirmLoading.value = true;
    const isUpdate = ref<boolean>(false);
    //时间格式化
    let model = formData;
    if (model.id) {
      isUpdate.value = true;
    }
    //循环数据
    for (let data in model) {
      //如果该数据是数组并且是字符串类型
      if (model[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          model[data] = model[data].join(',');
        }
      }
    }
    await saveOrUpdate(model, isUpdate.value)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }


  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 14px;
  }
</style>
