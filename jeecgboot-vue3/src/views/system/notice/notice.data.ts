import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import { h } from 'vue';
import { Tinymce } from '@/components/Tinymce';

export const columns: BasicColumn[] = [
  {
    title: '标题',
    width: 150,
    dataIndex: 'titile',
  },
  {
    title: '消息类型',
    dataIndex: 'msgCategory',
    width: 100,
    customRender: ({ text }) => {
      return render.renderDict(text, 'msg_category');
    },
  },
  {
    title: '发布人',
    width: 100,
    dataIndex: 'sender',
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 70,
    customRender: ({ text }) => {
      const color = text == 'L' ? 'blue' : text == 'M' ? 'yellow' : 'red';
      return render.renderTag(render.renderDict(text, 'priority'), color);
    },
  },
  {
    title: '通告对象',
    dataIndex: 'msgType',
    width: 100,
    customRender: ({ text }) => {
      return render.renderDict(text, 'msg_type');
    },
  },
  {
    title: '发布状态',
    dataIndex: 'sendStatus',
    width: 70,
    customRender: ({ text }) => {
      const color = text == '0' ? 'red' : text == '1' ? 'green' : 'gray';
      return render.renderTag(render.renderDict(text, 'send_status'), color);
    },
  },
  {
    title: '发布时间',
    width: 100,
    dataIndex: 'sendTime',
  },
  {
    title: '撤销时间',
    width: 100,
    dataIndex: 'cancelTime',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'titile',
    label: '标题',
    component: 'JInput',
    colProps: { span: 6 },
  },
  {
    field: 'msgCategory',
    label: '消息类型',
    component: 'JDictSelectTag',
    defaultValue: '1',
    componentProps: {
      dictCode: 'msg_category',
      placeholder: '请选择类型',
    },
    colProps: { span: 6 },
  },
  {
    field: 'msgClassify',
    label: '公告分类',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'notice_type',
      placeholder: '请选择公告分类',
    },
    colProps: { span: 6 },
  },
  {
    field: 'sendTime',
    label: '发布时间',
    component: 'RangePicker',
    componentProps: {
      valueType: 'Date',
    },
    colProps: { span: 6 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'msgCategory',
    label: '消息类型',
    required: true,
    component: 'JDictSelectTag',
    defaultValue: '1',
    componentProps: {
      type: 'radio',
      dictCode: 'msg_category',
      placeholder: '请选择类型',
    },
  },
  {
    field: 'izTop',
    label: '是否置顶',
    defaultValue: '0',
    component: 'JSwitch',
    componentProps: {
      //取值 options
      options: ['1', '0'],
      //文本option
      labelOptions: ['是', '否'],
      placeholder: '是否置顶',
      checkedChildren: '是',
      unCheckedChildren: '否',
    },
  },
  {
    field: 'titile',
    label: '通告标题',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入标题',
    },
    // 代码逻辑说明: 【TV360X-1632】标题过长保存报错，长度校验
    dynamicRules() {
      return [
        {
          validator: (_, value) => {
            return new Promise<void>((resolve, reject) => {
              if (value.length > 100) {
                reject('最长100个字符');
              }
              resolve();
            });
          },
        },
      ];
    },
  },
  {
    field: 'msgAbstract',
    label: '通告摘要',
    component: 'InputTextArea',
    componentProps: {
      allowClear: true,
      autoSize: {
        minRows: 2,
        maxRows: 5,
      },
    },
    required: true,
  },
  // {
  //   field: 'endTime',
  //   label: '截至日期',
  //   component: 'DatePicker',
  //   componentProps: {
  //     showTime: true,
  //     valueFormat: 'YYYY-MM-DD HH:mm:ss',
  //     placeholder: '请选择截至日期',
  //   },
  //   dynamicRules: ({ model }) => rules.endTime(model.startTime, true),
  // },
  {
    field: 'msgType',
    label: '接收用户',
    defaultValue: 'ALL',
    component: 'JDictSelectTag',
    required: true,
    componentProps: {
      type: 'radio',
      dictCode: 'msg_type',
      placeholder: '请选择发布范围',
    },
  },
  {
    field: 'userIds',
    label: '指定用户',
    component: 'JSelectUserByDepartment',
    required: true,
    componentProps: {
      rowKey: 'id',
      // 代码逻辑说明: 【TV360X-1627】通知公告用户选择组件没翻译
      labelKey: 'realname',
    },
    ifShow: ({ values }) => values.msgType == 'USER',
  },
  {
    field: 'msgClassify',
    label: '公告分类',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'notice_type',
      placeholder: '请选择公告分类',
    },
  },
  {
    field: 'priority',
    label: '优先级别',
    defaultValue: 'H',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'priority',
      type: 'radio',
      placeholder: '请选择优先级',
    },
  },
  {
    field: 'izApproval',
    label: '是否审批',
    component: 'RadioGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        {
          label: '是',
          value: '1',
        },
        {
          label: '否',
          value: '0',
        },
      ],
    },
  },
  {
    field: 'msgTemplate',
    label: '公告模版',
    component: 'Input',
    slot: 'msgTemplate',
  },
  {
    field: 'files',
    label: '通告附件',
    component: 'JUpload',
    componentProps: {
      //是否显示选择按钮
      text: '文件上传',
      //最大上传数
      maxCount: 20,
      //是否显示下载按钮
      download: true,
    },
  },
  {
    field: 'msgContent',
    label: '通告内容',
    component: 'Input',
    colProps: { span: 24 },
    render: render.renderTinymce,
  },
];

/**
 * 流程表单调用这个方法获取formSchema
 * @param param
 */
export function getBpmFormSchema(_formData): FormSchema[] {
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return [
    {
      field: 'id',
      label: 'id',
      component: 'Input',
      show: false,
    },
    {
      field: 'msgCategory',
      label: '消息类型',
      required: true,
      component: 'JDictSelectTag',
      defaultValue: '1',
      componentProps: {
        type: 'radio',
        dictCode: 'msg_category',
        placeholder: '请选择类型',
      },
    },
    {
      field: 'izTop',
      label: '是否置顶',
      defaultValue: '0',
      component: 'JSwitch',
      componentProps: {
        //取值 options
        options: ['1', '0'],
        //文本option
        labelOptions: ['是', '否'],
        placeholder: '是否置顶',
        checkedChildren: '是',
        unCheckedChildren: '否',
      },
    },
    {
      field: 'titile',
      label: '通告标题',
      component: 'Input',
      required: true,
      componentProps: {
        placeholder: '请输入标题',
      },
      // 代码逻辑说明: 【TV360X-1632】标题过长保存报错，长度校验
      dynamicRules() {
        return [
          {
            validator: (_, value) => {
              return new Promise<void>((resolve, reject) => {
                if (value.length > 100) {
                  reject('最长100个字符');
                }
                resolve();
              });
            },
          },
        ];
      },
    },
    {
      field: 'msgAbstract',
      label: '通告摘要',
      component: 'InputTextArea',
      required: true,
    },
    {
      field: 'msgType',
      label: '接收用户',
      defaultValue: 'ALL',
      component: 'JDictSelectTag',
      required: true,
      componentProps: {
        type: 'radio',
        dictCode: 'msg_type',
        placeholder: '请选择发布范围',
      },
    },
    {
      field: 'userIds',
      label: '指定用户',
      component: 'JSelectUserByDepartment',
      required: true,
      componentProps: {
        rowKey: 'id',
        // 代码逻辑说明: 【TV360X-1627】通知公告用户选择组件没翻译
        labelKey: 'realname',
      },
      ifShow: ({ values }) => values.msgType == 'USER',
    },
    {
      field: 'msgClassify',
      label: '公告分类',
      component: 'JDictSelectTag',
      componentProps: {
        dictCode: 'notice_type',
        placeholder: '请选择公告分类',
      },
    },
    {
      field: 'priority',
      label: '优先级别',
      defaultValue: 'H',
      component: 'JDictSelectTag',
      componentProps: {
        dictCode: 'priority',
        type: 'radio',
        placeholder: '请选择优先级',
      },
    },
    {
      field: 'msgTemplate',
      label: '公告模版',
      component: 'Input',
      slot: 'msgTemplate',
    },
    {
      field: 'files',
      label: '通告附件',
      component: 'JUpload',
      componentProps: {
        //是否显示选择按钮
        text: '文件上传',
        //最大上传数
        maxCount: 2,
        //是否显示下载按钮
        download: true,
      },
    },
    {
      field: 'msgContent',
      label: '通告内容',
      component: 'Input',
      colProps: { span: 24 },
      ifShow: ({}) => _formData.disabled == false,
      render: ({ model, field }) => {
        return h(Tinymce, {
          showImageUpload: false,
          disabled: _formData.disabled !== false,
          height: 300,
          value: model[field],
          onChange: (value: string) => {
            model[field] = value;
          },
        });
      },
    },
    {
      field: 'msgContent',
      label: '通告内容',
      component: 'Input',
      colProps: { span: 24 },
      ifShow: ({}) => _formData.disabled !== false,
      slot: 'msgContent',
    },
  ];
}
