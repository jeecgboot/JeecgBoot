import { ModalOptionsPartial } from '/@/hooks/web/useMessage';
import { RenderCallbackParams, Rule } from '/@/components/Form';

export interface JPromptProps extends ModalOptionsPartial {
  // 输入框是否必填
  required?: boolean;
  // 校验
  rules?: Rule[];
  // 动态校验
  dynamicRules?: (renderCallbackParams: RenderCallbackParams) => Rule[];
  // 占位字符
  placeholder?: string;
  // 输入框默认值
  defaultValue?: string;
}
