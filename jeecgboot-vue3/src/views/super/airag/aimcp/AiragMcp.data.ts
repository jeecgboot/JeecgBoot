import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '图标',
    align: "center",
    dataIndex: 'icon'
  },
  {
    title: '名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '描述',
    align: "center",
    dataIndex: 'descr'
  },
  {
    title: 'mcp类型（sse：sse类型；stdio：标准类型）',
    align: "center",
    dataIndex: 'type'
  },
  {
    title: '服务端点（SSE类型为URL，stdio类型为命令）',
    align: "center",
    dataIndex: 'endpoint'
  },
  {
    title: '请求头（sse类型）、环境变量（stdio类型）',
    align: "center",
    dataIndex: 'headers'
  },
  {
    title: '工具列表',
    align: "center",
    dataIndex: 'tools'
  },
  {
    title: '状态（enable=启用、disable=禁用）',
    align: "center",
    dataIndex: 'status'
  },
  {
    title: '是否同步',
    align: "center",
    dataIndex: 'synced'
  },
  {
    title: '元数据',
    align: "center",
    dataIndex: 'metadata'
  },
  {
    title: '租户id',
    align: "center",
    dataIndex: 'tenantId'
  },
];

// 高级查询数据
export const superQuerySchema = {
  icon: {title: '应用图标',order: 0,view: 'text', type: 'string',},
  name: {title: '名称',order: 1,view: 'text', type: 'string',},
  descr: {title: '描述',order: 2,view: 'text', type: 'string',},
  type: {title: 'mcp类型（sse：sse类型；stdio：标准类型）',order: 3,view: 'text', type: 'string',},
  endpoint: {title: '服务端点（SSE类型为URL，stdio类型为命令）',order: 4,view: 'textarea', type: 'string',},
  headers: {title: '请求头（sse类型）、环境变量（stdio类型）',order: 5,view: 'textarea', type: 'string',},
  tools: {title: '工具列表',order: 6,view: 'textarea', type: 'string',},
  status: {title: '状态（enable=启用、disable=禁用）',order: 7,view: 'text', type: 'string',},
  synced: {title: '是否同步',order: 8,view: 'number', type: 'number',},
  metadata: {title: '元数据',order: 9,view: 'textarea', type: 'string',},
  tenantId: {title: '租户id',order: 10,view: 'text', type: 'string',},
};
