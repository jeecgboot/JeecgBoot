import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { getDepartName } from "@/utils/common/compUtils";

export const columns: BasicColumn[] = [
  {
    title: '姓名',
    dataIndex: 'realname',
    width: 100,
  },
  {
    title: '部门',
    dataIndex: 'departName',
    customRender:({ text })=>{
      return getDepartName(text);
    }
  },
  {
    title: '主岗位',
    dataIndex: 'postName',
    customRender:({ text })=>{
      return getDepartName(text);
    }
  },
  {
    title: '兼职岗位',
    dataIndex: 'otherPostName',
    customRender:({ text })=>{
      return getDepartName(text);
    }
  },
/*  {
    title: '职务',
    dataIndex: 'post',
    width: 150,
    slots: { customRender: 'post' },
  },*/
  {
    title: '手机',
    width: 110,
    dataIndex: 'phone',
    customRender:( { record, text })=>{
      if(record.izHideContact && record.izHideContact === '1'){
        return '/';
      }
      return text;
    }
  },
  {
    title: '邮箱',
    width: 180,
    dataIndex: 'email',
    customRender:( { record, text })=>{
      if(record.izHideContact && record.izHideContact === '1'){
        return text?'/':'';
      }
      return text;
    }
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '姓名',
    field: 'realname',
    component: 'Input',
    colProps: { span: 6 },
  },
];
