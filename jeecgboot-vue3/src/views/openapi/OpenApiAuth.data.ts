import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '授权名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: 'AK',
    align: "center",
    dataIndex: 'ak'
  },
  {
    title: 'SK',
    align: "center",
    dataIndex: 'sk'
  },
  {
    title: '创建人',
    align: "center",
    dataIndex: 'createBy'
  },
  {
    title: '创建时间',
    align: "center",
    dataIndex: 'createTime'
  },
  {
    title: '关联系统用户名',
    align: "center",
    dataIndex: 'createBy',

  },
];

// 高级查询数据
export const superQuerySchema = {
  name: {title: '授权名称',order: 0,view: 'text', type: 'string',},
  ak: {title: 'AK',order: 1,view: 'text', type: 'string',},
  sk: {title: 'SK',order: 2,view: 'text', type: 'string',},
  createBy: {title: '关联系统用户名',order: 3,view: 'text', type: 'string',},
  createTime: {title: '创建时间',order: 4,view: 'datetime', type: 'string',},
  // systemUserId: {title: '关联系统用户名',order: 5,view: 'text', type: 'string',},
};
