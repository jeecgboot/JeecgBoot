import { FormSchema } from '/@/components/Table';
import { render } from "/@/utils/common/renderUtils";
import { getToken } from '/@/utils/auth';

//列表
export const columns = [
  {
    title:'用户账号',
    align:"center",
    dataIndex: 'username',
    customRender: ( {text,record} ) => {
      let token = getToken();
      if(record.token === token) {
        return text + '（我）'
      }
      return text
    },
  },{
    title:'用户姓名',
    align:"center",
    dataIndex: 'realname'
  },{
    title: '头像',
    align: "center",
    width: 120,
    dataIndex: 'avatar',
    customRender: render.renderAvatar,
  },{
    title:'生日',
    align:"center",
    dataIndex: 'birthday'
  },{
    title: '性别',
    align: "center",
    dataIndex: 'sex',
    customRender: ({text}) => {
      return render.renderDict(text, 'sex');
    }
  },{
    title:'手机号',
    align:"center",
    dataIndex: 'phone'
  }
];

//查询区域
export const searchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: '用户账号',
    component: 'Input',
    colProps: { span: 6 },
  }
];
