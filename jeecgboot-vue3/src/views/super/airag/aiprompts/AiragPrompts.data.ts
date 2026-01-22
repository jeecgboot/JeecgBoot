import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {duplicateCheckDelay} from "@/views/system/user/user.api";
export const DESFORM_NAME_MAX_LENGTH = 40;
import {pinyin} from "pinyin-pro";
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '名称',
    align: 'center',
    dataIndex: 'name',
  },
  {
    title: '功能描述',
    align: 'center',
    dataIndex: 'description',
  },
  // {
  //   title: '状态',
  //   align: 'center',
  //   dataIndex: 'status',
  // },
  {
    title: '最近提交人',
    align: 'center',
    dataIndex: 'updateBy',
  },
  {
    title: '最近提交时间',
    align: 'center',
    dataIndex: 'updateTime',
  },
  {
    title: '创建人',
    align: 'center',
    dataIndex: 'createBy',
  },
  {
    title: '创建时间',
    align: 'center',
    dataIndex: 'createTime',
  }
];
//查询数据
export const searchFormSchema: FormSchema[] = [
    {
      label: '名称',
      field: 'name',
      component: 'Input',
    },
];
// 名称最大长度
export const NAME_MAX_LENGTH = 40;
// 编码最大长度
export const CODE_MAX_LENGTH = 50;
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'name',
    component: 'Input',
    componentProps: ({ formModel }) => {
      return {
        placeholder: '例如：SQL转换',
        maxlength: DESFORM_NAME_MAX_LENGTH,
        showCount: true,
        onChange: (e: ChangeEvent) => {
          if(formModel.id){
            return
          }
          let code = pinyin(e.target.value, {
            toneType: 'none',
            type: 'array',
            nonZh: 'consecutive',
          }).join('_');
          code = code.replace(/[^a-zA-Z0-9_\-]/g, '');
          formModel.promptKey = code;
        },
      };
    },
    dynamicRules() {
      return [
        {required: true, message: '请输入提示词名称'},
        {
          max: NAME_MAX_LENGTH,
          message: `名称长度不能超过${NAME_MAX_LENGTH}个字符`,
        },
      ];
    }
  },
  {
    label: '提示词编码',
    field: 'promptKey',
    component: 'Input',
    dynamicRules({ model }) {
      return [
        { required: true, message: '提示词编码' },
        {
          async validator(_, value) {
            if (value?.length > CODE_MAX_LENGTH) {
              throw `编码长度不能超过${CODE_MAX_LENGTH}个字符`;
            }
            const pattern = /^[a-z|A-Z][a-z|A-Z\d_-]*$/;
            if (!pattern.test(value)) {
              throw '编码必须以字母开头，可包含数字、下划线、横杠';
            } else if (/[A-Z]/.test(value)) {
              throw '不支持大写字母';
            } else {
              const res = await duplicateCheckDelay({
                tableName: 'airag_prompts',
                fieldName: 'prompt_key',
                fieldVal: value,
                dataId: model.id,
              }) as any;
              if (!res.success) {
                throw '表单编码已存在！';
              }
            }
          },
        },
      ];
    }
  },
  {
    label: '提示词功能描述',
    field: 'description',
    component: 'InputTextArea',
  },
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];

/**
 * 流程表单调用这个方法获取formSchema
 * @param param
 */
export function getBpmFormSchema(_formData): FormSchema[] {
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
