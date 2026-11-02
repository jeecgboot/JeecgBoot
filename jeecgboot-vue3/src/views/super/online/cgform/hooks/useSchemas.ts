import { computed, h, Ref } from 'vue';
import { FormSchema, RenderCallbackParams } from '/@/components/Form';
import { Input, Button } from 'ant-design-vue';
import { FolderOpenOutlined } from '@ant-design/icons-vue';
import { bindMapFormSchema } from '/@/utils/common/compUtils';
import { usePermission } from '/@/hooks/web/usePermission';
import { rules } from '/@/utils/helper/validator';

const { isDisabledAuth } = usePermission();

export function useFormSchemas(_props, expandingConfig, handlers) {
  type SpanType = 'one' | 'tow' | 'three';
  // 动态布局
  const mapFormSchema = bindMapFormSchema<SpanType>(
    {
      // 一列
      one: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 2 },
          wrapperCol: { xs: 24, sm: 22 },
        },
      },
      // 两列
      tow: {
        colProps: { xs: 24, sm: 12 },
        itemProps: {
          labelCol: { xs: 24, sm: 4 },
          wrapperCol: { xs: 24, sm: 20 },
        },
      },
      // 三列
      three: {
        colProps: { xs: 24, sm: 8 },
        itemProps: {
          labelCol: { xs: 24, sm: 6 },
          wrapperCol: { xs: 24, sm: 18 },
        },
      },
    },
    'three'
  );

  // 表单 FormSchemas
  const formSchemas: FormSchema[] = [
    { label: '', field: 'id', component: 'Input', show: false },
    { label: '', field: 'tableVersion', component: 'Input', show: false },
    mapFormSchema({
      label: '表名',
      field: 'tableName',
      component: 'Input',
      required: true,
      // 如果版本号为1 表示未曾修改 未曾同步 可以修改表名
      dynamicDisabled: ({ model }) => model.tableVersion && model.tableVersion != 1,
      dynamicRules: ({ model, schema }) => {
        // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8492】online表名校验不允许输入中文
        return [
          {
            validator: (_, value) => {
              return new Promise((resolve, reject) => {
                let reg = /[\u4E00-\u9FA5]/g;
                if (reg.test(value)) {
                  reject('不允许输入中文');
                }
                resolve();
              });
            },
          },
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          {
            validator: (_, value) => {
              return new Promise((resolve, reject) => {
                if (value.length > 50) {
                  reject('表名最长50个字符');
                }
                resolve();
              });
            },
          },
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          ...rules.duplicateCheckRule('onl_cgform_head', 'table_name', model, schema, true),
        ];
        // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8492】online表名校验不允许输入中文
      },
    }),
    mapFormSchema({
      label: '表描述',
      field: 'tableTxt',
      component: 'Input',
      required: true,
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
      dynamicRules: ({ model, schema }) => {
        return [
          {
            validator: (_, value) => {
              return new Promise((resolve, reject) => {
                if (value.length > 200) {
                  reject('表描述最长200个字');
                }
                resolve();
              });
            },
          },
        ];
      },
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
    }),
    mapFormSchema({
      label: '表类型',
      field: 'tableType',
      component: 'Select',
      defaultValue: 1,
      componentProps: {
        options: [
          { label: '单表', value: 1 },
          { label: '主表', value: 2 },
          { label: '附表', value: 3 },
        ],
        onChange: handlers.onTableTypeChange,
        allowClear: false,
      },
    }),
    // 此处为占位符，用于将 relationType 顶到最右边
    {
      label: '',
      field: 'relationType',
      component: 'InputNumber',
      render: () => '',
      colProps: { xs: 0, sm: 17 },
      ifShow: fieldIfShow,
    },
    mapFormSchema({
      label: '',
      field: 'relationType',
      component: 'RadioGroup',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '一对多', value: 0 },
          { label: '一对一', value: 1 },
        ],
        allowClear: false,
        onChange: handlers.onRelationTypeChange,
      },
      colProps: { xs: 24, sm: 4 },
      itemProps: {
        colon: false,
        labelCol: { xs: 0, sm: 0 },
        wrapperCol: { xs: 24, sm: 24 },
      },
      ifShow: fieldIfShow,
    }),
    mapFormSchema({
      label: '序号',
      field: 'tabOrderNum',
      component: 'InputNumber',
      componentProps: {
        style: {
          width: '100%',
        },
      },
      colProps: { xs: 24, sm: 3 },
      itemProps: {
        labelCol: { xs: 24, sm: 7 },
        wrapperCol: { xs: 24, sm: 24 - 7 },
      },
      ifShow: fieldIfShow,
    }),
    mapFormSchema({
      label: '表单分类',
      field: 'formCategory',
      component: 'JDictSelectTag',
      defaultValue: 'temp',
      componentProps: {
        dictCode: 'ol_form_biz_type',
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '主键策略',
      field: 'idType',
      component: 'Select',
      defaultValue: 'UUID',
      componentProps: {
        options: [
          { label: 'ID_WORKER(分布式自增)', value: 'UUID' },
          // { label: 'NATIVE(自增长方式)', value: 'NATIVE' },
          // { label: 'SEQUENCE(序列方式)', value: 'SEQUENCE' },
        ],
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '序号名称',
      field: 'idSequence',
      component: 'Input',
      componentProps: {},
      ifShow: fieldIfShow,
    }),
    mapFormSchema({
      label: '显示复选框',
      field: 'isCheckbox',
      component: 'Select',
      defaultValue: 'Y',
      componentProps: {
        options: [
          { label: '是', value: 'Y' },
          { label: '否', value: 'N' },
        ],
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '主题模板',
      field: 'themeTemplate',
      component: 'Select',
      defaultValue: 'normal',
      componentProps: {
        options: [
          { label: '默认主题', value: 'normal' },
          { label: 'ERP主题(一对多)', value: 'erp' },
          { label: '内嵌子表主题(一对多)', value: 'innerTable' },
          { label: 'TAB主题(一对多)', value: 'tab' },
        ],
        allowClear: false,
      },
      // 单表时禁用该字段
      dynamicDisabled: ({ model }) => model.tableType === 1,
      // update-begin--author:liaozhiyang---date:20231123---for：【QQYUN-7073】提示ERP、内嵌子表不支持联合查询
      dynamicRules() {
        return [
          {
            validator({}, value) {
              const data = expandingConfig.value;
              if (value === 'erp') {
                if (data.joinQuery) {
                  return Promise.reject('ERP不支持联合查询功能');
                }
              } else if (value === 'innerTable') {
                if (data.joinQuery) {
                  return Promise.reject('内嵌子表不支持联合查询功能');
                }
              }
              return Promise.resolve();
            },
          },
        ];
      },
      // update-end--author:liaozhiyang---date:20231123---for：【QQYUN-7073】提示ERP、内嵌子表不支持联合查询
    }),
    mapFormSchema({
      label: '表单风格',
      field: 'formTemplate',
      component: 'Select',
      defaultValue: '1',
      componentProps: {
        options: [
          { label: '一列', value: '1' },
          { label: '两列', value: '2' },
          { label: '三列', value: '3' },
          { label: '四列', value: '4' },
        ],
        placeholder: '请选择PC表单风格',
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '移动表单风格',
      field: 'formTemplateMobile',
      component: 'Select',
      defaultValue: '1',
      componentProps: {
        options: [
          { label: 'AntDesign模板', value: '1' },
          { label: 'Bootstrap模板', value: '2' },
        ],
        placeholder: '请选择移动表单风格',
      },
      // 暂时先隐藏
      ifShow: false,
    }),
    mapFormSchema({
      label: '滚动条',
      field: 'scroll',
      component: 'Select',
      defaultValue: 1,
      componentProps: {
        options: [
          { label: '有', value: 1 },
          { label: '无', value: 0 },
        ],
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '是否分页',
      field: 'isPage',
      component: 'Select',
      defaultValue: 'Y',
      componentProps: {
        options: [
          { label: '是', value: 'Y' },
          { label: '否', value: 'N' },
        ],
        allowClear: false,
      },
    }),
    mapFormSchema({
      label: '是否树',
      field: 'isTree',
      component: 'Select',
      defaultValue: 'N',
      componentProps: {
        options: [
          { label: '是', value: 'Y' },
          { label: '否', value: 'N' },
        ],
        onChange: handlers.onIsTreeChange,
        allowClear: false,
      },
      dynamicRules({ model }) {
        return [
          {
            validator({}, value) {
              if (value === 'Y' && (model.tableType == 2 || model.tableType == 3)) {
                return Promise.reject('主表和附表不支持树类型！');
              } else {
              }
              return Promise.resolve();
            },
          },
        ];
      },
      // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-125】选择主表和附表时隐藏树表配置
      show({ model, values }) {
        if (model.tableType == 2 || model.tableType == 3) {
          model.isTree = 'N';
          handlers.onIsTreeChange('N');
          return false;
        }
        return true;
      },
      // update-end--author:liaozhiyang---date:20240604---for：【TV360X-125】选择主表和附表时隐藏树表配置
    }),
    mapFormSchema({
      // 空格不要删，否则布局会乱
      label: ' ', // 扩展配置
      field: 'extConfigJson',
      component: 'Input',
      slot: 'extConfigButton',
      itemProps: { colon: false },
      // 一对多子表时隐藏扩展配置按钮
      ifShow: ({ model }) => !(model.tableType === 3 && model.relationType === 0),
    }),
    mapFormSchema({
      label: '树表单父ID',
      field: 'treeParentIdField',
      component: 'Input',
      ifShow: fieldIfShow,
    }),
    mapFormSchema({
      label: '是否有子节点字段',
      field: 'treeIdField',
      component: 'Input',
      show: false,
    }),
    mapFormSchema({
      label: '树开表单列',
      field: 'treeFieldname',
      required: true,
      component: 'Input',
      ifShow: fieldIfShow,
    }),
    mapFormSchema(
      {
        label: '附表',
        field: 'subTableStr',
        component: 'Input',
        componentProps: {
          disabled: true,
          placeholder: ' ',
          allowClear: false,
        },
        ifShow: handlers.ifShowOfSubTableStr,
      },
      'one'
    ),
  ];

  // 控制字段是否显示
  function fieldIfShow({ field, model }: RenderCallbackParams) {
    switch (field) {
      case 'relationType':
      case 'tabOrderNum':
        return model.tableType === 3;
      case 'treeParentIdField':
      case 'treeIdField':
      case 'treeFieldname':
        return model.isTree === 'Y';
      case 'idSequence':
        return model.idType === 'SEQUENCE';
    }
    return true;
  }

  return { formSchemas };
}

/** 获取 扩展参数 FormSchemas */
export function useExtendConfigFormSchemas(_props, handlers) {
  type SpanType = 'left' | 'right';
  // formItem 的绑定值，统一布局
  const mapFormSchema = bindMapFormSchema<SpanType>(
    {
      left: {
        colProps: { xs: 24, sm: 9 },
        itemProps: {
          labelCol: { xs: 24, sm: 11 },
          wrapperCol: { xs: 24, sm: 13 },
        },
        style: { width: '100%' },
      },
      right: {
        colProps: { xs: 24, sm: 15 },
        itemProps: {
          labelCol: { xs: 24, sm: 4 },
          wrapperCol: { xs: 24, sm: 19 },
        },
        style: { width: '100%' },
      },
    },
    'left'
  );

  // 一对一子表时只显示固定操作列、列宽拖动、表单Label长度
  function isNotOneToOneSub() {
    const { tableType, relationType } = _props.parentForm.getFieldsValue(['tableType', 'relationType']);
    return !(tableType === 3 && relationType === 1);
  }

  // update-begin--author:jeecg---date:20260512---for：【QQYUN-15337】online表单支持多数据源（附表跟随主表，不单独配置）
  function isNotSubTable() {
    const { tableType } = _props.parentForm.getFieldsValue(['tableType']);
    return tableType !== 3;
  }
  // update-end--author:jeecg---date:20260512---for：【QQYUN-15337】online表单支持多数据源

  const baseFormSchemas: FormSchema[] = [
    // 弹窗
    mapFormSchema(
      {
        label: '弹窗默认全屏',
        field: 'modelFullscreen',
        ifShow: isNotOneToOneSub,
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
        },
      },
      'left'
    ),
    mapFormSchema(
      {
        label: '弹窗宽度',
        field: 'modalMinWidth',
        component: 'InputNumber',
        ifShow: isNotOneToOneSub,
        componentProps: {
          style: 'width: 70%',
          placeholder: '弹窗最小宽度（单位：px）',
        },
        // update-begin--author:liaozhiyang---date:20240520---for：【TV360X-77】弹窗全屏后，输入宽度框禁用
        dynamicDisabled: ({ model }) => !!model.modelFullscreen,
        // update-end--author:liaozhiyang---date:20240520---for：【TV360X-77】弹窗全屏后，输入宽度框禁用
      },
      'right'
    ),
    //----------------------------表单评论 begin-----------------------------------------
    mapFormSchema(
      {
        label: '开启表单评论',
        field: 'commentStatus',
        component: 'RadioButtonGroup',
        ifShow: isNotOneToOneSub,
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
        },
      },
      'left'
    ),
    // 此处为占位符
    mapFormSchema(
      {
        label: '',
        field: 'commentStatus',
        component: 'InputNumber',
        ifShow: isNotOneToOneSub,
        render: () => '',
      },
      'right'
    ),
    // 启用联合查询
    mapFormSchema(
      {
        label: '启用联合查询',
        field: 'joinQuery',
        component: 'RadioButtonGroup',
        ifShow: isNotOneToOneSub,
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
          onChange: handlers.onJoinQueryChange,
        },
      },
      'left'
    ),
    // 此处为占位符
    mapFormSchema(
      {
        label: '',
        field: 'joinQuery',
        component: 'InputNumber',
        ifShow: isNotOneToOneSub,
        render: () => '',
      },
      'right'
    ),
    // 积木报表打印
    mapFormSchema(
      {
        label: '集成积木报表',
        field: 'reportPrintShow',
        component: 'RadioButtonGroup',
        ifShow: isNotOneToOneSub,
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
          onChange: handlers.onReportPrintShowChange,
        },
      },
      'left'
    ),
    mapFormSchema(
      {
        label: '报表地址',
        field: 'reportPrintUrl',
        component: 'Input',
        ifShow: isNotOneToOneSub,
        componentProps: {
          style: 'width: 70%',
        },
        dynamicDisabled: ({ model }) => !model.reportPrintShow,
        dynamicRules: ({ model }) => {
          return [
            { required: !!model.reportPrintShow, message: '请输入报表地址！' },
            {
              validator({}, value) {
                if (/\/jmreport\/view\/{积木报表ID}/.test(value)) {
                  return Promise.reject('请将{积木报表ID}替换为真实的积木报表ID！');
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      'right'
    ),
    // update-begin--author:liaozhiyang---date:20231213---for：【QQYUN-7421】vue3先注释集成设计表单功能
    // mapFormSchema(
    //   {
    //     label: '集成设计表单',
    //     field: 'isDesForm',
    //     component: 'RadioButtonGroup',
    //     componentProps: {
    //       options: [
    //         { label: '开启', value: 'Y' },
    //         { label: '关闭', value: 'N' },
    //       ],
    //       buttonStyle: 'solid',
    //       onChange: handlers.onIsDesformChange,
    //     },
    //   },
    //   'left'
    // ),
    // mapFormSchema(
    //   {
    //     label: '表单编码',
    //     field: 'desFormCode',
    //     component: 'Input',
    //     componentProps: {
    //       style: 'width: 80%',
    //     },
    //     dynamicDisabled: ({ model }) => model.isDesForm !== 'Y',
    //     dynamicRules: ({ model }) => {
    //       return [{ required: model.isDesForm === 'Y', message: '请输入表单编码！' }];
    //     },
    //   },
    //   'right'
    // ),
    // update-end--author:liaozhiyang---date:20231213---for：【QQYUN-7421】vue3先注释集成设计表单功能
    // 列表操作列
    mapFormSchema(
      {
        label: '固定操作列',
        field: 'tableFixedAction',
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
        },
        defaultValue: 1,
        // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-69】树表时固定操作列禁用
        dynamicDisabled: () => _props.parentForm.getFieldsValue(['isTree']).isTree === 'Y',
        // update-end--author:liaozhiyang---date:20260713---for：【LHZP-69】树表时固定操作列禁用
      },
      'left'
    ),
    mapFormSchema(
      {
        label: '固定方式',
        field: 'tableFixedActionType',
        component: 'Select',
        componentProps: {
          options: [
            { label: '固定到右侧', value: 'right' },
            { label: '固定到左侧', value: 'left' },
          ],
          style: 'width: 70%',
        },
        defaultValue: 'right',
        // update-begin--author:liaozhiyang---date:20260713---for：【LHZP-69】树表时固定方式禁用
        dynamicDisabled: ({ model }) => !model.tableFixedAction || _props.parentForm.getFieldsValue(['isTree']).isTree === 'Y',
        // update-end--author:liaozhiyang---date:20260713---for：【LHZP-69】树表时固定方式禁用
        dynamicRules: ({ model }) => {
          return [{ required: !!model.tableFixedAction, message: '请选择固定方式！' }];
        },
      },
      'right'
    ),
    // 列宽拖动调整
    mapFormSchema(
      {
        label: '列宽拖动',
        field: 'canResizeColumn',
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
        },
        defaultValue: 0,
      },
      'left'
    ),
    // 此处为占位符
    mapFormSchema(
      {
        label: '',
        field: 'canResizeColumn',
        component: 'InputNumber',
        render: () => '',
      },
      'right'
    ),
    //--------------------------表单评论 end-----------------------------------------
    // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    mapFormSchema(
      {
        label: '表单Label长度',
        field: 'formLabelLengthShow',
        component: 'RadioButtonGroup',
        helpMessage: '字段备注的文字个数',
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
          onChange: handlers.onFormLabelLengthShow,
        },
      },
      'left'
    ),
    mapFormSchema(
      {
        label: 'Label长度',
        field: 'formLabelLength',
        component: 'InputNumber',
        componentProps: {
          style: 'width: 70%',
          placeholder: '自定义表单Label长度',
        },
        dynamicDisabled: ({ model }) => !model.formLabelLengthShow,
        dynamicRules: ({ model }) => {
          return [{ required: !!model.formLabelLengthShow, message: '请填写表单label长度' }];
        },
      },
      'right'
    ),
    // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    mapFormSchema(
      {
        label: '启用外部链接',
        field: 'enableExternalLink',
        component: 'RadioButtonGroup',
        ifShow: isNotOneToOneSub,
        componentProps: {
          options: [
            {label: '开启', value: 1},
            {label: '关闭', value: 0},
          ],
          buttonStyle: 'solid',
          defaultValue: 0,
          // onChange: handlers.onFormLabelLengthShow,
        },
      },
      'left'
    ),
    mapFormSchema(
      {
        label: '允许的操作',
        field: 'externalLinkActions',
        component: 'JCheckbox',
        ifShow: isNotOneToOneSub,
        itemProps: { colon: true },
        componentProps: {
          options: [
            {label: '新增', value: 'add'},
            {label: '编辑', value: 'edit'},
            {label: '详情', value: 'detail'},
          ],
        },
        dynamicDisabled: ({model}) => !model.enableExternalLink,
      },
      'right'
    ),
    // update-begin--author:jeecg---date:20260512---for：【QQYUN-15337】online表单支持多数据源
    mapFormSchema(
      {
        label: '开启多数据源',
        field: 'enableMultiDataSource',
        component: 'RadioButtonGroup',
        ifShow: isNotSubTable,
        // update-begin--author:jeecg---date:20260513---for：【QQYUN-15337】开启多数据源处增加使用说明/注意事项
        helpMessage: [
          '开启后：本表单的同步建表、数据增删改查、列表查询、导入导出都走选定的数据源；附表自动跟随主表。',
          '✓ 关联记录/下拉关联已支持多数据源，会自动按被关联表所属表单的数据源路由，无需额外配置。',
          '⚠ 对接了流程审批(BPM)的表单不要配数据源——流程引擎走主库，请把对接流程的表单放在主库（不开启多数据源）。',
          '⚠ 数据源变更后该表单及其附表会被置为"未同步"，需重新同步建表；物理表不会自动迁库，旧库的表/数据需自行清理。',
        ],
        helpComponentProps: {
          maxWidth: '550px',
          showIndex: true,
          fontSize: '13px',
          color: '#4e5969',
          backgroundColor: '#ffffff',
        },
        // update-end--author:jeecg---date:20260513---for：【QQYUN-15337】开启多数据源处增加使用说明/注意事项
        componentProps: {
          options: [
            { label: '开启', value: 1 },
            { label: '关闭', value: 0 },
          ],
          buttonStyle: 'solid',
          defaultValue: 0,
          onChange: handlers.onEnableMultiDataSourceChange,
        },
      },
      'left'
    ),
    mapFormSchema(
      {
        label: '选择数据源',
        field: 'dbSource',
        component: 'JDictSelectTag',
        ifShow: isNotSubTable,
        itemProps: { colon: true },
        componentProps: {
          dictCode: 'sys_data_source,name,code',
          style: 'width: 70%',
          // update-begin--author:jeecg---date:20260512---for：【QQYUN-15337】允许不选数据源（不选时走默认数据源）
          placeholder: '请选择数据源（不选则走默认数据源；同步建表与数据增删改查均走该数据源）',
          // update-end--author:jeecg---date:20260512---for：【QQYUN-15337】允许不选数据源（不选时走默认数据源）
        },
        dynamicDisabled: ({ model }) => !model.enableMultiDataSource,
      },
      'right'
    ),
    // 附表：仅当已继承到主表数据源时，只读展示该数据源（未继承/默认数据源时不展示此字段）
    mapFormSchema(
      {
        label: '数据源',
        field: 'dbSource',
        component: 'JDictSelectTag',
        ifShow: ({ model }) => !isNotSubTable() && !!model.dbSource,
        helpMessage: '附表的数据源跟随主表，请在对应主表的扩展配置中设置',
        componentProps: {
          dictCode: 'sys_data_source,name,code',
          disabled: true,
        },
      },
      'left'
    ),
    // 占位符，与上面的只读数据源凑成一行
    mapFormSchema(
      {
        label: '',
        field: 'dbSource',
        component: 'Input',
        ifShow: ({ model }) => !isNotSubTable() && !!model.dbSource,
        render: () => '',
      },
      'right'
    ),
    // update-end--author:jeecg---date:20260512---for：【QQYUN-15337】online表单支持多数据源
    // 图片导出方式
    mapFormSchema(
      {
        label: '图片导出方式',
        field: 'imageExportMode',
        component: 'RadioButtonGroup',
        helpMessage: '嵌入图片会增大 Excel 文件体积；图片路径适合仅保留文件地址的场景',
        itemProps: { class: 'image-export-mode' },
        componentProps: {
          options: [
            { label: '嵌入图片', value: 'embed' },
            { label: '图片路径', value: 'path' },
          ],
          buttonStyle: 'solid',
        },
        defaultValue: 'path',
      },
      'left'
    ),
    // 此处为占位符
    mapFormSchema(
      {
        label: '',
        field: 'imageExportMode',
        component: 'InputNumber',
        render: () => '',
      },
      'right'
    ),
  ];

  // update-begin--author:jeecg---date:20260810---for：【LHZP-122】一对多附表仅允许配置弹窗默认全屏、弹窗宽度和表单 Label 长度，其余扩展配置统一禁用
  const oneToManySubEnabledFields = new Set(['modelFullscreen', 'modalMinWidth', 'formLabelLengthShow', 'formLabelLength']);
  const formSchemas = baseFormSchemas.map((schema) => {
    if (oneToManySubEnabledFields.has(schema.field)) {
      return schema;
    }
    const originalDynamicDisabled = schema.dynamicDisabled;
    return {
      ...schema,
      dynamicDisabled: (params) => {
        const { tableType, relationType } = _props.parentForm.getFieldsValue(['tableType', 'relationType']);
        if (tableType === 3 && relationType === 0) {
          return true;
        }
        return typeof originalDynamicDisabled === 'function' ? originalDynamicDisabled(params) : originalDynamicDisabled ?? false;
      },
    };
  });
  // update-end--author:jeecg---date:20260810---for：【LHZP-122】一对多附表仅允许配置弹窗默认全屏、弹窗宽度和表单 Label 长度，其余扩展配置统一禁用
  return { formSchemas };
}

/** 获取 代码生成 FormSchemas */
export function useCodeGeneratorFormSchemas(_, handlers, single: Ref<boolean>) {
  type SpanType = 'one' | 'tow' | 'towOne';
  // 动态布局
  const mapFormSchema = bindMapFormSchema<SpanType>(
    {
      // 一列
      one: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 5 },
          wrapperCol: { xs: 24, sm: 16 },
        },
      },
      // 两列中的一列
      towOne: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 3 },
          wrapperCol: { xs: 24, sm: 20 },
        },
      },
      // 两列
      tow: {
        colProps: { xs: 24, sm: 12 },
        itemProps: {
          labelCol: { xs: 24, sm: 6 },
          wrapperCol: { xs: 24, sm: 16 },
        },
      },
    },
    'one'
  );
  const getColSize = computed<SpanType>(() => (single.value ? 'one' : 'tow'));
  // 由于需要动态改变布局，所以使用 computed
  const formSchemas = computed<FormSchema[]>(() => [
    mapFormSchema(
      {
        label: '代码生成目录',
        field: 'projectPath',
        render: ({ model, field }) =>
          h(
            Input.Search,
            {
              value: model[field],
              onChange: (e) => {
                model[field] = e.target.value;
                handlers.onProjectPathChange(e);
              },
              onSearch: handlers.onProjectPathSearch,
              disabled: isDisabledAuth('online:codeGenerate:projectPath'),
            },
            {
              enterButton: () =>
                h(
                  Button,
                  {
                    preIcon: 'ant-design:folder-open',
                    disabled: isDisabledAuth('online:codeGenerate:projectPath'),
                  },
                  {
                    default: () => '浏览',
                    icon: () => h(FolderOpenOutlined),
                  }
                ),
            }
          ),
        component: 'InputSearch',
        required: true,
        // 如果版本号为1 表示未曾修改 未曾同步 可以修改表名
      },
      single.value ? 'one' : 'towOne'
    ),
    mapFormSchema(
      {
        label: '页面风格',
        field: 'jspMode',
        component: 'Select',
        componentProps: {
          options: handlers.jspModeOptions.value,
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-895】页面风格去掉x
          allowClear: false,
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-895】页面风格去掉x
        },
      },
      getColSize.value
    ),
    mapFormSchema(
      {
        label: '功能说明',
        field: 'ftlDescription',
        component: 'Input',
      },
      getColSize.value
    ),
    { label: '数据模型', field: 'jformType', component: 'Input', show: false },
    mapFormSchema(
      {
        label: '表名',
        field: 'tableName_tmp',
        required: true,
        dynamicDisabled: true,
        component: 'Input',
      },
      getColSize.value
    ),
    mapFormSchema(
      {
        label: '实体类名',
        field: 'entityName',
        required: true,
        component: 'Input',
        componentProps: {
          placeholder: '请输入实体类名(首字母大写)',
        },
      },
      getColSize.value
    ),
    mapFormSchema(
      {
        label: '包名(小写)',
        field: 'entityPackage',
        component: 'Input',
        rules: [{ required: true, pattern: /^[a-zA-Z0-9._]*$/, message: '包名必填，且只允许字母、数字、下划线、小数点组合' }],
      },
      getColSize.value
    ),
    mapFormSchema(
      {
        label: '代码分层样式',
        field: 'packageStyle',
        component: 'Select',
        componentProps: {
          disabled: true,
          options: [
            { label: '业务分层', value: 'service' },
            { label: '代码分层', value: 'project' },
          ],
        },
      },
      getColSize.value
    ),
    mapFormSchema(
      {
        label: '页面代码',
        field: 'vueStyle',
        required: true,
        component: 'Input',
        defaultValue: 'vue3',
        slot: 'pageCode',
        // componentProps: {
        //   options: [
        //     { label: 'Vue3(BasicForm)', value: 'vue3' },
        //     { label: 'Vue3原生(a-form)', value: 'vue3Native' },
        //     { label: 'Vue2', value: 'vue' },
        //   ],
        // },
        // update-begin--author:liaozhiyang---date:20240612---for：【TV360X-1057】代码生成页面代码鼠标移入给说明
        // dynamicPropskey: 'options',
        // dynamicPropsVal: ({ model }) => {
        //   if (model.jspMode && (model.jspMode == 'innerTable' || model.jspMode == 'tab')) {
        //     return [
        //       { value: 'vue3', label: '封装表单(BasicForm)' },
        //     ];
        //   } else {
        //     return [
        //       { value: 'vue3', label: '封装表单(BasicForm)' },
        //       { value: 'vue3Native', label: '原生表单(a-form)' },
        //     ];
        //   }
        // },
        // update-end--author:liaozhiyang---date:20240612---for：【TV360X-1057】代码生成页面代码鼠标移入给说明
      },
      getColSize.value
    ),
    { label: '需要生成的代码', field: 'codeTypes', component: 'Input', show: false },
  ]);

  return { formSchemas };
}
