<template>
  <a-spin :spinning="confirmLoading">
    <a-form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
      <a-row>
        <a-col :span="24">
          <a-form-item label="文本" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.name">
            <a-input v-model:value="formState.name" placeholder="请输入文本"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="密码" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.miMa">
            <a-input-password v-model:value="formState.miMa" placeholder="请输入密码" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="字典下拉" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.xiala">
            <JDictSelectTag type="select" v-model:value="formState.xiala" dictCode="sex" placeholder="请选择字典下拉" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="字典单选" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.danxuan">
            <JDictSelectTag type="radio" v-model:value="formState.danxuan" dictCode="sex" placeholder="请选择字典单选" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="字典多选" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.duoxuan">
            <JCheckbox v-model:value="formState.duoxuan" dictCode="urgent_level" placeholder="请选择字典多选" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开关" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.kaiguan">
            <JSwitch v-model:value="formState.kaiguan" :options="['1', '0']"></JSwitch>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="日期" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.riqi">
            <a-date-picker placeholder="请选择日期" format="YYYY-MM-DD" valueFormat="YYYY-MM-DD" v-model:value="formState.riqi" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="年月日时分秒" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.nyrsfm">
            <a-date-picker show-time v-model:value="formState.nyrsfm" style="width: 100%" valueFormat="YYYY-MM-DD HH:mm:ss" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.shijian">
            <TimePicker placeholder="请选择时间" v-model:value="formState.shijian" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="文件" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.wenjian">
            <JUpload v-model:value="formState.wenjian"></JUpload>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.tupian">
            <JImageUpload :fileMax="2" v-model:value="formState.tupian"></JImageUpload>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="多行文本框" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.dhwb">
            <a-textarea v-model:value="formState.dhwb" rows="4" placeholder="请输入多行文本框" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="字典表下拉搜索框" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.xlss">
            <JSearchSelect v-model:value="formState.xlss" dict="sys_user,realname,username" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="popup弹窗" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.popup">
            <JPopup
              v-model:value="formState.popup"
              :fieldConfig="[
                { source: 'name', target: 'popup' },
                { source: 'id', target: 'popback' },
              ]"
              code="report_user"
              :multi="true"
              :setFieldsValue="setFieldsValue"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="popback" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.popback">
            <a-input v-model:value="formState.popback" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="分类字典树" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.flzds">
            <JCategorySelect
              @change="(value) => handleFormChange('flzds', value)"
              v-model:value="formState.flzds"
              pcode="B02"
              placeholder="请选择分类字典树"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="部门选择" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.bmxz">
            <JSelectDept v-model:value="formState.bmxz" :multi="true" type="array" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="用户选择" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.yhxz">
            <JSelectUserByDept v-model:value="formState.yhxz" :multi="true" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="富文本" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.fwb">
            <JEditor v-model:value="formState.fwb" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="markdown" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.markdownString">
            <JMarkdownEditor v-model:value="formState.markdownString"></JMarkdownEditor>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="省市区JAreaSelect" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.shq">
            <JAreaSelect v-model:value="formState.shq" placeholder="请输入省市区" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="省市区JAreaLinkage" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.jssq">
            <JAreaLinkage v-model:value="formState.jssq" placeholder="请输入省市区" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="JInputPop" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.ldzje">
            <JInputPop
              v-model:value="formState.ldzje"
              placeholder="请输入JInputPop"
              @change="(value) => handleFormChange('ldzje', value)"
            ></JInputPop>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="JSelectInput" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.ldzjs">
            <JSelectInput
              v-model:value="formState.ldzjs"
              placeholder="请选择JSelectInput"
              :options="ldzjsOptions"
              @change="(value) => handleFormChange('ldzjs', value)"
            ></JSelectInput>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="下拉多选" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.zddtjxl">
            <JSelectMultiple v-model:value="formState.zddtjxl" placeholder="请选择下拉多选" dictCode="sex"></JSelectMultiple>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="用户" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.yongHu">
            <JSelectUser v-model:value="formState.yongHu" placeholder="请选择用户"></JSelectUser>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.zhiWu">
            <JSelectPosition
              v-model:value="formState.zhiWu"
              placeholder="请选择职务"
              @change="(value) => handleFormChange('zhiWu', value)"
            ></JSelectPosition>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="角色" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.jueSe">
            <JSelectRole v-model:value="formState.jueSe" placeholder="请选择角色" @change="(value) => handleFormChange('jueSe', value)"></JSelectRole>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="自定义树" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.zdys">
            <JTreeSelect
              ref="treeSelect"
              placeholder="请选择自定义树"
              v-model:value="formState.zdys"
              dict="sys_category,name,id"
              pidValue="0"
              loadTriggleChange
            >
            </JTreeSelect>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="数值" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.yuanjia">
            <a-input-number v-model:value="formState.yuanjia" placeholder="请输入double类型" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="输入2到10位的字母" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.ywzz">
            <a-input v-model:value="formState.ywzz" placeholder="请输入2到10位的字母"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="JTreeDict" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.zdbxl">
            <JTreeDict
              v-model:value="formState.zdbxl"
              placeholder="请选择JTreeDict"
              @change="(value) => handleFormChange('zdbxl', value)"
            ></JTreeDict>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="JCodeEditor" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.zdmrz">
            <JCodeEditor
              v-model:value="formState.zdmrz"
              placeholder="请输入JCodeEditor"
              @change="(value) => handleFormChange('zdmrz', value)"
            ></JCodeEditor>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="参数" :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.jsonParam">
            <JAddInput v-model:value="formState.jsonParam" placeholder="参数"></JAddInput>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  import dayjs from 'dayjs';
  import { TimePicker, Form } from 'ant-design-vue';
  import JCheckbox from '/@/components/Form/src/jeecg/components/JCheckbox.vue';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import JSwitch from '/@/components/Form/src/jeecg/components/JSwitch.vue';
  import JUpload from '/@/components/Form/src/jeecg/components/JUpload/JUpload.vue';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
  import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
  import JPopup from '/@/components/Form/src/jeecg/components/JPopup.vue';
  import JCategorySelect from '/@/components/Form/src/jeecg/components/JCategorySelect.vue';
  import JSelectUserByDept from '/@/components/Form/src/jeecg/components/JSelectUserByDept.vue';
  import JEditor from '/@/components/Form/src/jeecg/components/JEditor.vue';
  import JMarkdownEditor from '/@/components/Form/src/jeecg/components/JMarkdownEditor.vue';
  import JTreeSelect from '/@/components/Form/src/jeecg/components/JTreeSelect.vue';
  import JInputPop from '/@/components/Form/src/jeecg/components/JInputPop.vue';
  import JSelectInput from '/@/components/Form/src/jeecg/components/JSelectInput.vue';
  import JSelectPosition from '/@/components/Form/src/jeecg/components/JSelectPosition.vue';
  import JSelectMultiple from '/@/components/Form/src/jeecg/components/JSelectMultiple.vue';
  import JInput from '/@/components/Form/src/jeecg/components/JInput.vue';
  import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
  import JSelectUser from '/@/components/Form/src/jeecg/components/JSelectUser.vue';
  import JAreaSelect from '/@/components/Form/src/jeecg/components/JAreaSelect.vue';
  import JAreaLinkage from '/@/components/Form/src/jeecg/components/JAreaLinkage.vue';
  import JSelectRole from '/@/components/Form/src/jeecg/components/JSelectRole.vue';
  import JTreeDict from '/@/components/Form/src/jeecg/components/JTreeDict.vue';
  import JCodeEditor from '/@/components/Form/src/jeecg/components/JCodeEditor.vue';
  import JAddInput from '/@/components/Form/src/jeecg/components/JAddInput.vue';
  import { getValueType } from '/@/utils';

  const emit = defineEmits(['register', 'ok']);
  //update-begin---author:wangshuai ---date:20220616  for：报表示例验证修改--------------
  const formState = reactive<Record<string, any>>({
    name: '',
    miMa: '',
    ywzz: '',
    xiala: '',
    danxuan: '',
    duoxuan: '',
    riqi: '',
    shijian: '',
    wenjian: '',
    tupian: '',
    dhwb: '',
    xlss: '',
    popup: '',
    flzds: '',
    yhxz: '',
    fwb: '',
    shq: '',
    ldzje: '',
    ldzjs: '',
    zddtjxl: '',
    yongHu: '',
    zhiWu: '',
    jueSe: '',
    zdys: '',
    jssq: '',
    zdbxl: '',
    zdmrz: '',
    jsonParam: '',
    bmxz: '',
    yuanjia: '',
    nyrsfm: '',
  });
  //update-end---author:wangshuai ---date:20220616  for：报表示例验证修改--------------
  const { createMessage } = useMessage();
  const formRef = ref();
  const useForm = Form.useForm;
  const url = reactive<any>({
    duplicateCheck: '/sys/duplicate/check',
    add: '/test/jeecgDemo/oneNative/add',
    edit: '/test/jeecgDemo/oneNative/edit',
  });
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = {
    name: [{ required: false, message: '请输入文本!' }],
    miMa: [{ required: false, message: '请输入密码!' }],
    ywzz: [{ required: false }, { pattern: '^[a-z|A-Z]{2,10}$', message: '不符合校验规则!' }],
    xiala: [{ required: false, message: '请选择下拉组件!' }],
    danxuan: [{ required: false, message: '请选择单选组件!' }],
    duoxuan: [{ required: false, message: '请选择多选组件!' }],
    riqi: [{ required: false, message: '请选择日期!' }],
    shijian: [{ required: false, message: '请选择时间!' }],
    wenjian: [{ required: false, message: '请上传文件!' }],
    tupian: [{ required: false, message: '请上传图片!' }],
    dhwb: [{ required: false, message: '请填写多行文本!' }],
    xlss: [{ required: false, message: '请选择字典下拉搜索!' }],
    popup: [{ required: false, message: '请选择popup弹窗!' }],
    flzds: [{ required: false, message: '请选择分类字典树!' }],
    yhxz: [{ required: false, message: '请选择用户!' }],
    fwb: [{ required: false, message: '请填写富文本!' }],
    shq: [{ required: false, message: '请选择省市级!' }],
    ldzje: [{ required: false, message: '请输入JInputPop!' }],
    ldzjs: [{ required: false, message: '请选择下拉输入框!' }],
    zddtjxl: [{ required: false, message: '请选择多选输入框!' }],
    yongHu: [{ required: false, message: '请选择用户!' }],
    zhiWu: [{ required: false, message: '请选择职务!' }],
    jueSe: [{ required: false, message: '请选择角色!' }],
    zdys: [{ required: false, message: '请选择自定义树!' }],
    jssq: [{ required: false, message: '请选择三级联动!' }],
    zdbxl: [{ required: false, message: '请选择JTreeDict!' }],
    zdmrz: [{ required: false, message: '请输入JCodeEditor!' }],
    jsonParam: [{ required: false, message: '请输入参数!' }],
    bmxz: [{ required: false, message: '请选择部门!' }],
    yuanjia: [{ required: false, message: '请输入数值!' }],
    nyrsfm: [{ required: false, message: '请选择年月日时分秒!' }],
  };
  //update-begin---author:wangshuai ---date:20220616  for：报表示例验证修改------------
  const { resetFields, validate, validateInfos } = useForm(formState, validatorRules, { immediate: false });
  //update-end---author:wangshuai ---date:20220616  for：报表示例验证修改------------
  const ldzjsOptions = ref([
    { label: '男', value: '1' },
    { label: '女', value: '2' },
  ]);

  /**
   * 新增
   */
  function add() {
    edit({});
  }

  /**
   * 编辑
   */
  function edit(record) {
    nextTick(() => {
      resetFields();
      //赋值
      Object.assign(formState, record);
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    // 触发表单验证
    //update-begin---author:wangshuai ---date:20220616  for：报表示例验证修改------------
    await validate();
    confirmLoading.value = true;
    let httpurl = '';
    let method = '';
    //时间格式化
    let model = formState;
    if (!model.id) {
      httpurl += url.add;
      method = 'post';
    } else {
      httpurl += url.edit;
      method = 'put';
    }
    //循环数据如果是数组
    for (let data in formState) {
      //如果该数据是数组并且是字符串类型
      if (formState[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          formState[data] = formState[data].join(',');
        }
      }
    }
    defHttp
      .request(
        {
          url: httpurl,
          params: model,
          method: method,
        },
        { isTransformResponse: false }
      )
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
    //update-end---author:wangshuai ---date:20220616  for：报表示例验证修改--------------
  }

  /**
   * popup成功回调事件
   */
  function popupHandleSuccess(values) {
    Object.assign(formState, values);
  }

  /**
   *  popup组件值改变事件
   */
  function setFieldsValue(map) {
    Object.keys(map).map((key) => {
      formState[key] = map[key];
    });
  }

  /**
   * 值改变事件触发
   * @param key
   * @param value
   */
  function handleFormChange(key, value) {
    formState[key] = value;
  }

  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 24px 24px 24px 24px;
  }
</style>
