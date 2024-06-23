<template>
  <BasicModal v-bind="$attrs" @register="registerModal" @ok="handleSubmit" :title="title" :width="1200" destroyOnClose>
    <BasicForm @register="registerForm" />

    <a-tabs v-model:activeKey="activeKey" animated>
      <a-tab-pane tab="局部规则" key="1" :forceRender="true">
        <JVxeTable ref="vTable1" toolbar rowNumber dragSort rowSelection :maxHeight="580" :dataSource="dataSource1" :columns="columns1">
          <template #toolbarAfter>
            <a-alert type="info" showIcon message="局部规则按照你输入的位数有序的校验" style="margin-bottom: 8px" />
          </template>
        </JVxeTable>
      </a-tab-pane>
      <a-tab-pane tab="全局规则" key="2" :forceRender="true">
        <JVxeTable
          ref="vTable2"
          toolbar
          rowNumber
          dragSort
          rowSelection
          :maxHeight="580"
          :dataSource="dataSource2"
          :addSetActive="false"
          :columns="columns2"
        >
          <template #toolbarAfter>
            <a-alert type="info" showIcon message="全局规则可校验用户输入的所有字符；全局规则的优先级比局部规则的要高。" style="margin-bottom: 8px" />
          </template>
        </JVxeTable>
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './check.rule.data';
  import { saveCheckRule, updateCheckRule } from './check.rule.api';
  import { JVxeTypes, JVxeColumn, JVxeTableInstance } from '/@/components/jeecg/JVxeTable/types';
  import { pick } from 'lodash-es';

  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
  // 声明Emits
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);

  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const activeKey = ref('1');
  let arr1: any[] = [];
  let dataSource1 = ref(arr1);
  let arr2: any[] = [];
  let dataSource2 = ref(arr2);

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    activeKey.value = '1';
    dataSource1.value = [];
    dataSource2.value = [];
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });

      let ruleJson = data.record.ruleJson;
      if (ruleJson) {
        let ruleList = JSON.parse(ruleJson);
        // 筛选出全局规则和局部规则
        let global: any[] = [],
          design: any[] = [],
          priority = '1';
        ruleList.forEach((rule) => {
          if (rule.digits === '*') {
            global.push(Object.assign(rule, { priority }));
          } else {
            priority = '0';
            design.push(rule);
          }
        });
        dataSource1.value = design;
        dataSource2.value = global;
      }
    }
  });

  const vTable1 = ref<JVxeTableInstance>();
  const vTable2 = ref<JVxeTableInstance>();

  // 验证表格 返回表格数据
  function validateMyTable(tableRef, key) {
    return new Promise((resolve, reject) => {
      tableRef.value!.validateTable().then((errMap) => {
        if (errMap) {
          activeKey.value = key;
          reject();
        } else {
          const values = tableRef.value!.getTableData();
          resolve(values);
        }
      });
    });
  }

  //表单提交事件
  async function handleSubmit() {
    let mainData;
    let globalValues = [];
    let designValues = [];
    validate()
      .then((formValue) => {
        mainData = formValue;
        return validateMyTable(vTable1, '1');
      })
      .then((tableData1: []) => {
        if (tableData1 && tableData1.length > 0) {
          designValues = tableData1;
        }
        return validateMyTable(vTable2, '2');
      })
      .then((tableData2: []) => {
        if (tableData2 && tableData2.length > 0) {
          globalValues = tableData2;
        }
        // 整合两个子表的数据
        let firstGlobal: any[] = [],
          afterGlobal: any[] = [];
        for (let i = 0; i < globalValues.length; i++) {
          let v: any = globalValues[i];
          v.digits = '*';
          if (v.priority === '1') {
            firstGlobal.push(v);
          } else {
            afterGlobal.push(v);
          }
        }
        let concatValues = firstGlobal.concat(designValues).concat(afterGlobal);
        let subValues = concatValues.map((i) => pick(i, 'digits', 'pattern', 'message'));
        // 生成 formData，用于传入后台
        let ruleJson = JSON.stringify(subValues);
        let formData = Object.assign({}, mainData, { ruleJson });
        saveOrUpdateFormData(formData);
      })
      .catch(() => {
        setModalProps({ confirmLoading: false });
        console.error('验证未通过!');
      });
  }

  // 表单提交请求
  async function saveOrUpdateFormData(formData) {
    try {
      console.log('表单提交数据', formData);
      setModalProps({ confirmLoading: true });
      if (isUpdate.value) {
        await updateCheckRule(formData);
      } else {
        await saveCheckRule(formData);
      }
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 校验
   * @param cellValue
   * @param callback
   */
  const validatePatternHandler = ({ cellValue }, callback) => {
    try {
      new RegExp(cellValue);
      callback(true);
    } catch (e) {
      callback(false, '请输入正确的正则表达式');
    }
  };

  const columns1 = ref<JVxeColumn[]>([
    {
      title: '位数',
      key: 'digits',
      type: JVxeTypes.inputNumber,
      minWidth: 180,
      validateRules: [
        { required: true, message: '${title}不能为空' },
        { pattern: /^[1-9]\d*$/, message: '请输入零以上的正整数' },
      ],
    },
    {
      title: '规则（正则表达式）',
      key: 'pattern',
      minWidth: 320,
      type: JVxeTypes.input,
      validateRules: [{ required: true, message: '规则不能为空' }, { handler: validatePatternHandler }],
    },
    {
      title: '提示文本',
      key: 'message',
      minWidth: 180,
      type: JVxeTypes.input,
      validateRules: [{ required: true, message: '${title}不能为空' }],
    },
  ]);

  const columns2 = ref<JVxeColumn[]>([
    {
      title: '优先级',
      key: 'priority',
      type: JVxeTypes.select,
      defaultValue: '1',
      options: [
        { title: '优先运行', value: '1' },
        { title: '最后运行', value: '0' },
      ],
      validateRules: [],
    },
    {
      title: '规则（正则表达式）',
      key: 'pattern',
      width: '40%',
      type: JVxeTypes.input,
      validateRules: [{ required: true, message: '规则不能为空' }, { handler: validatePatternHandler }],
    },
    {
      title: '提示文本',
      key: 'message',
      width: '20%',
      type: JVxeTypes.input,
      validateRules: [{ required: true, message: '${title}不能为空' }],
    },
  ]);
</script>
