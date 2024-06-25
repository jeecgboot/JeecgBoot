import { FormSchema } from '/@/components/Form';

import dayjs from 'dayjs';

export const schemas: FormSchema[] = [
  {
    label: '文本框',
    field: 'name',
    component: 'Input',
    componentProps: {
      prefix: '中文',
      showCount: true,
    },
    defaultValue: '张三',
  },
  {
    label: '密码',
    field: 'password',
    component: 'InputPassword',
    componentProps: {
      //是否显示切换按钮或者控制密码显隐
      visibilityToggle: true,
      prefix: '密码',
    },
  },
  {
    label: '搜索框',
    field: 'searchBox',
    component: 'InputSearch',
    componentProps: {
      onSearch: (value) => {
        console.log(value);
      },
    },
  },
  {
    label: '文本域',
    field: 'textArea',
    component: 'InputTextArea',
    componentProps: {
      //可以点击清除图标删除内容
      allowClear: true,
      //是否展示字数
      showCount: true,
      //自适应内容高度，可设置为 true | false 或对象：{ minRows: 2, maxRows: 6 }
      autoSize: {
        //最小显示行数
        minRows: 2,
        //最大显示行数
        maxRows: 3,
      },
    },
  },
  {
    label: '数值输入框',
    field: 'number',
    component: 'InputNumber',
    componentProps: {
      //带标签的 input，设置后置标签
      addonAfter: '保留两位小数',
      //最大值
      max: 100,
      //数值经度
      precision: 2,
      //步数
      step: 0.1,
    },
  },

  {
    label: '下拉框',
    field: 'jinputtype',
    component: 'Select',
    componentProps: {
      options: [
        { value: 'like', label: '模糊（like）' },
        { value: 'ne', label: '不等于（ne）' },
        { value: 'ge', label: '大于等于（ge）' },
        { value: 'le', label: '小于等于（le)' },
      ],
      //下拉多选
      mode: 'multiple',
      //配置是否可搜索
      showSearch: true,
    },
  },
  {
    field: 'TreeSelect',
    label: '下拉树',
    component: 'TreeSelect',
    componentProps: {
      //是否显示下拉框，默认false
      treeCheckable: true,
      //标题
      title: '下拉树',
      //下拉树
      treeData: [
        {
          label: '洗衣机',
          value: '0',
          children: [
            {
              label: '滚筒洗衣机',
              value: '0-1',
            },
          ],
        },
        {
          label: '电视机',
          value: '1',
          children: [
            {
              label: '平板电视',
              value: '1-1',
              disabled: true,
            },
            {
              label: 'CRT电视机',
              value: '1-2',
            },
            {
              label: '投影电视',
              value: '1-3',
            },
          ],
        },
      ],
    },
  },
  {
    label: 'RadioButtonGroup组件',
    field: 'status',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        { label: '有效', value: 1 },
        { label: '无效', value: 0 },
      ],
    },
  },
  {
    label: '单选框',
    field: 'radioSex',
    component: 'RadioGroup',
    componentProps: {
      //options里面由一个一个的radio组成,支持disabled禁用
      options: [
        { label: '男', value: 1, disabled: false },
        { label: '女', value: 0 },
      ],
    },
  },
  {
    label: '多选框',
    field: 'checkbox',
    component: 'Checkbox',
    componentProps: {
      //是否禁用,默认false
      disabled: false,
    },
  },
  {
    label: '多选框组',
    field: 'checkSex',
    component: 'CheckboxGroup',
    componentProps: {
      //RadioGroup 下所有 input[type="radio"] 的 name 属性
      name: '爱好',
      //options支持disabled禁用
      options: [
        { label: '运动', value: 0, disabled: true },
        { label: '听音乐', value: 1 },
        { label: '看书', value: 2 },
      ],
    },
    defaultValue: [2],
  },
  {
    label: '自动完成组件',
    field: 'AutoComplete',
    component: 'AutoComplete',
    componentProps: {
      options: [{ value: 'Burns Bay Road' }, { value: 'Downing Street' }, { value: 'Wall Street' }],
    },
  },
  {
    label: '级联选择',
    field: 'cascade',
    component: 'Cascader',
    componentProps: {
      //最多显示多少个tag
      maxTagCount: 2,
      //浮层预设位置
      placement: 'bottomRight',
      //在选择框中显示搜索框,默认false
      showSearch: true,
      options: [
        {
          label: '北京',
          value: 'BeiJin',
          children: [
            {
              label: '海淀区',
              value: 'HaiDian',
            },
          ],
        },
        {
          label: '江苏省',
          value: 'JiangSu',
          children: [
            {
              label: '南京',
              value: 'Nanjing',
              children: [
                {
                  label: '中华门',
                  value: 'ZhongHuaMen',
                },
              ],
            },
          ],
        },
      ],
    },
  },
  {
    label: '日期选择',
    field: 'dateSelect',
    component: 'DatePicker',
    componentProps: {
      //日期格式化，页面上显示的值
      format: 'YYYY-MM-DD',
      //返回值格式化（绑定值的格式）
      valueFormat: 'YYYY-MM-DD',
      //是否显示今天按钮
      showToday: true,
      //不可选择日期
      disabledDate: (currentDate) => {
        let date = dayjs(currentDate).format('YYYY-MM-DD');
        let nowDate = dayjs(new Date()).format('YYYY-MM-DD');
        //当天不可选择
        if (date == nowDate) {
          return true;
        }
        return false;
      },
    },
  },
  {
    label: '月份选择',
    field: 'monthSelect',
    component: 'MonthPicker',
    componentProps: {
      //不可选择日期
      disabledDate: (currentDate) => {
        let date = dayjs(currentDate).format('YYYY-MM');
        let nowDate = dayjs(new Date()).format('YYYY-MM');
        //当天不可选择
        if (date == nowDate) {
          return true;
        }
        return false;
      },
    },
  },
  {
    label: '周选择',
    field: 'weekSelect',
    component: 'WeekPicker',
    componentProps: {
      size: 'small',
    },
  },
  {
    label: '时间选择',
    field: 'timeSelect',
    component: 'TimePicker',
    componentProps: {
      size: 'default',
      //日期时间或者时间模式下是否显示此刻，不支持日期时间范围和时间范围
      showNow: true,
    },
  },
  {
    label: '日期时间范围',
    field: 'dateTimeRangeSelect',
    component: 'RangePicker',
    componentProps: {
      //是否显示时间
      showTime: true,
      //日期格式化
      format: 'YYYY/MM/DD HH:mm:ss',
      //范围文本描述用集合
      placeholder: ['请选择开始日期时间', '请选择结束日期时间'],
    },
  },
  {
    label: '日期范围',
    field: 'dateRangeSelect',
    component: 'RangeDate',
    componentProps: {
      //日期格式化
      format: 'YYYY/MM/DD',
      //范围文本描述用集合
      placeholder: ['请选择开始日期', '请选择结束日期'],
    },
  },
  {
    label: '时间范围',
    field: 'timeRangeSelect',
    component: 'RangeTime',
    componentProps: {
      //日期格式化
      format: 'HH/mm/ss',
      //范围文本描述用集合
      placeholder: ['请选择开始时间', '请选择结束时间'],
    },
  },
  {
    label: '开关',
    field: 'switch',
    component: 'Switch',
    componentProps: {
      //开关大小，可选值：default small
      size: 'default',
      //非选中时的内容
      unCheckedChildren: '开启',
      //非选中时的值
      unCheckedValue: '0',
      //选中时的内容
      checkedChildren: '关闭',
      //选中时的值
      checkedValue: '1',
      //是否禁用
      disabled: false,
    },
  },
  {
    label: '滑动输入条',
    field: 'slider',
    component: 'Slider',
    componentProps: {
      //最小值
      min: -20,
      //最大值
      max: 100,
      //是否为双滑块模式
      range: true,
      //刻度标记
      marks: {
        '-20': '-20°C',
        0: '0°C',
        26: '26°C',
        37: '37°C',
        100: {
          style: {
            color: '#f50',
          },
          label: '100°C',
        },
      },
    },
  },
  {
    label: '评分',
    field: 'rate',
    component: 'Rate',
    componentProps: {
      //是否允许半选
      allowHalf: true,
      //star 总数
      count: 5,
      //tooltip提示，有几颗星写几个
      tooltips: ['非常差', '较差', '正常', '很好', '非很好'],
    },
  },
  {
    label: '分割线',
    field: 'divisionLine',
    component: 'Divider',
    componentProps: {
      //是否虚线
      dashed: false,
      //分割线标题的位置（left | right | center）
      orientation: 'center',
      //文字是否显示为普通正文样式
      plain: true,
      //水平还是垂直类型（horizontal | vertical）
      type: 'horizontal',
    },
  },
];
