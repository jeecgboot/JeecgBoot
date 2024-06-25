/**
 * 目前实现了异步加载的组件清单 ：
 * JAreaLinkage
 * JEditor
 * JMarkdownEditor
 * JCodeEditor
 * JEasyCron
 */
import type { Component } from 'vue';
import type { ComponentType } from './types/index';
import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
/**
 * Component list, register here to setting it in the form
 */
import {
  Input,
  Select,
  Radio,
  Checkbox,
  AutoComplete,
  Cascader,
  DatePicker,
  InputNumber,
  Switch,
  TimePicker,
  TreeSelect,
  Slider,
  Rate,
  Divider,
} from 'ant-design-vue';
import ApiRadioGroup from './components/ApiRadioGroup.vue';
import RadioButtonGroup from './components/RadioButtonGroup.vue';
import ApiSelect from './components/ApiSelect.vue';
import ApiTreeSelect from './components/ApiTreeSelect.vue';
import { BasicUpload } from '/@/components/Upload';
import { StrengthMeter } from '/@/components/StrengthMeter';
import { IconPicker } from '/@/components/Icon';
import { CountdownInput } from '/@/components/CountDown';
//自定义组件
// import JAreaLinkage from './jeecg/components/JAreaLinkage.vue';
import JSelectUser from './jeecg/components/JSelectUser.vue';
import JSelectPosition from './jeecg/components/JSelectPosition.vue';
import JSelectRole from './jeecg/components/JSelectRole.vue';
import JImageUpload from './jeecg/components/JImageUpload.vue';
import JDictSelectTag from './jeecg/components/JDictSelectTag.vue';
import JSelectDept from './jeecg/components/JSelectDept.vue';
import JAreaSelect from './jeecg/components/JAreaSelect.vue';
import JEditor from './jeecg/components/JEditor.vue';
// import JMarkdownEditor from './jeecg/components/JMarkdownEditor.vue';
import JSelectInput from './jeecg/components/JSelectInput.vue';
// import JCodeEditor from './jeecg/components/JCodeEditor.vue';
import JCategorySelect from './jeecg/components/JCategorySelect.vue';
import JSelectMultiple from './jeecg/components/JSelectMultiple.vue';
import JPopup from './jeecg/components/JPopup.vue';
// update-begin--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
import JPopupDict from './jeecg/components/JPopupDict.vue';
// update-end--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
import JSwitch from './jeecg/components/JSwitch.vue';
import JTreeDict from './jeecg/components/JTreeDict.vue';
import JInputPop from './jeecg/components/JInputPop.vue';
// import { JEasyCron } from './jeecg/components/JEasyCron';
import JCheckbox from './jeecg/components/JCheckbox.vue';
import JInput from './jeecg/components/JInput.vue';
import JTreeSelect from './jeecg/components/JTreeSelect.vue';
import JEllipsis from './jeecg/components/JEllipsis.vue';
import JSelectUserByDept from './jeecg/components/JSelectUserByDept.vue';
import JUpload from './jeecg/components/JUpload/JUpload.vue';
import JSearchSelect from './jeecg/components/JSearchSelect.vue';
import JAddInput from './jeecg/components/JAddInput.vue';
import { Time } from '/@/components/Time';
import JRangeNumber from './jeecg/components/JRangeNumber.vue';
import UserSelect from './jeecg/components/userSelect/index.vue';
import JRangeDate from './jeecg/components/JRangeDate.vue'
import JRangeTime from './jeecg/components/JRangeTime.vue'
import JInputSelect from './jeecg/components/JInputSelect.vue'
import RoleSelectInput from './jeecg/components/roleSelect/RoleSelectInput.vue';
import {DatePickerInFilter, CascaderPcaInFilter} from "@/components/InFilter";

const componentMap = new Map<ComponentType, Component>();

componentMap.set('Time', Time);
componentMap.set('Input', Input);
componentMap.set('InputGroup', Input.Group);
componentMap.set('InputPassword', Input.Password);
componentMap.set('InputSearch', Input.Search);
componentMap.set('InputTextArea', Input.TextArea);
componentMap.set('InputNumber', InputNumber);
componentMap.set('AutoComplete', AutoComplete);

componentMap.set('Select', Select);
componentMap.set('ApiSelect', ApiSelect);
componentMap.set('TreeSelect', TreeSelect);
componentMap.set('ApiTreeSelect', ApiTreeSelect);
componentMap.set('ApiRadioGroup', ApiRadioGroup);
componentMap.set('Switch', Switch);
componentMap.set('RadioButtonGroup', RadioButtonGroup);
componentMap.set('RadioGroup', Radio.Group);
componentMap.set('Checkbox', Checkbox);
componentMap.set('CheckboxGroup', Checkbox.Group);
componentMap.set('Cascader', Cascader);
componentMap.set('Slider', Slider);
componentMap.set('Rate', Rate);

componentMap.set('DatePicker', DatePicker);
componentMap.set('MonthPicker', DatePicker.MonthPicker);
componentMap.set('RangePicker', DatePicker.RangePicker);
componentMap.set('WeekPicker', DatePicker.WeekPicker);
componentMap.set('TimePicker', TimePicker);
componentMap.set('DatePickerInFilter', DatePickerInFilter);
componentMap.set('StrengthMeter', StrengthMeter);
componentMap.set('IconPicker', IconPicker);
componentMap.set('InputCountDown', CountdownInput);

componentMap.set('Upload', BasicUpload);
componentMap.set('Divider', Divider);

//注册自定义组件

componentMap.set(
  'JAreaLinkage',
  createAsyncComponent(() => import('./jeecg/components/JAreaLinkage.vue'))
);
componentMap.set('JSelectPosition', JSelectPosition);
componentMap.set('JSelectUser', JSelectUser);
componentMap.set('JSelectRole', JSelectRole);
componentMap.set('JImageUpload', JImageUpload);
componentMap.set('JDictSelectTag', JDictSelectTag);
componentMap.set('JSelectDept', JSelectDept);
componentMap.set('JAreaSelect', JAreaSelect);
// componentMap.set(
//   'JEditor',
//   createAsyncComponent(() => import('./jeecg/components/JEditor.vue'))
// );
componentMap.set('JEditor', JEditor);
componentMap.set(
  'JMarkdownEditor',
  createAsyncComponent(() => import('./jeecg/components/JMarkdownEditor.vue'))
);
componentMap.set('JSelectInput', JSelectInput);
componentMap.set(
  'JCodeEditor',
  createAsyncComponent(() => import('./jeecg/components/JCodeEditor.vue'))
);
componentMap.set('JCategorySelect', JCategorySelect);
componentMap.set('JSelectMultiple', JSelectMultiple);
componentMap.set('JPopup', JPopup);
// update-begin--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
componentMap.set('JPopupDict', JPopupDict);
// update-end--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
componentMap.set('JSwitch', JSwitch);
componentMap.set('JTreeDict', JTreeDict);
componentMap.set('JInputPop', JInputPop);
componentMap.set(
  'JEasyCron',
  createAsyncComponent(() => import('./jeecg/components/JEasyCron/EasyCronInput.vue'))
);
componentMap.set('JCheckbox', JCheckbox);
componentMap.set('JInput', JInput);
componentMap.set('JTreeSelect', JTreeSelect);
componentMap.set('JEllipsis', JEllipsis);
componentMap.set('JSelectUserByDept', JSelectUserByDept);
componentMap.set('JUpload', JUpload);
componentMap.set('JSearchSelect', JSearchSelect);
componentMap.set('JAddInput', JAddInput);
componentMap.set('JRangeNumber', JRangeNumber);
componentMap.set('CascaderPcaInFilter', CascaderPcaInFilter);
componentMap.set('UserSelect', UserSelect);
componentMap.set('RangeDate', JRangeDate);
componentMap.set('RangeTime', JRangeTime);
componentMap.set('RoleSelect', RoleSelectInput);
componentMap.set('JInputSelect', JInputSelect);



export function add(compName: ComponentType, component: Component) {
  componentMap.set(compName, component);
}

export function del(compName: ComponentType) {
  componentMap.delete(compName);
}

export { componentMap };
