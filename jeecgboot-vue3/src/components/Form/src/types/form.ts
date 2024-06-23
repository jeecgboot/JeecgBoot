import type { NamePath, RuleObject, ValidateOptions } from 'ant-design-vue/lib/form/interface';
import type { VNode, ComputedRef } from 'vue';
import type { ButtonProps as AntdButtonProps } from '/@/components/Button';
import type { FormItem } from './formItem';
import type { ColEx, ComponentType } from './index';
import type { TableActionType } from '/@/components/Table/src/types/table';
import type { CSSProperties } from 'vue';
import type { RowProps } from 'ant-design-vue/lib/grid/Row';

export type FieldMapToTime = [string, [string, string], string?][];
export type FieldMapToNumber = [string, [string, string]][];

export type Rule = RuleObject & {
  trigger?: 'blur' | 'change' | ['change', 'blur'];
};

export interface RenderCallbackParams {
  schema: FormSchema;
  values: Recordable;
  model: Recordable;
  field: string;
}

export interface ButtonProps extends AntdButtonProps {
  text?: string;
}

export interface FormActionType {
  submit: () => Promise<void>;
  setFieldsValue: <T>(values: T) => Promise<void>;
  resetFields: () => Promise<void>;
  getFieldsValue: () => Recordable;
  clearValidate: (name?: string | string[]) => Promise<void>;
  updateSchema: (data: Partial<FormSchema> | Partial<FormSchema>[]) => Promise<void>;
  resetSchema: (data: Partial<FormSchema> | Partial<FormSchema>[]) => Promise<void>;
  setProps: (formProps: Partial<FormProps>) => Promise<void>;
  getProps: ComputedRef<Partial<FormProps>>;
  removeSchemaByFiled: (field: string | string[]) => Promise<void>;
  appendSchemaByField: (schema: FormSchema, prefixField: string | undefined, first?: boolean | undefined) => Promise<void>;
  validateFields: (nameList?: NamePath[], options?: ValidateOptions) => Promise<any>;
  validate: (nameList?: NamePath[]) => Promise<any>;
  scrollToField: (name: NamePath, options?: ScrollOptions) => Promise<void>;
}

export type RegisterFn = (formInstance: FormActionType) => void;

export type UseFormReturnType = [RegisterFn, FormActionType];

export interface FormProps {
  layout?: 'vertical' | 'inline' | 'horizontal';
  // Form value
  model?: Recordable;
  // The width of all items in the entire form
  labelWidth?: number | string;
  //alignment
  labelAlign?: 'left' | 'right';
  //Row configuration for the entire form
  rowProps?: RowProps;
  // Submit form on reset
  submitOnReset?: boolean;
  // Col configuration for the entire form
  labelCol?: Partial<ColEx> | null;
  // Col configuration for the entire form
  wrapperCol?: Partial<ColEx> | null;

  // General row style
  baseRowStyle?: CSSProperties;

  // General col configuration
  baseColProps?: Partial<ColEx>;

  // Form configuration rules
  schemas?: FormSchema[];
  // Function values used to merge into dynamic control form items
  mergeDynamicData?: Recordable;
  // Compact mode for search forms
  compact?: boolean;
  // Blank line span
  emptySpan?: number | Partial<ColEx>;
  // Internal component size of the form
  size?: 'default' | 'small' | 'large';
  // Whether to disable
  disabled?: boolean;
  // Time interval fields are mapped into multiple
  fieldMapToTime?: FieldMapToTime;
  // number interval fields are mapped into multiple
  fieldMapToNumber?: FieldMapToNumber;
  // Placeholder is set automatically
  autoSetPlaceHolder?: boolean;
  // Auto submit on press enter on input
  autoSubmitOnEnter?: boolean;
  // Check whether the information is added to the label
  rulesMessageJoinLabel?: boolean;
  // 是否显示展开收起按钮
  showAdvancedButton?: boolean;
  // Whether to focus on the first input box, only works when the first form item is input
  autoFocusFirstItem?: boolean;
  // 【jeecg】如果 showAdvancedButton 为 true，超过指定列数默认折叠，默认为3
  autoAdvancedCol?: number;
  // 如果 showAdvancedButton 为 true，超过指定行数行默认折叠
  autoAdvancedLine?: number;
  // 折叠时始终保持显示的行数
  alwaysShowLines?: number;
  // Whether to show the operation button
  showActionButtonGroup?: boolean;

  // Reset button configuration
  resetButtonOptions?: Partial<ButtonProps>;

  // Confirm button configuration
  submitButtonOptions?: Partial<ButtonProps>;

  // Operation column configuration
  actionColOptions?: Partial<ColEx>;

  // Show reset button
  showResetButton?: boolean;
  // Show confirmation button
  showSubmitButton?: boolean;

  resetFunc?: () => Promise<void>;
  submitFunc?: () => Promise<void>;
  transformDateFunc?: (date: any) => string;
  colon?: boolean;
}
export interface FormSchema {
  // Field name
  field: string;
  // Event name triggered by internal value change, default change
  changeEvent?: string;
  // Variable name bound to v-model Default value
  valueField?: string;
  // Label name
  label: string | VNode;
  // Auxiliary text
  subLabel?: string;
  // Help text on the right side of the text
  helpMessage?: string | string[] | ((renderCallbackParams: RenderCallbackParams) => string | string[]);
  // BaseHelp component props
  helpComponentProps?: Partial<HelpComponentProps>;
  // Label width, if it is passed, the labelCol and WrapperCol configured by itemProps will be invalid
  labelWidth?: string | number;
  // Disable the adjustment of labelWidth with global settings of formModel, and manually set labelCol and wrapperCol by yourself
  disabledLabelWidth?: boolean;
  // render component
  component: ComponentType;
  // Component parameters
  componentProps?:
    | ((opt: { schema: FormSchema; tableAction: TableActionType; formActionType: FormActionType; formModel: Recordable }) => Recordable)
    | object;
  // Required
  required?: boolean | ((renderCallbackParams: RenderCallbackParams) => boolean);

  suffix?: string | number | ((values: RenderCallbackParams) => string | number);

  // Validation rules
  rules?: Rule[];
  // Check whether the information is added to the label
  rulesMessageJoinLabel?: boolean;

  // Reference formModelItem
  itemProps?: Partial<FormItem>;

  // col configuration outside formModelItem
  colProps?: Partial<ColEx>;

  // 默认值
  defaultValue?: any;
  isAdvanced?: boolean;

  // Matching details components
  span?: number;

  ifShow?: boolean | ((renderCallbackParams: RenderCallbackParams) => boolean);

  show?: boolean | ((renderCallbackParams: RenderCallbackParams) => boolean);

  // Render the content in the form-item tag
  render?: (renderCallbackParams: RenderCallbackParams) => VNode | VNode[] | string;

  // Rendering col content requires outer wrapper form-item
  renderColContent?: (renderCallbackParams: RenderCallbackParams) => VNode | VNode[] | string;

  renderComponentContent?: ((renderCallbackParams: RenderCallbackParams) => any) | VNode | VNode[] | string;

  // Custom slot, in from-item
  slot?: string;

  // Custom slot, similar to renderColContent
  colSlot?: string;

  dynamicDisabled?: boolean | ((renderCallbackParams: RenderCallbackParams) => boolean);

  dynamicRules?: (renderCallbackParams: RenderCallbackParams) => Rule[];
  // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改
  // 设置组件props的key
  dynamicPropskey?: string;
  dynamicPropsVal?: ((renderCallbackParams: RenderCallbackParams) => any);
  // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8377】formSchema props支持动态修改

  // 这个属性自定义的 用于自定义的业务 比如在表单打开的时候修改表单的禁用状态，但是又不能重写componentProps，因为他的内容太多了，所以使用dynamicDisabled和buss实现
  buss?: any;
  
  //label字数控制（label宽度）
  labelLength?: number;
  // update-begin--author:liaozhiyang---date:20240529---for【TV360X-460】basicForm支持v-auth指令(权限控制显隐)
  auth?: string;
  // update-end--author:liaozhiyang---date:20240529---for【TV360X-460】basicForm支持v-auth指令(权限控制显隐)
}
export interface HelpComponentProps {
  maxWidth: string;
  // Whether to display the serial number
  showIndex: boolean;
  // Text list
  text: any;
  // colour
  color: string;
  // font size
  fontSize: string;
  icon: string;
  absolute: boolean;
  // Positioning
  position: any;
}
