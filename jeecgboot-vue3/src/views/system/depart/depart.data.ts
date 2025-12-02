import { FormSchema } from '/@/components/Form';
import { getPositionByDepartId } from "./depart.api";
import { useMessage } from "@/hooks/web/useMessage";
import { BasicColumn } from "@/components/Table";
import {
  getDepartName,
  getDepartPathName,
  getDepartPathNameByOrgCode,
  getMultiDepartPathName
} from '@/utils/common/compUtils';
import { h, ref } from 'vue';

const { createMessage: $message } = useMessage();
//部门名称
const departNamePath = ref<Record<string, string>>({});

// 部门基础表单
export function useBasicFormSchema(treeData) {
  const basicFormSchema: FormSchema[] = [
    {
      field: 'departName',
      label: '机构名称',
      component: 'Input',
      componentProps: {
        placeholder: '请输入机构/部门名称',
      },
      rules: [{ required: true, message: '机构名称不能为空' }],
    },
    {
      field: 'departNameAbbr',
      label: '机构简称',
      component: 'Input',
      componentProps: {
        placeholder: '请输入机构/部门简称',
      }
    },
    {
      field: 'parentId',
      label: '上级部门',
      component: 'TreeSelect',
      componentProps: {
        treeData: [],
        placeholder: '无',
        treeCheckAble: true,
        multiple: true,
        dropdownStyle: { maxHeight: '200px', overflow: 'auto' },
        tagRender: (options) => {
          const { value, label, option } = options;
          if (departNamePath.value[value]) {
            return h(
                'span', {  style: { marginLeft: '10px' } },
                departNamePath.value[value]
            );
          }
          getDepartPathNameByOrgCode('', label, option.id).then((data) => {
            departNamePath.value[value] = data;
          });
        },
      },
    },
    {
      field: 'orgCode',
      label: '机构编码',
      component: 'Input',
      componentProps: {
        placeholder: '请输入机构编码',
      },
    },
    {
      field: 'orgCategory',
      label: '机构类型',
      component: 'RadioButtonGroup',
      componentProps: { options: [] },
    },
    {
      field: 'positionId',
      label: '职务级别',
      component: 'JDictSelectTag',
      componentProps: ({ formModel, formActionType }) => {
        return {
          dictCode: "sys_position,name,id, 1=1 order by post_level asc",
          getPopupContainer: ()=> document.body,
          onChange: (value) => {
            formModel.depPostParentId = "";
            return positionChange(value, formModel, treeData);
          },
        }
      },
      ifShow:({ values })=>{
        return values.orgCategory === '3'
      },
      required: true,
    },
    {
      field: 'depPostParentId',
      label: '上级岗位',
      component: 'TreeSelect',
      ifShow:({ values })=>{
        return values.orgCategory === '3'
      },
      slot: 'depPostParentId',
    },
    {
      field: 'departOrder',
      label: '排序',
      component: 'InputNumber',
      componentProps: {},
    },
    {
      field: 'mobile',
      label: '电话',
      component: 'Input',
      componentProps: {
        placeholder: '请输入电话',
      },
      ifShow:({ values })=>{
        return values.orgCategory !== '3'
      },
    },
    {
      field: 'fax',
      label: '传真',
      component: 'Input',
      componentProps: {
        placeholder: '请输入传真',
      },
      ifShow:({ values })=>{
        return values.orgCategory !== '3'
      },
    },
    {
      field: 'address',
      label: '地址',
      component: 'Input',
      componentProps: {
        placeholder: '请输入地址',
      },
      ifShow:({ values })=>{
        return values.orgCategory !== '3'
      },
    },
    {
      field: 'memo',
      label: '备注',
      component: 'InputTextArea',
      componentProps: {
        placeholder: '请输入备注',
      },
      ifShow:({ values })=>{
        return values.orgCategory !== '3'
      },
    },
    {
      field: 'id',
      label: 'ID',
      component: 'Input',
      show: false,
    },
  ];
  return { basicFormSchema };
}

// 机构类型选项
export const orgCategoryOptions = {
  // 一级部门
  root: [{ value: '1', label: '公司' }],
  // 子级部门
  child: [
    { value: '4', label: '子公司' },
    { value: '2', label: '部门' },
    { value: '3', label: '岗位' },
  ],
  //部门岗位
  childDepartPost: [
    { value: '2', label: '部门' },
    { value: '3', label: '岗位' },
  ],
  //岗位
  childPost: [
    { value: '3', label: '岗位' },
  ]
};

/**
 * 用户列表
 */
export const userColumns: BasicColumn[] = [
  {
    title: '姓名',
    dataIndex: 'realname',
    width: 150,
  },
  {
    title: '手机',
    width: 150,
    dataIndex: 'phone',
    customRender:( { record, text })=>{
      if(record.izHideContact && record.izHideContact === '1'){
        return '/';
      }
      return text;
    }
  },
  {
    title: '主岗位',
    dataIndex: 'mainDepPostId',
    customRender: ({ record, text })=>{
      if(!text){
        return '';
      }
      return getDepartName(getDepartPathName(record.mainDepPostId_dictText,text,false));
    },
    width: 200,
  },
  {
    title: '兼职岗位',
    dataIndex: 'otherDepPostId',
    customRender: ({ record, text })=>{
      if(!text){
        return '';
      }
      return getDepartName(getMultiDepartPathName(record.otherDepPostId_dictText,text));
    },
    width: 200,
  },
];

/**
 * 职位改变事件
 * @param value
 * @param model
 * @param treeData
 */
export function positionChange(value, model, treeData) {
  if(value && model.parentId){
    getPositionByDepartId({ parentId: model.parentId, departId: model.id ? model.id:'', positionId: value }).then((res) =>{
      if(res.success){
        treeData.value = res.result;
      }else{
        treeData.value = [];
        $message.warning(res.message);
      }
    });
  } else {
    treeData.value = [];
  }
}
